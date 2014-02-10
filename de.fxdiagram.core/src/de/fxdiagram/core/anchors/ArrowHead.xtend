package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XConnection
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.transform.Affine

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*

import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import static extension de.fxdiagram.core.extensions.TransformExtensions.*
import static extension java.lang.Math.*

abstract class ArrowHead extends Parent {
	
	Node node
	
	@FxProperty XConnection connection
	
	@FxProperty boolean isSource
	
	def void initialize() {
		children += node
		this.isSource = isSource
		if(isSource)
			connection.sourceArrowHead = this
		else 
			connection.targetArrowHead = this
	}
	
	protected def setNode(Node node) {
		this.node = node
	} 
	
	def getNode() {
		node
	}

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

