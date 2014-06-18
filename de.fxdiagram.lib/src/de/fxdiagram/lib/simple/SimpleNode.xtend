package de.fxdiagram.lib.simple

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

@ModelNode
class SimpleNode extends XNode {

	protected Text label
	
	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
	}
	
	new(String name) {
		super(name)
	}
	
	protected override createNode() {
		new RectangleBorderPane => [
			children += label = new Text => [
				textOrigin = VPos.TOP
				text = name
			]
			StackPane.setMargin(label, new Insets(10, 20, 10, 20))
		]
	}

	override doActivate() {
		super.doActivate
		label.text = name
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}

}
