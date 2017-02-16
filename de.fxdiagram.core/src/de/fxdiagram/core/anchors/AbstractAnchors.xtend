package de.fxdiagram.core.anchors

import de.fxdiagram.core.XNode
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.geometry.Side
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

@FinalFieldsConstructor
abstract class AbstractAnchors implements Anchors, ManhattanAnchors {
	
	protected val XNode host
	
	protected def getDefaultAnchor(Bounds bounds, Side side) {
		val center = bounds.center
		switch side {
			case TOP: new Point2D(center.x, bounds.minY)
		 	case BOTTOM: new Point2D(center.x, bounds.maxY)
			case LEFT: new Point2D(bounds.minX, center.y)
			case RIGHT: new Point2D(bounds.maxX, center.y)
		}
	}
	
	protected def Bounds getBoundsInRoot() {
		host.node.localToRootDiagram(host.node.layoutBounds)
	}

	protected def Bounds getSnapBoundsInRoot() {
		host.localToRootDiagram(host.snapBounds)
	}

	override getDefaultAnchor(Side side) {
		getDefaultAnchor(boundsInRoot, side)
	}
	
	override getDefaultSnapAnchor(Side side) {
		getDefaultAnchor(snapBoundsInRoot, side)
	}
}