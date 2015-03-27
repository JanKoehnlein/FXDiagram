@echo off
setlocal EnableDelayedExpansion

for /r %%f in (*.jar) do (
	set CLASSPATH=%%f;!CLASSPATH!
)

echo CLASSPATH=%CLASSPATH%

if not defined JAVA_HOME (
	:: Replace with the path to your local JDK.
	set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_40
)

echo JAVA_HOME=%JAVA_HOME%

java -cp !CLASSPATH! de.fxdiagram.examples.Demo