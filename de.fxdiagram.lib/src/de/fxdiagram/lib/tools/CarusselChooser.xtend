package de.fxdiagram.lib.tools

import de.fxdiagram.core.XNode
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow
import javafx.scene.transform.Transform

import static java.lang.Math.*
import de.fxdiagram.core.tools.AbstractChooser

class CarusselChooser extends AbstractChooser {

	@Property double spacing = 6

	Effect currentNodeEffect

	double radius
	
	new(XNode host, Pos layoutPosition) {
		super(host, layoutPosition, true)
		currentNodeEffect = new InnerShadow
	}

	override activate() {
		super.activate()
	}

	protected override doSetInterpolatedPosition(double interpolatedPosition) {
		val maxHeight = nodes.fold(0.0, [a, b|max(a, b.layoutBounds.height)]) + spacing
		val angle = PI / nodes.size
		radius = maxHeight / 2 / sin(angle)
		for (i : 0 ..< nodes.size) {
			val node = nodes.get(i)
			val nodeCenterAngle = 2 * PI * (i - interpolatedPosition) / nodes.size
			if (cos(nodeCenterAngle) < 0)
				node.visible = false
			else {
				node.visible = true
				val scaleY = cos(nodeCenterAngle)
				val scaleX = (scaleY + 0.5) / 1.5
				node.transforms.setAll(#[
					Transform.translate(0, radius * sin(nodeCenterAngle - angle)),
					Transform.scale(scaleX, scaleY),
					Transform.translate(- node.layoutBounds.width / 2, spacing / 2)
				])
				node.opacity = scaleY * scaleY * scaleY
				if (abs(nodeCenterAngle) < angle) {
					node.effect = currentNodeEffect
				} else {
					node.effect = null
				}
			}
		}
	}

	override protected nodeChosen(XNode choice) {
		choice.effect = null
		super.nodeChosen(choice)
	}
	
	override relocateButtons(Node minusButton, Node plusButton) {
		minusButton.layoutX = group.layoutX - 0.5 * minusButton.layoutBounds.width
		minusButton.layoutY = group.layoutY + radius
		plusButton.layoutX = group.layoutX - 0.5 * plusButton.layoutBounds.width
		plusButton.layoutY = group.layoutY - radius - plusButton.layoutBounds.height
	}

}
