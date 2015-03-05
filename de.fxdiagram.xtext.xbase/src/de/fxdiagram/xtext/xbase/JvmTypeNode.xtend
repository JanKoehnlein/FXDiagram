package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.eclipse.shapes.BaseFlipNode
import de.fxdiagram.lib.animations.Inflator
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.beans.property.BooleanProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.control.CheckBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.Label
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.xbase.validation.UIStrings

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

@ModelNode("showPackage", "showAttributes", "showMethods", "bgColor")
class JvmTypeNode extends BaseFlipNode<JvmDeclaredType> {

	@FxProperty boolean showPackage = false
	@FxProperty boolean showAttributes = true
	@FxProperty boolean showMethods = true
	@FxProperty Color bgColor = Color.web('#ffe6cc')
	
	@Inject extension JvmDomainUtil 
	@Inject extension UIStrings
	
	CheckBox packageBox
	CheckBox attributesBox
	CheckBox methodsBox
	
	Pane contentArea
	
	new(JvmEObjectDescriptor<JvmDeclaredType> descriptor) {
		super(descriptor)
	}
	
	override JvmEObjectDescriptor<JvmDeclaredType> getDomainObject() {
		super.getDomainObject as JvmEObjectDescriptor<JvmDeclaredType>
	}
	
	override createNode() {
		val pane = super.createNode
		front = new RectangleBorderPane => [
			tooltip = 'Right-click to configure'
			backgroundPaintProperty.bind(bgColorProperty)
			children += contentArea = new VBox => [
				padding = new Insets(10, 20, 10, 20)
				children += new VBox => [
					alignment = Pos.CENTER
					children += new Text => [
						textOrigin = VPos.TOP
						text = name
						font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
					]
				]
			]
		]
		back = new RectangleBorderPane => [
			tooltip = 'Right-click to show node'
			backgroundPaintProperty.bind(bgColorProperty)
			children += new VBox => [
				padding = new Insets(10, 20, 10, 20)
				spacing = 5
				children += new Label(URI.createURI(domainObject.uri).lastSegment)
				children += packageBox = new CheckBox('Package')
				children += attributesBox = new CheckBox('Attributes')
				children += methodsBox = new CheckBox('Methods')
				children += new ColorPicker => [ 
					valueProperty.bindBidirectional(bgColorProperty)
				]
			]			
		]
		pane
	}
	
	override doActivate() {
		super.doActivate()
		val inflator = new Inflator(this, contentArea)
		val packageLabel = new VBox => [
			alignment = Pos.CENTER
			children += new Text => [
				textOrigin = VPos.TOP
				font = Font.font(font.family, font.size * 0.8)
				val lastIndexOf = domainObject.fqn.lastIndexOf('.')
				text = if(lastIndexOf != -1) 
						domainObject.fqn.substring(0, lastIndexOf)
					else
						'<default>'
			]
		]
		packageLabel.activate(showPackageProperty, packageBox, [ 0 ], inflator)
		
		val attributeCompartment = new VBox => [ c |
			c.padding = new Insets(10,0,0,0)
			domainObject.withDomainObject[ type |
				type.attributes.forEach[ field |
					c.children += new Text => [
						textOrigin = VPos.TOP
						text = '''«field.simpleName»: «field.type.simpleName»'''
						tooltip = field.signature
					]
				]
				null
			]
		]
		attributeCompartment.activate(showAttributesProperty, attributesBox, [if(showPackage) 2 else 1], inflator)
		
		val methodCompartment = new VBox => [ c |
			c.padding = new Insets(10,0,0,0)
			domainObject.withDomainObject[ type |
				type.methods.forEach[ operation |
					c.children += new Text => [
						textOrigin = VPos.TOP
						text = '''«operation.simpleName»: «operation.returnType.simpleName»'''
						tooltip = operation.signature
					]
				]
				null
			]
		]
		methodCompartment.activate(showMethodsProperty, methodsBox, [contentArea.children.size], inflator)
		inflator.inflateAnimation?.play
	}
	
	protected def activate(VBox compartment, BooleanProperty showProperty, CheckBox box, ()=>int index, Inflator inflator) {
		showProperty.bindCheckbox(box, compartment, index, inflator)
		if(showProperty.get) 
			inflator.addInflatable(compartment, index.apply)
	}
	
	protected def bindCheckbox(BooleanProperty property, CheckBox box, VBox compartment, ()=>int index, Inflator inflator) {
		box.selectedProperty.bindBidirectional(property)
		property.addListener[ p, o, show |
			if(contentArea.children.contains(compartment)) {
				if(!show) 
					inflator.removeInflatable(compartment)							
			} else {
				if(show) 
					inflator.addInflatable(compartment, index.apply)
			}
		]
	}
}