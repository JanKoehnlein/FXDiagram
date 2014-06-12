package de.fxdiagram.examples.slides

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.core.tools.actions.ZoomToFitAction
import javafx.scene.effect.ColorAdjust
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

@ModelNode
class Slide extends XNode {
	
	@FxProperty Image backgroundImage
	
	ImageView imageView
	
	()=>void onActivate
	
	new(String name) {
		super(name)
	}
	
	new(String name, Image backgroundImage) {
		this(name)
		this.backgroundImage = backgroundImage
	} 
	
	protected override StackPane createNode() {
		new StackPane => [
			children += imageView = new ImageView => [
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
		imageView.image = backgroundImage
		new ZoomToFitAction().perform(root)
		onActivate?.apply
	}
	
	def setOnActivate(()=>void onActivate) {
		this.onActivate = onActivate
	}
	
	def getStackPane() {
		node as StackPane
	}

	override selectionFeedback(boolean isSelected) {
	}
	
}