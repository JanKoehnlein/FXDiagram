package de.fxdiagram.core

import de.fxdiagram.core.XDiagram
import javafx.geometry.Insets

/**
 * Interface for {@XNode}s that contain diagrams.
 */
interface XDiagramContainer {

	def XDiagram getInnerDiagram()
	
	def boolean isInnerDiagramActive()
	
	def Insets getInsets()
}