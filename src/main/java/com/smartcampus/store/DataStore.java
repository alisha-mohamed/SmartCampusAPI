
package com.smartcampus.store;


import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// This class stores all our data in memory (no database needed)
public class DataStore {
    
    // Only one DataStore exists in the whole app
    private static final DataStore INSTANCE = new DataStore();
    
    // All rooms stored here
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    
    // All sensors stored here
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    
    // All sensor readings stored here (each sensor has its own list)
    private final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    // Constructor runs once when the app starts - adds some sample data
    private DataStore() {
        
        // Create two sample rooms
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Lab", 30);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);

        // Create two sample sensors
        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "ACTIVE", 400.0, "LAB-101");
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);

        // Assign each sensor to its room
        r1.getSensorIds().add(s1.getId());
        r2.getSensorIds().add(s2.getId());

        // Give each sensor an empty list to store future readings
        readings.put(s1.getId(), new ArrayList<>());
        readings.put(s2.getId(), new ArrayList<>());
    }

    // Get the one and only DataStore instance
    public static DataStore getInstance() { 
        return INSTANCE; 
    }
    
    // Get all rooms
    public Map<String, Room> getRooms() {
        return rooms;
    }
    
    // Get all sensors
    public Map<String, Sensor> getSensors() {
        return sensors;
    }
    
    // Get all readings
    public Map<String, List<SensorReading>> getReadings() {
        return readings; 
    }
}