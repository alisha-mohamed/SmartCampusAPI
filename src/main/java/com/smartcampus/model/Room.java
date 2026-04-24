
package com.smartcampus.model;

import java.util.ArrayList;
import java.util.List;

// This class represents a Room in the Smart Campus
public class Room {
    private String id; // Unique ID for the room e.g. "LIB-301"
    private String name; // Human readable name e.g. "Library Quiet Study"
    private int capacity; // Maximum number of people allowed in the room
    private List<String> sensorIds = new ArrayList<>(); // List of sensor IDs that are inside this room

    // Empty constructor needed for JSON conversion
    public Room() {}

    // Constructor to create a room with all details
    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // Getters and Setters
    
    // Returns and sets the ID
    public String getId() {
        return id; 
    }
    public void setId(String id) {
        this.id = id;
    }
    
    //Returns and sets the name
    public String getName() { 
        return name;
    }
    public void setName(String name) { 
        this.name = name; 
    }

    //Returns and sets the capacity
    public int getCapacity() { 
        return capacity; 
    }
    public void setCapacity(int capacity) { 
        this.capacity = capacity;
    }
    
    //Returns and sets the list of sensor IDs
    public List<String> getSensorIds() {
        return sensorIds;
    }
    public void setSensorIds(List<String> sensorIds) { 
        this.sensorIds = sensorIds; 
    }
}