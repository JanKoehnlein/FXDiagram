package de.fxdiagram.mapping.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractReconcileBehavior
import de.fxdiagram.core.behavior.DirtyState
import de.fxdiagram.core.behavior.ReconcileBehavior
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.mapping.AbstractConnectionMappingCall
import de.fxdiagram.mapping.AbstractLabelMappingCall
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.InterpreterContext
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.NodeMappingCall
import de.fxdiagram.mapping.XDiagramConfigInterpreter
import java.util.NoSuchElementException
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline

import static de.fxdiagram.core.behavior.DirtyState.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class ConnectionReconcileBehavior<T> extends AbstractReconcileBehavior<XConnection> {

	Animation dirtyAnimation
	
	@FxProperty double dirtyAnimationValue
	
	new(XConnection host) {
		super(host)
	}

	override showDirtyState(DirtyState dirtyState) {
		super.showDirtyState(dirtyState)
		host.labels.forEach [
			val descriptor = host.domainObjectDescriptor
			if(descriptor instanceof IMappedElementDescriptor<?>) {
				(descriptor as IMappedElementDescriptor<T>).withDomainObject [ domainObject |
					val connectionMapping = descriptor.mapping as ConnectionMapping<T>
					compareLabels(connectionMapping, domainObject, new AddRemoveAcceptor {
						override add(XConnectionLabel label) {
						}
						
						override remove(XConnectionLabel label) {
							val behavior = label.getBehavior(ReconcileBehavior)
							if(behavior != null) {
								switch behavior.dirtyState {
									case DANGLING:
										behavior.showDirtyState(DANGLING)
									default:
										behavior.showDirtyState(DIRTY)
								}
							}
						}
						
						override keep(XConnectionLabel label) {
							label.getBehavior(ReconcileBehavior)?.showDirtyState(CLEAN)
						}
					})
					null		
				]
			}
		]
	}

	override hideDirtyState() {
		super.hideDirtyState()
		host.labels.forEach [
			getBehavior(ReconcileBehavior)?.hideDirtyState
		]
	}

	override getDirtyState() {
		val descriptor = host.domainObjectDescriptor
		if (descriptor instanceof IMappedElementDescriptor<?>) {
			try {
				return (descriptor as IMappedElementDescriptor<T>).withDomainObject [ domainObject |
					val connectionMapping = descriptor.mapping as ConnectionMapping<T>
					val resolvedSourceDescriptor = resolveConnectionEnd(domainObject as T, connectionMapping, host.source.domainObjectDescriptor, true)
					if (resolvedSourceDescriptor == host.source.domainObjectDescriptor) {
						val resolvedTarget = resolveConnectionEnd(domainObject as T, connectionMapping, host.target.domainObjectDescriptor, false)
						if(resolvedTarget == host.target.domainObjectDescriptor) {
							val toBeAdded = newArrayList
							compareLabels(connectionMapping, domainObject, new AddRemoveAcceptor {
								override add(XConnectionLabel label) {
									toBeAdded.add(label)
								}
								
								override remove(XConnectionLabel label) {
								}
								
								override keep(XConnectionLabel label) {
								}
							})
							if(!toBeAdded.empty)
								return DIRTY
							else
								return CLEAN
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
		val interpreter = new XDiagramConfigInterpreter
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
				val nodeMapping = nodeDescriptor.mapping
				if (nodeMapping instanceof NodeMapping<?>) {
					val nodeMappingCasted = nodeMapping as NodeMapping<U>
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
	
	override protected doActivate() {
		dirtyAnimation = new Timeline => [
			keyFrames += new KeyFrame(0.millis, new KeyValue(dirtyAnimationValueProperty, 1))
			keyFrames += new KeyFrame(300.millis, new KeyValue(dirtyAnimationValueProperty, 0.2))
			keyFrames += new KeyFrame(900.millis, new KeyValue(dirtyAnimationValueProperty, 1))
			cycleCount = Animation.INDEFINITE
		]
	}
	
	override protected dirtyFeedback(boolean isDirty) {
		if(isDirty) {
			#[host.sourceArrowHead?.node, host.targetArrowHead?.node]
				.filterNull
				.forEach[ 
					opacityProperty.bind(dirtyAnimationValueProperty)
				]
			dirtyAnimation.play
		} else {
			#[host.sourceArrowHead?.node, host.targetArrowHead?.node]
				.filterNull
				.forEach[ 
					opacityProperty.unbind
					opacity = 1
				]	
			dirtyAnimation.stop
		}
	}
	
	override reconcile(UpdateAcceptor acceptor) {
		val descriptor = host.domainObjectDescriptor
		if (descriptor instanceof IMappedElementDescriptor<?>) {
			try {
				(descriptor as IMappedElementDescriptor<T>).withDomainObject [ domainObject |
					val connectionMapping = descriptor.mapping as ConnectionMapping<T>
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
					null
				]
			} catch (NoSuchElementException exc) {
				acceptor.delete(host)
			}
		} 
	}
	
	protected def compareLabels(ConnectionMapping<T> connectionMapping, T domainObject, AddRemoveAcceptor acceptor) {
		val interpreter = new XDiagramConfigInterpreter
		val context = new InterpreterContext(host.diagram)
		connectionMapping.labels.forEach [ labelMappingCall |
			val resolvedLabels = interpreter.execute(labelMappingCall as AbstractLabelMappingCall<?,T>, domainObject, context).filter(XConnectionLabel)
			val existingLabels = host.labels
			existingLabels.forEach[ existing |
				if(!resolvedLabels.exists[equals(it, existing)]) {
					acceptor.remove(existing)
				} else {
					acceptor.keep(existing)
				} 
			]
			resolvedLabels.forEach[ resolved |
				if(!existingLabels.exists[equals(it, resolved)]) {
					acceptor.add(resolved)
				} 
			]
		]
	}
	
	def boolean equals(XConnectionLabel one, XConnectionLabel two) {
		one.domainObjectDescriptor == two.domainObjectDescriptor
			&& one.text.text == two.text.text
	}
	
	protected def findNode(DomainObjectDescriptor descriptor) {
		host.diagram.allChildren.filter(XNode).findFirst[domainObjectDescriptor == descriptor]
	}
}

interface AddRemoveAcceptor {
	def void add(XConnectionLabel label)
	
	def void remove(XConnectionLabel label)

	def void keep(XConnectionLabel label)
}

