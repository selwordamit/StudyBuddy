# 📚 StudyBuddy – Smart Study Matching App

StudyBuddy is a social learning platform that connects students from the same institution and courses based on shared learning styles and academic goals. It helps them match, chat, coordinate study sessions (online or in-person), and rate each other after meetings.

---

## 🚀 Main Technologies

- **Backend**: Spring Boot (Java) + Spring Security + JWT  
- **Database**: PostgreSQL + JPA/Hibernate  
- **Frontend**: React (coming soon)  
- **Real-time**: WebSocket for group chat  
- **Auth**: Academic email verification + JWT-based authentication  
- **External APIs**: Zoom API, Email Service  
- **DevOps**: Docker + Swagger + CI/CD pipelines  

---

## 🛠️ Key Features

- Sign up using academic email only  
- Create a personal profile with selected courses and learning preferences  
- Automatic matching based on course and preferred group size  
- Real-time group chat and scheduling of online/physical study sessions  
- Automatic meeting reminders via email or in-app notifications  
- Post-meeting peer rating (community feedback displayed on profiles)

---

## 🧪 Run Locally

```bash
git clone https://github.com/your-username/studybuddy.git
cd studybuddy
./gradlew bootRun
