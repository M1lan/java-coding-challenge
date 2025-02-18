#!/usr/bin/env bash

if [ "$#" -eq 0 ]; then
	exec ./mvnw package
else
	exec ./mvnw "$@"
fi
