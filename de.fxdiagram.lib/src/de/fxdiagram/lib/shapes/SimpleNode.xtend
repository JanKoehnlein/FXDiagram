package de.fxdiagram.lib.shapes

import de.fxdiagram.core.XNode
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.StackPane
import javafx.scene.text.Text

class SimpleNode extends XNode {

	String name

	new(String name) {
		this.name = name
		node = new RectangleBorderPane => [
			val label = new Text
			children += label => [
				text = name
				textOrigin = VPos.TOP
			]
			StackPane.setMargin(label, new Insets(10, 20, 10, 20))
		]
		node.effect = new InnerShadow => [
			radius = 7
		]
		key = name
	}

	override doActivate() {
		super.doActivate()
		val rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate()
	}

	override toString() {
		name
	}
}
