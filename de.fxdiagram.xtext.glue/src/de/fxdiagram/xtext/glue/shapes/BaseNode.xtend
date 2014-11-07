package de.fxdiagram.xtext.glue.shapes

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.xtext.glue.behavior.OpenElementInEditorBehavior
import de.fxdiagram.xtext.glue.mapping.AbstractXtextDescriptor
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text

import static extension de.fxdiagram.xtext.glue.behavior.LazyConnectionMappingBehavior.*

@ModelNode
class BaseNode<T> extends XNode {
	
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
		new RectangleBorderPane => [
			children += new Text => [
				textOrigin = VPos.TOP
				text = name
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
				font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
			]
			backgroundPaint = new LinearGradient(
				0, 0, 1, 1, 
				true, CycleMethod.NO_CYCLE,
				#[
					new Stop(0, Color.rgb(158, 188, 227)), 
					new Stop(1, Color.rgb(220, 230, 255))
				])
		] 
	}
	
	override doActivate() {
		super.doActivate
		addLazyBehavior(domainObject)
		addBehavior(new OpenElementInEditorBehavior(this))
	}
}	
