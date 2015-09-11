package de.fxdiagram.lib.media

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import de.fxdiagram.core.services.ResourceDescriptor

@ModelNode
class ImageNode extends XNode {

	new() {
		println()
	}
	new(ResourceDescriptor imageDescriptor) {
		super(imageDescriptor)
	}
	
	override protected createNode() {
		new ImageView => [
			preserveRatio = true
			image = new Image((domainObjectDescriptor as ResourceDescriptor).toURI)
			fitWidthProperty.bind(widthProperty) 
			fitHeightProperty.bind(heightProperty)
		]
	}
}

