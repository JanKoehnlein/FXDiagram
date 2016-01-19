package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.XControlPoint.Type.*
import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*

import static extension de.fxdiagram.core.extensions.Point2DExtensions.*
import de.fxdiagram.core.behavior.MoveBehavior

@FinalFieldsConstructor
class AddControlPointCommand extends AbstractCommand {
	
	val XConnection connection
	val int index
	val Point2D newPoint
	
	static def createAddControlPointCommand(XConnection connection, Point2D localPosition) {
		if(connection.kind != POLYLINE)
			return null
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