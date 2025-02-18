#!/usr/bin/env bash

###
# project-info.bash
#
# Generates an information dump about this project and metadata.
#
# Dev tooling, not for use in PROD env.
#
# ZERO warranty for any purpose!
#
#
# Author: Milan `insomniaSalt` Santosi  (github.com/m1lan)
#
# February 16, 2025. MIT License.


##
# Function to rint a separator line
#

print_sep() {
    echo "---------------------------------------"
}

echo "========== PROJECT INFO DUMP =========="
echo "Generated on: $(date)"
print_sep

echo ">>> Operating System Information:"
fastfetch || uname -a
echo ""

echo ">>> Java Version:"
java -version 2>&1
echo ""

echo ">>> Git Information:"
if command -v git &>/dev/null; then
    git --version 2>&1
    echo "Current Branch: $(git rev-parse --abbrev-ref HEAD 2>/dev/null)"
    echo "Latest Commit:"
    git log -1 --oneline 2>/dev/null
else
    echo "Git is not installed." && exit 1
fi
echo ""

echo ">>> Directory Structure (depth 2):"
# Use 'tree' if available; otherwise fallback to 'find'
if command -v tree &>/dev/null; then
    tree -L 2
else
    find . -maxdepth 2 -print | sort
fi
echo ""

##
# Java-Dependencies
#
echo ">>> Java-Level Dependencies:"
if [ -f pom.xml ]; then
    echo "Maven build file detected. Listing dependencies..."
    ./mvnw dependency:tree || echo "Could not list Java dependencies."
else
    echo "No Maven (pom.xml)  build file found. Skipping Java dependency extraction."
fi
echo ""

##
# Project Statistics
#
echo ">>> Project Statistics:"
total_files=$(find . -type f | wc -l)
echo "Total number of files: $total_files"

# Get lines of code using cloc, or count lines in .java files manually.
if command -v cloc &>/dev/null; then
    echo ""
    echo "Code statistics (using cloc):"
    cloc .
else
    echo ""
    echo "cloc is not installed. Counting Java files manually:"
    java_files=$(find . -name "*.java")
    java_line_count=0
    for file in $java_files; do
        file_lines=$(wc -l < "$file")
        java_line_count=$((java_line_count + file_lines))
    done
    echo "Total lines of Java code: $java_line_count"
fi

##
# Git Statistics
#
if command -v git &>/dev/null && git rev-parse --is-inside-work-tree &>/dev/null; then
    echo ""
    echo ">>> Git Statistics:"

    # Total commits
    commit_count=$(git rev-list --all --count)
    echo "Total git commits: $commit_count"

    # First commit date and latest commit date
    first_commit_date=$(git log --reverse --format="%at" | head -1)
    latest_commit_date=$(git log -1 --format="%at")
    if [[ -n "$first_commit_date" && -n "$latest_commit_date" ]]; then
        first_commit=$(date -d @"$first_commit_date" "+%Y-%m-%d")
        latest_commit=$(date -d @"$latest_commit_date" "+%Y-%m-%d")
        echo "Project started on: $first_commit"
        echo "Project last updated on: $latest_commit"

        # Calculate project duration in days
        duration_days=$(( (latest_commit_date - first_commit_date) / 86400 ))
        echo "Total project duration: ${duration_days} day(s)"

        # Calculate average commits per day (avoid division by zero)
        if [ "$duration_days" -gt 0 ]; then
            avg_commits_per_day=$(echo "scale=2; $commit_count / $duration_days" | bc)
            echo "Average commits per day: $avg_commits_per_day"
        else
            echo "Average commits per day: $commit_count (project duration less than 1 day)"
        fi
    else
        echo "Could not determine project start and end dates."
    fi

    # Number of branches
    branch_count=$(git branch --list | wc -l | tr -d ' ')
    echo "Total number of branches: $branch_count"

    # Number of tags
    tag_count=$(git tag | wc -l | tr -d ' ')
    echo "Total number of tags: $tag_count"

    # Top contributors
    echo ""
    echo "Top Contributors (by commit count):"
    git shortlog -s -n 2>/dev/null
fi

echo ""
echo "========== END OF DUMP =========="





#!/usr/bin/env bash

###
# info_dump.sh
# Generates an information dump about this project.
# It outputs system and project metadata into info_dump.txt


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
