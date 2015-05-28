#!/bin/sh

read -p 'Version: ' VERSION
mvn -Dtycho.mode=maven tycho-versions:set-version -DnewVersion=$VERSION

