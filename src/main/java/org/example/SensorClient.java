package org.example;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class SensorClient {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private static final long MEASUREMENT_INTERVAL_SECONDS = 5;

    private final SensorType sensorType;
    private final PrintWriter out;
    private final Random random;
    private Thread sendingThread;

    public SensorClient(SensorType sensorType, PrintWriter out) {
        this(sensorType, out, new Random());
    }

    SensorClient(SensorType sensorType, PrintWriter out, Random random) {
        this.sensorType = Objects.requireNonNull(sensorType, "sensorType must not be null");
        this.out = Objects.requireNonNull(out, "out must not be null");
        this.random = Objects.requireNonNull(random, "random must not be null");
    }

    public synchronized void start() {
        if (sendingThread != null && sendingThread.isAlive()) {
            return;
        }

        sendingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                sendMeasurement();
                try {
                    Thread.sleep(MEASUREMENT_INTERVAL_SECONDS * 1000);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        sendingThread.start();
    }

    public synchronized void stop() {
        if (sendingThread != null) {
            sendingThread.interrupt();
            sendingThread = null;
        }
    }

    private void sendMeasurement() {
        out.println(generateMeasurement());
    }

    public static void main(String[] args) {
        try (BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Angiv sensortype (TEMP, O2, PRESSURE eller CO2): ");
            String input = keyboard.readLine();
            SensorType sensorType = SensorType.valueOf(input.trim().toUpperCase());

            try (Socket socket = new Socket(HOST, PORT);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                SensorClient sensorClient = new SensorClient(sensorType, out);

                Runtime.getRuntime().addShutdownHook(new Thread(sensorClient::stop));
                sensorClient.start();

                try {
                    Thread.currentThread().join();
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    sensorClient.stop();
                }
            }
        } catch (IllegalArgumentException exception) {
            System.err.println("Ukendt sensortype. Brug TEMP, O2, PRESSURE eller CO2.");
        } catch (IOException exception) {
            System.err.println("Could not connect to HQServer on port " + PORT
                    + ": " + exception.getMessage());
        }
    }

    private String generateMeasurement() {
        return switch (sensorType) {
            case TEMP -> String.format(Locale.ROOT, "TEMP: %.1f °C", random.nextDouble(-30, 46));
            case O2 -> String.format(Locale.ROOT, "O2: %.1f %%", random.nextDouble(15, 28));
            case PRESSURE -> String.format(Locale.ROOT, "PRESSURE: %.1f hPa", random.nextDouble(700, 1201));
            case CO2 -> String.format(Locale.ROOT, "CO2: %.0f ppm", random.nextDouble(500, 3001));
        };
    }
}
