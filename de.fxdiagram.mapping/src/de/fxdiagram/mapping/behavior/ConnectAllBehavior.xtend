package de.fxdiagram.mapping.behavior

import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.tools.actions.DiagramAction
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.execution.InterpreterContext
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter
import eu.hansolo.enzo.radialmenu.SymbolType
import javafx.scene.input.KeyEvent

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class ConnectAllBehavior<T> extends AbstractHostBehavior<XDiagram> {
	
	new(XDiagram host) {
		super(host)
	}
	
	override protected doActivate() {
		host.root.diagramActionRegistry += new ConnectAllAction
	}
	
	override getBehaviorKey() {
		ConnectAllBehavior
	}
}

class ConnectAllAction<T> implements DiagramAction {
	
	val configInterpreter = new XDiagramConfigInterpreter
	
	override matches(KeyEvent event) {
		false
	}
	
	override getSymbol() {
		SymbolType.REFRESH
	}
	
	override getTooltip() {
		'Connect all nodes'
	}
	
	override perform(XRoot root) {
		val diagramDO = root.diagram.domainObjectDescriptor
		if(diagramDO instanceof IMappedElementDescriptor<?>) {
			(diagramDO as IMappedElementDescriptor<T>).withDomainObject[ diagramObject |
				val diagramMapping = diagramDO.mapping as DiagramMapping<T>
				val allConnectionMappings = ((diagramMapping.config.mappings.filter[it instanceof ConnectionMapping<?>]) as Iterable<ConnectionMapping<?>>).toSet
				val context = new InterpreterContext(root.diagram)
				diagramMapping.nodes.forEach[
					configInterpreter.connectNodesEagerly(it, 
						diagramObject, 
						allConnectionMappings, 
						context
					)
				]
				context.executeCommands(root.commandStack)
				null
			]
		}
	}
}