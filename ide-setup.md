---
layout: default
title: IDE Setup
---
# {{page.title}} {#ide-setup}
Most of FXDiagram is developed in Eclipse. Just the IDEA plug-in is developed in IDEA. If you want to seriously work with FXDiagram, I recommend setting up a development environment with the source files.


## Eclipse IDE

The easiest way to get the development IDE up and running is to use the Eclipse Installer (formerly known as Oomph).

- Install the [common requirements](gettingstarted.html#common-requirements).
- Download the [Eclipse Installer](https://wiki.eclipse.org/Eclipse_Installer) for your OS, unpack it and run it.
- Make sure it is up-to-date, i.e. there is no exclamation mark on the menu icon in the upper right corner. Update if necessary.
- Click on the menu icon in the upper right corner and choose *Advanced Mode*
- Select the base Eclipse package you want to use for developing FXDiagram. I recommend *Eclipse IDE for Eclipse Committers*. Make sure the Java 1.7+ VM points to your Java 8 SDK. Click *Next*.
- Double-click on *Github.com > FXDiagram* in the tree and click *Next*.
- Set the required directory names, the Git access information etc on the next page. Then click *Next*.
- Oomph now downloads and configures an Eclipse IDE, a target platform and the required Git repositories for you. This may take a while.


## IDEA IDE (experimental)

If you want to develop the InteliJ IDEA plug-in, follow these steps.

- Clone FXDiagram's Git repository.
- Import *de.fxdiagram.idea* as a new project in IDEA. I tried IDEA 14 Community Edition.
- Download the [latest JARs](#install-jars) and put them into the *de.fxdiagram.idea/lib* folder.
- Add the JARs as libraries in *File > Project Structure > Project Settings > Libraries*.
- Make sure you are using the Eclipse Java Compiler.
- Run using *Run > Run 'Plugin'*
