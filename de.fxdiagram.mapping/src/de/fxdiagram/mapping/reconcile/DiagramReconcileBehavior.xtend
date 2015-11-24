package de.fxdiagram.mapping.reconcile

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractReconcileBehavior
import de.fxdiagram.core.behavior.ReconcileBehavior.UpdateAcceptor
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.tools.actions.ReconcileAction
import de.fxdiagram.mapping.DiagramMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.execution.InterpreterContext
import de.fxdiagram.mapping.execution.XDiagramConfigInterpreter
import de.fxdiagram.mapping.shapes.BaseDiagram
import eu.hansolo.enzo.radialmenu.SymbolCanvas
import eu.hansolo.enzo.radialmenu.SymbolType
import java.util.HashMap
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.paint.Color

import static de.fxdiagram.core.behavior.DirtyState.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

class DiagramReconcileBehavior<T> extends AbstractReconcileBehavior<XDiagram> {

	val interpreter = new XDiagramConfigInterpreter
	
	Node repairButton

	new(BaseDiagram<T> host) {
		super(host)
	}

	override getDirtyState() {
		if (host.domainObjectDescriptor instanceof IMappedElementDescriptor<?>) {
			val descriptor = host.domainObjectDescriptor as IMappedElementDescriptor<T>
			descriptor.withDomainObject [
				val localNodes = host.nodes.toMap[domainObjectDescriptor]
				val localConnections = host.connections.toMap[domainObjectDescriptor]
				val nodesToBeDeleted = new HashMap(localNodes)
				val connectionsToBeDeleted = new HashMap(localConnections)
				val allOtherShapes = host.getRootDiagram.allShapes.filter[diagram != host]
				val otherNodes = allOtherShapes.filter(XNode).toMap[domainObjectDescriptor]
				val otherConnections = allOtherShapes.filter(XConnection).toMap[domainObjectDescriptor]
				val flatContext = new InterpreterContext(host) {
					
					override getConnection(DomainObjectDescriptor descriptor) {
						connectionsToBeDeleted.remove(descriptor) 
							?: localConnections.get(descriptor) 
							?: otherConnections.get(descriptor) 
							?: addedConnections.findFirst[descriptor == domainObjectDescriptor]
					}
					
					override getNode(DomainObjectDescriptor descriptor) {
						nodesToBeDeleted.remove(descriptor) 
							?: localNodes.get(descriptor) 
							?: otherNodes.get(descriptor)  
							?: addedNodes.findFirst[descriptor == domainObjectDescriptor]
					}
				}
				interpreter.createDiagram(it, descriptor.mapping as DiagramMapping<T>, false, flatContext)
				if(!flatContext.addedShapes.empty || !nodesToBeDeleted.empty || !connectionsToBeDeleted.empty) 
					return DIRTY
				else
					return CLEAN
			]
		}
	}

	override reconcile(UpdateAcceptor acceptor) {
		if (host.domainObjectDescriptor instanceof IMappedElementDescriptor<?>) {
			val descriptor = host.domainObjectDescriptor as IMappedElementDescriptor<T>
			descriptor.withDomainObject [
				val localNodes = host.nodes.toMap[domainObjectDescriptor]
				val localConnections = host.connections.toMap[domainObjectDescriptor]
				val nodesToBeDeleted = new HashMap(localNodes)
				val connectionsToBeDeleted = new HashMap(localConnections)
				val allOtherShapes = host.getRootDiagram.allShapes.filter[diagram != host]
				val otherNodes = allOtherShapes.filter(XNode).toMap[domainObjectDescriptor]
				val otherConnections = allOtherShapes.filter(XConnection).toMap[domainObjectDescriptor]
				val flatContext = new InterpreterContext(host) {
					
					override getConnection(DomainObjectDescriptor descriptor) {
						connectionsToBeDeleted.remove(descriptor) 
							?: localConnections.get(descriptor) 
							?: otherConnections.get(descriptor)
							?: addedConnections.findFirst[descriptor == domainObjectDescriptor]
					}
					
					override getNode(DomainObjectDescriptor descriptor) {
						nodesToBeDeleted.remove(descriptor) 
							?: localNodes.get(descriptor) 
							?: otherNodes.get(descriptor)
							?: addedNodes.findFirst[descriptor == domainObjectDescriptor]
					}
				}
				interpreter.createDiagram(it, descriptor.mapping as DiagramMapping<T>, false, flatContext)
				for(addedShape: flatContext.addedShapes) 
					acceptor.add(addedShape, host)
					
				(nodesToBeDeleted.values + connectionsToBeDeleted.values).forEach [
					acceptor.delete(it, host)
				]
				null
			]
		}
	}

	override protected dirtyFeedback(boolean isDirty) {
		if(host.isRootDiagram) {
			if(isDirty)
				host.root.headsUpDisplay.add(repairButton, Pos.TOP_RIGHT)
			else 
				host.root.headsUpDisplay.children -= repairButton
		}
	}
	
	override protected doActivate() {
		if(host.isRootDiagram) {
			repairButton = SymbolCanvas.getSymbol(SymbolType.TOOL, 32, Color.GRAY) => [
				onMouseClicked = [
					new ReconcileAction().perform(host.root)
					host.root.headsUpDisplay.children -= repairButton
				]
				tooltip = "Repair diagram"
			]
		}
		super.doActivate
	}
}