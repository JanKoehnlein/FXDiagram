package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.behavior.MoveBehavior
import java.util.ArrayList
import java.util.List
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

@FinalFieldsConstructor
class SetControlPointsCommand extends AbstractCommand {

	val XConnection connection
	val List<XControlPoint> newControlPoints
	val Point2D localMousePos
	
	boolean splineShapeKeeperState
	ArrayList<XControlPoint> oldControlPoints
			
	override execute(CommandContext context) {
		oldControlPoints = new ArrayList(connection.controlPoints)
		connection.controlPoints.setAll(newControlPoints)
		splineShapeKeeperState = connection.connectionRouter.splineShapeKeeperEnabled
		connection.connectionRouter.splineShapeKeeperEnabled = true
		if(localMousePos != null) {
			val mousePos = connection.localToScreen(localMousePos)
			newControlPoints.filter[selected].forEach[
				getBehavior(MoveBehavior).startDrag(mousePos.x, mousePos.y)
			]
		}
	}
	
	override undo(CommandContext context) {
		connection.controlPoints.setAll(oldControlPoints)
		connection.connectionRouter.splineShapeKeeperEnabled = splineShapeKeeperState
	}
	
	override redo(CommandContext context) {
		connection.controlPoints.setAll(newControlPoints)
		connection.connectionRouter.splineShapeKeeperEnabled = true
	}
}