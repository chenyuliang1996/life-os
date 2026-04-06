const TRANSLATIONS = {
  "zh-CN": {
    "app.title": "OtterLife",
    "topbar.badge": "OtterLife Service Platform",
    "topbar.surfaceToc": "ToC 体验",
    "topbar.surfaceTob": "ToB 运营台",
    "topbar.tocFocus": "聚焦模式",
    "topbar.tocStudio": "全功能",
    "hero.title": "OtterLife",
    "hero.subtitle": "面向真实用户的出行与生活协同服务，统一承接节日灵感、行程规划、多人协作与执行闭环。",
    "hero.badgeRag": "Hybrid RAG",
    "hero.badgeSearch": "Claw + FlyAI Search",
    "hero.badgeA2a": "A2A Travel",
    "hero.runPreview": "生成可执行方案",
    "hero.refresh": "刷新数据",
    "hero.runAssistant": "运行助手",
    "metrics.targetDau": "DAU 目标",
    "metrics.currentQps": "当前 QPS",
    "metrics.successRate": "成功率",
    "metrics.assistantP95": "助手 P95",
    "sections.control.title": "任务控制台",
    "sections.control.subtitle": "统一入口，输入真实需求并生成可执行方案、触发助手与查看结果。",
    "sections.control.pill": "编排流",
    "sections.runtime.title": "运行时状态",
    "sections.runtime.subtitle": "查看当前运行方式、模型接入状态和执行说明。",
    "sections.runtime.pill": "AgentScope",
    "sections.operations.title": "服务指标",
    "sections.operations.subtitle": "基于真实请求、审批和前端体验埋点汇总容量、可靠性与体验指标。",
    "sections.operations.pill": "SLO",
    "sections.trace.title": "链路观测",
    "sections.trace.subtitle": "按 session / context / trace 关联用户请求和前端交互。",
    "sections.trace.pill": "Trace Lens",
    "sections.sequence.title": "用户动线引导",
    "sections.sequence.subtitle": "告诉用户下一步该做什么，把规划、确认和执行变成可理解的操作路径。",
    "sections.sequence.pill": "Journey",
    "sections.festivals.title": "近期节日灵感",
    "sections.festivals.subtitle": "根据近期节日推荐可玩 POI，并自动轮播刷新。",
    "sections.festivals.pill": "POI Feed",
    "sections.calendar.title": "行程日历",
    "sections.calendar.subtitle": "默认月视图，可切换年/日；点击日期可直接修改每日规划。",
    "sections.calendar.pill": "Calendar",
    "sections.group.title": "多人出行协同",
    "sections.group.subtitle": "添加同行成员和偏好，自动生成兼顾多人诉求的规划上下文。",
    "sections.group.pill": "Group Trip",
    "sections.hub.title": "行程主线",
    "sections.hub.subtitle": "把灵感、日历、多人偏好和执行合成一条连续操作主线。",
    "sections.hub.pill": "Journey Hub",
    "sections.arch.title": "部署架构",
    "sections.arch.subtitle": "展示当前部署模式、数据库、RAG 存储与会话策略。",
    "sections.arch.pill": "Cluster",
    "sections.rag.title": "RAG 运行态",
    "sections.rag.subtitle": "展示当前是否启用了向量检索、采用什么 embedding provider，以及当前检索模式。",
    "sections.rag.pill": "Semantic",
    "sections.assistant.title": "助手回复",
    "sections.assistant.subtitle": "展示当前回复、亮点摘要以及运行模式。",
    "sections.assistant.pill": "实时输出",
    "sections.plan.title": "计划输出",
    "sections.plan.subtitle": "结构化展示当前生成的多 Agent 计划卡片。",
    "sections.plan.pill": "Structured",
    "sections.history.title": "计划历史",
    "sections.history.subtitle": "数据库持久化后的历史计划。",
    "sections.history.pill": "已存储",
    "sections.timeline.title": "执行时间线",
    "sections.timeline.subtitle": "查看主控 Agent 和子 Agent 的执行轨迹。",
    "sections.timeline.pill": "Execution",
    "sections.confirmations.title": "待确认动作",
    "sections.confirmations.subtitle": "写入外部系统前统一经过人工确认。",
    "sections.confirmations.pill": "Human Loop",
    "sections.profile.title": "用户画像",
    "sections.profile.subtitle": "查看和编辑长期记忆中的偏好。",
    "sections.profile.pill": "Memory",
    "sections.knowledge.title": "知识库",
    "sections.knowledge.subtitle": "持久化文档进入混合 RAG 层，支持 PostgreSQL + pgvector，并可叠加 Claw Skill 与 FlyAI 旅行搜索。",
    "sections.knowledge.pill": "RAG Seeds",
    "sections.modules.title": "模块探针",
    "sections.modules.subtitle": "查看每个模块的健康探针和执行摘要。",
    "sections.modules.pill": "Probe",
    "sections.connectors.title": "连接器",
    "sections.connectors.subtitle": "展示工具层可用能力和外部依赖状态。",
    "sections.connectors.pill": "Tools",
    "sections.personas.title": "MBTI 人格场景",
    "sections.personas.subtitle": "按 MBTI 人格切换真实用户画像，快速验证不同决策风格下的服务表现。",
    "sections.personas.pill": "MBTI",
    "sections.guardrails.title": "用户护栏",
    "sections.guardrails.subtitle": "展示当前身份的信任级别、写入审批和外部连接限制。",
    "sections.guardrails.pill": "Safety",
    "sections.security.title": "安全控制",
    "sections.security.subtitle": "查看当前身份的信任状态、工具权限和外呼白名单。",
    "sections.security.pill": "Guardrail",
    "sections.audit.title": "审计流水",
    "sections.audit.subtitle": "记录计划、审批、画像更新和知识写入等关键操作。",
    "sections.audit.pill": "Audit",
    "fields.prompt": "提示词",
    "fields.userId": "用户 ID",
    "fields.threadId": "线程 ID",
    "fields.travelStyle": "旅行风格",
    "fields.budgetLevel": "预算等级",
    "fields.studyGoal": "学习目标",
    "fields.authUsername": "登录名",
    "fields.authPassword": "密码",
    "fields.traceUserFilter": "按用户筛选",
    "fields.traceSessionFilter": "按会话筛选",
    "fields.traceContextFilter": "按上下文筛选",
    "fields.traceIdFilter": "按链路筛选",
    "fields.traceOperationFilter": "按操作筛选",
    "fields.personaSearch": "搜索人格",
    "fields.personaTemperament": "气质分组",
    "fields.personaCount": "展示 {visible}/{total} 个身份",
    "fields.personaCompact": "当前人格",
    "fields.memberName": "成员名称",
    "fields.memberStyle": "偏好类型",
    "fields.contextCurrent": "当前链路",
    "fields.knowledgeTitle": "标题",
    "fields.knowledgeTags": "标签",
    "fields.knowledgeSummary": "摘要",
    "fields.knowledgeContent": "正文",
    "actions.saveProfile": "保存画像",
    "actions.login": "登录",
    "actions.register": "注册",
    "actions.logout": "退出",
    "actions.applyFilter": "应用筛选",
    "actions.resetFilter": "重置筛选",
    "actions.addKnowledge": "新增知识",
    "actions.openPlan": "打开",
    "actions.approve": "通过",
    "actions.reject": "拒绝",
    "actions.usePersona": "使用此身份",
    "actions.prev": "上一个",
    "actions.next": "下一个",
    "actions.addMember": "添加成员",
    "actions.applyGroupContext": "应用多人偏好",
    "actions.editDayPlan": "编辑当天规划",
    "actions.clearDayPlan": "清空当天规划",
    "actions.saveDayPlan": "保存当天规划",
    "actions.applyFestivalPoi": "用于本次规划",
    "actions.close": "关闭",
    "actions.cancel": "取消",
    "actions.showMore": "展开更多",
    "actions.showLess": "收起",
    "actions.showImage": "看图片",
    "actions.showVideo": "看视频",
    "state.previewIdle": "点击“生成可执行方案”开始规划。",
    "state.assistantIdle": "运行助手后，这里会显示最新回复。",
    "state.planIdle": "计划卡片会显示在这里。",
    "state.knowledgeEmpty": "暂无知识文档。",
    "state.personaEmpty": "暂无身份场景。",
    "state.personaFilteredEmpty": "没有匹配当前筛选条件的身份。",
    "state.planEmpty": "还没有计划。",
    "state.planOverflow": "仅展示最近 {visible} 条，共 {total} 条。",
    "state.timelineEmpty": "执行时间线会显示在这里。",
    "state.confirmationEmpty": "当前没有待确认动作。",
    "state.confirmationOverflow": "仅展示最近 {visible} 条待处理项，共 {total} 条。",
    "state.architectureEmpty": "架构信息加载后会显示在这里。",
    "state.ragEmpty": "RAG 状态加载后会显示在这里。",
    "state.operationsEmpty": "服务指标加载后会显示在这里。",
    "state.guardrailEmpty": "安全护栏加载后会显示在这里。",
    "state.auditEmpty": "审计流水会显示在这里。",
    "state.profileLoading": "正在加载画像...",
    "state.requestFailed": "请求失败，请查看控制台日志。",
    "state.authGuest": "当前未登录，可注册或登录后启用用户级记忆与轨迹。",
    "state.authLoggedIn": "已登录：{name}（{userId}）",
    "state.authExpired": "登录已失效，请重新登录。",
    "state.authLoginSuccess": "登录成功，已切换到当前账号。",
    "state.authLogoutSuccess": "已退出登录。",
    "state.traceEmpty": "尚无链路数据，先发起一次请求查看关联关系。",
    "state.memoryOpsEmpty": "暂无可用的记忆操作明细。",
    "state.groupEmpty": "暂无同行成员，默认按单人出行规划。",
    "state.festivalEmpty": "暂无可用节日灵感。",
    "state.calendarEmpty": "当天还没有规划内容，点击“编辑当天规划”开始记录。",
    "state.personaCompactHint": "MBTI 已改为紧凑模式，可搜索后快速切换。",
    "state.profileSaved": "用户画像已保存。",
    "state.knowledgeSaved": "知识文档已入库。",
    "state.planGenerated": "已生成“{title}”，包含 {tasks} 个任务和 {confirmations} 个确认节点。",
    "state.personaApplied": "已切换到“{name}”，并同步载入该身份画像。",
    "state.resumeWaiting": "确认已记录，仍在等待其他确认。",
    "state.resumeBlocked": "确认被拒绝，当前运行已阻塞。",
    "state.resumeApplied": "确认完成，已恢复并执行外部写入。",
    "assistant.highlights": "关键亮点",
    "assistant.mode": "运行模式",
    "assistant.planId": "计划 ID",
    "assistant.runId": "执行 ID",
    "runtime.live": "实时",
    "runtime.fallback": "回退",
    "runtime.provider": "提供方",
    "runtime.model": "模型",
    "runtime.summary": "说明",
    "architecture.deploymentMode": "部署模式",
    "architecture.persistenceMode": "持久化模式",
    "architecture.database": "主数据库",
    "architecture.ragStore": "RAG 存储",
    "architecture.sessionStore": "会话存储",
    "architecture.topology": "集群拓扑",
    "architecture.travelSearch": "旅行搜索",
    "architecture.travelSpecialist": "旅行专家模式",
    "architecture.notes": "说明",
    "rag.enabled": "向量检索",
    "rag.vectorReady": "向量可用",
    "rag.retrievalMode": "检索模式",
    "rag.store": "存储",
    "rag.provider": "Embedding 提供方",
    "rag.modelName": "Embedding 模型",
    "rag.dimensions": "向量维度",
    "rag.summary": "状态说明",
    "sequence.user.title": "用户意图",
    "sequence.user.meta": "输入目标",
    "sequence.orchestrator.title": "主控编排",
    "sequence.orchestrator.meta": "任务拆解",
    "sequence.rag.title": "混合 RAG",
    "sequence.rag.meta": "知识拼装",
    "sequence.search.title": "旅行搜索",
    "sequence.search.meta": "动态增强",
    "sequence.a2a.title": "远程专家",
    "sequence.a2a.meta": "A2A Specialist",
    "sequence.hitl.title": "确认流",
    "sequence.hitl.meta": "Human in the loop",
    "sequence.persist.title": "状态落库",
    "sequence.persist.meta": "持久化",
    "sequence.emptyPrompt": "等待新的用户目标进入编排。",
    "sequence.hitlWaiting": "待确认动作将阻断外部写入。",
    "sequence.hitlClear": "当前没有待确认动作，执行可继续推进。",
    "sequence.persistSummary": "计划、运行记录、确认单和知识文档都会写入数据库。",
    "sequence.nextAction": "下一步建议",
    "sequence.actionDraft": "先在任务控制台输入目标，或选择节日 POI 自动填入。",
    "sequence.actionCalendar": "检查日历并补齐当天规划，避免排期冲突。",
    "sequence.actionGroup": "如有同行人，请先添加成员偏好再生成方案。",
    "sequence.actionRun": "点击“生成可执行方案”，查看任务和确认点。",
    "sequence.actionConfirm": "完成待确认动作后，系统会继续执行外部写入。",
    "calendar.view.month": "月",
    "calendar.view.year": "年",
    "calendar.view.day": "日",
    "calendar.editPrompt": "请输入 {date} 的主要规划内容",
    "calendar.groupSummary": "共 {count} 天有计划",
    "calendar.sheet.title": "编辑当天规划",
    "calendar.sheet.subtitle": "快速记录今天重点、节奏和注意事项。",
    "calendar.sheet.placeholder": "例如：上午逛浅草和上野；下午回酒店休息；晚间河边散步。",
    "calendar.sheet.length": "已输入 {count} 字",
    "calendar.sheet.template.relaxed": "上午轻松打卡，下午安排恢复时间，晚上只保留一项活动。",
    "calendar.sheet.template.efficient": "按片区连线减少换乘，每 2 小时预留 20 分钟机动。",
    "calendar.sheet.template.family": "优先室内亲子点位，中午固定休息，夜间不安排远距离移动。",
    "group.style.culture": "文化体验",
    "group.style.food": "美食探索",
    "group.style.family": "亲子友好",
    "group.style.photo": "拍照打卡",
    "group.style.night": "夜间活动",
    "hub.step.poi": "节日灵感",
    "hub.step.calendar": "日历规划",
    "hub.step.group": "同行协同",
    "hub.step.run": "执行生成",
    "hub.ready": "准备度",
    "hub.next": "下一步",
    "hub.next.poi": "选择一个 POI 灵感并应用到目标。",
    "hub.next.calendar": "先补齐至少一天的日历规划。",
    "hub.next.group": "如有同行成员，建议先录入偏好。",
    "hub.next.run": "已就绪，直接生成可执行方案。",
    "ops.card.target": "服务目标",
    "ops.card.traffic": "流量与容量",
    "ops.card.reliability": "可靠性",
    "ops.card.experience": "用户体验",
    "ops.targetPeakQps": "峰值 QPS",
    "ops.availabilitySlo": "可用性 SLO",
    "ops.requestsPerMinute": "每分钟请求量",
    "ops.totalRequests": "累计请求量",
    "ops.pendingConfirmations": "待确认积压",
    "ops.previewP95": "方案生成 P95",
    "ops.resumeP95": "恢复执行 P95",
    "ops.confirmationP95": "审批决策 P95",
    "ops.uxBootstrapP95": "首屏加载 P95",
    "ops.uxInteractionP95": "交互反馈 P95",
    "ops.activeSessions": "活跃会话",
    "ops.activeContexts": "活跃上下文",
    "ops.recentTraces": "最近链路事件",
    "ops.capacityOk": "容量充足",
    "ops.capacityRisk": "接近容量阈值",
    "ops.sloOk": "SLO 正常",
    "ops.sloRisk": "SLO 需关注",
    "label.owner": "负责人",
    "label.status": "状态",
    "label.comment": "备注",
    "label.locale": "语言",
    "label.persona": "身份",
    "label.trustTier": "信任级别",
    "label.allowlist": "白名单",
    "label.outcome": "结果",
    "label.userId": "用户",
    "label.thread": "线程",
    "label.session": "会话",
    "label.context": "上下文",
    "label.trace": "链路",
    "label.timestamp": "时间",
    "common.enabled": "已启用",
    "common.disabled": "已关闭",
    "security.workspaceTrusted": "工作区信任",
    "security.mcpTrusted": "MCP 信任",
    "security.network": "外部网络",
    "security.writeApproval": "写入审批",
    "security.summary": "说明",
    "security.allowlist": "外呼白名单",
    "security.policies": "工具权限",
    "persona.prompt": "典型诉求",
    "persona.description": "身份说明",
    "persona.mbti": "MBTI",
    "persona.temperament": "气质",
    "persona.decisionLens": "决策倾向",
    "option.allTemperaments": "全部分组",
    "trust.restricted": "受限",
    "trust.guarded": "受保护",
    "trust.trusted": "可信",
    "trust.operator": "运营级",
    "access.allowed": "允许",
    "access.denied": "拒绝",
    "access.approval-required": "需审批",
    "access.seeded-only": "仅种子知识",
    "access.guarded-live": "受控实时",
    "access.guarded-auto": "受控自动",
    "outcome.SUCCESS": "成功",
    "outcome.FAILURE": "失败",
    "outcome.DENIED": "拒绝",
    "outcome.APPROVED": "已通过",
    "outcome.REJECTED": "已拒绝",
    "outcome.RESUMED": "已恢复",
    "outcome.WAITING": "等待中",
    "outcome.BLOCKED": "已阻塞",
    "status.PENDING": "待处理",
    "status.READY_FOR_CONFIRMATION": "待确认",
    "status.SCHEDULED": "已排期",
    "status.DONE": "已完成",
    "status.PENDING_CONFIRMATION": "待确认",
    "status.PENDING_APPROVAL": "待审批",
    "status.APPROVED": "已通过",
    "status.REJECTED": "已拒绝",
    "status.PENDING_RUNTIME": "待运行",
    "status.ACTIVE": "进行中",
    "status.COMPLETED": "已完成",
    "status.BLOCKED": "已阻塞"
  },
  "en-US": {
    "app.title": "OtterLife",
    "topbar.badge": "OtterLife Service Platform",
    "topbar.surfaceToc": "ToC Experience",
    "topbar.surfaceTob": "ToB Operations",
    "topbar.tocFocus": "Focus",
    "topbar.tocStudio": "Studio",
    "hero.title": "OtterLife",
    "hero.subtitle": "A user-facing travel service that connects holiday inspiration, itinerary planning, collaboration, and execution in one flow.",
    "hero.badgeRag": "Hybrid RAG",
    "hero.badgeSearch": "Claw + FlyAI Search",
    "hero.badgeA2a": "A2A Travel",
    "hero.runPreview": "Create Action Plan",
    "hero.refresh": "Refresh Data",
    "hero.runAssistant": "Run Assistant",
    "metrics.targetDau": "Target DAU",
    "metrics.currentQps": "Current QPS",
    "metrics.successRate": "Success Rate",
    "metrics.assistantP95": "Assistant P95",
    "sections.control.title": "Mission Control",
    "sections.control.subtitle": "Use one entry point to capture real user intent, create an action plan, and inspect structured results.",
    "sections.control.pill": "Flow",
    "sections.runtime.title": "Runtime Status",
    "sections.runtime.subtitle": "Check the active runtime mode, model access, and execution notes.",
    "sections.runtime.pill": "AgentScope",
    "sections.operations.title": "Service Metrics",
    "sections.operations.subtitle": "Capacity, reliability, and UX signals derived from real requests, approvals, and client telemetry.",
    "sections.operations.pill": "SLO",
    "sections.trace.title": "Trace Lens",
    "sections.trace.subtitle": "Correlate user requests and UX actions via session / context / trace ids.",
    "sections.trace.pill": "Trace Lens",
    "sections.sequence.title": "User Journey Guide",
    "sections.sequence.subtitle": "Shows what users should do next, not only technical execution order.",
    "sections.sequence.pill": "Journey",
    "sections.festivals.title": "Upcoming Holiday POIs",
    "sections.festivals.subtitle": "A rotating feed of nearby holiday ideas and playable POIs.",
    "sections.festivals.pill": "POI Feed",
    "sections.calendar.title": "Trip Calendar",
    "sections.calendar.subtitle": "Month by default, switchable to year/day; click a date to edit daily plans.",
    "sections.calendar.pill": "Calendar",
    "sections.group.title": "Group Travel Collaboration",
    "sections.group.subtitle": "Add companions and preferences to generate balanced multi-person planning context.",
    "sections.group.pill": "Group Trip",
    "sections.hub.title": "Trip Journey Hub",
    "sections.hub.subtitle": "Connect inspiration, calendar, companions, and execution into one continuous flow.",
    "sections.hub.pill": "Journey Hub",
    "sections.arch.title": "Deployment Architecture",
    "sections.arch.subtitle": "Shows deployment mode, database, RAG storage, and session strategy.",
    "sections.arch.pill": "Cluster",
    "sections.rag.title": "RAG Runtime",
    "sections.rag.subtitle": "Shows whether vector retrieval is active, which embedding provider is configured, and which retrieval mode is in use.",
    "sections.rag.pill": "Semantic",
    "sections.assistant.title": "Assistant Reply",
    "sections.assistant.subtitle": "Shows the latest reply, highlights, and execution mode.",
    "sections.assistant.pill": "Live Output",
    "sections.plan.title": "Plan Output",
    "sections.plan.subtitle": "Structured cards generated by the orchestrator and specialist agents.",
    "sections.plan.pill": "Structured",
    "sections.history.title": "Plan History",
    "sections.history.subtitle": "Plans persisted into the database.",
    "sections.history.pill": "Stored Plans",
    "sections.timeline.title": "Execution Timeline",
    "sections.timeline.subtitle": "Inspect orchestrator and specialist execution traces.",
    "sections.timeline.pill": "Execution",
    "sections.confirmations.title": "Confirmations",
    "sections.confirmations.subtitle": "Every external write goes through a human approval gate.",
    "sections.confirmations.pill": "Human Loop",
    "sections.profile.title": "Profile",
    "sections.profile.subtitle": "Inspect and edit long-term memory preferences.",
    "sections.profile.pill": "Memory",
    "sections.knowledge.title": "Knowledge Base",
    "sections.knowledge.subtitle": "Persist documents into the hybrid RAG layer with PostgreSQL + pgvector and optional Claw Skill plus FlyAI travel search.",
    "sections.knowledge.pill": "RAG Seeds",
    "sections.modules.title": "Module Probes",
    "sections.modules.subtitle": "Inspect health probes and execution summaries for every module.",
    "sections.modules.pill": "Probe",
    "sections.connectors.title": "Connectors",
    "sections.connectors.subtitle": "Available tool capabilities and external dependency status exposed by the tool layer.",
    "sections.connectors.pill": "Tools",
    "sections.personas.title": "MBTI Personas",
    "sections.personas.subtitle": "Switch MBTI persona profiles to validate service behavior under different decision styles.",
    "sections.personas.pill": "MBTI",
    "sections.guardrails.title": "User Guardrails",
    "sections.guardrails.subtitle": "Shows trust level, write approvals, and outbound restrictions for the active identity.",
    "sections.guardrails.pill": "Safety",
    "sections.security.title": "Security Controls",
    "sections.security.subtitle": "Inspect trust state, tool permissions, and the outbound allowlist for the active identity.",
    "sections.security.pill": "Guardrail",
    "sections.audit.title": "Audit Ledger",
    "sections.audit.subtitle": "Tracks key actions such as planning, approvals, profile edits, and knowledge writes.",
    "sections.audit.pill": "Audit",
    "fields.prompt": "Prompt",
    "fields.userId": "User ID",
    "fields.threadId": "Thread ID",
    "fields.travelStyle": "Travel style",
    "fields.budgetLevel": "Budget level",
    "fields.studyGoal": "Study goal",
    "fields.authUsername": "Username",
    "fields.authPassword": "Password",
    "fields.traceUserFilter": "Filter by user",
    "fields.traceSessionFilter": "Filter by session",
    "fields.traceContextFilter": "Filter by context",
    "fields.traceIdFilter": "Filter by trace",
    "fields.traceOperationFilter": "Filter by operation",
    "fields.personaSearch": "Search personas",
    "fields.personaTemperament": "Temperament",
    "fields.personaCount": "Showing {visible}/{total} personas",
    "fields.personaCompact": "Active persona",
    "fields.memberName": "Member name",
    "fields.memberStyle": "Preference type",
    "fields.contextCurrent": "Current Context",
    "fields.knowledgeTitle": "Title",
    "fields.knowledgeTags": "Tags",
    "fields.knowledgeSummary": "Summary",
    "fields.knowledgeContent": "Content",
    "actions.saveProfile": "Save Profile",
    "actions.login": "Sign In",
    "actions.register": "Sign Up",
    "actions.logout": "Sign Out",
    "actions.applyFilter": "Apply Filters",
    "actions.resetFilter": "Reset Filters",
    "actions.addKnowledge": "Add Knowledge",
    "actions.openPlan": "Open",
    "actions.approve": "Approve",
    "actions.reject": "Reject",
    "actions.usePersona": "Use Persona",
    "actions.prev": "Previous",
    "actions.next": "Next",
    "actions.addMember": "Add Member",
    "actions.applyGroupContext": "Apply Group Context",
    "actions.editDayPlan": "Edit day plan",
    "actions.clearDayPlan": "Clear day plan",
    "actions.saveDayPlan": "Save day plan",
    "actions.applyFestivalPoi": "Use for planning",
    "actions.close": "Close",
    "actions.cancel": "Cancel",
    "actions.showMore": "Show more",
    "actions.showLess": "Show less",
    "actions.showImage": "Image",
    "actions.showVideo": "Video",
    "state.previewIdle": "Press \"Create Action Plan\" to start planning.",
    "state.assistantIdle": "Run the assistant to see the latest reply.",
    "state.planIdle": "Plan cards will appear here.",
    "state.knowledgeEmpty": "No knowledge documents yet.",
    "state.personaEmpty": "No persona presets available.",
    "state.personaFilteredEmpty": "No personas match the active filters.",
    "state.planEmpty": "No plans yet.",
    "state.planOverflow": "Showing the latest {visible} plans out of {total}.",
    "state.timelineEmpty": "Execution timeline will appear here.",
    "state.confirmationEmpty": "No pending confirmations.",
    "state.confirmationOverflow": "Showing the latest {visible} pending approvals out of {total}.",
    "state.architectureEmpty": "Architecture details will appear here after loading.",
    "state.ragEmpty": "RAG details will appear here after loading.",
    "state.operationsEmpty": "Service metrics will appear here after loading.",
    "state.guardrailEmpty": "Guardrail details will appear here after loading.",
    "state.auditEmpty": "Audit events will appear here after loading.",
    "state.profileLoading": "Loading profile...",
    "state.requestFailed": "Request failed. Please inspect the console logs.",
    "state.authGuest": "Signed out. Register or sign in to enable user-level memory and trace.",
    "state.authLoggedIn": "Signed in: {name} ({userId})",
    "state.authExpired": "Session expired. Please sign in again.",
    "state.authLoginSuccess": "Signed in successfully. Identity has been updated.",
    "state.authLogoutSuccess": "Signed out.",
    "state.traceEmpty": "No trace data yet. Run an action to start correlation.",
    "state.memoryOpsEmpty": "No memory operation details yet.",
    "state.groupEmpty": "No companions yet, planning as a solo trip.",
    "state.festivalEmpty": "No holiday ideas available right now.",
    "state.calendarEmpty": "No plan yet for this day. Click \"Edit day plan\" to add one.",
    "state.personaCompactHint": "MBTI is now compact. Search and switch quickly.",
    "state.profileSaved": "Profile saved.",
    "state.knowledgeSaved": "Knowledge document persisted.",
    "state.planGenerated": "Generated \"{title}\" with {tasks} tasks and {confirmations} confirmation gates.",
    "state.personaApplied": "Switched to \"{name}\" and loaded the persona profile.",
    "state.resumeWaiting": "The decision was saved, but the run is still waiting on other confirmations.",
    "state.resumeBlocked": "A confirmation was rejected, so the run is now blocked.",
    "state.resumeApplied": "Confirmation is complete and external writes have resumed.",
    "assistant.highlights": "Highlights",
    "assistant.mode": "Mode",
    "assistant.planId": "Plan ID",
    "assistant.runId": "Run ID",
    "runtime.live": "Live",
    "runtime.fallback": "Fallback",
    "runtime.provider": "Provider",
    "runtime.model": "Model",
    "runtime.summary": "Summary",
    "architecture.deploymentMode": "Deployment mode",
    "architecture.persistenceMode": "Persistence mode",
    "architecture.database": "Primary database",
    "architecture.ragStore": "RAG store",
    "architecture.sessionStore": "Session store",
    "architecture.topology": "Topology",
    "architecture.travelSearch": "Travel search",
    "architecture.travelSpecialist": "Travel specialist mode",
    "architecture.notes": "Notes",
    "rag.enabled": "Vector enabled",
    "rag.vectorReady": "Vector ready",
    "rag.retrievalMode": "Retrieval mode",
    "rag.store": "Store",
    "rag.provider": "Embedding provider",
    "rag.modelName": "Embedding model",
    "rag.dimensions": "Vector dimensions",
    "rag.summary": "Summary",
    "sequence.user.title": "User intent",
    "sequence.user.meta": "Goal intake",
    "sequence.orchestrator.title": "Orchestrator",
    "sequence.orchestrator.meta": "Task split",
    "sequence.rag.title": "Hybrid RAG",
    "sequence.rag.meta": "Grounding",
    "sequence.search.title": "Travel search",
    "sequence.search.meta": "Live enrichment",
    "sequence.a2a.title": "Remote specialist",
    "sequence.a2a.meta": "A2A specialist",
    "sequence.hitl.title": "Approval gate",
    "sequence.hitl.meta": "Human in the loop",
    "sequence.persist.title": "Persistence",
    "sequence.persist.meta": "State writes",
    "sequence.emptyPrompt": "Waiting for the next user goal to enter the orchestrator.",
    "sequence.hitlWaiting": "Pending confirmations will pause external writes.",
    "sequence.hitlClear": "No confirmations are pending, so execution can continue.",
    "sequence.persistSummary": "Plans, runs, confirmations, and knowledge documents are stored in the database.",
    "sequence.nextAction": "Next suggested action",
    "sequence.actionDraft": "Start with your travel goal, or apply a holiday POI suggestion.",
    "sequence.actionCalendar": "Review the calendar and add day-level highlights to avoid conflicts.",
    "sequence.actionGroup": "If traveling with others, add companion preferences before planning.",
    "sequence.actionRun": "Run \"Create Action Plan\" to generate executable tasks and gates.",
    "sequence.actionConfirm": "Approve pending confirmations to continue external writes safely.",
    "calendar.view.month": "Month",
    "calendar.view.year": "Year",
    "calendar.view.day": "Day",
    "calendar.editPrompt": "Enter the main plan for {date}",
    "calendar.groupSummary": "{count} planned day(s)",
    "calendar.sheet.title": "Edit Day Plan",
    "calendar.sheet.subtitle": "Capture highlights, pace, and constraints for this day.",
    "calendar.sheet.placeholder": "Example: Morning in Asakusa/Ueno, afternoon recovery window, calm evening walk.",
    "calendar.sheet.length": "{count} characters",
    "calendar.sheet.template.relaxed": "Keep the morning light, reserve an afternoon recovery window, one evening activity only.",
    "calendar.sheet.template.efficient": "Route by district to reduce transfers, leave a 20-minute buffer every 2 hours.",
    "calendar.sheet.template.family": "Prioritize indoor family-friendly spots, fixed midday break, no long night transfers.",
    "group.style.culture": "Culture",
    "group.style.food": "Food",
    "group.style.family": "Family-friendly",
    "group.style.photo": "Photo spots",
    "group.style.night": "Night life",
    "hub.step.poi": "Holiday inspiration",
    "hub.step.calendar": "Calendar planning",
    "hub.step.group": "Companion sync",
    "hub.step.run": "Execution",
    "hub.ready": "Readiness",
    "hub.next": "Next",
    "hub.next.poi": "Pick one POI inspiration and apply it to the goal.",
    "hub.next.calendar": "Add at least one day plan in the calendar.",
    "hub.next.group": "If not solo, add companion preferences first.",
    "hub.next.run": "Ready to go. Generate the executable plan.",
    "ops.card.target": "Service Targets",
    "ops.card.traffic": "Traffic and Capacity",
    "ops.card.reliability": "Reliability",
    "ops.card.experience": "User Experience",
    "ops.targetPeakQps": "Peak QPS",
    "ops.availabilitySlo": "Availability SLO",
    "ops.requestsPerMinute": "Requests per minute",
    "ops.totalRequests": "Total requests",
    "ops.pendingConfirmations": "Pending approvals",
    "ops.activeSessions": "Active sessions",
    "ops.activeContexts": "Active contexts",
    "ops.recentTraces": "Recent trace events",
    "ops.previewP95": "Plan preview P95",
    "ops.resumeP95": "Resume P95",
    "ops.confirmationP95": "Approval decision P95",
    "ops.uxBootstrapP95": "Page bootstrap P95",
    "ops.uxInteractionP95": "Interaction feedback P95",
    "ops.capacityOk": "Capacity healthy",
    "ops.capacityRisk": "Near capacity threshold",
    "ops.sloOk": "SLO healthy",
    "ops.sloRisk": "SLO needs attention",
    "label.owner": "Owner",
    "label.status": "Status",
    "label.comment": "Comment",
    "label.locale": "Locale",
    "label.persona": "Persona",
    "label.trustTier": "Trust tier",
    "label.allowlist": "Allowlist",
    "label.outcome": "Outcome",
    "label.userId": "User",
    "label.thread": "Thread",
    "label.session": "Session",
    "label.context": "Context",
    "label.trace": "Trace",
    "label.timestamp": "Timestamp",
    "common.enabled": "Enabled",
    "common.disabled": "Disabled",
    "security.workspaceTrusted": "Workspace trust",
    "security.mcpTrusted": "MCP trust",
    "security.network": "Outbound network",
    "security.writeApproval": "Write approvals",
    "security.summary": "Summary",
    "security.allowlist": "Outbound allowlist",
    "security.policies": "Tool permissions",
    "persona.prompt": "Typical intent",
    "persona.description": "Description",
    "persona.mbti": "MBTI",
    "persona.temperament": "Temperament",
    "persona.decisionLens": "Decision lens",
    "option.allTemperaments": "All temperaments",
    "trust.restricted": "Restricted",
    "trust.guarded": "Guarded",
    "trust.trusted": "Trusted",
    "trust.operator": "Operator",
    "access.allowed": "Allowed",
    "access.denied": "Denied",
    "access.approval-required": "Approval required",
    "access.seeded-only": "Seeded only",
    "access.guarded-live": "Guarded live",
    "access.guarded-auto": "Guarded auto",
    "outcome.SUCCESS": "Success",
    "outcome.FAILURE": "Failure",
    "outcome.DENIED": "Denied",
    "outcome.APPROVED": "Approved",
    "outcome.REJECTED": "Rejected",
    "outcome.RESUMED": "Resumed",
    "outcome.WAITING": "Waiting",
    "outcome.BLOCKED": "Blocked",
    "status.PENDING": "Pending",
    "status.READY_FOR_CONFIRMATION": "Ready for confirmation",
    "status.SCHEDULED": "Scheduled",
    "status.DONE": "Done",
    "status.PENDING_CONFIRMATION": "Pending confirmation",
    "status.PENDING_APPROVAL": "Pending approval",
    "status.APPROVED": "Approved",
    "status.REJECTED": "Rejected",
    "status.PENDING_RUNTIME": "Pending runtime",
    "status.ACTIVE": "Active",
    "status.COMPLETED": "Completed",
    "status.BLOCKED": "Blocked"
  }
};

