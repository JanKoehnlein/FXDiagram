package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.simple.SimpleNode
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter

@ModelNode
class BaseNode<T> extends SimpleNode {
	
	new(XtextDomainObjectDescriptor<T> descriptor) {
		super(descriptor)
	}

	protected def getDescriptor() {
		domainObject as XtextDomainObjectDescriptor<T>
	}
	
	override doActivate() {
		super.doActivate()
		if(descriptor.mapping instanceof NodeMapping<?>) {
			val nodeMapping = descriptor.mapping as NodeMapping<T>
			nodeMapping.outgoing.filter[lazy].forEach[
				addBehavior(new LazyConnectionMappingBehavior(this, it, new XDiagramConfigInterpreter(descriptor.provider), true))
			]
			nodeMapping.incoming.filter[lazy].forEach[
				addBehavior(new LazyConnectionMappingBehavior(this, it, new XDiagramConfigInterpreter(descriptor.provider), false))
			]
		}
		addBehavior(new OpenElementInEditorBehavior(this))
	}
}	
