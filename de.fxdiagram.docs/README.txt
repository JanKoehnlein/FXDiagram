DISCLAIMER
This is a prototype for demonstration purposes only. 
Use at your own risk. 
It is neither a complete solution nor built for reuse as a framework.
I cannot give any support on this.
I tested on MacOSX only. 
The LCARS demo won't run as it depends on a currently unreleased database. 


PREREQUISITES
- Java 7 SDK (including JavaFX)
- Eclipse Kepler 4.3.1
	- Bleeding edge Xtend
	- Bleeding edge e(fx)clipse

You need some plug-ins from the KIELER project. Use 
the kielerProjectSet.psf to get them. Unfortunately, KIELER does not 
run without Equinox. You have to apply the patch kieler.patch for some
quick workarounds.

'de.fxdiagram.eclipse' contains the integration as an Eclipse view (needs   
https://github.com/JanKoehnlein/JavaFX-SWT-Gesture-Bridge) If you don't 
need that just close the project.


RUN
- Run the launch config in de.fxdiagram.examples