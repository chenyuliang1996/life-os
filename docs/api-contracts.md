# Life OS API 契约

## 1. 页面入口

- `GET /`
  - 中文页面
- `GET /en/index.html`
  - 英文页面

## 2. REST API

### 系统与架构

- `GET /api/v1/modules`
  - 触发各模块 `executeProbe()`，返回只读探针摘要
- `GET /api/v1/system/connectors`
  - 返回工具层连接器能力
- `GET /api/v1/system/health`
  - 服务健康
- `GET /api/v1/system/architecture`
  - 返回部署模式、持久化模式、数据库、RAG 存储、session 策略、旅行搜索模式、旅行专家模式
- `GET /api/v1/system/rag`
  - 返回当前 RAG 运行态
  - 包含是否启用向量、检索模式、provider、model、summary

### 安全与 Persona

- `GET /api/v1/personas?locale=zh-CN`
  - 返回 persona 预设，用于多身份模拟
- `GET /api/v1/security/overview?userId=persona-travel&limit=8`
  - 返回当前身份的信任状态、工具权限和最近审计流水
- `GET /api/v1/security/audit?userId=persona-travel&limit=12`
  - 返回当前身份的审计流水
  - 运营视角可省略 `userId`，查看全局审计流水

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
- `POST /api/v1/assistant/resume`
  - 请求体：
    - `userId`
    - `planId`
    - `locale`
  - 用于在确认流完成后恢复执行

### 用户画像

- `GET /api/v1/profile?userId=lifeos-user`
- `PUT /api/v1/profile?userId=lifeos-user`

### 计划与执行

- `POST /api/v1/plans/preview`
  - 请求体：
    - `userId`
    - `threadId`
    - `input`
    - `locale`
- `GET /api/v1/plans`
  - 必须带 `userId`
- `GET /api/v1/plans/{planId}`
  - 必须带 `userId`
- `GET /api/v1/executions/{runId}`
- `GET /api/v1/executions/{runId}/timeline`

### 确认流

- `GET /api/v1/confirmations`
  - 必须带 `userId`
- `POST /api/v1/confirmations/{id}/decision`
  - 必须带 `userId`

### 知识库

- `GET /api/v1/knowledge/documents`
  - 必须带 `userId`
- `POST /api/v1/knowledge/documents`
  - 必须带 `userId`
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
