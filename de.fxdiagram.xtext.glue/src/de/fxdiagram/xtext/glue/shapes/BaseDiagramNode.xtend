package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop

@ModelNode
class BaseDiagramNode<T> extends OpenableDiagramNode {
	
	new() {
		domainObjectProperty.addListener [
			prop, oldVal, newVal |
			if(newVal instanceof AbstractXtextDescriptor<?>)
				newVal.injectMembers(this)
		]
	}
	
	new(AbstractXtextDescriptor<T> descriptor) {
		super(descriptor)
		descriptor.injectMembers(this)
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

	protected def getDescriptor() {
		domainObject as AbstractXtextDescriptor<T>
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
		addBehavior(new OpenElementInEditorBehavior(this))
		innerDiagram.isLayoutOnActivate = true
	}
	
}