const TRANSLATIONS = {
  "zh-CN": {
    "app.title": "Life OS",
    "topbar.badge": "AgentScope Java Demo",
    "hero.title": "Life OS",
    "hero.subtitle": "面向 ToC 的个人生活操作系统 Demo，将旅行、学习、日程与执行联动到同一条 Agent 工作流里。",
    "hero.runPreview": "运行演示方案",
    "hero.refresh": "刷新数据",
    "hero.runAssistant": "运行助手",
    "metrics.modules": "模块数",
    "metrics.connectors": "连接器",
    "metrics.confirmations": "待确认",
    "metrics.runtime": "运行时",
    "sections.control.title": "任务控制台",
    "sections.control.subtitle": "统一入口，触发规划、运行助手、查看结构化输出。",
    "sections.control.pill": "编排流",
    "sections.runtime.title": "运行时状态",
    "sections.runtime.subtitle": "确认当前是 deterministic fallback 还是 AgentScope ReAct 实时运行。",
    "sections.runtime.pill": "AgentScope",
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
    "sections.modules.title": "模块执行入口",
    "sections.modules.subtitle": "验证每个模块都具备独立执行能力。",
    "sections.modules.pill": "Executable",
    "sections.connectors.title": "连接器",
    "sections.connectors.subtitle": "展示工具层可用能力。",
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
    "state.previewIdle": "点击“运行演示方案”生成协同行动方案。",
    "state.assistantIdle": "运行助手后，这里会显示最新回复。",
    "state.planIdle": "计划卡片会显示在这里。",
    "state.knowledgeEmpty": "暂无知识文档。",
    "state.planEmpty": "还没有计划。",
    "state.timelineEmpty": "执行时间线会显示在这里。",
    "state.confirmationEmpty": "当前没有待确认动作。",
    "state.architectureEmpty": "架构信息加载后会显示在这里。",
    "state.ragEmpty": "RAG 状态加载后会显示在这里。",
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
    "topbar.badge": "AgentScope Java Demo",
    "hero.title": "Life OS",
    "hero.subtitle": "A consumer-facing Life OS demo that links travel, learning, schedule, and execution into one Agent workflow.",
    "hero.runPreview": "Run Demo Plan",
    "hero.refresh": "Refresh Data",
    "hero.runAssistant": "Run Assistant",
    "metrics.modules": "Modules",
    "metrics.connectors": "Connectors",
    "metrics.confirmations": "Pending",
    "metrics.runtime": "Runtime",
    "sections.control.title": "Mission Control",
    "sections.control.subtitle": "Use one entry point to preview plans, run the assistant, and inspect structured results.",
    "sections.control.pill": "Flow",
    "sections.runtime.title": "Runtime Status",
    "sections.runtime.subtitle": "Check whether the app is using deterministic fallback or live AgentScope ReAct execution.",
    "sections.runtime.pill": "AgentScope",
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
    "sections.modules.title": "Executable Modules",
    "sections.modules.subtitle": "Verifies that every module still has an executable entry point.",
    "sections.modules.pill": "Executable",
    "sections.connectors.title": "Connectors",
    "sections.connectors.subtitle": "Available tool capabilities exposed by the tool layer.",
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
    "state.previewIdle": "Press \"Run Demo Plan\" to generate a coordinated plan.",
    "state.assistantIdle": "Run the assistant to see the latest reply.",
    "state.planIdle": "Plan cards will appear here.",
    "state.knowledgeEmpty": "No knowledge documents yet.",
    "state.planEmpty": "No plans yet.",
    "state.timelineEmpty": "Execution timeline will appear here.",
    "state.confirmationEmpty": "No pending confirmations.",
    "state.architectureEmpty": "Architecture details will appear here after loading.",
    "state.ragEmpty": "RAG details will appear here after loading.",
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
  latestRunId: null,
  latestPlanId: null,
  latestAssistantReply: null,
  architecture: null,
  rag: null
};

document.addEventListener("DOMContentLoaded", () => {
  applyTranslations();
  document.getElementById("run-preview").addEventListener("click", () => runSafely(runPreview));
  document.getElementById("run-agent").addEventListener("click", () => runSafely(runAssistant));
  document.getElementById("reload-data").addEventListener("click", () => runSafely(bootstrap));
  document.getElementById("save-profile").addEventListener("click", () => runSafely(saveProfile));
  document.getElementById("add-knowledge").addEventListener("click", () => runSafely(addKnowledge));
  document.getElementById("profile-view").textContent = t("state.profileLoading");
  bootstrap().catch(handleError);
});

async function bootstrap() {
  await Promise.all([
    loadArchitecture(),
    loadRagStatus(),
    loadRuntime(),
    loadModules(),
    loadProfile(),
    loadKnowledge(),
    loadConnectors(),
    loadConfirmations(),
    loadPlans()
  ]);
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

  renderPlan(result.plan);
  renderTimeline(result.executionRun.timeline);
  renderConfirmations(result.confirmations);
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

  if (result.runId) {
    state.latestRunId = result.runId;
    await loadTimeline(result.runId);
  }
  if (result.planId) {
    state.latestPlanId = result.planId;
    await loadPlans();
  }
}

async function loadArchitecture() {
  state.architecture = await api("/api/v1/system/architecture");
  renderArchitecture(state.architecture);
}

async function loadRagStatus() {
  state.rag = await api("/api/v1/system/rag");
  renderRagStatus(state.rag);
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
  document.getElementById("module-count").textContent = Object.keys(data).length;
}

async function loadRuntime() {
  const runtime = await api("/api/v1/assistant/runtime");
  document.getElementById("runtime-mode").textContent = runtime.modelBacked ? t("runtime.live") : t("runtime.fallback");
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
}

async function loadConnectors() {
  const connectors = await api("/api/v1/system/connectors");
  const items = connectors.map(connector => `
    <div class="list-item">
      <strong>${escapeHtml(connector.connectorName)}</strong>
      <small>${escapeHtml(connector.summary)}</small>
    </div>
  `).join("");
  document.getElementById("connectors-view").innerHTML = items;
  document.getElementById("connector-count").textContent = connectors.length;
}

async function loadConfirmations() {
  const confirmations = await api("/api/v1/confirmations");
  renderConfirmations(confirmations);
}

async function loadPlans() {
  const plans = await api("/api/v1/plans");
  const html = plans.map(plan => `
    <div class="list-item">
      <strong>${escapeHtml(plan.title)}</strong>
      <small>${escapeHtml(plan.summary)}</small>
      <div class="actions">
        <button class="secondary" data-plan-id="${escapeHtml(plan.id)}">${escapeHtml(t("actions.openPlan"))}</button>
      </div>
    </div>
  `).join("");
  document.getElementById("plans-view").innerHTML = html || `<div class="empty-state">${escapeHtml(t("state.planEmpty"))}</div>`;
  document.querySelectorAll("[data-plan-id]").forEach(button => {
    button.addEventListener("click", () => runSafely(async () => {
      const plan = await api(`/api/v1/plans/${button.dataset.planId}`);
      renderPlan(plan);
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
}

function renderTimeline(events) {
  const html = events.map(event => `
    <div class="list-item">
      <strong>${escapeHtml(event.eventType)}</strong>
      <small>${escapeHtml(event.message)}</small>
    </div>
  `).join("");
  document.getElementById("timeline-view").innerHTML = html || `<div class="empty-state">${escapeHtml(t("state.timelineEmpty"))}</div>`;
}

function renderConfirmations(confirmations) {
  const html = confirmations.map(item => `
    <div class="list-item">
      <strong>${escapeHtml(item.action)}</strong>
      <small>${escapeHtml(t("label.status"))}: ${escapeHtml(formatStatus(item.status))}${item.comment ? ` | ${escapeHtml(item.comment)}` : ""}</small>
      <div class="actions">
        <button class="primary" data-confirm-approve="${escapeHtml(item.id)}">${escapeHtml(t("actions.approve"))}</button>
        <button class="secondary" data-confirm-reject="${escapeHtml(item.id)}">${escapeHtml(t("actions.reject"))}</button>
      </div>
    </div>
  `).join("");
  document.getElementById("confirmations-view").innerHTML = html || `<div class="empty-state">${escapeHtml(t("state.confirmationEmpty"))}</div>`;
  document.getElementById("confirmation-count").textContent = confirmations.length;
  document.querySelectorAll("[data-confirm-approve]").forEach(button => {
    button.addEventListener("click", () => runSafely(() => decideConfirmation(button.dataset.confirmApprove, "APPROVED")));
  });
  document.querySelectorAll("[data-confirm-reject]").forEach(button => {
    button.addEventListener("click", () => runSafely(() => decideConfirmation(button.dataset.confirmReject, "REJECTED")));
  });
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
}

async function api(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) {
    throw new Error(`API request failed: ${response.status}`);
  }
  return response.json();
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
  return document.getElementById("user-id").value.trim() || "demo-user";
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
