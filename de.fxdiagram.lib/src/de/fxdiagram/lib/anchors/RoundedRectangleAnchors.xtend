package de.fxdiagram.lib.anchors

import de.fxdiagram.core.XNode
import de.fxdiagram.core.anchors.RectangleAnchors
import javafx.geometry.BoundingBox
import javafx.geometry.Dimension2D
import javafx.geometry.Point2D

import static java.lang.Math.*

import static extension de.fxdiagram.core.Extensions.*

class RoundedRectangleAnchors extends RectangleAnchors {

	double radiusX
	double radiusY

	new(XNode host, double radiusX, double radiusY) {
		super(host)
		this.radiusX = radiusX
		this.radiusY = radiusY
	}

	override getAnchor(double x, double y) {
		val rectAnchor = super.getAnchor(x, y)
		val boundsInRootDiagram = host.getNode.localToRootDiagram(host.getNode.layoutBounds)
		if(boundsInRootDiagram == null)
			return null
		val radiusBounds = host.getNode.localToRootDiagram(new BoundingBox(0, 0, radiusX, radiusY))
		val radiusInRootDiagram = new Dimension2D(radiusBounds.width, radiusBounds.height)
		if (rectAnchor.x < boundsInRootDiagram.minX + radiusInRootDiagram.width && 
			rectAnchor.y < boundsInRootDiagram.minY + radiusInRootDiagram.height) {
			// top-left corner
			getPointOnCircle(rectAnchor.x, rectAnchor.y, boundsInRootDiagram.minX + radiusInRootDiagram.width,
				boundsInRootDiagram.minY + radiusInRootDiagram.height,
				radiusInRootDiagram)
		} else if (rectAnchor.x > boundsInRootDiagram.maxX - radiusInRootDiagram.width &&
			rectAnchor.y < boundsInRootDiagram.minY + radiusInRootDiagram.height) {
			// top-right
			getPointOnCircle(rectAnchor.x, rectAnchor.y, boundsInRootDiagram.maxX - radiusInRootDiagram.width,
				boundsInRootDiagram.minY + radiusInRootDiagram.height,
				radiusInRootDiagram)
		} else if (rectAnchor.x < boundsInRootDiagram.minX + radiusInRootDiagram.width &&
			rectAnchor.y > boundsInRootDiagram.maxY - radiusInRootDiagram.height) {
			// bottom-left corner
			getPointOnCircle(rectAnchor.x, rectAnchor.y, boundsInRootDiagram.minX + radiusInRootDiagram.width,
				boundsInRootDiagram.maxY - radiusInRootDiagram.height,
				radiusInRootDiagram)
		} else if (rectAnchor.x > boundsInRootDiagram.maxX - radiusInRootDiagram.width &&
			rectAnchor.y > boundsInRootDiagram.maxY - radiusInRootDiagram.height) {
			// bottom-right corner
			getPointOnCircle(rectAnchor.x, rectAnchor.y, boundsInRootDiagram.maxX - radiusInRootDiagram.width,
				boundsInRootDiagram.maxY - radiusInRootDiagram.height,
				radiusInRootDiagram)
		} else
			rectAnchor
	}

	def getPointOnCircle(double x, double y, double centerX, double centerY, Dimension2D radius) {
		val angle = atan2(y - centerY, x - centerX)
		new Point2D(centerX + cos(angle) * radius.width, centerY + sin(angle) * radius.height)
	}

}
