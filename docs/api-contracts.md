# Life OS API Contracts

## REST

- `GET /`
  - Serves the demo dashboard UI.
- `GET /api/v1/modules`
  - Runs module-level `executeDemo()` methods and returns a summary per module.
- `GET /api/v1/assistant/runtime`
  - Returns the current runtime mode (`orchestrator-fallback` or `agentscope-react`).
- `POST /api/v1/assistant/message`
  - Runs either the deterministic fallback or the model-backed ReAct runtime and returns a consolidated reply.
- `GET /api/v1/profile`
  - Returns the current user profile snapshot.
- `PUT /api/v1/profile`
  - Updates long-term preferences and goals.
- `POST /api/v1/plans/preview`
  - Runs the orchestrator and returns plan, execution timeline, contributions, and pending confirmations.
- `GET /api/v1/plans`
  - Lists stored plans filtered by status.
- `GET /api/v1/plans/{planId}`
  - Returns a stored plan by id.
- `GET /api/v1/executions/{runId}`
  - Returns execution metadata for a specific run.
- `GET /api/v1/executions/{runId}/timeline`
  - Returns only the execution timeline events.
- `GET /api/v1/confirmations`
  - Lists pending confirmation requests.
- `POST /api/v1/confirmations/{id}/decision`
  - Updates the confirmation status to `APPROVE` or `REJECT`.
- `GET /api/v1/knowledge/documents`
  - Lists seeded or uploaded knowledge documents.
- `POST /api/v1/knowledge/documents`
  - Adds a knowledge document record.
- `GET /api/v1/system/connectors`
  - Lists mock connector availability.
- `GET /api/v1/system/health`
  - Basic service health.

## AG-UI

- `POST /agui/runs`
- `POST /agui/agents/default/runs`

The default AG-UI agent is `LifeOsAguiAgent`, which wraps the orchestrator and stores the latest response in AgentScope `JsonSession`.
