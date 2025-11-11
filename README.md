[![Build Status](https://travis-ci.org/eliostvs/clean-architecture-delivery-example.svg?branch=master)](https://travis-ci.org/eliostvs/clean-architecture-delivery-example)

# Clean Architecture Example

## Description

The architecture of the project follows the principles of Clean Architecture. It is a simple food delivery app. One can list stores, cousines, products and create food orders. JWT it is used for authentication.

## Running

`./gradlew bootRun`

## Architecture

The project consists of 3 packages: *core*, *data* and *presenter*.

### *core* package

This module contains the domain entities and use cases.
This module contains the business rules that are essential for our application.
In this module, gateways for the repositories are also being defined.
There are no dependencies to frameworks and/or libraries and could be extracted to its own module.

### *data* package

### *presenter* package

## Delivery Tracking API

This API provides functionality to track the delivery status of customer orders.

### Endpoint

`GET /api/v1/delivery/track/{orderId}`

Retrieves the current delivery status and estimated delivery time for a given order.

**Path Parameters:**

*   `orderId` (string, required): Unique identifier for the order.

**Example Response (200 OK):**

```json
{
  "orderId": "ORDER123",
  "deliveryStatus": "Shipped",
  "estimatedDeliveryTime": "2025-12-31T10:30:00Z",
  "lastUpdated": "2025-12-31T09:45:00Z",
  "currentLocation": {
    "latitude": 34.0522,
    "longitude": -118.2437,
    "address": "Los Angeles, CA"
  }
}
```

