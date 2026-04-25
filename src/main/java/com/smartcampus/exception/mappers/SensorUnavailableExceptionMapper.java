
package com.smartcampus.exception.mappers;

import com.smartcampus.exception.SensorUnavailableException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

// Handles SensorUnavailableException and returns HTTP 403
@Provider
public class SensorUnavailableExceptionMapper implements ExceptionMapper<SensorUnavailableException> {

    // Runs when a POST reading is attempted on a sensor in MAINTENANCE status
    @Override
    public Response toResponse(SensorUnavailableException e) {

        // Build a JSON error body explaining why the request was forbidden
        Map<String, String> error = new HashMap<>();
        error.put("error", "Forbidden");
        error.put("message", e.getMessage());

        // Return HTTP 403 Forbidden
        return Response.status(403)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
