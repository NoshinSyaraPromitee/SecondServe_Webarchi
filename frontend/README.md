# SecondServe – React Frontend

A minimal React replacement for the old JavaFX desktop client. Talks to the
existing Spring Boot backend (`secondserve-server`) over REST — no backend
changes needed.

## Setup

```bash
npm install
npm run dev
```

Then open http://localhost:5173. Make sure the Spring Boot server is running
on http://localhost:8080 first (context path `/api`).

## What's included

- **Role select screen** – choose Hotel Manager, Kitchen Staff, or NGO
- **Login / Signup** for all three roles (hits `/auth/login`, `/hotels/register`,
  `/ngos/register`, `/staff/register`)
- **Hotel dashboard** – weekly stats, leftovers pending review (approve/reject),
  pending NGO donation requests (approve/reject/complete)
- **Kitchen staff page** – simple form to log surplus food
- **NGO portal** – browse available food, request items, track request status

## Structure

```
src/
  api.js      – fetch wrapper + all backend calls + localStorage session helper
  App.jsx     – routing and every page/component (kept in one file for simplicity)
  index.css   – all styling (no UI framework, just plain CSS)
```

## Notes

- Auth token is stored in `localStorage` and sent as `Authorization: Bearer <token>`.
- The backend's CORS config already allows `http://localhost:5173` (Vite's default dev port).
