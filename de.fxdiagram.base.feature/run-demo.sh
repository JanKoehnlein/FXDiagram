#!/bin/sh

CP='.'
for file in `find * -name \*.jar` ; do
  CP=$file:$CP
done

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_40.jdk/Contents/Home/
echo $CP
java -cp $CP de.fxdiagram.examples.Demo