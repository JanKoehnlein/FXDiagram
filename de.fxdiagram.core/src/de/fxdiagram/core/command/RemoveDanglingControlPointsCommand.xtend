package de.fxdiagram.core.command

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import java.util.List
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.XConnection.Kind.*
import static de.fxdiagram.core.XControlPoint.Type.*

import static extension de.fxdiagram.core.extensions.ConnectionExtensions.*

@FinalFieldsConstructor
class RemoveDanglingControlPointsCommand extends AbstractCommand {

	val XConnection connection

	List<XControlPoint> controlPointsCopy

	override execute(CommandContext context) {
		controlPointsCopy = newArrayList
		controlPointsCopy += connection.controlPoints
		val label2position = newHashMap(connection.labels.map [
			it -> connection.at(position)
		])
		connection.controlPoints.setAll(controlPointsCopy.filter[type != DANGLING])
		connection.labels.forEach [
			position = label2position.get(it).getNearestPointOnConnection(connection.controlPoints.map [
				toPoint2D
			], POLYLINE).parameter
		]
	}

	override undo(CommandContext context) {
		val label2position = newHashMap(connection.labels.map [
			it -> connection.at(position)
		])
		connection.controlPoints.setAll(controlPointsCopy)
		connection.labels.forEach [
			position = label2position.get(it).getNearestPointOnConnection(connection.controlPoints.map [
				toPoint2D
			], POLYLINE).parameter
		]
	}

	override redo(CommandContext context) {
		val label2position = newHashMap(connection.labels.map [
			it -> connection.at(position)
		])
		connection.controlPoints.setAll(controlPointsCopy.filter[type != DANGLING])
		connection.labels.forEach [
			position = label2position.get(it).getNearestPointOnConnection(connection.controlPoints.map [
				toPoint2D
			], POLYLINE).parameter
		]
	}
}
