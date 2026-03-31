# Life OS 实现细节

## 1. 页面双语

实现方式：

- 页面入口拆成两套：
  - `static/index.html`
  - `static/en/index.html`
- 两套页面共用：
  - `static/app.js`
  - `static/styles.css`
- 前端通过 `data-default-locale` 注入默认语言
- 动态文案统一由 `TRANSLATIONS` 字典驱动
- 后端请求统一带 `locale`

## 2. 编排链路

核心入口：

- `LifeOrchestrator`
- `TravelAgent`
- `LearningAgent`
- `ScheduleAgent`

执行过程：

1. 主控 Agent 接收 `PlanPreviewRequest`
2. 解析 `locale`
3. 更新长期记忆中的最近意图
4. 调用 3 个专家 Agent
5. 合并任务为 `LifePlan`
6. 写入 `ExecutionRun`
7. 生成 `ConfirmationRequest`
8. 返回 `OrchestrationResult`

## 3. 数据库持久化

### 3.1 仓储边界

领域层只保留接口：

- `UserProfileRepository`
- `LifePlanRepository`
- `ConfirmationRequestRepository`
- `ExecutionRunRepository`
- `KnowledgeDocumentRepository`

### 3.2 数据库实现

基础设施层包含：

- JPA Entity
- Spring Data Repository
- Domain Adapter
- JSON 编解码器 `JsonValueCodec`

### 3.3 为什么这样实现

- 领域模型继续保持纯 record，不污染 JPA 注解
- 持久化对象和领域对象解耦，后续换成 MyBatis / jOOQ / 事件存储都容易
- `Map`、`List`、`PlanTask`、`TimelineEvent` 这类结构使用 JSON 列存储，便于快速演进服务数据模型

## 4. RAG 实现

当前实现是“**数据库文本召回 + pgvector 可选增强**”：

- 知识文档先持久化到 `knowledge_documents`
- 文本召回从 `title / summary / content / tags` 做轻量匹配
- 当 `lifeos.rag.vector.enabled=true` 时：
  - 文档会先 chunk
  - 生成 embedding
  - 写入 `PgVectorStore`
- 查询时先走向量召回，再补文本召回
- 如果 pgvector 或 embedding 不可用，自动回退到文本模式

这样实现的原因：

- 先把知识数据真正持久化
- 保持单机和集群都能稳定运行
- 将生产选型真正落到代码，而不是只写在文档里

## 5. FlyAI 搜索增强

实现方式：

- `ToolModuleFacade` 的 `search` 工具统一收口到 `FlyAiSearchConnector`
- `FlyAiSearchConnector` 使用 `McpClientBuilder`
- 当前支持：
  - `sse`
  - `streamable-http`
- 如果配置了 `FlyAI` 端点，会在首次调用时懒初始化 MCP client
- 会尝试发现可用 search tool
- 如果外部不可用，会回退到种子旅行知识

这样实现的原因：

- 保持工具层统一入口
- 不把外部搜索源耦死在 `TravelAgent`
- 后续替换搜索源或补更多旅行源时，编排层不需要重写

## 6. A2A 旅行专家场景

实现方式：

- `TravelAgent` 在本地保留行程整合职责
- `TravelA2aAdvisor` 负责远程 specialist 调用
- 当前场景是：
  - 主编排器本地拆解任务
  - 旅行搜索先拿本地 / FlyAI 搜索结果
  - 再通过 A2A 远程拿旅行专家补充建议
  - 最后由本地 `TravelAgent` 合并成可执行计划

这样实现的原因：

- A2A 更适合“远程专家能力复用”，而不是把整个主控编排直接拆散
- 旅行场景天然适合远程 specialist
- 远端不可用时，本地仍可回退

## 7. 确认流恢复

当前确认流已经完成产品级闭环：

- 计划生成后，如果存在外部写动作，会生成 `ConfirmationRequest`
- 用户在页面审批后，调用 `/api/v1/assistant/resume`
- `LifeExecutionContinuationService` 判断：
  - 是否仍有待确认项
  - 是否有拒绝项
  - 是否可以恢复后续动作

恢复成功后会：

- 更新 `LifePlan` metadata
- 更新待确认任务状态
- 追加时间线事件
- 把执行状态收敛到 `COMPLETED`

## 8. 分布式集群

已经补上的资产：

- `Dockerfile`
- `deploy/docker-compose.cluster.yml`
- `deploy/nginx/life-os.conf`
- `deploy/k8s/*`

当前集群策略：

- 应用层双副本
- 共享 PostgreSQL
- 共享 session 目录
- Nginx/Ingress 做统一入口
- 集群配置支持打开 pgvector、FlyAI 搜索和远程 A2A specialist
