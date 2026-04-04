# FlyAI 打通方案（Claw / Skill 路径）

## 1. 结论

当前服务已经支持通过 MCP 方式接入 FlyAI（`FlyAiSearchConnector`），所以“打通 FlyAI”不需要改主流程，只需要补齐一个稳定的 MCP skill 接入层。

推荐路径：

1. 在 `clawhub.ai` 选择已有 FlyAI 检索 skill（如果有可直接复用）
2. 如果没有完全匹配的 skill，就按我们的旅行场景自建一个 `travel_search` skill
3. 把 skill 暴露成 SSE/HTTP MCP endpoint，填到服务配置

## 2. 对接点

服务读取以下配置：

- `LIFEOS_FLYAI_ENABLED=true`
- `LIFEOS_FLYAI_ENDPOINT=<mcp endpoint>`
- `LIFEOS_FLYAI_TRANSPORT=sse`（或 `http`）
- `LIFEOS_FLYAI_TOOL_NAME=travel_search`（可选）
- `LIFEOS_FLYAI_AUTH_HEADER_NAME`
- `LIFEOS_FLYAI_AUTH_HEADER_VALUE`

连接器会在首次调用时懒初始化，并自动探测搜索工具名：

- `travel_search`
- `search_travel`
- `search`
- `trip_search`
- `destination_search`

## 3. 推荐 Skill Schema（旅行场景）

工具名建议统一为：`travel_search`

参数建议至少包含：

- `query`：用户目标（目的地 + 偏好）
- `locale`：语言
- `travelers`：人数
- `budget`：预算级别
- `time_window`：出行时间窗口

输出建议：

- `poi_candidates`（结构化）
- `route_suggestion`（分日建议）
- `risk_notes`（天气/拥堵/闭馆）

## 4. 运维验证

1. 打开 ToB 的连接器面板，确认 `flyai-search` 为可用
2. 在 ToC 输入旅行目标，检查计划中 `travel.search` 元数据是否为实时内容
3. 关闭 FlyAI endpoint，验证服务自动回退到 seeded search（不中断主链路）
