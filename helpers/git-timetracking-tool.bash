#!/usr/bin/env bash
# shellcheck disable=SC2034,SC2154

##
# Time-Tracking Auto Saver
#
# This helper monitors a Git-controlled folder and auto-saves its file-system
# state into a time-tracking branch.
#
# Every 5 minutes (hard-coded) the script copies the current state of the
# specified repository (excluding its .git folder) into a separate folder.
#
# Two modes:
#   - Default: runs for 3 hours and 6 minutes.
#   - --forever: runs indefinitely until interrupted.
#
# Depending on your Git version, the script will:
#   - Use Git worktree to create a second checkout of the time-tracking branch
#     (if worktree is supported), OR
#   - Fall back to creating a separate repository in the new folder.
#
# No remote pushes occur during auto-save. When finished, you may later push
# both branches manually.
#
# Usage: $(basename "$0") /path/to/git_repo [--forever]
#
# Author: Milan `insomniaSalt` Santosi  (github.com/m1lan)
# License: MIT
#

set -euo pipefail

#############################
# Configuration & Constants #
#############################

# Total duration in seconds (3 hours and 6 minutes)
TOTAL_DURATION=$((186 * 60))
# Interval between auto-save cycles: 5 minutes (in seconds)
INTERVAL=$((5 * 60))

# Log file in the same directory as the script
LOGFILE="$(dirname "$(realpath "$0")")/time_track.log"

# Run-forever mode flag (default: false)
RUN_FOREVER=false

# Flag indicating whether to use worktree mode (true) or fallback (false)
USE_WORKTREE=false

#####################
# Helper Functions  #
#####################

# Logging function
log() {
    local level="$1"
    local msg="$2"
    local timestamp
    timestamp=$(date '+%Y-%m-%d %H:%M:%S' || exit 1)
    echo -e "${timestamp} [${level}] ${msg}" | tee -a "$LOGFILE"
}

info()    { log "\033[1;36mINFO\033[0m" "$1"; }
success() { log "\033[1;32mSUCCESS\033[0m" "$1"; }
error()   { log "\033[1;31mERROR\033[0m" "$1"; }

# Header display
header() {
    clear
    echo -e "\033[1;35m===========================================\033[0m"
    echo -e "\033[1;35m       Time-Tracking Auto Saver          \033[0m"
    echo -e "\033[1;35m===========================================\033[0m"
}

# Optional desktop notification (if available)
notify() {
    local title="$1"
    local message="$2"
    if command -v notify-send &>/dev/null; then
        notify-send -u normal "$title" "$message"
    elif command -v osascript &>/dev/null; then
        osascript -e "display notification \"${message}\" with title \"${title}\""
    else
        info "Notification: ${title} - ${message}"
    fi
}

# Show help message
show_help() {
    cat << EOF
Usage: $(basename "$0") /path/to/git_repo [--forever]

This script monitors a Git-controlled directory and auto-saves its state
into a time-tracking branch. Every 5 minutes, it:
  - Copies the current state of the specified repository (excluding the .git folder)
    into a separate folder.
  - Commits the changes with an auto-generated message.
  - Uses Git worktree (if supported) to maintain the time-tracking branch, or falls
    back to a separate repository if not.
  - Works entirely locally: the main branch remains untouched and no pushes occur.

Modes:
  Default      - Runs for 3 hours and 6 minutes.
  --forever    - Runs indefinitely until interrupted.

Example:
  $(basename "$0") /path/to/git_repo --forever

EOF
}

# Pre-flight dependency checks and determine worktree support using version check
preflight_checks() {
    local deps=("rsync" "git" "realpath")
    for dep in "${deps[@]}"; do
        if ! command -v "$dep" &>/dev/null; then
            error "Dependency '$dep' is not installed."
            exit 1
        fi
    done

    # Determine Git version and check if it's >= 2.5 (which supports worktree)
    GIT_VER=$(git --version | awk '{print $3}')
    if [[ "$(printf '%s\n' "2.5" "$GIT_VER" | sort -V | head -n1)" = "2.5" ]]; then
        USE_WORKTREE=true
        info "Git worktree supported (version $GIT_VER). Using worktree mode for time-tracking."
    else
        USE_WORKTREE=false
        info "Git worktree not supported (version $GIT_VER). Falling back to a separate repository for time-tracking."
    fi
}

# Cleanup function for graceful termination
cleanup() {
    header
    error "Script interrupted. Exiting gracefully..."
    notify "Time-Tracking Auto Saver" "Script terminated prematurely."
    exit 1
}

# Trap signals for graceful exit
trap cleanup SIGINT SIGTERM

#############################
# Core Functionality        #
#############################

