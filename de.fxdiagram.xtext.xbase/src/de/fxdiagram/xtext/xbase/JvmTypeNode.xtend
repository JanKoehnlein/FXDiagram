package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.nodes.InflatableCompartment
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.xtext.glue.shapes.BaseNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.xbase.validation.UIStrings

import static extension de.fxdiagram.core.extensions.TextExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

@ModelNode
class JvmTypeNode extends BaseNode<JvmDeclaredType> {
	
	@Inject extension JvmDomainUtil 
	
	@Inject extension UIStrings
	
	Pane contentArea
	
	new(JvmEObjectDescriptor<JvmDeclaredType> descriptor) {
		super(descriptor)
	}
	
	override protected createNode() {
		super.createNode as RectangleBorderPane => [
			backgroundPaint = new LinearGradient(
				0, 0, 1, 1, 
				true, CycleMethod.NO_CYCLE,
				#[
					new Stop(0, Color.rgb(225,158,168)), 
					new Stop(1, Color.rgb(255,193,201))
				]) 
			children.clear
			children += contentArea = new VBox => [
				padding = new Insets(10, 20, 10, 20)
				spacing = 10
				children += new VBox => [
					alignment = Pos.CENTER
					children += label = new Text => [
						textOrigin = VPos.TOP
						text = name
						font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
					]
				]
			]
		]
	}
	
	override activate() {
		super.activate()
		val attributeCompartment = new InflatableCompartment(this, label.offlineWidth)
		contentArea.children += attributeCompartment
		descriptor.withDomainObject[ type |
			type.attributes.forEach[ field |
				attributeCompartment.add(new Text => [
					textOrigin = VPos.TOP
					text = '''«field.simpleName»: «field.type.simpleName»'''
					opacity = 0
					tooltip = field.signature
				])
			]
			null
		]
		attributeCompartment.inflate		
		val operationCompartment = new InflatableCompartment(this, label.offlineWidth)
		contentArea.children += operationCompartment
		descriptor.withDomainObject[ type |
			type.methods.forEach[ operation |
				operationCompartment.add(new Text => [
					textOrigin = VPos.TOP
					text = '''«operation.simpleName»: «operation.returnType.simpleName»'''
					opacity = 0
					tooltip = operation.signature
				])
			]
			null
		]
		operationCompartment.inflate		
	}
}