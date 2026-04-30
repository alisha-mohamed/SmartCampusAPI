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
---

## Report - Question Answers

### Part 1.1 - JAX-RS Resource Class Lifecycle

JAX-RS creates a new instance of every resource class for every incoming HTTP request by default. This is known as per-request scope. In the SmartCampusAPI system, classes such as `RoomResource`, `SensorResource`, and `SensorReadingResource` are all per-request scoped. This indicates that each HTTP request gets its own fresh instance of the relevant class, and once the response is sent it is discarded. Hence, a new instance is instantiated for every incoming request and not treated as a singleton. 

This architectural decision directly impacts how shared data must be managed. Since each resource instance is discarded after a response is sent, any data stored inside the resource class would be lost between API calls, this makes it impossible to maintain state across requests. This is why all resource classes delegate to the shared `DataStore.getInstance()` singleton, which contains all rooms, sensors, and readings in-memory across requests.

This is where thread safety becomes a concern, as multiple requests can arrive simultaneously and all read from or write to the same maps. For example, two simultaneous `POST /rooms` requests could both pass the duplicate ID check before either has inserted its entry, resulting in one overwriting the other and causing data loss. To handle this, the DataStore uses `ConcurrentHashMap`, which handles concurrent reads and writes safely without needing explicit `synchronized` blocks. Thereby, preventing data loss and corruption under concurrent load.

---

### 1.2 — HATEOAS & Hypermedia

HATEOAS (Hypermedia as the Engine of Application State) is the principle that an API response should include links to related resources and available actions, allowing clients to navigate the API without having to know every URL in advance. In the SmartCampusAPI project, the `DiscoveryResource` at `GET /api/v1` returns a JSON object containing a `links` map with the paths to `/api/v1/rooms` and `/api/v1/sensors`. This means that the client immediately knows what resources are available and where to find them, without having to read seperate documentation.

This approach is far better than static documentation that can go out of date as endpoints change. A HATEOAS response is always live and accurate as it is generated by the server itself. 

Key benefits of HATEOAS include: 
- Decoupling and Evolvability - Clients are not tightly linked to ther server's internal structure, allowing developers to update URLs or backend logic without affecting existing clients.
- Self-Discoverability - Clients can understand and navigate the API by following links in responses, eliminating the need for external documentation.
- Reduced Client-Side Complexity - Clients do not need to hardcode URL structures, as they can reply on links provided in responses.
- Simplified Documentation - The API is mostly self-explanatory, as it communicates available actions directly through its responses.

---

### Part 2.1 - Returning IDs vs Full Objects

Returning only IDs:
- Uses less network bandwith because the response payload is smaller, which is useful when dealing with large collections.
- However, the client must make additional requests to retrieve full details for each room, increasing the number of network round trips and overall latency.
- This also increases client-side processing, as the client has to manage and combine multiple responses.

Returning full room objects (Current Implementation):
- Increases the size of the response payload, which may affect performance if the data is large or complex.
- Reduces the need for multiple requests, since all required information is returned in a single call.
- Simplifies client-side processing, as no additional requests or data aggregation are needed.

In this project, `RoomResource.getAllRooms()` returns full room objects using `new ArrayList<>(store.getRooms().values())`. This is an appropriate design choice because each room object is relatively small (containing only `id`, `name`, `capacity`, and `sensorIds`). As a result, returning full objects is the better approach, as it minimises network round trips and reduces client-side complexity while keeping bandwidth usage at an acceptable level.

---

### Part 2.2 - DELETE Idempotency

Yes, the  `DELETE ` operation is idempotent in this implementation. Idempotency means that making the same request multiple times produces the same server state as making it once. The server does not end up in a different state no matter how many times the request is repeated.

In the implementation, if a client sends `DELETE /api/v1/rooms/LAB-101` and the room is successfully deleted, the server removes if from the `DataStore` and returns a `200 OK`. If the same client mistakenly send the exact same request again, the room no longer exists in the `DataStore` so `RoomResource.deleteRoom()` returns `404 Not Found`. Most importantly, the server state has not changes, the room is still gone, no data modification occured, and nothing was duplicated. The only difference is that the response code changed from a `200` to a `400`.

---

### Part 3.1 - @Consumes Annotation Behaviour

In the SmartCampusAPI system the `POST` methods in `RoomResource` and `SensorResource` are annotated with `@Consumes(MediaType.APPLICATION_JSON)`. This specifies that these endpoints only accept requests with a `Content-Type: application/json header`. If a client sends data in a different format, such as `text/plain` or `application/xml`, JAX-RS detects a mismatch between the request’s `Content-Type` and the media types supported by the resource method. As a result, the framework automatically rejects the request and returns a `415 Unsupported Media Type` response.

Consequently, the method is never invoked, the request body is not deserialised, and no interaction with the `DataStore` takes place. This prevents the application from processing incompatible data formats and ensures robustness. Overall, JAX-RS enforces the `@Consumes` constraint through automatic content negotiation, ensuring that only supported media types are processed by the application.

---

### Part 3.2 - @QueryParam vs Path Parameter for Filtering