# Validate input and initialize directories
initialize() {
    if [ "$#" -lt 1 ] || [ "$#" -gt 2 ]; then
        error "Usage: $(basename "$0") /path/to/git_repo [--forever]"
        exit 1
    fi

    # Process command-line arguments
    local repo_arg=""
    for arg in "$@"; do
        case "$arg" in
            --forever)
                RUN_FOREVER=true
                ;;
            --help)
                show_help
                exit 0
                ;;
            *)
                repo_arg="$arg"
                ;;
        esac
    done

    if [ -z "$repo_arg" ]; then
        error "Repository path is required."
        exit 1
    fi

    ORIG_DIR=$(realpath "$repo_arg" || exit 1)
    if [ ! -d "$ORIG_DIR" ]; then
        error "Directory '$ORIG_DIR' does not exist."
        exit 1
    fi

    if [ ! -d "$ORIG_DIR/.git" ]; then
        error "Directory '$ORIG_DIR' is not a git repository (missing .git folder)."
        exit 1
    fi

    PARENT_DIR=$(dirname "$ORIG_DIR")
    BASENAME=$(basename "$ORIG_DIR")
    TRACK_DIR="${PARENT_DIR}/${BASENAME}-time-tracking"

    if [ -d "$TRACK_DIR" ]; then
        error "Time-tracking directory already exists: $TRACK_DIR"
        exit 1
    fi

    info "Time-tracking folder will be created at: $TRACK_DIR"
}

# Initialize the time-tracking branch either via worktree or fallback
init_time_tracking() {
    if $USE_WORKTREE; then
        init_time_tracking_worktree
    else
        init_time_tracking_fallback
    fi
}

# Initialize time-tracking using Git worktree
init_time_tracking_worktree() {
    pushd "$ORIG_DIR" > /dev/null
    if git worktree add -b time-tracking "$TRACK_DIR" > /dev/null; then
        success "Created time-tracking worktree at $TRACK_DIR on branch 'time-tracking'."
    else
        error "Failed to create time-tracking worktree."
        exit 1
    fi
    popd > /dev/null
}

# Fallback: Initialize a separate repository for time-tracking
init_time_tracking_fallback() {
    mkdir -p "$TRACK_DIR"
    pushd "$TRACK_DIR" > /dev/null
    if git init > /dev/null; then
        success "Initialized separate git repository for time-tracking at $TRACK_DIR."
    else
        error "Failed to initialize git repository in $TRACK_DIR."
        exit 1
    fi
    if git checkout -b time-tracking > /dev/null; then
        success "Checked out branch 'time-tracking' in time-tracking repository."
    else
        error "Failed to create branch 'time-tracking' in $TRACK_DIR."
        exit 1
    fi
    popd > /dev/null
}

# Synchronize directories and commit changes (no push)
sync_commit() {
    header
    info "Cycle $CYCLE - $(date)"

    # Copy current state from main repository to time-tracking folder
    rsync -a --delete --exclude='.git' "$ORIG_DIR"/ "$TRACK_DIR"/
    info "Directory synchronized."

    pushd "$TRACK_DIR" > /dev/null
    git add -A

    COMMIT_MSG="Auto-save: $(date '+%Y-%m-%d %H:%M:%S') - Cycle $CYCLE"

    if ! git rev-parse --verify HEAD > /dev/null 2>&1; then
        if git commit -m "$COMMIT_MSG" > /dev/null; then
            success "Initial commit with message: '$COMMIT_MSG'"
        else
            error "Failed to commit changes (initial commit)."
            notify "Git Error" "Initial commit failed in time-tracking branch."
        fi
    elif git diff-index --quiet HEAD --; then
        info "No changes detected. Skipping commit."
    else
        if git commit -m "$COMMIT_MSG" > /dev/null; then
            success "Committed changes with message: '$COMMIT_MSG'"
        else
            error "Failed to commit changes."
            notify "Git Error" "Commit failed in time-tracking branch."
        fi
    fi
    popd > /dev/null
}

# Countdown timer between cycles
countdown() {
    for ((i = INTERVAL; i > 0; i--)); do
        printf "\rNext update in: %02d seconds " "$i"
        sleep 1
    done
    echo
}

# Entrypoint
main() {
    preflight_checks
    initialize "$@"
    init_time_tracking

    header
    if $RUN_FOREVER; then
        info "Monitoring '$ORIG_DIR' indefinitely (every 5 minutes)..."
    else
        info "Monitoring '$ORIG_DIR' for $((TOTAL_DURATION / 60)) minutes (every 5 minutes)..."
        START_TIME=$(date +%s)
        END_TIME=$((START_TIME + TOTAL_DURATION))
    fi

    CYCLE=1
    while $RUN_FOREVER || [ "$(date +%s)" -lt "$END_TIME" ]; do
        sync_commit
        CYCLE=$((CYCLE + 1))
        countdown
    done

    header
    success "Time tracking complete. Auto-saving stopped."
    notify "Time-Tracking Auto Saver" "Time tracking complete. Auto-saving stopped."
}

# Process command-line help option
if [[ "${1:-}" == "--help" ]]; then
    show_help
    exit 0
fi

# Start the script with provided arguments
main "$@"
