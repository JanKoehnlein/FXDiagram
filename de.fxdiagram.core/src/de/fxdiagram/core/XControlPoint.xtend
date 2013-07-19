package de.fxdiagram.core

import de.fxdiagram.core.behavior.MoveBehavior
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class XControlPoint extends XShape {
	
	MoveBehavior moveBehavior
	
	new() {
		node = new Circle => [
			radius = 5
			stroke = Color.RED
			fill = Color.WHITE
		]
	}
		
	override protected doActivate() {
		selectedProperty.addListener [
			prop, oldVal, newVal |
			if(newVal)
				circle.fill = Color.RED
			else 
				circle.fill = Color.WHITE
		]
		moveBehavior?.activate
	}
	
	def setMovable(boolean isMovable) {
		if(isMovable) {
			if(moveBehavior == null) 
				moveBehavior = new MoveBehavior(this)
			if(isActive) 
				moveBehavior.activate
		} else if(moveBehavior != null) { 
			moveBehavior = null
		}
	}
	
	override getMoveBehavior() {
		moveBehavior
	}
	
	def getCircle() {
		node as Circle
	}
	
	override toString() {
		'''XControlPoint at («layoutX»,«layoutY»)'''
	}
}