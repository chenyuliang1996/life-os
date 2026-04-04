# Life OS

`Life OS` 是一套基于 `agentscope-java` 的 ToC 服务平台，目标不是只做一个“能聊天”的页面，而是把多 Agent 编排、工具调用、长期记忆、RAG、确认流、AG-UI 和可部署架构整合成一套能继续演进的产品骨架。

## 现在已经具备的能力

- 中英文双入口页面：
  - 中文：`/`
  - 英文：`/en/index.html`
- App 可嵌入 H5 形态：
  - 适配移动端安全区与底部操作栏
  - 支持 `WebView/JSBridge` 回调（`window.LifeOsH5`、`ReactNativeWebView`、`webkit messageHandlers`）
  - 支持通过 URL 或 Native 注入 `userId/threadId/sessionId/contextId/traceId`
- 双展示面：
  - `ToC` 只保留用户操作动线，聚焦输入、计划、确认、画像和知识沉淀
  - `ToB` 聚焦系统运行态，查看组件、连接器、RAG、执行链路和部署状态
- 多身份模拟与用户作用域：
  - 内置 `16` 类 MBTI 人格身份 + 谨慎体验者 + 运营观察者
  - 每个 MBTI 身份包含 `MBTI / 气质分组 / 决策倾向` 元数据与可执行偏好
  - 计划、确认项、知识文档、审计流水按 `userId` 作用域隔离
- 只读探针：
  - ToB 中的模块探针不再写入计划、确认项或画像
  - 运营视角刷新页面不会继续污染真实业务数据
- 统一主控编排：
  - 旅行、学习、日程三个专业 Agent
  - 主编排器负责拆任务、汇总计划、生成确认节点
- AgentScope 运行时：
  - 默认 deterministic fallback
  - 配置模型后切换到 `ReActAgent + Toolkit + Session`
- 数据库持久化：
  - 用户画像
  - 计划
  - 执行时间线
  - 确认请求
  - 知识文档
- RAG 基础层：
  - 当前将知识文档落到数据库
  - 已支持 `PgVectorStore`、chunking 和文本回退
  - 生产选型明确收敛到 `PostgreSQL + pgvector`
- 外部能力增强：
  - 可选 `FlyAI` MCP 旅行搜索
  - 可选远程 `A2A` 旅行专家
- 安全控制面：
  - 用户信任分层
  - 工具权限策略
  - 外呼白名单
  - 审计流水
- 分布式部署骨架：
  - `Dockerfile`
  - `deploy/docker-compose.cluster.yml`
  - `deploy/k8s/*`

## 模块

- `life-os-domain`: 领域模型、DTO、仓储接口、架构状态对象
- `life-os-memory`: 长期记忆 facade
- `life-os-rag`: 知识文档检索 facade
- `life-os-tools`: 工具与连接器 facade
- `life-os-agents`: Travel / Learning / Schedule 三个专家 Agent
- `life-os-orchestrator`: 主控编排器
- `life-os-infra`: 数据库适配、JPA 实体、JSON 序列化适配
- `life-os-web`: Spring Boot、REST API、AG-UI agent、双语页面

## 运行

### 本地单机

```bash
mvn test
mvn -DskipTests install
mvn -f life-os-web/pom.xml spring-boot:run
```

打开：

- 中文：[http://127.0.0.1:8080/](http://127.0.0.1:8080/)
- 英文：[http://127.0.0.1:8080/en/index.html](http://127.0.0.1:8080/en/index.html)

### 集群部署

```bash
docker compose -f deploy/docker-compose.cluster.yml up --build
```

这会启动：

- `pgvector/pgvector:pg16`
- `life-os-1`
- `life-os-2`
- `nginx` 负载均衡入口

## 配置

核心配置在 `life-os-web/src/main/resources/application.yml`：

- `LIFEOS_PERSISTENCE_MODE`
- `LIFEOS_DATASOURCE_URL`
- `LIFEOS_DATASOURCE_USERNAME`
- `LIFEOS_DATASOURCE_PASSWORD`
- `LIFEOS_RAG_STORE`
- `LIFEOS_RAG_VECTOR_ENABLED`
- `LIFEOS_RAG_VECTOR_PROVIDER`
- `LIFEOS_RAG_VECTOR_MODEL`
- `LIFEOS_SESSION_STORE`
- `LIFEOS_FLYAI_ENABLED`
- `LIFEOS_FLYAI_ENDPOINT`
- `LIFEOS_FLYAI_TOOL_NAME`
- `LIFEOS_A2A_TRAVEL_ENABLED`
- `LIFEOS_A2A_TRAVEL_BASE_URL`
- `LIFEOS_SECURITY_TRUSTED_PREFIXES`
- `LIFEOS_SECURITY_OPERATOR_PREFIXES`
- `LIFEOS_SECURITY_RESTRICTED_PREFIXES`
- `LIFEOS_SECURITY_MCP_PREFIXES`
- `LIFEOS_SECURITY_ALLOWLIST`
- `OPENAI_API_KEY`
- `OPENAI_BASE_URL`
- `OPENAI_MODEL`
- `OLLAMA_BASE_URL`
- `OLLAMA_MODEL`

集群配置见 `application-cluster.yml`。

## 文档导航

- [架构说明](docs/architecture.md)
- [实现细节](docs/implementation-guide.md)
- [PMF 核心闭环](docs/pmf-core-loop.md)
- [AgentScope 能力印证](docs/agentscope-validation.md)
- [RAG 选型](docs/rag-selection.md)
- [FlyAI 与 A2A 旅行链路](docs/flyai-a2a-travel-search.md)
- [FlyAI 与 Claw Skill 打通](docs/flyai-claw-integration.md)
- [安全架构与多身份模拟](docs/security-architecture.md)
- [MBTI 人格优化方案](docs/mbti-persona-optimization.md)
- [真实用户动线验证](docs/user-journey-validation.md)
- [分布式部署说明](docs/deployment-cluster.md)
- [API 契约](docs/api-contracts.md)
