# <p align="center">
  <img src="docs/Logo.png" alt="StudyBuddy Logo" width="200"/>
</p>

# 📚 StudyBuddy – Smart Study Matching App

StudyBuddy is a social learning platform that connects students from the same institution and courses based on shared learning styles and academic goals.  
It helps them match, chat, coordinate study sessions (online or in-person), and rate each other after meetings.

---


## 🚀 Main Technologies

| Layer         | Technology                        |
|---------------|-----------------------------------|
| **Backend**   | Spring Boot, Spring Security, JWT |
| **Database**  | PostgreSQL, JPA/Hibernate         |
| **DevOps**    | Docker, Docker Compose            |
| **Auth**      | Email Verification, JWT           |
| **Docs & QA** | Swagger, Postman                  |

---

## 🛠️ Key Features

- Sign up using academic email only  
- Create a personal profile with selected courses and learning preferences  
- Automatic matching based on course 


## 📂 Project Structure

```
StudyBuddy/
├── Dockerfile
├── README.md
├── build.gradle
├── docker-compose.yml
├── docs/
│   ├── Logo.png
│   ├── StudyBuddyERD.png
│   ├── functional-requirements.md
│   └── nonfunctional-requirements.md
├── src/
│   ├── main/
│   │   ├── java/com/amit/studybuddy/
│   │   │   ├── StudyBuddyApplication.java
│   │   │   ├── config/               # OpenAPI & security configuration
│   │   │   ├── controllers/          # REST controllers
│   │   │   ├── domain/               # DTOs, entities, enums and mappers
│   │   │   ├── exceptions/           # Global exception handling
│   │   │   ├── repositories/         # Spring Data repositories
│   │   │   ├── security/             # JWT filters and services
│   │   │   ├── services/             # Core business services
│   │   │   └── utils/                # Utilities (e.g., course importer)
│   │   └── resources/
│   │       ├── application.yml
│   │       └── courses.json
│   └── test/
│       └── java/
├── gradle/
│   └── wrapper/
├── gradlew
├── gradlew.bat
└── settings.gradle
```

## 🔗 REST API Overview

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/v1/auth/register` | Register a new user with an academic email. |
| `POST` | `/api/v1/auth/login` | Authenticate and receive JWT tokens. |
| `GET` | `/api/v1/auth/verify?token=` | Verify email ownership via verification token. |
| `POST` | `/api/v1/auth/resend-verification?email=` | Resend verification email to a user. |
| `DELETE` | `/api/v1/auth/dev/delete-user?email=` | Remove a user (development/testing utility). |
| `GET` | `/api/v1/profile` | Retrieve the authenticated user's profile. |
| `PUT` | `/api/v1/profile` | Update the authenticated user's profile. |
| `GET` | `/api/v1/courses/by-degree` | List courses filtered by degree type and study year. |
| `GET` | `/api/v1/courses/my-degree` | List courses that match the user's profile. |
| `POST` | `/api/v1/match` | Attempt to match the current user with another student. |
| `GET` | `/api/v1/match` | Fetch the user's current active match. |
| `GET` | `/api/v1/match/all` | Retrieve all matches involving the user. |
| `POST` | `/api/v1/meetings/match/{matchId}` | Schedule a meeting for a specific match. |
| `GET` | `/api/v1/meetings/all` | Fetch every meeting (admin/debug access). |
| `GET` | `/api/v1/meetings/match/{matchId}` | List meetings associated with a match. |
| `DELETE` | `/api/v1/meetings/{meetingId}` | Delete a meeting by its identifier. |
| `POST` | `/api/v1/chat/messages` | Send a chat message within a match. |
| `GET` | `/api/v1/chat/messages/{matchId}` | Retrieve chat history for a match. |

---

## 📄 Documentation

- [📚 Functional Requirements](docs/functional-requirements.md)
- [🛡️ Non-Functional Requirements](docs/nonfunctional-requirements.md)
- [📊 ERD (Entity Relationship Diagram)](docs/StudyBuddyERD.png)

---

## 🧪 Run Locally

```bash
git clone https://github.com/your-username/studybuddy.git
cd studybuddy
./gradlew bootRun

```

---
 © 2025 Amit Or
