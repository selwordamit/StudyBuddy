# 📘 Functional Requirements – Updated for 1-on-1 StudyBuddy

1. **User Registration and Login**
    - Users can register and log in using email and password.
    - Authentication is done using an academic email address (e.g., `@campus.ac.il`) to verify that the user is a student at a recognized institution.

2. **1-on-1 StudyBuddy Matching by Course**
    - Users can select multiple courses for their profile.
    - For each course, the system automatically matches the user with **exactly one** other student looking for a study partner in the same course.
    - Matching is automatic, based only on course – availability is coordinated later.
    - If no match is available, the user remains in a queue until a partner is found.
    - Once matched:

4. **User Profile Management**
    - Users can update:
        - Degree type (e.g., `BSc in Computer Science`)
        - Institution
        - Year of study
        - Active courses
        - Study goals
    - **4.1 Update Active Courses**
        - Users can modify their list of active courses at any time.

5. **Automatic Reminders(Not in MVP)**
    - Reminders are sent before scheduled study sessions via email or in-app notification, based on user preferences.

6. **Leaving a Match**
    - Users can unmatch (leave the StudyBuddy connection) if needed.
