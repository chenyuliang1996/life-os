# FlyAI 搜索与 A2A 旅行专家

## 1. 这条链路解决什么问题

旅行场景同时需要两种外部能力：

- 动态搜索
- 垂类专家建议

因此当前实现把它们拆成两层：

- `FlyAI`
  - 负责旅行搜索增强
- `A2A`
  - 负责远程旅行专家建议

## 2. 代码落点

- `life-os-tools/.../FlyAiSearchConnector.java`
- `life-os-agents/.../TravelA2aAdvisor.java`
- `life-os-agents/.../TravelAgent.java`

## 3. 调用顺序

1. `TravelAgent` 读取用户目标
2. 先调用 `ToolModuleFacade.search`
3. `ToolModuleFacade` 优先走 `FlyAiSearchConnector`
4. 再结合知识库召回
5. 再调用 `TravelA2aAdvisor`
6. 最终把本地知识、搜索结果和远程专家建议合并成 `AgentContribution`

## 4. 为什么这样设计

- 搜索和专家建议是两种不同能力，不应该塞成一个工具
- `TravelAgent` 保持业务整合，不直接依赖外部协议细节
- `FlyAI` 或 `A2A` 任意一端不可用时，本地仍可回退

## 5. 当前实现边界

- `FlyAI` 通过通用 MCP connector 接入
- 端点、transport、tool name 都做成配置项
- `A2A` 通过 well-known agent card 发现远程 specialist
- 当前没有把远程 specialist service 模板也直接写进 repo；这一步适合下一阶段拆成单独服务
