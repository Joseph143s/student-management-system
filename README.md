# Student Management System (Spring Boot Backend)

This is a backend project built using **Spring Boot**.  
It manages students, courses, departments, faculty, enrollments, and authentication using JWT.

The project is mainly built for learning and improving backend development skills in real-world backend development.


#  Features

- Student management (CRUD operations)
- Course management system
- Department management system
- Faculty management system
- Address management (One-to-One mapping)
- Enrollment system (student-course mapping)
- JWT authentication system
- Role-based access control (Admin, Student, Faculty)
- Global exception handling
- API response wrapper for consistent API structure
- Input validation using annotations


# Tech Stack

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL
- Maven



#  Project Structure

Controller → Service → ServiceImpl → Repository → Database

Other layers used:
- DTO (Request / Response)
- Mapper classes
- Exception Handling (Global Exception Handler)
- Utility classes (API Response Builder)


#  Security

- JWT-based authentication
- Role-based authorization using `@PreAuthorize`
- Secure REST APIs
- Stateless session management


#  API Endpoints

##  Auth
- POST `/auth/login`
- POST `/auth/register/student`
- POST `/auth/register/faculty`
- POST `/auth/register/admin`

##  Student
- GET `/students`
- POST `/students`
- GET `/students/{id}`
- PUT `/students/{id}`
- DELETE `/students/{id}`

##  Course
- GET `/courses`
- POST `/courses`
- GET `/courses/{id}`
- PUT `/courses/{id}`
- DELETE `/courses/{id}`

##  Department
- GET `/departments`
- POST `/departments`
- GET `/departments/{id}`
- PUT `/departments/{id}`
- DELETE `/departments/{id}`

##  Enrollment
- POST `/enrollments`
- GET `/enrollments`
- PATCH `/enrollments/{id}/drop`
- PATCH `/enrollments/{id}/complete`

##  Address
- GET `/addresses`
- POST `/addresses`
- PUT `/addresses/{id}`
- DELETE `/addresses`

---

#  How to Run This Project

## 1. Clone the repository

```bash
git clone https://github.com/Joseph143s/student-management-system.git

2. Configure database

Update application.properties:

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


3. Run the application
mvn spring-boot:run


4. Swagger UI

http://localhost:9092/swagger-ui/index.html


# Project Learning & Improvements

What I learned from this project

-REST API development using Spring Boot
-JWT authentication and security
-Role-based access control
-Entity relationships (OneToOne, OneToMany, ManyToOne)
--DTO pattern and clean architecture
-Global exception handling
-Input validation
-API design best practices


# Future Improvements:

-Add pagination and sorting
-Add React frontend
-Improve exception handling further
-Deploy project on cloud (AWS / Render)
-Add unit testing (JUnit + Mockito)

# Author

  Built by a student as a learning project to understand backend development using Spring Boot.

 If you like this project, feel free to star the repository!
