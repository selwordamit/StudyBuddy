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
        - A 1-on-1 private chat is created.
        - Users can schedule meetings.
        - They can rate each other after studying together.

3. **Meeting Scheduling via Chat**
    - After matching, users communicate in a real-time private chat.
    - They coordinate when and how to meet using the chat interface.
    - Users can generate a Zoom link or manually set a physical location.

4. **User Profile Management**
    - Users can update:
        - Degree type (e.g., `BSc in Computer Science`)
        - Institution
        - Year of study
        - Active courses
        - Study goals
        - Preferred learning style (visual, verbal, group, etc.)
    - **4.1 Update Active Courses**
        - Users can modify their list of active courses at any time.

5. **Automatic Reminders**
    - Reminders are sent before scheduled study sessions via email or in-app notification, based on user preferences.

6. **Leaving a Match**
    - Users can unmatch (leave the StudyBuddy connection) if needed.
    - Option to report inappropriate behavior.

7. **Meeting Options**
    - Meetings can be scheduled either **online** or **in-person**.
    - Online: the system auto-generates a Zoom link.
    - In-person: users enter location and time manually.

8. **User Rating After Meeting**
    - After each meeting, users can rate each other (1–5) and optionally leave feedback.
    - Positive ratings are reflected on user profiles to indicate community reputation.

9. **Real-Time Chat**
    - Each StudyBuddy pair has a private chat via **WebSocket**.
    - Messages are saved to the database.
    - Only matched users can access their chat.
