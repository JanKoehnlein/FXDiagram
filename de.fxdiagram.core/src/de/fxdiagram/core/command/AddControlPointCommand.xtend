package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.behavior.MoveBehavior
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.XControlPoint.Type.*
import de.fxdiagram.core.XConnectionLabel
import java.util.Map
import java.util.ArrayList

import static extension de.fxdiagram.core.extensions.ConnectionExtensions.*

@FinalFieldsConstructor
class AddControlPointCommand extends AbstractCommand {
	
	val XConnection connection
	val int index
	val Point2D newPoint
	
	XControlPoint newControlPoint
	Map<XConnectionLabel, Double> label2oldPosition = newHashMap
	Map<XConnectionLabel, Double> label2newPosition = newHashMap
	
	override execute(CommandContext context) {
		newControlPoint = new XControlPoint => [
			layoutX = newPoint.x
			layoutY = newPoint.y
			type = DANGLING
			selected = true
		]
		val newControlPoints = new ArrayList(connection.controlPoints)
		newControlPoints.add(index, newControlPoint)
		connection.labels.forEach[
			label2oldPosition.put(it, position)
			val oldLocation = connection.at(position)
			val newPointOnCurve = oldLocation.getNearestPointOnConnection(newControlPoints, connection.kind)
			label2newPosition.put(it, newPointOnCurve.parameter)
			position = newPointOnCurve.parameter
		]		
		connection.controlPoints.add(index, newControlPoint)
		connection.updateShapes
		val mousePos = newControlPoint.parent.localToScreen(newPoint)
		newControlPoint.getBehavior(MoveBehavior)?.startDrag(mousePos.x, mousePos.y)
	}
	
	override undo(CommandContext context) {
		connection.labels.forEach [
			position = label2oldPosition.get(it)
		]
		connection.controlPoints.remove(index)
		connection.updateShapes
	}
	
	override redo(CommandContext context) {
		connection.labels.forEach [
			position = label2newPosition.get(it)
		]
		connection.controlPoints.add(index, newControlPoint)
		connection.updateShapes
		val mousePos = newControlPoint.parent.localToScreen(newPoint)
		newControlPoint.getBehavior(MoveBehavior)?.startDrag(mousePos.x, mousePos.y)
	}
}