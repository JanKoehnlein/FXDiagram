package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XNode
import javafx.scene.control.Label
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle

class MyNode extends XNode {

	AddRapidButtonBehavior rapidButtonBehavior

	new(String name) {
		node = new StackPane => [
			children += new Rectangle => [
				width = 80
				height = 30
				fill = createFill
				stroke = Color::gray(0.5)
				strokeWidth = 1.2
				arcWidth = 12
				arcHeight = 12
			]
			children += new Label => [
				text = name
			]
		]
		node.effect = new InnerShadow => [
			radius = 7
		]
	}
	
	override activate() {
		super.activate()
		rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate()
	}

	def protected createFill() {
		val stops = newArrayList(new Stop(0, Color::gray(0.6)), new Stop(1, Color::gray(0.9)))
		new LinearGradient(0, 0, 1, 1, true, CycleMethod::NO_CYCLE, stops);
	}
}
