package de.fxdiagram.examples.neonsign

import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.lib.nodes.FlipNode
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.geometry.Insets
import javafx.scene.control.TextField
import javafx.scene.effect.Blend
import javafx.scene.effect.BlendMode
import javafx.scene.effect.Bloom
import javafx.scene.effect.InnerShadow
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment


import static extension de.fxdiagram.core.extensions.TooltipExtensions.*
import static extension javafx.scene.layout.VBox.*
import static extension javafx.util.Duration.*
import de.fxdiagram.annotations.properties.ModelNode
import static de.fxdiagram.core.extensions.ClassLoaderExtensions.*

@ModelNode
class NeonSignNode extends FlipNode {

	TextField textField

	Text neonText

	new(String name) {
		super(name)
	}
	
	protected override createNode() {
		val node = super.createNode
		front = neonSign
		back = new ImageView => [
			image = ImageCache.get.getImage(this, 'code.png')
		]
		node
	}
	
	override doActivate() {
		super.doActivate()
		front => [
			tooltip = 'Double-click for Xtend code'
			onMouseClicked = [                            // flickering animation
				new Timeline => [
					cycleCount = 20
					keyFrames += new KeyFrame(10.millis, new KeyValue(neonText.opacityProperty, 0.45))
					keyFrames += new KeyFrame(20.millis, new KeyValue(neonText.opacityProperty, 0.95))
					keyFrames += new KeyFrame(40.millis, new KeyValue(neonText.opacityProperty, 0.65))
					keyFrames += new KeyFrame(50.millis, new KeyValue(neonText.opacityProperty, 1))
					play
				]
			]
		]
	}

	protected def getNeonSign() {
		new VBox => [
			style = '''
				-fx-background-image: url("«toURI(this, 'brick.jpg')»");
			'''
			children += textField = new TextField => [ // text input widget
				text = 'JavaFX loves Xtend'
				margin = new Insets(10, 40, 10, 40)
			]
			children += neonText = new Text => [          // neon text
				textProperty.bind(textField.textProperty) // databinding
				wrappingWidth = 580                       // text placement
				textAlignment = TextAlignment.CENTER
				rotate = -7
				font = Font.font('Nanum Pen Script', 100) // font and color
				fill = Color.web('#feeb42')
				effect = new Blend => [                   // neon effect
					mode = BlendMode.MULTIPLY
					topInput = new Bloom
					bottomInput = new InnerShadow => [
						color = Color.web('#f13a00')
						radius = 5
						choke = 0.4
					]
				]
			]
		]
	}
}
