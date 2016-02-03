package de.fxdiagram.core.behavior

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.command.AddControlPointCommand
import de.fxdiagram.core.command.SetControlPointsCommand
import java.util.ArrayList
import java.util.List
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.QuadCurve

import static de.fxdiagram.core.XControlPoint.Type.*
import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*

import static extension de.fxdiagram.core.extensions.BezierExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*

class AddControlPointBehavior extends AbstractHostBehavior<XConnection> {

	new(XConnection connection) {
		super(connection)
	}
	
	override protected doActivate() {
	}
	
	override getBehaviorKey() {
		AddControlPointBehavior
	}
	
	def addControlPoint(Point2D newPointLocalPosition) {
		val createCommand = createAddControlPointCommand(newPointLocalPosition)
		if(createCommand != null)
			host.root.commandStack.execute(createCommand)
	}
	
	protected def createAddControlPointCommand(Point2D localPosition) {
		switch host.kind {
			case POLYLINE:
				createCommandForPolyline(localPosition)
			case QUAD_CURVE:
				createCommandForQuadCurve(localPosition)
			case CUBIC_CURVE:
				createCommandForCubicCurve(localPosition)
			default: 
				null	
		}
	}
	
	protected def createCommandForPolyline(Point2D localPosition) {
		val controlPoints = host.controlPoints
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
			return new AddControlPointCommand(host, index, newPoint)
	}
	
	protected def createCommandForQuadCurve(Point2D localPosition) {
		val splineSegments = (host.node as Group)
			.children
			.filter(QuadCurve)
			.toList
		val splineSegment =	splineSegments.findFirst[ contains(localPosition) ]
		if(splineSegment == null)
			return null
		val t = splineSegment.findT(localPosition)
		val splitSegments = splineSegment.splitAt(t)
		val segmentIndex = splineSegments.indexOf(splineSegment)
		val oldControlPoints = new ArrayList(host.controlPoints)
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
		return new SetControlPointsCommand(host, newControlPoints, host.localToScreen(localPosition))
	}
	
	protected def createCommandForCubicCurve(Point2D localPosition) {
		val splineSegments = (host.node as Group)
			.children
			.filter(CubicCurve)
			.toList
		val splineSegment =	splineSegments.findFirst[ contains(localPosition) ]
		if(splineSegment == null)
			return null
		val t = splineSegment.findT(localPosition)
		val splitSegments = splineSegment.splitAt(t)
		val segmentIndex = splineSegments.indexOf(splineSegment)
		val oldControlPoints = new ArrayList(host.controlPoints)
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
		return new SetControlPointsCommand(host, newControlPoints, localPosition)
	}
	
	protected def add(List<XControlPoint> controlPoints, double x, double y, XControlPoint.Type cpType) {
		val newPoint = new XControlPoint => [
			layoutX = x
			layoutY = y
			type = cpType
		]
		controlPoints += newPoint
		newPoint 
	}
	
}