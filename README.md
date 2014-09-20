# FXDiagram

A next generation diagram editing framework based on JavaFX. Strong focus
on usability aspects.


## DISCLAIMER
* Use at your own risk. I cannot give any support on this.
* I tested on MacOSX only. 
* The LCARS demo won't run as it depends on a currently unreleased database. 

## PREREQUISITES
* EITHER
 * Java 7 SDK
 * Eclipse Kepler 4.3
  * Xtext SDK 2.7 (including Xtend 2.7)
  * e(fx)clipse 0.9 

* OR
 * Java 8 SDK 40 early access build >=5 (https://jdk8.java.net/download.html)
  (must include fix for https://javafx-jira.kenai.com/browse/RT-37879)
 * Eclipse Luna 4.4
  * Xtext SDK 2.7
  * e(fx)clipse 1.0
	
As of version 1.0 e(fx)clipse no longer supports Java 7. If you're stuck 
with Java 7 you need the e(fx)clipse version 0.9 which runs on Kepler only. 

You need some plug-ins from the [KIELER](http://www.informatik.uni-kiel.de/rtsys/kieler/) project. Use 
the `kielerProjectSet.psf` to get them. Unfortunately, KIELER does not 
run without Equinox. If you want to run the standalone demo you have to 
apply the patch kieler.patch for some quick workarounds.

[Graphviz](http://www.graphviz.org/) must be installed on your system.

`de.fxdiagram.eclipse` contains the integration as an Eclipse view (needs   
https://github.com/JanKoehnlein/JavaFX-SWT-Gesture-Bridge) If you don't 
need that just close the project.

The `*.xtext.*` projects require the respective Xtext examples in the 
workspace. Use *New > Example > Xtext > ...* to instantiate them.

## RUN
* Run the launch config in `de.fxdiagram.examples` (standalone demo)
* Run the launch config in `de.fxdiagram.eclipse` (Eclipse integration)
