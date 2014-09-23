package de.fxdiagram.xtext.xbase

import com.google.inject.Inject
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.xtext.glue.shapes.BaseNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.eclipse.xtext.common.types.JvmDeclaredType

@ModelNode
class JvmTypeNode extends BaseNode<JvmDeclaredType> {
	
	@Inject extension JvmDomainUtil 
	
	new(JvmEObjectDescriptor<JvmDeclaredType> descriptor) {
		super(descriptor)
	}
	
	override protected createNode() {
		new RectangleBorderPane => [
			borderRadius = 6
			backgroundRadius = 6
			children += new VBox => [
				padding = new Insets(10, 20, 10, 20)
				alignment = Pos.CENTER
				children += label = new Text => [
					textOrigin = VPos.TOP
					text = name
					font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
				]
				VBox.setMargin(label, new Insets(0, 0, 10, 0))
				children += new VBox => [ fieldCompartment |
					descriptor.withDomainObject[ type |
						type.attributes.forEach[ field |
							fieldCompartment.children += new Text => [
								textOrigin = VPos.TOP
								text = '''«field.simpleName»: «field.type.simpleName»'''
							]
						]
						null
					]
				]
			]
		]
	}
}