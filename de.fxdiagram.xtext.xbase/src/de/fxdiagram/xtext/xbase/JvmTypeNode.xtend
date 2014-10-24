package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.nodes.InflatableCompartment
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.xtext.glue.shapes.BaseFlipNode
import javafx.beans.property.BooleanProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Node
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
import static javafx.scene.input.MouseButton.*

import static extension de.fxdiagram.core.extensions.TextExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

@ModelNode("showPackage", "showAttributes", "showMethods", "bgColor")
class JvmTypeNode extends BaseFlipNode<JvmDeclaredType> {

	@FxProperty boolean showPackage = false
	@FxProperty boolean showAttributes = true
	@FxProperty boolean showMethods = true
	@FxProperty Color bgColor = Color.web('#ffe6cc')
	
	@Inject extension JvmDomainUtil 
	@Inject extension UIStrings
	
	VBox titleArea
	Text label
	Text packageLabel
	
	CheckBox packageBox
	CheckBox attributesBox
	CheckBox methodsBox
	
	Pane contentArea
	
	new(JvmEObjectDescriptor<JvmDeclaredType> descriptor) {
		super(descriptor)
	}
	
	override registerOnClick() {
		onMouseClicked = [ 
			if (button == SECONDARY) {
				if (front != null && back != null) 
					flip(isHorizontal(it))
			}
		]
	}
	
	override createNode() {
		val pane = super.createNode
		front = new RectangleBorderPane => [
			tooltip = 'Right-click to configure'
			backgroundPaintProperty.bind(bgColorProperty)
			children += contentArea = new VBox => [
				padding = new Insets(10, 20, 10, 20)
				spacing = 10
				children += titleArea = new VBox => [
					alignment = Pos.CENTER
					children += label = new Text => [
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
				children += new Label(URI.createURI(descriptor.uri).lastSegment)
				children += packageBox = new CheckBox('Package')
				children += attributesBox = new CheckBox('Attributes')
				children += methodsBox = new CheckBox('Methods')
				children += new ColorPicker => [ 
					valueProperty.bindBidirectional(bgColorProperty)
				]
			]			
		]
		packageLabel = new Text => [
			textOrigin = VPos.TOP
			font = Font.font(font.family, font.size * 0.8)
			text = descriptor.fqn.substring(0, descriptor.fqn.lastIndexOf('.'))		
		]
		pane
	}
	
	override doActivate() {
		super.doActivate()
		
		if(showPackage)
			titleArea.children.add(0, packageLabel)
		showPackageProperty.bindCheckbox(packageBox, packageLabel, titleArea, [ 0 ])

		val attributeCompartment = new InflatableCompartment(this, label.offlineWidth)
		descriptor.withDomainObject[ type |
			type.attributes.forEach[ field |
				attributeCompartment.add(new Text => [
					textOrigin = VPos.TOP
					text = '''«field.simpleName»: «field.type.simpleName»'''
					tooltip = field.signature
				])
			]
			null
		]
		attributeCompartment.activate(showAttributesProperty, attributesBox, [1])

		val methodCompartment = new InflatableCompartment(this, label.offlineWidth)
		descriptor.withDomainObject[ type |
			type.methods.forEach[ operation |
				methodCompartment.add(new Text => [
					textOrigin = VPos.TOP
					text = '''«operation.simpleName»: «operation.returnType.simpleName»'''
					tooltip = operation.signature
				])
			]
			null
		]
		methodCompartment.activate(showMethodsProperty, methodsBox, [contentArea.children.size])
	}
	
	protected def activate(InflatableCompartment compartment, BooleanProperty showProperty, CheckBox box, ()=>int index) {
		if(showProperty.get) {
			contentArea.children += compartment
			compartment.inflate
		} else {
			compartment.populate		
		} 
		showProperty.bindCheckbox(box, compartment, contentArea, index)
	}
	
	protected def bindCheckbox(BooleanProperty property, CheckBox box, Node node, Pane container, ()=>int index) {
		box.selectedProperty.bindBidirectional(property)
		property.addListener[ p, o, show |
			if(container.children.contains(node)) {
				if(!show) 
					container.children -= node							
			} else {
				if(show) 
					container.children.add(index.apply, node)
			}
		]
	}
}