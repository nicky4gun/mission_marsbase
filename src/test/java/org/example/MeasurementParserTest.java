package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MeasurementParserTest {
    @Test
    void rejectsEmptyMeasurement() {
        assertThrows(IllegalArgumentException.class, () -> MeasurementParser.parse(""));
    }

    @Test
    void rejectsWhitespaceOnlyMeasurement() {
        assertThrows(IllegalArgumentException.class, () -> MeasurementParser.parse("   "));
    }

    @Test
    void rejectsMeasurementWithoutSeparator() {
        assertThrows(IllegalArgumentException.class, () -> MeasurementParser.parse("TEMP"));
    }

    @Test
    void rejectsMeasurementWithoutValue() {
        assertThrows(IllegalArgumentException.class, () -> MeasurementParser.parse("TEMP:"));
    }

    @Test
    void rejectsMeasurementWithoutSensorType() {
        assertThrows(IllegalArgumentException.class, () -> MeasurementParser.parse(":20"));
    }

    @Test
    void rejectsUnknownSensorType() {
        assertThrows(IllegalArgumentException.class,
                () -> MeasurementParser.parse("UNKNOWN: 20"));
    }

    @Test
    void rejectsMeasurementWithExtraField() {
        assertThrows(IllegalArgumentException.class,
                () -> MeasurementParser.parse("TEMP: 20: extra"));
    }

    @Test
    void rejectsNullMeasurement() {
        assertThrows(IllegalArgumentException.class, () -> MeasurementParser.parse(null));
    }

    @Test
    void rejectsNonNumericMeasurementValue() {
        assertThrows(NumberFormatException.class,
                () -> MeasurementParser.parse("TEMP: not-a-number"));
    }

    @Test
    void parsesMeasurementWithUnit() {
        SensorMeasurement measurement = MeasurementParser.parse("TEMP: 20.5 °C");

        assertEquals(SensorType.TEMP, measurement.type());
        assertEquals(20.5, measurement.value());
    }

    @Test
    void parsesTemperatureFixtureMeasurement() {
        assertEquals(SensorType.TEMP, MeasurementParser.parse("TEMP: 20").type());
    }

    @Test
    void parsesOxygenFixtureMeasurement() {
        assertEquals(SensorType.O2, MeasurementParser.parse("O2: 21").type());
    }

    @Test
    void parsesPressureFixtureMeasurement() {
        assertEquals(SensorType.PRESSURE, MeasurementParser.parse("PRESSURE: 950").type());
    }

    @Test
    void parsesCarbonDioxideFixtureMeasurement() {
        assertEquals(SensorType.CO2, MeasurementParser.parse("CO2: 1500").type());
    }
}
