const state = {
  latestRunId: null,
  latestPlanId: null
};

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("run-preview").addEventListener("click", runPreview);
  document.getElementById("reload-data").addEventListener("click", bootstrap);
  bootstrap();
});

async function bootstrap() {
  await Promise.all([
    loadModules(),
    loadProfile(),
    loadKnowledge(),
    loadConnectors(),
    loadConfirmations()
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

async function loadProfile() {
  const data = await api("/api/v1/profile");
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
    </div>
  `).join("");
  document.getElementById("confirmations-view").innerHTML = html || `<div class="empty-state">No pending confirmations.</div>`;
  document.getElementById("confirmation-count").textContent = confirmations.length;
}

async function api(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) {
    throw new Error(`API request failed: ${response.status}`);
  }
  return response.json();
}

function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll("\"", "&quot;")
    .replaceAll("'", "&#39;");
}
