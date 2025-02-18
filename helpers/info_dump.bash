#!/usr/bin/env bash

###
# info_dump.sh
# Generates an information dump about this project.
# It outputs system and project metadata into info_dump.txt

###
##  Author: Milan Santosi. February 16, 2025. MIT License.
#

echo "========== PROJECT INFO DUMP =========="
echo "Generated on: $(date || exit 1)"
echo "---------------------------------------"

echo ">>> Operating System Information:"
uname -a
echo ""

echo ">>> Java Version:"
java -version 2>&1
echo ""

echo ">>> Git Information:"
git --version 2>&1
echo "Current Branch: $(git rev-parse --abbrev-ref HEAD 2>/dev/null || exit 1)"
echo "Latest Commit:"
git log -1 --oneline 2>/dev/null
echo ""

echo ">>> Directory Structure (depth 2):"
# Use 'tree' if available; otherwise fallback to 'find'
if command -v tree &>/dev/null; then
    tree -L 2
else
    find . -maxdepth 2 -print | sort
fi

echo "========== END OF DUMP =========="
