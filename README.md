# SGS Server

Backend API for the SGS school management system.

The server provides authentication, student management, employee management, and other school-related services through a REST API.

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA / Hibernate
* Spring Security Crypto
* PostgreSQL
* Flyway
* Gradle

## Features

* JWT-based authentication
* BCrypt password hashing
* Student accounts
* Employee accounts
* Admin authorization
* Student CRUD operations
* Database migrations with Flyway
* PostgreSQL persistence
* Request validation and HTTP error handling

## API

Authentication endpoints currently include:

```text
POST /auth/login
POST /auth/logout
POST /auth/change-password

POST /auth/create/student
POST /auth/create/employee
```

Student endpoints:

```text
GET    /api/students
GET    /api/students/{id}
POST   /api/students
PATCH  /api/students/{id}
DELETE /api/students/{id}
```

More endpoints will be added as the system develops.

## Authentication

Login requests contain the account ID, password, and account type.

Example:

```json
{
  "id": 2,
  "password": "your-password",
  "userType": "EMPLOYEE"
}
```

A successful login returns a signed JWT.

The JWT contains the user's ID and account type and is used to authenticate protected requests.

Passwords are never stored in plaintext. Passwords are hashed using BCrypt before being stored in PostgreSQL.

## Database

PostgreSQL is used as the primary database.

Flyway manages database schema changes, while Hibernate is configured to validate the existing schema rather than modify it automatically.

```text
Application
     │
     ▼
   Flyway
     │
     ▼
PostgreSQL
     │
     ▼
 Hibernate validation
```

Migrations are located in:

```text
src/main/resources/db/migration/
```

## Running Locally

### Requirements

* Java 21+
* PostgreSQL
* Git

Clone the repository:

```bash
git clone <repository-url>
cd <repository-directory>
```

Configure the database connection in the application's configuration.

Then run:

```bash
./gradlew bootRun
```

The server will start on:

```text
http://localhost:6767
```

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/sgs/server/
│   │       ├── Controllers/
│   │       ├── Objects/
│   │       ├── Repositories/
│   │       └── Services/
│   │
│   └── resources/
│       └── db/
│           └── migration/
│
└── test/
```

The project is structured around controllers, services, repositories, and domain objects, with database changes managed separately through Flyway migrations.

## Status

The backend is currently under active development.

Authentication and the initial student/employee management functionality are implemented. Additional school management endpoints and features are planned.

## License

This project is currently not licensed for redistribution.
