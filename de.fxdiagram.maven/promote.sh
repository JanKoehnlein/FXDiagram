#!/bin/sh
echo Replacing the update site with the lates build
pushd .
FXDIAGRAM_GIT=`pwd`/..
FXDIAGRAM_BINTRAY=`pwd`/bintray

rm -rf $FXDIAGRAM_BINTRAY
mkdir $FXDIAGRAM_BINTRAY
cd $FXDIAGRAM_BINTRAY/
mkdir p2
mkdir p2/updates
mkdir standalone

cd p2/updates
rm -rf *
unzip $FXDIAGRAM_GIT/de.fxdiagram.eclipse.updatesite/target/de.fxdiagram.eclipse.updatesite-*.zip 
popd

echo Replacing the standalone jars with the latest build
pushd .
cd $FXDIAGRAM_GIT/de.fxdiagram.base.feature/target/FXDiagram
mv *-lib/*jar .
rmdir *-lib
rm -f org.eclipse.xtext*SNAPSHOT*
rm -f org.eclipse.xtend*SNAPSHOT*
for file in *.jar.jar ; do
	mv $file ${file/.jar.jar/.jar}
done
cp ../../run-demo.sh .
cd ..
zip fxdiagram-jars.zip FXDiagram/* 
cp -f fxdiagram-jars.zip $FXDIAGRAM_BINTRAY/standalone/
popd
echo done
