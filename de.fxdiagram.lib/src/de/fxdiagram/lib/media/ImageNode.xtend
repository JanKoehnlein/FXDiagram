package de.fxdiagram.lib.media

import de.fxdiagram.core.XNode
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode(#['layoutX', 'layoutY', 'domainObject', 'width', 'height'])
class ImageNode extends XNode {

	new() {
		super('Image')
	}
	
	override protected createNode() {
		new ImageView => [
			preserveRatio = true
			fitWidthProperty.bind(widthProperty) 
			fitHeightProperty.bind(heightProperty)
		]
	}

	protected def getImageView() {
		node as ImageView
	}
	
	def setImage(Image image) {
		imageView.image = image
	}
	
	def getImage() {
		imageView.image
	}
}