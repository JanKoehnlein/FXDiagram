#!/bin/sh
#set -x
echo Replacing the update site with the latest build
pushd .
FXDIAGRAM_GIT=`pwd`/..
FXDIAGRAM_BINTRAY=`pwd`/bintray

rm -rf $FXDIAGRAM_BINTRAY
mkdir $FXDIAGRAM_BINTRAY
cd $FXDIAGRAM_BINTRAY/

rm -rf *
mkdir standalone
unzip $FXDIAGRAM_GIT/de.fxdiagram.eclipse.updatesite/target/de.fxdiagram.eclipse.updatesite-*.zip 
popd

echo Replacing the standalone jars with the latest build
pushd .
cd $FXDIAGRAM_GIT/de.fxdiagram.base.feature/target/FXDiagram
mv *-lib/*jar .
for file in *.jar.jar ; do
  mv $file ${file/.jar.jar/.jar}
done
rmdir *-lib
rm -f org.eclipse.xtext*SNAPSHOT*
rm -f org.eclipse.xtend*SNAPSHOT*
rm $FXDIAGRAM_GIT/de.fxdiagram.idea/libs/*
cp *.jar $FXDIAGRAM_GIT/de.fxdiagram.idea/libs
cp ../../run-demo.sh .
cp ../../run-demo.bat .
cd ..
zip fxdiagram-jars.zip FXDiagram/* 
cp -f fxdiagram-jars.zip $FXDIAGRAM_BINTRAY/standalone/
popd
echo done
