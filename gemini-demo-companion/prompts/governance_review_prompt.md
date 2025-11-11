As a Governance Agent, your task is to review the work performed in the previous development and deployment tasks against the established enterprise guardrails and customization guidelines.

Specifically, you need to:
1.  **Validate Adherence to Guardrails:** Assess whether the implemented Delivery Tracking API, its tests, documentation, and proposed deployment configurations (Terraform, Cloud Build) comply with the policies outlined in `guardrails.md`.
2.  **Identify Customization Opportunities/Adherence:** Review if any customization guidelines from `customization_guidelines.md` were implicitly followed or if there are explicit opportunities for GCA customization to further enhance compliance or efficiency.
3.  **Generate a Guardrail Validation Report:** Provide a summary of compliance, identified deviations, and recommendations for improvement.

Use the following documents as your input:
*   **Enterprise Guardrails:**
    {{ENTERPRISE_GUARDRAILS_CONTENT}}
*   **Customization Guidelines:**
    {{CUSTOMIZATION_GUIDELINES_CONTENT}}
*   **Delivery Tracking API PRD:**
    {{DELIVERY_TRACKING_API_PRD_CONTENT}}
*   **Delivery Tracking API Design Blueprint:**
    {{DELIVERY_TRACKING_API_DESIGN_BLUEPRINT_CONTENT}}
*   **Delivery Tracking API Code Review:**
    {{DELIVERY_TRACKING_API_CODE_REVIEW_CONTENT}}
*   **Cousine Repository Modernization Report:**
    {{COUSINE_REPOSITORY_MODERNIZATION_REPORT_CONTENT}}
*   **Delivery Tracking API Terraform Configuration:**
    {{DELIVERY_TRACKING_API_TERRAFORM_CONTENT}}
*   **Delivery Tracking API Cloud Build Configuration:**
    {{DELIVERY_TRACKING_API_CLOUDBUILD_CONTENT}}
