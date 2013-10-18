package de.fxdiagram.lib.media

import de.fxdiagram.core.XNode
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ImageNode extends XNode {

	ImageView imageView

	new(String key) {
		super(key)
		node = imageView = new ImageView => [
			preserveRatio = true
			fitWidthProperty.bind(widthProperty) 
			fitHeightProperty.bind(heightProperty)
		]
	}
	
	def setImage(Image image) {
		imageView.image = image
	}
	
	def getImage() {
		imageView.image
	}
}