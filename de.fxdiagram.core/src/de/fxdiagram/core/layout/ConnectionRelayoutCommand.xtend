package de.fxdiagram.core.layout

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.anchors.ConnectionMemento
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import java.util.List

import static de.fxdiagram.core.extensions.TransitionExtensions.*

class ConnectionRelayoutCommand extends AbstractAnimationCommand {
	
	XConnection connection
	
	ConnectionMemento from
	ConnectionMemento to
	
	new(XConnection connection, XConnection.Kind toKind, List<XControlPoint> toPoints) {
		this.connection = connection
		this.to = new ConnectionMemento(connection, toKind, toPoints)
	}
	
	override createExecuteAnimation(CommandContext context) {
		this.from = new ConnectionMemento(connection)
		createMorphTransition(connection, from, to, context.executeDuration)
	}

	override createUndoAnimation(CommandContext context) {
		createMorphTransition(connection, to, from, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		createMorphTransition(connection, from, to, context.defaultUndoDuration)
	}	
}