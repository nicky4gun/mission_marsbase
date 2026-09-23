package org.example;

import java.util.Locale;

public final class MeasurementParser {
    private MeasurementParser() {
    }

    public static SensorMeasurement parse(String line) {
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
        double value = Double.parseDouble(valueText);
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Measurement value must be finite: " + valueText);
        }

        return new SensorMeasurement(sensorType, value);
    }
}
