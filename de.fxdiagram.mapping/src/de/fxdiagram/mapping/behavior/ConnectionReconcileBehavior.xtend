package de.fxdiagram.mapping.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.behavior.AbstractReconcileBehavior
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.mapping.AbstractConnectionMappingCall
import de.fxdiagram.mapping.ConnectionMapping
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.NodeMapping
import de.fxdiagram.mapping.NodeMappingCall
import de.fxdiagram.mapping.XDiagramConfigInterpreter
import java.util.NoSuchElementException
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline

import static de.fxdiagram.core.behavior.DirtyState.*

import static extension de.fxdiagram.core.extensions.DoubleExpressionExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class ConnectionReconcileBehavior<T> extends AbstractReconcileBehavior<XConnection> {

	Animation dirtyAnimation
	
	@FxProperty double dirtyAnimationValue
	
	double strokeWidth
	
	new(XConnection host) {
		super(host)
	}

	override getDirtyState() {
		val descriptor = host.domainObjectDescriptor
		if (descriptor instanceof IMappedElementDescriptor<?>) {
			try {
				return descriptor.withDomainObject [ domainObject |
					val connectionMapping = descriptor.mapping as ConnectionMapping<T>
					val resolvedSourceDescriptor = resolveConnectionEnd(domainObject as T, connectionMapping, host.source.domainObjectDescriptor, true)
					if (resolvedSourceDescriptor == host.source.domainObjectDescriptor) {
						val resolvedTarget = resolveConnectionEnd(domainObject as T, connectionMapping, host.target.domainObjectDescriptor, false)
						if(resolvedTarget == host.target.domainObjectDescriptor)								
							return CLEAN
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
			keyFrames += new KeyFrame(300.millis, new KeyValue(dirtyAnimationValueProperty, 0.96))
			keyFrames += new KeyFrame(900.millis, new KeyValue(dirtyAnimationValueProperty, 1.04))
			keyFrames += new KeyFrame(1200.millis, new KeyValue(dirtyAnimationValueProperty, 1))
			autoReverse = true
			cycleCount = Animation.INDEFINITE
		]
	}
	
	override protected dirtyFeedback(boolean isDirty) {
		if(isDirty) {
			strokeWidth = host.strokeWidth;
			(host.labels + #[host.sourceArrowHead?.node, host.targetArrowHead?.node])
				.filterNull
				.forEach[ 
					scaleXProperty.bind(dirtyAnimationValueProperty)
					scaleYProperty.bind(dirtyAnimationValueProperty)
				]
			host.strokeWidthProperty.bind((dirtyAnimationValueProperty - 1) * 40 + strokeWidth) 
			dirtyAnimation.play
		} else {
			(host.labels + #[host.sourceArrowHead?.node, host.targetArrowHead?.node])
				.filterNull
				.forEach[ 
					scaleXProperty.unbind
					scaleYProperty.unbind
					scaleX = 1
					scaleY = 1
				]	
			host.strokeWidthProperty.unbind
			host.strokeWidth = strokeWidth
			dirtyAnimation.stop
		}
	}
	
	override reconcile(UpdateAcceptor acceptor) {
		val descriptor = host.domainObjectDescriptor
		if (descriptor instanceof IMappedElementDescriptor<?>) {
			try {
				descriptor.withDomainObject [ domainObject |
					val connectionMapping = descriptor.mapping as ConnectionMapping<T>
					val resolvedSourceDescriptor = resolveConnectionEnd(domainObject as T, connectionMapping, host.source.domainObjectDescriptor, true)
					if (resolvedSourceDescriptor != host.source.domainObjectDescriptor) {
						// TODO: return reconnect source command
						acceptor.delete(host)
					} else {
						val resolvedTarget = resolveConnectionEnd(domainObject as T, connectionMapping, host.target.domainObjectDescriptor, false)
						if(resolvedTarget != host.target.domainObjectDescriptor)
							// TODO: return reconnect target command
							acceptor.delete(host)
					}
					null
				]
			} catch (NoSuchElementException exc) {
				acceptor.delete(host)
			}
		} 
	}
}