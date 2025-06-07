# My social network



---

## 🚀 Features

- ✅ User registration with email verification
- 📧 Email delivery via Mailtrap SMTP
- 🔐 JWT authentication (Access + Refresh tokens)
- 🔁 Access token refreshing with refresh token
- 🔓 Logout with refresh token invalidation
- 🛠 Password change (requires current password)
- 🧠 Password recovery and reset
- 🔒 Route protection using `@AuthenticationPrincipal`, `@PreAuthorize`
- 📨 Double email confirmation flow before changing email
- 📬 Kafka events for user registration, login, email update
- 📦 `.env` support with `java-dotenv`
- 🐳 Docker setup for PostgreSQL and Redis

---

## 🧰 Tech Stack

- Java 17+
- Spring Boot 3+
- Spring Security 6
- Spring Data JPA (PostgreSQL)
- Redis
- Kafka
- Mailtrap SMTP
- JWT (JSON Web Tokens)
- Docker / Docker Compose

---

## ⚙️ Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/auth-service.git
cd auth-service
```

### 2. Create `.env` file

```bash
cp .env.example .env
```

Edit `.env` with the appropriate values:

```env
DB_URL=jdbc:postgresql://localhost:15432/auth_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

REDIS_HOST=localhost
REDIS_PORT=6379

JWT_SECRET=your_very_secure_secret_key

MAIL_HOST=sandbox.smtp.mailtrap.io
MAIL_PORT=2525
MAIL_USERNAME=your_mailtrap_username
MAIL_PASSWORD=your_mailtrap_password
MAIL_FROM=no-reply@yourapp.com

BACKEND_BASE_URL=http://localhost:8080
FRONTEND_BASE_URL=http://localhost:3000

KAFKA_BOOTSTRAP_SERVERS=localhost:9092
```

---

### 3. Run PostgreSQL, Redis & Kafka with Docker

```bash
docker compose up -d
```

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

## 🔐 Auth Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/register` | Register a user |
| GET  | `/api/auth/verify?token=` | Email verification |
| POST | `/api/auth/login` | Login (access + refresh tokens) |
| POST | `/api/auth/change-password` | Change password |
| POST | `/api/auth/forgot-password` | Send password reset email |
| POST | `/api/auth/reset-password?token=` | Reset password |
| POST | `/api/auth/refresh` | Refresh access token |
| POST | `/api/auth/logout` | Logout |
| POST | `/api/auth/change-email/request-old` | Send code to old email |
| POST | `/api/auth/change-email/request-new` | Send code to new email |
| POST | `/api/auth/change-email/confirm` | Confirm and update email |

---

## 🗂️ Project Structure

```
src/
├── config/
├── controller/
├── dto/
├── entity/
├── kafka/
├── repository/
├── security/
├── service/
├── util/
```

---

## 📬 Kafka Integration

- `user.registered`: Published on registration.
- `user.login`: Published on login (contains IP, agent, timestamp).
- `user.emailChanged`: Published on successful email change.

---

## 🧪 Email Testing

Use [Mailtrap.io](https://mailtrap.io) to simulate inbox and email verification flows.

---

## ✅ Example Usage: Change Email Flow

1. Send code to old email:

```http
POST /api/auth/change-email/request-old
Authorization: Bearer {accessToken}
```

2. Send code to new email:

```http
POST /api/auth/change-email/request-new
Authorization: Bearer {accessToken}
{
  "newEmail": "new@example.com"
}
```

3. Confirm both codes:

```http
POST /api/auth/change-email/confirm
Authorization: Bearer {accessToken}
{
  "oldEmailCode": "123456",
  "newEmailCode": "654321"
}
```

---

## 📦 Docker Compose

Includes PostgreSQL, Redis, and Kafka:

```bash
docker compose up
```

---

## 📁 .env Security

Make sure `.env` is in `.gitignore`:

```
.env
```

---

## 📄 License

MIT License – feel free to use and modify!