package org.example;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;
import java.util.Objects;

public class SensorClient {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;
    private static final long MEASUREMENT_INTERVAL_SECONDS = 5;

    private final MeasurementGenerator generator;

    private final PrintWriter out;
    private final BufferedReader in;
    private Thread sendingThread;
    private Thread receivingThread;

    SensorClient(SensorType sensorType, PrintWriter out, BufferedReader in) {
        this.generator = new MeasurementGenerator(sensorType);
        this.out = Objects.requireNonNull(out, "out must not be null");
        this.in = in;
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

        if (in != null && (receivingThread == null || !receivingThread.isAlive())) {
            receivingThread = new Thread(this::receiveAlarms);
            receivingThread.start();
        }
    }

    public synchronized void stop() {
        if (sendingThread != null) {
            sendingThread.interrupt();
            sendingThread = null;
        }
        if (receivingThread != null) {
            receivingThread.interrupt();
            receivingThread = null;
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException exception) {
                System.err.println("[ERROR] Kunne ikke lukke forbindelsen til HQServer: "
                        + exception.getMessage());
            }
        }
    }

    private void sendMeasurement() {
        out.println(generator.generateMeasurement());
    }

    private void receiveAlarms() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException exception) {
            if (!Thread.currentThread().isInterrupted()) {
                System.err.println("[ERROR] Forbindelsen til HQServer blev afbrudt: "
                        + exception.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try (BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Angiv sensortype (TEMP, O2, PRESSURE eller CO2): ");
            String input = keyboard.readLine();
            SensorType sensorType = parseSensorType(input);

            try (Socket socket = new Socket(HOST, PORT);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                SensorClient sensorClient = new SensorClient(sensorType, out, in);
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
            System.err.println("[ERROR] Kunne ikke oprette forbindelse til HQServer på port " + PORT
                    + ": " + exception.getMessage());
        }
    }

    private static SensorType parseSensorType(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Sensortype mangler.");
        }

        try {
            return SensorType.valueOf(input.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Ukendt sensortype: " + input.trim(), exception);
        }
    }
}
