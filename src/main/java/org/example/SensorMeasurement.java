package org.example;

public record SensorMeasurement(SensorType type, double value) {
    public boolean isWithinSafeRange() {
        return switch (type) {
            case TEMP -> value >= -15 && value <= 35;
            case O2 -> value >= 19 && value <= 23;
            case PRESSURE -> value >= 800 && value <= 1100;
            case CO2 -> value <= 2000;
        };
    }
}
