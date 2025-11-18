# Spring Boot Auth App

**Simple Login / Registration demo** built with **Spring Boot (Thymeleaf)** and an **H2 in-memory database**.  
Deployed to Railway: **https://spring-boot-auth-app-production.up.railway.app**

> A small learning project that demonstrates user registration, login, session handling and a minimal UI.

---

## Demo
Live demo: https://spring-boot-auth-app-production.up.railway.app

---

## Screenshots

**Login page (after registration)**  
![Login](/images/screen1.png)

**Home page (shows user details after login)**  
![Home](/images/screen2.png)

**Register page**  
![Register](/images/screen3.png)

---

## Features
- Register new users (name, username, password, gender, email, mobile)
- Login with username + password
- Session-based home page showing user details
- Password hashing (BCrypt)
- H2 In-Memory DB for quick demo (data resets on restart)
- Ready-to-deploy to Railway / Docker

---

## Tech stack
- Java 17
- Spring Boot 3 (Spring Web, Thymeleaf, Spring Data JPA)
- H2 In-Memory DB (dev/demo)
- Maven
- Railway (for deployment)
- Optional: `spring-security-crypto` for BCrypt

---

## Project structure
```
spring-boot-app/
├─ pom.xml
├─ README.md
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/springauth/
│  │  │  ├─ Application.java
│  │  │  ├─ AuthController.java
│  │  │  ├─ User.java
│  │  │  ├─ UserRepository.java
│  │  ├─ resources/
│  │  │  ├─ application.properties
│  │  │  └─ templates/
│  │  │     ├─ login.html
│  │  │     ├─ register.html
│  │  │     └─ home.html
└─ dockerfile/ (optional)
```

---

## Endpoints
- `GET /` or `/login` — Login page  
- `POST /login` — Authenticate user  
- `GET /register` — Registration page  
- `POST /register` — Save new user  
- `GET /home` — Home page (requires session)  
- `POST /logout` — Invalidate session  

---

## Running locally
```bash
git clone https://github.com/<your-username>/spring-boot-auth-app.git
cd spring-boot-auth-app/spring-boot-app
mvn spring-boot:run
```

Then open:  
`http://localhost:8080`

---

## Deploying to Railway
Build settings:

**Build command**
```
mvn -DskipTests package
```

**Start command**
```
java -jar target/*.jar --server.port=$PORT
```

Steps:
1. Push your repo to GitHub  
2. On Railway → New Project → Deploy from GitHub  
3. Set start command if needed  
4. Deploy → visit generated URL

---

## Using MySQL instead of H2
Set these Railway environment variables:

```
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:<port>/<db>
SPRING_DATASOURCE_USERNAME=<username>
SPRING_DATASOURCE_PASSWORD=<password>
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

Remove H2 from `pom.xml` if moving to MySQL only.

---

## Dockerfile example
```dockerfile
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/spring-boot-app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

---

## Contributing
Feel free to open issues or PRs.

Enjoy building! 🚀
