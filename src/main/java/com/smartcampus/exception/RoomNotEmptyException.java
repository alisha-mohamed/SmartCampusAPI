
package com.smartcampus.exception;


// Exception thrown when a room deletion is attempted
// but the room still has sensors assigned to it
public class RoomNotEmptyException extends RuntimeException {

    // Stores the room ID that caused the exception
    private final String roomId;

    // Constructor receives the room ID and passes a message to the parent class
    public RoomNotEmptyException(String roomId) {
        super("Room '" + roomId + "' cannot be deleted because it still has sensors assigned to it.");
        this.roomId = roomId;
    }

    // Returns the room ID that triggered this exception
    public String getRoomId() {
        return roomId;
    }
}
