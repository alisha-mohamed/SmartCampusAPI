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

## API Endpoints

### Discovery

#### `GET /`
Returns API metadata so clients can explore available resources without hardcoded URLs.

**Response:**
```json
{
  "api": "Smart Campus API",
  "version": "v1",
  "description": "RESTful API for managing campus rooms and sensors",
  "contact": "admin@smartcampus.ac.uk",
  "links": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```
---
### Rooms

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/rooms` | List all rooms | 200 |
| GET | `/rooms/{roomId}` | Get a specific room | 200 / 404 |
| POST | `/rooms` | Create a new room | 201 / 409 |
| DELETE | `/rooms/{roomId}` | Delete a room | 204 / 404 / 409 |

#### `GET /rooms`
Returns a list of all registered rooms.

#### `GET /rooms/{roomId}`
Returns a single room by its ID. Returns `404` if the room does not exist.

#### `POST /rooms`
Creates a new room. Returns `409 Conflict` if the ID is already taken.

**Request body:**
```json
{
  "id": "CS-101",
  "name": "Computer Science Lab",
  "capacity": 30
}
```

**Response:**
```json
{
  "id": "CS-101",
  "name": "Computer Science Lab",
  "capacity": 30,
  "sensorIds": []
}
```

---

## Curl Commands

All commands below assume the server is running on `http://localhost:8080`

### Discovery

`GET /api/v1`

Returns API metadata and navigation links to all available resources.

**Response:** `200 OK`
```json
{
  "api": "Smart Campus API",
  "version": "v1",
  "description": "RESTful API for managing campus rooms and sensors",
  "contact": "admin@smartcampus.ac.uk",
  "links": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```

---

### Rooms
`GET /api/v1/rooms`

Returns a list of all rooms currently registered in the system.

**Response:** `200 OK`
```json
[
  { "id": "LIB-301", "name": "Library Quiet Study", "capacity": 50, "sensorIds": ["TEMP-001"] },
  { "id": "CS-101", "name": "Computer Science Lab", "capacity": 30, "sensorIds": [] }
]
```

---

`GET /api/v1/rooms/{roomId}`

Returns a single room by its ID.

**Response:** `200 OK`
```json
{ "id": "LIB-301", "name": "Library Quiet Study", "capacity": 50, "sensorIds": ["TEMP-001"] }
```

**Response:** `404 Not Found`
```json
{ "error": "Room not found" }
```

---

`POST /api/v1/rooms`

Registers a new room. Returns `409` if a room with that ID already exists.

**Request body:**
```json
{ "id": "CS-101", "name": "Computer Science Lab", "capacity": 30 }
```

**Response:** `201 Created`
```json
{ "id": "CS-101", "name": "Computer Science Lab", "capacity": 30, "sensorIds": [] }
```

**Response:** `409 Conflict`
```json
{ "error": "Room with this ID already exists" }
```

---

`DELETE /api/v1/rooms/{roomId}`

Deletes a room by ID. Blocked if the room still has sensors assigned to it.

**Response:** `204 No Content` — room successfully deleted, no body returned.

**Response:** `404 Not Found`
```json
{ "error": "Room not found" }
```

**Response:** `409 Conflict`
```json
{ "error": "Conflict", "message": "Room 'LIB-301' cannot be deleted as it still has sensors assigned." }
```

---

### Sensors

`GET /api/v1/sensors`

Returns all sensors. Filter by type using the optional `?type=` query parameter — case-insensitive.

**Response:** `200 OK`
```json
[
  { "id": "TEMP-001", "type": "Temperature", "status": "ACTIVE", "currentValue": 22.5, "roomId": "LIB-301" },
  { "id": "CO2-001", "type": "CO2", "status": "ACTIVE", "currentValue": 412.0, "roomId": "LIB-301" }
]
```
`GET /api/v1/sensors/{sensorId}`

Returns a single sensor by its ID.

**Response:** `200 OK`
```json
{ "id": "TEMP-001", "type": "Temperature", "status": "ACTIVE", "currentValue": 22.5, "roomId": "LIB-301" }
```

**Response:** `404 Not Found`
```json
{ "error": "Sensor 'TEMP-001' not found." }
```

---
`POST /api/v1/sensors`

Registers a new sensor. The `roomId` must reference an existing room. Status defaults to `ACTIVE` if not provided.

**Request body:**
```json
{ "id": "HUM-001", "type": "Humidity", "status": "ACTIVE", "currentValue": 55.0, "roomId": "LIB-301" }
```

**Response:** `201 Created`
```json
{ "id": "HUM-001", "type": "Humidity", "status": "ACTIVE", "currentValue": 55.0, "roomId": "LIB-301" }
```

**Response:** `409 Conflict`
```json
{ "error": "Sensor 'HUM-001' already exists." }
```

**Response:** `422 Unprocessable Entity`
```json
{ "error": "Unprocessable Entity", "message": "Room 'FAKE-999' does not exist. Cannot register sensor." }
```

---

### Sensor Readings
`GET /api/v1/sensors/{sensorId}/readings`

Returns the full reading history for a sensor. Returns an empty list if no readings exist yet.

**Response:** `200 OK`
```json
[
  { "id": "a3f1c...", "timestamp": 1714123456789, "value": 22.5 },
  { "id": "b9e2d...", "timestamp": 1714123512345, "value": 23.1 }
]
```

**Response:** `404 Not Found`
```json
{ "error": "Sensor 'TEMP-001' not found." }
```

---
`POST /api/v1/sensors/{sensorId}/readings`

Adds a new reading to a sensor. Reading ID and timestamp are auto-generated. Also updates the parent sensor's `currentValue`. Blocked if the sensor is in `MAINTENANCE` status.

**Request body:**
```json
{ "value": 450.5 }
```

**Response:** `201 Created`
```json
{ "id": "a3f1c...", "timestamp": 1714123456789, "value": 450.5 }
```

**Response:** `403 Forbidden`
```json
{ "error": "Forbidden", "message": "Sensor 'TEMP-001' is currently under MAINTENANCE and cannot accept new readings." }
```

**Response:** `404 Not Found`
```json
{ "error": "Sensor 'TEMP-001' not found." }
```

## Report - Question Answers

### Part 1.1 - JAX-RS Resource Class Lifecycle

JAX-RS creates a new instance of every resource class for every incoming HTTP request by default. This is known as per-request scope. In the SmartCampusAPI system, classes such as RoomResource, SensorResource, and SensorReadingResource are all per-request scoped. This indicates that each HTTP request gets its own fresh instance of the relevant class, and once the response is sent it is discarded. Hence, a new instance is instantiated for every incoming request and not treated as a singleton. 

This architectural decision directly impacts how shared data must be managed. Since each resource instance is discarded after a response is sent, any data stored inside the resource class would be lost between API calls, this makes it impossible to maintain state across requests. This is why all resource classes delegate to the shared DataStore.getInstance() singleton, which contains all rooms, sensors, and readings in-memory across requests.

This is where thread safety becomes a concern, as multiple requests can arrive simultaneously and all read from or write to the same maps. For example, two simultaneous POST /rooms requests could both pass the duplicate ID check before either has inserted its entry, resulting in one overwriting the other and causing data loss. To handle this, the DataStore uses ConcurrentHashMap, which handles concurrent reads and writes safely without needing explicit synchronized blocks. Thereby, preventing data loss and corruption under concurrent load.
---
