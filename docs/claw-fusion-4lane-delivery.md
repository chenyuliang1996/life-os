# Claw 融合四职能交付记录

## Product Agent 输出

- 目标：把 Claw 作为旅行搜索首选实时源，FlyAI 作为二级源，保证主链路可回退。
- 验收：
  - 外部能力链路 `Claw -> FlyAI -> seeded` 生效。
  - ToB 可看到 `claw-skill-search` 与 `flyai-search` 状态。
  - POI 接口支持结构化参数（`travelers/budget/timeWindow`）。

## Designer Agent 输出

- ToC：
  - 节日卡片显示来源标签（Claw/FlyAI/Seeded/Crawler）。
  - 搜索徽标文案升级为 `Claw + FlyAI Search`。
- ToB：
  - 连接器文案明确优先级链路和回退策略。

## Frontend Agent 输出

- 页面文案更新（中英）：
  - hero search badge
  - knowledge 区块说明
  - connector 与安全策略说明
- POI 拉取参数增强：
  - 自动透传 `travelers`、`budget`、`timeWindow`
- 来源标签渲染：
  - `formatPoiSource(...)` 统一展示。

## Backend Agent 输出

- 新增 `ClawSkillConnector`（MCP client + tool discovery + payload 兼容）。
- `ToolModuleFacade` 实现搜索 provider 链：
  - `ClawSkillConnector.searchLive(...)`
  - `FlyAiSearchConnector.searchLive(...)`
  - `fallbackSearch(...)`
- `PoiDiscoveryService` 根据 provider 生成不同来源卡片：
  - `claw-skill`
  - `flyai-skill`
  - `seeded`
- 配置与部署资产补齐：
  - `application.yml`
  - `application-cluster.yml`
  - `docker-compose.cluster.yml`
  - `deploy/k8s/*`

## 验证清单

1. `/api/v1/system/connectors` 包含 `claw-skill-search`。
2. `/api/v1/poi/festivals` 支持 `travelers/budget/timeWindow` 参数。
3. 关闭 Claw/FlyAI 时仍返回 seeded POI，不影响主流程。
4. `mvn -pl life-os-web -am test` 通过。