const state = {
  locale: document.body.dataset.defaultLocale || "zh-CN",
  surface: document.body.dataset.surface || "toc",
  surfacePinnedByHost: false,
  tocDensity: "focus",
  hostApp: "browser",
  latestRunId: null,
  latestPlanId: null,
  latestAssistantReply: null,
  activePersonaId: null,
  architecture: null,
  rag: null,
  runtime: null,
  operations: null,
  personas: [],
  personaSearch: "",
  personaTemperament: "all",
  personaExpanded: false,
  festivalFeed: [],
  festivalIndex: 0,
  festivalMediaMode: "image",
  festivalTimer: null,
  calendarView: "month",
  calendarAnchor: null,
  selectedDate: null,
  dayPlans: {},
  groupMembers: [],
  festivalBound: false,
  calendarBound: false,
  groupBound: false,
  hubBound: false,
  editingDate: null,
  securityOverview: null,
  auditEntries: [],
  connectors: [],
  traceEvents: [],
  traceFilter: {
    userId: "",
    sessionId: "",
    contextId: "",
    traceId: "",
    operation: ""
  },
  auth: {
    token: "",
    user: null
  },
  confirmationCount: 0,
  requestContext: {
    sessionId: "",
    contextId: "",
    traceId: ""
  },
  identityRefreshTimer: null
};

