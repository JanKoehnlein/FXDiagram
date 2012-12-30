package de.itemis.javafx.diagram

import javafx.beans.value.ChangeListener
import javafx.geometry.Point2D
import javafx.scene.shape.Polyline
import org.eclipse.xtext.xbase.lib.Pair

class XConnection extends Polyline implements XActivatable {
	
	@Property XNode source

	@Property XNode target
	
	boolean isActive
	
	new(XNode source, XNode target) {
		this.source = source
		this.target = target
	}
	
	def protected calculatePoints() {
		var shortestDistance = Double::POSITIVE_INFINITY
		var Pair<Point2D, Point2D> nearestAnchors = 
			source.anchorPoints.get.head -> target.anchorPoints.get.head 
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
		val ChangeListener changeListener = [
			element, oldValue, newValue | 
			calculatePoints()
		]  
		source.anchorPoints.addListener(changeListener)
		target.anchorPoints.addListener(changeListener)
		calculatePoints
	}
}
