# SecondServe

SecondServe is a food redistribution platform that connects hotel kitchens with NGOs to reduce food waste.

<img width="1920" height="1080" alt="SecondServe home screen" src="https://github.com/user-attachments/assets/58f18f7d-d598-4eca-a57d-495d55d71dc2" />

## Project Structure

```
SecondServe/
├── backend/          Spring Boot REST API
├── frontend/         React + Vite application
├── docker-compose.yml
└── README.md
```

## Tech Stack

| Layer            | Technology              |
|-------------------|--------------------------|
| Frontend          | React + Vite             |
| Backend           | Spring Boot (Java 17)    |
| Database          | MySQL 8                  |
| Authentication    | JWT                      |
| Containerization  | Docker / Docker Compose  |

---

## Quick Start (Recommended: Docker)

You only need **Docker Desktop** and this project — no local Java, Maven, Node.js, or MySQL installs required.

1. **Start Docker Desktop** and confirm it's running:
   ```bash
   docker --version
   docker compose version
   ```

2. **Go to the project folder:**
   ```bash
   cd "D:\SecondServe"
   ```

3. **Build and start everything:**
   ```bash
   docker compose up --build
   ```
   This starts MySQL, the Spring Boot backend, and the React frontend. The first build may take a few minutes.

4. **Wait for the backend to finish starting.** Look for:
   ```
   Started SecondServeServerApplication
   ```
   You'll also see a lot of Spring Security filter logging (`FilterChainProxy`, `SecurityContextHolderFilter`, `OncePerRequestFilter`, etc.) — that's normal, not an error.

5. **Open the app:** [http://localhost:5173](http://localhost:5173)

Once the images are built, you don't need `--build` again — just run `docker compose up`. Only use `--build` after you've changed the project code.

### Application URLs

| Service      | URL                              |
|--------------|-----------------------------------|
| Frontend     | http://localhost:5173             |
| Backend API  | http://localhost:8080/api         |
| MySQL        | localhost:3307 (maps to container port 3306) |

<img width="1920" height="1080" alt="SecondServe dashboard" src="https://github.com/user-attachments/assets/c3885a88-70bf-467f-a126-8b6fa5ba344b" />

---

## Verifying Everything Is Running

```bash
docker ps
```

You should see three containers **Up**: `secondserve-frontend`, `secondserve-backend`, `secondserve-mysql`.

Check logs for either service if something looks wrong:
```bash
docker logs secondserve-backend --tail 100
docker logs secondserve-frontend --tail 100
```

For the backend, a healthy startup ends with `Started SecondServeServerApplication`. If you instead see `APPLICATION FAILED TO START` or `Caused by:`, the backend hit a real startup error — check the log for the underlying cause.

---

## Stopping / Restarting

| Action                              | Command                    |
|--------------------------------------|-----------------------------|
| Stop containers (keep data)          | `docker compose down`      |
| Start again (no rebuild needed)      | `docker compose up`        |
| Reset database (⚠️ deletes all data) | `docker compose down -v` then `docker compose up --build` |

---

## Manual Setup (Without Docker)

Requires Java 17+, Maven, MySQL 8, and Node.js 18+.

**Backend**
```bash
cd backend
```
Set up the database:
```sql
CREATE DATABASE secondserve_db;
CREATE USER 'second_serve'@'localhost' IDENTIFIED BY 'second_serve';
GRANT ALL PRIVILEGES ON secondserve_db.* TO 'second_serve'@'localhost';
```
Run the backend:
```bash
mvn spring-boot:run
```
Available at [http://localhost:8080/api](http://localhost:8080/api).

**Frontend**
```bash
cd frontend
npm install
npm run dev
```
Available at [http://localhost:5173](http://localhost:5173).

---

## Authentication

SecondServe uses JWT authentication. After login, the token is stored in the browser's `localStorage` and sent on subsequent requests as:
```
Authorization: Bearer <token>
```

---

## Project Demonstration Flow

For a walkthrough (e.g. a teacher demo), open [http://localhost:5173](http://localhost:5173) and follow:

**Role Selection → Hotel Manager → Kitchen Staff → NGO**

**1. Hotel Manager**
- Login → Hotel dashboard
- Surplus food management
- View and approve/reject donation requests
- Dashboard statistics

**2. Kitchen Staff**
- Record surplus food, e.g.:
  - Food: Cooked Rice
  - Quantity: 20 portions
  - Description: Surplus food from today's preparation
- Submit the entry

**3. NGO**
- View available surplus food and details
- Request food
- Track request status

---

## Troubleshooting

| Problem | Solution |
|---|---|
| `docker` command not recognized | Install Docker Desktop and make sure it's running. |
| Frontend won't open | Run `docker ps` to confirm the container is `Up`, then check `docker logs secondserve-frontend --tail 100`. |
| Backend container stops or won't start | Run `docker logs secondserve-backend --tail 100` and look for `ERROR`, `APPLICATION FAILED TO START`, or `Caused by:`. |
| Lots of Spring Security log lines | Normal startup logging. Confirm success by looking for `Started SecondServeServerApplication`, not the absence of these lines. |
