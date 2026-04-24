
package com.smartcampus.model;


// This class represents a Sensor installed in a Room
public class Sensor {

    // Unique ID for the sensor e.g. "TEMP-001"
    private String id;

    // Type of sensor e.g. "Temperature", "CO2", "Occupancy"
    private String type;

    // Current status of the sensor: "ACTIVE", "MAINTENANCE", or "OFFLINE"
    private String status;

    // The latest value recorded by the sensor
    private double currentValue;

    // The ID of the room this sensor belongs to
    private String roomId;

    // Empty constructor needed for JSON conversion
    public Sensor() {}

    // Constructor to create a sensor with all details
    public Sensor(String id, String type, String status, double currentValue, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
        this.roomId = roomId;
    }

    // Returns the sensor's unique ID
    public String getId() { 
        return id;
    }
    // Sets the sensor's unique ID
    public void setId(String id) { 
        this.id = id; 
    }

    // Returns the type of sensor e.g. Temperature, CO2
    public String getType() { 
        return type; 
    }
    // Sets the type of sensor
    public void setType(String type) { 
        this.type = type; 
    }

    // Returns the current status of the sensor
    public String getStatus() {
        return status;
    }
    // Sets the status of the sensor e.g. ACTIVE, MAINTENANCE, OFFLINE
    public void setStatus(String status) {
        this.status = status;
    }

    // Returns the most recent value recorded by the sensor
    public double getCurrentValue() { 
        return currentValue;
    }
    // Updates the sensor's current value when a new reading comes in
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    // Returns the ID of the room this sensor is installed in
    public String getRoomId() { 
        return roomId; 
    }
    // Sets which room this sensor belongs to
    public void setRoomId(String roomId) { 
        this.roomId = roomId; 
    }
}
