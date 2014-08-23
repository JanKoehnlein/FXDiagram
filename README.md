FXDiagram
=========

A next generation diagram editing framework based on JavaFX. Strong focus
on usability aspects.


DISCLAIMER
Use at your own risk. I cannot give any support on this.
I tested on MacOSX only. 
The LCARS demo won't run as it depends on a currently unreleased database. 


PREREQUISITES
- Java 7 SDK (including JavaFX)
- Eclipse Kepler 4.3
	- Xtext SDK 2.7 (including Xtend 2.7)
	- e(fx)clipse 0.9 
	
Unfortunately, it currently doesn't run with JavaFX 8 (Java 8 SDK). As 
e(fx)clipse decideded to no longer support Java 7, you need the old 0.9 
version. The latter runs on Kepler only. This is why the Java 8 issues
have top priority to be solved.

You need some plug-ins from the KIELER project. Use 
the kielerProjectSet.psf to get them. Unfortunately, KIELER does not 
run without Equinox. If you want to run the standalone demo you have to 
apply the patch kieler.patch for some quick workarounds.

'de.fxdiagram.eclipse' contains the integration as an Eclipse view (needs   
https://github.com/JanKoehnlein/JavaFX-SWT-Gesture-Bridge) If you don't 
need that just close the project.

The *.xtext.* projects require the respective Xtext examples in the 
workspace. Use New > Example > Xtext > ... to instantiate them.


RUN
- Run the launch config in de.fxdiagram.examples (standalone demo)
- Run the launch config in de.fxdiagram.eclipse (Eclipse integration)
