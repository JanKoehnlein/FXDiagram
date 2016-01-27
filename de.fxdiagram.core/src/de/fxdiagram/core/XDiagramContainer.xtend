package de.fxdiagram.core

import de.fxdiagram.core.XDiagram
import javafx.geometry.Insets
import javafx.beans.property.ObjectProperty

/**
 * Interface for {@XNode}s that contain diagrams.
 */
interface XDiagramContainer {

	def XDiagram getInnerDiagram()
	
	def void setInnerDiagram(XDiagram diagram)
	
	def ObjectProperty<XDiagram> innerDiagramProperty()
	
	def boolean isInnerDiagramActive()
	
	def Insets getInsets()
}