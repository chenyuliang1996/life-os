# AgentScope 功能印证

下面这份说明只写“代码里已经能看到的能力”和“仍然保留为下一阶段的边界”，避免把能力说得比代码更多。

## 1. 已经落地的能力

### 1.1 AG-UI

- 已落地：
  - `LifeOsAguiAgent` 已注册为默认 AG-UI agent
  - `agentscope-agui-spring-boot-starter` 已接入
- 代码位置：
  - `life-os-web/.../LifeOsAguiAgent.java`
- 官方参考：
  - [AG-UI 文档](https://java.agentscope.io/en/task/agui.html)

### 1.2 ReAct Agent

- 已落地：
  - `LifeOsAgentRuntimeService` 在配置模型后会启用 `ReActAgent`
  - 已接入 `Toolkit`
  - 已调用 `enablePlan()`
- 代码位置：
  - `life-os-web/.../LifeOsAgentRuntimeService.java`
  - `life-os-web/.../LifeOsToolset.java`
- 官方参考：
  - [Agent 快速开始](https://java.agentscope.io/en/quickstart/agent.html)
  - [Plan 文档](https://java.agentscope.io/en/task/plan.html)

### 1.3 Session

- 已落地：
  - 使用 `JsonSession`
  - runtime 会在调用前后加载与保存 session
- 代码位置：
  - `LifeOsRuntimeConfiguration`
  - `LifeOsAgentRuntimeService`
- 官方参考：
  - [Session 文档](https://java.agentscope.io/en/task/session.html)

### 1.4 Tool / Toolkit

- 已落地：
  - `LifeOsToolset` 通过 `@Tool` 暴露工具
  - `life_plan_preview`
  - `search_personal_knowledge`
  - `list_connectors`
- 代码位置：
  - `life-os-web/.../LifeOsToolset.java`
- 官方参考：
  - [Tool 文档](https://java.agentscope.io/en/task/tool.html)

### 1.5 MCP

- 已落地：
  - `FlyAiSearchConnector` 通过 `McpClientBuilder` 对接外部 MCP 搜索端点
  - 支持 `sse` 和 `streamable-http`
  - 工具名支持显式配置和懒发现
- 代码位置：
  - `life-os-tools/.../FlyAiSearchConnector.java`
- 说明：
  - 当前把 `FlyAI` 作为旅行搜索增强源接入
  - 若未配置端点，则自动回退到种子知识检索
- 官方参考：
  - [MCP 文档](https://java.agentscope.io/en/task/mcp.html)

### 1.6 RAG

- 已落地：
  - 知识文档持久化到数据库
  - 文本召回已贯通主流程
  - `PgVectorStore` 已接入，可按配置启用
  - 文档入库时会做 chunking，再写入 pgvector
  - 向量检索不可用时自动回退文本召回
- 代码位置：
  - `life-os-rag/.../KnowledgeModuleFacade.java`
  - `life-os-infra/.../AgentScopePgVectorKnowledgeStore.java`
  - `life-os-web/.../RagController.java`
- 官方参考：
  - [RAG 文档](https://java.agentscope.io/en/task/rag.html)

### 1.7 A2A

- 已落地：
  - `TravelA2aAdvisor` 使用 `A2aAgent` + `WellKnownAgentCardResolver`
  - 旅行专家场景已支持远程 specialist 调用
  - 远程不可用时自动回退本地 `TravelAgent`
- 代码位置：
  - `life-os-agents/.../TravelA2aAdvisor.java`
  - `life-os-agents/.../TravelAgent.java`
- 场景说明：
  - 主编排器仍在本地
  - 旅行垂类建议可由远程 agent service 提供
- 官方参考：
  - [A2A 文档](https://java.agentscope.io/en/task/a2a.html)

### 1.8 Human-in-the-Loop

- 已落地：
  - 计划里会生成 `ConfirmationRequest`
  - 前端可以 approve / reject
  - 所有确认通过后可调用 `/api/v1/assistant/resume` 恢复后续外部动作
- 代码位置：
  - `LifeExecutionContinuationService`
  - `AssistantController`
- 当前状态：
  - 这是产品层确认流与恢复流，已完成闭环
  - 还没有完全替换成 AgentScope `Hook` 级中断令牌恢复
- 官方参考：
  - [Human-in-the-Loop](https://java.agentscope.io/en/task/hitl.html)
  - [Hook](https://java.agentscope.io/en/task/hook.html)

## 2. 为什么这些印证是可信的

这套 demo 对 AgentScope 的印证不是“只引入依赖”，而是每项能力都有代码入口和运行路径：

- AG-UI 真正注册了 agent
- ReAct runtime 能按配置切换
- Session 真正参与运行前后读写
- MCP 真正用于外部搜索增强
- RAG 真正消费数据库和 pgvector
- A2A 真正用于旅行专家远程建议
- HITL 真正可以暂停确认并恢复执行

## 3. 仍然保留为下一阶段边界的部分

- 把旅行 specialist 单独部署成独立 A2A 服务模板
- 把当前产品层确认恢复进一步收敛到更纯粹的 Hook/HITL runtime 形态
- 为 FlyAI 增加针对公开工具 schema 的更细粒度参数映射
