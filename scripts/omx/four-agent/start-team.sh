#!/usr/bin/env bash
set -euo pipefail

# 启动四职能 Team Runtime / Start OMX team runtime with 4 execution workers.

usage() {
  cat <<'EOF'
Usage:
  bash scripts/omx/four-agent/start-team.sh --goal "<delivery goal>" [options]

Options:
  --goal <text>         Delivery goal (required)
  --workers <n>         Worker count (default: 4)
  --worker-role <role>  OMX worker role type (default: executor)
  -h, --help            Show this help

Example:
  bash scripts/omx/four-agent/start-team.sh \
    --goal "完成本周 ToC 主动线改造与后端确认流优化"
EOF
}

need_cmd() {
  local cmd="$1"
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "[four-agent/start-team] missing command: $cmd"
    exit 1
  fi
}

GOAL=""
WORKERS="4"
WORKER_ROLE="executor"

while [ "$#" -gt 0 ]; do
  case "$1" in
    --goal)
      GOAL="${2:-}"
      shift 2
      ;;
    --workers)
      WORKERS="${2:-}"
      shift 2
      ;;
    --worker-role)
      WORKER_ROLE="${2:-}"
      shift 2
      ;;
    -h|--help)
      usage
      exit 0
      ;;
    *)
      echo "[four-agent/start-team] unknown arg: $1"
      usage
      exit 1
      ;;
  esac
done

if [ -z "$GOAL" ]; then
  echo "[four-agent/start-team] --goal is required."
  usage
  exit 1
fi

if ! [[ "$WORKERS" =~ ^[0-9]+$ ]] || [ "$WORKERS" -le 0 ]; then
  echo "[four-agent/start-team] --workers must be a positive integer."
  exit 1
fi

need_cmd omx

if ! command -v tmux >/dev/null 2>&1; then
  echo "[four-agent/start-team] tmux is required by omx team runtime."
  echo "Install on macOS: brew install tmux"
  exit 1
fi

TEAM_SPEC="${WORKERS}:${WORKER_ROLE}"
echo "[four-agent/start-team] launching: omx team $TEAM_SPEC \"$GOAL\""
exec omx team "$TEAM_SPEC" "$GOAL"