const PLAN_HISTORY_LIMIT = 8;
const CONFIRMATION_LIST_LIMIT = 6;
const FESTIVAL_ROTATE_MS = 12000;
const MAX_GROUP_MEMBERS = 8;
const TRACE_LIST_LIMIT = 24;
const AUTH_TOKEN_STORAGE_KEY = "otterlife-auth-token";

const FESTIVAL_LIBRARY = {
  "zh-CN": [
    { id: "labor-day", date: "2026-05-01", name: "五一假期", city: "上海", pois: ["武康路", "愚园路", "苏州河步道"], vibe: "城市漫游 + 轻体力夜游", imageUrl: "https://images.unsplash.com/photo-1549692520-acc6669e2f0c?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-5s.mp4" },
    { id: "dragon-boat", date: "2026-06-19", name: "端午假期", city: "杭州", pois: ["西湖环线", "河坊街", "良渚博物院"], vibe: "湖景慢节奏 + 文化体验", imageUrl: "https://images.unsplash.com/photo-1517309230475-6736d926b979?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-10s.mp4" },
    { id: "summer-weekend", date: "2026-07-11", name: "盛夏周末", city: "青岛", pois: ["八大关", "小麦岛", "奥帆中心"], vibe: "海风散步 + 傍晚观景", imageUrl: "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-5s.mp4" },
    { id: "mid-autumn", date: "2026-09-25", name: "中秋假期", city: "苏州", pois: ["平江路", "拙政园", "金鸡湖"], vibe: "园林夜景 + 月下散步", imageUrl: "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-10s.mp4" },
    { id: "national-day", date: "2026-10-01", name: "国庆假期", city: "成都", pois: ["宽窄巷子", "东郊记忆", "望江楼公园"], vibe: "美食密度 + 慢生活节奏", imageUrl: "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-5s.mp4" }
  ],
  "en-US": [
    { id: "labor-day", date: "2026-05-01", name: "Labor Day Break", city: "Shanghai", pois: ["Wukang Rd", "Yuyuan Rd", "Suzhou Creek Walk"], vibe: "Urban stroll + low-fatigue evenings", imageUrl: "https://images.unsplash.com/photo-1549692520-acc6669e2f0c?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-5s.mp4" },
    { id: "dragon-boat", date: "2026-06-19", name: "Dragon Boat Holiday", city: "Hangzhou", pois: ["West Lake Loop", "Hefang Street", "Liangzhu Museum"], vibe: "Lake-side pace + culture", imageUrl: "https://images.unsplash.com/photo-1517309230475-6736d926b979?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-10s.mp4" },
    { id: "summer-weekend", date: "2026-07-11", name: "Summer Weekend", city: "Qingdao", pois: ["Badaguan", "Xiaomai Island", "Olympic Sailing Center"], vibe: "Sea breeze + sunset route", imageUrl: "https://images.unsplash.com/photo-1500375592092-40eb2168fd21?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-5s.mp4" },
    { id: "mid-autumn", date: "2026-09-25", name: "Mid-Autumn Holiday", city: "Suzhou", pois: ["Pingjiang Road", "Humble Administrator's Garden", "Jinji Lake"], vibe: "Gardens + moonlight walk", imageUrl: "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-10s.mp4" },
    { id: "national-day", date: "2026-10-01", name: "National Day Golden Week", city: "Chengdu", pois: ["Kuanzhai Alley", "Eastern Suburb Memory", "Wangjianglou Park"], vibe: "Food-focused + relaxed rhythm", imageUrl: "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?auto=format&fit=crop&w=1400&q=80", videoUrl: "https://samplelib.com/lib/preview/mp4/sample-5s.mp4" }
  ]
};

document.addEventListener("DOMContentLoaded", () => {
  const bootStartedAt = performance.now();
  const today = toIsoDate(new Date());
  state.calendarAnchor = today;
  state.selectedDate = today;
  initHostEmbedding();
  restoreAuthState();
  applyTranslations();
  prepareMotion();
  restoreSurfaceMode();
  restoreTocDensity();
  bindSurfaceSwitch();
  bindTocDensitySwitch();
  bindIdentityInputs();
  bindAuthActions();
  bindTraceFilters();
  bindActionButton("run-preview", runPreview);
  bindActionButton("run-agent", runAssistant);
  bindActionButton("h5-run-preview", runPreview);
  bindActionButton("h5-run-agent", runAssistant);
  bindActionButton("reload-data", bootstrap);
  bindActionButton("save-profile", saveProfile);
  bindActionButton("add-knowledge", addKnowledge);
  bindActionButton("auth-register", registerWithPassword);
  bindActionButton("auth-login", loginWithPassword);
  bindActionButton("auth-logout", logoutCurrentUser);
  bindActionButton("trace-filter-apply", applyTraceFilters);
  bindActionButton("trace-filter-reset", resetTraceFilters);
  refreshContextStrip();
  document.getElementById("profile-view").textContent = t("state.profileLoading");
  bootstrap()
    .then(async () => {
      markReady();
      postHostEvent("lifeos_ready", {
        userId: currentUserId(),
        surface: state.surface,
        locale: state.locale
      });
      await sendUxMetric("page_bootstrap", bootStartedAt, true);
    })
    .catch(async error => {
      await sendUxMetric("page_bootstrap", bootStartedAt, false);
      handleError(error);
    });
});

async function bootstrap() {
  await hydrateAuthIdentity();
  await Promise.all([
    loadArchitecture(),
    loadRagStatus(),
    loadRuntime(),
    loadOperations(),
    loadTraceLinks(),
    loadPersonas(),
    loadConnectors(),
    loadFestivalFeed()
  ]);
  initializeDefaultPersona();
  loadWorkbenchState();
  initFestivalExperience();
  initCalendarExperience();
  initGroupExperience();
  initJourneyHub();
  initDayPlanSheet();
  // Hard-close any residual day-plan overlay state on bootstrap / 启动时强制收起弹层，避免历史状态或缓存导致首屏被遮挡。
  closeDayPlanSheet();
  renderFestivalView();
  renderCalendarView();
  renderGroupMembers();
  renderJourneyHub();
  await Promise.all([
    loadSecurityOverview(),
    loadAuditTrail(),
    loadModules(),
    loadProfile(),
    loadKnowledge(),
    loadConfirmations(),
    loadPlans()
  ]);
  renderSequence();
}

function restoreAuthState() {
  state.auth.token = window.localStorage.getItem(AUTH_TOKEN_STORAGE_KEY) || "";
  state.auth.user = null;
}

function bindAuthActions() {
  const usernameInput = document.getElementById("auth-username");
  const passwordInput = document.getElementById("auth-password");
  if (!usernameInput || !passwordInput) {
    return;
  }

  passwordInput.addEventListener("keydown", event => {
    if (event.key === "Enter") {
      event.preventDefault();
      runSafely(() => loginWithPassword());
    }
  });

  usernameInput.addEventListener("keydown", event => {
    if (event.key === "Enter") {
      event.preventDefault();
      passwordInput.focus();
    }
  });

  renderAuthStatus();
}

async function hydrateAuthIdentity() {
  if (!state.auth.token) {
    state.auth.user = null;
    renderAuthStatus();
    syncTraceFilterInputs();
    return;
  }
  try {
    const me = await api("/api/v1/auth/me");
    state.auth.user = me;
    const usernameInput = document.getElementById("auth-username");
    if (usernameInput && me.username) {
      usernameInput.value = me.username;
    }
    const userInput = document.getElementById("user-id");
    if (userInput && me.userId) {
      userInput.value = me.userId;
    }
    if (!state.traceFilter.userId && me.userId) {
      state.traceFilter.userId = me.userId;
    }
    syncTraceFilterInputs();
    renderAuthStatus();
  } catch (error) {
    clearAuthState();
    renderAuthStatus(t("state.authExpired"));
  }
}

function clearAuthState() {
  state.auth.token = "";
  state.auth.user = null;
  window.localStorage.removeItem(AUTH_TOKEN_STORAGE_KEY);
}

function renderAuthStatus(explicitMessage) {
  const status = document.getElementById("auth-status");
  if (!status) {
    return;
  }
  const message = explicitMessage || (state.auth.user
    ? t("state.authLoggedIn", {
      name: state.auth.user.displayName || state.auth.user.username || state.auth.user.userId || "user",
      userId: state.auth.user.userId || "unknown"
    })
    : t("state.authGuest"));
  status.textContent = message;
}

async function registerWithPassword() {
  const usernameInput = document.getElementById("auth-username");
  const passwordInput = document.getElementById("auth-password");
  const username = usernameInput?.value.trim() || "";
  const password = passwordInput?.value.trim() || "";
  if (!username || !password) {
    throw new Error("missing_username_or_password");
  }

  const response = await api("/api/v1/auth/register", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    skipAuth: true,
    body: JSON.stringify({
      username,
      password,
      displayName: username,
      locale: state.locale
    })
  });
  await applyAuthSession(response);
}

async function loginWithPassword() {
  const usernameInput = document.getElementById("auth-username");
  const passwordInput = document.getElementById("auth-password");
  const username = usernameInput?.value.trim() || "";
  const password = passwordInput?.value.trim() || "";
  if (!username || !password) {
    throw new Error("missing_username_or_password");
  }

  const response = await api("/api/v1/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    skipAuth: true,
    body: JSON.stringify({ username, password })
  });
  await applyAuthSession(response);
}

async function applyAuthSession(session) {
  if (!session?.token) {
    throw new Error("invalid_auth_response");
  }

  state.auth.token = session.token;
  state.auth.user = {
    userId: session.userId,
    username: session.username,
    displayName: session.displayName,
    locale: session.locale,
    sessionExpiresAt: session.expiresAt
  };
  window.localStorage.setItem(AUTH_TOKEN_STORAGE_KEY, session.token);
  const userInput = document.getElementById("user-id");
  if (userInput && session.userId) {
    userInput.value = session.userId;
  }
  const usernameInput = document.getElementById("auth-username");
  const passwordInput = document.getElementById("auth-password");
  if (usernameInput && session.username) {
    usernameInput.value = session.username;
  }
  if (passwordInput) {
    passwordInput.value = "";
  }
  if (session.userId) {
    state.traceFilter.userId = session.userId;
  }
  syncTraceFilterInputs();
  renderAuthStatus();
  document.getElementById("preview-summary").textContent = t("state.authLoginSuccess");
  flashElement("preview-summary", "is-updated");
  await handleIdentityChange();
  await loadOperations();
}

async function logoutCurrentUser() {
  if (state.auth.token) {
    try {
      await api("/api/v1/auth/logout", { method: "POST" });
    } catch (error) {
      // Local logout should still succeed even if server token is already expired.
      console.debug("auth logout request failed", error);
    }
  }
  clearAuthState();
  state.traceFilter.userId = "";
  syncTraceFilterInputs();
  renderAuthStatus(t("state.authLogoutSuccess"));
  await handleIdentityChange();
  await loadOperations();
}

function bindTraceFilters() {
  ["trace-filter-user", "trace-filter-session", "trace-filter-context", "trace-filter-trace", "trace-filter-operation"]
    .map(id => document.getElementById(id))
    .filter(Boolean)
    .forEach(input => {
      input.addEventListener("keydown", event => {
        if (event.key === "Enter") {
          event.preventDefault();
          runSafely(() => applyTraceFilters());
        }
      });
    });
  syncTraceFilterInputs();
}

function readTraceFilterInputs() {
  state.traceFilter.userId = document.getElementById("trace-filter-user")?.value.trim() || "";
  state.traceFilter.sessionId = document.getElementById("trace-filter-session")?.value.trim() || "";
  state.traceFilter.contextId = document.getElementById("trace-filter-context")?.value.trim() || "";
  state.traceFilter.traceId = document.getElementById("trace-filter-trace")?.value.trim() || "";
  state.traceFilter.operation = document.getElementById("trace-filter-operation")?.value.trim() || "";
}

function syncTraceFilterInputs() {
  const authUserId = state.auth.user?.userId || "";
  const values = {
    "trace-filter-user": state.traceFilter.userId || authUserId,
    "trace-filter-session": state.traceFilter.sessionId || "",
    "trace-filter-context": state.traceFilter.contextId || "",
    "trace-filter-trace": state.traceFilter.traceId || "",
    "trace-filter-operation": state.traceFilter.operation || ""
  };
  Object.entries(values).forEach(([id, value]) => {
    const input = document.getElementById(id);
    if (input) {
      input.value = value;
    }
  });
}

async function applyTraceFilters() {
  readTraceFilterInputs();
  await loadTraceLinks();
}

async function resetTraceFilters() {
  state.traceFilter = {
    userId: state.auth.user?.userId || "",
    sessionId: "",
    contextId: "",
    traceId: "",
    operation: ""
  };
  syncTraceFilterInputs();
  await loadTraceLinks();
}

// Keep motion setup centralized so new cards inherit the same stagger / 集中管理入场动效，后续新增卡片会自动继承节奏。
function prepareMotion() {
  document.querySelectorAll(".hero-copy, .hero-panel, .card").forEach((panel, index) => {
    panel.style.setProperty("--stagger", `${index * 70}ms`);
  });
}

function initHostEmbedding() {
  const hostConfig = window.__LIFEOS_H5_CONFIG__ && typeof window.__LIFEOS_H5_CONFIG__ === "object"
    ? window.__LIFEOS_H5_CONFIG__
    : {};
  const params = new URLSearchParams(window.location.search);

  const locale = params.get("locale") || hostConfig.locale;
  if (locale && TRANSLATIONS[locale]) {
    state.locale = locale;
    document.body.dataset.defaultLocale = locale;
  }

  const surface = params.get("surface") || hostConfig.surface;
  if (surface === "toc" || surface === "tob") {
    state.surface = surface;
    state.surfacePinnedByHost = true;
  }

  const userId = params.get("userId") || hostConfig.userId;
  const threadId = params.get("threadId") || hostConfig.threadId;
  if (userId) {
    const userInput = document.getElementById("user-id");
    if (userInput) {
      userInput.value = userId;
    }
  }
  if (threadId) {
    const threadInput = document.getElementById("thread-id");
    if (threadInput) {
      threadInput.value = threadId;
    }
  }

  state.hostApp = detectHostApp();
  document.body.dataset.hostApp = state.hostApp;
  initializeRequestContext(params, hostConfig);

  window.LifeOsH5 = {
    setIdentity(payload = {}) {
      if (typeof payload.userId === "string" && payload.userId.trim()) {
        document.getElementById("user-id").value = payload.userId.trim();
      }
      if (typeof payload.threadId === "string" && payload.threadId.trim()) {
        document.getElementById("thread-id").value = payload.threadId.trim();
      }
      if (typeof payload.locale === "string" && TRANSLATIONS[payload.locale]) {
        state.locale = payload.locale;
        applyTranslations();
      }
      if (payload.refresh === true) {
        runSafely(() => handleIdentityChange());
      } else {
        refreshContextStrip();
      }
    },
    setContext(payload = {}) {
      state.requestContext.sessionId = payload.sessionId || state.requestContext.sessionId;
      state.requestContext.contextId = payload.contextId || state.requestContext.contextId;
      state.requestContext.traceId = payload.traceId || state.requestContext.traceId;
      refreshContextStrip();
    },
    switchSurface(surfaceTarget) {
      applySurfaceMode(surfaceTarget);
    },
    refresh() {
      return runSafely(() => bootstrap());
    }
  };
}

function detectHostApp() {
  if (window.ReactNativeWebView) {
    return "react-native";
  }
  if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.lifeos) {
    return "ios-webkit";
  }
  if (window.AppBridge && typeof window.AppBridge.postMessage === "function") {
    return "android-bridge";
  }
  return "browser";
}

function initializeRequestContext(params, hostConfig) {
  const explicitSession = params.get("sessionId") || hostConfig.sessionId;
  const explicitContext = params.get("contextId") || hostConfig.contextId;
  const explicitTrace = params.get("traceId") || hostConfig.traceId;
  const userId = currentUserId();
  const storageKey = `otterlife-h5-session:${userId}`;
  const persistedSession = window.localStorage.getItem(storageKey);
  const sessionId = explicitSession || persistedSession || `sess-${shortId(cryptoRandomId())}`;
  state.requestContext.sessionId = sessionId;
  state.requestContext.contextId = explicitContext || `ctx-${shortId(cryptoRandomId())}`;
  state.requestContext.traceId = explicitTrace || `trc-${shortId(cryptoRandomId())}`;
  window.localStorage.setItem(storageKey, sessionId);
}

function restoreTocDensity() {
  const saved = window.localStorage.getItem("life-os-toc-density");
  if (saved === "focus" || saved === "studio") {
    state.tocDensity = saved;
  }
  applyTocDensity(state.tocDensity);
}

function bindTocDensitySwitch() {
  document.querySelectorAll("[data-density-target]").forEach(button => {
    button.addEventListener("click", () => {
      applyTocDensity(button.dataset.densityTarget);
    });
  });
}

function applyTocDensity(mode) {
  state.tocDensity = mode === "studio" ? "studio" : "focus";
  document.body.dataset.tocDensity = state.tocDensity;
  window.localStorage.setItem("life-os-toc-density", state.tocDensity);
  document.querySelectorAll("[data-density-target]").forEach(button => {
    button.classList.toggle("is-active", button.dataset.densityTarget === state.tocDensity);
  });
}

function rotateRequestTrace(action) {
  state.requestContext.traceId = `trc-${shortId(cryptoRandomId())}`;
  if (action === "run-preview" || action === "run-agent") {
    state.requestContext.contextId = `ctx-${shortId(cryptoRandomId())}`;
  }
  refreshContextStrip();
}

function refreshContextStrip() {
  const container = document.getElementById("context-strip");
  if (!container) {
    return;
  }
  container.innerHTML = `
    <span>${escapeHtml(t("fields.contextCurrent"))}</span>
    <code>${escapeHtml(state.requestContext.sessionId || "-")}</code>
    <code>${escapeHtml(state.requestContext.contextId || "-")}</code>
    <code>${escapeHtml(state.requestContext.traceId || "-")}</code>
  `;
}

