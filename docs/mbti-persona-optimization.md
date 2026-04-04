# MBTI 人格优化方案

## 1. 目标

将多身份模拟从少量预设扩展为可用于真实服务验证的全量人格样本，并让人格信息真正参与执行逻辑，而不是只用于前端展示。

## 2. 已落地改造

1. Persona 扩展
- 新增 16 个 MBTI 身份（`mbti-intj` ~ `mbti-esfp`）
- 保留 `guest-explorer`（受限身份）和 `ops-reviewer`（运营身份）
- Persona 模型新增字段：
  - `mbtiType`
  - `temperament`
  - `decisionLens`

2. 人格偏好入记忆
- 每个 persona 的 `preferences` 统一包含：
  - `travelStyle`
  - `budgetLevel`
  - `studyGoal`
  - `mbtiType`
  - `temperament`
  - `decisionLens`
  - `planningDepth`
  - `feedbackStyle`
  - `energyMode`
  - `studyCadence`

3. Agent 行为适配
- `TravelAgent`
  - 按 `decisionLens/planningDepth/feedbackStyle/energyMode` 调整任务描述和总结表达
- `LearningAgent`
  - 按 `studyCadence/decisionLens/feedbackStyle` 调整学习节奏建议
- `ScheduleAgent`
  - 接入记忆模块，按 `planningDepth/energyMode/feedbackStyle` 输出时间块策略

4. 前端可操作性增强
- Persona 面板新增：
  - 人格搜索
  - 气质分组筛选
  - 实时可见数量统计
- Persona 卡片新增：
  - MBTI 标签
  - 气质标签
  - 决策倾向标签

5. 安全与信任映射
- `persona-mbti-*` 可被识别为对应 `mbti-*` personaId
- MCP 信任前缀默认加入 `persona-mbti-`，保证 MBTI 场景可验证 A2A / MCP 受控能力

## 3. 为什么这样设计

- 真实性：MBTI 不再只是 UI 标签，而是贯穿“画像 -> 记忆 -> 任务生成 -> 执行反馈”。
- 可验证性：同一目标在不同人格下会产生可比较的计划差异，便于做产品与策略 AB 评估。
- 可运维性：保留受限身份与运营身份，能同时覆盖 ToC 用户路径和 ToB 风控路径。

## 4. 验证建议

1. 使用 `persona-mbti-intj` 与 `persona-mbti-esfp` 分别生成同一旅行目标，比较任务描述差异。
2. 切换 `guest-weekend`，确认远程专家能力被禁用。
3. 在 ToC 页面使用筛选器，验证 18 个身份可被快速定位和切换。
