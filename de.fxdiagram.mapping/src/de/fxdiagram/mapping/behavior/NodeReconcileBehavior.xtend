package de.fxdiagram.mapping.behavior

import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractReconcileBehavior
import de.fxdiagram.core.behavior.DirtyState
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.mapping.IMappedElementDescriptor
import java.util.NoSuchElementException

import static de.fxdiagram.core.behavior.DirtyState.*

class NodeReconcileBehavior extends AbstractReconcileBehavior<XNode> {
	
	new(XNode host) {
		super(host)
	}
	
	override DirtyState getDirtyState() {
		val descriptor = host.domainObject 
		if(descriptor instanceof IMappedElementDescriptor<?>) {
			try {
				descriptor.withDomainObject[
				]					
			} catch(NoSuchElementException e) {
				return DANGLING
			}
		}
		return CLEAN 
	}
	
	override reconcile(UpdateAcceptor acceptor) {
		if(dirtyState == DANGLING) 
			 acceptor.delete(host)
	}
}
