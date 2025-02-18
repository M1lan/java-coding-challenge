#!/usr/bin/env bash
exec 3< <(curl -sSL https://jsonplaceholder.typicode.com/users/1 &) ; exec 4< <(curl -sSL https://jsonplaceholder.typicode.com/posts?userId=1 &) ; jq --null-input --argjson user "$(cat <&3)" --argjson posts "$(cat <&4)" '{user: $user, posts: $posts}'
