# SpringTrace AI

A Java/Spring debugging assistant built with Spring Boot, Thymeleaf and BharatCode.

## What works

- Local pattern checks run without an API key. They flag selected risky patterns (for example, unsafe nullable method calls, possible negative withdrawals, common Spring bean errors and common SQL/HTTP clues). These checks are hints, not a compiler or a full static-analysis engine.
- Optional live AI diagnosis through BharatCode's OpenAI-compatible Chat Completions API.
- Markdown diagnosis is rendered as HTML and sanitized before display.
- Persistent analysis history using H2 by default; optional MySQL profile.
- API key is read only from an environment variable and is never sent to the browser.
- Configurable API URL, model, timeouts, temperature and output-token budget.

## Requirements

- JDK 21 or newer
- Maven 3.9+
- BharatCode API key for live AI mode (local checks work without one)

## Run in Windows PowerShell

From the folder containing `pom.xml`:

```powershell
$env:BHARATCODE_API_KEY="YOUR_REAL_KEY"
mvn clean test
mvn spring-boot:run
```

Open http://localhost:8080.

If you do not have an API key, run without setting `BHARATCODE_API_KEY`. SpringTrace will use local pattern checks and label the result `LOCAL`; it will not pretend that a live model was called.

## BharatCode configuration

Create an API key from your BharatCode account and keep it private. The current documented API settings are:

- Base URL: `https://bharatcode.ai/api/model/v1`
- Chat endpoint: `POST /chat/completions`
- Model: `qwen-3.8-27b`
- Authentication: `Authorization: Bearer <API_KEY>`

PowerShell:

```powershell
$env:BHARATCODE_API_KEY="YOUR_REAL_KEY"
$env:BHARATCODE_MODEL="qwen-3.8-27b"
$env:BHARATCODE_MAX_TOKENS="6000"
$env:BHARATCODE_READ_TIMEOUT="90s"
mvn spring-boot:run
```

The key is read by the Java backend only. Never paste it into HTML, JavaScript, screenshots, GitHub, or `application.properties`.

Optional environment variables:

| Variable | Default | Purpose |
|---|---|---|
| `BHARATCODE_ENABLED` | `true` | Enable/disable remote AI calls |
| `BHARATCODE_API_KEY` | empty | Secret API key |
| `BHARATCODE_BASE_URL` | `https://bharatcode.ai/api/model/v1` | API base URL |
| `BHARATCODE_CHAT_PATH` | `/chat/completions` | Chat endpoint path |
| `BHARATCODE_MODEL` | `qwen-3.8-27b` | Model ID |
| `BHARATCODE_CONNECT_TIMEOUT` | `10s` | Connection timeout |
| `BHARATCODE_READ_TIMEOUT` | `90s` | Response timeout |
| `BHARATCODE_MAX_TOKENS` | `6000` | Maximum response tokens |
| `BHARATCODE_TEMPERATURE` | `0.2` | Response randomness |

## Database

H2 file database is the default; data is stored in `./data/` and ignored by Git.

For MySQL, activate the `mysql` profile and provide `DB_URL`, `DB_USERNAME` and `DB_PASSWORD`. Do not commit database passwords.

## Tests

```powershell
mvn clean test
```

The test suite includes application-context startup and local-analyzer pattern tests.

## Known limitations / next engineering steps

- Local checks are deliberately conservative and pattern-based; they can miss bugs and may produce false positives. They do not compile or execute submitted code.
- Live model quality depends on the selected BharatCode model, API access, queue availability and the quality of the submitted context.
- Before public deployment, add authentication/rate limiting, request-size controls at the reverse proxy, retention controls for saved source code, and operational monitoring.
