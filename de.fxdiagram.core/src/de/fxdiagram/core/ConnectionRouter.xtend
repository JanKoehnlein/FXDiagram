package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import javafx.beans.value.ChangeListener
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.scene.Node

import static extension de.fxdiagram.core.Extensions.*
import de.fxdiagram.annotations.properties.ReadOnly

class ConnectionRouter implements XActivatable {
	
	@FxProperty ObservableList<XControlPoint> controlPoints = FXCollections.observableArrayList
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

	def protected calculatePoints() {
		val anchors = findClosestAnchors
		val sourcePoint = anchors.key
		val targetPoint = anchors.value
		if (controlPoints.size < 2) {
			controlPoints.setAll(
				#[new XControlPoint => [
					layoutX = sourcePoint.x
					layoutY = sourcePoint.y
					movable = false
				], new XControlPoint => [
					layoutX = targetPoint.x
					layoutY = targetPoint.y
					movable = false
				]])
		} else {
			controlPoints.head => [
				layoutX = sourcePoint.x
				layoutY = sourcePoint.y
				movable = false
			]
			controlPoints.last => [
				layoutX = targetPoint.x
				layoutY = targetPoint.y
				movable = false
			]
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