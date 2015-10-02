package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XNode
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*


@FinalFieldsConstructor
class SplineShapeKeeper {

	Point2D lastSourceCenter
	Point2D lastTargetCenter

	val XConnection connection

	def reset() {
		lastSourceCenter = null
		lastTargetCenter = null
	}

	protected def getControlPoints() {
		connection.controlPoints
	}

	protected def adjustControlPointsToNodeMove() {
		if (connection.isActive
				&& connection.kind != POLYLINE 
				&& controlPoints.size > 2 
				&& lastSourceCenter != null 
				&& lastTargetCenter != null 
				&& !controlPoints.exists[selected || layoutXProperty.bound || layoutYProperty.bound]) {
			val newSourceCenter = connection.source.midPoint
			val newTargetCenter = connection.target.midPoint
			
			var Point2D oldFixedPoint = lastTargetCenter
			var Point2D newFixedPoint = newTargetCenter
			for(i: 1..<controlPoints.size -1) 
				adjust(controlPoints.get(i), oldFixedPoint, lastSourceCenter, newFixedPoint, newSourceCenter)					
			lastSourceCenter = newSourceCenter
			lastTargetCenter = newTargetCenter
		} else {
			lastSourceCenter = connection.source.midPoint
			lastTargetCenter = connection.target.midPoint
		}
	}
	
	protected def adjust(XControlPoint cp, Point2D oldFixed, Point2D oldCenter, Point2D newFixed, Point2D newCenter) {
		val oldDelta = oldCenter - oldFixed
		val cpPoint = new Point2D(cp.layoutX, cp.layoutY)
		val oldCpDelta = cpPoint - oldFixed
		val oldDeltaNorm2 = (oldDelta.x * oldDelta.x + oldDelta.y * oldDelta.y)
		if(oldDeltaNorm2 < EPSILON) 
			return
		val lambda = (oldDelta.x * oldCpDelta.x + oldDelta.y * oldCpDelta.y) / oldDeltaNorm2
		val theta = (oldDelta.y * oldCpDelta.x - oldDelta.x *oldCpDelta.y) / oldDeltaNorm2
		
		val newDelta = newCenter - newFixed
		val newPointOnLine = newFixed + lambda * newDelta
		if(newDelta.norm < EPSILON) {
			cp.layoutX = newPointOnLine.x
			cp.layoutY = newPointOnLine.y
		} else {
			val orthoNewDelta = theta * new Point2D(newDelta.y, -newDelta.x)
			cp.layoutX = newPointOnLine.x + orthoNewDelta.x
			cp.layoutY = newPointOnLine.y + orthoNewDelta.y
		}
	}
	

	protected def midPoint(XNode node) {
		node.localToRootDiagram(node.node.boundsInLocal.center)
	}
}