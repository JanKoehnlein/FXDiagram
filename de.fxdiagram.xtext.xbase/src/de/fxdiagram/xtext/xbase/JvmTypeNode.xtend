package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.animations.Inflator
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
				children += new Label(URI.createURI(domainObject.uri).lastSegment)
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
			text = domainObject.fqn.substring(0, domainObject.fqn.lastIndexOf('.'))		
		]
		pane
	}
	
	override doActivate() {
		super.doActivate()

		if(showPackage)
			titleArea.children.add(0, packageLabel)
		showPackageProperty.bindCheckbox(packageBox, packageLabel, titleArea, [ 0 ])
		
		val inflator = new Inflator(this, contentArea)
		
		val attributeCompartment = new VBox => [ c |
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
		attributeCompartment.activate(showAttributesProperty, attributesBox, [1], inflator)

		val methodCompartment = new VBox => [ c |
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
		compartment.padding = new Insets(10,0,0,0)
		showProperty.bindCheckbox(box, compartment, contentArea, index)
		if(showProperty.get) 
			inflator.addInflatable(compartment, index.apply)
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