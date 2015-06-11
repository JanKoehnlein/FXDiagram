---
layout: default
title: Get Started
---
# {{page.title}} {#getting-started}

If you just want to use FXDiagram or play with the examples follow the instructions on this page. If you want to develop your own diagrams you should install the [common requirements]{#common-requirements} and [setup an entire IDE](ide-setup.html/#setup-ide) instead.


## Common Requirements {#common-requirements}

FXDiagram runs in Eclipse, in IDEA and in standalone JavaFX applications. In any case, you have to install the common requirements.

FXDiagram is build with JavaFX 8. To run it without glitches you need a [Java 8 SDK u40](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or higher. We rely on bugfixes in the rendering engine.

For auto-layout [Graphviz](http://www.graphviz.org/) must be installed on your system.


## Running FXDiagram in Eclipse {#install}

- Make sure you're running [Eclipse 4.4 (Luna)](http://www.eclipse.org/downloads/) or higher on a [Java 8 JRE](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
- Point your update manager *(Help > Install New Software)* to the link [http://dl.bintray.com/jankoehnlein/FXDiagram].
- Select *FXDiagram > FXDiagram SDK*.
- If you want to have the source code as well, additionally select *FXDiagram > FXDiagram SDK Developer Resources*. This will allow you to step through the code 
with a debugger. Note that if you really want to work with FXDiagram, you should rather [setup the IDE](ide-setup.html#setup-ide).
- Click *Next* and follow the installation wizard. This will automatically install some required plug-ins from the KIELER project and e(fx)clipse, as well as Xtext including the domain model and statemachine example DSLs.
- Restart Eclipse when prompted to do so.

You have now installed four different diagram configurations. They are bound to different types of model elements. If you need some example DSL files, download and install the [Eclipse example project](http://dl.bintray.com/jankoehnlein/FXDiagram/#FXDiagram_Eclipse_Examples.zip) into your workspace.
The diagram view will automatically be opened and populated once you choose the  *Show in FXDiagram as...* action from the context menu:

- on a statemachine, a state or a transition in the editor of a *statemachine* file.
- on an entity or package in the editor of a *dmodel* file.
- on a Java/Xtend/Xbase type in the respective editor.
- on a plug-in project in the navigator to explore the dependencies of Eclipse plug-ins.

Most nodes can be explored using rapid buttons. Watch out for tooltips. 


## Running FXDiagram in plain Java {#install-jars}

- Download the the [latest zip distribution of FXDiagram](http://dl.bintray.com/jankoehnlein/FXDiagram/standalone/) and unpack it. It includes all necessary JARs and some runner scripts.
- To run the demo application, open *run_demo.bat* (Windows) or *run_demo.sh* (MacOSX, Linux) and make sure the *JAVA_HOME* variable points to the directory where your Java 8 SDK is installed. 
- On Windows, double-click on the batch file, on Linux/MacOSX open a terminal, change to the directory where you unzipped the files and enter `./run_demo.sh`.

The demo application includes the slides of a talk I gave at EclipseCon Europe 2013 and demoes various features of FXDiagram. Some nodes can be explored using rapid buttons. Watch out for tooltips. 


## Running FXDiagram in IntelliJ IDEA (experimental)

- Download the [FXDiagram IDEA plug-in](http://dl.bintray.com/jankoehnlein/FXDiagram/idea/#de.fxdiagram.idea.zip) and save it to disk.
- Make sure you are running [IDEA](https://www.jetbrains.com/idea/) with Java 8. Here are [some hints how to do that](https://intellij-support.jetbrains.com/entries/23455956-Selecting-the-JDK-version-the-IDE-will-run-under) for your OS. Note that officially Java 8 is not yet supported by IntelliJ.
-  Choose *Preferences > Plug-ins > Install plug-in from disk...* to install the plug-in.

After a restart, you will have a class diagram editor for Java classes. Choose *Show in FXDiagram as...* in the context menu of a Java class to take it for a spin.

