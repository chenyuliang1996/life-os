const state = {
  latestRunId: null,
  latestPlanId: null,
  latestAssistantReply: null
};

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("run-preview").addEventListener("click", runPreview);
  document.getElementById("run-agent").addEventListener("click", runAssistant);
  document.getElementById("reload-data").addEventListener("click", bootstrap);
  document.getElementById("save-profile").addEventListener("click", saveProfile);
  document.getElementById("add-knowledge").addEventListener("click", addKnowledge);
  bootstrap();
});

async function bootstrap() {
  await Promise.all([
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
  const userId = document.getElementById("user-id").value.trim() || "demo-user";
  const threadId = document.getElementById("thread-id").value.trim() || "thread-life-os";
  const input = document.getElementById("plan-input").value.trim();

  const payload = { userId, threadId, input };
  const result = await api("/api/v1/plans/preview", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  state.latestRunId = result.executionRun.id;
  state.latestPlanId = result.plan.id;

  document.getElementById("preview-summary").textContent =
    `${result.plan.title} created with ${result.plan.tasks.length} tasks and ${result.confirmations.length} confirmation gates.`;

  renderPlan(result.plan);
  renderTimeline(result.executionRun.timeline);
  renderConfirmations(result.confirmations);
  await loadPlans();
}

async function runAssistant() {
  const userId = document.getElementById("user-id").value.trim() || "demo-user";
  const threadId = document.getElementById("thread-id").value.trim() || "thread-life-os";
  const input = document.getElementById("plan-input").value.trim();
  const result = await api("/api/v1/assistant/message", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ userId, threadId, input })
  });

  state.latestAssistantReply = result;
  document.getElementById("assistant-view").innerHTML = `
    <div class="list-item">
      <strong>${escapeHtml(result.mode)}</strong>
      <small>${escapeHtml(result.message)}</small>
    </div>
  `;

  if (result.runId) {
    state.latestRunId = result.runId;
    await loadTimeline(result.runId);
  }
  if (result.planId) {
    state.latestPlanId = result.planId;
    await loadPlans();
  }
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
  document.getElementById("runtime-mode").textContent = runtime.modelBacked ? "Live" : "Fallback";
  document.getElementById("runtime-view").innerHTML = `
    <div class="list-item">
      <strong>${escapeHtml(runtime.mode)}</strong>
      <small>${escapeHtml(runtime.summary)}</small>
    </div>
    <div class="list-item">
      <strong>${escapeHtml(runtime.provider)}</strong>
      <small>Model: ${escapeHtml(runtime.modelName)}</small>
    </div>
  `;
}

async function loadProfile() {
  const data = await api("/api/v1/profile");
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
    </div>
  `).join("");
  document.getElementById("knowledge-view").innerHTML = items || `<div class="empty-state">No knowledge documents yet.</div>`;
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
        <button class="secondary" data-plan-id="${escapeHtml(plan.id)}">Open</button>
      </div>
    </div>
  `).join("");
  document.getElementById("plans-view").innerHTML = html || `<div class="empty-state">No plans yet.</div>`;
  document.querySelectorAll("[data-plan-id]").forEach(button => {
    button.addEventListener("click", async () => {
      const plan = await api(`/api/v1/plans/${button.dataset.planId}`);
      renderPlan(plan);
    });
  });
}

async function loadTimeline(runId) {
  const events = await api(`/api/v1/executions/${runId}/timeline`);
  renderTimeline(events);
}

async function saveProfile() {
  await api("/api/v1/profile?userId=demo-user", {
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
  await loadProfile();
}

async function addKnowledge() {
  const title = document.getElementById("knowledge-title").value.trim();
  const summary = document.getElementById("knowledge-summary").value.trim();
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
      summary
    })
  });

  document.getElementById("knowledge-title").value = "";
  await loadKnowledge();
}

function renderPlan(plan) {
  const html = plan.tasks.map(task => `
    <article class="plan-card">
      <span class="status-tag">${escapeHtml(task.status)}</span>
      <h3>${escapeHtml(task.title)}</h3>
      <p>${escapeHtml(task.description)}</p>
      <div class="meta">Owner: ${escapeHtml(task.owner)}</div>
    </article>
  `).join("");
  document.getElementById("plan-view").innerHTML = html;
}

function renderTimeline(events) {
  const html = events.map(event => `
    <div class="list-item">
      <strong>${escapeHtml(event.eventType)}</strong>
      <small>${escapeHtml(event.message)}</small>
    </div>
  `).join("");
  document.getElementById("timeline-view").innerHTML = html || `<div class="empty-state">Execution timeline will appear here.</div>`;
}

function renderConfirmations(confirmations) {
  const html = confirmations.map(item => `
    <div class="list-item">
      <strong>${escapeHtml(item.action)}</strong>
      <small>Status: ${escapeHtml(item.status)}${item.comment ? ` | ${escapeHtml(item.comment)}` : ""}</small>
      <div class="actions">
        <button class="primary" data-confirm-approve="${escapeHtml(item.id)}">Approve</button>
        <button class="secondary" data-confirm-reject="${escapeHtml(item.id)}">Reject</button>
      </div>
    </div>
  `).join("");
  document.getElementById("confirmations-view").innerHTML = html || `<div class="empty-state">No pending confirmations.</div>`;
  document.getElementById("confirmation-count").textContent = confirmations.length;
  document.querySelectorAll("[data-confirm-approve]").forEach(button => {
    button.addEventListener("click", () => decideConfirmation(button.dataset.confirmApprove, "APPROVED"));
  });
  document.querySelectorAll("[data-confirm-reject]").forEach(button => {
    button.addEventListener("click", () => decideConfirmation(button.dataset.confirmReject, "REJECTED"));
  });
}

async function api(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) {
    throw new Error(`API request failed: ${response.status}`);
  }
  return response.json();
}

async function decideConfirmation(id, decision) {
  await api(`/api/v1/confirmations/${id}/decision`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ decision, comment: `Decision submitted from dashboard: ${decision}` })
  });
  await loadConfirmations();
}

function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll("\"", "&quot;")
    .replaceAll("'", "&#39;");
}
