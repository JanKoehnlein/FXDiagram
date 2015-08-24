package de.fxdiagram.mapping.shapes

import de.fxdiagram.lib.nodes.AbstractClassNode
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import javafx.scene.input.MouseEvent
import static extension de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior.*
import static javafx.geometry.Side.*
import static javafx.scene.input.MouseButton.*

abstract class BaseClassNode<T> extends AbstractClassNode implements INodeWithLazyMappings {

	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}

	override IMappedElementDescriptor<T> getDomainObject() {
		super.getDomainObject() as IMappedElementDescriptor<T>
	}

	override doActivate() {
		super.doActivate()
		addLazyBehavior(domainObject)
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
		#[TOP, BOTTOM, LEFT, RIGHT]
	}
}