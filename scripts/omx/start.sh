#!/usr/bin/env bash
set -euo pipefail

# 进入仓库根目录 / enter repository root
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$REPO_ROOT"

if ! command -v omx >/dev/null 2>&1; then
  echo "[omx-start] command 'omx' not found. run scripts/omx/setup-project.sh first."
  exit 1
fi

if [ ! -f "$REPO_ROOT/AGENTS.md" ]; then
  echo "[omx-start] AGENTS.md not found at repo root; OMX guidance may be incomplete."
fi

# 默认高执行强度 / default strong execution profile
if [ "$#" -eq 0 ]; then
  exec omx --madmax --high
fi

# 支持用户自定义参数 / forward custom args
exec omx "$@"

