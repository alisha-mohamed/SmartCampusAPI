
package com.smartcampus.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;

// Handles all sensor endpoints under /api/v1/sensors
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // Get the single shared instance of DataStore
    private final DataStore store = DataStore.getInstance();

    // GET /api/v1/sensors or GET /api/v1/sensors?type=CO2
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        Collection<Sensor> all = store.getSensors().values();

        // Filter by type if query parameter is provided
        if (type != null && !type.isBlank()) {
            List<Sensor> filtered = all.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
            return Response.ok(filtered).build();
        }

        return Response.ok(all).build();
    }

    // POST /api/v1/sensors - registers a new sensor
    @POST
    public Response createSensor(Sensor sensor) {
        // Sensor ID must be provided
        if (sensor.getId() == null || sensor.getId().isBlank()) {
            return Response.status(400)
                    .entity(errorBody("Sensor ID is required."))
                    .build();
        }

        // Sensor must not already exist
        if (store.getSensors().containsKey(sensor.getId())) {
            return Response.status(409)
                    .entity(errorBody("Sensor '" + sensor.getId() + "' already exists."))
                    .build();
        }

        // The room the sensor belongs to must exist
        if (sensor.getRoomId() == null || !store.getRooms().containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room '" + sensor.getRoomId() + "' does not exist. Cannot register sensor."
            );
        }

        // Default status to ACTIVE if not provided
        if (sensor.getStatus() == null || sensor.getStatus().isBlank()) {
            sensor.setStatus("ACTIVE");
        }

        // Save sensor and link it to its room
        store.getSensors().put(sensor.getId(), sensor);
        store.getReadings().put(sensor.getId(), new ArrayList<>());
        store.getRooms().get(sensor.getRoomId()).getSensorIds().add(sensor.getId());

        return Response.status(201).entity(sensor).build();
    }

    // GET /api/v1/sensors/{sensorId} - get a specific sensor
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(404)
                    .entity(errorBody("Sensor '" + sensorId + "' not found."))
                    .build();
        }

        return Response.ok(sensor).build();
    }

    // Sub-resource locator - delegates to SensorReadingResource
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }

    // Helper method to build a consistent error response body
    private Map<String, String> errorBody(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
