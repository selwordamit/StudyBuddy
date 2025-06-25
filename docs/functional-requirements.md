 # Functional Requirements

1. **User Registration and Login**
    - Users can register and log in using email and password.
    - Authentication is done using an academic email address (e.g., `@campus.ac.il`) to verify that the user is a student at a recognized institution.

2. **Matching or Study Group Creation by Course**
    - Users can select multiple courses for their profile.
    - For each course, the user specifies whether they prefer a study partner (total of 2) or a group of 3.
    - The system will match accordingly:
        - If "2" is selected → a pair match is created.
        - If "3" is selected → a study group is formed.
    - Availability is **not** part of the initial match; it is set later by users.
    - Each match or group includes its own chat, meeting coordination, and rating section for the specific course.

3. **Meeting Scheduling via Chat**
    - After matching, users can communicate in a real-time group chat.
    - Through the chat, users can coordinate when and how to meet.
    - They can generate a Zoom link or manually set a physical meeting location.

4. **User Profile Management**
    - Users can update:
        - Degree type (e.g., `BSc in Computer Science`)
        - Institution
        - Year of study
        - Active courses
        - Study goals
        - Preferred learning style (visual, verbal, group, etc.)
    - **4.1 Update Active Courses**
        - Users can update their list of active courses at any time, depending on the academic year and semester.

5. **Study Groups**
    - Users can join study groups and collaborate via group chat (video support planned later).

6. **Automatic Reminders**
    - Reminders are sent before study sessions via email or in-app message, depending on user preferences.

7. **Group Management**
    - Users can leave a group at any time.
    - It is possible to report disruptive or inappropriate users.

8. **Meeting Options**
    - Users can choose between **online** or **in-person** meetings when scheduling a session.
    - For online: the system generates a Zoom link automatically.
    - For physical: users add location and time manually.

9. **User Rating After Meeting**
    - After each meeting, users can like or rate other group members.
    - Positive feedback is shown on the user's profile as community reputation.

10. **Real-Time Group Chat**
- Group members can chat in real-time using **WebSocket**.
- Messages are stored in the database.
- The chat is available **only** to group members (not public).

---