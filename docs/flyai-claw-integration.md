# Claw + FlyAI 搜索融合实现

## 1. 当前实现结论

项目已落地统一搜索 provider 链：

`Claw Skill -> FlyAI MCP -> Seeded Fallback`

对应代码：

- `life-os-tools/.../ClawSkillConnector.java`
- `life-os-tools/.../FlyAiSearchConnector.java`
- `life-os-tools/.../ToolModuleFacade.java`

## 2. 为什么这样做

- Claw 适合承接 skill 级别的聚合搜索能力。
- FlyAI 作为第二层实时源，提升可用性和覆盖面。
- 两者不可用时回退到内置知识，保证主链路稳定可响应。

这样可同时满足实时性和稳定性，不把单一外部依赖做成硬故障点。

## 3. 配置项

FlyAI：

- `LIFEOS_FLYAI_ENABLED`
- `LIFEOS_FLYAI_ENDPOINT`
- `LIFEOS_FLYAI_TRANSPORT`
- `LIFEOS_FLYAI_TOOL_NAME`
- `LIFEOS_FLYAI_AUTH_HEADER_NAME`
- `LIFEOS_FLYAI_AUTH_HEADER_VALUE`
- `LIFEOS_FLYAI_TIMEOUT_SECONDS`

Claw：

- `LIFEOS_CLAW_ENABLED`
- `LIFEOS_CLAW_ENDPOINT`
- `LIFEOS_CLAW_TRANSPORT`
- `LIFEOS_CLAW_TOOL_NAME`（默认 `travel_search`）
- `LIFEOS_CLAW_AUTH_HEADER_NAME`
- `LIFEOS_CLAW_AUTH_HEADER_VALUE`
- `LIFEOS_CLAW_TIMEOUT_SECONDS`

## 4. 工具参数兼容策略

Claw/FlyAI 工具调用会尝试多种入参命名，兼容不同 skill schema：

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

1. 开启 Claw，关闭 FlyAI：验证 `claw-skill-search` 生效。
2. 关闭 Claw，开启 FlyAI：验证回落到 FlyAI。
3. 同时关闭：验证回退到 seeded，且主链路不中断。
4. ToB 中查看 connectors，确认 provider 切换符合预期。

