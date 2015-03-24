package de.fxdiagram.xtext.domainmodel

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import de.fxdiagram.mapping.IMappedElementDescriptor
import de.fxdiagram.mapping.shapes.BaseNode
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javax.inject.Inject
import org.eclipse.xtext.example.domainmodel.domainmodel.Entity
import org.eclipse.xtext.example.domainmodel.domainmodel.Property

@ModelNode
class EntityNode extends BaseNode<Entity> {
	
	@Inject extension DomainModelUtil util;
	
	new(IMappedElementDescriptor<Entity> descriptor) {
		super(descriptor)
	}
	
	override protected createNode() {
		new RectangleBorderPane => [
			backgroundPaint = new LinearGradient(
				0, 0, 1, 1, 
				true, CycleMethod.NO_CYCLE,
				#[
					new Stop(0, Color.rgb(158, 188, 227)), 
					new Stop(1, Color.rgb(220, 230, 255))
				])
			children += new VBox => [
				padding = new Insets(10, 20, 10, 20)
				alignment = Pos.CENTER
				children += new Text => [
					textOrigin = VPos.TOP
					text = name
					font = Font.font(font.family, FontWeight.BOLD, font.size * 1.1)
					VBox.setMargin(it, new Insets(0, 0, 10, 0))
				]
				children += new VBox => [ attributeCompartment |
					domainObject.withDomainObject[ entity |
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
		]
	}
	
}