function postHostEvent(type, payload = {}) {
  const message = JSON.stringify({
    type,
    timestamp: new Date().toISOString(),
    payload
  });
  try {
    if (window.ReactNativeWebView && typeof window.ReactNativeWebView.postMessage === "function") {
      window.ReactNativeWebView.postMessage(message);
      return;
    }
    if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.lifeos) {
      window.webkit.messageHandlers.lifeos.postMessage({ type, payload });
      return;
    }
    if (window.AppBridge && typeof window.AppBridge.postMessage === "function") {
      window.AppBridge.postMessage(message);
    }
  } catch (error) {
    console.debug("host bridge post failed", error);
  }
}

function restoreSurfaceMode() {
  const saved = window.localStorage.getItem("life-os-surface");
  if (!state.surfacePinnedByHost && (saved === "toc" || saved === "tob")) {
    state.surface = saved;
  }
  applySurfaceMode(state.surface);
}

function bindSurfaceSwitch() {
  document.querySelectorAll("[data-surface-target]").forEach(button => {
    button.addEventListener("click", () => {
      applySurfaceMode(button.dataset.surfaceTarget);
      renderSequence();
    });
  });
}

// Re-sync user-scoped views when the active identity changes / 当前身份变化后，重新拉取用户维度的数据，避免跨用户状态残留。
function bindIdentityInputs() {
  const userInput = document.getElementById("user-id");
  const threadInput = document.getElementById("thread-id");
  if (!userInput) {
    return;
  }

  const scheduleRefresh = () => {
    syncActivePersona();
    window.clearTimeout(state.identityRefreshTimer);
    state.identityRefreshTimer = window.setTimeout(() => {
      runSafely(() => handleIdentityChange());
    }, 180);
  };

  userInput.addEventListener("change", scheduleRefresh);
  userInput.addEventListener("blur", scheduleRefresh);
  if (threadInput) {
    threadInput.addEventListener("input", () => {
      refreshContextStrip();
    });
  }
}

// Toggle consumer/operator views without reloading the page / 不刷新页面切换 ToC 与 ToB 视角。
function applySurfaceMode(surface) {
  state.surface = surface === "tob" ? "tob" : "toc";
  document.body.dataset.surface = state.surface;
  window.localStorage.setItem("life-os-surface", state.surface);
  document.querySelectorAll("[data-surface-target]").forEach(button => {
    button.classList.toggle("is-active", button.dataset.surfaceTarget === state.surface);
  });
  postHostEvent("surface_changed", {
    surface: state.surface
  });
}

function markReady() {
  document.body.classList.add("is-ready");
}

// Wrap every button action with loading feedback / 为操作按钮统一包一层 loading 反馈。
function bindActionButton(id, action) {
  const button = document.getElementById(id);
  if (!button) {
    return;
  }

  button.addEventListener("click", () => runSafely(() => withButtonState(button, action)));
}

async function withButtonState(button, action) {
  const startedAt = performance.now();
  rotateRequestTrace(button.id);
  button.disabled = true;
  button.classList.add("is-loading");
  try {
    await action();
    await sendUxMetric(resolveUxAction(button.id), startedAt, true);
    postHostEvent("action_success", {
      action: resolveUxAction(button.id),
      userId: currentUserId(),
      threadId: currentThreadId(),
      traceId: state.requestContext.traceId
    });
  } catch (error) {
    await sendUxMetric(resolveUxAction(button.id), startedAt, false);
    postHostEvent("action_failure", {
      action: resolveUxAction(button.id),
      userId: currentUserId(),
      threadId: currentThreadId(),
      traceId: state.requestContext.traceId,
      message: error?.message || "unknown"
    });
    throw error;
  } finally {
    button.disabled = false;
    button.classList.remove("is-loading");
  }
}

async function runPreview() {
  const payload = currentRequestPayload();
  const result = await api("/api/v1/plans/preview", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  state.latestRunId = result.executionRun.id;
  state.latestPlanId = result.plan.id;
  state.requestContext.contextId = `ctx-${shortId(result.executionRun.id)}`;
  refreshContextStrip();
  document.getElementById("preview-summary").textContent = t("state.planGenerated", {
    title: result.plan.title,
    tasks: result.plan.tasks.length,
    confirmations: result.confirmations.length
  });
  flashElement("preview-summary", "is-updated");

  renderPlan(result.plan);
  renderTimeline(result.executionRun.timeline);
  renderConfirmations(result.confirmations);
  renderSequence();
  await loadOperations();
  await loadTraceLinks();
  await loadPlans();
  await loadSecurityOverview();
  await loadAuditTrail();
}

async function runAssistant() {
  const result = await api("/api/v1/assistant/message", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(currentRequestPayload())
  });

  state.latestAssistantReply = result;
  renderAssistantReply(result);
  flashElement("assistant-view");

  if (result.runId) {
    state.latestRunId = result.runId;
    state.requestContext.contextId = `ctx-${shortId(result.runId)}`;
    refreshContextStrip();
    await loadTimeline(result.runId);
  }
  if (result.planId) {
    state.latestPlanId = result.planId;
    await loadPlans();
  }
  await loadOperations();
  await loadTraceLinks();
  await loadSecurityOverview();
  await loadAuditTrail();
  renderSequence();
}

async function loadArchitecture() {
  state.architecture = await api("/api/v1/system/architecture");
  renderArchitecture(state.architecture);
  renderSequence();
}

async function loadOperations() {
  state.operations = await api("/api/v1/system/operations");
  renderOperations(state.operations);
}

async function loadTraceLinks() {
  try {
    const params = new URLSearchParams({ limit: String(TRACE_LIST_LIMIT) });
    const activeFilters = {
      userId: state.traceFilter.userId,
      sessionId: state.traceFilter.sessionId,
      contextId: state.traceFilter.contextId,
      traceId: state.traceFilter.traceId,
      operation: state.traceFilter.operation
    };
    Object.entries(activeFilters).forEach(([key, value]) => {
      if (value && value.trim()) {
        params.set(key, value.trim());
      }
    });
    state.traceEvents = await api(`/api/v1/system/trace-links?${params.toString()}`);
  } catch (error) {
    state.traceEvents = [];
  }
  renderTraceLinks(state.traceEvents);
}

async function loadPersonas() {
  state.personas = await api(`/api/v1/personas?locale=${encodeURIComponent(state.locale)}`);
  syncActivePersona();
  renderPersonas(state.personas);
}

async function loadFestivalFeed() {
  try {
    const query = document.getElementById("plan-input")?.value.trim() || "";
    const travelers = Math.max(1, (state.groupMembers?.length || 0) + 1);
    const budget = document.getElementById("budget-level")?.value || "";
    const timeWindow = state.selectedDate || "";
    const cards = await api(
      `/api/v1/poi/festivals?locale=${encodeURIComponent(state.locale)}`
      + `&query=${encodeURIComponent(query)}`
      + `&travelers=${encodeURIComponent(String(travelers))}`
      + `&budget=${encodeURIComponent(budget)}`
      + `&timeWindow=${encodeURIComponent(timeWindow)}`
    );
    state.festivalFeed = Array.isArray(cards) ? cards : [];
  } catch (error) {
    state.festivalFeed = [];
  }
}

async function loadSecurityOverview() {
  state.securityOverview = await api(`/api/v1/security/overview?userId=${encodeURIComponent(currentUserId())}&limit=8`);
  renderSecurityOverview(state.securityOverview);
}

async function loadAuditTrail() {
  const url = shouldUseGlobalAudit()
    ? "/api/v1/security/audit?limit=12"
    : `/api/v1/security/audit?userId=${encodeURIComponent(currentUserId())}&limit=12`;
  state.auditEntries = await api(url);
  renderAuditTrail(state.auditEntries);
}

async function loadRagStatus() {
  state.rag = await api("/api/v1/system/rag");
  renderRagStatus(state.rag);
  renderSequence();
}

async function loadModules() {
  const data = await api("/api/v1/modules");
  const items = Object.entries(data).map(([name, summary]) => `
    <div class="list-item">
      <strong>${escapeHtml(name)}</strong>
      <small>${escapeHtml(formatModuleSummary(name, summary))}</small>
    </div>
  `).join("");
  document.getElementById("module-list").innerHTML = items;
  animateChildren("module-list");
}

async function loadRuntime() {
  const runtime = await api("/api/v1/assistant/runtime");
  state.runtime = runtime;
  document.getElementById("runtime-view").innerHTML = `
    <div class="list-item">
      <strong>${escapeHtml(formatRuntimeMode(runtime.mode))}</strong>
      <small>${escapeHtml(formatRuntimeSummary(runtime))}</small>
    </div>
    <div class="list-item">
      <strong>${escapeHtml(t("runtime.provider"))}: ${escapeHtml(formatProvider(runtime.provider))}</strong>
      <small>${escapeHtml(t("runtime.model"))}: ${escapeHtml(formatModelName(runtime.modelName))}</small>
    </div>
  `;
  animateChildren("runtime-view");
}

async function loadProfile() {
  const userId = currentUserId();
  const data = await api(`/api/v1/profile/memory-details?userId=${encodeURIComponent(userId)}&limit=8`);
  const preferences = data.preferences || {};
  document.getElementById("travel-style").value = preferences.travelStyle || "";
  document.getElementById("budget-level").value = preferences.budgetLevel || "";
  document.getElementById("study-goal").value = preferences.studyGoal || "";
  const summary = document.getElementById("memory-summary");
  if (summary) {
    summary.textContent = data.summary || "";
  }
  renderMemoryOperations(data.recentOperations || []);
  document.getElementById("profile-view").textContent = JSON.stringify(data, null, 2);
}

async function loadKnowledge() {
  const docs = await api(`/api/v1/knowledge/documents?userId=${encodeURIComponent(currentUserId())}`);
  const items = docs.map(doc => `
    <div class="list-item">
      <strong>${escapeHtml(doc.title)}</strong>
      <small>${escapeHtml(doc.summary)}</small>
      <div class="list-item-meta">
        <span class="source-pill ${doc.userId === currentUserId() ? "is-personal" : "is-shared"}">${escapeHtml(formatKnowledgeScope(doc))}</span>
        <span class="meta-pill">${escapeHtml(t("label.locale"))}: ${escapeHtml(doc.locale || "en-US")}</span>
      </div>
    </div>
  `).join("");
  document.getElementById("knowledge-view").innerHTML = items || `<div class="empty-state">${escapeHtml(t("state.knowledgeEmpty"))}</div>`;
  animateChildren("knowledge-view");
}

async function loadConnectors() {
  const connectors = await api("/api/v1/system/connectors");
  state.connectors = connectors;
  const items = connectors.map(connector => `
    <div class="list-item">
      <strong>${escapeHtml(connector.connectorName)}</strong>
      <small>${escapeHtml(formatConnectorSummary(connector))}</small>
    </div>
  `).join("");
  document.getElementById("connectors-view").innerHTML = items;
  animateChildren("connectors-view");
  renderSequence();
}

async function loadConfirmations() {
  const confirmations = await api(`/api/v1/confirmations?userId=${encodeURIComponent(currentUserId())}`);
  renderConfirmations(confirmations);
}

async function loadPlans() {
  const plans = await api(`/api/v1/plans?userId=${encodeURIComponent(currentUserId())}`);
  const visiblePlans = plans.slice(0, PLAN_HISTORY_LIMIT);
  const overflow = plans.length > PLAN_HISTORY_LIMIT
    ? `<div class="list-note">${escapeHtml(t("state.planOverflow", { visible: PLAN_HISTORY_LIMIT, total: plans.length }))}</div>`
    : "";
  const html = visiblePlans.map(plan => `
    <div class="list-item">
      <strong>${escapeHtml(plan.title)}</strong>
      <small>${escapeHtml(plan.summary)}</small>
      <div class="actions">
        <button class="secondary" data-plan-id="${escapeHtml(plan.id)}">${escapeHtml(t("actions.openPlan"))}</button>
      </div>
    </div>
  `).join("");
  document.getElementById("plans-view").innerHTML = overflow + (html || `<div class="empty-state">${escapeHtml(t("state.planEmpty"))}</div>`);
  animateChildren("plans-view");
  document.querySelectorAll("[data-plan-id]").forEach(button => {
    button.addEventListener("click", () => runSafely(async () => {
      const plan = await api(`/api/v1/plans/${button.dataset.planId}?userId=${encodeURIComponent(currentUserId())}`);
      renderPlan(plan);
      flashElement("plan-view");
    }));
  });
}

async function loadTimeline(runId) {
  const events = await api(`/api/v1/executions/${runId}/timeline`);
  renderTimeline(events);
}

async function handleIdentityChange() {
  syncActivePersona();
  if (!state.traceFilter.userId && state.auth.user?.userId) {
    state.traceFilter.userId = state.auth.user.userId;
  }
  syncTraceFilterInputs();
  initializeRequestContext(new URLSearchParams(), {});
  refreshContextStrip();
  resetUserScopedViews();
  loadWorkbenchState();
  renderCalendarView();
  renderGroupMembers();
  await Promise.all([
    loadProfile(),
    loadKnowledge(),
    loadConfirmations(),
    loadPlans(),
    loadSecurityOverview(),
    loadAuditTrail()
  ]);
  renderSequence();
  renderFestivalView();
  renderJourneyHub();
  await loadTraceLinks();
}

function resetUserScopedViews() {
  state.latestRunId = null;
  state.latestPlanId = null;
  state.latestAssistantReply = null;
  state.confirmationCount = 0;
  document.getElementById("preview-summary").textContent = t("state.previewIdle");
  document.getElementById("profile-view").textContent = t("state.profileLoading");
  document.getElementById("assistant-view").innerHTML = `<div class="empty-state">${escapeHtml(t("state.assistantIdle"))}</div>`;
  document.getElementById("plan-view").innerHTML = `<div class="empty-state">${escapeHtml(t("state.planIdle"))}</div>`;
  document.getElementById("timeline-view").innerHTML = `<div class="empty-state">${escapeHtml(t("state.timelineEmpty"))}</div>`;
  document.getElementById("plans-view").innerHTML = `<div class="empty-state">${escapeHtml(t("state.planEmpty"))}</div>`;
  document.getElementById("confirmations-view").innerHTML = `<div class="empty-state">${escapeHtml(t("state.confirmationEmpty"))}</div>`;
  const memorySummary = document.getElementById("memory-summary");
  if (memorySummary) {
    memorySummary.textContent = "";
  }
  const memoryOps = document.getElementById("memory-ops-view");
  if (memoryOps) {
    memoryOps.innerHTML = `<div class="empty-state">${escapeHtml(t("state.memoryOpsEmpty"))}</div>`;
  }
}

function workbenchStorageKey() {
  return `otterlife-workbench:${currentUserId()}`;
}

function loadWorkbenchState() {
  const today = toIsoDate(new Date());
  const fallback = {
    calendarView: "month",
    calendarAnchor: today,
    selectedDate: today,
    dayPlans: {},
    groupMembers: []
  };
  try {
    const raw = window.localStorage.getItem(workbenchStorageKey());
    const stored = raw ? JSON.parse(raw) : {};
    state.calendarView = ["month", "year", "day"].includes(stored.calendarView) ? stored.calendarView : fallback.calendarView;
    state.calendarAnchor = stored.calendarAnchor || fallback.calendarAnchor;
    state.selectedDate = stored.selectedDate || fallback.selectedDate;
    state.dayPlans = stored.dayPlans && typeof stored.dayPlans === "object" ? stored.dayPlans : {};
    state.groupMembers = Array.isArray(stored.groupMembers) ? stored.groupMembers.slice(0, MAX_GROUP_MEMBERS) : [];
  } catch (error) {
    state.calendarView = fallback.calendarView;
    state.calendarAnchor = fallback.calendarAnchor;
    state.selectedDate = fallback.selectedDate;
    state.dayPlans = fallback.dayPlans;
    state.groupMembers = fallback.groupMembers;
  }
}

function persistWorkbenchState() {
  const payload = {
    calendarView: state.calendarView,
    calendarAnchor: state.calendarAnchor,
    selectedDate: state.selectedDate,
    dayPlans: state.dayPlans,
    groupMembers: state.groupMembers
  };
  window.localStorage.setItem(workbenchStorageKey(), JSON.stringify(payload));
}

function initFestivalExperience() {
  const container = document.getElementById("festival-view");
  if (!container) {
    return;
  }
  if (!state.festivalBound) {
    container.addEventListener("click", event => {
      const button = event.target.closest("[data-festival-action]");
      if (!button) {
        return;
      }
      const action = button.dataset.festivalAction;
      if (action === "prev") {
        shiftFestival(-1);
      } else if (action === "next") {
        shiftFestival(1);
      } else if (action === "apply") {
        applyFestivalToPrompt();
      } else if (action === "image" || action === "video") {
        state.festivalMediaMode = action;
        renderFestivalView();
      }
    });
    state.festivalBound = true;
  }
  if (state.festivalTimer) {
    window.clearInterval(state.festivalTimer);
  }
  state.festivalTimer = window.setInterval(() => {
    if (document.body.dataset.surface === "toc") {
      shiftFestival(1, true);
    }
  }, FESTIVAL_ROTATE_MS);
}

