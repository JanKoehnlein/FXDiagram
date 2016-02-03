package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.behavior.MoveBehavior
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.XControlPoint.Type.*

@FinalFieldsConstructor
class AddControlPointCommand extends AbstractCommand {
	
	val XConnection connection
	val int index
	val Point2D newPoint
	
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