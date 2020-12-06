##### Products and orders CRUD

Built with IntelliJ IDEA Community Edition 2020.2

Uses:
- Apache Maven 3.6.3
- Springboot 2.4.0
- Spring Data JPA for entity management (Repository)
- Automatically creates database
- In-memory H2 relational database persisting to file
- Separate In-memory H2 database not persistent for testing purposes
- Separate prod profile with no automatic creation of database

##### How to run
1. Git clone from 
2. From root directory, run mvn springboot:run

##### API:

- POST /api/product - Creates a product
- GET /api/products - Retrieves all products
- PUT /api/product - Updates a product
- DELETE /api/product/{product_id} - Deletes a product

##### Model:
Product:
- id - int - auto
- name - String
- price - double
- create_date - datetime
- is_valid - boolean

Order:
- id - int - auto
- create_date - datetime
