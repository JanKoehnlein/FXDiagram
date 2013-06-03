package de.itemis.javafx.diagram

import javafx.scene.shape.Polyline

import static extension de.itemis.javafx.diagram.Extensions.*
import static extension de.itemis.javafx.diagram.binding.DoubleExpressionExtensions.*

class XConnection extends Polyline implements XActivatable {
	
	@Property XNode source
	@Property XNode target
	@Property XConnectionLabel label
	
	boolean isActive
	
	new(XNode source, XNode target) {
		this.source = source
		this.target = target
	}
	
	def protected calculatePoints() {
		var shortestDistance = Double.POSITIVE_INFINITY
		var nearestAnchors = source.anchorPoints.get.head -> target.anchorPoints.get.head 
		for(sourceAnchor: source.anchorPoints.get) {
			for(targetAnchor: target.anchorPoints.get) {
				val currentDistance = sourceAnchor.distance(targetAnchor)
				if(currentDistance < shortestDistance) {
					shortestDistance = currentDistance
					nearestAnchors = sourceAnchor -> targetAnchor
				}
			}
		}
		points.setAll(nearestAnchors.key.x, nearestAnchors.key.y,
			nearestAnchors.value.x, nearestAnchors.value.y)
	}

	override activate() {
		if(!isActive)
			doActivate
		isActive = true
	}
	
	def doActivate() {
		strokeWidthProperty.bind(1.5 / this.rootDiagram.scaleProperty)
		source.anchorPoints.addListener [
			element, oldValue, newValue | 
			calculatePoints()
		]
		target.anchorPoints.addListener[
			element, oldValue, newValue | 
			calculatePoints()
		]
		calculatePoints
		if(label != null)
			label.activate
	}
}
