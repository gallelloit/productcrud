##### Products and orders CRUD

Built with IntelliJ IDEA Community Edition 2020.2

Uses:
- Apache Maven 3.6.3
- Springboot 2.4.0
- Spring Data JPA for entity management (Repository)
- Runs on port 8085

##### TODOs
- Customize error handling
- Dates format validation
- Enhancing order placing. Only working with direct persisting so far. Necesary to link existing products to new orders.

##### How to run
1. Git clone from 
2. To run tests, type mvn test from command line
3. To run app, type mvn springboot:run from command line. **Make sure you don't have other app running on port 8085**
4. Manually test app with Swagger 2 (http://localhost:8085/api/swagger-ui/)
5. With the app running, you can open H2 Console (http://localhost:8085/api/h2-console))
6. In the root directory a Postman JSON export can be found with the necessary Requests configured. 

##### Database:
- DDL auto-generated
- In-memory H2 Database persisted to File
- Separate In-memory H2 Database for testing purposes
- Prod profile des not generates the DDL (would be necesary to manually mantain a phisical DB model)

##### API:

- POST /api/product - Creates a product
- GET /api/products - Retrieves all products
- PUT /api/product - Updates a product
- DELETE /api/product/{product_id} - Deletes a product
- POST /api/order - Place an order
- GET /api/order/findByDatesRange/{startDate}/{endDate}
- GET /api/order/amount/{orderId}

##### Model:
Product:
- id - int - auto
- name - String
- price - double
- create_date - LocalDateTime
- is_valid - boolean

Order:
- id - int - auto
- email - String
- create_date - LocalDateTime
- products - List of Product
