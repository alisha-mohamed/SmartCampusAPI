
package com.smartcampus.exception.mappers;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

// Handles LinkedResourceNotFoundException and returns HTTP 422
@Provider
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    // Runs when a sensor is created with a roomId that does not exist
    @Override
    public Response toResponse(LinkedResourceNotFoundException e) {

        // Build a JSON error body explaining the issue
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unprocessable Entity");
        error.put("message", e.getMessage());

        // Return HTTP 422 - the request was valid but the referenced resource was not found
        return Response.status(422)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
