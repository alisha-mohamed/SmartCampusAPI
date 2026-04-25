
package com.smartcampus.filter;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.logging.Logger;

// This filter runs on every single request and response
// It logs the HTTP method, URI and response status for observability
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    // Logger to print request and response info to the server console
    private static final Logger log = Logger.getLogger(LoggingFilter.class.getName());

    // Runs before every request reaches the resource method
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("INCOMING REQUEST → Method: " + requestContext.getMethod()
                + " | URI: " + requestContext.getUriInfo().getRequestUri());
    }

    // Runs after every response is sent back to the client
    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {
        log.info("OUTGOING RESPONSE → Status: " + responseContext.getStatus());
    }
}
