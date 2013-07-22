package de.fxdiagram.core

import de.fxdiagram.core.behavior.MoveBehavior
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

import static de.fxdiagram.core.XControlPointType.*

class XControlPoint extends XShape {
	
	MoveBehavior moveBehavior
	
	XControlPointType type
	
	new() {
		setType(CONTROL_POINT)
	}
	
	def getType() {
		type
	}

	def setType(XControlPointType type) {
		this.type = type
		switch type {
			case ANCHOR: {
				node = new Circle => [
					radius = 3
					stroke = Color.BLUE
					fill = Color.WHITE
				]
			}
			case CONTROL_POINT: {
				node = new Circle => [
					radius = 5
					stroke = Color.RED
					fill = Color.WHITE
				]				
			}
			case INTERPOLATED: {
				node = new Circle => [
					radius = 5
					stroke = Color.RED
					fill = Color.WHITE
				]
			}
		}
		if(type != ANCHOR) 
			moveBehavior = new MoveBehavior(this)
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
	
	override isSelectable() {
		type != XControlPointType.ANCHOR && super.isSelectable()
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

enum XControlPointType {
	ANCHOR, INTERPOLATED, CONTROL_POINT
}