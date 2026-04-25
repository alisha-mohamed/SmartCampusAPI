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

**JAX-RS / Jersey** → Routes incoming HTTP requests to the correct resource class via `@Path` annotations

↓

**Resource Layer** → Handles business logic (`DiscoveryResource`, `RoomResource`, `SensorResource`, `SensorReadingResource`)

↓

**Model Layer** → Defines core data entities (`Room`, `Sensor`, `SensorReading`)

↓

**Data Layer** → `DataStore` singleton manages all in-memory data using thread-safe `ConcurrentHashMap`

**Cross-Cutting Concerns** (apply across all layers):
- **Logging Filter** – Logs every incoming request method/URI and outgoing response status code
- **Exception Mappers** – Intercept exceptions and return structured JSON error responses (403, 404, 409, 422, 500)

---

