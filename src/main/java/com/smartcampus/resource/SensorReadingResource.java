
package com.smartcampus.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;

// This class handles all reading-related endpoints for a specific sensor
// It is a sub-resource accessed via /api/v1/sensors/{sensorId}/readings
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    // Stores the sensor ID passed from the parent SensorResource
    private final String sensorId;

    // Access the shared in-memory DataStore
    private final DataStore store = DataStore.getInstance();

    // Constructor receives sensorId from the sub-resource locator
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /api/v1/sensors/{sensorId}/readings - fetch all readings for a sensor
    @GET
    public Response getReadings() {
        // Check if sensor exists
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(404)
                    .entity(errorBody("Sensor '" + sensorId + "' not found."))
                    .build();
        }

        // Return reading history, or empty list if none exist
        List<SensorReading> history = store.getReadings()
                .getOrDefault(sensorId, List.of());

        return Response.ok(history).build();
    }

    // POST /api/v1/sensors/{sensorId}/readings - add a new reading for a sensor
    @POST
    public Response addReading(SensorReading reading) {
        // Check if sensor exists
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(404)
                    .entity(errorBody("Sensor '" + sensorId + "' not found."))
                    .build();
        }

        // Block readings if sensor is under MAINTENANCE
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is currently under MAINTENANCE and cannot accept new readings."
            );
        }

        // Auto-generate reading ID and timestamp if not provided
        if (reading.getId() == null || reading.getId().isBlank()) {
            reading = new SensorReading(reading.getValue());
        }

        // Save the reading to the sensor's reading list
        store.getReadings()
             .computeIfAbsent(sensorId, k -> new java.util.ArrayList<>())
             .add(reading);

        // Update the sensor's current value with the latest reading
        sensor.setCurrentValue(reading.getValue());

        return Response.status(201).entity(reading).build();
    }

    // Helper method to build consistent error response bodies
    private Map<String, String> errorBody(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
