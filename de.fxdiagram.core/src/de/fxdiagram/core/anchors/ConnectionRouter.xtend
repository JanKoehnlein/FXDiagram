package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XActivatable
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XNode
import javafx.beans.value.ChangeListener
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.XControlPoint.Type.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

@Logging
class ConnectionRouter implements XActivatable {
	
	@FxProperty(readOnly=true) boolean isActive
		
	XConnection connection

	ChangeListener<Number> scalarListener  
	ChangeListener<Bounds> boundsListener
	
	double selfEdgeDist = 60
	
	new(XConnection connection) {
		this.connection = connection
		scalarListener = [ prop, oldVal, newVal | 
			connection.requestLayout
		]
		boundsListener = [ prop, oldVal, newVal |
			connection.requestLayout
		]
	}
	
	def getControlPoints() {
		connection.controlPoints
	}
	
	override activate() {
		if(!isActive) {
			bindNode(connection.source)
			bindNode(connection.target)
		}
		isActiveProperty.set(true)
	}
	
	protected def bindNode(XNode host) {
		var Node current = host.node
		current.layoutBoundsProperty.addListener(boundsListener)
		while(current != null && !current.isRootDiagram) {
			current.boundsInParentProperty.addListener(boundsListener)
			current = current.parent
		} 
	}
	
	def growToSize(int newSize) {
		if (controlPoints.size < 2) 
			calculatePoints
		val nodeDiff = newSize - controlPoints.size
		if(nodeDiff > 0) {
			val newControlPoints = newArrayList
			val delta = 1.0 / (nodeDiff + 1)
			val first = controlPoints.head
			val last = controlPoints.last
			for(i: 1..nodeDiff) {
				val newControlPoint = new XControlPoint => [
					val lambda = delta * i
					layoutX = (1-lambda) * first.layoutX + (lambda) * last.layoutX 
					layoutY = (1-lambda) * first.layoutY + (lambda) * last.layoutY
				]				
				newControlPoint.layoutXProperty.addListener(scalarListener)
				newControlPoint.layoutYProperty.addListener(scalarListener)
				newControlPoints += newControlPoint
			} 
			controlPoints.addAll(controlPoints.size-1, newControlPoints)
			resetPointTypes
		}
	}
	
	def shrinkToSize(int newSize) {
		if (controlPoints.size < 2) 
			calculatePoints
		val nodeDiff = newSize - controlPoints.size
		if(nodeDiff < 0) {
			val toBeRemoved = newArrayList
			for(i: controlPoints.size-1>..controlPoints.size + nodeDiff-1) {
				val removeMe = controlPoints.get(i)
				removeMe.layoutXProperty.removeListener(scalarListener)
				removeMe.layoutYProperty.removeListener(scalarListener)				
				toBeRemoved.add(removeMe)
			}
			controlPoints.removeAll(toBeRemoved)
			resetPointTypes
		}
	}
	
	protected def resetPointTypes() {
		if(controlPoints.size < 2) 
			return;
		controlPoints.head.type = ANCHOR
		controlPoints.last.type = ANCHOR
		for(i: 1..<controlPoints.size - 1) {
			val currentPoint = controlPoints.get(i)
			currentPoint.type = switch connection.kind {
				case POLYLINE:
					INTERPOLATED
				case QUAD_CURVE:
					if(i % 2 == 0)
						INTERPOLATED
					else 
						CONTROL_POINT
				case CUBIC_CURVE:
					if(i % 3 == 0)
						INTERPOLATED
					else 
						CONTROL_POINT
			}
		}
	}
	
