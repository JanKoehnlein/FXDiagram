package de.fxdiagram.core.anchors

import de.fxdiagram.core.XNode
import javafx.geometry.Point2D

import static java.lang.Math.*

import static extension de.fxdiagram.core.Extensions.*

class RectangleAnchors implements Anchors {

	static val double EPSILON = 1e-6

	protected XNode host

	new(XNode host) {
		this.host = host
	}

	override getAnchor(double x, double y) {
		val boundsInRootDiagram = host.node.localToRootDiagram(host.node.layoutBounds)
		val centerX = 0.5 * (boundsInRootDiagram.minX + boundsInRootDiagram.maxX)
		val centerY = 0.5 * (boundsInRootDiagram.minY + boundsInRootDiagram.maxY)
		val finder = new NearestPointFinder(x,y)
		if (abs(centerY - y) > EPSILON) {
			val xTop = getXIntersection(boundsInRootDiagram.minY, centerX, centerY, x, y)
			if(xTop >= boundsInRootDiagram.minX && xTop <= boundsInRootDiagram.maxX)
				finder.addCandidate(xTop, boundsInRootDiagram.minY)
			val xBottom = getXIntersection(boundsInRootDiagram.maxY, centerX, centerY, x, y)
			if(xBottom >= boundsInRootDiagram.minX && xBottom <= boundsInRootDiagram.maxX)
				finder.addCandidate(xBottom, boundsInRootDiagram.maxY)
		}
		if (abs(centerX - x) > EPSILON) {
			val yLeft = getYIntersection(boundsInRootDiagram.minX, centerX, centerY, x, y)
			if(yLeft >= boundsInRootDiagram.minY  && yLeft <= boundsInRootDiagram.maxY)
				finder.addCandidate(boundsInRootDiagram.minX, yLeft)
			val yRight = getYIntersection(boundsInRootDiagram.maxX, centerX, centerY, x, y)
			if(yRight >= boundsInRootDiagram.minY  && yRight <= boundsInRootDiagram.maxY)
				finder.addCandidate(boundsInRootDiagram.maxX, yRight)
		}
		finder.currentNearest 
	}

	protected def getXIntersection(double yIntersection, double centerX, double centerY, double pointX, double pointY) {
		val t = (yIntersection - centerY) / (pointY - centerY)
		(pointX - centerX) * t + centerX
	}

	protected def getYIntersection(double xIntersection, double centerX, double centerY, double pointX, double pointY) {
		val t = (xIntersection - centerX) / (pointX - centerX)
		(pointY - centerY) * t + centerY
	}
}

class NearestPointFinder {
	double refX
	double refY
	double currentDistanceSquared = Double.MAX_VALUE
	Point2D currentNearest

	new(double refX, double refY) {
		this.refX = refX
		this.refY = refY
	}

	protected def getCurrentNearest() {
		currentNearest
	}

	def addCandidate(Point2D point) {
		val distanceSquared = (point.x - refX) * (point.x - refX) + (point.y - refY) * (point.y - refY)
		if (distanceSquared < currentDistanceSquared) {
			currentDistanceSquared = distanceSquared
			currentNearest = point
		}
	}

	def addCandidate(double px, double py) {
		val distanceSquared = (px - refX) * (px - refX) + (py - refY) * (py - refY)
		if (distanceSquared < currentDistanceSquared) {
			currentDistanceSquared = distanceSquared
			currentNearest = new Point2D(px,py)
		}
	}
}
