package de.fxdiagram.core.anchors

import de.fxdiagram.annotations.properties.FxProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node

import static extension de.fxdiagram.core.Extensions.*
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.XActivatable
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRootDiagram
import static de.fxdiagram.core.XConnectionKind.*
import static de.fxdiagram.core.XControlPointType.*

class ConnectionRouter implements XActivatable {
	
	@FxProperty@ReadOnly ObservableList<XControlPoint> controlPoints = FXCollections.observableArrayList
	@FxProperty@ReadOnly boolean isActive
		
	XConnection connection

	ChangeListener<Number> scalarListener  
	ChangeListener<Bounds> boundsListener
	
	new(XConnection connection) {
		this.connection = connection
		scalarListener = [ prop, oldVal, newVal | connection.requestLayout ]
		boundsListener = [ prop, oldVal, newVal | connection.requestLayout ]
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
		do {
			current.boundsInLocalProperty.addListener(boundsListener)
			current.layoutXProperty.addListener(scalarListener)
			current.layoutYProperty.addListener(scalarListener)
			current.scaleXProperty.addListener(scalarListener)
			current.scaleYProperty.addListener(scalarListener)
			current.rotateProperty.addListener(scalarListener)
			current = current.parent
		} while (current != null && !(current instanceof XRootDiagram))
	}
	
	def growToSize(int newSize) {
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
			return
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
	
	def calculatePoints() {
		val anchors = findClosestAnchors
		val sourcePoint = anchors.key
		val targetPoint = anchors.value
		if (controlPoints.size < 2) {
			for(controlPoint: controlPoints) {
				controlPoint.layoutXProperty.removeListener(scalarListener)
				controlPoint.layoutYProperty.removeListener(scalarListener)
			} 
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
			controlPoints.forEach[update(controlPoints)]
		}
	}
	
	protected def findClosestAnchors() {
		if (controlPoints.size <= 2) {
			getNearestAnchor(connection.source, connection.target.midPoint) 
			-> getNearestAnchor(connection.target, connection.source.midPoint) 
		} else {
			getNearestAnchor(connection.source, controlPoints.get(1)) 
			-> getNearestAnchor(connection.target, controlPoints.get(controlPoints.size - 2))
		}
	}
	
	protected def midPoint(XNode node) {
		node.localToRootDiagram(
			0.5 * (node.boundsInLocal.minX + node.boundsInLocal.maxX),
			0.5 * (node.boundsInLocal.minY + node.boundsInLocal.maxY)
		)
	}

	protected def getNearestAnchor(XNode node, XControlPoint controlPoint) {
		node.anchors.getAnchor(controlPoint.layoutX, controlPoint.layoutY)
	}
	
	protected def getNearestAnchor(XNode node, Point2D point) {
		node.anchors.getAnchor(point.x, point.y)
	}
	
}