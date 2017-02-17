package de.fxdiagram.mapping.shapes

import de.fxdiagram.mapping.IMappedElementDescriptor
import javafx.geometry.Insets
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode
class BaseNodeHeadingLabel<T> extends BaseNodeLabel<T> {
	
	new(IMappedElementDescriptor<T> descriptor) {
		super(descriptor)
	}
	
	override protected createNode() {
		super.createNode => [
			StackPane.setMargin(this, new Insets(10, 20, 10, 20))
			text.font = Font.font(text.font.family, FontWeight.BOLD, text.font.size * 1.1)
		]
	}
}