Path parameters are intended to identify a specific, unique resource (for example, `/sensors/{sensorId}`), whereas query parameters are used for filtering, searching, and refining collections. Using a path such as `/api/v1/sensors/type/CO2` incorrectly treats the filter value (`CO2`) as part of the resource identity rather than as a constraint applied to a collection.

The query parameter approach (for example, `/api/v1/sensors?type=CO2`) is generally considered superior for filtering and searching because it preserves correct REST semantics. The path continues to represent the resource collection (`/sensors`), while query parameters modify the result set without changing the resource being addressed.

Additionally, query parameters provide greater flexibility. Multiple filters can be easily combined (for example, `/sensors?type=CO2&status=active`), and parameters can be optional, allowing the same endpoint to support both filtered and unfiltered requests. This avoids the need to define multiple rigid URL structures for different filter combinations.

Overall, query parameters demonstrate a clean and extensible design that aligns with REST principles, making them a more appropriate choice for filtering and searching collections.

---

### Part 4.1 - Sub-Resource Locator Pattern

The Sub-Resource Locator pattern is a JAX-RS feature that allows a parent resource class to delegate handling of nested paths to a separate child resource class, instead of managing all endpoints within a single controller. In this project, `SensorResource` is responsible for `/sensors` and `/sensors/{sensorId}`, but delegates `/sensors/{sensorId}/readings` to `SensorReadingResource` using a locator method:

```java
@Path("/{sensorId}/readings")
public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
    return new SensorReadingResource(sensorId);
}
```

Jersey detects that the method returns a resource instance rather than a response object, and therefore forwards the request to the appropriate method inside `SensorReadingResource`.

### Why this approach is beneficial:

-  **Separation of concerns** — `SensorResource` handles sensor-level operations, while `SensorReadingResource` manages all reading-related functionality. This ensures each class has a single responsibility.
-  **Improved maintainability** — Prevents `SensorResource` from becoming overly large and complex as nested endpoints increase.
-  **Clean context passing** — The `sensorId` is passed directly through the constructor, avoiding the need for repeated path parsing in the child resource.
-  **Easier testing** — `SensorReadingResource` can be tested independently by instantiating it with a sensor ID, without requiring full HTTP request execution.
-  **Scalability** — Additional nested resources (e.g., `/sensors/{sensorId}/readings/{readingId}`) can be added using further sub-resource locators without modifying existing logic.

---

### Part 5.2 - HTTP 422 vs HTTP 404

- 404 Not Found: The requested URL or endpoint does not exist on the server.
- 422 Unprocessable Entity: The endpoint exists and the request is syntactically valid, but the server cannot process it due to semantic or business rule violations in the request body.

HTTP 404 Not Found indicates a routing issue, meaning the client has accessed an invalid or non-existent endpoint. In contrast, HTTP 422 Unprocessable Entity indicates that the request has reached a valid endpoint, but the data provided within the request is logically incorrect or violates application constraints.

In this scenario, a 422 response is more appropriate when a `roomId` provided in a POST `/api/v1/sensors` request does not exist. The endpoint `/api/v1/sensors` is valid, and the JSON structure is correct, so the issue is not with the request format or URL, but with the meaning of the data being sent.

Overall, 422 is more semantically accurate because it distinguishes between routing errors (404) and data validation or business logic errors, providing clearer feedback to the client.

---

### Part 5.4 - Security Risks of Exposing Stack Traces

Exposing raw Java stack traces in API responses is a significant security risk because they reveal internal implementation details that attackers can exploit.

1. **Dependency and version disclosure**
   Stack traces can reveal library names and versions (e.g., Jersey, Jackson). Attackers can use this information to identify known vulnerabilities (CVEs) and target outdated or insecure dependencies.

2. **Internal system structure exposure**
   They may expose package names, class names, and method names (e.g., `com.smartcampus.store.DataStore`). This gives attackers insight into the application architecture and how different components interact.

3. **Insight into application behaviour and logic flaws**
   Stack traces show execution paths and failure points, helping attackers understand how the system behaves under invalid input. This can assist in crafting inputs that trigger errors or exploit weak validation logic.

In this project, `GlobalExceptionMapper` implements `ExceptionMapper<Throwable>`, ensuring that all unhandled exceptions are centrally managed. While full stack traces are logged on the server for debugging purposes, the API response returns only a generic `500 Internal Server Error` message without exposing any internal details. This prevents information leakage while still allowing developers to investigate issues through server logs.

---

### Part 5.5 - JAX-RS Filters vs Manual Logging

A cross-cutting concern is functionality that applies across multiple parts of an application, such as logging, authentication, or error handling. Logging is a good example because it is required for all or most endpoints.

Manually adding `Logger.info()` statements inside every resource method is inefficient and difficult to maintain. It leads to duplicated code, and if the logging format or behaviour needs to change, every endpoint must be updated individually. This increases the risk of inconsistency and human error across the application.

JAX-RS filters provide a better solution by centralising this logic in one place. A class implementing `ContainerRequestFilter` or `ContainerResponseFilter`, annotated with `@Provider`, is automatically applied to all incoming or outgoing requests by the JAX-RS runtime. This means logging can be handled globally without modifying any resource classes.

This approach improves maintainability, ensures consistency in logging behaviour, and makes the system easier to extend. It also keeps resource classes focused on business logic rather than infrastructure concerns, resulting in a cleaner and more modular design.


