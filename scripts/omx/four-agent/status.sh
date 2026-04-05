#!/usr/bin/env bash
set -euo pipefail

# 四职能多 Agent 状态面板 / Read 4-lane OMX team task status in one place.

usage() {
  cat <<'EOF'
Usage:
  bash scripts/omx/four-agent/status.sh --team <team-name> [options]

Options:
  --team <name>      Existing OMX team name (required)
  --locale <zh|en>   Output language (default: zh)
  --json             Print raw JSON only
  -h, --help         Show this help
EOF
}

need_cmd() {
  local cmd="$1"
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "[four-agent/status] missing command: $cmd"
    exit 1
  fi
}

TEAM_NAME=""
LOCALE="zh"
RAW_JSON="false"

while [ "$#" -gt 0 ]; do
  case "$1" in
    --team)
      TEAM_NAME="${2:-}"
      shift 2
      ;;
    --locale)
      LOCALE="${2:-}"
      shift 2
      ;;
    --json)
      RAW_JSON="true"
      shift
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "[four-agent/status] unknown arg: $1"
      usage
      exit 1
      ;;
  esac
done

if [ -z "$TEAM_NAME" ]; then
  echo "[four-agent/status] --team is required."
  usage
  exit 1
fi

if [ "$LOCALE" != "zh" ] && [ "$LOCALE" != "en" ]; then
  echo "[four-agent/status] invalid locale: $LOCALE (use zh or en)"
  exit 1
fi

need_cmd omx
need_cmd jq

payload="$(jq -cn --arg team "$TEAM_NAME" '{team_name:$team}')"
summary_json="$(omx team api get-summary --input "$payload" --json)"
tasks_json="$(omx team api list-tasks --input "$payload" --json)"

if [ "$RAW_JSON" = "true" ]; then
  jq -n --argjson summary "$summary_json" --argjson tasks "$tasks_json" \
    '{summary:$summary, tasks:$tasks}'
  exit 0
fi

task_rows="$(jq -r '
  def arr:
    (.tasks // .items // .data // (if type == "array" then . else [] end));
  arr[]
  | [
      ((.task_id // .id // .taskId // "-") | tostring),
      (.status // .lifecycle_status // "unknown"),
      (.owner // "-"),
      (.subject // .title // "-"),
      ((.blocked_by // []) | join(","))
    ]
  | @tsv
' <<<"$tasks_json")"

total="$(jq -r '
  def arr:
    (.tasks // .items // .data // (if type == "array" then . else [] end));
  arr | length
' <<<"$tasks_json")"

in_progress="$(jq -r '
  def arr:
    (.tasks // .items // .data // (if type == "array" then . else [] end));
  arr | map(select((.status // .lifecycle_status // "") == "in_progress")) | length
' <<<"$tasks_json")"

completed="$(jq -r '
  def arr:
    (.tasks // .items // .data // (if type == "array" then . else [] end));
  arr | map(select((.status // .lifecycle_status // "") == "completed")) | length
' <<<"$tasks_json")"

blocked="$(jq -r '
  def arr:
    (.tasks // .items // .data // (if type == "array" then . else [] end));
  arr | map(select((.blocked_by // []) | length > 0)) | length
' <<<"$tasks_json")"

if [ "$LOCALE" = "zh" ]; then
  echo "== 四职能协作状态 =="
  echo "团队: $TEAM_NAME"
  echo "任务总数: $total | 进行中: $in_progress | 已完成: $completed | 存在依赖: $blocked"
  echo
  echo "ID    状态          Owner            主题"
  echo "----  ------------  ---------------  ----------------------------------------"
else
  echo "== Four-Lane Delivery Status =="
  echo "Team: $TEAM_NAME"
  echo "Total: $total | In progress: $in_progress | Completed: $completed | Blocked-by deps: $blocked"
  echo
  echo "ID    Status        Owner            Subject"
  echo "----  ------------  ---------------  ----------------------------------------"
fi

if [ -n "$task_rows" ]; then
  while IFS=$'\t' read -r id status owner subject blocked_by_refs; do
    printf "%-4s  %-12s  %-15s  %s\n" "$id" "$status" "$owner" "$subject"
    if [ -n "$blocked_by_refs" ] && [ "$blocked_by_refs" != "null" ]; then
      if [ "$LOCALE" = "zh" ]; then
        printf "      依赖: %s\n" "$blocked_by_refs"
      else
        printf "      blocked by: %s\n" "$blocked_by_refs"
      fi
    fi
  done <<<"$task_rows"
else
  if [ "$LOCALE" = "zh" ]; then
    echo "当前没有任务。"
  else
    echo "No tasks found."
  fi
fi

echo
echo "$summary_json" | jq -r '
  if type == "object" then
    "summary(raw): " + (tojson)
  else
    "summary(raw): " + tostring
  end
'

