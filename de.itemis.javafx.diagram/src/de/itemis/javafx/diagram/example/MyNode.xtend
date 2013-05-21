package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XNode
import javafx.geometry.VPos
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

class MyNode extends XNode {

	String name

	new(String name) {
		this.name = name
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
			children += new Text => [
				text = name
				textOrigin = VPos::TOP
			]
		]
		node.effect = new InnerShadow => [
			radius = 7
		]
	}
	
	override doActivate() {
		super.doActivate()
		val rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate()
	}

	def protected createFill() {
		val stops = newArrayList(
			new Stop(0, Color::gray(0.6)), 
			new Stop(1, Color::gray(0.9))
		)
		new LinearGradient(0, 0, 1, 1, true, CycleMethod::NO_CYCLE, stops)
	}
	
	override toString() {
		name
	}
}
