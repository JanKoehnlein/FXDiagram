package de.fxdiagram.mapping.reconcile

import de.fxdiagram.core.XLabel
import de.fxdiagram.core.behavior.AbstractReconcileBehavior
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.mapping.IMappedElementDescriptor
import java.util.NoSuchElementException
import static de.fxdiagram.core.behavior.DirtyState.*

class LabelReconcileBehavior extends AbstractReconcileBehavior<XLabel> {
	
	new(XLabel host) {
		super(host)
	}
	
	override getDirtyState() {
		val descriptor = host.domainObjectDescriptor
		if(descriptor instanceof IMappedElementDescriptor<?>) {
			try {
				descriptor.withDomainObject[]
			} catch(NoSuchElementException e) {
				return DANGLING	
			}
		}
		return CLEAN
	}
	
	override reconcile(UpdateAcceptor acceptor) {
	}
}