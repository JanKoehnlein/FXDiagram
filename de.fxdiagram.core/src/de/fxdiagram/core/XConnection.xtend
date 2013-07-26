package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.Lazy
import de.fxdiagram.core.anchors.ConnectionRouter
import java.util.List
import javafx.beans.value.ChangeListener
import javafx.geometry.BoundingBox
import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.Polyline
import javafx.scene.shape.QuadCurve
import javafx.scene.shape.Shape

import static de.fxdiagram.core.XConnectionKind.*

import static extension de.fxdiagram.core.Extensions.*

@Logging
class XConnection extends XShape {
	
	@FxProperty XNode source
	@FxProperty XNode target
	@FxProperty @Lazy XConnectionLabel label
	@FxProperty XConnectionKind kind = POLYLINE
	@FxProperty double strokeWidth = 2.0

	Group controlPointGroup = new Group
	Group shapeGroup = new Group

	ChangeListener<Number> controlPointListener
	
	ConnectionRouter connectionRouter

	new(XNode source, XNode target) {
		node = shapeGroup 
		children += controlPointGroup => [
			visible = false
		]
		this.source = source
		this.target = target
		connectionRouter = new ConnectionRouter(this)		
	}

	override doActivate() {
		controlPointListener = [ prop, oldVal, newVal |
			updateShapes
		]
		controlPoints.addListener [
			val points = list
			updateShapes
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
		]
		connectionRouter.activate
		updateShapes
	}
	
	def getConnectionRouter() {
		connectionRouter
	}
	
	def getControlPoints() {
		connectionRouter.controlPoints
	}

	def void updateShapes() {
		switch kind {
			case CUBIC_CURVE: {
				if((controlPoints.size - 1) % 3 != 0) {
					kind = POLYLINE
					LOG.warning("Cannot create cubic curve for " + controlPoints.size + " control points. Switching to polyline")
					updateShapes
				}
				val numSegments = (controlPoints.size - 1) / 3
				val curves = shapeGroup.children.filter(CubicCurve).toList
				while(curves.size > numSegments) 
					curves.remove(curves.last)
				while(curves.size < numSegments)
					curves += new CubicCurve => [
						fill = null
						stroke = Color.BLACK
					]
				for(i: 0..<numSegments) {
					val curve = curves.get(i)
					val offset = i * 3
					curve.startX = controlPoints.get(offset).layoutX
					curve.startY = controlPoints.get(offset).layoutY
					curve.controlX1 = controlPoints.get(offset+1).layoutX
					curve.controlY1 = controlPoints.get(offset+1).layoutY
					curve.controlX2 = controlPoints.get(offset+2).layoutX
					curve.controlY2 = controlPoints.get(offset+2).layoutY
					curve.endX = controlPoints.get(offset+3).layoutX
					curve.endY = controlPoints.get(offset+3).layoutY
				}
				shapes = curves
				
			}
			case QUAD_CURVE: {
				if((controlPoints.size - 1) % 2 != 0) {
					kind = POLYLINE
					LOG.warning("Cannot create quadratic curve for " + controlPoints.size + " control points. Switching to polyline")
					updateShapes
				}
				val numSegments = (controlPoints.size - 1) / 2
				val curves = shapeGroup.children.filter(QuadCurve).toList
				while(curves.size > numSegments) 
					curves.remove(curves.last)
				while(curves.size < numSegments)
					curves += new QuadCurve => [
						fill = null
						stroke = Color.BLACK
					]
				for(i: 0..<numSegments) {
					val curve = curves.get(i)
					val offset = i * 2
					curve.startX = controlPoints.get(offset).layoutX
					curve.startY = controlPoints.get(offset).layoutY
					curve.controlX = controlPoints.get(offset+1).layoutX
					curve.controlY = controlPoints.get(offset+1).layoutY
					curve.endX = controlPoints.get(offset+2).layoutX
					curve.endY = controlPoints.get(offset+2).layoutY
				}
				shapes = curves
			}
			case POLYLINE: {
				val polyline = shapeGroup.children.filter(Polyline).head 
					?: new Polyline => [
						stroke = Color.BLACK
					]
				polyline.points.setAll(controlPoints.map[#[layoutX, layoutY]].flatten)
				shapes = #[polyline]
			}
		}
		controlPointGroup.children.setAll(connectionRouter.controlPoints)
	}
	
	def protected setShapes(List<? extends Shape> shapes) {
		shapeGroup.children.setAll(shapes)
		val strokeBoundsInRoot = source.localToRootDiagram(new BoundingBox(0, 0, this.strokeWidth, this.strokeWidth))
		val strokeInRoot = 0.5 * (strokeBoundsInRoot.width + strokeBoundsInRoot.height) 
		shapes.forEach[
			strokeWidth = strokeInRoot
		]
	}
	
	override isSelectable() {
		isActive
	}

	override getMoveBehavior() {
		null
	}
	
	override layoutChildren() {
		super.layoutChildren
		connectionRouter.calculatePoints
		label?.place(controlPoints)	
	}
}

enum XConnectionKind {
	POLYLINE, QUAD_CURVE, CUBIC_CURVE 
}
