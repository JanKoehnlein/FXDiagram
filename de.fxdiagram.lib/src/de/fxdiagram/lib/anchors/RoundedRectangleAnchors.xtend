package de.fxdiagram.lib.anchors

import de.fxdiagram.core.XNode
import de.fxdiagram.core.anchors.RectangleAnchors
import javafx.geometry.BoundingBox
import javafx.geometry.Dimension2D
import javafx.geometry.Point2D

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import javafx.geometry.Side
import javafx.geometry.Bounds

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
		if(rectAnchor === null)
			return null
		val boundsInRootDiagram = boundsInRoot
		if(boundsInRootDiagram === null)
			return null
		val radiusBounds = host.node.localToRootDiagram(new BoundingBox(0, 0, radiusX, radiusY))
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
	
	override getManhattanAnchor(double x, double y, Side side) {
		val rectAnchor = super.getManhattanAnchor(x, y, side)
		if(rectAnchor === null)
			return null
		val bounds = boundsInRoot
		val radius= host.node.localToRootDiagram(new BoundingBox(0, 0, radiusX, radiusY))
		switch side {
			case TOP:
				if(bounds.minX < x && x < bounds.minX + radius.width)
					return new Point2D(x, bounds.minY + radius.height - getY(bounds.minX + radius.width - x, radius))
				else if(bounds.maxX - radius.width < x && x < bounds.maxX)
					return new Point2D(x, bounds.minY + radius.height - getY(bounds.maxX - radius.width - x, radius))
			case BOTTOM:
				if(bounds.minX < x && x < bounds.minX + radius.width)
					return new Point2D(x, bounds.maxY - radius.height + getY(bounds.minX + radius.width - x, radius))
				else if(bounds.maxX - radius.width < x && x < bounds.maxX)
					return new Point2D(x, bounds.maxY - radius.height + getY(bounds.maxX - radius.width - x, radius))
			case LEFT:
				if(bounds.minY < y && y < bounds.minY + radius.height)
					return new Point2D(bounds.minX + radius.width - getX(bounds.minY + radius.height - y, radius), y)
				else if(bounds.maxY - radius.height < y && y < bounds.maxY)
					return new Point2D(bounds.minX + radius.width - getX(bounds.maxY - radius.height - y, radius), y)
			case RIGHT:
				if(bounds.minY < y && y < bounds.minY + radius.height)
					return new Point2D(bounds.maxX - radius.width + getX(bounds.minY + radius.height - y, radius), y)
				else if(bounds.maxY - radius.height < y && y < bounds.maxY)
					return new Point2D(bounds.maxX - radius.width + getX(bounds.maxY - radius.height - y, radius), y)
		}
		return rectAnchor
	}

	protected def getX(double y, Bounds radius) {
		radius.width * sqrt(1-(y*y) / (radius.height * radius.height))
	}

	protected def getY(double x, Bounds radius) {
		radius.height * sqrt(1-(x*x) / (radius.width * radius.width))
	}
}