function renderFestivalView() {
  const container = document.getElementById("festival-view");
  if (!container) {
    return;
  }
  const feed = state.festivalFeed.length > 0 ? state.festivalFeed : resolveFestivalFeed();
  if (feed.length === 0) {
    container.innerHTML = `<div class="empty-state">${escapeHtml(t("state.festivalEmpty"))}</div>`;
    return;
  }
  state.festivalFeed = feed;
  state.festivalIndex = ((state.festivalIndex % feed.length) + feed.length) % feed.length;
  const current = feed[state.festivalIndex];
  const poiHtml = current.pois.map(poi => `<span class="festival-poi">${escapeHtml(poi)}</span>`).join("");
  const mediaToggle = `
    <div class="festival-media-toggle">
      <button type="button" class="secondary ${state.festivalMediaMode === "image" ? "is-active" : ""}" data-festival-action="image">${escapeHtml(t("actions.showImage"))}</button>
      <button type="button" class="secondary ${state.festivalMediaMode === "video" ? "is-active" : ""}" data-festival-action="video">${escapeHtml(t("actions.showVideo"))}</button>
    </div>
  `;
  const mediaView = state.festivalMediaMode === "video" && current.videoUrl
    ? `<video class="festival-media" src="${escapeHtml(current.videoUrl)}" controls muted playsinline></video>`
    : current.imageUrl
      ? `<img class="festival-media" src="${escapeHtml(current.imageUrl)}" alt="${escapeHtml(current.name)}">`
      : "";
  const sourceText = formatPoiSource(current.source || "seeded");
  container.innerHTML = `
    <article class="festival-card-panel">
      <div class="festival-head">
        <div>
          <h3>${escapeHtml(current.name)} · ${escapeHtml(current.city)}</h3>
          <p>${escapeHtml(current.vibe)}</p>
        </div>
        <span class="meta-pill">${escapeHtml(current.date)} · ${escapeHtml(sourceText)}</span>
      </div>
      ${mediaToggle}
      ${mediaView}
      <div class="festival-pois">${poiHtml}</div>
      <div class="actions">
        <button class="secondary" type="button" data-festival-action="prev">${escapeHtml(t("actions.prev"))}</button>
        <button class="secondary" type="button" data-festival-action="next">${escapeHtml(t("actions.next"))}</button>
        <button class="primary" type="button" data-festival-action="apply">${escapeHtml(t("actions.applyFestivalPoi"))}</button>
      </div>
    </article>
  `;
}

function resolveFestivalFeed() {
  const bundle = FESTIVAL_LIBRARY[state.locale] || FESTIVAL_LIBRARY["en-US"];
  const today = new Date(`${toIsoDate(new Date())}T00:00:00`);
  const upcoming = bundle.filter(item => new Date(`${item.date}T00:00:00`) >= today);
  if (upcoming.length >= 3) {
    return upcoming.slice(0, 5);
  }
  const history = bundle.filter(item => new Date(`${item.date}T00:00:00`) < today);
  return [...upcoming, ...history].slice(0, 5);
}

function shiftFestival(delta, auto = false) {
  if (state.festivalFeed.length === 0) {
    return;
  }
  state.festivalIndex = (state.festivalIndex + delta + state.festivalFeed.length) % state.festivalFeed.length;
  renderFestivalView();
  if (auto) {
    const element = document.querySelector("#festival-view .festival-card-panel");
    if (element) {
      flashElement(element, "is-festival-flash");
    }
  }
  renderSequence();
}

function applyFestivalToPrompt() {
  if (state.festivalFeed.length === 0) {
    return;
  }
  const selected = state.festivalFeed[state.festivalIndex];
  const suggestion = isChineseLocale()
    ? `围绕${selected.name}${selected.city}的 ${selected.pois.join("、")} 规划 3 天低疲劳路线，晚上保留恢复时段。`
    : `Build a 3-day low-fatigue itinerary around ${selected.name} in ${selected.city}, covering ${selected.pois.join(", ")} with calm evenings.`;
  document.getElementById("plan-input").value = suggestion;
  document.getElementById("preview-summary").textContent = suggestion;
  flashElement("preview-summary", "is-updated");
  renderSequence();
}

function initCalendarExperience() {
  if (!state.calendarBound) {
    document.querySelectorAll("[data-calendar-view]").forEach(button => {
      button.addEventListener("click", () => {
        state.calendarView = button.dataset.calendarView;
        persistWorkbenchState();
        renderCalendarView();
        renderSequence();
      });
    });

    const prev = document.getElementById("calendar-prev");
    const next = document.getElementById("calendar-next");
    if (prev) {
      prev.addEventListener("click", () => {
        shiftCalendar(-1);
      });
    }
    if (next) {
      next.addEventListener("click", () => {
        shiftCalendar(1);
      });
    }

    const calendarView = document.getElementById("calendar-view");
    if (calendarView) {
      calendarView.addEventListener("click", event => {
        const inlineAction = event.target.closest("[data-calendar-action]")?.dataset.calendarAction;
        if (inlineAction === "edit") {
          editDayPlan();
          return;
        }
        const dayButton = event.target.closest("[data-date]");
        if (dayButton) {
          state.selectedDate = dayButton.dataset.date;
          renderCalendarView();
          persistWorkbenchState();
          renderSequence();
          return;
        }
        const monthButton = event.target.closest("[data-month]");
        if (monthButton) {
          state.calendarAnchor = toIsoDate(new Date(Number(monthButton.dataset.year), Number(monthButton.dataset.month), 1));
          state.calendarView = "month";
          persistWorkbenchState();
          renderCalendarView();
        }
      });
    }

    const detail = document.getElementById("calendar-detail");
    if (detail) {
      detail.addEventListener("click", event => {
        const action = event.target.closest("[data-calendar-action]")?.dataset.calendarAction;
        if (action === "edit") {
          editDayPlan();
        }
        if (action === "clear") {
          delete state.dayPlans[state.selectedDate];
          persistWorkbenchState();
          renderCalendarView();
          renderSequence();
        }
      });
    }
    state.calendarBound = true;
  }
}

function renderCalendarView() {
  const container = document.getElementById("calendar-view");
  const detail = document.getElementById("calendar-detail");
  const range = document.getElementById("calendar-range-label");
  if (!container || !detail || !range) {
    return;
  }

  document.querySelectorAll("[data-calendar-view]").forEach(button => {
    button.classList.toggle("is-active", button.dataset.calendarView === state.calendarView);
  });

  range.textContent = calendarRangeLabel();
  if (state.calendarView === "year") {
    container.innerHTML = renderCalendarYear();
  } else if (state.calendarView === "day") {
    container.innerHTML = renderCalendarDay();
  } else {
    container.innerHTML = renderCalendarMonth();
  }

  const plan = state.dayPlans[state.selectedDate];
  detail.innerHTML = `
    <div class="list-item">
      <strong>${escapeHtml(state.selectedDate)}</strong>
      <small>${escapeHtml(plan || t("state.calendarEmpty"))}</small>
      <div class="actions">
        <button type="button" class="secondary" data-calendar-action="edit">${escapeHtml(t("actions.editDayPlan"))}</button>
        <button type="button" class="secondary" data-calendar-action="clear">${escapeHtml(t("actions.clearDayPlan"))}</button>
      </div>
    </div>
  `;
  flashElement("calendar-detail", "is-calendar-updated");
}

function renderCalendarMonth() {
  const anchor = new Date(`${state.calendarAnchor}T00:00:00`);
  const year = anchor.getFullYear();
  const month = anchor.getMonth();
  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();
  const weekdayLabels = isChineseLocale()
    ? ["日", "一", "二", "三", "四", "五", "六"]
    : ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

  const cells = [];
  for (let i = 0; i < firstDay; i += 1) {
    cells.push(`<span class="calendar-blank"></span>`);
  }
  for (let day = 1; day <= daysInMonth; day += 1) {
    const date = toIsoDate(new Date(year, month, day));
    const note = state.dayPlans[date] || "";
    const shortNote = note.length > 16 ? `${note.slice(0, 16)}...` : note;
    cells.push(`
      <button type="button" class="calendar-day ${state.selectedDate === date ? "is-selected" : ""} ${note ? "is-planned" : ""}" data-date="${date}">
        <strong>${day}</strong>
        <small>${escapeHtml(shortNote)}</small>
      </button>
    `);
  }

  return `
    <div class="calendar-weekdays">${weekdayLabels.map(label => `<span>${label}</span>`).join("")}</div>
    <div class="calendar-grid">${cells.join("")}</div>
  `;
}

function renderCalendarYear() {
  const anchor = new Date(`${state.calendarAnchor}T00:00:00`);
  const year = anchor.getFullYear();
  const monthLabels = isChineseLocale()
    ? ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
    : ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

  return `
    <div class="calendar-year-grid">
      ${monthLabels.map((label, month) => {
        const count = Object.keys(state.dayPlans).filter(date => {
          const parsed = new Date(`${date}T00:00:00`);
          return parsed.getFullYear() === year && parsed.getMonth() === month;
        }).length;
        return `
          <button type="button" class="calendar-month" data-year="${year}" data-month="${month}">
            <strong>${label}</strong>
            <small>${escapeHtml(t("calendar.groupSummary", { count: String(count) }))}</small>
          </button>
        `;
      }).join("")}
    </div>
  `;
}

function renderCalendarDay() {
  const plan = state.dayPlans[state.selectedDate] || t("state.calendarEmpty");
  return `
    <article class="calendar-day-focus">
      <strong>${escapeHtml(state.selectedDate)}</strong>
      <p>${escapeHtml(plan)}</p>
      <button type="button" class="primary" data-calendar-action="edit">${escapeHtml(t("actions.editDayPlan"))}</button>
    </article>
  `;
}

function shiftCalendar(delta) {
  const anchor = new Date(`${state.calendarAnchor}T00:00:00`);
  const selected = new Date(`${state.selectedDate}T00:00:00`);
  if (state.calendarView === "year") {
    anchor.setFullYear(anchor.getFullYear() + delta);
    selected.setFullYear(selected.getFullYear() + delta);
  } else if (state.calendarView === "day") {
    selected.setDate(selected.getDate() + delta);
    anchor.setFullYear(selected.getFullYear(), selected.getMonth(), 1);
  } else {
    anchor.setMonth(anchor.getMonth() + delta);
    selected.setFullYear(anchor.getFullYear(), anchor.getMonth(), Math.min(selected.getDate(), 28));
  }
  state.calendarAnchor = toIsoDate(anchor);
  state.selectedDate = toIsoDate(selected);
  persistWorkbenchState();
  renderCalendarView();
  renderSequence();
}

function editDayPlan() {
  openDayPlanSheet(state.selectedDate);
}

function calendarRangeLabel() {
  const anchor = new Date(`${state.calendarAnchor}T00:00:00`);
  if (state.calendarView === "year") {
    return String(anchor.getFullYear());
  }
  if (state.calendarView === "day") {
    return state.selectedDate;
  }
  const monthLabel = isChineseLocale()
    ? `${anchor.getFullYear()}年${anchor.getMonth() + 1}月`
    : `${anchor.toLocaleString("en-US", { month: "long" })} ${anchor.getFullYear()}`;
  return monthLabel;
}

function initGroupExperience() {
  if (state.groupBound) {
    return;
  }
  const addButton = document.getElementById("group-add-member");
  const applyButton = document.getElementById("group-apply-context");
  const members = document.getElementById("group-members-view");

  if (addButton) {
    addButton.addEventListener("click", () => {
      addGroupMember();
    });
  }
  if (applyButton) {
    applyButton.addEventListener("click", () => {
      applyGroupContextToPrompt();
    });
  }
  if (members) {
    members.addEventListener("click", event => {
      const target = event.target.closest("[data-group-remove]");
      if (!target) {
        return;
      }
      state.groupMembers = state.groupMembers.filter(item => item.id !== target.dataset.groupRemove);
      persistWorkbenchState();
      renderGroupMembers();
      renderSequence();
    });
  }
  state.groupBound = true;
}

function addGroupMember() {
  const nameInput = document.getElementById("group-member-name");
  const styleInput = document.getElementById("group-member-style");
  if (!nameInput || !styleInput) {
    return;
  }
  const name = nameInput.value.trim();
  if (!name) {
    nameInput.focus();
    return;
  }
  const member = {
    id: `m-${Date.now()}-${Math.floor(Math.random() * 1000)}`,
    name,
    style: styleInput.value || "culture"
  };
  state.groupMembers = [...state.groupMembers, member].slice(-MAX_GROUP_MEMBERS);
  nameInput.value = "";
  persistWorkbenchState();
  renderGroupMembers();
  renderSequence();
}

function renderGroupMembers() {
  const container = document.getElementById("group-members-view");
  const summary = document.getElementById("group-summary");
  if (!container || !summary) {
    return;
  }

  if (!state.groupMembers.length) {
    container.innerHTML = `<div class="empty-state">${escapeHtml(t("state.groupEmpty"))}</div>`;
    summary.textContent = t("state.groupEmpty");
    return;
  }

  container.innerHTML = state.groupMembers.map(member => `
    <article class="group-member">
      <strong>${escapeHtml(member.name)}</strong>
      <span>${escapeHtml(t(`group.style.${member.style}`, humanizeValue(member.style)))}</span>
      <button type="button" class="secondary" data-group-remove="${escapeHtml(member.id)}">×</button>
    </article>
  `).join("");

  const styles = state.groupMembers.map(member => t(`group.style.${member.style}`, humanizeValue(member.style))).join(isChineseLocale() ? "、" : ", ");
  summary.textContent = isChineseLocale()
    ? `已添加 ${state.groupMembers.length} 位成员：${styles}`
    : `${state.groupMembers.length} companion(s) added: ${styles}`;
  flashElement("group-summary", "is-group-updated");
}

function groupContextText() {
  if (!state.groupMembers.length) {
    return "";
  }
  const members = state.groupMembers.map(member => `${member.name}(${t(`group.style.${member.style}`, humanizeValue(member.style))})`);
  return isChineseLocale()
    ? `多人出行成员：${members.join("、")}。请提供兼顾全员偏好的分时段方案，并在冲突场景给出 A/B 备选。`
    : `Group members: ${members.join(", ")}. Provide a time-blocked plan balancing all preferences with A/B alternatives for conflicts.`;
}

function applyGroupContextToPrompt() {
  const context = groupContextText();
  if (!context) {
    return;
  }
  const input = document.getElementById("plan-input");
  if (!input) {
    return;
  }
  if (!input.value.includes(context)) {
    input.value = `${input.value.trim()}\n\n${context}`.trim();
  }
  document.getElementById("preview-summary").textContent = isChineseLocale()
    ? "已将多人出行偏好注入到本次目标。"
    : "Group travel preferences were added to the current goal.";
  flashElement("preview-summary", "is-updated");
  renderSequence();
}

function initJourneyHub() {
  if (state.hubBound) {
    return;
  }
  const hub = document.getElementById("journey-hub");
  if (!hub) {
    return;
  }
  hub.addEventListener("click", event => {
    const jumpTarget = event.target.closest("[data-jump-section]")?.dataset.jumpSection;
    if (jumpTarget) {
      const section = document.getElementById(jumpTarget);
      if (section) {
        section.scrollIntoView({ behavior: "smooth", block: "start" });
      }
      return;
    }

    if (event.target.closest("[data-jump-run]")) {
      document.getElementById("run-preview")?.click();
    }
  });
  state.hubBound = true;
}

function renderJourneyHub() {
  const hub = document.getElementById("journey-hub");
  if (!hub) {
    return;
  }
  const festival = state.festivalFeed[state.festivalIndex];
  const hasFestival = Boolean(festival);
  const plannedDays = Object.keys(state.dayPlans).length;
  const hasCalendar = plannedDays > 0;
  const hasGroup = state.groupMembers.length > 0;
  const hasPrompt = Boolean(document.getElementById("plan-input")?.value.trim());
  const activePersona = state.personas.find(persona => persona.id === state.activePersonaId);
  const readiness = Math.round(((Number(hasFestival) + Number(hasCalendar) + Number(hasPrompt) + 1) / 4) * 100);

  const nextHint = !hasFestival
    ? t("hub.next.poi")
    : !hasCalendar
      ? t("hub.next.calendar")
      : !hasPrompt
        ? t("hub.next.run")
        : (hasGroup ? t("hub.next.run") : t("hub.next.group"));

  const steps = [
    { key: "hub.step.poi", ready: hasFestival, target: "section-festival", detail: hasFestival ? `${festival.name} · ${festival.city}` : "-" },
    { key: "hub.step.calendar", ready: hasCalendar, target: "section-calendar", detail: t("calendar.groupSummary", { count: String(plannedDays) }) },
    { key: "hub.step.group", ready: hasGroup, target: "section-group", detail: hasGroup ? String(state.groupMembers.length) : "0" },
    { key: "hub.step.run", ready: hasPrompt, target: "section-control", detail: hasPrompt ? "ready" : "todo" }
  ];

  hub.innerHTML = `
    <div class="hub-head">
      <div class="hub-meta">
        <strong>${escapeHtml(t("fields.personaCompact"))}: ${escapeHtml(activePersona?.displayName || "-")}</strong>
        <small>${escapeHtml(t("hub.next"))}: ${escapeHtml(nextHint)}</small>
      </div>
      <div class="hub-ready">
        <span>${escapeHtml(t("hub.ready"))}</span>
        <strong>${readiness}%</strong>
      </div>
    </div>
    <div class="hub-track">
      ${steps.map(step => `
        <button type="button" class="hub-step ${step.ready ? "is-ready" : ""}" data-jump-section="${escapeHtml(step.target)}">
          <span>${escapeHtml(t(step.key))}</span>
          <strong>${escapeHtml(step.detail)}</strong>
        </button>
      `).join("")}
    </div>
    <div class="actions">
      <button type="button" class="secondary" data-jump-section="section-festival">${escapeHtml(t("sections.festivals.title"))}</button>
      <button type="button" class="secondary" data-jump-section="section-calendar">${escapeHtml(t("sections.calendar.title"))}</button>
      <button type="button" class="primary" data-jump-run>${escapeHtml(t("hero.runPreview"))}</button>
    </div>
  `;
}

function initDayPlanSheet() {
  const overlay = document.getElementById("day-plan-sheet");
  if (!overlay || overlay.dataset.bound === "true") {
    return;
  }
  const closeButton = document.getElementById("day-plan-close");
  const cancelButton = document.getElementById("day-plan-cancel");
  const saveButton = document.getElementById("day-plan-save");
  const clearButton = document.getElementById("day-plan-clear");
  const input = document.getElementById("day-plan-input");
  const quick = document.getElementById("day-plan-quick");

  closeButton?.addEventListener("click", closeDayPlanSheet);
  cancelButton?.addEventListener("click", closeDayPlanSheet);
  saveButton?.addEventListener("click", saveDayPlanSheet);
  clearButton?.addEventListener("click", () => {
    if (!state.editingDate) {
      return;
    }
    delete state.dayPlans[state.editingDate];
    persistWorkbenchState();
    closeDayPlanSheet();
    renderCalendarView();
    renderSequence();
  });
  if (input) {
    input.addEventListener("input", updateDayPlanInputMeta);
  }
  if (quick) {
    quick.addEventListener("click", event => {
      const template = event.target.closest("[data-day-plan-template]")?.dataset.dayPlanTemplate;
      if (!template || !input) {
        return;
      }
      input.value = input.value.trim()
        ? `${input.value.trim()}\n${template}`
        : template;
      updateDayPlanInputMeta();
      input.focus();
      input.setSelectionRange(input.value.length, input.value.length);
    });
  }

  overlay.addEventListener("click", event => {
    if (event.target === overlay) {
      closeDayPlanSheet();
    }
  });

  document.addEventListener("keydown", event => {
    if (event.key === "Escape" && !overlay.hidden) {
      closeDayPlanSheet();
    }
  });
  overlay.hidden = true;
  document.body.classList.remove("sheet-open");
  state.editingDate = null;
  renderDayPlanQuickTemplates();
  updateDayPlanInputMeta();
  overlay.dataset.bound = "true";
}

