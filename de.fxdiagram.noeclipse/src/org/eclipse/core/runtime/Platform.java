package org.eclipse.core.runtime;

/**
 * Avoids linking errors in FXDiagram and KIELER when no OSGI is present.
 * 
 * @author koehnlein
 */
public class Platform {

	public static boolean isRunning() {
		return false;
	} 
}
