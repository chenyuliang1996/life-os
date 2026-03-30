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

效果：

- 中文页与英文页各自有独立入口
- 同一个后端 API 即可输出对应语言的计划与部分摘要

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

基础设施层新增：

- JPA Entity
- Spring Data Repository
- Domain Adapter
- JSON 编解码器 `JsonValueCodec`

### 3.3 为什么这样实现

- 领域模型继续保持纯 record，不污染 JPA 注解
- 持久化对象和领域对象解耦，后续换成 MyBatis / jOOQ / 事件存储都容易
- `Map`、`List`、`PlanTask`、`TimelineEvent` 这类结构使用 JSON 列存储，便于快速演进 Demo 数据模型

## 4. RAG 当前实现

当前实现不是完整向量检索，而是“**数据库落库 + 轻量内容召回**”：

- 知识文档先持久化到 `knowledge_documents`
- 检索层从 `title / summary / content / tags` 做轻量匹配
- 页面支持直接新增文档和正文内容

这样做的原因：

- 先把知识数据真正持久化
- 先打通知识录入 -> 存储 -> 召回 -> 规划
- 为后续切换 `pgvector` 留出同库演进路径

## 5. 分布式集群

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

## 6. 为什么页面也要展示架构状态

这个 demo 的目标不是只展示对话，而是展示：

- 系统当前运行在哪种 runtime
- 数据是不是已经持久化
- RAG 当前落在什么存储
- 是否已经具备集群部署前提

所以前端新增了 `Architecture` 区块，直接展示后端返回的架构状态。
