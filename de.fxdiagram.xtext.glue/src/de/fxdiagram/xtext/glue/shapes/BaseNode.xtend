package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.lib.simple.SimpleNode
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop

import static extension de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior.*

@ModelNode
class BaseNode<T> extends SimpleNode {
	
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
	
	
	override protected createNode() {
		val pane = super.createNode() as RectangleBorderPane
		pane.backgroundPaint = new LinearGradient(
			0, 0, 1, 1, 
			true, CycleMethod.NO_CYCLE,
			#[
				new Stop(0, Color.rgb(158, 188, 227)), 
				new Stop(1, Color.rgb(220, 230, 255))
			]) 
		pane
	}
	
	protected def getDescriptor() {
		domainObject as AbstractXtextDescriptor<T>
	}
	
	override doActivate() {
		super.doActivate()
		addLazyBehavior(descriptor)
		addBehavior(new OpenElementInEditorBehavior(this))
	}
}	
