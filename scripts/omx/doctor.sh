#!/usr/bin/env bash
set -euo pipefail

# 仓库根目录 / repository root
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$REPO_ROOT"

if ! command -v omx >/dev/null 2>&1; then
  echo "[omx-doctor] command 'omx' not found. run scripts/omx/setup-project.sh first."
  exit 1
fi

echo "[omx-doctor] running omx doctor..."
omx doctor

