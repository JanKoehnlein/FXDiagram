package de.fxdiagram.core.anchors

import de.fxdiagram.core.XConnection

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.ConnectionExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import de.fxdiagram.core.XControlPoint
import javafx.geometry.Point2D

/**
 * Makes sure that the outer control points don't lie within the arrow head
 */
class ControlPointCorrector {
	
	def void correctControlPoints(XConnection connection) {
		val controlPoints = connection.controlPoints
		if(controlPoints.size > 2) {
			if(connection.sourceArrowHead != null) {
				val arrowTip = connection.connectionRouter.getNearestAnchor(connection.source, controlPoints.head, null)
				correct(controlPoints.get(1), controlPoints.head.toPoint2D, arrowTip, connection.sourceArrowHead)
			}
			if(connection.targetArrowHead != null) {
				val arrowTip = connection.connectionRouter.getNearestAnchor(connection.target, controlPoints.last, null)
				correct(controlPoints.get(controlPoints.size-2), controlPoints.last.toPoint2D, arrowTip, connection.targetArrowHead)
			}
		}
	}
	
	protected def correct(XControlPoint criticalPoint, Point2D arrowEnd, Point2D arrowTip, ArrowHead arrowHead) {
		if(arrowTip != null && norm(criticalPoint.toPoint2D - arrowTip) < arrowHead.lineCut) {
			var delta = arrowTip - arrowEnd
			delta = delta / norm(delta)  
			criticalPoint.layoutXProperty.setSafely(arrowEnd.x - delta.x)
			criticalPoint.layoutYProperty.setSafely(arrowEnd.y - delta.y)
		}
	}
}