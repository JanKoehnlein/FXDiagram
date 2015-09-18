package de.fxdiagram.mapping.reconcile

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.mapping.AbstractConnectionMappingCall
import de.fxdiagram.mapping.AbstractMapping
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.NodeMappingCall
import java.util.NoSuchElementException

import static de.fxdiagram.core.behavior.DirtyState.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class ConnectionReconcileBehavior<T> extends AbstractLabelOwnerReconcileBehavior<T, XConnection> {
	
	new(XConnection host) {
		super(host)
	}

	override getDirtyState() {
		if (host.domainObjectDescriptor instanceof IMappedElementDescriptor<?>) {
			try {
				val descriptor = host.domainObjectDescriptor as IMappedElementDescriptor<T>
				return descriptor.withDomainObject [ domainObject |
					val connectionMapping = descriptor.mapping as ConnectionMapping<T>
					val resolvedSourceDescriptor = resolveConnectionEnd(domainObject as T, connectionMapping, host.source.domainObjectDescriptor, true)
					if (resolvedSourceDescriptor == host.source.domainObjectDescriptor) {
						val resolvedTarget = resolveConnectionEnd(domainObject as T, connectionMapping, host.target.domainObjectDescriptor, false)
						if(resolvedTarget == host.target.domainObjectDescriptor) {
							return CLEAN //connectionMapping.getLabelsDirtyState(domainObject)
						}
					}
					return DIRTY
				]
			} catch (NoSuchElementException exc) {
				return DANGLING
			}
		} else {
			return CLEAN
		}
	}

	protected def <U> resolveConnectionEnd(T domainObject, ConnectionMapping<T> connectionMapping, DomainObjectDescriptor nodeDescriptor, boolean isSource) {
		val nodeMappingCall = (if(isSource) connectionMapping.source else connectionMapping.target) as NodeMappingCall<U, T>
		if (nodeMappingCall != null) {
			val nodeObject = interpreter.select(nodeMappingCall, domainObject).head as U
			if (nodeObject == null)
				return null
			val resolvedNodeDescriptor = interpreter.getDescriptor(nodeObject, nodeMappingCall.mapping)
			if (resolvedNodeDescriptor != nodeDescriptor)
				return resolvedNodeDescriptor
			else
				return nodeDescriptor
		} else {
			if (nodeDescriptor instanceof IMappedElementDescriptor<?>) {
				if (nodeDescriptor.mapping instanceof NodeMapping<?>) {
					val nodeMappingCasted = nodeDescriptor.mapping as NodeMapping<U>
					val siblingMappingCalls = if(isSource) 
							nodeMappingCasted
								.outgoing
								.filter[ mapping == connectionMapping ]
								.map[it as AbstractConnectionMappingCall<T, U>]
						else
							nodeMappingCasted
								.incoming
								.filter[ mapping == connectionMapping ]
								.map[it as AbstractConnectionMappingCall<T, U>]
					return nodeDescriptor.withDomainObject [ nodeDomainObject |
						val nodeObjectCasted = nodeDomainObject as U
						for (siblingMappingCall : siblingMappingCalls) {
							val siblingDescriptors = interpreter
								.select(siblingMappingCall, nodeObjectCasted)
								.map[interpreter.getDescriptor(it, siblingMappingCall.mapping)].toSet
							if (siblingDescriptors.contains(host.domainObjectDescriptor))
								return nodeDescriptor
						}
						return null
					]
				}
			}
		}				
		return null
	}
	
	override protected reconcile(AbstractMapping<T> mapping, T domainObject, UpdateAcceptor acceptor) {
		val connectionMapping = mapping as ConnectionMapping<T>
		val resolvedSourceDescriptor = resolveConnectionEnd(domainObject, connectionMapping, host.source.domainObjectDescriptor, true)
		if (resolvedSourceDescriptor != host.source.domainObjectDescriptor) {
			val newSource = resolvedSourceDescriptor.findNode
			if(newSource != null) 
				acceptor.morph(new ReconnectMorphCommand(host, host.source, newSource, true))
			else
				acceptor.delete(host)
		} else {
			val resolvedTarget = resolveConnectionEnd(domainObject, connectionMapping, host.target.domainObjectDescriptor, false)
			if(resolvedTarget != host.target.domainObjectDescriptor) {
				val newTarget = resolvedTarget.findNode
				if(newTarget != null) 
					acceptor.morph(new ReconnectMorphCommand(host, host.target, newTarget, false))
				else
					acceptor.delete(host)
			}
		}
		val labelMorphCommand = new ConnectionLabelMorphCommand(host)
		compareLabels(connectionMapping, domainObject, labelMorphCommand)
		if(!labelMorphCommand.empty)
			acceptor.morph(labelMorphCommand)
	}
		
	protected def findNode(DomainObjectDescriptor descriptor) {
		host.diagram.allChildren.filter(XNode).findFirst[domainObjectDescriptor == descriptor]
	}
	
	override protected getExistingLabels() {
		host.labels
	}
	
	override protected getLabelMappingCalls(AbstractMapping<T> mapping) {
		(mapping as ConnectionMapping<T>).labels
	}
}


