package de.fxdiagram.mapping.shapes

import de.fxdiagram.lib.nodes.AbstractClassNode
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import javafx.scene.input.MouseEvent

import static javafx.geometry.Side.*
import static javafx.scene.input.MouseButton.*

import static extension de.fxdiagram.mapping.behavior.LazyConnectionMappingBehavior.*
import static extension de.fxdiagram.mapping.shapes.BaseShapeInitializer.*

abstract class BaseClassNode<T> extends AbstractClassNode implements INodeWithLazyMappings {

	new() {
		initializeLazily
	}

	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}

	override IMappedElementDescriptor<T> getDomainObjectDescriptor() {
		super.domainObjectDescriptor as IMappedElementDescriptor<T>
	}

	override doActivate() {
		super.doActivate()
		addLazyBehavior(domainObjectDescriptor)
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