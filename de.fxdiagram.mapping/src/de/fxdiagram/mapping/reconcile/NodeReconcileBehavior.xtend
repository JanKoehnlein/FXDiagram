package de.fxdiagram.mapping.reconcile

import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.NodeMapping

class NodeReconcileBehavior<T> extends AbstractLabelOwnerReconcileBehavior<T, XNode> {
	
	new(XNode host) {
		super(host)
	}
	
	override protected getExistingLabels() {
		host.labels
	}
	
	override protected getLabelMappingCalls(AbstractMapping<T> mapping) {
		(mapping as NodeMapping<T>).labels
	}
	
	override protected reconcile(AbstractMapping<T> mapping, T domainObject, UpdateAcceptor acceptor) {
		val nodeMorphCommand = new NodeLabelMorphCommand(host)
		compareLabels(mapping, domainObject, nodeMorphCommand)
		if(!nodeMorphCommand.empty)
			acceptor.morph(nodeMorphCommand)
	}
}