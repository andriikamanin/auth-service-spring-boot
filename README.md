# 🛡️ Auth Service – Spring Boot Microservice

This is a basic authentication microservice built with **Spring Boot**, **PostgreSQL**, **Redis**, and **Mailtrap SMTP** for email confirmation. It's designed for integration into a microservices-based system.

---

## 🚀 Features

- User registration with email confirmation
- Email delivery via Mailtrap SMTP (sandbox)
- Environment variable support using `.env`
- Dockerized PostgreSQL and Redis
- Ready for integration with API Gateway or other services

---

## 🧰 Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Security
- PostgreSQL
- Redis
- Mailtrap (SMTP sandbox)
- Docker / Docker Compose
- `java-dotenv` for environment variable support

---

## 📦 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/auth-service-spring-boot.git
cd auth-service-spring-boot
```

---

### 2. Create `.env` file

Copy the template and update credentials:

```bash
cp .env.example .env
```

Update values like:

- `MAIL_USERNAME`, `MAIL_PASSWORD` → your [Mailtrap.io](https://mailtrap.io/) credentials
- `DB_PASSWORD` → your preferred DB password

---

### 3. Start PostgreSQL and Redis via Docker

```bash
docker compose up -d
```

This will start:
- PostgreSQL at `localhost:15432`
- Redis at `localhost:6379`

---

### 4. Run the application

Make sure Java 17+ and Maven are installed. Then:

```bash
./mvnw spring-boot:run
```

If using `java-dotenv`, the `.env` file will be loaded automatically.

---

## 📬 Testing email with Mailtrap

1. Go to [Mailtrap.io](https://mailtrap.io/)
2. Create a new inbox and copy SMTP credentials
3. Paste them into your `.env`:

```env
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password
```

You will receive verification emails in the Mailtrap inbox when registering users.

---

## 🧪 Example: Register via Postman

- **POST** `http://localhost:8080/api/auth/register`
- **Body (JSON)**:

```json
{
  "email": "test@example.com",
  "password": "Test1234!"
}
```

Expected response:

```json
{
  "message": "Registration successful. Please check your email to confirm."
}
```

You will receive a confirmation link like:

```
http://localhost:8080/api/auth/verify?token=abc123
```

---

## 🗂️ Project Structure

```
src/
├── controller/
├── service/
├── entity/
├── config/
├── dto/
├── repository/
├── security/
├── util/
```

---

## 🛑 .env Notice

Your `.env` file **must NOT be committed** to the repository. Use `.env.example` to share default configs safely.

Make sure `.gitignore` includes:

```
.env
```

---

## 📄 License

MIT (or specify your license)