package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensorMeasurementTest {
    @Test
    void acceptsTemperatureAtLowerSafeBoundary() {
        assertTrue(new SensorMeasurement(SensorType.TEMP, -15.0).isWithinSafeRange());
    }

    @Test
    void acceptsOxygenAtLowerSafeBoundary() {
        assertTrue(new SensorMeasurement(SensorType.O2, 19.0).isWithinSafeRange());
    }

    @Test
    void acceptsPressureAtLowerSafeBoundary() {
        assertTrue(new SensorMeasurement(SensorType.PRESSURE, 800.0).isWithinSafeRange());
    }

    @Test
    void acceptsTemperatureAtUpperSafeBoundary() {
        assertTrue(new SensorMeasurement(SensorType.TEMP, 35.0).isWithinSafeRange());
    }

    @Test
    void acceptsOxygenAtUpperSafeBoundary() {
        assertTrue(new SensorMeasurement(SensorType.O2, 23.0).isWithinSafeRange());
    }

    @Test
    void acceptsPressureAtUpperSafeBoundary() {
        assertTrue(new SensorMeasurement(SensorType.PRESSURE, 1100.0).isWithinSafeRange());
    }

    @Test
    void acceptsCarbonDioxideAtUpperSafeBoundary() {
        assertTrue(new SensorMeasurement(SensorType.CO2, 2000.0).isWithinSafeRange());
    }

    @Test
    void rejectsTemperatureBelowLowerSafeBoundary() {
        assertFalse(new SensorMeasurement(SensorType.TEMP, -15.1).isWithinSafeRange());
    }

    @Test
    void rejectsOxygenBelowLowerSafeBoundary() {
        assertFalse(new SensorMeasurement(SensorType.O2, 18.9).isWithinSafeRange());
    }

    @Test
    void rejectsPressureBelowLowerSafeBoundary() {
        assertFalse(new SensorMeasurement(SensorType.PRESSURE, 799.9).isWithinSafeRange());
    }

    @Test
    void rejectsTemperatureAboveUpperSafeBoundary() {
        assertFalse(new SensorMeasurement(SensorType.TEMP, 35.1).isWithinSafeRange());
    }

    @Test
    void rejectsOxygenAboveUpperSafeBoundary() {
        assertFalse(new SensorMeasurement(SensorType.O2, 23.1).isWithinSafeRange());
    }

    @Test
    void rejectsPressureAboveUpperSafeBoundary() {
        assertFalse(new SensorMeasurement(SensorType.PRESSURE, 1100.1).isWithinSafeRange());
    }

    @Test
    void rejectsCarbonDioxideAboveUpperSafeBoundary() {
        assertFalse(new SensorMeasurement(SensorType.CO2, 2000.1).isWithinSafeRange());
    }

    @Test
    void acceptsTemperatureInsideSafeRange() {
        assertTrue(new SensorMeasurement(SensorType.TEMP, 20.0).isWithinSafeRange());
    }

    @Test
    void acceptsOxygenInsideSafeRange() {
        assertTrue(new SensorMeasurement(SensorType.O2, 21.0).isWithinSafeRange());
    }

    @Test
    void acceptsPressureInsideSafeRange() {
        assertTrue(new SensorMeasurement(SensorType.PRESSURE, 950.0).isWithinSafeRange());
    }

    @Test
    void acceptsCarbonDioxideInsideSafeRange() {
        assertTrue(new SensorMeasurement(SensorType.CO2, 1500.0).isWithinSafeRange());
    }
}
