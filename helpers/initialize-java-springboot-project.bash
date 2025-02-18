#!/usr/bin/env bash

###
# Very simple Java Spring Boot Project initialization script.
#
# Why does this file exists?
#
# Because I have initialized quite a bunch of Spring Boot projects for
# the purpose of training myself for this challenge.  So, I have noticed
# I found myself setting up the "public static void main(String[] args"
# boilerplate every single time after running "spring init" ... so I
# decide to added this during initialization.
#
# Normally I'd do this things like hidden from my Editor / IDE but I
# want to make it transparent for the interviewer and also self-disclose
# my working style. The only thing it adds to 'spring init' is the very
# common MVC-structure of "controller, service, cli, model", the Apps
# main entry point and an empty "DataFetchController" stub.
#
# This script also adds a very basic project-level README.md

##
# Dependencies: Bash >= 4.0, spring cli, recent GNU coreutils
#
# Only tested on Linux. Will probly work on BSD and MacOS too.
#

##
# Author: Milan `insomniaSalt` Santosi  (github.com/m1lan)
# License: MIT
#


########################################################################


##
# "strict mode" even if this is just for tooling.. not PROD.
#
set -euo pipefail
IFS=$'\n\t'

##
# Logs messages with timestamps.
#
_log() {
    printf "%s - %s\n" "$(date +'%Y-%m-%d %H:%M:%S' || exit 1)" "$*"
}

##
# Exits with an error message.
#
_exit_1() {
    printf "Error: %s\n" "$*" >&2
    exit 1
}

##
# Prints usage information.
#
_SELF="$(basename "${0}" || exit 1)"
_print_help() {
    cat <<EOF
Usage: ${_SELF} [options] <project-name>

Options:
  -h, --help    Display this help information.

Example:
  ${_SELF} your-project-name-here

Creates a Spring Boot project in "./your-project-name-here/" with:
  - Spring WebFlux and Cache dependencies.
  - Maven build and Java 17.
  - Package structure adhering to standard Java naming conventions.
EOF
}

##
# Returns the value for a given option or exits if invalid.
#
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
# Check if all os-level deps are available (only the spring boot cli).
#
_check_dependencies() {
    local dependencies=(spring)
    for dep in "${dependencies[@]}"; do
        if ! command -v "$dep" >/dev/null 2>&1; then
            _exit_1 "Dependency '${dep}' is not installed. Exiting."
        fi
    done
}

##
# Turns input into lowercase, because Java.
#
lowercase() {
    printf '%s\n' "${1,,}"
}

##
# Converts a hyphenated string to CamelCase, because Java.
#
toCamelCase() {
    IFS=- read -ra parts <<< "$1"
    local result=""
    for part in "${parts[@]}"; do
        first=$(echo "${part:0:1}" | tr '[:lower:]' '[:upper:]')
        rest="${part:1}"
        result+="${first}${rest}"
    done
    echo "$result"
}

##
# Basic Option Parsing (no script without --help)
#
_PRINT_HELP=0
while [[ "$#" -gt 0 ]]; do
    case "$1" in
        -h|--help)
            _PRINT_HELP=1
            shift
            ;;
        --) # End of options marker
            shift
            break
            ;;
        -*)  # Unknown option
            _exit_1 "Unexpected option: $1"
            ;;
        *)   # Positional argument (project name)
            break
            ;;
    esac
done

if [[ ${_PRINT_HELP} -eq 1 ]]; then
    _print_help
    exit 0
fi

# Ensure a project name is provided.
if [[ "$#" -lt 1 ]]; then
    _print_help
    _exit_1 "Project name is required."
fi

PROJECT_NAME="$1"
PROJECT_DIR="$PROJECT_NAME"

##
# Handle Java naming conventions and other quirks.
#

# Artifact ID: all lowercase, no hyphens.
ARTIFACT_ID=$(lowercase "${PROJECT_NAME//-}")
# Application name: CamelCase + "Application"
APPLICATION_NAME="$(toCamelCase "$PROJECT_NAME")Application"
# Package name: com.example.<artifact_id>
JAVA_PACKAGE="com.example.${ARTIFACT_ID}"


########################################################################
##                the boring stuff ends here                          ##
########################################################################


_log "Initializing '${PROJECT_NAME}' with package '${JAVA_PACKAGE}'..."

# Check if the project directory already exists.
if [[ -d "${PROJECT_DIR}" ]]; then
    _exit_1 "Directory '${PROJECT_DIR}' already exists. Aborting!"
fi

_check_dependencies

##
# Run Spring Boot initializer in the normal way.
#
_log "Running Spring Boot initializer..."
spring init \
	   --dependencies=webflux,cache \
	   --groupId=com.example \
	   --artifactId="${ARTIFACT_ID}" \
	   --name="${APPLICATION_NAME}" \
	   --package-name="${JAVA_PACKAGE}" \
	   --build=maven \
	   "${PROJECT_DIR}"

cd "${PROJECT_DIR}" || _exit_1 "Failed to cd into '${PROJECT_DIR}'."


##
# not enough, so we also create  directory structure!
#
# Created from Java package path (standard Java naming conventuons).
PACKAGE_PATH=$(echo "${JAVA_PACKAGE}" | tr '.' '/')
mkdir -p "src/main/java/${PACKAGE_PATH}/"{controller,service,cli,model}
mkdir -p "src/test/java/${PACKAGE_PATH}"


##
# boilerplate: Create / update the main application file.
MAIN_APP_FILE="src/main/java/${PACKAGE_PATH}/${APPLICATION_NAME}.java"
cat <<EOF > "${MAIN_APP_FILE}"
package ${JAVA_PACKAGE};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ${APPLICATION_NAME} {
    public static void main(String[] args) {
        SpringApplication.run(${APPLICATION_NAME}.class, args);
    }
}
EOF

##
# boilerplate: Create a sample REST controller that does nothing.
CONTROLLER_FILE="src/main/java/${PACKAGE_PATH}/controller/DataFetchController.java"
cat <<EOF > "${CONTROLLER_FILE}"
package ${JAVA_PACKAGE}.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class DataFetchController {

    @GetMapping("/data")
    public Mono<String> fetchData() {

        // TODO: Add async fetching/merging of JSON from source API.

        return Mono.just("{\"message\": \"Hello, world!\"}");
    }
}
EOF

##
# Create a friendly README.md starter.
#
cat <<EOF > README.md
# ${PROJECT_NAME}

This is a Spring Boot microservice that fetches third-party API data
asynchronously, merges the JSON responses into a unified blob, and
serves it via a REST API.

For your convenience, it also includes a CLI mode without the server.

## Getting Started

- Test the project using Maven (no build):
  \`\`\`
  ./mvnw test
  \`\`\`
- Build the project using Maven (no testing):
  \`\`\`
  ./mvnw clean package -DskipTests -Dmaven.test.skip=true
  \`\`\`

- Run the application:
  \`\`\`
  ./mvnw spring-boot:run
  # or alternatively:
  java -jar target/async-data-fetcher-0.0.1-SNAPSHOT.jar
  \`\`\`

- At a later point, I might include a Justfile (similar to Makefile),
  because the Maven cli is quite frankly, a little bit user-unfriendly.


## Endpoints

- GET \`/\`: Returns the unified JSON data.

Happy coding (to myself)!
Happy reviewing (to you)!
EOF

pwd

tree ../"${PROJECT_DIR}"

_log "Project '${PROJECT_NAME}' initialized successfully in directory '${PROJECT_DIR}'."
