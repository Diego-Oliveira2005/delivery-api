# DeliveryAPI

Delivery system developed with Spring Boot & Java 21.

## 🚀 Technologies
- **Java 21 LTS**
- Spring Boot 3.3.6
- Spring Web
- Spring Data JPA
- MariaDB
- Maven
- **SpringDoc OpenAPI (Swagger UI)**

## ⚡ Modern Resources Utilized
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ How to Execute
1. **Requirements:** JDK 21 installed
2. Clone the repository
3. Configure the database connection to match `application.properties`
4. Execute: `./mvnw spring-boot:run`
5. **Access Documentation:** http://localhost:8080/swagger-ui.html

## 📖 API Documentation (Swagger)
The API is fully documented using OpenAPI 3.0.
Access the interactive UI at: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### 🔑 How to Authenticate
Some endpoints require a JWT Token.
1. Go to `POST /api/auth/login` in Swagger.
2. Use the default admin credentials:
    - **Email:** `admin@delivery.com`
    - **Password:** `123456`
3. Copy the `token` from the response.
4. Click the **Authorize** 🔓 button at the top.
5. Paste the token and click **Authorize**.

## 📋 Main Endpoints
- **Auth:** Login and Register (`/api/auth`)
- **Restaurants:** Manage restaurants (`/api/restaurantes`)
- **Products:** Manage menu items (`/api/produtos`)
- **Orders:** Full order lifecycle (`/api/pedidos`)
- **Reports:** Sales and performance metrics (`/api/relatorios`)

## 🔧 Configuration
- Port: 8080
- Database: MariaDB (Docker or Local)
- Profile: development

## 👨‍💻 Developer
Diego Oliveira - Ages 2025.2

###### Developed with JDK 21 & Spring Boot 3.3.6