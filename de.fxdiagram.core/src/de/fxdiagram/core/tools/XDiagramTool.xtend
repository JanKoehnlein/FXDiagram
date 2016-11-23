package de.fxdiagram.core.tools

import de.fxdiagram.core.XRoot

/** 
 * Encapsulates the current master behavior of the {@link XRoot}.
 *
 * Usually, only one {@link XDiagramTool} is active at the time, though tools 
 * can be composed using {@link CompositeTool}. So this is used to diasable some
 * behavior under certain circumstances.
 */
interface XDiagramTool {
	
	def boolean activate()
	
	def boolean deactivate()
	
}