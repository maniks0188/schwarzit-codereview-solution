
# Manage Coupons APIs

This project provides REST APIs to create discount coupons that further can be applied on the shopping basket. The project also provide an API to fetch all the coupons.

---

## **Features**
- Create new coupons and saves in the database.
- Update the basket to apply discount coupons.
- Validates input for empty or invalid values.
- Provides test coverage.
- OpenAPI documentation

---
## **Scope of the Solution**
The goal of this project is to provide simple REST APIs to manage coupons. 
The solution involves:

1. Implementing simple Spring Boot-based APIs to handle HTTP requests.
2. Validating input to ensure no empty or invalid values are processed.
3. Extensive test coverage (up to 90%) bu using eclipse inbuild plugin(Eclemma code coverage) and the resolution of code quality issues (0% unresolved issues) using eclipse Sonarlint plugin for code quality checks and for test coverage analysis.
4. Well API documentation using Swagger, alongside clean code practices with meaningful comments to ensure maintainability

## **Prerequisites**
1. **Java**: JDK 11 or later.
2. **Maven**: Installed for building the project.

---

## **Build and Run the Application**

### **1. Build the Project**
To build the Spring Boot project and generate the JAR file:
```bash
mvn install -DskipTests
```

The JAR file will be created in the `target` directory.

---

### **2. Run the Application Using IDE or Jar**
 Using IDE, just click on run after building the project and the Springboot application should be up and running.
 Using Jar, after running the command (mvn clean install), navigate to the project "target" folder and run the jar using below command
 java -jar coupon-0.0.1-SNAPSHOT.jar

#### **2.3. Access the Application**
Once the container is running, the API will be accessible at:
```
http://localhost:8080
```

---

## **Endpoints**

### **POST /api/v1/apply**
**Description**: Applies the coupon code to the basket

**Request Body**:
```json
{
    "code":"code-10",
    "basket":{
        "value":"1233"
    }
}
```
### **POST /api/v1/create**
**Description**: Creates a new coupon in the database

**Request Body**:
```json
{
    "discount":177.50,
    "code":"1234",
    "minBasketValue":5000.50
}
```
### **GET /api/v1/coupons?couponCodes={list of codes}**
**Description**: Gets all the coupons based on code from the database

**Response body**:
```json
[
    {
        "discount": 177.50,
        "code": "1234",
        "minBasketValue": 5000.50
    }
]
```
