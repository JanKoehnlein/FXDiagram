package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import static extension de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior.*

class BaseFlipNode<T> extends FlipNode {

	new() {
		domainObjectProperty.addListener [ prop, oldVal, newVal |
			if (newVal instanceof AbstractXtextDescriptor<?>)
				newVal.injectMembers(this)
		]
	}

	new(AbstractXtextDescriptor<T> descriptor) {
		super(descriptor)
		descriptor.injectMembers(this)
	}

	protected def getDescriptor() {
		domainObject as AbstractXtextDescriptor<T>
	}

	override doActivate() {
		super.doActivate()
		addLazyBehavior(descriptor)
		addBehavior(new OpenElementInEditorBehavior(this))
	}
}
