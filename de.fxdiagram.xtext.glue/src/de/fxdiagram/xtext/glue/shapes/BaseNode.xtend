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
	
	new() {
		domainObjectProperty.addListener [
			prop, oldVal, newVal |
			if(newVal instanceof XtextDomainObjectDescriptor<?>)
				newVal.injectMembers(this)
		]
	}
	
	new(XtextDomainObjectDescriptor<T> descriptor) {
		super(descriptor)
		descriptor.injectMembers(this)
	}

	protected def getDescriptor() {
		domainObject as XtextDomainObjectDescriptor<T>
	}
	
	override doActivate() {
		super.doActivate()
		if(descriptor.mapping instanceof NodeMapping<?>) {
			val nodeMapping = descriptor.mapping as NodeMapping<T>
			var LazyConnectionMappingBehavior<T> lazyBehavior = null 
			val lazyOutgoing = nodeMapping.outgoing.filter[lazy]
			if(!lazyOutgoing.empty) {
				lazyBehavior = lazyBehavior ?: new LazyConnectionMappingBehavior<T>(this)
				for(out : lazyOutgoing) 
					lazyBehavior.addConnectionMappingCall(out, new XDiagramConfigInterpreter(descriptor.provider), true)
			}
			val lazyIncoming = nodeMapping.incoming.filter[lazy]
			if(!lazyIncoming.empty) {
				lazyBehavior = lazyBehavior ?: new LazyConnectionMappingBehavior<T>(this)
				for(in : lazyIncoming) 
					lazyBehavior.addConnectionMappingCall(in, new XDiagramConfigInterpreter(descriptor.provider), false)
			}
			if(lazyBehavior != null)
				addBehavior(lazyBehavior)
		}
		addBehavior(new OpenElementInEditorBehavior(this))
	}
}	
