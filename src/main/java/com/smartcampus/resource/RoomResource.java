
package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Map;

// Handles all room-related API endpoints under /api/v1/rooms
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // Access the shared in-memory data store
    private final DataStore store = DataStore.getInstance();

    
    // GET /api/v1/rooms - returns list of all rooms
    @GET
    public Response getAllRooms() {
        return Response.ok(new ArrayList<>(store.getRooms().values())).build();
    }

    
    // POST /api/v1/rooms - creates a new room
    @POST
    public Response createRoom(Room room) {
        // Validate that room ID is provided
        if (room.getId() == null || room.getId().isBlank()) {
            return Response.status(400)
                    .entity(Map.of("error", "Room ID is required"))
                    .build();
        }
        // Check if room already exists
        if (store.getRooms().containsKey(room.getId())) {
            return Response.status(409)
                    .entity(Map.of("error", "Room with this ID already exists"))
                    .build();
        }
        // Save the new room
        store.getRooms().put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    // GET /api/v1/rooms/{roomId} - returns a specific room by ID
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);
        // Return 404 if room not found
        if (room == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }
        return Response.ok(room).build();
    }

    // DELETE /api/v1/rooms/{roomId} - deletes a room only if it has no sensors
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);
        // Return 404 if room does not exist
        if (room == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }
        // Block deletion if room still has sensors assigned
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }
        store.getRooms().remove(roomId);
        return Response.noContent().build();
    }
}
