package de.fxdiagram.lib.simple

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectHandle
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

@ModelNode(#['layoutX', 'layoutY', 'domainObject', 'width', 'height'])
class SimpleNode extends XNode {

	Text label
	
	new(DomainObjectHandle domainObject) {
		super(domainObject)
	}
	
	new(String name) {
		super(name)
	}
	
	protected override createNode() {
		new RectangleBorderPane => [
			children += label = new Text => [
				textOrigin = VPos.TOP
				text = key
			]
			StackPane.setMargin(label, new Insets(10, 20, 10, 20))
			effect = new InnerShadow => [
				radius = 7
			]
		]
	}

	override doActivate() {
		super.doActivate
		label.text = domainObject?.key
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}

}
