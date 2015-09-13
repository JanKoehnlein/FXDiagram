package de.fxdiagram.mapping.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.behavior.AbstractReconcileBehavior
import de.fxdiagram.core.behavior.UpdateAcceptor
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.command.EmptyTransition
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
import javafx.animation.PathTransition
import javafx.animation.SequentialTransition
import javafx.animation.Timeline
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.Path
import javafx.util.Duration
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static de.fxdiagram.core.behavior.DirtyState.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.scene.shape.Circle

class ConnectionReconcileBehavior<T> extends AbstractReconcileBehavior<XConnection> {

	Animation dirtyAnimation
	
	@FxProperty double dirtyAnimationValue
	
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
			keyFrames += new KeyFrame(300.millis, new KeyValue(dirtyAnimationValueProperty, 0.2))
			keyFrames += new KeyFrame(900.millis, new KeyValue(dirtyAnimationValueProperty, 1))
			cycleCount = Animation.INDEFINITE
		]
	}
	
	override protected dirtyFeedback(boolean isDirty) {
		if(isDirty) {
			(host.labels + #[host.sourceArrowHead?.node, host.targetArrowHead?.node])
				.filterNull
				.forEach[ 
					opacityProperty.bind(dirtyAnimationValueProperty)
				]
			dirtyAnimation.play
		} else {
			(host.labels + #[host.sourceArrowHead?.node, host.targetArrowHead?.node])
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
				descriptor.withDomainObject [ domainObject |
					val connectionMapping = descriptor.mapping as ConnectionMapping<T>
					val resolvedSourceDescriptor = resolveConnectionEnd(domainObject as T, connectionMapping, host.source.domainObjectDescriptor, true)
					if (resolvedSourceDescriptor != host.source.domainObjectDescriptor) {
						val newSource = resolvedSourceDescriptor.findNode
						if(newSource != null) 
							acceptor.morph(new ReconnectMorphCommand(host, host.source, newSource, true))
						else
							acceptor.delete(host)
					} else {
						val resolvedTarget = resolveConnectionEnd(domainObject as T, connectionMapping, host.target.domainObjectDescriptor, false)
						if(resolvedTarget != host.target.domainObjectDescriptor) {
							val newTarget = resolvedTarget.findNode
							if(newTarget != null) 
								acceptor.morph(new ReconnectMorphCommand(host, host.target, newTarget, false))
							else
								acceptor.delete(host)
						}
					}
					null
				]
			} catch (NoSuchElementException exc) {
				acceptor.delete(host)
			}
		} 
	}
	
	protected def findNode(DomainObjectDescriptor descriptor) {
		host.diagram.allChildren.filter(XNode).findFirst[domainObjectDescriptor == descriptor]
	}
}

@FinalFieldsConstructor
class ReconnectMorphCommand extends AbstractAnimationCommand {
	val XConnection connection
	val XNode oldNode 
	val XNode newNode 
	val boolean isSource
	
	override createExecuteAnimation(CommandContext context) {
		morph(newNode, context.defaultExecuteDuration)
	}
	
	override createUndoAnimation(CommandContext context) {
		morph(oldNode, context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		morph(newNode, context.defaultUndoDuration)
	}
	
	protected def morph(XNode nodeAfterMorph, Duration duration) {
		val from = if(isSource)
				connection.connectionRouter.findClosestSourceAnchor(connection.source, false)
			else
				connection.connectionRouter.findClosestTargetAnchor(connection.target, false)
		val to = if(isSource)
				connection.connectionRouter.findClosestSourceAnchor(nodeAfterMorph, false)
			else  
				connection.connectionRouter.findClosestTargetAnchor(nodeAfterMorph, false)
		val dummy = new Group => [
			translateX = from.x
			translateY = from.y
		]
		val dummyNode = new XNode () {
			override protected createNode() {
				new Circle(0)
			}
		} => [
			layoutXProperty.bind(dummy.translateXProperty)
			layoutYProperty.bind(dummy.translateYProperty)
		]
		new SequentialTransition => [
			children += new EmptyTransition => [
				onFinished = [
					connection.diagram.nodes += dummyNode 
					if(isSource)
						connection.source = dummyNode
					else 
						connection.target = dummyNode
				]
			]
			children += createPathTransition(from, to, dummyNode, dummy, nodeAfterMorph, duration)
		]
	}
	
	protected def createPathTransition(Point2D from, Point2D to, XNode dummyNode, Node dummy, XNode nodeAfterMorph, Duration duration) {
		new PathTransition => [
			node = dummy
			it.duration = duration
			cycleCount = 1
			path = new Path => [
				elements += new MoveTo(from.x, from.y)
				elements += new LineTo(to.x, to.y)
			]
			onFinished = [
				if(isSource)
					connection.source = nodeAfterMorph
				else
					connection.target = nodeAfterMorph
				connection.diagram.nodes -= dummyNode 
			]
		]
	}
	
}