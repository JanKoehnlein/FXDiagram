package de.fxdiagram.examples.slides

import de.fxdiagram.core.XNode
import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.core.tools.actions.ZoomToFitAction
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

import static de.fxdiagram.examples.slides.Styles.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

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
	
	override doActivate() {
		super.doActivate()
		new ZoomToFitAction().perform(root)
	}
	
	def getStackPane() {
		node as StackPane
	}

	override selectionFeedback(boolean isSelected) {
	}
	
}