As a Legacy Modernizer, your task is to refactor the provided Java code to adhere to modern best practices, clean architecture principles, and improve maintainability.

Specifically, you need to:
1.  **Identify Anti-patterns:** Pinpoint any outdated patterns, tightly coupled dependencies, or business logic residing in the wrong layer (e.g., controller).
2.  **Refactor Code:**
    *   Extract business logic from the controller into a dedicated use case or service layer.
    *   Ensure proper dependency injection.
    *   Improve readability and maintainability.
    *   Apply modern Java features where appropriate.
3.  **Add/Update Tests:** If the refactoring significantly changes behavior, ensure existing tests are updated or new tests are added to cover the modified logic.
4.  **Update Documentation (if necessary):** Reflect any significant changes in the API or functionality in relevant documentation.

Provide a summary of the changes made and the rationale behind them.

Use the following legacy code as your input:
{{LEGACY_CODE_CONTENT}}
