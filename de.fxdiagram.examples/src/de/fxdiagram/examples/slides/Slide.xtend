package de.fxdiagram.examples.slides

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XNode
import de.fxdiagram.core.tools.actions.ZoomToFitAction
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

class Slide extends XNode {
	
	@FxProperty Image backgroundImage
	
	ImageView imageView
	
	new() {
		node = new StackPane => [
			children += imageView = new ImageView => [
				effect = new ColorAdjust => [
					brightness = -0.5
					saturation = 0
					contrast = -0.1
				]
			]
		]
	}
	
	new(String name, Image backgroundImage) {
		this()
		this.backgroundImage = backgroundImage
		this.name = name
	} 
	
	override doActivate() {
		super.doActivate()
		imageView.image = backgroundImage
		new ZoomToFitAction().perform(root)
	}
	
	def getStackPane() {
		node as StackPane
	}

	override selectionFeedback(boolean isSelected) {
	}
	
}