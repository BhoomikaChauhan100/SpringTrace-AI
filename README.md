# 🔍 SpringTrace AI

### AI-Powered Code Debugging Assistant

SpringTrace AI is a Java-based debugging assistant built with Spring Boot and BharatCode Qwen AI. It helps developers identify potential code issues, understand their causes, and receive debugging suggestions.

It combines local pattern-based analysis with AI-powered diagnosis to make debugging easier, especially for students and beginner developers.

## 🚀 Features

- **AI Code Analysis:** Uses BharatCode Qwen AI to analyze submitted code.
- **Local Debugging:** Detects selected suspicious code patterns without an API key.
- **Error Explanation:** Helps users understand potential bugs and their causes.
- **Fix Suggestions:** Provides possible solutions and debugging guidance.
- **Analysis History:** Stores previous analysis records in MySQL.
- **Secure API Handling:** Keeps API credentials in backend environment variables.
- **Clean Results:** Displays formatted and sanitized Markdown responses.

## 🔄 How It Works

1. User submits source code through the web interface.
2. Spring Boot backend receives the request.
3. Local analyzer checks selected suspicious patterns.
4. BharatCode API optionally provides AI-powered diagnosis.
5. Backend processes and sanitizes the response.
6. Results are displayed on the interface.
7. Analysis history is stored in MySQL.

## 🏗️ Architecture

```text
       User
        |
        v
   Web Interface
        |
        v
  Spring Boot Backend
        |
        v
   Analysis Service
      /       \
     v         v
 Local       BharatCode
 Analyzer       API
     \         /
      v       v
   Analysis Result
        |
        v
      MySQL
```

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot 3.5.7 | Backend framework |
| Spring MVC | Request handling |
| Spring Data JPA | Database operations |
| MySQL | Data storage |
| BharatCode Qwen AI | AI-powered debugging |
| Thymeleaf | Frontend rendering |
| HTML, CSS, JavaScript | User interface |
| Maven | Build management |
| JUnit | Testing |

## 📂 Project Structure

```text
src/main/java/com/springtrace/ai/
├── config/
├── controller/
├── dto/
├── entity/
├── repository/
└── service/

src/main/resources/
├── static/
│   ├── css/
│   └── js/
├── templates/
└── application.properties

src/test/
pom.xml
README.md
```

## ⚙️ Installation & Setup

### Requirements

- JDK 21 or newer
- Maven 3.9+
- MySQL Server
- BharatCode API key (optional for local analysis)

### 1. Clone the Repository

```bash
git clone https://github.com/BhoomikaChauhan100/SpringTrace-AI.git
cd SpringTrace-AI
```

### 2. Create MySQL Database

```sql
CREATE DATABASE spring_trace_ai;
```

### 3. Configure Environment Variables

Run these commands in Windows PowerShell:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/spring_trace_ai"
$env:DB_USERNAME="YOUR_MYSQL_USERNAME"
$env:DB_PASSWORD="YOUR_MYSQL_PASSWORD"
$env:BHARATCODE_API_KEY="YOUR_API_KEY"
```

Replace the placeholder values with your credentials.

### 4. Build and Run

```powershell
mvn clean test
mvn spring-boot:run
```

Open your browser:

**http://localhost:8080**

## 🤖 BharatCode API Integration

SpringTrace AI uses the BharatCode OpenAI-compatible Chat Completions API.

| Configuration | Value |
|---|---|
| Base URL | https://bharatcode.ai/api/model/v1 |
| Endpoint | /chat/completions |
| Model | qwen-3.8-27b |
| Authentication | Bearer Token |

The API key is accessed only by the Java backend through environment variables.

**Local mode:** The application can perform selected pattern-based checks without an API key. Results are labelled `LOCAL`.

## 🗄️ Database

MySQL stores previous analysis records through Spring Data JPA.

The `AnalysisHistory` entity stores submitted input, analysis results, language, issue type, analysis mode, and creation time.

## 🧪 Testing

Run the test suite:

```powershell
mvn clean test
```

Tests cover application startup and local analyzer patterns.

## ⚠️ Limitations

- Local checks are pattern-based and may miss bugs or produce false positives.
- Submitted code is not compiled or executed by the local analyzer.
- AI results depend on BharatCode API availability and model quality.
- AI suggestions should be reviewed before use.

## 🔮 Future Scope

- Support for more programming languages.
- Advanced static code analysis.
- Secure code execution sandbox.
- More detailed error detection.
- Automatic code correction suggestions.
- GitHub repository integration.

## 👩‍💻 Developer

**Bhoomika Chauhan**

BCA Student | Java Backend Developer

GitHub: [BhoomikaChauhan100](https://github.com/BhoomikaChauhan100)

---

**Built with Java, Spring Boot, MySQL and BharatCode Qwen AI.**
