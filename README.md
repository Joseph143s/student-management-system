# Student Management System (Spring Boot Backend)

A layered **Spring Boot REST API** for managing **students, courses, departments, faculty, enrollments**, and **role-based authentication** using **JWT**.

This project was built as a learning project to practice real-world backend development patterns: clean layering, DTOs/mappers, security, validation, consistent responses, and global error handling.

---

## Features
- Student management (CRUD)
- Course management (CRUD)
- Department management (CRUD)
- Faculty management (CRUD)
- Address management (One-to-One mapping)
- Enrollment system (Student ↔ Course mapping)
- JWT authentication
- Role-based access control (**Admin**, **Student**, **Faculty**)
- Global exception handling (`@ControllerAdvice`)
- Consistent API response wrapper
- Input validation using annotations

---

## Tech Stack
- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Swagger / OpenAPI

---

## Project Structure
**Controller → Service → Repository → Database**

Additional layers:
- **DTOs** (Request / Response models)
- **Mappers** (DTO ↔ Entity)
- **Exception Handling** (Global Exception Handler)
- **Utilities** (API Response Builder / wrapper)

---

## Security
- JWT-based authentication (stateless)
- Role-based authorization using `@PreAuthorize`
- Secure REST APIs using Spring Security

---

## API Endpoints

### Auth
- `POST /auth/login`
- `POST /auth/register/student`
- `POST /auth/register/faculty`
- `POST /auth/register/admin`

### Student
- `GET /students`
- `POST /students`
- `GET /students/{id}`
- `PUT /students/{id}`
- `DELETE /students/{id}`

### Course
- `GET /courses`
- `POST /courses`
- `GET /courses/{id}`
- `PUT /courses/{id}`
- `DELETE /courses/{id}`

### Department
- `GET /departments`
- `POST /departments`
- `GET /departments/{id}`
- `PUT /departments/{id}`
- `DELETE /departments/{id}`

### Enrollment
- `POST /enrollments`
- `GET /enrollments`
- `PATCH /enrollments/{id}/drop`
- `PATCH /enrollments/{id}/complete`

### Address
- `GET /addresses`
- `POST /addresses`
- `PUT /addresses/{id}`
- `DELETE /addresses/{id}`

---

## How to Run Locally

### 1) Clone the repository
  git clone https://github.com/Joseph143s/student-management-system.git
cd student-management-system

### 2) Configure the database
Update `application.properties`:

​
spring.application.name=StudentManagement
server.port=9092
spring.datasource.url=jdbc:mysql://localhost:3306/kishuu
spring.datasource.username=springuser
spring.datasource.password=Spring123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
logging.level.org.springframework.security=DEBUG

### 3) Run the application
​
mvn spring-boot:run

### 4) Swagger UI
- http://localhost:9092/swagger-ui/index.html

> For protected endpoints: login first, copy the JWT token, then click **Authorize** in Swagger and paste:  
> `Bearer <your-token>`

---

## What I Learned
- Building REST APIs with Spring Boot
- JWT authentication + Spring Security
- Role-based access control
- Entity relationships (OneToOne, OneToMany, ManyToOne)
- DTO pattern and clean layering
- Global exception handling
- Input validation
- API design best practices

---

## Future Improvements
- Add pagination and sorting
- Add unit tests (JUnit + Mockito)
- Add a React frontend
- Deploy to the cloud (AWS / Render)

---

## Author
Built as a learning project to understand backend development using Spring Boot.

If you like this project, feel free to ⭐ the repository!
