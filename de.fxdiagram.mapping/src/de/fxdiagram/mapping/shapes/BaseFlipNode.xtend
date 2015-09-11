package de.fxdiagram.mapping.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import javafx.scene.input.MouseEvent

import static javafx.geometry.Side.*
import static javafx.scene.input.MouseButton.*

import static extension de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior.*
import de.fxdiagram.mapping.behavior.NodeReconcileBehavior

/**
 * Base implementation for a flipable {@link XNode} that belongs to an {@link IMappedElementDescriptor}.
 * 
 * If the descriptor is an {@link AbstractXtextDescriptor}, members are automatically injected using
 * the Xtext language's injector. 
 */
 @ModelNode
class BaseFlipNode<T> extends FlipNode implements INodeWithLazyMappings {

	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}

	override IMappedElementDescriptor<T> getDomainObjectDescriptor() {
		super.domainObjectDescriptor as IMappedElementDescriptor<T> 
	}

	override doActivate() {
		super.doActivate()
		addLazyBehavior(domainObjectDescriptor)
		addBehavior(new NodeReconcileBehavior(this))
	}
	
	override registerOnClick() {
		addEventHandler(MouseEvent.MOUSE_CLICKED) [ 
			if (button == SECONDARY) {
				if (front != null && back != null) {
					flip(isHorizontal(it))
					consume					
				}
			}
		]
	}
	
	override getButtonSides(ConnectionMapping<?> mapping) {
		#[ TOP, BOTTOM, LEFT, RIGHT ]
	}
}
