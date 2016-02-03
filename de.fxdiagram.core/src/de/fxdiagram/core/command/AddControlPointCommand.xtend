package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.behavior.MoveBehavior
import java.util.List
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.shape.CubicCurve
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.XControlPoint.Type.*
import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*

import static extension de.fxdiagram.core.extensions.BezierExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import java.util.ArrayList
import javafx.scene.shape.QuadCurve

@FinalFieldsConstructor
class AddControlPointCommand extends AbstractCommand {
	
	val XConnection connection
	val int index
	val Point2D newPoint
	
	static def createAddControlPointCommand(XConnection connection, Point2D localPosition) {
		switch connection.kind {
			case POLYLINE:
				createCommandForPolyline(connection, localPosition)
			case QUAD_CURVE:
				createCommandForQuadCurve(connection, localPosition)
			case CUBIC_CURVE:
				createCommandForCubicCurve(connection, localPosition)
			default: 
				null	
		}
	}
	
	protected static def createCommandForPolyline(XConnection connection, Point2D localPosition) {
		val controlPoints = connection.controlPoints
		var index = -1
		var Point2D newPoint = null 
		for(i: 0..controlPoints.size-2) {
			val segmentStart = new Point2D(controlPoints.get(i).layoutX, controlPoints.get(i).layoutY)
			val segmentEnd = new Point2D(controlPoints.get(i+1).layoutX, controlPoints.get(i+1).layoutY)
			if(localPosition.distance(segmentStart) < EPSILON 
				|| localPosition.distance(segmentEnd) < EPSILON)
				return null
			val delta0 = localPosition - segmentStart
			val delta1 = segmentEnd - segmentStart
			val projectionScale = (delta0.x * delta1.x + delta0.y * delta1.y)/(delta1.x*delta1.x + delta1.y*delta1.y) 
			var testPoint = segmentStart + projectionScale * delta1
			val delta = testPoint - localPosition
			if(delta.norm < 1 && projectionScale >= 0 && projectionScale <=1) {
				index = i+1
				newPoint = testPoint
			}
		}
		if(index == -1)
			return null
		else
			return new AddControlPointCommand(connection, index, newPoint)
	}
	
	protected static def createCommandForQuadCurve(XConnection connection, Point2D localPosition) {
		val splineSegments = (connection.node as Group)
			.children
			.filter(QuadCurve)
			.toList
		val splineSegment =	splineSegments.findFirst[ contains(localPosition) ]
		if(splineSegment == null)
			return null
		val t = splineSegment.findT(localPosition)
		val splitSegments = splineSegment.splitAt(t)
		val segmentIndex = splineSegments.indexOf(splineSegment)
		val oldControlPoints = new ArrayList(connection.controlPoints)
		val newControlPoints = newArrayList
		var cpIndex = 0 
		for(var i=0; i<segmentIndex; i++) {
			newControlPoints += oldControlPoints.get(cpIndex++)	
			newControlPoints += oldControlPoints.get(cpIndex++)	
		}
		newControlPoints += oldControlPoints.get(cpIndex++)	
		newControlPoints => [
			val seg0 = splitSegments.head
			add(seg0.controlX, seg0.controlY, CONTROL_POINT)
			val seg1 = splitSegments.last
			add(seg1.startX, seg1.startY, INTERPOLATED) => [
				selected = true
			]
			add(seg1.controlX, seg1.controlY, CONTROL_POINT)
		]
		cpIndex += 1
		while(cpIndex < oldControlPoints.size) 
			newControlPoints += oldControlPoints.get(cpIndex++)	
		return new SetControlPointsCommand(connection, newControlPoints, connection.localToScreen(localPosition))
	}
	
	protected static def createCommandForCubicCurve(XConnection connection, Point2D localPosition) {
		val splineSegments = (connection.node as Group)
			.children
			.filter(CubicCurve)
			.toList
		val splineSegment =	splineSegments.findFirst[ contains(localPosition) ]
		if(splineSegment == null)
			return null
		val t = splineSegment.findT(localPosition)
		val splitSegments = splineSegment.splitAt(t)
		val segmentIndex = splineSegments.indexOf(splineSegment)
		val oldControlPoints = new ArrayList(connection.controlPoints)
		val newControlPoints = newArrayList
		var cpIndex = 0 
		for(var i=0; i<segmentIndex; i++) {
			newControlPoints += oldControlPoints.get(cpIndex++)	
			newControlPoints += oldControlPoints.get(cpIndex++)	
			newControlPoints += oldControlPoints.get(cpIndex++)	
		}
		newControlPoints += oldControlPoints.get(cpIndex++)	
		newControlPoints => [
			val seg0 = splitSegments.head
			add(seg0.controlX1, seg0.controlY1, CONTROL_POINT)
			add(seg0.controlX2, seg0.controlY2, CONTROL_POINT)
			val seg1 = splitSegments.last
			add(seg1.startX, seg1.startY, INTERPOLATED) => [
				selected = true
			]
			add(seg1.controlX1, seg1.controlY1, CONTROL_POINT)
			add(seg1.controlX2, seg1.controlY2, CONTROL_POINT)
		]
		cpIndex += 2
		while(cpIndex < oldControlPoints.size) 
			newControlPoints += oldControlPoints.get(cpIndex++)	
		return new SetControlPointsCommand(connection, newControlPoints, localPosition)
	}
	
	protected static def add(List<XControlPoint> controlPoints, double x, double y, XControlPoint.Type cpType) {
		val newPoint = new XControlPoint => [
			layoutX = x
			layoutY = y
			type = cpType
		]
		controlPoints += newPoint
		newPoint 
	}
			
	override execute(CommandContext context) {
		val newControlPoint = new XControlPoint => [
					layoutX = newPoint.x
					layoutY = newPoint.y
					type = DANGLING
					selected = true
				]
		connection.controlPoints.add(index, newControlPoint)
		connection.updateShapes
		val mousePos = newControlPoint.parent.localToScreen(newPoint)
		newControlPoint.getBehavior(MoveBehavior)?.startDrag(mousePos.x, mousePos.y)
	}
	
	override undo(CommandContext context) {
		connection.controlPoints.remove(index)
		connection.updateShapes
	}
	
	override redo(CommandContext context) {
		execute(context)
	}
	
}