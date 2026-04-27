# SmartCampusAPI
A fully RESTful API built using **JAX-RS (Jersey 2.41)** and **Apache Tomcat 9**, designed to manage Rooms and Sensors across a Smart Campus infrastructure. This API follows REST architectural principles including resource-based URIs, proper HTTP status codes, exception mapping, and request/response logging.

---

## API Overview

The Smart Campus API provides a seamless interface for campus facilities managers and automated building systems to interact with campus data. It supports full CRUD operations on three core resources:

- **Rooms** – Register, retrieve, and decommission physical campus spaces. Each room tracks its name, capacity, and the sensors deployed within it.
- **Sensors** – Register and monitor sensor devices installed in rooms (e.g. Temperature, CO2, Occupancy). Sensors can be filtered by type and linked to specific rooms.
- **Sensor Readings** – Log and retrieve historical measurement data recorded by each sensor. Each new reading automatically updates the sensor's current value.
- **Error Handling** – The API is fully leak-proof. All errors return structured JSON responses with appropriate HTTP status codes (400, 403, 404, 409, 422, 500). Raw Java stack traces are never exposed to clients.

**Base URL:** `http://localhost:8080/SmartCampusAPI/api/v1`

---

## Technology Stack

![Java](https://img.shields.io/badge/Java-11-orange?style=for-the-badge&logo=java)
![Jersey](https://img.shields.io/badge/Jersey-2.41-blue?style=for-the-badge)
![Tomcat](https://img.shields.io/badge/Tomcat-9-yellow?style=for-the-badge&logo=apachetomcat)
![Maven](https://img.shields.io/badge/Maven-3.6+-red?style=for-the-badge&logo=apachemaven)
![Jackson](https://img.shields.io/badge/Jackson-JSON-green?style=for-the-badge)
![JAX-RS](https://img.shields.io/badge/JAX--RS-REST%20API-purple?style=for-the-badge)

| Technology | Purpose |
|------------|---------|
| Java 11 | Core programming language |
| JAX-RS (Jersey 2.41) | Framework for building RESTful endpoints using annotations such as @GET, @POST, @Path |
| Jackson | Handles automatic conversion between Java objects and JSON format |
| Apache Tomcat 9 | External servlet container that hosts and runs the web application |
| Apache Maven | Manages project dependencies and handles the build and packaging process |
| ConcurrentHashMap | Thread-safe in-memory data structure used as the application's database |

---

## Architecture

The API follows a layered architecture pattern where each layer has a specific responsibility
```
HTTP Request
↓
[Apache Tomcat 9]          ← Servlet container, hosts and deploys the application
↓
[JAX-RS / Jersey Router]   ← Matches URL + HTTP method to correct resource class
↓
[Resource Layer]           ← Business logic (RoomResource, SensorResource, etc.)
↓
[Model Layer]              ← Plain Java objects: Room, Sensor, SensorReading
↓
[DataStore (Singleton)]    ← In-memory storage using ConcurrentHashMap
↑
[Exception Mappers]        ← Intercept thrown exceptions → return JSON error responses
↑
[Logging Filter]           ← Logs every request and response automatically
```

### Key Design Patterns

1. **Singleton Pattern** – `DataStore` is instantiated once and shared across all resource classes, ensuring consistent data access throughout the application lifecycle
2. **Sub-Resource Locator Pattern** – `SensorResource` delegates reading-related requests to `SensorReadingResource`, promoting separation of concerns and keeping resource classes focused
3. **Exception Mapper Pattern** – Custom `ExceptionMapper` implementations intercept specific exceptions and convert them into structured HTTP responses, ensuring the API never exposes raw stack traces
4. **Filter Pattern** – `LoggingFilter` implements both `ContainerRequestFilter` and `ContainerResponseFilter` to apply logging as a cross-cutting concern across all endpoints without modifying business logic

---
## How to Build and Run

### Prerequisites
- Java JDK 11 or higher
- Apache Maven 3.6+
- Apache Tomcat 9
- NetBeans IDE (recommended)

### Step-by-Step Instructions

**Step 01 - Clone the repository:**
```
git clone https://github.com/alisha-mohamed/SmartCampusAPI.git
cd SmartCampusAPI
```
**Step 02 - Open in Apache NetBeans:**
- Launch Apache Netbeans
- Click File → Open Project
- Select the cloned folder

**Step 03 - Add Tomcat Server (if not already added):**
- Click **Tools** → **Servers** → **Add Server**
- Select **Apache Tomcat or TomEE** and click Next
- Browse to your Tomcat 9 installation directory
- Set your credentials and click Finish

**Step 04 - Build the project:**
- Right-click **SmartCampusAPI** in the Projects panel
- Select **Clean and Build**
- Wait for the process to complete
- Confirm `BUILD SUCCESS` in the output panel at the bottom

**Step 05 - Run the project:**
- Right-click **SmartCampusAPI** in the Projects panel
- Select **Run**
- Tomcat will start automatically and deploy the application
- A browser window may open showing the default page

**Step 06 - Access the API:**
- Open your browser or Postman and navigate to:
```
http://localhost:8080/SmartCampusAPI/api/v1
```

---

## Curl Commands

All commands below assume the server is running on `http://localhost:8080`

### Discovery
```bash
# Check the API is running
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1
```

### Rooms
```bash
# List all rooms (includes pre-seeded LIB-301 and LAB-101)
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms

# Get a specific room
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301

# Create a new room
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"HALL-201\",\"name\":\"Main Hall\",\"capacity\":100}"

# Delete a room (only works if it has no sensors)
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/HALL-201

# Attempt to delete a room that has sensors → expect 409 Conflict
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301
```

### Sensors
```bash
# List all sensors
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors

# Filter sensors by type (case-insensitive)
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2"
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=temperature"

# Get a specific sensor
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001

# Register a new sensor in an existing room
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"HUM-001\",\"type\":\"Humidity\",\"status\":\"ACTIVE\",\"currentValue\":60.0,\"roomId\":\"LIB-301\"}"

# Attempt to register sensor in a non-existent room → expect 422
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"HUM-002\",\"type\":\"Humidity\",\"status\":\"ACTIVE\",\"currentValue\":55.0,\"roomId\":\"FAKE-999\"}"
```

### Sensor Readings
```bash
# Get all readings for a sensor
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings

# Add a new reading to a sensor
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":25.3}"

# Attempt to add reading to a MAINTENANCE sensor → expect 403
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":450.5}"
```

---
