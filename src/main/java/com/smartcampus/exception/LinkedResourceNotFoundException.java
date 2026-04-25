
package com.smartcampus.exception;

// Exception thrown when a sensor is created with a roomId
// that does not exist in the system
public class LinkedResourceNotFoundException extends RuntimeException {

    // Stores the resource ID that was not found
    private final String resourceId;

    // Constructor receives a custom message describing what was not found
    public LinkedResourceNotFoundException(String message) {
        super(message);
        this.resourceId = message;
    }

    // Returns the resource ID that triggered this exception
    public String getResourceId() {
        return resourceId;
    }
}
