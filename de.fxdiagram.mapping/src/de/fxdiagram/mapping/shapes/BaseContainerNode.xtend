package de.fxdiagram.mapping.shapes

import com.google.common.annotations.Beta
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XDiagramContainer
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.DirtyState
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.reconcile.NodeReconcileBehavior
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.layout.VBox

import static de.fxdiagram.core.behavior.DirtyState.*
import static de.fxdiagram.mapping.reconcile.MappingLabelListener.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior.*

/**
 * Base implementation for an {@link XNode} that contains other nodes and belongs to an {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector. 
 */
@Beta
@ModelNode('innerDiagram')
class BaseContainerNode<T> extends XNode implements INodeWithLazyMappings, XDiagramContainer {
	
	public static val NODE_HEADING = 'containerNodeHeading'
	
	@FxProperty XDiagram innerDiagram
	
	Group diagramGroup
	
	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}
	
	override postLoad() {
		domainObjectDescriptor?.mapping?.config?.initialize(this)
	}
	
	override IMappedElementDescriptor<T> getDomainObjectDescriptor() {
		super.domainObjectDescriptor as IMappedElementDescriptor<T>
	}
	
	override protected createNode() {
		val titleArea = new VBox => [
			alignment = Pos.CENTER
		]
		val pane = new RectangleBorderPane => [
			padding = this.insets
			children += new VBox => [
				alignment = Pos.CENTER
				spacing = 10
				children += titleArea
				children += diagramGroup = new Group
			]
		]
		addMappingLabelListener(labels, NODE_HEADING -> titleArea)
		pane	
	}
	
	override doActivate() {
		super.doActivate
		innerDiagramProperty.addInitializingListener(new InitializingListener => [
			set = [ XDiagram newDiagram |
				diagramGroup.children.clear
				newDiagram => [
					isRootDiagram = false
					parentDiagram = this.diagram 	
				]
				diagramGroup.children.add(newDiagram)
				newDiagram.activate							
			]
			unset = [
				diagramGroup.children.clear
			]
		])
		addLazyBehavior(domainObjectDescriptor)
		addBehavior(new de.fxdiagram.mapping.shapes.BaseContainerNode.ReconcileBehavior(this))
		// move container node when the inner diagram grows to the upper/left
		innerDiagram.boundsInLocalProperty.addListener [ p, o, n |
			if(!layoutXProperty.bound && !layoutYProperty.bound) {
				layoutX = layoutX + (n.minX - o.minX)  
				layoutY = layoutY + (n.minY - o.minY)
			}
		]	
	}
	
	override getButtonSides(ConnectionMapping<?> mapping) {
		#[ TOP, BOTTOM, LEFT, RIGHT ]
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}

	override getInsets() {
		new Insets(10,20,20,20)
	}
	
	override toFront() {
		super.toFront()
		(innerDiagram.nodes + innerDiagram.connections).forEach[toFront]
	}
	
	override isInnerDiagramActive() {
		isActive
	}
	
	static class ReconcileBehavior <T> extends NodeReconcileBehavior<T>  {
	
		new(BaseContainerNode<T> host) {
			super(host)
		}
		
		override BaseContainerNode<T> getHost() {
			super.getHost() as BaseContainerNode<T>
		}
		
		override getDirtyState() {
			val dirtyFromSuper = super.dirtyState
			val dirtyFromInnerDiagram = innerDiagramReconcileBehavior?.dirtyState
			if(dirtyFromSuper == CLEAN) {
				return dirtyFromInnerDiagram
			}
		}
	
		override protected reconcile(AbstractMapping<T> mapping, T domainObject, UpdateAcceptor acceptor) {
			super.reconcile(mapping, domainObject, acceptor)
			innerDiagramReconcileBehavior?.reconcile(acceptor)
		}
		
		override hideDirtyState() {
			super.hideDirtyState
			innerDiagramReconcileBehavior?.hideDirtyState
		}
		
		override showDirtyState(DirtyState dirtyState) {
			super.showDirtyState(dirtyState)
			innerDiagramReconcileBehavior?.showDirtyState(innerDiagramReconcileBehavior.dirtyState)
		}
		
		protected def getInnerDiagramReconcileBehavior() {
			host.innerDiagram.getBehavior(de.fxdiagram.core.behavior.ReconcileBehavior)
		}
		
	}
}