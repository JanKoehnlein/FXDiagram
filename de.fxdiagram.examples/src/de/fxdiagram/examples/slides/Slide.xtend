package de.fxdiagram.examples.slides

import de.fxdiagram.core.XNode
import de.fxdiagram.core.services.ImageCache
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

import static de.fxdiagram.examples.slides.Styles.*

class Slide extends XNode {
	
	new(String name) {
		super(name)
		node = new StackPane => [
			children += new ImageView => [
				image = ImageCache.get.getImage(this, 'images/jungle.jpg')
			]
		]
	} 
	
	new(String text, int fontSize) {
		this(text)
		stackPane => [
			children += createText(text, fontSize)
		]
	}
	
	def getStackPane() {
		node as StackPane
	}

	override selectionFeedback(boolean isSelected) {
	}
	
}