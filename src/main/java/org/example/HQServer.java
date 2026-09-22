package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HQServer {
    private static final int PORT = 5000;
    private static final int WORKER_COUNT = 5;
    private static final MarsLogger LOGGER = new MarsLogger();

    public static void main(String[] args) {
        ExecutorService workerPool = Executors.newFixedThreadPool(WORKER_COUNT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("HQServer started on port " + PORT);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                workerPool.execute(() -> handleClient(clientSocket));
            }
        } catch (IOException exception) {
            System.err.println("HQServer failed: " + exception.getMessage());
        } finally {
            workerPool.shutdown();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (clientSocket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    SensorMeasurement measurement = parseMeasurement(line);
                    boolean safe = measurement.isWithinSafeRange();
                    logMeasurement(measurement, safe);

                    if (!safe) {
                        String alarm = formatAlarm(measurement);
                        System.out.println(alarm);
                        writer.println(alarm);
                    }
                } catch (IllegalArgumentException exception) {
                    System.err.println("Invalid client data: " + exception.getMessage());
                } catch (IOException exception) {
                    System.err.println("Failed to write sensor log: " + exception.getMessage());
                }
            }
        } catch (IOException exception) {
            System.err.println("Client connection failed: " + exception.getMessage());
        }
    }

    private static void logMeasurement(SensorMeasurement measurement, boolean safe) throws IOException {
        String alarmSuffix = safe ? "" : " -> ALARM!";
        LOGGER.log(String.format(
                Locale.ROOT,
                "%s: %s%s",
                measurement.type(),
                measurement.value(),
                alarmSuffix
        ));
    }

    private static String formatAlarm(SensorMeasurement measurement) {
        return String.format(
                "ALARM: [%s] value out of range! (value = %s)",
                measurement.type(),
                measurement.value()
        );
    }

    private static SensorMeasurement parseMeasurement(String line) {
        String[] parts = line.split(":", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid measurement: " + line);
        }

        SensorType sensorType;
        try {
            sensorType = SensorType.valueOf(parts[0].trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown sensor type: " + parts[0].trim(), exception);
        }

        String valueText = parts[1].trim().split("\\s+", 2)[0];
        double value;
        try {
            value = Double.parseDouble(valueText);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid measurement value: " + valueText, exception);
        }

        return new SensorMeasurement(sensorType, value);
    }
}
