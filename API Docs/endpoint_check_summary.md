## API Endpoint Check Summary

All endpoints are prefixed with `/api/v1`.

### Cousine Endpoints
*   `GET /Cousine`: **Working** - Retrieved a list of cousines.
*   `GET /Cousine/{id}/stores`: **Working** - Retrieved stores for cousine ID 1.
*   `GET /Cousine/search/{text}`: **Working** - Searched for cousines by name (e.g., "Pi").

### Product Endpoints
*   `GET /Product`: **Working** - Retrieved a list of products.
*   `GET /Product/{id}`: **Working** - Retrieved product with ID 1.
*   `GET /Product/search/{text}`: **Working** - Searched for products by name (e.g., "Shrimp").

### Store Endpoints
*   `GET /Store`: **Working** - Retrieved a list of stores.
*   `GET /Store/search/{text}`: **Working** - Searched for stores by name (e.g., "Hai").
*   `GET /Store/{id}`: **Working** - Retrieved store with ID 1.
*   `GET /Store/{id}/products`: **Working** - Retrieved products for store ID 1.

### Endpoints Not Checked (Requires Authentication or Request Body)

**Customer Endpoints:**
*   `POST /Customer` (Sign Up): Requires a `SignUpRequest` body.
*   `POST /Customer/auth` (Sign In): Requires a `SignInRequest` body.

**Order Endpoints:**
*   `POST /Order`
*   `GET /Order/{id}`
*   `GET /Order/{id}/customer`
*   `DELETE /Order/{id}`
*   `POST /Order/{id}/payment`
*   `POST /Order/{id}/delivery`

These `Order` endpoints all require user authentication and could not be tested without a valid user token.