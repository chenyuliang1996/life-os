const TRANSLATIONS = {
  "zh-CN": {
    "app.title": "Life OS",
    "topbar.badge": "Life OS Service Platform",
    "topbar.surfaceToc": "ToC 体验",
    "topbar.surfaceTob": "ToB 运营台",
    "hero.title": "Life OS",
    "hero.subtitle": "面向真实用户的生活服务平台，统一承接旅行、学习、日程与执行闭环。",
    "hero.badgeRag": "Hybrid RAG",
    "hero.badgeSearch": "FlyAI Search",
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
    "sections.sequence.title": "请求时序",
    "sections.sequence.subtitle": "展示从用户输入到 RAG、搜索、远程专家、确认流和持久化的完整执行顺序。",
    "sections.sequence.pill": "Sequence",
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
    "sections.knowledge.subtitle": "持久化文档进入混合 RAG 层，支持 PostgreSQL + pgvector，并可叠加 FlyAI 旅行搜索。",
    "sections.knowledge.pill": "RAG Seeds",
    "sections.modules.title": "模块探针",
    "sections.modules.subtitle": "查看每个模块的健康探针和执行摘要。",
    "sections.modules.pill": "Probe",
    "sections.connectors.title": "连接器",
    "sections.connectors.subtitle": "展示工具层可用能力和外部依赖状态。",
    "sections.connectors.pill": "Tools",
    "fields.prompt": "提示词",
    "fields.userId": "用户 ID",
    "fields.threadId": "线程 ID",
    "fields.travelStyle": "旅行风格",
    "fields.budgetLevel": "预算等级",
    "fields.studyGoal": "学习目标",
    "fields.knowledgeTitle": "标题",
    "fields.knowledgeTags": "标签",
    "fields.knowledgeSummary": "摘要",
    "fields.knowledgeContent": "正文",
    "actions.saveProfile": "保存画像",
    "actions.addKnowledge": "新增知识",
    "actions.openPlan": "打开",
    "actions.approve": "通过",
    "actions.reject": "拒绝",
    "state.previewIdle": "点击“生成可执行方案”开始规划。",
    "state.assistantIdle": "运行助手后，这里会显示最新回复。",
    "state.planIdle": "计划卡片会显示在这里。",
    "state.knowledgeEmpty": "暂无知识文档。",
    "state.planEmpty": "还没有计划。",
    "state.planOverflow": "仅展示最近 {visible} 条，共 {total} 条。",
    "state.timelineEmpty": "执行时间线会显示在这里。",
    "state.confirmationEmpty": "当前没有待确认动作。",
    "state.confirmationOverflow": "仅展示最近 {visible} 条待处理项，共 {total} 条。",
    "state.architectureEmpty": "架构信息加载后会显示在这里。",
    "state.ragEmpty": "RAG 状态加载后会显示在这里。",
    "state.operationsEmpty": "服务指标加载后会显示在这里。",
    "state.profileLoading": "正在加载画像...",
    "state.requestFailed": "请求失败，请查看控制台日志。",
    "state.profileSaved": "用户画像已保存。",
    "state.knowledgeSaved": "知识文档已入库。",
    "state.planGenerated": "已生成“{title}”，包含 {tasks} 个任务和 {confirmations} 个确认节点。",
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
    "ops.capacityOk": "容量充足",
    "ops.capacityRisk": "接近容量阈值",
    "ops.sloOk": "SLO 正常",
    "ops.sloRisk": "SLO 需关注",
    "label.owner": "负责人",
    "label.status": "状态",
    "label.comment": "备注",
    "label.locale": "语言",
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
    "app.title": "Life OS",
    "topbar.badge": "Life OS Service Platform",
    "topbar.surfaceToc": "ToC Experience",
    "topbar.surfaceTob": "ToB Operations",
    "hero.title": "Life OS",
    "hero.subtitle": "A real consumer service that connects travel, learning, schedule, and execution in one operating flow.",
    "hero.badgeRag": "Hybrid RAG",
    "hero.badgeSearch": "FlyAI Search",
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
    "sections.sequence.title": "Request Sequence",
    "sections.sequence.subtitle": "Shows the full execution order from user intent through RAG, search, remote specialist, approvals, and persistence.",
    "sections.sequence.pill": "Sequence",
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
    "sections.knowledge.subtitle": "Persist documents into the hybrid RAG layer with PostgreSQL + pgvector and optional FlyAI travel search.",
    "sections.knowledge.pill": "RAG Seeds",
    "sections.modules.title": "Module Probes",
    "sections.modules.subtitle": "Inspect health probes and execution summaries for every module.",
    "sections.modules.pill": "Probe",
    "sections.connectors.title": "Connectors",
    "sections.connectors.subtitle": "Available tool capabilities and external dependency status exposed by the tool layer.",
    "sections.connectors.pill": "Tools",
    "fields.prompt": "Prompt",
    "fields.userId": "User ID",
    "fields.threadId": "Thread ID",
    "fields.travelStyle": "Travel style",
    "fields.budgetLevel": "Budget level",
    "fields.studyGoal": "Study goal",
    "fields.knowledgeTitle": "Title",
    "fields.knowledgeTags": "Tags",
    "fields.knowledgeSummary": "Summary",
    "fields.knowledgeContent": "Content",
    "actions.saveProfile": "Save Profile",
    "actions.addKnowledge": "Add Knowledge",
    "actions.openPlan": "Open",
    "actions.approve": "Approve",
    "actions.reject": "Reject",
    "state.previewIdle": "Press \"Create Action Plan\" to start planning.",
    "state.assistantIdle": "Run the assistant to see the latest reply.",
    "state.planIdle": "Plan cards will appear here.",
    "state.knowledgeEmpty": "No knowledge documents yet.",
    "state.planEmpty": "No plans yet.",
    "state.planOverflow": "Showing the latest {visible} plans out of {total}.",
    "state.timelineEmpty": "Execution timeline will appear here.",
    "state.confirmationEmpty": "No pending confirmations.",
    "state.confirmationOverflow": "Showing the latest {visible} pending approvals out of {total}.",
    "state.architectureEmpty": "Architecture details will appear here after loading.",
    "state.ragEmpty": "RAG details will appear here after loading.",
    "state.operationsEmpty": "Service metrics will appear here after loading.",
    "state.profileLoading": "Loading profile...",
    "state.requestFailed": "Request failed. Please inspect the console logs.",
    "state.profileSaved": "Profile saved.",
    "state.knowledgeSaved": "Knowledge document persisted.",
    "state.planGenerated": "Generated \"{title}\" with {tasks} tasks and {confirmations} confirmation gates.",
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
    "ops.card.target": "Service Targets",
    "ops.card.traffic": "Traffic and Capacity",
    "ops.card.reliability": "Reliability",
    "ops.card.experience": "User Experience",
    "ops.targetPeakQps": "Peak QPS",
    "ops.availabilitySlo": "Availability SLO",
    "ops.requestsPerMinute": "Requests per minute",
    "ops.totalRequests": "Total requests",
    "ops.pendingConfirmations": "Pending approvals",
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
  latestRunId: null,
  latestPlanId: null,
  latestAssistantReply: null,
  architecture: null,
  rag: null,
  runtime: null,
  operations: null,
  connectors: [],
  confirmationCount: 0
};

const PLAN_HISTORY_LIMIT = 8;
const CONFIRMATION_LIST_LIMIT = 6;

document.addEventListener("DOMContentLoaded", () => {
  const bootStartedAt = performance.now();
  applyTranslations();
  prepareMotion();
  restoreSurfaceMode();
  bindSurfaceSwitch();
  bindActionButton("run-preview", runPreview);
  bindActionButton("run-agent", runAssistant);
  bindActionButton("reload-data", bootstrap);
  bindActionButton("save-profile", saveProfile);
  bindActionButton("add-knowledge", addKnowledge);
  document.getElementById("profile-view").textContent = t("state.profileLoading");
  bootstrap()
    .then(async () => {
      markReady();
      await sendUxMetric("page_bootstrap", bootStartedAt, true);
    })
    .catch(async error => {
      await sendUxMetric("page_bootstrap", bootStartedAt, false);
      handleError(error);
    });
});

async function bootstrap() {
  await Promise.all([
    loadArchitecture(),
    loadRagStatus(),
    loadRuntime(),
    loadOperations(),
    loadModules(),
    loadProfile(),
    loadKnowledge(),
    loadConnectors(),
    loadConfirmations(),
    loadPlans()
  ]);
  renderSequence();
}

// Keep motion setup centralized so new cards inherit the same stagger / 集中管理入场动效，后续新增卡片会自动继承节奏。
function prepareMotion() {
  document.querySelectorAll(".hero-copy, .hero-panel, .card").forEach((panel, index) => {
    panel.style.setProperty("--stagger", `${index * 70}ms`);
  });
}

function restoreSurfaceMode() {
  const saved = window.localStorage.getItem("life-os-surface");
  if (saved === "toc" || saved === "tob") {
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

// Toggle consumer/operator views without reloading the page / 不刷新页面切换 ToC 与 ToB 视角。
function applySurfaceMode(surface) {
  state.surface = surface === "tob" ? "tob" : "toc";
  document.body.dataset.surface = state.surface;
  window.localStorage.setItem("life-os-surface", state.surface);
  document.querySelectorAll("[data-surface-target]").forEach(button => {
    button.classList.toggle("is-active", button.dataset.surfaceTarget === state.surface);
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
  button.disabled = true;
  button.classList.add("is-loading");
  try {
    await action();
    await sendUxMetric(resolveUxAction(button.id), startedAt, true);
  } catch (error) {
    await sendUxMetric(resolveUxAction(button.id), startedAt, false);
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
  await loadPlans();
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
    await loadTimeline(result.runId);
  }
  if (result.planId) {
    state.latestPlanId = result.planId;
    await loadPlans();
  }
  await loadOperations();
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
      <small>${escapeHtml(summary)}</small>
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
      <strong>${escapeHtml(runtime.mode)}</strong>
      <small>${escapeHtml(runtime.summary)}</small>
    </div>
    <div class="list-item">
      <strong>${escapeHtml(t("runtime.provider"))}: ${escapeHtml(runtime.provider)}</strong>
      <small>${escapeHtml(t("runtime.model"))}: ${escapeHtml(runtime.modelName)}</small>
    </div>
  `;
  animateChildren("runtime-view");
}

async function loadProfile() {
  const userId = currentUserId();
  const data = await api(`/api/v1/profile?userId=${encodeURIComponent(userId)}`);
  document.getElementById("travel-style").value = data.preferences.travelStyle || "";
  document.getElementById("budget-level").value = data.preferences.budgetLevel || "";
  document.getElementById("study-goal").value = data.preferences.studyGoal || "";
  document.getElementById("profile-view").textContent = JSON.stringify(data, null, 2);
}

async function loadKnowledge() {
  const docs = await api("/api/v1/knowledge/documents");
  const items = docs.map(doc => `
    <div class="list-item">
      <strong>${escapeHtml(doc.title)}</strong>
      <small>${escapeHtml(doc.summary)}</small>
      <small>${escapeHtml(t("label.locale"))}: ${escapeHtml(doc.locale || "en-US")}</small>
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
      <small>${escapeHtml(connector.summary)}</small>
    </div>
  `).join("");
  document.getElementById("connectors-view").innerHTML = items;
  animateChildren("connectors-view");
  renderSequence();
}

async function loadConfirmations() {
  const confirmations = await api("/api/v1/confirmations");
  renderConfirmations(confirmations);
}

async function loadPlans() {
  const plans = await api("/api/v1/plans");
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
      const plan = await api(`/api/v1/plans/${button.dataset.planId}`);
      renderPlan(plan);
      flashElement("plan-view");
    }));
  });
}

async function loadTimeline(runId) {
  const events = await api(`/api/v1/executions/${runId}/timeline`);
  renderTimeline(events);
}

async function saveProfile() {
  await api(`/api/v1/profile?userId=${encodeURIComponent(currentUserId())}`, {
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
}

async function addKnowledge() {
  const title = document.getElementById("knowledge-title").value.trim();
  const summary = document.getElementById("knowledge-summary").value.trim();
  const content = document.getElementById("knowledge-content").value.trim();
  const tags = document.getElementById("knowledge-tags").value
    .split(",")
    .map(item => item.trim())
    .filter(Boolean);

  await api("/api/v1/knowledge/documents", {
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
      <span>${escapeHtml(humanizeValue(value))}</span>
    </article>
  `).join("");
  animateChildren("architecture-view");
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
      <small class="ops-meta">${escapeHtml(t("ops.totalRequests"))}: ${escapeHtml(formatCompactNumber(operations.totalRequests))}</small>
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
      <small class="ops-meta">${escapeHtml(operations.summary)}</small>
    </article>
  `;
  animateChildren("operations-view");
  setMetricText("target-dau", formatCompactNumber(operations.target.dailyActiveUsers));
  setMetricText("current-qps", String(operations.currentQps));
  setMetricText("success-rate", formatPercentage(operations.successRate));
  setMetricText("assistant-p95", formatMillis(operations.assistantP95Ms));
}

function renderAssistantReply(reply) {
  const highlights = (reply.highlights || []).map(item => `<li>${escapeHtml(item)}</li>`).join("");
  document.getElementById("assistant-view").innerHTML = `
    <div class="list-item">
      <strong>${escapeHtml(t("assistant.mode"))}: ${escapeHtml(reply.mode)}</strong>
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
  const updated = await api(`/api/v1/confirmations/${id}/decision`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ decision, comment: `Decision submitted from dashboard: ${decision}` })
  });
  if (decision === "APPROVED") {
    const continuation = await api("/api/v1/assistant/resume", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        planId: updated.planId,
        locale: state.locale
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
  await loadConfirmations();
  await loadPlans();
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
      <small>${escapeHtml(String(value))}</small>
    </div>
  `).join("");
  animateChildren("rag-view");
}

// Render a lightweight sequence diagram in the page / 在页面里渲染轻量时序图，帮助解释请求如何流动。
function renderSequence() {
  const container = document.getElementById("sequence-view");
  if (!container) {
    return;
  }

  const prompt = document.getElementById("plan-input")?.value.trim() || t("sequence.emptyPrompt");
  const searchMode = state.architecture?.travelSearch || "seeded-search-plus-optional-flyai";
  const specialistMode = state.architecture?.travelSpecialist || "local-travel-agent-plus-optional-a2a";

  const steps = [
    {
      title: t("sequence.user.title"),
      meta: t("sequence.user.meta"),
      detail: prompt
    },
    {
      title: t("sequence.orchestrator.title"),
      meta: t("sequence.orchestrator.meta"),
      detail: humanizeValue(state.runtime?.mode || "orchestrator-fallback"),
      live: true
    },
    {
      title: t("sequence.rag.title"),
      meta: t("sequence.rag.meta"),
      detail: state.rag ? humanizeValue(state.rag.retrievalMode) : "loading",
      live: Boolean(state.rag),
      optional: !state.rag?.vectorReady
    },
    {
      title: t("sequence.search.title"),
      meta: t("sequence.search.meta"),
      detail: connectorSummary("flyai-search", searchMode),
      optional: true
    },
    {
      title: t("sequence.a2a.title"),
      meta: t("sequence.a2a.meta"),
      detail: humanizeValue(specialistMode),
      optional: true
    },
    {
      title: t("sequence.hitl.title"),
      meta: t("sequence.hitl.meta"),
      detail: state.confirmationCount > 0 ? t("sequence.hitlWaiting") : t("sequence.hitlClear"),
      live: state.confirmationCount > 0
    },
    {
      title: t("sequence.persist.title"),
      meta: t("sequence.persist.meta"),
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
}

async function api(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) {
    throw new Error(`API request failed: ${response.status}`);
  }
  return response.json();
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
    "reload-data": "refresh_dashboard",
    "save-profile": "save_profile",
    "add-knowledge": "add_knowledge"
  }[id] || id;
}

function currentRequestPayload() {
  return {
    userId: currentUserId(),
    threadId: document.getElementById("thread-id").value.trim() || "thread-life-os",
    input: document.getElementById("plan-input").value.trim(),
    locale: state.locale
  };
}

function currentUserId() {
  return document.getElementById("user-id").value.trim() || "lifeos-user";
}

function applyTranslations() {
  document.title = t("app.title");
  document.querySelectorAll("[data-i18n]").forEach(node => {
    node.textContent = t(node.dataset.i18n);
  });
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

function connectorSummary(name, fallback) {
  const connector = state.connectors.find(item => item.connectorName === name);
  return connector ? connector.summary : humanizeValue(fallback);
}

function formatPercentage(value) {
  return `${Number(value || 0).toFixed(2)}%`;
}

function formatMillis(value) {
  return `${Math.round(Number(value || 0))}ms`;
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
