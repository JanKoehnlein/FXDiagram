package de.fxdiagram.core

import javafx.beans.property.ObjectProperty
import javafx.geometry.Insets

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