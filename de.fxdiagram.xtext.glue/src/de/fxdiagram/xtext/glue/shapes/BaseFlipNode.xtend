package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor

import static extension de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior.*

class BaseFlipNode<T> extends FlipNode {

	new() {
		domainObjectProperty.addListener [ 
			prop, oldVal, newVal |
			injectMembers
		]
	}

	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
		injectMembers
	}

	protected def injectMembers() {
		val domainObject = getDomainObject
		if(domainObject instanceof AbstractXtextDescriptor<?>)
			domainObject.injectMembers(this)
	}

	override IMappedElementDescriptor<T> getDomainObject() {
		super.getDomainObject() as IMappedElementDescriptor<T> 
	}

	override doActivate() {
		super.doActivate()
		addLazyBehavior(domainObject)
		addBehavior(new OpenElementInEditorBehavior(this))
	}
}
