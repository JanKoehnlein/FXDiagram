package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.Lazy
import javafx.beans.value.ChangeListener
import javafx.scene.Group
import javafx.scene.shape.Polyline
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.QuadCurve
import javafx.scene.paint.Color
import javafx.scene.shape.Shape

class XConnection extends XShape {

	@FxProperty XNode source
	@FxProperty XNode target
	@FxProperty @Lazy XConnectionLabel label
	@FxProperty double strokeWidth = 1.5

	Group controlPointGroup = new Group
	Group shapeGroup = new Group

	ChangeListener<Number> controlPointListener
	
	ConnectionRouter connectionRouter

	Polyline polyline = new Polyline
	QuadCurve quadCurve
	CubicCurve cubicCurve

	new(XNode source, XNode target) {
		node = shapeGroup 
		children += controlPointGroup => [
			visible = false
		]
		this.source = source
		this.target = target
		controlPointListener = [ prop, oldVal, newVal |
			updatePoints
		]
		connectionRouter = new ConnectionRouter(this)		
	}

	override doActivate() {
		shape = polyline
		controlPoints.addListener [
			val points = list
			updatePoints
			while(next) 
				addedSubList.forEach[
					val index = points.indexOf(it)					
					if(index != 0 && index != points.size)
						activate
					layoutXProperty.addListener(controlPointListener)
					layoutYProperty.addListener(controlPointListener)
				]
				removed.forEach [
					layoutXProperty.removeListener(controlPointListener)
					layoutYProperty.removeListener(controlPointListener)
				]
		]
		if(label != null)
			label.activate
		selectedProperty.addListener [
			prop, oldVal, newVal |
			controlPointGroup.visible = newVal
			for(i: 1..<(controlPoints.size - 1)) {
				controlPoints.get(i).selected = newVal
			}		
		]
		connectionRouter.activate
	}
	
	def getConnectionRouter() {
		connectionRouter
	}
	
	def getControlPoints() {
		connectionRouter.controlPoints
	}

	def updatePoints() {
		switch controlPoints.size {
			case 3: {
				if(quadCurve == null) 
					quadCurve = new QuadCurve
				quadCurve.startX = controlPoints.get(0).layoutX
				quadCurve.startY = controlPoints.get(0).layoutY
				quadCurve.controlX = controlPoints.get(1).layoutX
				quadCurve.controlY = controlPoints.get(1).layoutY
				quadCurve.endX = controlPoints.get(2).layoutX
				quadCurve.endY = controlPoints.get(2).layoutY
				shape = quadCurve
			}
			case 4: {
				if(cubicCurve == null) 
					cubicCurve = new CubicCurve
				cubicCurve.startX = controlPoints.get(0).layoutX
				cubicCurve.startY = controlPoints.get(0).layoutY
				cubicCurve.controlX1 = controlPoints.get(1).layoutX
				cubicCurve.controlY1 = controlPoints.get(1).layoutY
				cubicCurve.controlX2 = controlPoints.get(2).layoutX
				cubicCurve.controlY2 = controlPoints.get(2).layoutY
				cubicCurve.endX = controlPoints.get(3).layoutX
				cubicCurve.endY = controlPoints.get(3).layoutY
				shape = cubicCurve
			}
			default: {
				polyline.points.setAll(controlPoints.map[#[layoutX, layoutY]].flatten)
				shapeGroup.children.setAll(polyline)
			}
		}
		controlPointGroup.children.setAll(connectionRouter.controlPoints)
	}
	
	def protected setShape(Shape shape) {
		shapeGroup.children.setAll(shape)
		shape.strokeWidthProperty.bind(this.strokeWidthProperty)
		shape => [
			fill = null
			stroke = Color.BLACK
		]
	}
	
	override isSelectable() {
		isActive
	}

	def getPolyline() {
		polyline 		
	}
	
	override getMoveBehavior() {
		null
	}
}
