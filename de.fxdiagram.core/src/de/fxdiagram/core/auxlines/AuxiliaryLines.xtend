package de.fxdiagram.core.auxlines

import de.fxdiagram.core.XNode
import javafx.geometry.Bounds
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Line

abstract class AuxiliaryLine {
	
	double position
	
	XNode[] relatedNodes

	new(double position, XNode[] relatedNodes) {
		this.position = position
		this.relatedNodes = relatedNodes
	}

	def double getPosition() {
		position
	}
	
	def XNode[] getRelatedNodes() {
		relatedNodes
	}
	
	def Node createNode() 
}

class NodeLine extends AuxiliaryLine {
	
	val overlap = 5
	
	Orientation orientation
	double min
	double max
	
	new(double position, Orientation orientation, XNode node, Bounds boundsInDiagram) {
		super(position, #[node])
		this.orientation = orientation
		switch orientation {
			case Orientation.HORIZONTAL: {
				min = boundsInDiagram.minX
				max = boundsInDiagram.maxX
			}
			case Orientation.VERTICAL: {
				min = boundsInDiagram.minY
				max = boundsInDiagram.maxY
			}
		}
	}
	
	def getOrientation() {
		orientation
	}
	
	override createNode() {
		switch orientation {
			case Orientation.HORIZONTAL:
				new Line => [
					startX = min - overlap
					startY = getPosition
					endX = max + overlap
					endY = getPosition
				]						
			case Orientation.VERTICAL:
				new Line => [
					startX = getPosition
					startY = min - overlap
					endX = getPosition
					endY = max + overlap
				]						
		} => [
			stroke = Color.RED
			strokeWidth = 2
		]
	}
}