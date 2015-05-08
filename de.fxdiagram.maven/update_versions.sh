#!/bin/sh

read -p 'Version: ' VERSION

function main() {
	mvn tycho-versions:set-version -DnewVersion=$VERSION
}

main "$@"