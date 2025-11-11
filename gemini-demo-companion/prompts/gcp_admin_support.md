As a GCP Admin, your task is to generate the necessary Infrastructure-as-Code (IaC) configurations for deploying the "Delivery Tracking API" on Google Cloud Platform (GCP). The deployment should be secure, scalable, and follow GCP best practices.

Specifically, you need to generate:
1.  **Terraform Configuration (`main.tf`):**
    *   A Google Kubernetes Engine (GKE) cluster (if not already existing, or assume an existing one).
    *   A Kubernetes Deployment for the Delivery Tracking API application.
    *   A Kubernetes Service to expose the API internally or externally (e.g., LoadBalancer).
    *   Any necessary IAM roles and service accounts for the application to interact with other GCP services (e.g., Pub/Sub for message queues, Cloud SQL for database if applicable).
2.  **Cloud Build Configuration (`cloudbuild.yaml`):**
    *   A pipeline that builds the Docker image of the Delivery Tracking API.
    *   Pushes the Docker image to Google Container Registry (GCR) or Artifact Registry.
    *   Deploys the application to the GKE cluster using the generated Kubernetes manifests.

Assume the application is a Spring Boot application packaged as a Docker image.

Use the following context about the Delivery Tracking API:
{{DELIVERY_TRACKING_API_CONTEXT}}