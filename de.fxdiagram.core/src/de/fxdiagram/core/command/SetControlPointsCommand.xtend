package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.behavior.MoveBehavior
import java.util.ArrayList
import java.util.List
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import java.util.Map
import de.fxdiagram.core.XConnectionLabel

import static extension de.fxdiagram.core.extensions.ConnectionExtensions.*

@FinalFieldsConstructor
class SetControlPointsCommand extends AbstractCommand {

	val XConnection connection
	val List<XControlPoint> newControlPoints
	val Point2D localMousePos
	
	boolean splineShapeKeeperState
	List<XControlPoint> oldControlPoints
	Map<XConnectionLabel, Double> label2oldPosition = newHashMap
	Map<XConnectionLabel, Double> label2newPosition = newHashMap
			
	override execute(CommandContext context) {
		oldControlPoints = new ArrayList(connection.controlPoints)
		connection.labels.forEach[
			label2oldPosition.put(it, position)
			val oldLocation = connection.at(position)
			val newPointOnCurve = oldLocation.getNearestPointOnConnection(newControlPoints, connection.kind)
			label2newPosition.put(it, newPointOnCurve.parameter)
			position = newPointOnCurve.parameter
		]	
		connection.controlPoints.setAll(newControlPoints)
		splineShapeKeeperState = connection.connectionRouter.splineShapeKeeperEnabled
		connection.connectionRouter.splineShapeKeeperEnabled = true
		if(localMousePos != null) {
			val mousePos = connection.localToScreen(localMousePos)
			newControlPoints.filter[selected].forEach[
				getBehavior(MoveBehavior)?.startDrag(mousePos.x, mousePos.y)
			]
		}
	}
	
	override undo(CommandContext context) {
		connection.labels.forEach [
			position = label2oldPosition.get(it)
		]
		connection.controlPoints.setAll(oldControlPoints)
		connection.connectionRouter.splineShapeKeeperEnabled = splineShapeKeeperState
	}
	
	override redo(CommandContext context) {
		connection.labels.forEach [
			position = label2newPosition.get(it)
		]
		connection.controlPoints.setAll(newControlPoints)
		connection.connectionRouter.splineShapeKeeperEnabled = true
	}
}