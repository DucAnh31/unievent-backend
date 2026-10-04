# 🎓 UniEvent 

**University event management API built with Java and Spring Boot.**

UniEvent is a basic university event management platform for students, event organizers, and administrators. It brings common event activities into one place, including viewing events, managing registrations, and recording attendance.
## ✨ Features

| Area | Capabilities                                                                                                                                            |
| --- |---------------------------------------------------------------------------------------------------------------------------------------------------------|
| Authentication | Registration, email verification, login, JWT access and refresh tokens, refresh token rotation, logout, and password recovery via email OTP.            |
| Students | Browse and filter events, register or cancel, view registrations, retrieve a check-in code, and check attendance status.                                |
| Organizers | Create and manage their own events, submit them for approval, close registration, cancel events, update events, check students in, and view statistics. |
| Administrators | Approve, reject, and cancel events; manage categories, user roles, and account statuses.                                                                |

### Event workflow

1. An organizer creates an event as `DRAFT` and submits it for approval.
2. An administrator approves it; the event becomes `PUBLISHED` and students can register while registration is open.
3. The system checks the deadline and available capacity before creating a registration.
4. The student retrieves a check-in code. A client can render that code as a QR code.
5. The organizer validates the code and records one check-in for the registration.

## 🧰 Tech stack

| Layer | Technologies |
| --- | --- |
| API and security | Java 17, Spring Boot, Spring Security, JWT, Bean Validation |
| Data | Spring Data JPA, Hibernate, MySQL, MapStruct |
| Temporary state | Redis for refresh tokens, OTPs, cooldowns, and request counters |
| Email | Spring Mail / SMTP |
| Build and runtime | Maven, Docker, Docker Compose |


## 🚀 Getting started

### Prerequisites

- Docker Engine and Docker Compose
- SMTP credentials

Run the following commands from the repository root, where `compose.yaml`, `Dockerfile`, and `pom.xml` are located.

### 1. Configure the environment

Create a `.env` file in the repository root, example `.env`:


```dotenv
DBMS_USERNAME=your_mysql_username
DBMS_PASSWORD=your_mysql_password
DBMS_ROOT_PASSWORD=your_mysql_root_password
EMAIL_USERNAME=you@example.com
EMAIL_PASSWORD=your_email_app_password
JWT_SECRET_KEY=your_secret_key
FRONTEND_BASE_URL=your_fe_base_url
```

### 2. Start the services

```bash
docker compose up --build -d
```

The API is available at http://localhost:8080. Within the Compose network, the app connects to MySQL at db:3306 and Redis at redis:6379.

## 📚 API overview

All endpoints start with `/api/v1`. Protected endpoints require `Authorization: Bearer <access_token>`.

### Public and account

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/auth/register` | Create a student account |
| `POST` | `/auth/email/verify` | Verify an email address |
| `POST` | `/auth/email/verification-resend` | Request another verification email |
| `POST` | `/auth/login` | Obtain access and refresh tokens |
| `POST` | `/auth/refresh` | Rotate a refresh token |
| `POST` | `/auth/logout` | Revoke the current refresh token |
| `POST` | `/auth/password/forgot` | Request a password recovery OTP |
| `POST` | `/auth/password/otp/verify` | Verify the OTP and obtain a password reset token |
| `POST` | `/auth/password/reset` | Set a new password |
| `GET` | `/events` | Search, filter, sort, and paginate public events |
| `GET` | `/events/{eventId}` | View a public event |
| `GET` | `/categories` | Search and paginate categories |
| `GET` | `/categories/{eventCategoryId}` | View a category |

### Authenticated user

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/users/me` | View the current user's profile |
| `PATCH` | `/users/me` | Update the current user's profile |
| `PATCH` | `/users/me/password` | Change the current user's password |

### Student

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/student/events/{eventId}/registrations` | Register for an event |
| `DELETE` | `/student/events/{eventId}/registrations` | Cancel an event registration |
| `GET` | `/student/registrations` | List the current student's registrations |
| `GET` | `/student/registrations/{registrationId}` | View a registration |
| `GET` | `/student/registrations/{registrationId}/check-in-code` | Retrieve the check-in code |
| `GET` | `/student/registrations/{registrationId}/check-in` | View the check-in status |

### Organizer

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/organizer/events` | Create a draft event |
| `GET` | `/organizer/events` | List the organizer's events |
| `GET` | `/organizer/events/{eventId}` | View an owned event |
| `PATCH` | `/organizer/events/{eventId}` | Update an owned event |
| `DELETE` | `/organizer/events/{eventId}` | Delete an eligible draft event |
| `POST` | `/organizer/events/{eventId}/submit-for-approval` | Submit an event for administrator approval |
| `POST` | `/organizer/events/{eventId}/close-registration` | Close registration |
| `POST` | `/organizer/events/{eventId}/cancel` | Cancel an event |
| `GET` | `/organizer/events/{eventId}/registrations` | List event registrations |
| `DELETE` | `/organizer/events/{eventId}/registrations/{registrationId}` | Cancel a student's registration |
| `POST` | `/organizer/events/{eventId}/check-ins` | Check in a registered student |
| `GET` | `/organizer/events/{eventId}/check-ins` | List event check-ins |
| `GET` | `/organizer/events/{eventId}/statistics` | View event statistics |

### Administrator

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/admin/users` | Search and paginate users |
| `GET` | `/admin/users/{userId}` | View a user |
| `PATCH` | `/admin/users/{id}/role` | Change a user's role |
| `PATCH` | `/admin/users/{id}/status` | Change an account's status |
| `GET` | `/admin/events` | Search and paginate all events |
| `GET` | `/admin/events/{eventId}` | View an event |
| `POST` | `/admin/events/{eventId}/approve` | Approve an event |
| `POST` | `/admin/events/{eventId}/reject` | Reject an event |
| `POST` | `/admin/events/{eventId}/cancel` | Cancel an event |
| `POST` | `/admin/categories` | Create a category |
| `PATCH` | `/admin/categories/{eventCategoryId}` | Update a category |
| `DELETE` | `/admin/categories/{eventCategoryId}` | Delete a category |

