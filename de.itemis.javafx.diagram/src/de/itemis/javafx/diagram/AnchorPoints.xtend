package de.itemis.javafx.diagram

import java.util.List
import javafx.beans.binding.ObjectBinding
import javafx.geometry.Point2D
import static extension de.itemis.javafx.diagram.Extensions.*
import javafx.scene.Node

class AnchorPoints extends ObjectBinding<List<Point2D>> {
	
	XNode host

	new(XNode host) {
		this.host = host  
		var dependencies = newArrayList
		var Node current = host
		do {
			dependencies.add(current.boundsInLocalProperty)
			dependencies.add(current.layoutXProperty)
			dependencies.add(current.layoutYProperty)
			dependencies.add(current.scaleXProperty)
			dependencies.add(current.scaleYProperty)
			current = current.parent
		} while (current != null)
		bind(dependencies)
	}

	override protected computeValue() {
		val bounds = host?.node?.boundsInLocal
		if (bounds != null) {
			val middleX = (bounds.maxX + bounds.minX) / 2
			val middleY = (bounds.maxY + bounds.minY) / 2
			#[
				host.node.localToRoot(bounds.minX, middleY),
				host.node.localToRoot(bounds.maxX, middleY),
				host.node.localToRoot(middleX, bounds.minY),
				host.node.localToRoot(middleX, bounds.maxY)
			]
		}
	}
}

