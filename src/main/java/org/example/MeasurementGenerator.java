package org.example;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class MeasurementGenerator {
    private final SensorType sensorType;
    private final Random random;

    public MeasurementGenerator(SensorType sensorType) {
        this.sensorType = Objects.requireNonNull(sensorType, "sensorType must not be null");
        this.random = new Random();
    }

    public String generateMeasurement() {
        return switch (sensorType) {
            case TEMP -> String.format(Locale.ROOT, "TEMP: %.1f °C", random.nextDouble(-30, 46));
            case O2 -> String.format(Locale.ROOT, "O2: %.1f %%", random.nextDouble(15, 28));
            case PRESSURE -> String.format(Locale.ROOT, "PRESSURE: %.1f hPa", random.nextDouble(700, 1201));
            case CO2 -> String.format(Locale.ROOT, "CO2: %.0f ppm", random.nextDouble(500, 3001));
        };
    }
}
