package de.fxdiagram.core.behavior

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XNode
import javafx.scene.effect.DropShadow
import javafx.scene.effect.Effect
import javafx.scene.input.MouseEvent

class SelectionBehavior extends AbstractBehavior {

	@FxProperty boolean selected

	Effect selectionEffect

	new(XNode host) {
		super(host)
		selectionEffect = new DropShadow() => [
			offsetX = 4.0
			offsetY = 4.0
		]
	}

	override doActivate() {
		selectedProperty.addListener [ observable, oldValue, newValue |
			if (newValue) {
				host.effect = selectionEffect
				host.scaleX = 1.05
				host.scaleY = 1.05
				host.toFront
			} else {
				host.effect = null
				host.scaleX = 1.0
				host.scaleY = 1.0
			}
		]
	}

	def select(MouseEvent it) {
		selected = true
	}

	def toggleSelect(MouseEvent it) {
		if (shortcutDown) {
			selected = !selected
		}
	}
}
