# Design Blueprint: Delivery Tracking API

## 1. API Endpoints:

### Get Delivery Status by Order ID
*   **Endpoint:** `GET /api/v1/delivery/track/{orderId}`
*   **Description:** Retrieves the current delivery status and estimated delivery time for a given order.
*   **Path Parameters:**
    *   `orderId` (string, required): Unique identifier for the order.
*   **Response (200 OK):**
    ```json
    {
      "orderId": "string",
      "deliveryStatus": "string", // e.g., "Processing", "Shipped", "Out for Delivery", "Delivered", "Cancelled", "Returned"
      "estimatedDeliveryTime": "2025-12-31T10:30:00Z", // ISO 8601 format
      "lastUpdated": "2025-12-31T09:45:00Z", // ISO 8601 format
      "currentLocation": { // Optional, if available from 3PL
        "latitude": 0.0,
        "longitude": 0.0,
        "address": "string"
      }
    }
    ```
*   **Response (404 Not Found):**
    ```json
    {
      "message": "Order not found"
    }
    ```
*   **Response (500 Internal Server Error):**
    ```json
    {
      "message": "Internal server error"
    }
    ```

## 2. Data Model:

### `DeliveryStatus` Entity (Conceptual)
*   `orderId`: String (Primary Key, links to OMS order)
*   `status`: String (Enum: "Processing", "Shipped", "Out for Delivery", "Delivered", "Cancelled", "Returned")
*   `estimatedDeliveryTime`: DateTime
*   `lastUpdated`: DateTime
*   `currentLocation`: Object (Optional, Latitude, Longitude, Address)
*   `trackingProvider`: String (e.g., "FedEx", "UPS", "Internal")
*   `trackingNumber`: String (Provided by 3PL)

### Relationships:
*   One-to-one or one-to-many relationship with existing `Order` entity in OMS (via `orderId`).

## 3. Architectural Components:

*   **Delivery Tracking Service (New Microservice):**
    *   **Responsibility:** Exposes the `/api/v1/delivery/track/{orderId}` endpoint. Orchestrates calls to OMS and 3PL integration services.
    *   **Components:**
        *   **REST Controller:** Handles incoming HTTP requests.
        *   **Service Layer:** Contains business logic, calls domain services.
        *   **Domain Layer:** Core business rules and entities.
        *   **Infrastructure Layer:** Handles external integrations (OMS, 3PL).
*   **OMS Integration Service (Existing/New Component):**
    *   **Responsibility:** Fetches basic order details from the existing Order Management System.
*   **3PL Integration Service (New Component):**
    *   **Responsibility:** Connects to various third-party logistics providers (e.g., FedEx, UPS APIs) to retrieve real-time tracking data. This service will abstract away the complexities of different 3PL APIs.
    *   **Considerations:** May involve polling or webhook mechanisms depending on 3PL capabilities.
*   **Message Queue (e.g., Kafka/RabbitMQ):**
    *   **Purpose:** For asynchronous communication between the Delivery Tracking Service and 3PL Integration Service, especially for real-time updates or handling high volumes of tracking events.

## 4. Technology Stack:

*   **Backend:** Java 17+, Spring Boot 3+ (for Delivery Tracking Service)
*   **Database:** PostgreSQL (for Delivery Tracking Service to store aggregated/cached tracking data if needed, though direct 3PL calls are preferred for real-time)
*   **Build Tool:** Gradle
*   **Containerization:** Docker
*   **Deployment:** Kubernetes (GKE)
*   **API Documentation:** OpenAPI/Swagger

## 5. Suggested Folder Structure (within `src/main/java/com/delivery/`):

```
src/main/java/com/delivery/
├── core/
│   ├── domain/
│   │   ├── model/             // DeliveryStatus, Location, etc.
│   │   └── service/           // Interfaces for domain services
│   └── usecases/              // Application-specific use cases (e.g., TrackDeliveryUseCase)
├── data/
│   ├── db/                    // Repository interfaces and implementations (if using local DB)
│   └── external/              // Interfaces for OMS and 3PL integrations
│       ├── oms/               // OMS client
│       └── threepl/           // 3PL clients (e.g., FedExClient, UpsClient)
└── presenter/
    ├── Application.java
    ├── config/
    ├── rest/
    │   ├── controller/        // DeliveryTrackingController
    │   ├── dto/               // Request/Response DTOs (e.g., DeliveryStatusResponse)
    │   └── exception/         // Custom exceptions and handlers
    └── usecases/              // Concrete implementations of use cases
```