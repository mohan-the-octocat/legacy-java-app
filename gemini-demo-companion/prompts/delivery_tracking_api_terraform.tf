# Terraform Configuration for Delivery Tracking API

## `main.tf`

```terraform
# Configure the Google Cloud provider
provider "google" {
  project = var.gcp_project_id
  region  = var.gcp_region
}

# --- GKE Cluster (assuming existing or creating a basic one for demo) ---
# In a real scenario, you might reference an existing cluster
resource "google_container_cluster" "primary_cluster" {
  name               = "delivery-tracking-gke-cluster"
  location           = var.gcp_region
  initial_node_count = 1
  node_config {
    machine_type = "e2-medium"
    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }
  # Enable Workload Identity for secure access to GCP services
  workload_identity_config {
    identity_namespace = "${var.gcp_project_id}.svc.id.goog"
  }
}

# --- Kubernetes Deployment for Delivery Tracking API ---
resource "kubernetes_deployment" "delivery_tracking_api" {
  metadata {
    name      = "delivery-tracking-api"
    namespace = "default" # Or a dedicated namespace
    labels = {
      app = "delivery-tracking-api"
    }
  }
  spec {
    replicas = 2 # Example: 2 instances for high availability
    selector {
      match_labels = {
        app = "delivery-tracking-api"
      }
    }
    template {
      metadata {
        labels = {
          app = "delivery-tracking-api"
        }
      }
      spec {
        container {
          name  = "delivery-tracking-api"
          image = "gcr.io/${var.gcp_project_id}/delivery-tracking-api:${var.image_tag}"
          port {
            container_port = 8080 # Spring Boot default port
          }
          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "prod"
          }
          # Example: Environment variables for 3PL API keys (use Secret Manager in production)
          env {
            name = "THREEPL_API_KEY"
            value_from {
              secret_key_ref {
                name = "threepl-api-secret"
                key  = "api_key"
              }
            }
          }
          # Liveness and Readiness probes for health checks
          liveness_probe {
            http_get {
              path = "/actuator/health/liveness"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds        = 10
          }
          readiness_probe {
            http_get {
              path = "/actuator/health/readiness"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds        = 10
          }
          resources {
            requests = {
              cpu    = "200m"
              memory = "512Mi"
            }
            limits = {
              cpu    = "500m"
              memory = "1024Mi"
            }
          }
        }
        # Service Account for Workload Identity
        service_account_name = kubernetes_service_account.delivery_tracking_sa.metadata[0].name
      }
    }
  }
}

# --- Kubernetes Service to expose the API ---
resource "kubernetes_service" "delivery_tracking_api_service" {
  metadata {
    name      = "delivery-tracking-api-service"
    namespace = "default"
    labels = {
      app = "delivery-tracking-api"
    }
  }
  spec {
    selector = {
      app = "delivery-tracking-api"
    }
    port {
      port        = 80
      target_port = 8080
      protocol    = "TCP"
    }
    type = "LoadBalancer" # Expose externally via a GCP Load Balancer
  }
}

# --- Kubernetes Service Account for Workload Identity ---
resource "kubernetes_service_account" "delivery_tracking_sa" {
  metadata {
    name      = "delivery-tracking-sa"
    namespace = "default"
    annotations = {
      "iam.gke.io/gcp-service-account" = google_service_account.delivery_tracking_gsa.email
    }
  }
}

# --- GCP Service Account for Workload Identity ---
resource "google_service_account" "delivery_tracking_gsa" {
  account_id   = "delivery-tracking-gsa"
  display_name = "Service Account for Delivery Tracking API"
  project      = var.gcp_project_id
}

# --- IAM Binding for Workload Identity ---
resource "google_project_iam_member" "workload_identity_binding" {
  project = var.gcp_project_id
  role    = "roles/iam.workloadIdentityUser"
  member  = "serviceAccount:${var.gcp_project_id}.svc.id.goog[default/delivery-tracking-sa]"
}

# --- Variables ---
variable "gcp_project_id" {
  description = "The GCP project ID."
  type        = string
}

variable "gcp_region" {
  description = "The GCP region for resources."
  type        = string
  default     = "us-central1"
}

variable "image_tag" {
  description = "Docker image tag for the Delivery Tracking API."
  type        = string
  default     = "latest"
}

output "api_external_ip" {
  description = "The external IP address of the Delivery Tracking API service."
  value       = kubernetes_service.delivery_tracking_api_service.status[0].load_balancer[0].ingress[0].ip
}
```

---

# Cloud Build Configuration for Delivery Tracking API

## `cloudbuild.yaml`

```yaml
steps:
  # Step 1: Build the Docker image
  - name: 'gcr.io/cloud-builders/docker'
    id: Build
    args:
      - 'build'
      - '-t'
      - 'gcr.io/${PROJECT_ID}/delivery-tracking-api:${_IMAGE_TAG}'
      - '.'
    dir: 'legacy-java-app' # Assuming the app is in this directory relative to the Cloud Build context

  # Step 2: Push the Docker image to Google Container Registry
  - name: 'gcr.io/cloud-builders/docker'
    id: Push
    args:
      - 'push'
      - 'gcr.io/${PROJECT_ID}/delivery-tracking-api:${_IMAGE_TAG}'

  # Step 3: Deploy to GKE
  # Ensure gcloud is authenticated to the GKE cluster
  - name: 'gcr.io/cloud-builders/gcloud'
    id: Deploy
    args:
      - 'container'
      - 'clusters'
      - 'get-credentials'
      - 'delivery-tracking-gke-cluster' # Name of your GKE cluster
      - '--region=${_GCP_REGION}'
      - '--project=${PROJECT_ID}'
    entrypoint: 'bash'
    # Use a custom entrypoint to handle potential issues with default gcloud entrypoint
    # and ensure kubectl context is set correctly.
    # This step is primarily to set up kubectl context for the next step.
    # The actual deployment is done via kubectl apply.
    # This step might not be strictly necessary if kubectl is configured to use
    # the default context from get-credentials.
    # For simplicity, we'll assume kubectl can access the cluster after get-credentials.

  - name: 'gcr.io/cloud-builders/kubectl'
    id: Apply Deployment
    args:
      - 'apply'
      - '-f'
      - 'delivery-tracking-api-deployment.yaml' # Assuming this file is generated/present
      - '-n'
      - 'default' # Or your dedicated namespace
    env:
      - 'CLOUDSDK_COMPUTE_REGION=${_GCP_REGION}'
      - 'CLOUDSDK_CONTAINER_CLUSTER=delivery-tracking-gke-cluster'

  - name: 'gcr.io/cloud-builders/kubectl'
    id: Apply Service
    args:
      - 'apply'
      - '-f'
      - 'delivery-tracking-api-service.yaml' # Assuming this file is generated/present
      - '-n'
      - 'default' # Or your dedicated namespace
    env:
      - 'CLOUDSDK_COMPUTE_REGION=${_GCP_REGION}'
      - 'CLOUDSDK_CONTAINER_CLUSTER=delivery-tracking-gke-cluster'

# Substitutions for dynamic values
substitutions:
  _IMAGE_TAG: 'latest' # Default image tag, can be overridden
  _GCP_REGION: 'us-central1' # Default GCP region, can be overridden

images:
  - 'gcr.io/${PROJECT_ID}/delivery-tracking-api:${_IMAGE_TAG}'
```
