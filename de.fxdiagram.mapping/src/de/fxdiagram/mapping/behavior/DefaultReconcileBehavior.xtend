package de.fxdiagram.mapping.behavior

import de.fxdiagram.core.XDomainObjectShape
import de.fxdiagram.core.behavior.AbstractReconcileBehavior
import de.fxdiagram.core.behavior.DirtyState
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.mapping.IMappedElementDescriptor
import java.util.NoSuchElementException

import static de.fxdiagram.core.behavior.DirtyState.*

class DefaultReconcileBehavior<T extends XDomainObjectShape> extends AbstractReconcileBehavior<T> {
	
	new(T host) {
		super(host)
	}
	
	override DirtyState getDirtyState() {
		val descriptor = host.domainObjectDescriptor 
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
