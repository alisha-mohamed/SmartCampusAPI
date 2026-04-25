
package com.smartcampus.exception.mappers;

import com.smartcampus.exception.RoomNotEmptyException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

// Tells JAX-RS to use this class as an exception handler
@Provider
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {

    // This method runs whenever a RoomNotEmptyException is thrown
    // It converts the exception into a proper HTTP response
    @Override
    public Response toResponse(RoomNotEmptyException e) {

        // Build a JSON error body explaining the conflict
        Map<String, String> error = new HashMap<>();
        error.put("error", "Conflict");
        error.put("message", e.getMessage());

        // Return HTTP 409 Conflict with JSON error body
        return Response.status(409)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
