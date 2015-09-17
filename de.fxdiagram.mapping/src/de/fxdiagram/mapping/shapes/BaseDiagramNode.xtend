package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.layout.LayoutType
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.behavior.NodeReconcileBehavior
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop

import static de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior.*
import static extension de.fxdiagram.mapping.behavior.MappingLabelListener.*

/**
 * Base implementation for a {@link XNode} with a nested {@link XDiagram} that belongs to an
 * {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector. 
 */
@ModelNode
class BaseDiagramNode<T> extends OpenableDiagramNode {
	
	public static val NODE_HEADING = 'diagramNodeHeading'
	 
	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}

	override IMappedElementDescriptor<T> getDomainObjectDescriptor() {
		super.domainObjectDescriptor as IMappedElementDescriptor<T>
	}

	override protected createNode() {
		pane => [
			backgroundPaint = new LinearGradient(
				0, 0, 1, 1, 
				true, CycleMethod.NO_CYCLE,
				#[
					new Stop(0, Color.rgb(242,236,181)), 
					new Stop(1, Color.rgb(255,248,202))
				]) 
		]
		addMappingListener(labels, NODE_HEADING -> pane)	
		pane
	}

	override doActivate() {
		super.doActivate()
		addLazyBehavior(this, domainObjectDescriptor)
		addBehavior(new NodeReconcileBehavior(this))
		innerDiagram.layoutOnActivate = LayoutType.DOT
	}
}