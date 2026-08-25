# ⚽ Amateur Football Tournaments Manager

> A full-stack web application for managing amateur football tournaments, featuring team registrations, match tracking, and user authentication.

## 🌟 Features
- **User Authentication:** Secure login and registration using Spring Security.
- **Tournament Management:** Create and manage football tournaments, schedule matches, and track results.
- **Team Management:** Register teams and manage player rosters.
- **Role-based Access Control:** Different views and permissions for admins, team managers, and regular users.
- **Modern UI:** Fast and responsive user interface built with React and Vite.

## 💻 Tech Stack
- **Backend:** Java 17, Spring Boot 3, Spring Data JPA (Hibernate), Spring Security, Springdoc OpenAPI.
- **Frontend:** React 19, TypeScript, Vite, Axios.
- **Database:** PostgreSQL.
- **Build & Tooling:** Maven, npm, ESLint.

## 🏗 Architecture
The project follows a standard **Layered Architecture** for the backend:
- `Controller Layer`: Handles HTTP requests and REST API endpoints.
- `Service Layer`: Contains business logic and orchestrates data flow.
- `Repository Layer`: Interfaces with the PostgreSQL database using Spring Data JPA.
- `Model Layer`: Defines JPA entities representing the database schema.
- `Authentication Layer`: Manages security and user sessions.

The frontend is a **Single Page Application (SPA)** built with React, communicating with the backend via RESTful APIs.

## 🚀 Setup & Installation

### Prerequisites
- Java 17 or higher
- Node.js 20+
- PostgreSQL
- Maven

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/calcio-nuovo.git
cd calcio-nuovo
```

### 2. Database Configuration
Ensure PostgreSQL is running. Create a database named `tornei_calcio` and update the credentials in `src/main/resources/application.properties` if necessary:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tornei_calcio
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Start the Backend (Spring Boot)
```bash
./mvnw spring-boot:run
```
The backend API will be available at `http://localhost:8080`.

### 4. Start the Frontend (React)
Open a new terminal window:
```bash
cd frontend
npm install
npm run dev
```
The application will be accessible at `http://localhost:5173`.

## 📖 Usage
1. **Register/Login:** Create a new account or log in with existing credentials.
2. **Create a Tournament:** Admins can create a new tournament specifying dates and rules.
3. **Register Teams:** Team managers can enroll their teams into open tournaments.
4. **Manage Matches:** Input match results to update the tournament leaderboard automatically.

## 🔮 Future Improvements
- [ ] **Dockerization:** Add `docker-compose.yml` for zero-config local setup.
- [ ] **CI/CD Pipeline:** Implement GitHub Actions for automated testing and deployment.
- [ ] **Live Scoring:** Integrate WebSockets for real-time match score updates.
- [ ] **Automated Testing:** Increase unit test coverage for backend services and frontend components.


