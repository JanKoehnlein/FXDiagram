package de.fxdiagram.eclipse.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.eclipse.behavior.LazyConnectionMappingBehavior
import de.fxdiagram.eclipse.behavior.OpenElementInEditorBehavior
import de.fxdiagram.eclipse.mapping.AbstractXtextDescriptor
import de.fxdiagram.eclipse.mapping.IMappedElementDescriptor
import de.fxdiagram.eclipse.mapping.NodeMapping
import de.fxdiagram.eclipse.mapping.XDiagramConfigInterpreter
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop

@ModelNode
class BaseDiagramNode<T> extends OpenableDiagramNode {
	
	new() {
		domainObjectProperty.addListener [
			prop, oldVal, newVal |
			injectMembers
		]
	}
	
	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
		injectMembers()
	}

	protected def injectMembers() {
		val descriptor = domainObject
		if(descriptor instanceof AbstractXtextDescriptor<?>)
			descriptor.injectMembers(this)
	}
	
	override getDomainObject() {
		super.getDomainObject() as IMappedElementDescriptor<T>
	}

	override initializeGraphics() {
		super.initializeGraphics()
		pane.backgroundPaint = new LinearGradient(
			0, 0, 1, 1, 
			true, CycleMethod.NO_CYCLE,
			#[
				new Stop(0, Color.rgb(242,236,181)), 
				new Stop(1, Color.rgb(255,248,202))
			]) 
	}

	override doActivate() {
		super.doActivate()
		val descriptor = domainObject
		if(descriptor instanceof IMappedElementDescriptor<?>) {
			if(descriptor.mapping instanceof NodeMapping<?>) {
				val nodeMapping = descriptor.mapping as NodeMapping<T>
				var LazyConnectionMappingBehavior<T> lazyBehavior = null 
				val lazyOutgoing = nodeMapping.outgoing.filter[lazy]
				if(!lazyOutgoing.empty) {
					lazyBehavior = lazyBehavior ?: new LazyConnectionMappingBehavior<T>(this)
					for(out : lazyOutgoing) 
						lazyBehavior.addConnectionMappingCall(out, new XDiagramConfigInterpreter, true)
				}
				val lazyIncoming = nodeMapping.incoming.filter[lazy]
				if(!lazyIncoming.empty) {
					lazyBehavior = lazyBehavior ?: new LazyConnectionMappingBehavior<T>(this)
					for(in : lazyIncoming) 
						lazyBehavior.addConnectionMappingCall(in, new XDiagramConfigInterpreter, false)
				}
				if(lazyBehavior != null)
					addBehavior(lazyBehavior)
			}
		}
		addBehavior(new OpenElementInEditorBehavior(this))
		innerDiagram.isLayoutOnActivate = true
	}
	
}