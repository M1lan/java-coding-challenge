#!/usr/bin/env bash
#
# Prototype for "(async) fetch, merge, and serve back JSON"
#
# Note: I am using my own production-ready bash template & style guide:
# https://gist.github.com/M1lan/1454743f94f455a0aeff692c29a71ea0

#
# This tool runs in two modes:
#   1. Normal mode: outputs the combined JSON to stdout and exits.
#   2. Server mode (--serve): starts an HTTP server (default port 8181)
#      that serves the combined JSON on every connection.
#
# Use --help to learn more.
#

#####################################################################
#                    Header and Helpers                             #
#####################################################################

# Shellcheck configuration.
# shellcheck enable=avoid-nullary-conditions,add-default-case,check-extra-masked-returns
# shellcheck enable=require-double-brackets,check-unassigned-uppercase,deprecate-which

# "Strict mode" (apart from commands like curl that we allow to fail and retry)
set -euo pipefail
IFS=$'\n\t'

_SELF="$(basename "${0}" || true)"
_PRINT_HELP=0
_SERVE=0
_PORT="${PORT:-8181}"
_RETRY_COUNT="${RETRY_COUNT:-3}"
_RETRY_DELAY="${RETRY_DELAY:-2}"
_CURL_TIMEOUT="${CURL_TIMEOUT:-5}"
_VERBOSE="${VERBOSE:-0}"

# Caching settings.
# CACHE_TTL: seconds before the merged JSON cache expires.
_CACHE_TTL="${CACHE_TTL:-30}"
# CACHE_FILE: location to store the merged JSON.
_CACHE_FILE="${CACHE_FILE:-/tmp/${_SELF}_merged.cache}"
# CACHE_LOCK: lock file to avoid concurrent cache updates.
_CACHE_LOCK="${CACHE_LOCK:-/tmp/${_SELF}_cache.lock}"

# Global array for temporary files.
TEMP_FILES=()

##
# _exit_1()
# Prints an error message (in red, if possible) and exits with status 1.
_exit_1() {
    {
        printf "%s " "$(tput setaf 1 || true)!$(tput sgr0 || true)"
        "$@"
    } 1>&2
    exit 1
}

##
# _print_help()
# Prints usage information.
_print_help() {
    cat <<EOF
Usage: ${_SELF} [options]

Options:
  -h, --help                   Display this help information.
  -s, --serve                  Run in HTTP server mode.
  -p, --port PORT              Port to listen on (default: 8181).
  -v, --verbose                Enable verbose output.
  -c, --config FILE            Configuration file (.conf format).

Environment Variables:
  PORT                         Port to listen on (default: 8181).
  RETRY_COUNT                  Number of retry attempts for network requests (default: 3).
  RETRY_DELAY                  Delay between retry attempts in seconds (default: 2).
  CURL_TIMEOUT                 Timeout for curl requests in seconds (default: 5).
  VERBOSE                      Enable verbose output (0/1, default: 0).
  CACHE_TTL                    Seconds to cache the merged JSON (default: 30).

Examples:
  ${_SELF}                         # Output JSON blob once.
  ${_SELF} --serve                 # Start HTTP server on port 8181.
  ${_SELF} --serve --port 9090     # Start HTTP server on port 9090.
  VERBOSE=1 ${_SELF}               # Run with verbose output.
EOF
}

##
# __get_option_value()
# Returns the value for a given option or exits if missing/invalid.
__get_option_value() {
    local __arg="${1:-}"
    local __val="${2:-}"
    if [[ -n "${__val:-}" ]] && [[ ! "${__val:-}" =~ ^- ]]; then
        printf "%s\n" "$__val"
    else
        _exit_1 "Option '${__arg}' requires a valid argument."
    fi
}

##
# _check_dependencies()
# Ensures that required commands are available.
_check_dependencies() {
    local dependencies=(curl jq mktemp tcpserver flock stat)
    for dep in "${dependencies[@]}"; do
        if ! command -v "$dep" >/dev/null 2>&1; then
            _exit_1 "Dependency '${dep}' is not installed. Exiting."
        fi
    done
}

