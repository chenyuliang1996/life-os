#!/usr/bin/env bash
set -euo pipefail

# 四职能多 Agent 启动器 / Bootstrap 4-lane multi-agent tasks for an existing OMX team.

usage() {
  cat <<'EOF'
Usage:
  bash scripts/omx/four-agent/bootstrap.sh --team <team-name> --goal "<delivery goal>" [options]

Options:
  --team <name>        Existing OMX team name (required)
  --goal <text>        Iteration goal (required)
  --locale <zh|en>     Brief language (default: zh)
  --no-inbox           Skip worker inbox dispatch
  --dry-run            Print payload only, do not call OMX team API
  -h, --help           Show this help

Example:
  bash scripts/omx/four-agent/bootstrap.sh \
    --team lifeos-delivery-20260405 \
    --goal "优化 ToC 主动线并打通 FlyAI POI 搜索"
EOF
}

need_cmd() {
  local cmd="$1"
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "[four-agent/bootstrap] missing command: $cmd"
    exit 1
  fi
}

TEAM_NAME=""
GOAL=""
LOCALE="zh"
SEND_INBOX="true"
DRY_RUN="false"

while [ "$#" -gt 0 ]; do
  case "$1" in
    --team)
      TEAM_NAME="${2:-}"
      shift 2
      ;;
    --goal)
      GOAL="${2:-}"
      shift 2
      ;;
    --locale)
      LOCALE="${2:-}"
      shift 2
      ;;
    --no-inbox)
      SEND_INBOX="false"
      shift
      ;;
    --dry-run)
      DRY_RUN="true"
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "[four-agent/bootstrap] unknown arg: $1"
      usage
      exit 1
      ;;
  esac
done

if [ -z "$TEAM_NAME" ] || [ -z "$GOAL" ]; then
  echo "[four-agent/bootstrap] --team and --goal are required."
  usage
  exit 1
fi

if [ "$LOCALE" != "zh" ] && [ "$LOCALE" != "en" ]; then
  echo "[four-agent/bootstrap] invalid locale: $LOCALE (use zh or en)"
  exit 1
fi

need_cmd omx
need_cmd jq

list_task_ids() {
  local payload
  payload="$(jq -cn --arg team "$TEAM_NAME" '{team_name:$team}')"
  omx team api list-tasks --input "$payload" --json \
    | jq -r '
      def arr:
        (.tasks // .items // .data // (if type == "array" then . else [] end));
      arr[]
      | (.task_id // .id // .taskId // empty)
      | tostring
    ' \
    | sed '/^$/d'
}

verify_team_exists() {
  local payload
  payload="$(jq -cn --arg team "$TEAM_NAME" '{team_name:$team}')"
  omx team api read-manifest --input "$payload" --json >/dev/null
}

create_task() {
  local subject="$1"
  local description="$2"
  local owner="$3"
  local blocked_by_json="$4"
  local requires_code_change="$5"

  local payload
  payload="$(jq -cn \
    --arg team "$TEAM_NAME" \
    --arg subject "$subject" \
    --arg description "$description" \
    --arg owner "$owner" \
    --argjson blocked_by "$blocked_by_json" \
    --argjson requires_code_change "$requires_code_change" \
    '{
      team_name: $team,
      subject: $subject,
      description: $description,
      owner: $owner,
      blocked_by: $blocked_by,
      requires_code_change: $requires_code_change
    }')"

  if [ "$DRY_RUN" = "true" ]; then
    echo "[dry-run] create-task payload => $payload" >&2
    case "$owner" in
      product-agent)
        echo "TASK_PRODUCT"
        ;;
      designer-agent)
        echo "TASK_DESIGN"
        ;;
      frontend-agent)
        echo "TASK_FRONTEND"
        ;;
      backend-agent)
        echo "TASK_BACKEND"
        ;;
      *)
        echo "TASK_GENERIC"
        ;;
    esac
    return 0
  fi

  local before_ids after_ids new_id
  before_ids="$(list_task_ids || true)"
  omx team api create-task --input "$payload" --json >/dev/null
  after_ids="$(list_task_ids || true)"

  new_id="$(comm -13 <(printf "%s\n" "$before_ids" | sort -u) <(printf "%s\n" "$after_ids" | sort -u) | head -n 1 || true)"
  if [ -z "$new_id" ]; then
    echo "[four-agent/bootstrap] failed to resolve new task id for: $subject"
    exit 1
  fi

  echo "$new_id"
}

