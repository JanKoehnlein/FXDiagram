package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.lib.simple.SimpleNode
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor
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
			injectMembers
		]
	}
	
	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
		injectMembers
	}
	
	protected def injectMembers() {
		val domainObject = getDomainObject
		if(domainObject instanceof AbstractXtextDescriptor<?>)
			domainObject.injectMembers(this)
	}
	
	override IMappedElementDescriptor<T> getDomainObject() {
		super.getDomainObject() as IMappedElementDescriptor<T>
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
	
	override doActivate() {
		super.doActivate()
		addLazyBehavior(domainObject)
		addBehavior(new OpenElementInEditorBehavior(this))
	}
}	
