
package com.smartcampus.exception.mappers;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// Global catch-all exception handler - catches any unexpected errors
// Prevents raw Java stack traces from being exposed to API clients
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    // Logger to record unexpected errors on the server side
    private static final Logger log = Logger.getLogger(GlobalExceptionMapper.class.getName());

    // Runs whenever any unhandled exception occurs in the application
    @Override
    public Response toResponse(Throwable e) {

        // Log the full error details on the server for debugging
        log.log(Level.SEVERE, "Unexpected error occurred", e);

        // Build a generic error body - never expose internal details to clients
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred. Please try again later.");

        // Return HTTP 500 Internal Server Error
        return Response.status(500)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
