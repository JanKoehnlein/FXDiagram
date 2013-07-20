package de.fxdiagram.lib.media

import de.fxdiagram.core.XNode
import de.fxdiagram.core.anchors.RectangleAnchors
import javafx.scene.image.Image
import javafx.scene.image.ImageView

class ImageNode extends XNode {

	ImageView imageView

	new() {
		node = imageView = new ImageView => [
			preserveRatio = true
			fitWidthProperty.bind(widthProperty) 
			fitHeightProperty.bind(heightProperty)
		]
	}
	
	override protected createAnchors() {
		new RectangleAnchors(this)
	}
	
	def setImage(Image image) {
		imageView.image = image
	}
	
	def getImage() {
		imageView.image
	}
}