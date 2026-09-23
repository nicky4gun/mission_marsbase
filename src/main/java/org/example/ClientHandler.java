package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class ClientHandler implements Runnable {
    private static final MarsLogger LOGGER = new MarsLogger();

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String sensorName = clientSocket.getRemoteSocketAddress().toString();
        try (clientSocket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    SensorMeasurement measurement = MeasurementParser.parse(line);
                    sensorName = measurement.type().name();
                    boolean safe = measurement.isWithinSafeRange();
                    logMeasurement(measurement, safe);

                    if (!safe) {
                        String alarm = formatAlarm(measurement);
                        System.out.println(alarm);
                        writer.println(alarm);
                    }
                } catch (NumberFormatException exception) {
                    HQServer.printError("Invalid measurement value: " + exception.getMessage());
                } catch (IllegalArgumentException exception) {
                    HQServer.printError("Invalid client data: " + exception.getMessage());
                } catch (IOException exception) {
                    HQServer.printError("Failed to write sensor log: " + exception.getMessage());
                }
            }
        } catch (IOException exception) {
            HQServer.printError("Sensor " + sensorName + " mistede forbindelsen.");
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
}