write_worker_inbox() {
  local worker="$1"
  local content="$2"
  local payload
  payload="$(jq -cn --arg team "$TEAM_NAME" --arg worker "$worker" --arg content "$content" \
    '{team_name:$team, worker:$worker, content:$content}')"

  if [ "$DRY_RUN" = "true" ]; then
    echo "[dry-run] write-worker-inbox payload => $payload"
    return 0
  fi

  omx team api write-worker-inbox --input "$payload" --json >/dev/null || true
}

if [ "$DRY_RUN" = "false" ]; then
  verify_team_exists
fi

if [ "$LOCALE" = "zh" ]; then
  PRODUCT_SUBJECT="产品 Agent：迭代目标澄清与验收定义"
  DESIGN_SUBJECT="设计 Agent：信息架构、交互与视觉规范"
  FRONTEND_SUBJECT="前端 Agent：H5 页面与交互实现"
  BACKEND_SUBJECT="后端 Agent：编排链路、工具接入与可观测性实现"

  PRODUCT_DESCRIPTION=$(cat <<EOF
业务目标：
$GOAL

职责：
1. 输出本轮 PRD（范围、非目标、优先级、风险）。
2. 明确 ToC 主动线与 ToB 观测面的验收标准。
3. 给出核心指标（转化、时延、完成率、确认率）。

交付物：
- 需求说明与范围边界
- 验收标准（功能 + 数据 + 体验）
- 风险清单与降级策略
EOF
)

  DESIGN_DESCRIPTION=$(cat <<EOF
业务目标：
$GOAL

职责：
1. 输出信息架构与页面模块关系。
2. 定义关键交互（首屏、搜索、计划编辑、确认流）。
3. 给出双语文案规范和动效节奏。

交付物：
- 低保真/高保真结构说明
- 关键状态流（空态、加载、错误、成功）
- 双语文案与组件行为规范
EOF
)

  FRONTEND_DESCRIPTION=$(cat <<EOF
业务目标：
$GOAL

职责：
1. 按产品与设计规范实现 ToC / ToB 前端行为。
2. 完成埋点与上下文追踪字段透传（sessionId/contextId/traceId）。
3. 保证移动端 H5 体验与中英文一致性。

交付物：
- 可运行页面与交互逻辑
- 埋点事件接入与验证
- 体验回归记录（核心路径）
EOF
)

  BACKEND_DESCRIPTION=$(cat <<EOF
业务目标：
$GOAL

职责：
1. 完成 API/编排/工具/记忆/RAG 相关后端改造。
2. 提供可审计的链路数据和运行指标。
3. 保证回退策略（模型不可用、外部工具不可用）可用。

交付物：
- 稳定 API 与编排逻辑
- 可观测性与审计数据
- 回退与容错验证记录
EOF
)
else
  PRODUCT_SUBJECT="Product Agent: scope and acceptance definition"
  DESIGN_SUBJECT="Design Agent: IA, interaction, and visual direction"
  FRONTEND_SUBJECT="Frontend Agent: H5 implementation and interaction delivery"
  BACKEND_SUBJECT="Backend Agent: orchestration, integrations, and observability"

  PRODUCT_DESCRIPTION=$(cat <<EOF
Delivery goal:
$GOAL

Responsibilities:
1. Produce PRD (scope, non-goals, priorities, risks).
2. Define acceptance for both ToC journey and ToB operations view.
3. Define core metrics (conversion, latency, completion, approval rates).

Deliverables:
- Requirement scope and boundaries
- Acceptance criteria (functionality + data + UX)
- Risk list and fallback strategy
EOF
)

  DESIGN_DESCRIPTION=$(cat <<EOF
Delivery goal:
$GOAL

Responsibilities:
1. Define information architecture and module relations.
2. Specify critical interactions (first-run, search, day-plan edit, approvals).
3. Provide bilingual copy guidance and motion rhythm.

Deliverables:
- IA and interaction specs
- Key state handling (empty, loading, error, success)
- Bilingual copy and component behavior rules
EOF
)

  FRONTEND_DESCRIPTION=$(cat <<EOF
Delivery goal:
$GOAL

Responsibilities:
1. Implement ToC/ToB frontend behavior from product/design specs.
2. Wire telemetry and context propagation (sessionId/contextId/traceId).
3. Keep mobile H5 experience and zh/en behavior consistent.

Deliverables:
- Working UI and interactions
- Telemetry wiring and verification
- UX regression notes for key paths
EOF
)

  BACKEND_DESCRIPTION=$(cat <<EOF
Delivery goal:
$GOAL

Responsibilities:
1. Implement API/orchestrator/tool/memory/RAG backend updates.
2. Emit traceable runtime and audit metrics.
3. Ensure fallback paths when model/tools are unavailable.

Deliverables:
- Stable API and orchestration behavior
- Observability and audit outputs
- Fallback and resilience verification notes
EOF
)
fi

