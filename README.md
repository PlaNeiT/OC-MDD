# P6 Full-Stack Reseau Dev

## Project Overview

This repository contains a full-stack application split into two parts:
1. **Frontend**: Built with Angular 14.
2. **Backend**: A Spring Boot application for handling API requests and managing data.

---

## Prerequisites

### General Requirements
- **Node.js**: Version 16 or higher. [Download Node.js](https://nodejs.org/)
- **Angular CLI**: Version 14.1.3. Install it globally using:
```bash
npm install -g @angular/cli@14.1.3
```
- Java Development Kit (JDK): Version 17. Ensure JAVA_HOME is properly set.
- Maven: Version 3.6 or later. Install Maven
- MySQL Database: Install MySQL server locally or use a cloud-hosted MySQL database.
- Environment Variables
- 
### Backend Configuration
To configure the backend, modify the application.properties, modify the env variable for it to work.
```
# Database Configuration
DB_USERNAME=your_username
DB_PASSWORD=your_password

# JWT Configuration
JWT_SECRET=your_secret_key
JWT_EXPIRATION=3600000
```

### Setting Up the Database
- Install MySQL
- Start the MySQL server.
- run the schema.sql and data.sql from back/src/ressources

### Execute the backend
- install backend
```bash
cd back
mvn clean install
```
- Run the Backend
```bash
mvn spring-boot:run
```
The backend server will start at http://localhost:8080.

### Execute the frontend

- install frontend
```bash
cd front
npm install
```

- Run the frontend
Start the frontend server:
```bash
ng serve
```

The frontend application will run at http://localhost:4200.

## API Documentation
The backend includes Swagger for API documentation. Once the backend server is running, visit the following URL:

http://localhost:8080/swagger-ui/index.html







