# RAG 选型说明

## 1. 当前代码状态

当前 Demo 已经不是“只留接口”的 RAG：

- 知识文档真正落库
- 文本召回真正接进主编排流程
- `PgVectorStore` 已接入
- 文档入库时会先做 chunking，再写入向量索引
- 向量不可用时自动回退文本召回

换句话说，这套代码已经落成了“**数据库文本召回 + pgvector 可选增强**”的混合 RAG。

## 2. 正式选型结论

这套项目的正式生产选型仍然是：

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

当前项目使用的 `AgentScope 1.0.9` 依赖里，已经能直接使用：

- `io.agentscope.core.rag.store.PgVectorStore`
- `io.agentscope.core.embedding.openai.OpenAITextEmbedding`
- `io.agentscope.core.embedding.ollama.OllamaTextEmbedding`

这说明从框架能力上，`pgvector` 路线是顺的，不是额外硬接。

### 3.3 官方文档支持这类外部向量库路线

官方 RAG 文档展示了：

- 本地 `SimpleKnowledge`
- 外部向量库路线

参考：

- [RAG 文档](https://java.agentscope.io/en/task/rag.html)

## 4. 这版实现到底做到了什么

### 4.1 关系型落库

- `knowledge_documents` 保存文档元数据和正文
- 文档录入后立即可被文本召回

### 4.2 向量索引

- 当 `lifeos.rag.vector.enabled=true` 时，系统会启用 `PgVectorStore`
- 当前支持 `OpenAI` 和 `Ollama` embedding
- 文档会按 chunk 写入向量库，而不是整篇只存一条

### 4.3 混合召回

- 先尝试向量召回
- 再补文本召回
- 最终按文档 id 去重并返回

### 4.4 安全回退

下面几种情况下会自动退回文本模式：

- 数据源不是 PostgreSQL
- 未提供 embedding 凭据
- pgvector 初始化失败
- 远程 embedding 服务不可用

## 5. 为什么不是单独上 Qdrant / ES / Milvus

不是说这些库不好，而是对这套 demo 来说，`PostgreSQL + pgvector` 的收敛性最好：

- 已经必须有 PostgreSQL
- 与事务表共库最省事
- 对 Demo 和一期产品都足够
- 后面若需要独立向量库，也能沿着当前 `VectorKnowledgeStore` 接口继续演进

## 6. 结论

当前状态：

- 已落库
- 已召回
- 已支持 chunk + pgvector
- 已支持文本回退

正式选型：

- `PostgreSQL + pgvector`

原因：

- 基础设施最少
- 和事务数据天然同库
- 与当前 AgentScope 能力和本项目架构最匹配
