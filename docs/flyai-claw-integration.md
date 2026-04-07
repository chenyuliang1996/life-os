# FlyAI CLI + Claw 回退搜索融合实现

## 1. 当前实现结论

项目已落地意图路由与分级回退链：

`Intent Router -> FlyAI (CLI/MCP by mode) -> Claw Skill Fallback -> Seeded Fallback`

对应代码：

- `life-os-tools/.../TravelIntentRouter.java`
- `life-os-tools/.../ClawSkillConnector.java`
- `life-os-tools/.../FlyAiSearchConnector.java`
- `life-os-tools/.../ToolModuleFacade.java`

## 2. 为什么这样做

- 普通问答与旅行检索先做意图分流，避免所有查询都走外部搜索。
- FlyAI 支持本地 CLI 服务层（也可切回 MCP），便于本地封装 skill 与策略治理。
- Claw 保持为回退层，避免 FlyAI 单点故障时中断旅行主链路。
- 两者不可用时回退到内置知识，保证主链路稳定可响应。

这样可同时满足实时性和稳定性，不把单一外部依赖做成硬故障点。

## 3. 配置项

FlyAI：

- `LIFEOS_FLYAI_ENABLED`
- `LIFEOS_FLYAI_MODE`（`cli` / `mcp` / `hybrid` / `mcp-first`）
- `LIFEOS_FLYAI_ENDPOINT`
- `LIFEOS_FLYAI_TRANSPORT`
- `LIFEOS_FLYAI_TOOL_NAME`
- `LIFEOS_FLYAI_AUTH_HEADER_NAME`
- `LIFEOS_FLYAI_AUTH_HEADER_VALUE`
- `LIFEOS_FLYAI_TIMEOUT_SECONDS`
- `LIFEOS_FLYAI_CLI_ENABLED`
- `LIFEOS_FLYAI_CLI_COMMAND`
- `LIFEOS_FLYAI_CLI_WORKING_DIRECTORY`
- `LIFEOS_FLYAI_CLI_TIMEOUT_SECONDS`

Claw：

- `LIFEOS_CLAW_ENABLED`
- `LIFEOS_CLAW_ENDPOINT`
- `LIFEOS_CLAW_TRANSPORT`
- `LIFEOS_CLAW_TOOL_NAME`（默认 `travel_search`）
- `LIFEOS_CLAW_AUTH_HEADER_NAME`
- `LIFEOS_CLAW_AUTH_HEADER_VALUE`
- `LIFEOS_CLAW_TIMEOUT_SECONDS`

## 4. 意图与命令映射策略

旅行请求会映射到 FlyAI 命令集：

- 模糊需求：`ai-search` -> `keyword-search`（回退）
- 机票：`search-flight`
- 火车：`search-train`
- 酒店：`search-hotel`
- 景点：`search-poi`

FlyAI/Claw 工具调用仍会尝试多种入参命名，兼容不同 schema：

- `query`
- `topic`
- `keyword`
- `destination`
- 扩展字段：`travelers`、`budget`、`time_window`

前端 POI 接口已支持：

- `GET /api/v1/poi/festivals?locale=...&query=...&travelers=...&budget=...&timeWindow=...`

## 5. ToB 可观测输出

`GET /api/v1/system/connectors` 现在可看到：

- `search`（统一聚合入口）
- `claw-skill-search`
- `flyai-search`

前端会区分 connector 状态与 POI 来源标签：

- `claw-skill`
- `flyai-skill`
- `seeded`
- `crawler`

## 6. 安全策略

保持现有信任与审批模型不变：

- `mcpTrusted` 才可进入远程实时搜索能力
- 仍受 outbound allowlist 限制
- 外部写入动作继续走确认流

默认 allowlist 已补充：

- `open.fly.ai`
- `clawhub.ai`

## 7. 验证建议

1. `LIFEOS_FLYAI_MODE=cli` 且 `LIFEOS_FLYAI_CLI_ENABLED=true`：验证旅行请求优先走本地 CLI。
2. 关闭 CLI 或命令失败：验证自动回退到 FlyAI MCP（hybrid 模式）或 Claw。
3. 关闭 Claw/FlyAI：验证 seeded 回退生效且主链路不中断。
4. 在 `/api/v1/system/connectors` 检查 `flyai-search` 的 mode 与 readiness 输出是否符合预期。