##
# _validate_port()
# Validates that the port number is within the valid range (1-65535).
_validate_port() {
    local port="${1:-}"
    if ! [[ "$port" =~ ^[0-9]+$ ]] || ((port < 1 || port > 65535)); then
        _exit_1 "Invalid port number: ${port}. Port must be between 1 and 65535."
    fi
}

##
# _log()
# Logs messages with timestamps.
_log() {
    printf "%s - %s\n" "$(date +'%Y-%m-%d %H:%M:%S' || true)" "$*"
}

##
# _verbose_log()
# Logs messages with timestamps but only if verbose mode is enabled.
_verbose_log() {
    if [[ $_VERBOSE -eq 1 ]]; then
        _log "$@"
    fi
}

##
# _load_config()
# Loads configuration from a .conf file.
_load_config() {
    local config_file="${1:-}"
    if [[ -f "$config_file" ]]; then
        _verbose_log "Loading configuration from ${config_file}..."
        # shellcheck source=/dev/null
        source "$config_file"
    else
        _verbose_log "Configuration file ${config_file} not found. Using default settings."
    fi
}

##
# _create_temp_file()
# Creates a secure temporary file.
_create_temp_file() {
    mktemp -t "${_SELF}.XXXXXX"
}

##
# _cleanup()
# Cleans up temporary files on exit.
_cleanup() {
    rm -f "${TEMP_FILES[@]}" 2>/dev/null || true
}

##
# _handle_signal()
# Handles signals (e.g., Ctrl-C) to allow graceful shutdown.
_handle_signal() {
    _log "Received termination signal. Cleaning up..."
    _cleanup
    exit 0
}

#####################################################################
#                      Core Functionality                           #
#####################################################################

##
# Data endpoints we fetch from...
TYPI_USERS="http://jsonplaceholder.typicode.com/users/1"
TYPI_POSTS="http://jsonplaceholder.typicode.com/posts?userId=1"

##
# _fetch_json()
#
# Fetches a JSON resource via curl and outputs it to a specified file.
_fetch_json() {
    local url=$1
    local output=$2
    local retry_count=0

    while [[ $retry_count -lt $_RETRY_COUNT ]]; do
        if curl -sSL --max-time "$_CURL_TIMEOUT" "$url" -o "$output"; then
            _verbose_log "Successfully fetched JSON from ${url}"
            break
        fi
        _log "Failed to fetch JSON from ${url}. Retrying..."
        ((retry_count++))
        sleep "$_RETRY_DELAY"
    done

    if [[ $retry_count -eq $_RETRY_COUNT ]]; then
        _exit_1 "Failed to fetch JSON from ${url} after ${_RETRY_COUNT} attempts."
    fi
}

##
# _merge_json()
#
# Merges two JSON files into a single JSON object.
_merge_json() {
    local user_file=$1
    local posts_file=$2
    local output=$3

    jq --null-input --argjson user "$(< "$user_file")" --argjson posts "$(< "$posts_file")" '{user: $user, posts: $posts}' > "$output"
}

