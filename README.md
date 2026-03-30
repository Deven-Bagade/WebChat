# WebChat

A Java-based web chat application built using **JSP, Servlets, JDBC, and MySQL** in **Apache NetBeans**.  
This project demonstrates full-stack web development fundamentals, database integration, session management, file handling, and structured backend design using DAO-based classes.

---

## Project Summary

WebChat is a browser-based chat system that allows users to:

- Register and log in securely
- Start one-to-one conversations
- Create and manage group chats
- Send text messages
- Upload and download files in chat
- Track unread messages
- Use session-based user identity across the application

The project is designed as a practical Java web application and is suitable for learning, demonstrations, and academic portfolio use.

---

## Tech Stack

**Frontend**
- HTML
- CSS
- JavaScript
- JSP

**Backend**
- Java
- Servlets
- JDBC
- DAO pattern

**Database**
- MySQL

**Tools / Server**
- Apache NetBeans
- Apache Tomcat
- Ant build system

**Libraries**
- Gson
- MySQL Connector/J
- JSON processing library

---

## Key Features

### 1. User Authentication
- Register new users
- Login with username and password
- Maintain user sessions after login

### 2. Private Messaging
- Send and view direct messages between two users
- Fetch previous chat history
- Display sender-specific conversation view

### 3. Group Chat
- Create or manage chat groups
- Add members to a group
- Send group messages
- View group conversations

### 4. File Sharing
- Upload files while chatting
- Store file metadata in the database
- Download shared files through the application

### 5. Unread Message Tracking
- Identify unread messages
- Show unread senders for a user
- Improve chat usability with message status awareness

### 6. Session-Based Navigation
- Keep the user logged in using HTTP session
- Store active chat receiver information in session
- Support smooth navigation between chat screens

---

## Architecture

The project follows a simple layered structure:

**JSP UI → Servlet Controller → DAO → MySQL Database**

### Main Java Components
- `ServletLR` — handles login and registration
- `Sendmsg` — opens private chat and loads messages
- `MessageHandling` — processes private messages
- `GrpChatMessageHandling` — processes group messages
- `UploadFile` — handles file uploads
- `FileDownloadServlet` — handles file downloads
- `UserDAO`, `MessageDAO`, `GMessageDAO` — database operations
- `DatabaseConnection` — JDBC connection manager

---

## Database Tables Used

Based on the application logic, the main tables include:

- `USR` — stores user details
- `MESSAGE` — stores private and group messages
- `GRP` — stores group chat information
- `FILE` — stores uploaded file details

---

## Skills Demonstrated

This project shows practical knowledge in:

- Java web development
- Server-side programming with Servlets
- JSP page rendering
- MySQL database integration
- JDBC query handling
- DAO design pattern
- Session management
- File upload/download handling
- Group chat workflow design
- Backend logic organization

---

## How to Run the Project

### Prerequisites
- JDK 17 or compatible version
- Apache NetBeans
- Apache Tomcat 10+
- MySQL Server
- MySQL Connector/J

### Setup Steps
1. Clone or extract the project.
2. Open the project in **Apache NetBeans**.
3. Create the MySQL database and required tables.
4. Update the database connection details in `DatabaseConnection.java`.
5. Build the project.
6. Deploy it on Apache Tomcat.
7. Open the application in your browser and register a user.

---



## Why This Project Matters

This project is a strong addition to a student developer profile because it shows more than just UI work. It proves that you can:

- connect frontend and backend
- manage sessions and user flow
- work with SQL databases
- structure code into reusable classes
- build a deployable Java web application

That makes it useful for internships, entry-level backend roles, and campus placements.

---



## Author

Built by Deven Bagade as a practical Java web development project in the second year.

---


