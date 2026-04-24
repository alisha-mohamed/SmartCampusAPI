
package com.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.LinkedHashMap;
import java.util.Map;

// Root discovery endpoint - provides API metadata and available resource links
@Path("/")
public class DiscoveryResource {

    // GET /api/v1 - returns API version, contact info and navigation links
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {

        // Build response body with API metadata
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("api", "Smart Campus API");
        info.put("version", "v1");
        info.put("description", "RESTful API for managing campus rooms and sensors");
        info.put("contact", "admin@smartcampus.ac.uk");

        // HATEOAS links - allows clients to navigate available resources
        Map<String, String> links = new LinkedHashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        info.put("links", links);

        //Send back the info with a 200 OK status
        return Response.ok(info).build();
    }
}
