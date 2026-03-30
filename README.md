# Life OS

`Life OS` is a ToC demo scaffold that combines AgentScope Java's multi-agent orchestration, planning, memory, RAG, tools, and AG-UI integration into a consumer-facing "personal life operating system".

## Modules

- `life-os-domain`: shared contracts, DTOs, enums, repository interfaces
- `life-os-memory`: user profile and session-oriented memory facade
- `life-os-rag`: lightweight knowledge retrieval facade
- `life-os-tools`: mock tool layer for search, weather, calendar, reminder
- `life-os-agents`: specialist agents for travel, learning, and scheduling
- `life-os-orchestrator`: the main orchestrator that builds plans and confirmation steps
- `life-os-infra`: in-memory repositories and shared infrastructure beans
- `life-os-web`: Spring Boot application, REST API, startup demos

## Design Notes

- Each core module includes an explicit executable entry, either `execute(...)` or `executeDemo()`.
- The first iteration uses deterministic mock outputs so the architecture can be exercised before wiring a real LLM and external tools.
- AgentScope dependencies are already declared in the parent POM and `life-os-web`, so the scaffold is ready for AG-UI and ReAct agent registration.

## Run

```bash
mvn -DskipTests install
mvn -f life-os-web/pom.xml spring-boot:run
```

Open `http://127.0.0.1:8080/` for the dashboard.

## Demo Highlights

- Every module has an executable facade via `execute(...)` or `executeDemo()`.
- `LifeOsAguiAgent` is registered as the default AG-UI agent.
- The dashboard now supports assistant replies, runtime status, plan history, confirmation approval, profile editing, and knowledge creation.
- The default path is deterministic and works without API keys, but the runtime can switch to a real AgentScope `ReActAgent` when `OPENAI_API_KEY` or Ollama settings are provided.
