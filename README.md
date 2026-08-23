# SecondServe

SecondServe is a food redistribution platform that connects hotel kitchens
with NGOs to reduce food waste.

The project contains:

    SecondServe/
    ├── backend/          Spring Boot REST API
    ├── frontend/         React + Vite application
    ├── docker-compose.yml
    └── README.md

------------------------------------------------------------
TECHNOLOGIES
------------------------------------------------------------

Frontend     : React + Vite
Backend      : Spring Boot
Database     : MySQL 8
Authentication: JWT
Containerization: Docker / Docker Compose
Java         : Java 17


============================================================
QUICK START - RECOMMENDED METHOD
============================================================

The easiest way to run the complete project is with Docker.

You only need:

    1. Docker Desktop
    2. This project

You do NOT need to install Java, Maven, Node.js, or MySQL
separately when using Docker.


------------------------------------------------------------
STEP 1 - START DOCKER DESKTOP
------------------------------------------------------------

Open Docker Desktop and make sure it is running.

Check Docker from PowerShell:

    docker --version

Then check Docker Compose:

    docker compose version

Both commands should display version information.


------------------------------------------------------------
STEP 2 - OPEN THE PROJECT FOLDER
------------------------------------------------------------

Open PowerShell and go to the folder containing SecondServe.

Example:

    cd "D:\SecondServe"

Check that you are in the correct folder:

    dir

You should see something similar to:

    backend
    frontend
    docker-compose.yml
    README.md


------------------------------------------------------------
STEP 3 - START THE COMPLETE APPLICATION
------------------------------------------------------------

Run:

    docker compose up --build

Docker will start:

    1. MySQL database
    2. Spring Boot backend
    3. React frontend

The first build may take several minutes.


------------------------------------------------------------
STEP 4 - WAIT FOR THE BACKEND
------------------------------------------------------------

Watch the terminal.

The backend has successfully started when you see:

    Started SecondServeServerApplication

You may also see many Spring Security messages such as:

    FilterChainProxy
    SecurityContextHolderFilter
    OncePerRequestFilter
    DisableEncodeUrlFilter

These messages are not necessarily errors.


------------------------------------------------------------
STEP 5 - OPEN THE APPLICATION
------------------------------------------------------------

Open your browser and go to:

    http://localhost:5173

This is the main SecondServe application.


============================================================
APPLICATION URLS
============================================================

Frontend:

    http://localhost:5173

Backend API:

    http://localhost:8080/api

MySQL:

    localhost:3306


============================================================
HOW TO CHECK IF EVERYTHING IS RUNNING
============================================================

Open another PowerShell window.

Run:

    docker ps

You should see the SecondServe containers running.

Look for containers similar to:

    secondserve-frontend
    secondserve-backend
    secondserve-mysql

Their status should contain:

    Up


------------------------------------------------------------
CHECK BACKEND LOGS
------------------------------------------------------------

Run:

    docker logs secondserve-backend --tail 100

Look for:

    Started SecondServeServerApplication

If you see:

    APPLICATION FAILED TO START

or:

    Caused by:

then the backend has encountered an actual startup error.


------------------------------------------------------------
CHECK FRONTEND LOGS
------------------------------------------------------------

Run:

    docker logs secondserve-frontend --tail 100


============================================================
PROJECT DEMONSTRATION
============================================================

For the teacher demonstration, open:

    http://localhost:5173

Recommended demonstration flow:

    Role Selection
          |
          v
    Hotel Manager
          |
          v
    Kitchen Staff
          |
          v
         NGO


------------------------------------------------------------
1. HOTEL MANAGER
------------------------------------------------------------

Demonstrate:

    - Login
    - Hotel dashboard
    - Surplus food management
    - Donation requests
    - Approving/rejecting requests
    - Statistics


------------------------------------------------------------
2. KITCHEN STAFF
------------------------------------------------------------

Demonstrate how kitchen staff can record surplus food.