##
# _get_merged_json_cache()
#
# Checks if the merged JSON is cached and fresh. If not, fetches the
# data, merges it, and updates the cache. Returns the path to the merged
# JSON file.
_get_merged_json_cache() {
    local cache_file="${_CACHE_FILE}"
    local cache_ttl="${_CACHE_TTL}"
    local now
    now=$(date +%s)

    if [[ -f "$cache_file" ]]; then
        local mtime
        mtime=$(stat -c %Y "$cache_file")
        if (( now - mtime < cache_ttl )); then
            _verbose_log "Using cached JSON (age: $(( now - mtime )) sec)"
            echo "$cache_file"
            return 0
        fi
    fi

    # If we reach here, the cache is missing or expired.
    (
		# Use flock to ensure that only one process updates the cache.
		flock -n 200 || { _verbose_log "Another process updating cache; waiting..."; flock 200; }
		# Re-check cache after acquiring lock.
		if [[ -f "$cache_file" ]]; then
			local mtime_after
			mtime_after=$(stat -c %Y "$cache_file")
			if (( now - mtime_after < cache_ttl )); then
				_verbose_log "Cache updated while waiting; using cached JSON"
				echo "$cache_file"
				exit 0
			fi
		fi
		_verbose_log "Fetching JSON endpoints for cache update..."
		local user_file posts_file merged_file
		user_file="$(_create_temp_file)"
		posts_file="$(_create_temp_file)"
		merged_file="$(_create_temp_file)"
		TEMP_FILES+=("$user_file" "$posts_file" "$merged_file")
		_fetch_json "$TYPI_USERS" "$user_file" &
		_fetch_json "$TYPI_POSTS" "$posts_file" &
		wait
		_merge_json "$user_file" "$posts_file" "$merged_file"
		cp "$merged_file" "$cache_file"
		_verbose_log "Cache updated at ${cache_file}"
		echo "$cache_file"
    ) 200>"${_CACHE_LOCK}"
}

##
# _handle_connection()
#
# Handles a single connection: outputs HTTP headers and the JSON
# response.  Supports ApacheBench for stress testing.
_handle_connection() {
    # Drain the incoming HTTP request headers.
    while IFS= read -r line; do
        line="${line%%$'\r'}"
        [[ -z "$line" ]] && break
    done

    # Get the merged JSON (using cache if fresh).
    local merged_file
    merged_file="$(_get_merged_json_cache)"
    local content_length
    content_length=$(stat -c%s "$merged_file" 2>/dev/null || wc -c < "$merged_file")

    # Output HTTP headers with Content-Length and Connection: close.
    printf "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nContent-Length: %s\r\nConnection: close\r\n\r\n" "$content_length"
    cat "$merged_file"

    _cleanup
}

##
# _serve_http()
#
# Starts an HTTP server on a specified port using tcpserver.
# Each incoming connection spawns a new instance of this script with --handle-connection.
_serve_http() {
    local port="${1:-8282}"
    _log "Starting HTTP server on port ${port} using tcpserver..."

    # Trap signals (e.g., Ctrl-C) for graceful shutdown.
    trap '_handle_signal' SIGINT SIGTERM

    # tcpserver listens on 0.0.0.0 at the specified port and, for each connection,
    # re-invokes this script with the --handle-connection flag.
    tcpserver -v 0.0.0.0 "$port" "$0" --handle-connection
}

##
# _main()
#
# Entry point: check dependencies and either print help, run the server,
# or output JSON.
_main() {
    _check_dependencies
    _validate_port "$_PORT"

    if ((_PRINT_HELP)); then
        _print_help
        exit 0
    elif ((_SERVE)); then
        _serve_http "$_PORT"
    else
        # Non-server mode: output merged JSON (using cache if available)
        local merged_file
        merged_file="$(_get_merged_json_cache)"
        cat "$merged_file"
        _cleanup
    fi
}

#####################################################################
#                           Option Parsing                          #
#####################################################################
while ((${#})); do
    __arg="${1:-}"
    __val="${2:-}"
    case "$__arg" in
        -h|--help)
            _PRINT_HELP=1
            ;;
        -s|--serve)
            _SERVE=1
            ;;
        -p|--port)
            _PORT="$(__get_option_value "$__arg" "${__val:-}")"
            _validate_port "$_PORT"
            shift
            ;;
        -v|--verbose)
            _VERBOSE=1
            ;;
        -c|--config)
            _config=$(__get_option_value "$__arg" "${__val:-}")
            _load_config "$_config"
            shift
            ;;
        --handle-connection)
            # Internal flag used by tcpserver: handle one connection then exit.
            _handle_connection
            exit 0
            ;;
        -*)
            _exit_1 "Unexpected option: ${__arg}"
            ;;
        *)
            break
            ;;
    esac
    shift
done

#####################################################################
#                         Script Entry Point                        #
#####################################################################
_main "$@"
