# ⚽ Amateur Football Tournaments Manager / Gestore Tornei di Calcio Amatoriali

*Read this in other languages: [English](#english), [Italiano](#italiano)*

---

<a id="english"></a>
## 🇬🇧 English

> A full-stack web application for managing amateur football tournaments, featuring team registrations, match tracking, and user authentication.

### 🌟 Features
- **User Authentication:** Secure login and registration using Spring Security.
- **Tournament Management:** Create and manage football tournaments, schedule matches, and track results.
- **Team Management:** Register teams and manage player rosters.
- **Role-based Access Control:** Different views and permissions for admins, team managers, and regular users.
- **Modern UI:** Fast and responsive user interface built with React and Vite.

### 💻 Tech Stack
- **Backend:** Java 17, Spring Boot 3, Spring Data JPA (Hibernate), Spring Security, Springdoc OpenAPI.
- **Frontend:** React 19, TypeScript, Vite, Axios.
- **Database:** PostgreSQL.
- **Build & Tooling:** Maven, npm, ESLint.

### 🏗 Architecture
The project follows a standard **Layered Architecture** for the backend:
- `Controller Layer`: Handles HTTP requests and REST API endpoints.
- `Service Layer`: Contains business logic and orchestrates data flow.
- `Repository Layer`: Interfaces with the PostgreSQL database using Spring Data JPA.
- `Model Layer`: Defines JPA entities representing the database schema.
- `Authentication Layer`: Manages security and user sessions.

The frontend is a **Single Page Application (SPA)** built with React, communicating with the backend via RESTful APIs.

### 🚀 Setup & Installation

**Prerequisites**
- Java 17 or higher
- Node.js 20+
- PostgreSQL
- Maven

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/calcio-nuovo.git
cd calcio-nuovo
```

**2. Database Configuration**
Ensure PostgreSQL is running. Create a database named `tornei_calcio` and update the credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tornei_calcio
spring.datasource.username=your_username
spring.datasource.password=your_password
```

**3. Start the Backend (Spring Boot)**
```bash
./mvnw spring-boot:run
```
The backend API will be available at `http://localhost:8080`.

**4. Start the Frontend (React)**
Open a new terminal window:
```bash
cd frontend
npm install
npm run dev
```
The application will be accessible at `http://localhost:5173`.

### 📖 Usage
1. **Register/Login:** Create a new account or log in with existing credentials.
2. **Create a Tournament:** Admins can create a new tournament specifying dates and rules.
3. **Register Teams:** Team managers can enroll their teams into open tournaments.
4. **Manage Matches:** Input match results to update the tournament leaderboard automatically.

### 🔮 Future Improvements
- [ ] **Dockerization:** Add `docker-compose.yml` for zero-config local setup.
- [ ] **CI/CD Pipeline:** Implement GitHub Actions for automated testing and deployment.
- [ ] **Live Scoring:** Integrate WebSockets for real-time match score updates.
- [ ] **Automated Testing:** Increase unit test coverage for backend services and frontend components.

---

<a id="italiano"></a>
## 🇮🇹 Italiano

> Un'applicazione web full-stack per la gestione di tornei di calcio amatoriali, con registrazione squadre, tracciamento partite e autenticazione utenti.

### 🌟 Funzionalità (Features)
- **Autenticazione Utente:** Login e registrazione sicuri tramite Spring Security.
- **Gestione Tornei:** Creazione e gestione di tornei, programmazione partite e monitoraggio risultati.
- **Gestione Squadre:** Iscrizione delle squadre e gestione dei giocatori (roster).
- **Controllo Accessi basato su Ruoli (RBAC):** Viste e permessi differenti per admin, manager di squadra e utenti normali.
- **UI Moderna:** Interfaccia utente veloce e responsiva sviluppata con React e Vite.

### 💻 Stack Tecnologico
- **Backend:** Java 17, Spring Boot 3, Spring Data JPA (Hibernate), Spring Security, Springdoc OpenAPI.
- **Frontend:** React 19, TypeScript, Vite, Axios.
- **Database:** PostgreSQL.
- **Build & Strumenti:** Maven, npm, ESLint.

### 🏗 Architettura
Il progetto segue una **Layered Architecture** standard per il backend:
- `Controller Layer`: Gestisce le richieste HTTP e gli endpoint API REST.
- `Service Layer`: Contiene la logica di business e orchestra il flusso di dati.
- `Repository Layer`: Si interfaccia con il database PostgreSQL tramite Spring Data JPA.
- `Model Layer`: Definisce le entità JPA che rappresentano lo schema del database.
- `Authentication Layer`: Gestisce la sicurezza e le sessioni utente.

Il frontend è una **Single Page Application (SPA)** costruita con React, che comunica con il backend tramite API RESTful.

### 🚀 Setup e Installazione

**Prerequisiti**
- Java 17 o superiore
- Node.js 20+
- PostgreSQL
- Maven

**1. Clona il repository**
```bash
git clone https://github.com/yourusername/calcio-nuovo.git
cd calcio-nuovo
```

**2. Configurazione Database**
Assicurati che PostgreSQL sia in esecuzione. Crea un database chiamato `tornei_calcio` e aggiorna le credenziali in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tornei_calcio
spring.datasource.username=tuo_username
spring.datasource.password=tua_password
```

**3. Avvia il Backend (Spring Boot)**
```bash
./mvnw spring-boot:run
```
Le API del backend saranno disponibili su `http://localhost:8080`.

**4. Avvia il Frontend (React)**
Apri una nuova finestra del terminale:
```bash
cd frontend
npm install
npm run dev
```
L'applicazione sarà accessibile su `http://localhost:5173`.

### 📖 Utilizzo
1. **Registrazione/Login:** Crea un nuovo account o accedi con le tue credenziali.
2. **Crea un Torneo:** Gli amministratorori possono creare un nuovo torneo specificando date e regole.
3. **Iscrivi Squadre:** I manager delle squadre possono iscrivere i propri team ai tornei aperti.
4. **Gestione Partite:** Inserisci i risultati delle partite per aggiornare automaticamente la classifica del torneo.

### 🔮 Sviluppi Futuri
- [ ] **Dockerizzazione:** Aggiunta di un file `docker-compose.yml` per un setup locale senza configurazioni manuali.
- [ ] **CI/CD Pipeline:** Implementazione di GitHub Actions per test e deploy automatizzati.
- [ ] **Risultati in tempo reale (Live Scoring):** Integrazione di WebSockets per aggiornare i risultati delle partite in real-time.
- [ ] **Test Automatici:** Aumentare la copertura dei test unitari per i servizi backend e i componenti frontend.
