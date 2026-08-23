# SecondServe

A platform connecting hotel kitchens with surplus food to NGOs that can
redistribute it. This repo contains both halves of the app:

```
SecondServe/
  backend/    Spring Boot REST API (Java, MySQL, JWT auth)
  frontend/   React client (Vite)
```

## Option 1: Run everything with Docker (easiest)

Requires only [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running. No need for local Java, Maven, Node, or MySQL.

```bash
docker compose up --build
```

This starts three containers:
- `mysql` — MySQL 8, with the `secondserve_db` database and `second_serve` user created automatically
- `backend` — the Spring Boot API, built from source, available at `http://localhost:8080/api`
- `frontend` — the React app, built and served via nginx, available at `http://localhost:5173`

Open **http://localhost:5173** in your browser once it's up (first build takes a few minutes; watch the logs for `Started SecondServeServerApplication`).

To stop everything:
```bash
docker compose down
```

To stop and also wipe the database:
```bash
docker compose down -v
```

## Option 2: Run manually (without Docker)

### 1. Start the backend

Requires Java 17+ and a running MySQL instance.

```bash
cd backend
```

Create the database and user (matches `src/main/resources/application.properties`):

```sql
CREATE DATABASE secondserve_db;
CREATE USER 'second_serve'@'localhost' IDENTIFIED BY 'second_serve';
GRANT ALL PRIVILEGES ON secondserve_db.* TO 'second_serve'@'localhost';
```

Then run the server with Maven (there's no `mvnw` wrapper included, so you'll
need Maven installed — `mvn -v` to check, or grab it from
https://maven.apache.org/download.cgi):

```bash
mvn spring-boot:run
```

The API comes up at `http://localhost:8080/api`.

### 2. Start the frontend

Requires Node.js 18+.

```bash
cd frontend
npm install
npm run dev
```

The app opens at `http://localhost:5173` and talks to the backend at
`http://localhost:8080/api` (see `frontend/src/api.js`). CORS is already
configured on the backend to allow this origin.

## Notes

- JWT is stored in the browser's `localStorage` after login and sent as
  `Authorization: Bearer <token>` on authenticated requests.
- If you deploy the backend somewhere other than `localhost:8080`, update
  `BASE_URL` in `frontend/src/api.js`.
- Each subfolder has its own README with more detail specific to that half.
