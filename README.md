# Rock-Paper-Scissors API

Ett HTTP-API för att lösa konflikter genom spelet Sten, Sax, Påse. Detta projekt är en del av en backend-uppgift som följer REST-principer och bygger på Java och Spring Boot.

---

## 🛠 Teknisk översikt

- **Java 17+**
- **Spring Boot**
- **Gradle**
- **Docker / Docker Compose**
- Ingen databas – allt speldata hålls i minnet

---

## 🚀 Starta applikationen

### Med Gradle

```bash
./gradlew bootRun
```

Eller bygg en JAR-fil och kör:

```bash
./gradlew build
java -jar build/libs/rockpaperscissors-0.0.1-SNAPSHOT.jar
```

---

### Med Docker

#### Bygg Docker-image

```bash
docker build -t rockpaperscissors .
```

#### Kör containern

```bash
docker run -p 8080:8080 rockpaperscissors
```

#### Alternativ: Docker Compose

```bash
docker-compose up --build
```

Applikationen är då tillgänglig på:
```
http://localhost:8080/api/games
```

---

## 📬 API-endpoints

### Skapa nytt spel

```http
POST /api/games
```

**curl-exempel:**
```bash
curl -X POST http://localhost:8080/api/games \
  -H "Content-Type: application/json" \
  -d '{"name": "Daniel"}'
```

---

### Anslut till spel

```http
POST /api/games/{id}/join
```

**curl-exempel:**
```bash
curl -X POST http://localhost:8080/api/games/{id}/join \
  -H "Content-Type: application/json" \
  -d '{"name": "Erik"}'
```

---

### Gör ett drag

```http
POST /api/games/{id}/move
```

**curl-exempel:**
```bash
curl -X POST http://localhost:8080/api/games/{id}/move \
  -H "Content-Type: application/json" \
  -d '{"name": "Daniel", "move": "ROCK"}'
```

---

### Hämta spelstatus

```http
GET /api/games/{id}
```

**curl-exempel:**
```bash
curl http://localhost:8080/api/games/{id}
```

**Response-exempel:**
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

## 📦 Projektstruktur

```plaintext
src/main/java
├── controller        // REST-API
├── service           // Affärslogik
├── model             // Domänobjekt
│   └── enums         // Move, GameStatus
├── dto               // GameDTO
├── exception         // Egendefinierade fel
├── resources/
│   └── application.yml
```

---

## ✅ Tester

Kör tester med:

```bash
./gradlew test
```

---

## 📄 Licens

MIT – använd fritt i utbildningssyfte.
