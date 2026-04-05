#!/usr/bin/env bash
set -euo pipefail

# 项目根目录 / Resolve repository root
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"

need_cmd() {
  local cmd="$1"
  if ! command -v "$cmd" >/dev/null 2>&1; then
    echo "[omx-setup] missing command: $cmd"
    exit 1
  fi
}

need_cmd node
need_cmd npm

# Node 版本校验 / Node version guard
NODE_MAJOR="$(node -p 'process.versions.node.split(".")[0]')"
if [ "$NODE_MAJOR" -lt 20 ]; then
  echo "[omx-setup] Node.js >= 20 is required, current: $(node -v)"
  exit 1
fi

ensure_global_package() {
  local pkg="$1"
  local bin="$2"
  if command -v "$bin" >/dev/null 2>&1; then
    echo "[omx-setup] found $bin"
    return
  fi

  echo "[omx-setup] installing global package: $pkg"
  npm install -g "$pkg"
}

# Codex CLI + OMX / Install codex runtime layer
ensure_global_package "@openai/codex" "codex"
ensure_global_package "oh-my-codex" "omx"

cd "$REPO_ROOT"

echo "[omx-setup] running project-scoped setup..."
omx setup --scope project --force

echo "[omx-setup] running diagnostics..."
omx doctor || true

cat <<'EOF'
[omx-setup] done.
next steps:
  1) omx --madmax --high
  2) use $deep-interview / $ralplan / $ralph or $team
EOF

