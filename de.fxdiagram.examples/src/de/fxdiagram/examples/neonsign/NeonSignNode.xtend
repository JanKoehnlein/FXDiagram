package de.fxdiagram.examples.neonsign

import de.fxdiagram.core.XNode
import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.control.TextField
import javafx.scene.effect.Blend
import javafx.scene.effect.BlendMode
import javafx.scene.effect.Bloom
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment

import static extension javafx.scene.layout.VBox.*
import static extension javafx.util.Duration.*

class NeonSignNode extends XNode {
	
	TextField textField
	
	Text neonText

	new() {
		node = new FlipNode => [
			front = new RectangleBorderPane => [
				children += new Text => [
					textOrigin = VPos.TOP
					text = 'Neon Sign'
					StackPane.setMargin(it, new Insets(10, 20, 10, 20))
				]
			]
			back = neonSign
		]
	}
	
	protected def getNeonSign() {
		new VBox => [
			style = '-fx-background-image: url("de/fxdiagram/examples/neonsign/brick.jpg");'
			alignment = Pos.TOP_CENTER
			children += textField = new TextField => [
				text = 'How many LOC are necessary?'
				margin = new Insets(10,40,10,40)
			]
			children += neonText = new Text => [
				textProperty.bind(textField.textProperty)
				font = Font.font('Nanum Pen Script', 100)
				fill = Color.web('#feeb42')
				wrappingWidth = 580
				textAlignment = TextAlignment.CENTER
				rotate = -7
				effect = new Blend => [
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
	
	override doActivate() {
		super.doActivate()
		new Timeline => [
			cycleCount = -1
			keyFrames += new KeyFrame(10.millis, new KeyValue(neonText.opacityProperty, 0.75)) 			 	
			keyFrames += new KeyFrame(20.millis, new KeyValue(neonText.opacityProperty, 0.95)) 			 	
			keyFrames += new KeyFrame(40.millis, new KeyValue(neonText.opacityProperty, 0.85)) 			 	
			keyFrames += new KeyFrame(50.millis, new KeyValue(neonText.opacityProperty, 1)) 			 	
			play
		]
	}
	
}
