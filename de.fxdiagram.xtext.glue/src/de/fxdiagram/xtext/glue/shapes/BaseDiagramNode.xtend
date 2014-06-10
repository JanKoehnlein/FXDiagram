package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.simple.OpenableDiagramNode
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.NodeMapping
import de.fxdiagram.xtext.glue.mapping.XDiagramConfigInterpreter
import javafx.scene.paint.Color

@ModelNode
class BaseDiagramNode<T> extends OpenableDiagramNode {
	
	new() {
		domainObjectProperty.addListener [
			prop, oldVal, newVal |
			if(newVal instanceof XtextDomainObjectDescriptor<?>)
				newVal.injectMembers(this)
		]
	}
	
	new(XtextDomainObjectDescriptor<T> descriptor) {
		super(descriptor)
		descriptor.injectMembers(this)
	}

	override initializeGraphics() {
		super.initializeGraphics()
		pane.backgroundPaint = Color.BLANCHEDALMOND
		pane.borderRadius = 0
		pane.backgroundRadius = 0
	}

	protected def getDescriptor() {
		domainObject as XtextDomainObjectDescriptor<T>
	}
	
	override doActivate() {
		super.doActivate()
		if(descriptor.mapping instanceof NodeMapping<?>) {
			val nodeMapping = descriptor.mapping as NodeMapping<T>
			nodeMapping.outgoing.filter[lazy].forEach[
				addBehavior(new LazyConnectionMappingBehavior(this, it, new XDiagramConfigInterpreter(descriptor.provider), true))
			]
			nodeMapping.incoming.filter[lazy].forEach[
				addBehavior(new LazyConnectionMappingBehavior(this, it, new XDiagramConfigInterpreter(descriptor.provider), false))
			]
		}
		addBehavior(new OpenElementInEditorBehavior(this))
		innerDiagram.isLayoutOnActivate = true
	}
	
}