# Swagger Documentation Guide

## Overview

This document provides instructions on how to access and use the Swagger UI documentation for the Form Services microservices.

## Issues Fixed

The following issues were identified and fixed:
1. **Port Configuration**: API Gateway was running on port 8080, changed to port **8762**
2. **Security Blocking**: Security configurations were blocking Swagger endpoints - fixed by permitting Swagger paths
3. **Swagger Configuration**: Updated `use-root-path` from `false` to `true` in all services

## Service Ports

| Service | Port | Swagger UI Path |
|---------|------|-----------------|
| API Gateway | **8762** | `/swagger-ui.html` |
| User Service | 8082 | `/swagger-ui.html` |
| Form Service | 8081 | `/swagger-ui.html` |
| Role Service | 8083 | `/swagger-ui.html` |
| Auth Server | 8085 | `/swagger-ui.html` |
| Submission Service | 8086 | `/swagger-ui.html` |

## Accessing Swagger Documentation

### Central Swagger UI (Recommended)

Access all services through the API Gateway on port **8762**:

```
http://localhost:8762/swagger-ui.html
```

This provides:
- API Gateway endpoints documentation
- Links to all backend services' Swagger documentation

### Individual Service Swagger

You can also access each service's Swagger documentation directly:

| Service | URL |
|---------|-----|
| API Gateway | `http://localhost:8762/swagger-ui.html` |
| User Service | `http://localhost:8082/swagger-ui.html` |
| Form Service | `http://localhost:8081/swagger-ui.html` |
| Role Service | `http://localhost:8083/swagger-ui.html` |
| Auth Server | `http://localhost:8085/swagger-ui.html` |
| Submission Service | `http://localhost:8086/swagger-ui.html` |

### API Docs (JSON)

Access the OpenAPI 3.0 specification in JSON format:

| Service | URL |
|---------|-----|
| API Gateway | `http://localhost:8762/v3/api-docs` |
| User Service | `http://localhost:8082/v3/api-docs` |
| Form Service | `http://localhost:8081/v3/api-docs` |
| Role Service | `http://localhost:8083/v3/api-docs` |
| Auth Server | `http://localhost:8085/v3/api-docs` |
| Submission Service | `http://localhost:8086/v3/api-docs` |

## Starting the Services

### Prerequisites
- Java 21
- Maven
- MongoDB (for Form Service and Submission Service)
- PostgreSQL (for User Service, Role Service, Auth Server)
- Eureka Server (typically on port 8761)

### Starting Order

1. **Eureka Server** (Port 8761)
2. **Config Server** (Port 8888)
3. **API Gateway** (Port **8762**) - Run last to ensure Swagger UI is accessible
4. **User Service** (Port 8082)
5. **Form Service** (Port 8081)
6. **Role Service** (Port 8083)
7. **Auth Server** (Port 8085)
8. **Submission Service** (Port 8086)

### Maven Commands

```bash
# Build all services
cd API-Gateway && ./mvnw clean install -DskipTests
cd ../User-Service && ./mvnw clean install -DskipTests
cd ../Form-Service && ./mvnw clean install -DskipTests
cd ../Role-Service && ./mvnw clean install -DskipTests
cd ../Auth-Server && ./mvnw clean install -DskipTests
cd ../Submission-Service && ./mvnw clean install -DskipTests

# Run a specific service
cd API-Gateway && ./mvnw spring-boot:run
cd User-Service && ./mvnw spring-boot:run
cd Form-Service && ./mvnw spring-boot:run
```

## Using Swagger UI

1. Open browser and navigate to: `http://localhost:8762/swagger-ui.html`

2. The Swagger UI will display all available API endpoints

3. Click on any endpoint to expand it and see:
   - HTTP method and path
   - Request parameters
   - Request body schema
   - Response codes and schemas

4. Click "Try it out" to test API endpoints directly from the browser

## Troubleshooting

### Swagger UI Not Loading

1. **Check if API Gateway is running on port 8762**
   ```bash
   curl http://localhost:8762/actuator/health
   ```

2. **Verify Swagger endpoints are not blocked by security**
   - Check `GatewaySecurityConfig.java` has permitAll for `/swagger-ui/**`, `/v3/api-docs/**`
   - Check `SecurityConfig.java` in Auth Server

3. **Check application logs for errors**

### Endpoints Not Showing

1. Ensure controllers have proper annotations:
   ```java
   @RestController
   @RequestMapping("/api/users")
   @Tag(name = "User Management", description = "User management APIs")
   ```

2. Verify the service is registered with Eureka

3. Check that `springdoc-openapi-starter-webmvc-ui` dependency is in pom.xml

### Common Error Messages

- **404 Not Found**: Service may be down or running on different port
- **403 Forbidden**: Security is blocking Swagger paths
- **Blank Page**: Check browser console for JavaScript errors

## Additional Resources

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI 3.0 Specification](https://swagger.io/specification/)
- [Swagger UI GitHub](https://github.com/swagger-api/swagger-ui)

## Configuration Files Modified

1. `API-Gateway/src/main/resources/application.yaml` - Changed port to 8762
2. `API-Gateway/src/main/java/com/form_builder/API_Gateway/GatewaySecurityConfig.java` - Added Swagger paths to permitAll
3. `Auth-Server/src/main/java/com/form_builder/Auth_Server/config/SecurityConfig.java` - Added Swagger paths to permitAll
4. All service `application.yaml` files - Updated swagger-ui configuration

---

**Note**: Port 8762 was chosen as requested to replace the default 8761 (Eureka) port for Swagger documentation.
