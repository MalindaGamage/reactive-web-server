# ⚡ Reactive Web Server with Spring WebFlux

> A blazing-fast, non-blocking, reactive web server that streams real-time stock price updates using Spring Boot WebFlux and Java 21.

---

## 📌 Overview

This project demonstrates how to build an end-to-end **Reactive Web Server** using:

- **Java 21**
- **Spring Boot 3.2.x**
- **Spring WebFlux**
- **Reactor (Project Reactor)**
- **Netty Server (Non-blocking I/O)**

The application simulates a real-world **financial trading platform**, streaming stock prices to thousands of concurrent users in real time with **zero thread contention**.

---

## 🧠 Key Concepts Covered

| Concept                 | Description                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| 🔁 Reactive Programming | Non-blocking, asynchronous event-driven flow using `Mono` and `Flux`         |
| 🔗 Backpressure         | Server adapts to clients' ability to consume data, preventing overload       |
| 🧵 Thread Efficiency    | One thread can handle thousands of connections using Netty                   |
| 🧪 SSE Streaming        | Real-time updates using Server-Sent Events (`text/event-stream`)             |
| 📐 Functional Composition | Declarative data flow using Reactor's stream operators                     |
| ♻️ Hot Publisher Sharing | Efficient broadcasting of stream to multiple clients using `share()`       |

---

## 📁 Project Structure

```text
reactive-web-server/
├── src/
│   └── main/
│       ├── java/com/example/reactivewebserver/
│       │   ├── ReactiveWebServerApplication.java  # Boot app entry
│       │   ├── controller/
│       │   │   └── StockController.java           # REST API for streaming
│       │   ├── service/
│       │   │   └── StockPriceService.java         # Emits live prices
│       │   └── model/
│       │       └── StockPrice.java                # StockPrice data model
│       └── resources/
│           └── application.yml                    # Server config
├── pom.xml                                         # Maven setup
└── README.md                                       # Project documentation
```

---

## 🚀 How It Works – Flow of Execution

```text
Client sends GET request to /stream
      ⮕ StockController handles it via WebFlux
          ⮕ Delegates to StockPriceService
              ⮕ Returns a Flux that emits StockPrice every second
                  ⮕ Stream is shared across all clients (hot stream)
                      ⮕ Output sent as SSE (Server-Sent Events)
```

---

### 🧬 Sequence Diagram
https://xventureglobalsolutions-my.sharepoint.com/:u:/g/personal/malinda_g_x-venture_io/EcuFOClXhuxHjwkH-rcPO78Ba96RjDLsLarIKl9QS8KR2A?e=hKDH9G
```mermaid
sequenceDiagram
    participant C as Client
    participant Ctrl as StockController
    participant Svc as StockPriceService

    C->>+Ctrl: GET /stream
    Ctrl->>+Svc: getPriceStream()
    Svc-->>-Ctrl: Flux<StockPrice>
    Ctrl-->>-C: SSE Stream of StockPrice
```

---

## 💡 Example Output (SSE)

```text
data: {"symbol":"AAPL","price":151.32,"time":"2025-04-24T13:00:11.123Z"}
data: {"symbol":"AAPL","price":152.21,"time":"2025-04-24T13:00:12.123Z"}
data: {"symbol":"AAPL","price":149.99,"time":"2025-04-24T13:00:13.123Z"}
```

---

## 🛠️ Setup & Run

### 1. Clone the Project

```bash
git clone https://github.com/your-user/reactive-web-server.git
cd reactive-web-server
```

### 2. Build & Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

### 3. Access the Stream

- Browser: [http://localhost:8080/stream](http://localhost:8080/stream)
- Curl:

```bash
curl http://localhost:8080/stream
```

---

## 🏗️ Technology Stack

| Tech              | Purpose                             |
|------------------|-------------------------------------|
| Java 21          | Latest LTS release with record types |
| Spring Boot 3.2  | Application framework                |
| Spring WebFlux   | Reactive programming support         |
| Project Reactor  | Core reactive library (`Flux`, `Mono`) |
| Netty            | Non-blocking HTTP server             |
| SSE              | Streaming updates via HTTP           |
| Lombok           | Less boilerplate code                |

---

## 📊 Real-World Use Case

A financial platform broadcasting real-time **stock ticker prices**:
- 10,000+ users connected simultaneously
- Users receive per-second price updates
- No thread blocking
- Handles load without increasing resource usage
- Ideal for dashboards, real-time monitors, and alerting systems

---

## 📦 Architecture Overview

```text
Client  <--->  Controller (WebFlux)  <--->  Service (Reactive Stream)  <--->  Event Loop (Netty)
                     ↑                        ↑
             Backpressure Enabled     Reactive Flux of Events
```

---

## 🧪 Test It With Multiple Tabs

Open [http://localhost:8080/stream](http://localhost:8080/stream) in multiple browser tabs.

> All clients will receive updates simultaneously — without duplicating work on the backend.

---

## 📌 Next Steps (Optional Extensions)

- ✅ Add MongoDB with `ReactiveMongoRepository`
- ✅ Expose WebSocket endpoint for 2-way communication
- ✅ Deploy with Docker & Kubernetes
- ✅ Secure endpoints with OAuth2

---

## 👨‍💻 Author

Built by [Your Name] – Expert Java Backend Developer & Reactive Systems Engineer.

---

## 🧾 License

MIT License. Free to use, improve, and build amazing reactive systems.