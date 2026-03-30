# RAG 选型说明

## 1. 当前代码状态

当前 Demo 已经完成两件最关键的事：

- 知识文档真正落库
- 规划链路真正消费知识召回结果

当前检索方式是轻量文本匹配，不是最终生产形态。

## 2. 生产选型结论

这套项目建议的正式选型是：

**`PostgreSQL + pgvector`**

## 3. 为什么选它

### 3.1 事务数据和向量数据尽量同库

这套系统天然已经需要共享关系型数据库来存：

- 用户画像
- 计划
- 执行记录
- 确认请求
- 知识文档元数据

如果 RAG 也落到 PostgreSQL 生态里，部署会更简单：

- 减少额外基础设施
- 运维路径统一
- 事务数据和向量索引更容易关联

### 3.2 AgentScope 依赖里已经包含 PgVectorStore

当前项目使用的 `AgentScope 1.0.9` 依赖里，已经能看到：

- `io.agentscope.core.rag.store.PgVectorStore`
- `io.agentscope.core.rag.store.QdrantStore`
- `io.agentscope.core.rag.store.InMemoryStore`

这说明从框架能力上，后续直接接 pgvector 是顺的。

这里是基于本地依赖包的直接检查结论，不是猜测。

### 3.3 官方文档也明确支持向量存储型 RAG 路线

官方 RAG 文档展示了：

- 本地 `SimpleKnowledge`
- 外部向量库 `QdrantStore`

参考：

- [RAG 文档](https://java.agentscope.io/en/task/rag.html)

因此我们选择 `pgvector` 是一个工程上的收敛决策，不是偏离框架能力。

## 4. 为什么当前代码还没直接写成 pgvector 检索

这是一个有意分阶段的实现：

### 第一阶段

- 先把知识文档持久化
- 先把知识录入和知识召回接进主流程
- 先验证 ToC 体验

### 第二阶段

- 文档切块
- Embedding 生成
- `PgVectorStore` 建索引
- 检索召回替换当前轻量文本匹配

## 5. 结论

当前状态：

- 已落库
- 已可检索
- 已参与规划

正式选型：

- `PostgreSQL + pgvector`

原因：

- 共享基础设施最少
- 和事务数据同库
- 与当前 AgentScope 依赖能力相容
