#!/usr/bin/env bash

# Test script for fetch-merge-serve.bash

SCRIPT_PATH="./fetch-merge-serve.bash"
TEST_PORT=9090

_log() {
    printf "%s - %s\n" "$(date +'%Y-%m-%d %H:%M:%S' || true)" "$*"
}

_assert() {
    if [[ $1 -ne 0 ]]; then
        _log "Test failed: $2"
        exit 1
    else
        _log "Test passed: $2"
    fi
}

_start_server() {
    "$SCRIPT_PATH" --port "$TEST_PORT" --serve &
    SERVER_PID=$!
    sleep 1
}

_stop_server() {
    kill "$SERVER_PID"
}

# Unit Tests
_test_fetch_json() {
    local temp_file
    temp_file=$(mktemp)

    # Test fetching JSON from nc
    curl -s "http://127.0.0.1:$TEST_PORT" -o "$temp_file"
    _assert $? "Fetch JSON from server"

    # Check if the output is valid JSON
    jq . "$temp_file" >/dev/null 2>&1
    _assert $? "Validate JSON output"

    # Clean up
    rm -f "$temp_file"
}

_test_merge_json() {
    local merged_file
    merged_file=$(mktemp)

    # Merge JSON files
    curl -s "http://127.0.0.1:$TEST_PORT" -o "$merged_file"
    _assert $? "Merge JSON files"

    # Check if the output is valid JSON
    jq . "$merged_file" >/dev/null 2>&1
    _assert $? "Validate merged JSON output"

    # Clean up
    rm -f "$merged_file"
}

# Integration Tests
_test_normal_mode() {
    local output
    output=$(mktemp)

    # Run the script in normal mode
    "$SCRIPT_PATH" > "$output"
    _assert $? "Run script in normal mode"

    # Check if the output is valid JSON
    jq . "$output" >/dev/null 2>&1
    _assert $? "Validate JSON output in normal mode"

    # Clean up
    rm -f "$output"
}

# Stress Tests
_test_stress() {
    # Use ApacheBench to simulate concurrent requests
    ab -n 100 -c 10 "http://127.0.0.1:$TEST_PORT/" > stress_test_results.txt
    _assert $? "Stress test with ApacheBench"

    # Check for failed requests in the results
    if grep -q "Failed requests" stress_test_results.txt; then
        _log "Stress test failed: Some requests failed."
        exit 1
    else
        _log "Stress test passed: All requests succeeded."
    fi
}

# Shellcheck Tests
_test_shellcheck() {
    shellcheck "$SCRIPT_PATH"
    _assert $? "ShellCheck for checking the script for obvious problems"
}

# Run all tests

_log "Running shellcheck..."
_test_shellcheck

_log "Running unit tests..."
_test_fetch_json
_test_merge_json

_log "Running integration tests..."
_test_normal_mode

_log "Starting server..."
_start_server

_log "Running stress tests..."
_test_stress

_log "Stopping server..."
_stop_server


_log "All tests passed!"
