# Life OS API 契约

## 1. 页面入口

- `GET /`
  - 中文页面
- `GET /en/index.html`
  - 英文页面

## 2. REST API

### 系统与架构

- `GET /api/v1/modules`
  - 触发各模块 `executeDemo()`，验证模块执行入口
- `GET /api/v1/system/connectors`
  - 返回工具层连接器能力
- `GET /api/v1/system/health`
  - 服务健康
- `GET /api/v1/system/architecture`
  - 返回部署模式、持久化模式、数据库、RAG 存储和 session 策略

### 运行时

- `GET /api/v1/assistant/runtime`
  - 返回当前 runtime
  - 可能值：
    - `orchestrator-fallback`
    - `agentscope-react`
- `POST /api/v1/assistant/message`
  - 请求体：
    - `userId`
    - `threadId`
    - `input`
    - `locale`

### 用户画像

- `GET /api/v1/profile?userId=demo-user`
- `PUT /api/v1/profile?userId=demo-user`

### 计划与执行

- `POST /api/v1/plans/preview`
  - 请求体：
    - `userId`
    - `threadId`
    - `input`
    - `locale`
- `GET /api/v1/plans`
- `GET /api/v1/plans/{planId}`
- `GET /api/v1/executions/{runId}`
- `GET /api/v1/executions/{runId}/timeline`

### 确认流

- `GET /api/v1/confirmations`
- `POST /api/v1/confirmations/{id}/decision`

### 知识库

- `GET /api/v1/knowledge/documents`
- `POST /api/v1/knowledge/documents`
  - 请求体：
    - `title`
    - `sourceType`
    - `tags`
    - `summary`
    - `content`
    - `locale`

## 3. AG-UI

- `POST /agui/agents/default/runs`
- `POST /agui/runs`

默认 AG-UI agent 为 `LifeOsAguiAgent`。
