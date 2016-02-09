package de.fxdiagram.core.extensions

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import java.util.List
import javafx.geometry.Point2D
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.QuadCurve
import org.eclipse.xtend.lib.annotations.Data

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BezierExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*

class ConnectionExtensions {
	
	static def PointOnCurve getNearestPointOnConnection(Point2D pointInLocal, List<Point2D> controlPoints, XConnection.Kind kind) {
		switch kind {
			case POLYLINE:
				getNearestPointOnPolyline(pointInLocal, controlPoints)
			case QUAD_CURVE:
				getNearestPointOnQuadraticSpline(pointInLocal, controlPoints)
			case CUBIC_CURVE:
				getNearestPointOnCubicSpline(pointInLocal, controlPoints)
		}
	}
	
	def static PointOnCurve getNearestPointOnCubicSpline(Point2D pointInLocal, List<Point2D> controlPoints) {
		if((controlPoints.size - 1) % 3 != 0) 
			throw new IllegalArgumentException('Invalid number of points for a cubic spline curve: ' + controlPoints.size)
		val numSegments = (controlPoints.size - 1) / 3
		var PointOnCurve bestMatch = null
		for(i: 0..<numSegments) {
			val start = controlPoints.get(3 * i)
			val control0 = controlPoints.get(3 * i + 1)
			val control1 = controlPoints.get(3 * i + 2)
			val end = controlPoints.get(3 * i + 3)
			val curve = new CubicCurve(start.x, start.y, control0.x, control0.y, control1.x, control1.y, end.x, end.y)
			val match = findNearestPoint([curve.at(it)], pointInLocal, i, numSegments)
			if(match.isBetterThan(bestMatch))
				bestMatch = match
		}
		return bestMatch
	}
	
	
	def static PointOnCurve getNearestPointOnQuadraticSpline(Point2D pointInLocal, List<Point2D> controlPoints) {
		if((controlPoints.size - 1) % 2 != 0) 
			throw new IllegalArgumentException('Invalid number of points for a quadratic spline curve: ' + controlPoints.size)
		val numSegments = (controlPoints.size - 1) / 2
		var PointOnCurve bestMatch = null
		for(i: 0..<numSegments) {
			val start = controlPoints.get(2 * i)
			val control = controlPoints.get(2 * i + 1)
			val end = controlPoints.get(2 * i + 2)
			val curve = new QuadCurve(start.x, start.y, control.x, control.y, end.x, end.y)
			val match = findNearestPoint([curve.at(it)], pointInLocal, i, numSegments)
			if(match.isBetterThan(bestMatch))
				bestMatch = match
		}
		return bestMatch
	}
	
	protected static def findNearestPoint((double)=>Point2D curve, Point2D pointInLocal, int segmentIndex, int numSegments) {
		var left = 0.0
		var right = 1.0
		var distLeft = norm(curve.apply(left) - pointInLocal)
		var distRight = norm(curve.apply(right) - pointInLocal)
		var double mid
		var Point2D midPoint = null
		var double distMid 
		while (right-left > sqrt(EPSILON)) {
			mid = linear(left, right, 0.5)
			midPoint = curve.apply(mid)
		  	distMid = norm(midPoint - pointInLocal)
		  	if(distMid < EPSILON)
		  		return new PointOnCurve(midPoint, mid, (mid + segmentIndex) / numSegments, segmentIndex, distMid)
		  	val dLeftMid = norm(curve.apply(mid - EPSILON) - pointInLocal) - distMid
		  	val dRightMid = norm(curve.apply(mid + EPSILON) - pointInLocal) - distMid
		  	if(dLeftMid < 0) {
		  		if(dRightMid < 0) {
		  			return new PointOnCurve(midPoint, mid, (mid + segmentIndex) / numSegments, segmentIndex, distMid)
		  		} else {
		  			right=mid
		  			distRight = distMid	
		  		}
		  	} else if(dRightMid < 0) {
		  		left=mid
		  		distLeft = distMid
		  	} else {
	  			return new PointOnCurve(midPoint, mid, (mid + segmentIndex) / numSegments, segmentIndex, distMid)
		  	}
		}
		return new PointOnCurve(midPoint, mid, (mid + segmentIndex) / numSegments, segmentIndex, distMid) 
	}
	
	
	def static PointOnCurve getNearestPointOnPolyline(Point2D pointInLocal, List<Point2D> controlPoints) {
		val numSegments =  controlPoints.size - 1.0
		var PointOnCurve bestMatch = null 
		for(i: 0..controlPoints.size-2) {
			val segmentStart = controlPoints.get(i)
			val segmentEnd = controlPoints.get(i+1)
			if(pointInLocal.distance(segmentStart) < EPSILON) 
				return new PointOnCurve(segmentStart, 0, i / numSegments, i, pointInLocal.distance(segmentStart))
			if(pointInLocal.distance(segmentEnd) < EPSILON)
				return new PointOnCurve(segmentEnd, 1, (i + 1) / numSegments, i, pointInLocal.distance(segmentEnd))
			val delta0 = pointInLocal - segmentStart
			val delta1 = segmentEnd - segmentStart
			val scale = delta1.x*delta1.x + delta1.y*delta1.y
			if(scale > EPSILON) {
				val projectionScale = (delta0.x * delta1.x + delta0.y * delta1.y)/scale 
				var testPoint = segmentStart + projectionScale * delta1
				val delta = testPoint - pointInLocal
				if(projectionScale >= 0 && projectionScale <=1) {
					val match = new PointOnCurve(testPoint, projectionScale, (i + projectionScale) / numSegments, i, delta.norm)
					if(match.isBetterThan(bestMatch))
						bestMatch = match
				}
			}
		}
		return bestMatch
	}
	
	def static toPoint2D(XControlPoint controlPoint) {
		new Point2D(controlPoint.layoutX, controlPoint.layoutY)
	}
	
	@Data
	static class PointOnCurve {
		Point2D point
		double localParameter
		double parameter
		int segmentIndex
		double distance
		
		def isBetterThan(PointOnCurve other) {
			other == null || distance < other.distance
		}
	}
	
}