function openDayPlanSheet(date) {
  const overlay = document.getElementById("day-plan-sheet");
  const dateLabel = document.getElementById("day-plan-date");
  const input = document.getElementById("day-plan-input");
  if (!overlay || !dateLabel || !input) {
    return;
  }
  if (!date) {
    return;
  }
  state.editingDate = date;
  dateLabel.textContent = date;
  input.value = state.dayPlans[date] || "";
  renderDayPlanQuickTemplates();
  updateDayPlanInputMeta();
  overlay.hidden = false;
  document.body.classList.add("sheet-open");
  window.setTimeout(() => {
    input.focus();
    input.setSelectionRange(input.value.length, input.value.length);
  }, 20);
}

function closeDayPlanSheet() {
  const overlay = document.getElementById("day-plan-sheet");
  if (!overlay) {
    return;
  }
  overlay.hidden = true;
  document.body.classList.remove("sheet-open");
  state.editingDate = null;
}

function saveDayPlanSheet() {
  const input = document.getElementById("day-plan-input");
  if (!input || !state.editingDate) {
    return;
  }
  const value = input.value.trim();
  if (!value) {
    delete state.dayPlans[state.editingDate];
  } else {
    state.dayPlans[state.editingDate] = value;
  }
  persistWorkbenchState();
  closeDayPlanSheet();
  renderCalendarView();
  renderSequence();
}

function renderDayPlanQuickTemplates() {
  const quick = document.getElementById("day-plan-quick");
  if (!quick) {
    return;
  }
  const templates = [
    t("calendar.sheet.template.relaxed"),
    t("calendar.sheet.template.efficient"),
    t("calendar.sheet.template.family")
  ];
  quick.innerHTML = templates.map(template => `
    <button type="button" class="quick-chip" data-day-plan-template="${escapeHtml(template)}">${escapeHtml(template)}</button>
  `).join("");
}

function updateDayPlanInputMeta() {
  const input = document.getElementById("day-plan-input");
  const length = document.getElementById("day-plan-length");
  if (!input || !length) {
    return;
  }
  length.textContent = t("calendar.sheet.length", { count: String(input.value.trim().length) });
}

// Default the first-load experience to a clean persona instead of the legacy generic user / 首次加载优先进入干净 persona，避免历史测试数据污染真实体验。
function initializeDefaultPersona() {
  const userInput = document.getElementById("user-id");
  if (!userInput || state.personas.length === 0) {
    return;
  }

  const current = userInput.value.trim();
  if (current && current !== "lifeos-user") {
    syncActivePersona();
    return;
  }

  const defaultPersona = state.personas.find(persona => persona.id.startsWith("mbti-"))
    || state.personas.find(persona => persona.id !== "ops-reviewer")
    || state.personas[0];
  if (!defaultPersona) {
    return;
  }

  setPersonaContext(defaultPersona, { switchSurface: true });
  syncActivePersona();
  renderPersonas(state.personas);
}

async function saveProfile() {
  const profileParams = new URLSearchParams({
    userId: currentUserId(),
    sessionId: state.requestContext.sessionId,
    contextId: state.requestContext.contextId,
    traceId: state.requestContext.traceId,
    surface: state.surface,
    locale: state.locale
  });
  await api(`/api/v1/profile?${profileParams.toString()}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      preferences: {
        travelStyle: document.getElementById("travel-style").value.trim(),
        budgetLevel: document.getElementById("budget-level").value.trim(),
        studyGoal: document.getElementById("study-goal").value.trim()
      },
      goals: []
    })
  });
  document.getElementById("preview-summary").textContent = t("state.profileSaved");
  flashElement("preview-summary", "is-updated");
  await loadOperations();
  await loadProfile();
  await loadSecurityOverview();
  await loadAuditTrail();
}

async function addKnowledge() {
  const title = document.getElementById("knowledge-title").value.trim();
  const summary = document.getElementById("knowledge-summary").value.trim();
  const content = document.getElementById("knowledge-content").value.trim();
  const tags = document.getElementById("knowledge-tags").value
    .split(",")
    .map(item => item.trim())
    .filter(Boolean);

  await api(`/api/v1/knowledge/documents?userId=${encodeURIComponent(currentUserId())}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      title,
      sourceType: "manual",
      tags,
      summary,
      content,
      locale: state.locale
    })
  });

  document.getElementById("knowledge-title").value = "";
  document.getElementById("knowledge-summary").value = "";
  document.getElementById("knowledge-content").value = "";
  document.getElementById("preview-summary").textContent = t("state.knowledgeSaved");
  flashElement("preview-summary", "is-updated");
  await loadOperations();
  await loadKnowledge();
  await loadSecurityOverview();
  await loadAuditTrail();
}

function renderPersonas(personas, options = {}) {
  const container = document.getElementById("persona-view");
  if (!container) {
    return;
  }
  if (!personas || personas.length === 0) {
    container.innerHTML = `<div class="empty-state">${escapeHtml(t("state.personaEmpty"))}</div>`;
    return;
  }

  const searchTerm = state.personaSearch.trim().toLowerCase();
  const activeTemperament = state.personaTemperament;
  const temperaments = [...new Set(personas
    .map(persona => (persona.temperament || "").trim())
    .filter(Boolean))]
    .sort((a, b) => a.localeCompare(b, state.locale));

  const filtered = personas.filter(persona => {
    const byTemperament = activeTemperament === "all" || persona.temperament === activeTemperament;
    const bySearch = !searchTerm || [
      persona.displayName,
      persona.description,
      persona.prompt,
      persona.mbtiType,
      persona.id
    ].some(value => String(value || "").toLowerCase().includes(searchTerm));
    return byTemperament && bySearch;
  });

  const temperamentOptions = [
    `<option value="all"${activeTemperament === "all" ? " selected" : ""}>${escapeHtml(t("option.allTemperaments"))}</option>`,
    ...temperaments.map(temperament => `<option value="${escapeHtml(temperament)}"${temperament === activeTemperament ? " selected" : ""}>${escapeHtml(temperament)}</option>`)
  ].join("");

  const sorted = filtered
    .slice()
    .sort((left, right) => {
      if (left.id === state.activePersonaId) {
        return -1;
      }
      if (right.id === state.activePersonaId) {
        return 1;
      }
      return String(left.displayName).localeCompare(String(right.displayName), state.locale);
    });
  const activePersona = sorted[0] || personas.find(persona => persona.id === state.activePersonaId) || personas[0];
  const visibleLimit = state.personaExpanded ? sorted.length : 10;
  const visible = sorted.slice(0, visibleLimit);
  const chips = visible.map(persona => `
    <button type="button" class="persona-chip ${persona.id === state.activePersonaId ? "is-selected" : ""}" data-persona-id="${escapeHtml(persona.id)}">
      <strong>${escapeHtml(persona.mbtiType || "N/A")}</strong>
      <span>${escapeHtml(persona.displayName)}</span>
    </button>
  `).join("");

  const compactBody = filtered.length > 0
    ? `
      <article class="persona-active">
        <div class="persona-active-head">
          <strong>${escapeHtml(t("fields.personaCompact"))}: ${escapeHtml(activePersona.displayName)}</strong>
          <span class="meta-pill is-mbti">${escapeHtml(activePersona.mbtiType || "N/A")}</span>
        </div>
        <div class="persona-meta">
          <span class="meta-pill">${escapeHtml(t("persona.temperament"))}: ${escapeHtml(activePersona.temperament || "-")}</span>
          <span class="meta-pill">${escapeHtml(t("persona.decisionLens"))}: ${escapeHtml(activePersona.decisionLens || "-")}</span>
          <span class="meta-pill">${escapeHtml(t("label.userId"))}: ${escapeHtml(activePersona.userId)}</span>
        </div>
        <details>
          <summary>${escapeHtml(t("persona.prompt"))}</summary>
          <p>${escapeHtml(activePersona.prompt)}</p>
          <p>${escapeHtml(activePersona.description)}</p>
        </details>
      </article>
      <p class="persona-hint">${escapeHtml(t("state.personaCompactHint"))}</p>
      <div class="persona-chip-grid">${chips}</div>
      ${filtered.length > 10 ? `<div class="actions"><button type="button" class="secondary" data-persona-toggle>${escapeHtml(t(state.personaExpanded ? "actions.showLess" : "actions.showMore"))}</button></div>` : ""}
    `
    : `<div class="empty-state">${escapeHtml(t("state.personaFilteredEmpty"))}</div>`;

  container.innerHTML = `
    <div class="persona-toolbar">
      <div class="persona-filter">
        <label for="persona-search">${escapeHtml(t("fields.personaSearch"))}</label>
        <input id="persona-search" type="text" placeholder="${escapeHtml(t("fields.personaSearch"))}" value="${escapeHtml(state.personaSearch)}">
      </div>
      <div class="persona-filter">
        <label for="persona-temperament">${escapeHtml(t("fields.personaTemperament"))}</label>
        <select id="persona-temperament">${temperamentOptions}</select>
      </div>
      <p class="persona-count">${escapeHtml(t("fields.personaCount", { visible: String(filtered.length), total: String(personas.length) }))}</p>
    </div>
    <div class="persona-compact-view">${compactBody}</div>
  `;

  const searchInput = document.getElementById("persona-search");
  if (searchInput) {
    searchInput.addEventListener("input", event => {
      const cursor = event.target.selectionStart ?? String(event.target.value || "").length;
      state.personaSearch = event.target.value || "";
      renderPersonas(state.personas, { preserveSearchFocus: true, searchCursor: cursor, skipAnimation: true });
    });
  }

  const temperamentSelect = document.getElementById("persona-temperament");
  if (temperamentSelect) {
    temperamentSelect.addEventListener("change", event => {
      state.personaTemperament = event.target.value || "all";
      renderPersonas(state.personas, { skipAnimation: true });
    });
  }

  const personaToggle = container.querySelector("[data-persona-toggle]");
  if (personaToggle) {
    personaToggle.addEventListener("click", () => {
      state.personaExpanded = !state.personaExpanded;
      renderPersonas(state.personas, { skipAnimation: true });
    });
  }

  document.querySelectorAll("[data-persona-id]").forEach(button => {
    button.addEventListener("click", () => runSafely(() => applyPersona(button.dataset.personaId)));
  });
  if (options.preserveSearchFocus) {
    const restoredSearch = document.getElementById("persona-search");
    if (restoredSearch) {
      const cursor = Math.min(Number(options.searchCursor ?? restoredSearch.value.length), restoredSearch.value.length);
      restoredSearch.focus();
      restoredSearch.setSelectionRange(cursor, cursor);
    }
  }

  if (!options.skipAnimation) {
    animateChildren("persona-view");
  }
  renderJourneyHub();
}

