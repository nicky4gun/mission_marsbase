package org.example;

import java.util.Random;
import java.io.PrintWriter;
import java.util.Objects;

public class SensorClient {
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

    private String generateMeasurement() {
        return switch (sensorType) {
            case TEMP -> String.format("TEMP: %.1f °C", random.nextDouble(-30, 46));
            case O2 -> String.format("O2: %.1f %%", random.nextDouble(15, 28));
            case PRESSURE -> String.format("PRESSURE: %.1f hPa", random.nextDouble(700, 1201));
            case CO2 -> String.format("CO2: %.0f ppm", random.nextDouble(500, 3001));
        };
    }
}
