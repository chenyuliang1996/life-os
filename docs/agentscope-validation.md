# AgentScope 功能印证

下面这份说明只写“已经落地的”和“明确预留但还没完全落地的”，避免把能力说得比代码更多。

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
- 官方参考：
  - [Tool 文档](https://java.agentscope.io/en/task/tool.html)

### 1.5 Human-in-the-Loop 业务门控

- 已落地：
  - 计划里会生成 `ConfirmationRequest`
  - 前端可以 approve / reject
- 当前状态：
  - 这部分是产品层确认流，已完成闭环
  - 还没有完全替换成 AgentScope `HITL Hook` 级别暂停恢复
- 官方参考：
  - [Human-in-the-Loop](https://java.agentscope.io/en/task/hitl.html)
  - [Hook](https://java.agentscope.io/en/task/hook.html)

## 2. 已经为后续落地留好边界的能力

### 2.1 RAG

- 当前代码：
  - 持久化知识文档
  - 轻量文本召回
- 对应 AgentScope 能力：
  - 文档与向量存储扩展边界已保留
- 官方参考：
  - [RAG 文档](https://java.agentscope.io/en/task/rag.html)

### 2.2 A2A

- 当前代码：
  - 还是模块化单体
  - 但专家 Agent 已经独立模块化，可单独执行
- 为什么说已预留：
  - 边界已经清晰
  - 部署形态已具备拆服务基础
- 官方参考：
  - [A2A 文档](https://java.agentscope.io/en/task/a2a.html)

## 3. 结论

这套 demo 对 AgentScope 的印证不是停留在“引入依赖”，而是已经覆盖：

- 前端协议接入
- ReAct runtime
- session
- toolkit
- 规划
- 确认流
- RAG 演进边界

还未完全落地但已经设计好的，是：

- Hook/HITL runtime 级暂停恢复
- A2A 独立服务化
- 真正的向量检索链路