async function applyPersona(personaId) {
  const persona = state.personas.find(item => item.id === personaId);
  if (!persona) {
    return;
  }

  setPersonaContext(persona, { switchSurface: true });

  await api(`/api/v1/profile?userId=${encodeURIComponent(persona.userId)}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      preferences: persona.preferences,
      goals: []
    })
  });

  await handleIdentityChange();
  renderPersonas(state.personas);
  document.getElementById("preview-summary").textContent = t("state.personaApplied", { name: persona.displayName });
  flashElement("preview-summary", "is-updated");
}

function setPersonaContext(persona, options = {}) {
  document.getElementById("user-id").value = persona.userId;
  document.getElementById("thread-id").value = persona.threadId;
  document.getElementById("plan-input").value = persona.prompt;
  document.getElementById("travel-style").value = persona.preferences.travelStyle || "";
  document.getElementById("budget-level").value = persona.preferences.budgetLevel || "";
  document.getElementById("study-goal").value = persona.preferences.studyGoal || "";
  state.activePersonaId = persona.id;

  if (options.switchSurface) {
    applySurfaceMode(persona.id === "ops-reviewer" ? "tob" : "toc");
  }
}

function syncActivePersona() {
  const activePersona = state.personas.find(item => item.userId === currentUserId());
  state.activePersonaId = activePersona ? activePersona.id : null;
}

function renderArchitecture(architecture) {
  if (!architecture) {
    document.getElementById("architecture-view").innerHTML = `<div class="empty-state">${escapeHtml(t("state.architectureEmpty"))}</div>`;
    return;
  }

  const items = [
    ["architecture.deploymentMode", architecture.deploymentMode],
    ["architecture.persistenceMode", architecture.persistenceMode],
    ["architecture.database", architecture.database],
    ["architecture.ragStore", architecture.ragStore],
    ["architecture.sessionStore", architecture.sessionStore],
    ["architecture.topology", architecture.topology],
    ["architecture.travelSearch", architecture.travelSearch],
    ["architecture.travelSpecialist", architecture.travelSpecialist],
    ["architecture.notes", architecture.notes]
  ];

  document.getElementById("architecture-view").innerHTML = items.map(([key, value]) => `
    <article class="chip-card">
      <strong>${escapeHtml(t(key))}</strong>
      <span>${escapeHtml(formatArchitectureValue(key, value))}</span>
    </article>
  `).join("");
  animateChildren("architecture-view");
}

function renderSecurityOverview(overview) {
  const guardrailContainer = document.getElementById("guardrail-view");
  const securityContainer = document.getElementById("security-view");
  if (!overview) {
    if (guardrailContainer) {
      guardrailContainer.innerHTML = `<div class="empty-state">${escapeHtml(t("state.guardrailEmpty"))}</div>`;
    }
    if (securityContainer) {
      securityContainer.innerHTML = `<div class="empty-state">${escapeHtml(t("state.guardrailEmpty"))}</div>`;
    }
    return;
  }

  const trust = overview.trust;
  const trustHtml = `
    <div class="list-item">
      <strong>${escapeHtml(t("label.persona"))}: ${escapeHtml(formatPersonaId(trust.personaId))}</strong>
      <small>${escapeHtml(t("label.trustTier"))}: ${escapeHtml(formatTrustTier(trust.trustTier))}</small>
      <small>${escapeHtml(t("security.summary"))}: ${escapeHtml(formatSecuritySummary(trust))}</small>
    </div>
    <div class="list-item">
      <strong>${escapeHtml(t("security.writeApproval"))}</strong>
      <small>${escapeHtml(booleanLabel(trust.writeRequiresApproval))}</small>
      <small>${escapeHtml(t("security.network"))}: ${escapeHtml(booleanLabel(trust.outboundNetworkAllowed))}</small>
    </div>
  `;

  if (guardrailContainer) {
    guardrailContainer.innerHTML = trustHtml;
    animateChildren("guardrail-view");
  }

  if (securityContainer) {
    securityContainer.innerHTML = `
      <article class="chip-card security-card">
        <h3>${escapeHtml(t("label.trustTier"))}</h3>
        <p>${escapeHtml(formatTrustTier(trust.trustTier))}</p>
        <div class="security-meta">
          <span class="meta-pill">${escapeHtml(t("security.workspaceTrusted"))}: ${escapeHtml(booleanLabel(trust.workspaceTrusted))}</span>
          <span class="meta-pill">${escapeHtml(t("security.mcpTrusted"))}: ${escapeHtml(booleanLabel(trust.mcpTrusted))}</span>
          <span class="meta-pill">${escapeHtml(t("security.network"))}: ${escapeHtml(booleanLabel(trust.outboundNetworkAllowed))}</span>
        </div>
        <p>${escapeHtml(formatSecuritySummary(trust))}</p>
      </article>
      <article class="chip-card security-card">
        <h3>${escapeHtml(t("security.policies"))}</h3>
        <ul>
          ${(overview.policies || []).map(policy => `<li>${escapeHtml(policy.capability)} · ${escapeHtml(formatAccessLevel(policy.accessLevel))} · ${escapeHtml(formatPolicyReason(policy))}</li>`).join("")}
        </ul>
        <p>${escapeHtml(t("security.allowlist"))}: ${escapeHtml((trust.outboundAllowlist || []).join(", "))}</p>
      </article>
    `;
    animateChildren("security-view");
  }
}

function renderOperations(operations) {
  const container = document.getElementById("operations-view");
  if (!container) {
    return;
  }
  if (!operations) {
    container.innerHTML = `<div class="empty-state">${escapeHtml(t("state.operationsEmpty"))}</div>`;
    return;
  }

  container.innerHTML = `
    <article class="chip-card ops-card">
      <strong>${escapeHtml(t("ops.card.target"))}</strong>
      <div class="ops-value">${escapeHtml(formatCompactNumber(operations.target.dailyActiveUsers))}</div>
      <small class="ops-meta">${escapeHtml(t("metrics.targetDau"))} · ${escapeHtml(t("ops.targetPeakQps"))}: ${escapeHtml(String(operations.target.peakQps))}</small>
      <small class="ops-meta">${escapeHtml(t("ops.availabilitySlo"))}: ${escapeHtml(formatPercentage(operations.target.availabilityPercentage))}</small>
    </article>
    <article class="chip-card ops-card">
      <strong>${escapeHtml(t("ops.card.traffic"))}</strong>
      <div class="ops-value">${escapeHtml(String(operations.currentQps))}</div>
      <small class="ops-meta">${escapeHtml(t("metrics.currentQps"))} · ${escapeHtml(t("ops.requestsPerMinute"))}: ${escapeHtml(String(operations.requestsPerMinute))}</small>
      <small class="ops-meta">${escapeHtml(t("ops.totalRequests"))}: ${escapeHtml(formatCompactNumber(operations.totalRequests))} · ${escapeHtml(t("ops.recentTraces"))}: ${escapeHtml(String(operations.recentTraceEvents || 0))}</small>
      <span class="ops-status ${operations.withinCapacity ? "is-good" : "is-warn"}">${escapeHtml(operations.withinCapacity ? t("ops.capacityOk") : t("ops.capacityRisk"))}</span>
    </article>
    <article class="chip-card ops-card">
      <strong>${escapeHtml(t("ops.card.reliability"))}</strong>
      <div class="ops-value">${escapeHtml(formatPercentage(operations.successRate))}</div>
      <small class="ops-meta">${escapeHtml(t("metrics.successRate"))} · ${escapeHtml(t("ops.previewP95"))}: ${escapeHtml(formatMillis(operations.previewP95Ms))}</small>
      <small class="ops-meta">${escapeHtml(t("ops.resumeP95"))}: ${escapeHtml(formatMillis(operations.resumeP95Ms))} · ${escapeHtml(t("ops.confirmationP95"))}: ${escapeHtml(formatMillis(operations.confirmationP95Ms))}</small>
      <span class="ops-status ${operations.withinSlo ? "is-good" : "is-warn"}">${escapeHtml(operations.withinSlo ? t("ops.sloOk") : t("ops.sloRisk"))}</span>
    </article>
    <article class="chip-card ops-card">
      <strong>${escapeHtml(t("ops.card.experience"))}</strong>
      <div class="ops-value">${escapeHtml(formatMillis(operations.assistantP95Ms))}</div>
      <small class="ops-meta">${escapeHtml(t("metrics.assistantP95"))} · ${escapeHtml(t("ops.uxBootstrapP95"))}: ${escapeHtml(formatMillis(operations.uxBootstrapP95Ms))}</small>
      <small class="ops-meta">${escapeHtml(t("ops.uxInteractionP95"))}: ${escapeHtml(formatMillis(operations.uxInteractionP95Ms))} · ${escapeHtml(t("ops.pendingConfirmations"))}: ${escapeHtml(String(operations.pendingConfirmations))}</small>
      <small class="ops-meta">${escapeHtml(t("ops.activeSessions"))}: ${escapeHtml(String(operations.activeSessions || 0))} · ${escapeHtml(t("ops.activeContexts"))}: ${escapeHtml(String(operations.activeContexts || 0))}</small>
      <small class="ops-meta">${escapeHtml(formatOperationsSummary(operations.summary, operations))}</small>
    </article>
  `;
  animateChildren("operations-view");
  setMetricText("target-dau", formatCompactNumber(operations.target.dailyActiveUsers));
  setMetricText("current-qps", String(operations.currentQps));
  setMetricText("success-rate", formatPercentage(operations.successRate));
  setMetricText("assistant-p95", formatMillis(operations.assistantP95Ms));
}

function renderTraceLinks(events) {
  const container = document.getElementById("trace-view");
  if (!container) {
    return;
  }
  if (!Array.isArray(events) || events.length === 0) {
    container.innerHTML = `<div class="empty-state">${escapeHtml(t("state.traceEmpty"))}</div>`;
    return;
  }

  const html = events.map(event => `
    <div class="list-item">
      <strong>${escapeHtml(event.operation)}</strong>
      <small>${escapeHtml(t("label.timestamp"))}: ${escapeHtml(formatDateTime(event.timestamp))}</small>
      <small>${escapeHtml(t("label.userId"))}: ${escapeHtml(event.userId || "unknown")} · ${escapeHtml(t("label.thread"))}: ${escapeHtml(event.threadId || "unknown")}</small>
      <small>${escapeHtml(t("label.session"))}: ${escapeHtml(event.sessionId || "unknown")}</small>
      <small>${escapeHtml(t("label.context"))}: ${escapeHtml(event.contextId || "unknown")} · ${escapeHtml(t("label.trace"))}: ${escapeHtml(event.traceId || "unknown")}</small>
      <small>${escapeHtml(t("label.status"))}: ${escapeHtml(event.success ? t("outcome.SUCCESS") : t("outcome.FAILURE"))} · ${escapeHtml(formatMillis(event.durationMs || 0))}</small>
    </div>
  `).join("");
  container.innerHTML = html;
  animateChildren("trace-view");
}

function renderMemoryOperations(events) {
  const container = document.getElementById("memory-ops-view");
  if (!container) {
    return;
  }
  if (!Array.isArray(events) || events.length === 0) {
    container.innerHTML = `<div class="empty-state">${escapeHtml(t("state.memoryOpsEmpty"))}</div>`;
    return;
  }

  container.innerHTML = events.map(event => `
    <div class="list-item">
      <strong>${escapeHtml(event.operation || "unknown")}</strong>
      <small>${escapeHtml(t("label.timestamp"))}: ${escapeHtml(formatDateTime(event.timestamp))}</small>
      <small>${escapeHtml(t("label.context"))}: ${escapeHtml(event.contextId || "unknown")} · ${escapeHtml(t("label.trace"))}: ${escapeHtml(event.traceId || "unknown")}</small>
      <small>${escapeHtml(t("label.status"))}: ${escapeHtml(event.success ? t("outcome.SUCCESS") : t("outcome.FAILURE"))} · ${escapeHtml(formatMillis(event.durationMs || 0))}</small>
    </div>
  `).join("");
  animateChildren("memory-ops-view");
}

function renderAssistantReply(reply) {
  const highlights = (reply.highlights || []).map(item => `<li>${escapeHtml(item)}</li>`).join("");
  document.getElementById("assistant-view").innerHTML = `
    <div class="list-item">
      <strong>${escapeHtml(t("assistant.mode"))}: ${escapeHtml(formatRuntimeMode(reply.mode))}</strong>
      <small>${escapeHtml(reply.message)}</small>
      <small>${escapeHtml(t("assistant.planId"))}: ${escapeHtml(reply.planId || "-")}</small>
      <small>${escapeHtml(t("assistant.runId"))}: ${escapeHtml(reply.runId || "-")}</small>
      ${highlights ? `<div class="highlights"><span>${escapeHtml(t("assistant.highlights"))}</span><ul>${highlights}</ul></div>` : ""}
    </div>
  `;
  animateChildren("assistant-view");
}

function renderPlan(plan) {
  const html = plan.tasks.map(task => `
    <article class="plan-card">
      <span class="status-tag">${escapeHtml(formatStatus(task.status))}</span>
      <h3>${escapeHtml(task.title)}</h3>
      <p>${escapeHtml(task.description)}</p>
      <div class="meta">${escapeHtml(t("label.owner"))}: ${escapeHtml(task.owner)}</div>
    </article>
  `).join("");
  document.getElementById("plan-view").innerHTML = html || `<div class="empty-state">${escapeHtml(t("state.planIdle"))}</div>`;
  animateChildren("plan-view");
}

function renderTimeline(events) {
  const html = events.map(event => `
    <div class="list-item">
      <strong>${escapeHtml(event.eventType)}</strong>
      <small>${escapeHtml(event.message)}</small>
    </div>
  `).join("");
  document.getElementById("timeline-view").innerHTML = html || `<div class="empty-state">${escapeHtml(t("state.timelineEmpty"))}</div>`;
  animateChildren("timeline-view");
}

function renderConfirmations(confirmations) {
  state.confirmationCount = confirmations.length;
  const visibleConfirmations = confirmations.slice(0, CONFIRMATION_LIST_LIMIT);
  const overflow = confirmations.length > CONFIRMATION_LIST_LIMIT
    ? `<div class="list-note">${escapeHtml(t("state.confirmationOverflow", { visible: CONFIRMATION_LIST_LIMIT, total: confirmations.length }))}</div>`
    : "";
  const html = visibleConfirmations.map(item => `
    <div class="list-item">
      <strong>${escapeHtml(item.action)}</strong>
      <small>${escapeHtml(t("label.status"))}: ${escapeHtml(formatStatus(item.status))}${item.comment ? ` | ${escapeHtml(item.comment)}` : ""}</small>
      <div class="actions">
        <button class="primary" data-confirm-approve="${escapeHtml(item.id)}">${escapeHtml(t("actions.approve"))}</button>
        <button class="secondary" data-confirm-reject="${escapeHtml(item.id)}">${escapeHtml(t("actions.reject"))}</button>
      </div>
    </div>
  `).join("");
  document.getElementById("confirmations-view").innerHTML = overflow + (html || `<div class="empty-state">${escapeHtml(t("state.confirmationEmpty"))}</div>`);
  animateChildren("confirmations-view");
  document.querySelectorAll("[data-confirm-approve]").forEach(button => {
    button.addEventListener("click", () => runSafely(() => decideConfirmation(button.dataset.confirmApprove, "APPROVED")));
  });
  document.querySelectorAll("[data-confirm-reject]").forEach(button => {
    button.addEventListener("click", () => runSafely(() => decideConfirmation(button.dataset.confirmReject, "REJECTED")));
  });
  renderSequence();
}

async function decideConfirmation(id, decision) {
  rotateRequestTrace("confirmation-decision");
  const updated = await api(`/api/v1/confirmations/${id}/decision?userId=${encodeURIComponent(currentUserId())}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ decision, comment: `Decision submitted from dashboard: ${decision}` })
  });
  if (decision === "APPROVED") {
    const continuation = await api("/api/v1/assistant/resume", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userId: currentUserId(),
        planId: updated.planId,
        locale: state.locale,
        sessionId: state.requestContext.sessionId,
        contextId: state.requestContext.contextId,
        traceId: state.requestContext.traceId
      })
    });

    if (continuation.plan) {
      renderPlan(continuation.plan);
      state.latestPlanId = continuation.plan.id;
    }
    if (continuation.executionRun) {
      renderTimeline(continuation.executionRun.timeline);
      state.latestRunId = continuation.executionRun.id;
    }

    document.getElementById("preview-summary").textContent = continuation.blocked
      ? t("state.resumeBlocked")
      : continuation.resumed
        ? t("state.resumeApplied")
        : t("state.resumeWaiting");
  } else {
    document.getElementById("preview-summary").textContent = t("state.resumeBlocked");
  }
  flashElement("preview-summary", "is-updated");
  await loadOperations();
  await loadTraceLinks();
  await loadConfirmations();
  await loadPlans();
  await loadSecurityOverview();
  await loadAuditTrail();
}

function renderRagStatus(rag) {
  if (!rag) {
    document.getElementById("rag-view").innerHTML = `<div class="empty-state">${escapeHtml(t("state.ragEmpty"))}</div>`;
    return;
  }

  const items = [
    ["rag.enabled", rag.enabled],
    ["rag.vectorReady", rag.vectorReady],
    ["rag.retrievalMode", rag.retrievalMode],
    ["rag.store", rag.store],
    ["rag.provider", rag.provider],
    ["rag.modelName", rag.modelName],
    ["rag.dimensions", rag.dimensions],
    ["rag.summary", rag.summary]
  ];

  document.getElementById("rag-view").innerHTML = items.map(([key, value]) => `
    <div class="list-item">
      <strong>${escapeHtml(t(key))}</strong>
      <small>${escapeHtml(formatRagValue(key, value))}</small>
    </div>
  `).join("");
  animateChildren("rag-view");
}

function renderAuditTrail(entries) {
  const container = document.getElementById("audit-view");
  if (!container) {
    return;
  }
  if (!entries || entries.length === 0) {
    container.innerHTML = `<div class="empty-state">${escapeHtml(t("state.auditEmpty"))}</div>`;
    return;
  }

  container.innerHTML = entries.map(entry => `
    <div class="list-item">
      <strong>${escapeHtml(formatAuditTitle(entry))}</strong>
      <small>${escapeHtml(t("label.outcome"))}: ${escapeHtml(formatOutcome(entry.outcome))}</small>
      <small>${escapeHtml(t("label.userId"))}: ${escapeHtml(entry.userId)} · ${escapeHtml(t("label.thread"))}: ${escapeHtml(entry.threadId)}</small>
      <small>${escapeHtml(formatAuditDetail(entry))}</small>
      <span class="audit-outcome ${outcomeClass(entry.outcome)}">${escapeHtml(formatOutcome(entry.outcome))}</span>
    </div>
  `).join("");
  animateChildren("audit-view");
}

// Render user-facing journey guidance instead of pure technical traces / 用用户视角动线替代纯技术时序。
function renderSequence() {
  const container = document.getElementById("sequence-view");
  if (!container) {
    return;
  }

  const prompt = document.getElementById("plan-input")?.value.trim();
  const festival = state.festivalFeed[state.festivalIndex];
  const plannedDays = Object.keys(state.dayPlans).length;
  const hasGroup = state.groupMembers.length > 0;
  const searchMode = connectorSummary("search", state.architecture?.travelSearch || "claw-skill-plus-flyai-plus-seeded-fallback");
  const approvalState = state.confirmationCount > 0 ? t("sequence.hitlWaiting") : t("sequence.hitlClear");

  const steps = [
    {
      title: isChineseLocale() ? "1. 明确目标" : "1. Draft your goal",
      meta: t("sequence.nextAction"),
      detail: prompt || t("sequence.actionDraft")
    },
    {
      title: isChineseLocale() ? "2. 选节日灵感" : "2. Pick holiday inspiration",
      meta: festival ? `${festival.name} · ${festival.city}` : "-",
      detail: festival
        ? (isChineseLocale()
          ? `推荐 POI：${festival.pois.join("、")}`
          : `Recommended POIs: ${festival.pois.join(", ")}`)
        : t("state.festivalEmpty"),
      live: Boolean(festival)
    },
    {
      title: isChineseLocale() ? "3. 校准日历与同行人" : "3. Calibrate calendar and companions",
      meta: hasGroup
        ? (isChineseLocale() ? `${state.groupMembers.length} 位成员` : `${state.groupMembers.length} companions`)
        : (isChineseLocale() ? "单人模式" : "Solo mode"),
      detail: `${t("sequence.actionCalendar")} · ${t("calendar.groupSummary", { count: String(plannedDays) })}`
    },
    {
      title: isChineseLocale() ? "4. 生成可执行方案" : "4. Generate executable plan",
      meta: t("sequence.actionRun"),
      detail: searchMode,
      live: true
    },
    {
      title: isChineseLocale() ? "5. 完成确认并执行" : "5. Confirm and execute",
      meta: t("sequence.actionConfirm"),
      detail: approvalState,
      live: state.confirmationCount > 0
    },
    {
      title: isChineseLocale() ? "6. 复盘与持续优化" : "6. Review and iterate",
      meta: isChineseLocale() ? "持久化与追踪" : "Persistence and tracking",
      detail: state.latestPlanId
        ? `${t("sequence.persistSummary")} #${shortId(state.latestPlanId)}`
        : t("sequence.persistSummary"),
      live: true
    }
  ];

  container.innerHTML = steps.map((step, index) => `
    <article class="sequence-node ${step.live ? "is-live" : ""} ${step.optional ? "is-optional" : ""}">
      <span class="sequence-step">${index + 1}</span>
      <h3>${escapeHtml(step.title)}</h3>
      <p>${escapeHtml(step.detail)}</p>
      <span class="sequence-meta">${escapeHtml(step.meta)}</span>
    </article>
  `).join("");

  animateChildren("sequence-view");
  renderJourneyHub();
}

async function api(url, options = {}) {
  const {
    skipAuth = false,
    ...fetchOptions
  } = options || {};
  const headers = new Headers(fetchOptions.headers || {});
  if (!skipAuth && state.auth.token && !headers.has("Authorization")) {
    headers.set("Authorization", `Bearer ${state.auth.token}`);
  }

  const response = await fetch(url, {
    ...fetchOptions,
    headers
  });
  const contentType = response.headers.get("content-type") || "";

  if (!response.ok) {
    let detail = "";
    try {
      if (contentType.includes("application/json")) {
        const payload = await response.json();
        detail = payload.message || payload.error || JSON.stringify(payload);
      } else {
        detail = (await response.text()).trim();
      }
    } catch (error) {
      detail = "";
    }
    throw new Error(`API request failed: ${response.status}${detail ? ` ${detail}` : ""}`);
  }

  if (response.status === 204) {
    return null;
  }
  if (contentType.includes("application/json")) {
    return response.json();
  }
  return response.text();
}

// Re-animate freshly rendered children so updates feel alive / 数据刷新后重新给子项做入场动画。
function animateChildren(containerId) {
  const container = document.getElementById(containerId);
  if (!container) {
    return;
  }

  Array.from(container.children).forEach((child, index) => {
    child.style.animation = "none";
    child.offsetHeight;
    child.style.animation = `cardRise 560ms cubic-bezier(0.2, 0.8, 0.2, 1) forwards`;
    child.style.animationDelay = `${index * 60}ms`;
    child.style.opacity = "0";
  });
}

function flashElement(target, className = "is-flashed") {
  const element = typeof target === "string" ? document.getElementById(target) : target;
  if (!element) {
    return;
  }

  element.classList.remove(className);
  element.offsetHeight;
  element.classList.add(className);
  window.setTimeout(() => element.classList.remove(className), 760);
}

function setMetricText(id, value) {
  const element = document.getElementById(id);
  if (!element) {
    return;
  }
  element.textContent = value;
  flashElement(element.closest(".metric"));
}

async function sendUxMetric(action, startedAt, success) {
  try {
    await fetch("/api/v1/telemetry/ux", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        action,
        surface: state.surface,
        locale: state.locale,
        userId: currentUserId(),
        threadId: currentThreadId(),
        sessionId: state.requestContext.sessionId,
        contextId: state.requestContext.contextId,
        traceId: state.requestContext.traceId,
        durationMs: Math.max(0, Math.round(performance.now() - startedAt)),
        success
      })
    });
  } catch (error) {
    console.debug("ux telemetry failed", error);
  }
}

function resolveUxAction(id) {
  return {
    "run-preview": "create_action_plan",
    "run-agent": "assistant_message",
    "h5-run-preview": "create_action_plan",
    "h5-run-agent": "assistant_message",
    "reload-data": "refresh_dashboard",
    "save-profile": "save_profile",
    "add-knowledge": "add_knowledge",
    "auth-register": "auth_register",
    "auth-login": "auth_login",
    "auth-logout": "auth_logout",
    "trace-filter-apply": "trace_filter_apply",
    "trace-filter-reset": "trace_filter_reset"
  }[id] || id;
}

function currentRequestPayload() {
  const baseInput = document.getElementById("plan-input").value.trim();
  const groupContext = groupContextText();
  const composedInput = groupContext && !baseInput.includes(groupContext)
    ? `${baseInput}\n\n${groupContext}`.trim()
    : baseInput;
  return {
    userId: currentUserId(),
    threadId: currentThreadId(),
    input: composedInput,
    locale: state.locale,
    sessionId: state.requestContext.sessionId,
    contextId: state.requestContext.contextId,
    traceId: state.requestContext.traceId
  };
}

function currentUserId() {
  if (state.auth.user?.userId) {
    return state.auth.user.userId;
  }
  return document.getElementById("user-id").value.trim() || "lifeos-user";
}

