package de.fxdiagram.examples.slides

import de.fxdiagram.core.XNode
import de.fxdiagram.core.tools.actions.ZoomToFitAction
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class Slide extends XNode {
	
	new(String name, Image backgroundImage) {
		super(name)
		node = new StackPane => [
			children += new ImageView => [
				image = backgroundImage
				effect = new ColorAdjust => [
					brightness = -0.5
					saturation = 0
					contrast = -0.1
				]
			]
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