Example:

    Food: Cooked Rice
    Quantity: 20 portions
    Description: Surplus food from today's preparation

Submit the food information.


------------------------------------------------------------
3. NGO
------------------------------------------------------------

Demonstrate:

    - Viewing available surplus food
    - Viewing food details
    - Requesting food
    - Viewing request status


============================================================
HOW TO STOP THE PROJECT
============================================================

When the demonstration is finished:

    Press Ctrl + C

Then run:

    docker compose down

This stops the containers without deleting the database volume.


============================================================
HOW TO START IT AGAIN
============================================================

After the project has already been built:

    docker compose up

Then open:

    http://localhost:5173

You do NOT need --build every time.

Use:

    docker compose up --build

when you have changed the project and need Docker to rebuild
the application.


============================================================
RESET THE DATABASE
============================================================

WARNING:
This will delete the Docker database volume and all stored data.

Only use this if you intentionally want a fresh database.

Run:

    docker compose down -v

Then:

    docker compose up --build


============================================================
MANUAL SETUP - WITHOUT DOCKER
============================================================

Docker is recommended.

If you want to run the project manually, you need:

    - Java 17+
    - Maven
    - MySQL 8
    - Node.js 18+


------------------------------------------------------------
BACKEND
------------------------------------------------------------

Go to:

    cd backend

Create the database:

    CREATE DATABASE secondserve_db;

Create the database user:

    CREATE USER 'second_serve'@'localhost'
    IDENTIFIED BY 'second_serve';

Grant permissions:

    GRANT ALL PRIVILEGES ON secondserve_db.*
    TO 'second_serve'@'localhost';

Start Spring Boot:

    mvn spring-boot:run

Backend:

    http://localhost:8080/api


------------------------------------------------------------
FRONTEND
------------------------------------------------------------

Open another terminal.

Go to:

    cd frontend

Install dependencies:

    npm install

Start the frontend:

    npm run dev

Frontend:

    http://localhost:5173


============================================================
AUTHENTICATION
============================================================

SecondServe uses JWT authentication.

After login, the JWT token is stored in the browser's
localStorage and sent to the backend using:

    Authorization: Bearer <token>


============================================================
TROUBLESHOOTING
============================================================

PROBLEM:
Docker command is not recognized.

SOLUTION:
Install Docker Desktop and make sure Docker Desktop is running.


PROBLEM:
Frontend does not open.

Check:

    docker ps

Then check:

    docker logs secondserve-frontend --tail 100

Try:

    http://localhost:5173


PROBLEM:
Backend container stops.

Run:

    docker logs secondserve-backend --tail 100

Look for:

    ERROR
    APPLICATION FAILED TO START
    Caused by:


PROBLEM:
There are many Spring Security messages.

Messages such as:

    FilterChainProxy
    SecurityContextHolderFilter
    OncePerRequestFilter

can be normal Spring Security logging.

Look for:

    Started SecondServeServerApplication

to confirm successful startup.


============================================================
QUICK COMMANDS
============================================================

START FOR THE FIRST TIME:

    cd "D:\SecondServe"
    docker compose up --build


START AFTER FIRST BUILD:

    cd "D:\SecondServe"
    docker compose up


CHECK CONTAINERS:

    docker ps


CHECK BACKEND:

    docker logs secondserve-backend --tail 100


CHECK FRONTEND:

    docker logs secondserve-frontend --tail 100


STOP:

    docker compose down


RESET DATABASE:

    docker compose down -v


OPEN APPLICATION:

    http://localhost:5173


============================================================
DEMO SUMMARY
============================================================

    1. Open Docker Desktop
    2. Open PowerShell
    3. cd "D:\SecondServe"
    4. docker compose up --build
    5. Wait for "Started SecondServeServerApplication"
    6. Open http://localhost:5173
    7. Demonstrate Hotel Manager
    8. Demonstrate Kitchen Staff
    9. Demonstrate NGO
   10. Show the complete food redistribution workflow

============================================================
