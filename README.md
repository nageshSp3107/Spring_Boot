# Spring Boot Kotlin JWT Authentication with Notes CRUD

A **Spring Boot** project built with **Kotlin**, implementing secure JWT authentication (generation, validation, refresh), user registration & login, and **Notes CRUD operations**.  
Data persistence is managed with **MongoDB**.

---

## 🚀 Features

- **Authentication Service**
  - User Registration
  - User Login
  - JWT Token Generation & Validation
  - Token Refresh

- **Notes Service**
  - Create a Note
  - Read Notes (per user)
  - Update a Note
  - Delete a Note

- **Tech Stack**
  - Kotlin
  - Spring Boot (Web, Security, Data MongoDB)
  - JWT (JSON Web Tokens)
  - MongoDB
  - Gradle (Kotlin DSL)

🔑 API Endpoints

Authentication
| Method | Endpoint             | Description       |
| ------ | -------------------- | ----------------- |
| POST   | `/api/auth/register` | Register new user |
| POST   | `/api/auth/login`    | Login & get token |
| POST   | `/api/auth/refresh`  | Refresh JWT token |


Notes (Requires JWT in Authorization: Bearer <token>)
| Method | Endpoint          | Description     |
| ------ | ----------------- | --------------- |
| POST   | `/api/notes`      | Create new note |
| GET    | `/api/notes`      | Get all notes   |
| GET    | `/api/notes/{id}` | Get note by ID  |
| PUT    | `/api/notes/{id}` | Update a note   |
| DELETE | `/api/notes/{id}` | Delete a note   |


✅ Future Improvements

Role-based access control (Admin/User)

Unit & Integration Tests

Swagger/OpenAPI documentation

Docker Compose for MongoDB + App

---
