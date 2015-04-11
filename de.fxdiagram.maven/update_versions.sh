#!/bin/sh

read -p 'Version: ' VERSION

function main() {
	pushd .
	cd ../
	change_versions
	popd
	mvn tycho-versions:update-pom
}

function change_versions() {
	for file in `find * -name MANIFEST.MF` ; do
		sed -i '' -e "s/Bundle-Version: .*$/Bundle-Version: ${VERSION}.qualifier/1" $file
	done
}

main "$@"