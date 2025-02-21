#!/usr/bin/env bash

##
# project init.
#
# run from outside of new empty project dir or use the force!

PROJECT_NAME="$1"
PROJECT_DIR="round2"#fight!
##PROJECT_DIR="$PROJECT_NAME"

# Artifact ID: all lowercase, no hyphens.
ARTIFACT_ID=$(lowercase "${PROJECT_NAME//-}")

# Application name: CamelCase + "Application"
APPLICATION_NAME="$(toCamelCase "$PROJECT_NAME")Application"

# Package name: com.example.<artifact_id>
JAVA_PACKAGE="com.example.${ARTIFACT_ID}"
spring init \
	   --groupId=com.example \
	   --artifactId="${ARTIFACT_ID}" \
	   --name="${APPLICATION_NAME}" \
	   --package-name="${JAVA_PACKAGE}" \
	   --build=maven  "${PROJECT_DIR}"
