
package com.smartcampus.exception;

// Exception thrown when a POST reading is attempted
// on a sensor that is currently in MAINTENANCE status
public class SensorUnavailableException extends RuntimeException {

    // Stores the sensor ID that caused the exception
    private final String sensorId;

    // Constructor receives a custom message describing why the sensor is unavailable
    public SensorUnavailableException(String message) {
        super(message);
        this.sensorId = message;
    }

    // Returns the sensor ID that triggered this exception
    public String getSensorId() {
        return sensorId;
    }
}
