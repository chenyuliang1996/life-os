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
- `GET /api/v1/system/operations`
  - 返回容量、SLO、延迟与链路聚合指标（含 `activeSessions` / `activeContexts` / `recentTraceEvents`）
- `GET /api/v1/system/trace-links?limit=20`
  - 返回最近请求链路事件（已持久化到数据库表 `request_trace_events`）
  - 支持过滤参数：`userId`、`sessionId`、`contextId`、`traceId`、`operation`
  - 字段包含 `timestamp`、`operation`、`userId`、`threadId`、`sessionId`、`contextId`、`traceId`、`surface`、`durationMs`
- `GET /api/v1/system/trace-links/query?...`
  - 与 `/trace-links` 等价，便于前端单独挂“查询”入口

### 认证与登录

- `POST /api/v1/auth/register`
  - 请求体：
    - `username`
    - `password`
    - `displayName`
    - `locale`
  - 返回 `token`、`userId`、`username`、`displayName`、`locale`、`expiresAt`
- `POST /api/v1/auth/login`
  - 请求体：
    - `username`
    - `password`
  - 返回同上
- `GET /api/v1/auth/me`
  - Header：`Authorization: Bearer <token>`
  - 返回当前登录用户与会话到期时间
- `POST /api/v1/auth/logout`
  - Header：`Authorization: Bearer <token>`
  - 撤销当前 session

### 安全与 Persona

- `GET /api/v1/personas?locale=zh-CN`
  - 返回 persona 预设，用于多身份模拟
  - 重点字段：
    - `id`（例如 `mbti-intj`）
    - `mbtiType`
    - `temperament`
    - `decisionLens`
    - `preferences`（用于驱动 Agent 个性化执行）
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
    - `sessionId`（可选）
    - `contextId`（可选）
    - `traceId`（可选）
- `POST /api/v1/assistant/resume`
  - 请求体：
    - `userId`
    - `planId`
    - `locale`
    - `sessionId`（可选）
    - `contextId`（可选）
    - `traceId`（可选）
  - 用于在确认流完成后恢复执行

### 用户画像

- `GET /api/v1/profile?userId=lifeos-user`
- `PUT /api/v1/profile?userId=lifeos-user&sessionId=...&contextId=...&traceId=...&surface=toc&locale=zh-CN`
  - 支持带链路参数，写入画像时会同步落轨迹事件
- `GET /api/v1/profile/memory-details?userId=lifeos-user&limit=10`
  - 返回长期记忆明细：
    - `preferences`
    - `goals`
    - `summary`
    - `recentOperations`（最近操作轨迹，来自持久化轨迹表）

### 计划与执行

- `POST /api/v1/plans/preview`
  - 请求体：
    - `userId`
    - `threadId`
    - `input`
    - `locale`
    - `sessionId`（可选）
    - `contextId`（可选）
    - `traceId`（可选）
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

### POI 发现

- `GET /api/v1/poi/festivals?locale=zh-CN&query=...&travelers=...&budget=...&timeWindow=...`
  - 返回节日 POI 聚合卡片
  - 聚合策略：
    - `seeded`（本地种子）
    - `claw-skill`（Claw Skill）
    - `flyai-skill`（FlyAI / MCP）
    - `crawler`（合规抓取端点，按配置可选）

### UX 埋点

- `POST /api/v1/telemetry/ux`
  - 请求体：
    - `action`
    - `surface`
    - `locale`
    - `durationMs`
    - `success`
    - `userId`（可选）
    - `threadId`（可选）
    - `sessionId`（可选）
    - `contextId`（可选）
    - `traceId`（可选）

## 3. AG-UI

- `POST /agui/agents/default/runs`
- `POST /agui/runs`

默认 AG-UI agent 为 `LifeOsAguiAgent`。
