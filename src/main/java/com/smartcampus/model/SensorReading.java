
package com.smartcampus.model;

import java.util.UUID;

// This class represents a single reading recorded by a sensor
public class SensorReading {

    // Unique ID for this reading - automatically generated
    private String id;

    // The time this reading was recorded (in milliseconds since 1970)
    private long timestamp;

    // The actual value measured by the sensor e.g. 22.5 degrees
    private double value;

    // Empty constructor needed for JSON conversion
    public SensorReading() {}

    // Constructor that automatically sets ID and timestamp when a new reading is created
    public SensorReading(double value) {
        this.id = UUID.randomUUID().toString(); // generates a unique ID
        this.timestamp = System.currentTimeMillis(); // records current time
        this.value = value;
    }

    // Returns the unique ID of this reading
    public String getId() { 
        return id; 
    }
    // Sets the unique ID of this reading
    public void setId(String id) { 
        this.id = id;
    }

    // Returns the time this reading was recorded
    public long getTimestamp() { 
        return timestamp; 
    }
    // Sets the timestamp of this reading
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Returns the value measured by the sensor
    public double getValue() { 
        return value;
    }
    // Sets the value measured by the sensor
    public void setValue(double value) { 
        this.value = value;
    }
}