PRODUCT_ID="$(create_task "$PRODUCT_SUBJECT" "$PRODUCT_DESCRIPTION" "product-agent" "[]" "false")"
DESIGN_ID="$(create_task "$DESIGN_SUBJECT" "$DESIGN_DESCRIPTION" "designer-agent" "[\"$PRODUCT_ID\"]" "false")"
FRONTEND_ID="$(create_task "$FRONTEND_SUBJECT" "$FRONTEND_DESCRIPTION" "frontend-agent" "[\"$PRODUCT_ID\",\"$DESIGN_ID\"]" "true")"
BACKEND_ID="$(create_task "$BACKEND_SUBJECT" "$BACKEND_DESCRIPTION" "backend-agent" "[\"$PRODUCT_ID\"]" "true")"

if [ "$SEND_INBOX" = "true" ]; then
  if [ "$LOCALE" = "zh" ]; then
    write_worker_inbox "worker-1" "# 角色: Product Agent\n主任务: $PRODUCT_ID\n先完成范围、验收和指标定义，再同步给全组。"
    write_worker_inbox "worker-2" "# 角色: Designer Agent\n主任务: $DESIGN_ID\n完成 IA、关键交互和双语文案规范。"
    write_worker_inbox "worker-3" "# 角色: Frontend Agent\n主任务: $FRONTEND_ID\n按产品+设计规范实现 ToC/ToB，保证移动端体验。"
    write_worker_inbox "worker-4" "# 角色: Backend Agent\n主任务: $BACKEND_ID\n完成编排、工具与可观测性改造，保证回退能力。"
  else
    write_worker_inbox "worker-1" "# Role: Product Agent\nPrimary task: $PRODUCT_ID\nFinalize scope, acceptance, and metrics first."
    write_worker_inbox "worker-2" "# Role: Designer Agent\nPrimary task: $DESIGN_ID\nDeliver IA, key interactions, and bilingual copy rules."
    write_worker_inbox "worker-3" "# Role: Frontend Agent\nPrimary task: $FRONTEND_ID\nImplement ToC/ToB UX with mobile H5 quality."
    write_worker_inbox "worker-4" "# Role: Backend Agent\nPrimary task: $BACKEND_ID\nDeliver orchestration, integrations, observability, and fallback."
  fi
fi

cat <<EOF
[four-agent/bootstrap] done.
team: $TEAM_NAME
goal: $GOAL
tasks:
  product: $PRODUCT_ID
  design: $DESIGN_ID
  frontend: $FRONTEND_ID
  backend: $BACKEND_ID

next:
  omx team status $TEAM_NAME
  omx team api list-tasks --input '{"team_name":"$TEAM_NAME"}' --json
EOF
