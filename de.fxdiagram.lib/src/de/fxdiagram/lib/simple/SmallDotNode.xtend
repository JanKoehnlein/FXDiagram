package de.fxdiagram.lib.simple

import de.fxdiagram.core.XNode
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import javafx.scene.shape.Circle
import javafx.scene.paint.Color
import de.fxdiagram.lib.anchors.EllipseAnchors

@ModelNode
class SmallDotNode extends XNode {

	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
	}

	protected override createNode() {
		new Circle => [
			radius= 7
			fill = Color.HONEYDEW
			stroke = Color.BLACK
		]
	}

	override protected createAnchors() {
		new EllipseAnchors(this)
	}	
	
	override getName() {
		'foo'
	}
	
}