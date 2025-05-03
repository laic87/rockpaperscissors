# Rock-Paper-Scissors API

An HTTP API to resolve conflicts using the game Rock, Paper, Scissors. This project is part of a backend assignment, following REST principles and built with Java and Spring Boot.

---

## 🛠 Technical Overview

- **Java 17+**
- **Spring Boot**
- **Gradle**
- **Docker / Docker Compose**
- No database – all game data is stored in memory

---

## 🚀 Run the Application

### With Gradle

```bash
./gradlew bootRun
```

Or build a JAR file and run it:

```bash
./gradlew build
java -jar build/libs/rockpaperscissors-0.0.1-SNAPSHOT.jar
```

---

### With Docker

#### Build Docker image

```bash
docker build -t rockpaperscissors .
```

#### Run the container

```bash
docker run -p 8080:8080 rockpaperscissors
```

#### Alternatively: Docker Compose

```bash
docker-compose up --build
```

The application will then be available at:
```
http://localhost:8080/api/games
```

---

## 📬 API Endpoints

### Create a new game

```http
POST /api/games
```

**curl example:**
```bash
curl -X POST http://localhost:8080/api/games   -H "Content-Type: application/json"   -d '{"name": "Daniel"}'
```

---

### Join a game

```http
POST /api/games/{id}/join
```

**curl example:**
```bash
curl -X POST http://localhost:8080/api/games/{id}/join   -H "Content-Type: application/json"   -d '{"name": "Erik"}'
```

---

### Make a move

```http
POST /api/games/{id}/move
```

**curl example:**
```bash
curl -X POST http://localhost:8080/api/games/{id}/move   -H "Content-Type: application/json"   -d '{"name": "Daniel", "move": "ROCK"}'
```

---

### Get game status

```http
GET /api/games/{id}
```

**curl example:**
```bash
curl http://localhost:8080/api/games/{id}
```

**Response example:**
```json
{
  "id": "c1234567-89ab-cdef-0123-456789abcdef",
  "player1": "Daniel",
  "player2": "Erik",
  "player1Move": "ROCK",
  "player2Move": "SCISSORS",
  "status": "FINISHED",
  "result": "Daniel wins!"
}
```

---

## 📦 Project Structure

```plaintext
src/main/java
├── controller        // REST API
├── service           // Business logic
├── model             // Domain objects
│   └── enums         // Move, GameStatus
├── dto               // GameDTO
├── exception         // Custom exceptions
├── resources/
│   └── application.yml
```

---

## ✅ Tests

Run tests with:

```bash
./gradlew test
```

---

## 📄 License

MIT – freely usable for educational purposes.