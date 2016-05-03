#!/bin/bash
#Sample Usage: pushToBintray.sh username apikey owner repo package version pathToP2Repo
read -p "Version: " PCK_VERSION
read -s -p "Key: " BINTRAY_API_KEY
PUSH_ALL=true
API=https://api.bintray.com
BINTRAY_USER=jankoehnlein
BINTRAY_OWNER=jankoehnlein
BINTRAY_REPO=FXDiagram
PCK_NAME=FXDiagram
PATH_TO_REPOSITORY=bintray

function main() {
	deploy_updatesite
	mirror_updatesite
}

function deploy_updatesite() {
	#echo "${BINTRAY_USER}"
	#echo "${BINTRAY_API_KEY}"
	#echo "${BINTRAY_OWNER}"
	#echo "${BINTRAY_REPO}"
	#echo "${PCK_NAME}"
	#echo "${PCK_VERSION}"
	#echo "${PATH_TO_REPOSITORY}"

	if [ ! -z "$PATH_TO_REPOSITORY" ]; then
	   cd $PATH_TO_REPOSITORY
	fi

	FILES=./*
	BINARYDIR=./standalone/*
	PLUGINDIR=./plugins/*
	FEATUREDIR=./features/*

	echo "Processing features dir $FEATUREDIR file..."
	for f in $FEATUREDIR;
	do
	  echo "Processing feature: $f file..."
	  if [[ PUSH_ALL ]] || [[ "$f" == *${PCK_VERSION}* ]]
	  then
	  	curl -X PUT -T $f -u ${BINTRAY_USER}:${BINTRAY_API_KEY} https://api.bintray.com/content/${BINTRAY_OWNER}/${BINTRAY_REPO}/${PCK_NAME}/${PCK_VERSION}/$f;publish=0
	  	echo ""
	  fi
	done

	echo "Processing plugin dir $PLUGINDIR file..."

	for f in $PLUGINDIR;
	do
	   # take action on each file. $f store current file name
	  echo "Processing plugin: $f file..."
	  if [[ PUSH_ALL ]] || [[ "$f" == *${PCK_VERSION}* ]]
	  then
	  	curl -X PUT -T $f -u ${BINTRAY_USER}:${BINTRAY_API_KEY} https://api.bintray.com/content/${BINTRAY_OWNER}/${BINTRAY_REPO}/${PCK_NAME}/${PCK_VERSION}/$f;publish=0
	  	echo ""
	  fi
	done

	echo "Processing binary dir $BINARYDIR file..."
	for f in $BINARYDIR;
	do
	   # take action on each file. $f store current file name
	  echo "Processing plugin: $f file..."
	  curl -X PUT -T $f -u ${BINTRAY_USER}:${BINTRAY_API_KEY} https://api.bintray.com/content/${BINTRAY_OWNER}/${BINTRAY_REPO}/${PCK_NAME}/${PCK_VERSION}/${f/\.zip/-${PCK_VERSION}.zip};publish=0
	  echo ""
	done

	echo "Publishing the new version"
	curl -X POST -u ${BINTRAY_USER}:${BINTRAY_API_KEY} https://api.bintray.com/content/${BINTRAY_OWNER}/${BINTRAY_REPO}/${PCK_NAME}/${PCK_VERSION}/publish -d "{ \"discard\": \"false\" }"

	for f in $FILES;
	do
		if [ ! -d $f ]; then
		  echo "Processing $f file..."
		  if [[ "$f" == *content.jar ]] || [[ "$f" == *artifacts.jar ]]
		  then
		    echo "Uploading p2 metadata file directly to the repository"
		    curl -X PUT -T $f -u ${BINTRAY_USER}:${BINTRAY_API_KEY} https://api.bintray.com/content/${BINTRAY_OWNER}/${BINTRAY_REPO}/$f;publish=0
		  else
		    curl -X PUT -T $f -u ${BINTRAY_USER}:${BINTRAY_API_KEY} https://api.bintray.com/content/${BINTRAY_OWNER}/${BINTRAY_REPO}/${PCK_NAME}/${PCK_VERSION}/$f;publish=0
		  fi
		  echo ""
		fi
	done
}

function mirror_updatesite() {
	if [ ! -z "$PATH_TO_REPOSITORY" ]; then
	   cd $PATH_TO_REPOSITORY
	fi
	COPY_DIR=../../../p2mirror
	echo "Saving bintray clone to $COPY_DIR"
	if [ ! -d $COPY_DIR ]; then
		mkdir $COPY_DIR
		pushd .
		cd $COPY_DIR
		git init
		popd
	fi
	cp -rn * $COPY_DIR/
	cp -f *.jar $COPY_DIR/
	pushd .
	cd $COPY_DIR
	git add -A
	git commit -m "Version ${PCK_VERSION}"
	popd
}

main "$@"
