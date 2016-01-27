package de.fxdiagram.core.auxlines

import de.fxdiagram.core.XShape
import javafx.geometry.Bounds
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.shape.Line

abstract class AuxiliaryLine {
	
	double position
	
	XShape[] relatedShapes

	new(double position, XShape[] relatedShapes) {
		this.position = position
		this.relatedShapes = relatedShapes
	}

	def double getPosition() {
		position
	}
	
	def XShape[] getRelatedShapes() {
		relatedShapes
	}
	
	def Node createNode() 
}

class NodeLine extends AuxiliaryLine {
	
	val overlap = 5
	
	Orientation orientation
	double min
	double max
	
	new(double position, Orientation orientation, XShape shape, Bounds boundsInDiagram) {
		super(position, #[shape])
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
