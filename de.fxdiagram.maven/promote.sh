#!/bin/sh
echo Replacing the update site with the lates build
pushd .
FXDIAGRAM_GIT=`pwd`/..
FXDIAGRAM_GHPAGES=$FXDIAGRAM_GIT/../FXDiagram.gh-pages

cd $FXDIAGRAM_GHPAGES/p2/updates/latest
rm -rf *
unzip $FXDIAGRAM_GIT/de.fxdiagram.eclipse.updatesite/target/de.fxdiagram.eclipse.updatesite-*.zip 
popd

echo Replacing the standalone jars with the latest build
pushd .
cd $FXDIAGRAM_GIT/de.fxdiagram.base.feature/target/FXDiagram
mv *-lib/*jar .
rmdir *-lib
for file in *.jar.jar ; do
	mv $file ${file/.jar.jar/.jar}
done
cp ../../run-demo.sh .
cd ..
zip fxdiagram-jars.zip FXDiagram/* 
cp -f fxdiagram-jars.zip $FXDIAGRAM_GHPAGES/standalone/
popd
echo done
