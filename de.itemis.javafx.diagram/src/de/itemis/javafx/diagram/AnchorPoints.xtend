package de.itemis.javafx.diagram

import java.util.List
import javafx.beans.binding.ObjectBinding
import javafx.geometry.Point2D

import static extension de.itemis.javafx.diagram.Extensions.*

class AnchorPoints extends ObjectBinding<List<Point2D>> {
	
	XNode host

	new(XNode host) {
		bind(host.translateXProperty, host.translateYProperty, host.node.boundsInLocalProperty)
		this.host = host
	}

	override protected computeValue() {
		val bounds = host.node.localToRoot(host.node.boundsInLocal)
		val middleX = (bounds.maxX + bounds.minX) / 2
		val middleY = (bounds.maxY + bounds.minY) / 2
		newArrayList(
			new Point2D(bounds.minX, middleY),
			new Point2D(bounds.maxX, middleY),
			new Point2D(middleX, bounds.minY),
			new Point2D(middleX, bounds.maxY))
	}
}

