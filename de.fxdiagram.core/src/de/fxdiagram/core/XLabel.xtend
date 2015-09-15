package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import javafx.geometry.VPos
import javafx.scene.text.Text

@ModelNode('text', 'type')
class XLabel extends XDomainObjectShape {

	@FxProperty Text text = new Text
	@FxProperty String type
	
	new(DomainObjectDescriptor domainObjectDescriptor) {
		super(domainObjectDescriptor)
	}
	
	override protected createNode() {
		text => [
			textOrigin = VPos.TOP
		]
	}
	
	override protected doActivate() {
	}
}