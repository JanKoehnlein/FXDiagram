package de.fxdiagram.core.behavior

import de.fxdiagram.core.XNode
import de.fxdiagram.core.anchors.ConnectionMemento
import de.fxdiagram.core.command.ParallelAnimationCommand
import java.util.List

class NodeMoveBehavior extends MoveBehavior<XNode> {
	
	List<ConnectionMemento> connectionMementi
	
	new(XNode host) {
		super(host)
	}

	override startDrag(double screenX, double screenY) {
		connectionMementi = newArrayList
		connectionMementi += (host.outgoingConnections + host.incomingConnections).map[
			new ConnectionMemento(it)
		]
		super.startDrag(screenX, screenY)
	}
	
	override protected createMoveCommand() {
		val connectionCommands = connectionMementi.map[createChangeCommand].filterNull.toList
		val moveCommand = super.createMoveCommand()
		if(connectionCommands.empty)
			return moveCommand
		else 
			return new ParallelAnimationCommand => [ sac |
				sac += moveCommand
				connectionCommands.forEach [
					sac += it
				] 
			]
	}
}