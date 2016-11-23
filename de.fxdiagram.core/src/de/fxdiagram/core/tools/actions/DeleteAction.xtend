package de.fxdiagram.core.tools.actions

import com.google.common.collect.Multimaps
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XDiagramContainer
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.AddRemoveCommand
import de.fxdiagram.core.command.ParallelAnimationCommand
import de.fxdiagram.core.command.RemoveControlPointCommand
import de.fxdiagram.core.command.ResetConnectionCommand
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import static de.fxdiagram.core.XConnection.Kind.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class DeleteAction implements DiagramAction {
	
	override matches(KeyEvent event) {
		event.code == KeyCode.DELETE || event.code == KeyCode.BACK_SPACE
	}
	
	override getSymbol() {
		SymbolType.TRASH
	}
	
	override getTooltip() {
		'Delete selection'
	}
	
	override perform(XRoot root) {
  		val elements = root.currentSelection.toSet
		val nodes = elements.filter(XNode).allContainedNodes
  		val deleteThem = 
			(nodes.map[incomingConnections].flatten + 
			nodes.map[outgoingConnections].flatten +
			elements.filter[it instanceof XNode || it instanceof XConnection]).toSet
		val connection2controlPoints = Multimaps.index(elements.filter(XControlPoint), [connection])
		val connectionMorphCommands = <AbstractAnimationCommand>newArrayList
		connection2controlPoints.keySet.forEach [ connection |
			if(!elements.contains(connection)) {
				val controlPoints = connection2controlPoints.get(connection)
				if(connection.kind == RECTILINEAR)
					connectionMorphCommands += new ResetConnectionCommand(connection)
				else
					connectionMorphCommands += new RemoveControlPointCommand(
						connection, 
						controlPoints)
			}
		]
		val diagram2shape = Multimaps.index(deleteThem, [diagram])
		val removeCommands = diagram2shape.keySet.map [ diagram |
			AddRemoveCommand.newRemoveCommand(diagram, diagram2shape.get(diagram))
		]
		root.commandStack.execute(new ParallelAnimationCommand => [
			it += removeCommands
			if(!connectionMorphCommands.empty) 
				it += connectionMorphCommands
		])
	}
	
	protected def getConnection(XControlPoint point) {
		point?.parent?.parent as XConnection
	}
	
	protected def Iterable<XNode> getAllContainedNodes(Iterable<XNode> nodes) {
		nodes + nodes.filter(XDiagramContainer).map[innerDiagram.nodes.allContainedNodes].flatten
		
	}
}
