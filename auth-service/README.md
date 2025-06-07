# 🛡️ Auth Service – Spring Boot Microservice

A complete authentication microservice built with **Spring Boot**, **PostgreSQL**, **Redis**, and **Mailtrap SMTP**. Designed for integration into a microservices architecture.

---

## 🚀 Features

- ✅ User registration with email verification
- 📧 Email delivery via Mailtrap SMTP
- 🔐 JWT authentication (Access + Refresh tokens)
- 🔁 Access token refreshing with refresh token
- 🛠 Password change (requires current password)
- 🧠 Password recovery and reset
- 🔓 Logout with refresh token invalidation
- 🔒 Route protection using `@AuthenticationPrincipal`, `@PreAuthorize`
- 📦 `.env` support with `java-dotenv`
- 🐳 Docker setup for PostgreSQL and Redis

---

## 🧰 Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Security 6
- PostgreSQL
- Redis
- Mailtrap (SMTP sandbox)
- JWT
- Docker / Docker Compose

---

## ⚙️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/auth-service-spring-boot.git
cd auth-service-spring-boot
```

---

### 2. Create `.env` file

```bash
cp .env.example .env
```

Then configure your environment variables:

```env
# PostgreSQL
DB_URL=jdbc:postgresql://localhost:15432/auth_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT
JWT_SECRET=your_long_secret_key_here

# Mailtrap
MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password
MAIL_FROM=no-reply@yourapp.com

# URLs
BACKEND_BASE_URL=http://localhost:8080
FRONTEND_BASE_URL=http://localhost:3000
```

---

### 3. Run PostgreSQL & Redis via Docker

```bash
docker compose up -d
```

- PostgreSQL: `localhost:15432`
- Redis: `localhost:6379`

---

### 4. Run the App

```bash
./mvnw spring-boot:run
```

or

```bash
mvn spring-boot:run
```

---

## 📬 Email Testing (Mailtrap)

1. Go to [Mailtrap.io](https://mailtrap.io/)
2. Create an inbox
3. Copy SMTP credentials and paste into `.env`

All confirmation/reset emails will appear in Mailtrap.

---

## 🔐 Auth Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/register` | Register a user |
| GET  | `/api/auth/verify?token=` | Email verification |
| POST | `/api/auth/login` | Login (access + refresh tokens) |
| POST | `/api/auth/change-password` | Change password |
| POST | `/api/auth/forgot-password` | Send password reset email |
| POST | `/api/auth/reset-password?token=` | Reset password |
| POST | `/api/auth/refresh` | Get new access token |
| POST | `/api/auth/logout` | Logout (invalidate refresh token) |

---

## 📦 Example Usage

### Register

```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "Test1234!"
}
```

Response:

```json
{
  "message": "Registration successful. Please check your email to confirm."
}
```

---

### Login

```http
POST /api/auth/login
{
  "email": "test@example.com",
  "password": "Test1234!"
}
```

Response:

```json
{
  "accessToken": "...",
  "refreshToken": "..."
}
```

---

### Refresh Token

```http
POST /api/auth/refresh
{
  "refreshToken": "..."
}
```

---

### Logout

```http
POST /api/auth/logout
{
  "refreshToken": "..."
}
```

---

### Protected Request Example

```http
GET /api/users/me
Authorization: Bearer {accessToken}
```

---

## 🗂️ Project Structure

```
src/
├── controller/
├── service/
├── entity/
├── dto/
├── config/
├── repository/
├── security/
```

---

## ⚠️ Environment Security

Make sure `.env` is listed in `.gitignore`:

```bash
.env
```

---

## 📄 License

MIT (or your preferred license)
