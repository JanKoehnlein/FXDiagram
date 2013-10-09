package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.anchors.ArrowHead
import de.fxdiagram.core.anchors.ConnectionRouter
import de.fxdiagram.core.anchors.TriangleArrowHead
import java.util.List
import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener.Change
import javafx.geometry.BoundingBox
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.Polyline
import javafx.scene.shape.QuadCurve
import javafx.scene.shape.Shape
import javafx.scene.shape.StrokeLineCap

import static de.fxdiagram.core.XConnectionKind.*
import static de.fxdiagram.core.extensions.Point2DExtensions.*

import static extension de.fxdiagram.core.extensions.BezierExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

@Logging
class XConnection extends XShape {
	
	@FxProperty XNode source
	@FxProperty XNode target
	@FxProperty XConnectionLabel label
	@FxProperty ArrowHead sourceArrowHead
	@FxProperty ArrowHead targetArrowHead
	@FxProperty XConnectionKind kind = POLYLINE
	@FxProperty double strokeWidth = 2.0
	@FxProperty Paint stroke

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
		if(!source.outgoingConnections.contains(this))
			source.outgoingConnections.add(this)
		if(!target.incomingConnections.contains(this))
			target.incomingConnections.add(this)
		connectionRouter = new ConnectionRouter(this)
		targetArrowHead = new TriangleArrowHead(this, false)	
	}

	override doActivate() {
		if(stroke == null) 
			stroke = diagram.connectionPaint
		controlPointListener = [ prop, oldVal, newVal |
			updateShapes
		]
		// Xtend bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=410990
		val listChangeListener = [ 
			Change<? extends XControlPoint> it | 
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
		controlPoints.addListener(listChangeListener)
		if(label != null)
			label.activate
		connectionRouter.activate
		updateShapes
		parentProperty.addListener [
			property, oldValue, newValue |
			if(newValue == null) {
				source.outgoingConnections.remove(this)
				target.incomingConnections.remove(this)
			}
		]
	}
	
	override selectionFeedback(boolean isSelected) {
		if(isSelected) {
			source.toFront
			target.toFront
		}
		controlPointGroup.visible = isSelected
	}
	
	def getConnectionRouter() {
		connectionRouter
	}
	
	def getControlPoints() {
		connectionRouter.controlPoints
	}

	def void updateShapes() {
		var remainder = -1
		switch kind {
			case CUBIC_CURVE: {
				remainder = (controlPoints.size - 1) % 3
				if(remainder == 0) {
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
			}
			case QUAD_CURVE: {
				remainder = (controlPoints.size - 1) % 2 
				if(remainder == 0) {
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
			}
		}
		if (remainder != 0) {
			val polyline = shapeGroup.children.filter(Polyline).head 
				?: new Polyline => [ 
					stroke = Color.BLACK
				]
			polyline.points.setAll(controlPoints.map[#[layoutX, layoutY]].flatten)
			shapes = #[polyline]
		}
		controlPointGroup.children.setAll(connectionRouter.controlPoints)
	}
	
	def protected setShapes(List<? extends Shape> shapes) {
		shapeGroup.children.setAll(shapes)
		val strokeBoundsInRoot = source.localToRootDiagram(new BoundingBox(0, 0, this.strokeWidth, this.strokeWidth))
		val strokeInRoot = 0.5 * (strokeBoundsInRoot.width + strokeBoundsInRoot.height) 
		shapes.forEach [
			stroke = this.stroke
			strokeLineCap = StrokeLineCap.ROUND
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
		sourceArrowHead?.place
		targetArrowHead?.place
	}
	
	def at(double t) {
		if(t < 0 || t > 1)
			throw new IllegalArgumentException("Argument must be between 0 and 1") 
		if(t==1) 
			return new Point2D(controlPoints.last.layoutX, controlPoints.last.layoutY)
		switch (kind) {
			case CUBIC_CURVE: {
				val curves = shapeGroup.children.filter(CubicCurve)
				val segment = t * curves.size
				val index = segment as int
				val curve = curves.get(index)
				curve.at(segment - index)
			}
			case QUAD_CURVE: {
				val curves = shapeGroup.children.filter(QuadCurve)
				val segment = t * curves.size
				val index = segment as int
				val curve = curves.get(index)
				curve.at(segment - index)
			}
			case POLYLINE: {
				val line = shapeGroup.children.filter(Polyline).head
				val numSegments = (line.points.size / 2 - 1)
				val segment = t * numSegments
				val index = segment as int
				linear(
					line.points.get(index), line.points.get(index + 1), 
					line.points.get(index + 2), line.points.get(index + 3), 
					segment - index)
			}
		}
	}
	
	def derivativeAt(double t) {
		if(t < 0 || t > 1)
			throw new IllegalArgumentException("Argument must be between 0 and 1")
		switch (kind) {
			case CUBIC_CURVE: {
				val curves = shapeGroup.children.filter(CubicCurve)
				if (t==1) 
					return curves.last.derivativeAt(1)					
				val segment = t * curves.size
				val index = segment as int
				val curve = curves.get(index)
				curve.derivativeAt(segment - index)
			}
			case QUAD_CURVE: {
				val curves = shapeGroup.children.filter(QuadCurve)
				if (t==1) 
					return curves.last.derivativeAt(1)					
				val segment = t * curves.size
				val index = segment as int
				val curve = curves.get(index)
				curve.derivativeAt(segment - index)
			}
			case POLYLINE: {
				val line = shapeGroup.children.filter(Polyline).head
				val numSegments = (line.points.size / 2 - 1)
				val segment = if(t == 1)
						numSegments - 0.5 / numSegments
					else
					 	t * numSegments
				val index = segment as int
				new Point2D(line.points.get(index + 2) - line.points.get(index), 
					line.points.get(index + 3) - line.points.get(index + 1))
			}
		}
	}
}

enum XConnectionKind {
	POLYLINE, QUAD_CURVE, CUBIC_CURVE 
}
