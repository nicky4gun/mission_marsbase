package org.example;

import java.util.Locale;

public final class MeasurementParser {
    private MeasurementParser() {
    }

    public static SensorMeasurement parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Measurement must not be blank");
        }

        String[] parts = line.split(":", -1);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new IllegalArgumentException("Invalid measurement: " + line);
        }

        SensorType sensorType;
        try {
            sensorType = SensorType.valueOf(parts[0].trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown sensor type: " + parts[0].trim(), exception);
        }

        String valuePart = parts[1].trim();
        if (valuePart.contains(":")) {
            throw new IllegalArgumentException("Invalid measurement: " + line);
        }

        String valueText = valuePart.split("\\s+", 2)[0];
        double value = Double.parseDouble(valueText);
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Measurement value must be finite: " + valueText);
        }

        return new SensorMeasurement(sensorType, value);
    }
}
