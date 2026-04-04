# OtterLife PMF 核心闭环

## 1. 核心定位

不是“功能大杂烩助手”，而是“把一次出行从灵感到落地真正跑通”的个人助手：

1. 节日/目的地灵感（POI）
2. 日历落位（可编辑、可回看）
3. 多人偏好对齐
4. 生成可执行方案并过确认流
5. 复盘进入长期记忆

## 2. ToB 监控设计（中间件可接）

建议标准链路：

- `sessionId`：用户会话维度
- `contextId`：本次规划上下文（跨多个请求）
- `traceId`：单次请求链路（可关联后端 spans）

可接入：

- Tracing：OpenTelemetry + Tempo/Jaeger
- Metrics：Prometheus + Grafana
- Logs：Loki/ELK
- DB：postgres-exporter + 慢查询告警

## 3. 阶段埋点（可观测用户动线）

建议埋点事件：

- `poi_feed_loaded`
- `poi_applied_to_goal`
- `calendar_day_updated`
- `group_member_added`
- `group_context_applied`
- `plan_preview_created`
- `confirmation_submitted`
- `execution_resumed`

每个事件都带：

- `sessionId`
- `contextId`
- `traceId`
- `userId`
- `surface`（toc/tob）
- `locale`
- `durationMs`（如有）
- `success`

## 4. 会话记忆策略（AgentScope）

- 短时记忆（session memory）
  - 当前会话目标、临时偏好、最近确认状态
- 长时记忆（profile + history + rag）
  - 旅行偏好、预算风格、常用节奏、历史计划复盘

策略：

- 会话结束后，将高价值偏好汇总写入长期记忆
- 长期记忆只保留“稳定偏好”，避免噪声污染