	def void calculatePoints() {
		if(controlPoints.size < 2) {
			for(controlPoint: controlPoints) {
				controlPoint.layoutXProperty.removeListener(scalarListener)
				controlPoint.layoutYProperty.removeListener(scalarListener)
			} 
			if(connection.source == connection.target) {
				calculateSelfEdge()
			}
		} 
		val anchors = findClosestAnchors
		if(anchors.key != null && anchors.value != null) {
			val sourcePoint = anchors.key
			val targetPoint = anchors.value
			if (controlPoints.size < 2) {
				controlPoints.setAll(
					#[new XControlPoint => [
						layoutX = sourcePoint.x
						layoutY = sourcePoint.y
						type = ANCHOR
					], new XControlPoint => [
						layoutX = targetPoint.x
						layoutY = targetPoint.y
						type = ANCHOR
					]])
				switch connection.kind {
					case CUBIC_CURVE: growToSize(4)
					case QUAD_CURVE: growToSize(3)
				}
				for(controlPoint: controlPoints) {
					controlPoint.layoutXProperty.addListener(scalarListener)
					controlPoint.layoutYProperty.addListener(scalarListener)
				} 
			} else {
				controlPoints.head => [
					layoutX = sourcePoint.x
					layoutY = sourcePoint.y
				]
				controlPoints.last => [
					layoutX = targetPoint.x
					layoutY = targetPoint.y
				]
			}
			controlPoints.forEach[update(controlPoints)]
		}
	}
	
	protected def calculateSelfEdge() {
		val boundsInDiagram = connection.source.localToRootDiagram(connection.source.snapBounds)
		controlPoints.clear
		controlPoints += new XControlPoint => [
			type = ANCHOR
		]
		if(connection.kind == QUAD_CURVE) {
			LOG.severe("self-edges cannot be QUAD_CURVEs. Switching to CUBIC_CURVE")
			connection.kind = CUBIC_CURVE			
		}
		switch connection.kind {
			case POLYLINE: {
				val deltaX = min(selfEdgeDist, 0.5 * boundsInDiagram.width)
				val deltaY = min(selfEdgeDist, 0.5 * boundsInDiagram.height)
				controlPoints += new XControlPoint => [
					layoutX = boundsInDiagram.minX - deltaX
					layoutY = boundsInDiagram.minY + deltaY
					type = CONTROL_POINT
				]	
				controlPoints += new XControlPoint => [
					layoutX = boundsInDiagram.minX - deltaX
					layoutY = boundsInDiagram.minY - deltaY
					type = CONTROL_POINT
				]	
				controlPoints += new XControlPoint => [
					layoutX = boundsInDiagram.minX + deltaX
					layoutY = boundsInDiagram.minY - deltaY
					type = CONTROL_POINT
				]	
			}
			case CUBIC_CURVE: {
				controlPoints += new XControlPoint => [
					layoutX = boundsInDiagram.minX - selfEdgeDist
					layoutY = boundsInDiagram.minY + 0.3 * selfEdgeDist
					type = CONTROL_POINT
				]	
				controlPoints += new XControlPoint => [
					layoutX = boundsInDiagram.minX + 0.3 * selfEdgeDist
					layoutY = boundsInDiagram.minY - selfEdgeDist
					type = CONTROL_POINT
				]	
			}
		}
		controlPoints += new XControlPoint => [
			type = ANCHOR
		]
	}	
	
 	protected def findClosestAnchors() {
		if (controlPoints.size <= 2) {
			getNearestAnchor(connection.source, connection.target.midPoint, connection.sourceArrowHead) 
			-> getNearestAnchor(connection.target, connection.source.midPoint, connection.targetArrowHead) 
		} else {
			getNearestAnchor(connection.source, controlPoints.get(1), connection.sourceArrowHead) 
			-> getNearestAnchor(connection.target, controlPoints.get(controlPoints.size - 2), connection.targetArrowHead)
		}
	}
	
 	protected def midPoint(XNode node) {
		node.localToRootDiagram(node.node.boundsInLocal.center)
	}

	protected def getNearestAnchor(XNode node, XControlPoint controlPoint, ArrowHead arrowHead) {
		getNearestAnchor(node, controlPoint.layoutX, controlPoint.layoutY, arrowHead)
	}
	
	protected def getNearestAnchor(XNode node, Point2D point, ArrowHead arrowHead) {
		if(point == null)
			return null
		getNearestAnchor(node, point.x, point.y, arrowHead)
	}
	
	protected def getNearestAnchor(XNode node, double x, double y, ArrowHead arrowHead) {
		val anchor = node.anchors.getAnchor(x, y)
		if(anchor != null && arrowHead != null)
			arrowHead.correctAnchor(x, y, anchor)
		else 
			anchor
	}
}
