package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XConnection
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.paint.Paint
import javafx.scene.transform.Affine

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*

import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import static extension de.fxdiagram.core.extensions.TransformExtensions.*
import static extension java.lang.Math.*

/**
 * Arrow head for the start or end of an {@link XConnection}.
 * 
 * In order to avoid ugly overlapping with the connections curve, the curve ends 
 * {@link #getLineCut()} units before the anchor point, but will have the same 
 * tangent as calculated for the exact anchor point.
 * 
 * For the node's coordiante system consider that it is the target arrow head of 
 * a connection comming from (-infinity, 0) ending in (0,0).
 */
@ModelNode('connection', 'isSource', 'width', 'height', 'stroke')
@Logging
abstract class ArrowHead extends Parent {
	
	@FxProperty XConnection connection
	@FxProperty boolean isSource
	@FxProperty double width 
	@FxProperty double height 
	@FxProperty Paint stroke
	
	Node node

	new(XConnection connection, double width, double height, 
		Paint stroke, boolean isSource) {
		this.connection = connection
		this.isSource = isSource
		this.width = width
		this.height = height
		if(stroke != null)
			strokeProperty.set(stroke)
	}

	def initializeGraphics() {  
		if(getNode() == null)
			LOG.severe("Node is null")
	}
	
	def Node getNode() {
		if(stroke == null)
			strokeProperty.bind(connection.strokeProperty)
		if(node == null) {
			node = createNode()
			children += node
			this.isSource = isSource
			if(isSource)
				connection.sourceArrowHead = this
			else 
				connection.targetArrowHead = this
		}
		node
	}
	
	def Node createNode() 
	
	def getLineCut() {
		connection.strokeWidth
	} 
	
	def correctAnchor(double x, double y, Point2D anchorOnOutline) {
		if(anchorOnOutline != null && lineCut > 0) {
			val direction = new Point2D(anchorOnOutline.x - x, anchorOnOutline.y - y)
			if(direction.norm() > EPSILON) {
				val correction = direction * (lineCut / direction.norm)
				return anchorOnOutline - correction
			}
		} 
		anchorOnOutline
	}
	
	def void place() {
		val t = if(isSource) 0 else 1
		val trafo = new Affine
		val headBounds = boundsInLocal
		trafo.translate(- headBounds.width - headBounds.minX + lineCut,  - 0.5 * headBounds.height - headBounds.minY)
		val derivative = connection.derivativeAt(t)
		val angle = atan2(derivative.y, derivative.x).toDegrees 
			+ if(isSource) 180 else 0
		trafo.rotate(angle)
		val pos = connection.at(t)
		trafo.translate(pos.x, pos.y)
		transforms.setAll(trafo)
	}
}