function currentThreadId() {
  return document.getElementById("thread-id").value.trim() || "thread-otterlife";
}

function isChineseLocale() {
  return state.locale.startsWith("zh");
}

function shouldUseGlobalAudit() {
  return state.activePersonaId === "ops-reviewer" || state.securityOverview?.trust?.trustTier === "operator";
}

function applyTranslations() {
  document.title = t("app.title");
  document.querySelectorAll("[data-i18n]").forEach(node => {
    node.textContent = t(node.dataset.i18n);
  });
  document.querySelectorAll("[data-i18n-placeholder]").forEach(node => {
    node.setAttribute("placeholder", t(node.dataset.i18nPlaceholder));
  });
  renderDayPlanQuickTemplates();
  updateDayPlanInputMeta();
  refreshContextStrip();
  syncTraceFilterInputs();
  renderAuthStatus();
}

function runSafely(action) {
  return Promise.resolve(action()).catch(handleError);
}

function handleError(error) {
  console.error(error);
  document.getElementById("preview-summary").textContent = `${t("state.requestFailed")} ${error.message || ""}`.trim();
}

function formatStatus(status) {
  return t(`status.${status}`, status);
}

function formatTrustTier(trustTier) {
  return t(`trust.${trustTier}`, humanizeValue(trustTier));
}

function formatAccessLevel(accessLevel) {
  return t(`access.${accessLevel}`, humanizeValue(accessLevel));
}

function formatOutcome(outcome) {
  return t(`outcome.${outcome}`, humanizeValue(outcome));
}

function booleanLabel(value) {
  return value ? t("common.enabled") : t("common.disabled");
}

function outcomeClass(outcome) {
  if (["SUCCESS", "APPROVED", "RESUMED"].includes(outcome)) {
    return "is-good";
  }
  if (["DENIED", "FAILURE", "REJECTED", "BLOCKED"].includes(outcome)) {
    return "is-danger";
  }
  return "is-warn";
}

function connectorSummary(name, fallback) {
  const connector = state.connectors.find(item => item.connectorName === name);
  return connector ? formatConnectorSummary(connector) : formatArchitectureValue("architecture.travelSearch", fallback);
}

function formatRuntimeMode(mode) {
  const mappings = {
    "orchestrator-fallback": isChineseLocale() ? "确定性编排" : "Deterministic Orchestrator",
    "agentscope-react": "AgentScope ReAct"
  };
  return mappings[mode] || humanizeValue(mode);
}

function formatRuntimeSummary(runtime) {
  if (runtime?.mode === "agentscope-react") {
    return isChineseLocale()
      ? "已启用真实模型与 AgentScope ReAct 运行时，可进行带工具的推理与会话延续。"
      : "AgentScope ReAct is active with real model access, tool use, and persisted session context.";
  }
  if (runtime?.mode === "orchestrator-fallback") {
    return isChineseLocale()
      ? "当前未配置外部模型，服务使用确定性编排器提供稳定、可审计的响应。"
      : "No external model is configured, so the service stays on the deterministic orchestrator for stable, auditable responses.";
  }
  return runtime?.summary || "";
}

function formatProvider(provider) {
  if (provider === "deterministic") {
    return isChineseLocale() ? "内置稳定编排" : "Built-in deterministic runtime";
  }
  return humanizeValue(provider);
}

function formatModelName(modelName) {
  if (!modelName || modelName === "none") {
    return isChineseLocale() ? "未配置" : "Not configured";
  }
  return modelName;
}

function formatPersonaId(personaId) {
  if (personaId && personaId.startsWith("mbti-")) {
    const mbti = personaId.replace("mbti-", "").toUpperCase();
    return isChineseLocale() ? `${mbti} 人格` : `${mbti} Persona`;
  }
  const mappings = {
    "urban-traveler": isChineseLocale() ? "都市旅行者" : "Urban Traveler",
    "habit-builder": isChineseLocale() ? "习惯坚持者" : "Habit Builder",
    "guest-explorer": isChineseLocale() ? "谨慎体验者" : "Cautious Explorer",
    "ops-reviewer": isChineseLocale() ? "运营观察者" : "Operations Reviewer",
    "general-user": isChineseLocale() ? "通用用户" : "General User"
  };
  return mappings[personaId] || humanizeValue(personaId);
}

function formatSecuritySummary(trust) {
  const summaries = {
    restricted: isChineseLocale()
      ? "当前身份仍处于受限状态，只允许查看建议，不开放实时外呼和远程专家。"
      : "This identity stays restricted to safe planning. Live search and remote specialists remain disabled.",
    guarded: isChineseLocale()
      ? "当前身份可进行本地规划，但网络与外部写入仍受策略约束。"
      : "This identity can plan locally while network access and external writes remain policy-bound.",
    trusted: isChineseLocale()
      ? "当前身份已被信任，可在审批护栏下使用实时搜索与远程专家。"
      : "This identity is trusted and can use live search plus remote specialists behind approval guardrails.",
    operator: isChineseLocale()
      ? "运营身份可查看系统控制面，但写操作仍需经过审批。"
      : "Operator identities can inspect system controls, while write actions remain approval-gated."
  };
  return summaries[trust?.trustTier] || trust?.summary || "";
}

function formatPolicyReason(policy) {
  const key = `${policy.capability}|${policy.accessLevel}`;
  const mappings = {
    "knowledge.read|allowed": isChineseLocale()
      ? "种子知识与个人文档始终可用于生成有依据的回答。"
      : "Seeded knowledge plus personal documents are always available for grounded answers.",
    "travel.search|guarded-live": isChineseLocale()
      ? "可在白名单范围内调用 Claw Skill、FlyAI 或其他实时旅行搜索。"
      : "Claw skill, FlyAI, and live travel search can run only against the outbound allowlist.",
    "travel.search|seeded-only": isChineseLocale()
      ? "当前仅使用内置知识，不会访问外部搜索。"
      : "Search stays on seeded knowledge only until the identity becomes trusted.",
    "remote.specialist|approval-required": isChineseLocale()
      ? "A2A 与 MCP 远程专家需要先通过信任检查与人工审批。"
      : "A2A and MCP specialists remain behind trust checks and manual approvals.",
    "remote.specialist|disabled": isChineseLocale()
      ? "未授信身份暂不开放远程专家调用。"
      : "Remote specialists remain disabled for untrusted identities.",
    "external.write|approval-required": isChineseLocale()
      ? "日历、提醒等外部写入必须先经过用户确认。"
      : "Calendar writes, reminders, and other external mutations require explicit approval.",
    "external.write|guarded-auto": isChineseLocale()
      ? "仅在批准的维护窗口内允许运营自动化继续执行。"
      : "Operator automation can continue only inside approved maintenance windows.",
    "system.exec|approval-required": isChineseLocale()
      ? "系统执行始终需要额外授权，且不会自动触发。"
      : "System execution always requires extra approval and never runs automatically.",
    "system.exec|denied": isChineseLocale()
      ? "系统执行默认禁用，不会自动向宿主环境发出命令。"
      : "System execution stays disabled by default and never runs against the host automatically."
  };
  return mappings[key] || policy.reason;
}

function formatConnectorSummary(connector) {
  const mappings = {
    search: connector.enabled
      ? (isChineseLocale() ? "旅行搜索聚合连接器已启用（Claw -> FlyAI -> 种子回退）。" : "Unified travel search is active (Claw -> FlyAI -> seeded fallback).")
      : (isChineseLocale() ? "旅行搜索聚合连接器当前不可用。" : "Unified travel search is currently unavailable."),
    "claw-skill-search": connector.enabled
      ? (isChineseLocale() ? "Claw Skill 搜索已接入，可优先提供实时 POI 与节日灵感。" : "Claw skill search is connected and serves as the primary live POI source.")
      : (isChineseLocale() ? "Claw Skill 搜索未启用，系统将继续尝试 FlyAI 或种子回退。" : "Claw skill search is disabled, so the service continues with FlyAI or seeded fallback."),
    "flyai-search": connector.enabled
      ? (isChineseLocale() ? "FlyAI 搜索已接入，可为旅行建议补充实时信息。" : "FlyAI search is connected and can enrich travel planning with live results.")
      : (isChineseLocale() ? "FlyAI 搜索未启用，当前回退到内置种子知识。" : "FlyAI search is disabled, so the service falls back to seeded knowledge."),
    weather: connector.enabled
      ? (isChineseLocale() ? "天气连接器已启用，可补充节奏和体感判断。" : "Weather data is available for pacing and comfort checks.")
      : connector.summary,
    calendar: connector.enabled
      ? (isChineseLocale() ? "日历连接器已启用，但写入仍需审批。" : "Calendar drafts are available, with writes still behind approvals.")
      : connector.summary,
    reminder: connector.enabled
      ? (isChineseLocale() ? "提醒连接器已启用，但创建提醒仍需确认。" : "Reminder drafts are available, with creation still gated by confirmation.")
      : connector.summary
  };
  return mappings[connector.connectorName] || connector.summary;
}

function formatPoiSource(source) {
  const mappings = {
    seeded: isChineseLocale() ? "种子知识" : "Seeded",
    crawler: isChineseLocale() ? "聚合抓取" : "Crawler",
    skill: isChineseLocale() ? "技能检索" : "Skill",
    "claw-skill": "Claw Skill",
    "flyai-skill": "FlyAI Skill"
  };
  return mappings[source] || humanizeValue(source);
}

function formatKnowledgeScope(doc) {
  const personal = doc.userId === currentUserId();
  return personal
    ? (isChineseLocale() ? "个人知识" : "Personal knowledge")
    : (isChineseLocale() ? "共享知识" : "Shared knowledge");
}

function formatArchitectureValue(key, value) {
  const raw = String(value ?? "");
  const mappings = {
    "architecture.deploymentMode": {
      "single-node": isChineseLocale() ? "单节点" : "Single node"
    },
    "architecture.persistenceMode": {
      database: isChineseLocale() ? "数据库持久化" : "Database persistence"
    },
    "architecture.database": {
      "h2-file": isChineseLocale() ? "H2 文件数据库" : "H2 file database",
      postgresql: "PostgreSQL"
    },
    "architecture.ragStore": {
      "postgres-text-now-pgvector-target": isChineseLocale() ? "数据库文本检索，随时可切 pgvector" : "Database text retrieval with a pgvector upgrade path",
      "hybrid-text-plus-pgvector-ready": isChineseLocale() ? "混合文本检索，已具备 pgvector 接入条件" : "Hybrid text retrieval, pgvector ready"
    },
    "architecture.sessionStore": {
      "json-session": isChineseLocale() ? "JSON 会话文件" : "JSON session files"
    },
    "architecture.topology": {
      "modular-monolith-ready-for-cluster": isChineseLocale() ? "模块化单体，可平滑演进到集群" : "Modular monolith with a clean path to cluster deployment"
    },
    "architecture.travelSearch": {
      "seeded-search-plus-optional-flyai": isChineseLocale() ? "内置知识检索 + 可选 FlyAI 实时搜索" : "Seeded retrieval with optional FlyAI live search",
      "claw-skill-plus-flyai-plus-seeded-fallback": isChineseLocale()
        ? "Claw Skill 优先 + FlyAI 补充 + 种子知识回退"
        : "Claw skill first, FlyAI secondary, seeded fallback"
    },
    "architecture.travelSpecialist": {
      "local-travel-agent-plus-optional-a2a": isChineseLocale() ? "本地旅行专家 + 可选 A2A 远程专家" : "Local travel specialist with optional A2A remote advisor"
    }
  };

  if (key === "architecture.notes" && raw.startsWith("Session files live at ")) {
    const path = raw.replace("Session files live at ", "");
    return isChineseLocale() ? `会话文件目录：${path}` : `Session files directory: ${path}`;
  }

  return mappings[key]?.[raw] || humanizeValue(raw);
}

function formatRagValue(key, value) {
  if (key === "rag.enabled" || key === "rag.vectorReady") {
    return booleanLabel(Boolean(value));
  }
  if (key === "rag.retrievalMode") {
    return value === "text-only"
      ? (isChineseLocale() ? "仅文本召回" : "Text-only retrieval")
      : humanizeValue(value);
  }
  if (key === "rag.store") {
    return value === "database-text"
      ? (isChineseLocale() ? "数据库文本存储" : "Database text store")
      : humanizeValue(value);
  }
  if (key === "rag.provider" || key === "rag.modelName") {
    return !value || value === "none" ? (isChineseLocale() ? "未配置" : "Not configured") : String(value);
  }
  if (key === "rag.summary" && String(value).startsWith("Vector retrieval is not configured")) {
    return isChineseLocale()
      ? "当前未配置向量检索，因此服务仅使用已持久化的文本召回。"
      : "Vector retrieval is not configured, so the service uses persisted text retrieval only.";
  }
  return String(value);
}

function formatOperationsSummary(summary, operations) {
  if (operations.withinCapacity && operations.withinSlo) {
    return isChineseLocale()
      ? "当前容量、可用性与体验指标都处于目标区间内。"
      : "Capacity, reliability, and UX metrics are all within the target envelope.";
  }
  if (!operations.withinCapacity) {
    return isChineseLocale()
      ? "当前流量正在逼近峰值预算，建议检查自动扩缩容策略。"
      : "Traffic is approaching the configured peak budget. Review autoscaling policy.";
  }
  if (operations.pendingConfirmations > 10) {
    return isChineseLocale()
      ? "审批积压正在上升，建议检查确认吞吐和升级规则。"
      : "Approval backlog is rising. Review confirmation throughput and escalation rules.";
  }
  return summary;
}

function formatModuleSummary(name, summary) {
  const mappings = {
    domain: isChineseLocale()
      ? "领域契约已就绪，覆盖计划、画像、确认和执行模型。"
      : "Domain contracts are ready for plans, profiles, confirmations, and execution records.",
    agents: isChineseLocale()
      ? "专业智能体已就绪，可处理旅行、学习与日程协同。"
      : "Specialist agents are ready for travel, learning, and schedule coordination.",
    memory: isChineseLocale()
      ? "记忆服务已就绪，可按 persona 维护长期偏好与画像。"
      : "Memory services are ready for persona-scoped preferences and long-term profiles.",
    rag: isChineseLocale()
      ? "RAG 层已就绪，可使用数据库文本召回并切换到 pgvector。"
      : "RAG is ready with database text retrieval and a path to pgvector.",
    tools: isChineseLocale()
      ? "工具层已就绪，可提供天气、搜索、日历和提醒能力。"
      : "Tooling is ready for weather, search, calendar, and reminder capabilities.",
    orchestrator: isChineseLocale()
      ? "编排器已就绪，可协调旅行、学习与日程专家且不产生副作用。"
      : "The orchestrator is ready to coordinate travel, learning, and schedule specialists without side effects.",
    infra: isChineseLocale()
      ? "基础设施已就绪，当前使用数据库持久化模式。"
      : "Infrastructure is ready and currently runs with database-backed persistence.",
    web: isChineseLocale()
      ? "前台与运营台入口已就绪，支持双语视图和埋点采集。"
      : "The web surface is ready for bilingual views, telemetry capture, and orchestration endpoints."
  };
  return mappings[name] || summary;
}

function formatAuditTitle(entry) {
  const key = `${entry.category}.${entry.action}`;
  const mappings = {
    "plan.preview": isChineseLocale() ? "计划 · 预览" : "Plan · Preview",
    "assistant.message": isChineseLocale() ? "助手 · 回复" : "Assistant · Reply",
    "assistant.resume": isChineseLocale() ? "助手 · 恢复执行" : "Assistant · Resume",
    "profile.update": isChineseLocale() ? "画像 · 更新" : "Profile · Update",
    "knowledge.write": isChineseLocale() ? "知识 · 写入" : "Knowledge · Write",
    "plan.denied": isChineseLocale() ? "计划 · 拒绝跨用户访问" : "Plan · Cross-user access denied",
    "confirmation.decision": isChineseLocale() ? "确认流 · 审批决策" : "Confirmation · Decision"
  };
  return mappings[key] || `${entry.category} · ${entry.action}`;
}

function formatAuditDetail(entry) {
  const detail = entry.detail || "";
  if (detail.startsWith("Created a user-scoped action plan")) {
    return isChineseLocale() ? "已为当前身份生成并持久化一份作用域隔离的行动方案。" : "Created and persisted a user-scoped action plan.";
  }
  if (detail === "Updated explicit long-term preferences.") {
    return isChineseLocale() ? "已更新用户显式维护的长期偏好。" : detail;
  }
  if (detail.startsWith("Persisted manual document: ")) {
    const title = detail.replace("Persisted manual document: ", "");
    return isChineseLocale() ? `已持久化一条手动知识文档：${title}` : detail;
  }
  if (detail.startsWith("Blocked cross-user")) {
    return isChineseLocale() ? "已拦截跨身份访问请求。" : detail;
  }
  return detail;
}

function formatPercentage(value) {
  return `${Number(value || 0).toFixed(2)}%`;
}

function formatMillis(value) {
  return `${Math.round(Number(value || 0))}ms`;
}

function formatDateTime(value) {
  if (!value) {
    return "-";
  }
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return String(value);
  }
  return date.toLocaleString(state.locale === "zh-CN" ? "zh-CN" : "en-US", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit"
  });
}

function formatCompactNumber(value) {
  const number = Number(value || 0);
  if (number >= 1000000) {
    return `${(number / 1000000).toFixed(1)}M`;
  }
  if (number >= 1000) {
    return `${(number / 1000).toFixed(number >= 100000 ? 0 : 1)}k`;
  }
  return String(number);
}

function toIsoDate(date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

function cryptoRandomId() {
  if (window.crypto && typeof window.crypto.randomUUID === "function") {
    return window.crypto.randomUUID();
  }
  return `${Date.now().toString(36)}${Math.random().toString(36).slice(2, 10)}`;
}

function shortId(value) {
  return String(value || "").slice(0, 8);
}

function humanizeValue(value) {
  return String(value || "")
    .replaceAll("-", " ")
    .replaceAll("_", " ");
}

function t(key, replacements = {}) {
  const bundle = TRANSLATIONS[state.locale] || TRANSLATIONS["en-US"];
  const template = bundle[key] || key;
  return Object.entries(replacements).reduce(
    (result, [placeholder, value]) => result.replaceAll(`{${placeholder}}`, value),
    template
  );
}

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll("\"", "&quot;")
    .replaceAll("'", "&#39;");
}
