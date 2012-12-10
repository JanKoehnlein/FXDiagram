package de.itemis.javafx.diagram

import javafx.scene.input.MouseEvent
import javafx.scene.Node

class GraphUtil {
	def static getTargetShape(MouseEvent event) {
		if(event.target instanceof Node) 
			getContainerShape(event.target as Node)
		else
			null
	}
	
	def static ShapeContainer getContainerShape(Node it) {
		switch it {
			case null: 
				null
			ShapeContainer:
				it
			default: getContainerShape(parent)
		}
	}
}