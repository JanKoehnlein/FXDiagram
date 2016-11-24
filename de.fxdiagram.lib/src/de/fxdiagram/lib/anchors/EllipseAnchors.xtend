package de.fxdiagram.lib.anchors

import de.fxdiagram.core.XNode
import de.fxdiagram.core.anchors.Anchors
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import javafx.geometry.Side
import de.fxdiagram.core.anchors.ManhattanAnchors
import javafx.geometry.Bounds

@FinalFieldsConstructor
class EllipseAnchors implements Anchors, ManhattanAnchors {

	val XNode host

	override getAnchor(double x, double y) {
		val bounds = host.node.localToRootDiagram(host.node.boundsInLocal)
		if(bounds == null)
			return null
		val center = bounds.center
		val angle = atan2(y - center.y, x - center.x)		
		return center + new Point2D(0.5 * bounds.width * cos(angle), 0.5 * bounds.height * sin(angle)) 
	}
	
	override getManhattanAnchor(double x, double y, Side side) {
		val bounds = host.node.localToRootDiagram(host.node.boundsInLocal)
		val center = bounds.center
		switch side {
			case TOP:
				return new Point2D(x, center.y - getY(center.x - x, bounds))
			case BOTTOM:
				return new Point2D(x, center.y + getY(center.x - x, bounds))
			case LEFT:
				return new Point2D(center.x - getX(center.y - y, bounds), y)
			case RIGHT:
				return new Point2D(center.x + getX(center.y - y, bounds), y)
		}
	}

	protected def getX(double y, Bounds radius) {
		0.5 * radius.width * sqrt(1 - 4 * (y*y) / (radius.height * radius.height))
	}

	protected def getY(double x, Bounds radius) {
		0.5 * radius.height * sqrt(1 - 4 * (x*x) / (radius.width * radius.width))
	}
}