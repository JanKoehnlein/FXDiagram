package de.itemis.javafx.diagram

import java.util.List
import javafx.beans.binding.ObjectBinding
import javafx.geometry.Point2D
import static extension de.itemis.javafx.diagram.Extensions.*

class AnchorPoints extends ObjectBinding<List<Point2D>> {
	
	XNode host

	new(XNode host) {
		bind(host.layoutXProperty, host.layoutYProperty, host.node.boundsInLocalProperty, host.scaleXProperty, host.scaleYProperty)
		this.host = host  
	}

	override protected computeValue() {
		val bounds = host?.node?.boundsInLocal
		if(bounds != null) {
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

