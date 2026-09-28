# Labour Connect — Full-Stack Demo

A full-stack demonstration application for a construction workforce platform.

## Features
- **Role-Based Registration/Login**: Register as a Client, Labourer, Contractor, or Organization.
- **Find Labour**: Clients can search for labourers by skill and location.
- **Project Management**: Clients can post projects, and Labourers can apply for them.
- **Booking System**: (Demo APIs available) Create bookings for labourers.
- **Payments**: (Demo APIs available) Process simulated payments with generated Transaction IDs.

## Technology Stack
- **Frontend**: HTML5, CSS3, Vanilla JavaScript, Fetch API
- **Backend**: Java 17, Spring Boot, Spring Web, Spring Data JPA
- **Database**: SQLite (`labour_connect.db`)
- **Security**: BCrypt for password hashing, Session-based auth

## How to Run the Project

### 1. Run the Backend
1. Open the terminal and navigate to `labour-connect/backend`.
2. Compile and run using Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The database (`labour_connect.db`) will be created automatically in `labour-connect/database/` and seeded with demo data.

### 2. Run the Frontend
1. The frontend relies on plain HTML/JS and uses `localhost:8080` to communicate with the backend.
2. Open `labour-connect/frontend/index.html` in your web browser. (You can double click the file or serve it with a tool like `Live Server` in VS Code or `python3 -m http.server`).

## Demo Credentials
The application is pre-seeded with the following users (Password for all is `password`):

- **Admin**: `admin@labourconnect.com`
- **Client**: `client@labourconnect.com`
- **Contractor**: `contractor@labourconnect.com`
- **Labourers**: 
  - `labourer1@labourconnect.com`
  - `labourer2@labourconnect.com`

## Future Improvements
- Complete UI for Bookings, Payments, and Admin dashboard pages (APIs are ready).
- Implement WebSockets for real-time messaging.
- Add image uploading for profile pictures.
