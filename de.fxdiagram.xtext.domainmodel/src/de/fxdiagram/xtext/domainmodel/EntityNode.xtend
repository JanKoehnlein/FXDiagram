package de.fxdiagram.xtext.domainmodel

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.xtext.glue.XtextDomainObjectDescriptor
import de.fxdiagram.xtext.glue.shapes.BaseNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.example.domainmodel.domainmodel.Property
import javax.inject.Inject

@ModelNode
class EntityNode extends BaseNode<Entity> {
	
	@Inject extension DomainModelUtil util;
	
	new(XtextDomainObjectDescriptor<Entity> descriptor) {
		super(descriptor)
	}
	
	override protected createNode() {
		new RectangleBorderPane => [
			children += new VBox => [
				padding = new Insets(10, 20, 10, 20)
				alignment = Pos.CENTER
				children += label = new Text => [
					textOrigin = VPos.TOP
					text = name
					font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
				]
				VBox.setMargin(label, new Insets(0, 0, 10, 0))
				children += new VBox => [ attributeCompartment |
					descriptor.withDomainObject[ entity |
						entity.features.filter(Property).filter[type.referencedEntity == null].forEach[ attribute |
							attributeCompartment.children += new Text => [
								textOrigin = VPos.TOP
								text = '''«attribute.name»: «attribute.type.simpleName»'''
							]
						]
						null
					]
				]
			]
			effect = new InnerShadow => [
				radius = 7
			]
		]
 
	}
	
}