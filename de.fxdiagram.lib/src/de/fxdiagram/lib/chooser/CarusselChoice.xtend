package de.fxdiagram.lib.chooser

import de.fxdiagram.core.XNode
import javafx.scene.Node
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow
import javafx.scene.transform.Transform
import org.eclipse.xtend.lib.annotations.Accessors

import static java.lang.Math.*

/**
 * A pseudo 3D effect that projects all choices on a spinning wheel, similar to a slot machine. 
 */
class CarusselChoice extends AbstractChoiceGraphics {

	@Accessors double spacing = 6

	Effect currentNodeEffect = new InnerShadow

	double radius

	override setInterpolatedPosition(double interpolatedPosition) {
		val maxHeight = choiceNodes.fold(0.0, [a, b|max(a, b.layoutBounds.height)]) + spacing
		val angle = PI / choiceNodes.size
		radius = maxHeight / 2 / sin(angle)
		for (i : 0 ..< choiceNodes.size) {
			val node = choiceNodes.get(i)
			val nodeCenterAngle = 2 * PI * (i - interpolatedPosition) / choiceNodes.size
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

	override nodeChosen(XNode choice) {
		super.nodeChosen(choice)
		choice.setEffect(null)
	}

	override relocateButtons(Node minusButton, Node plusButton) {
		minusButton.layoutX = choiceGroup.layoutX - 0.5 * minusButton.layoutBounds.width
		minusButton.layoutY = choiceGroup.layoutY + radius + 0.5 * minusButton.layoutBounds.height
		plusButton.layoutX = choiceGroup.layoutX - 0.5 * plusButton.layoutBounds.width
		plusButton.layoutY = choiceGroup.layoutY - radius - plusButton.layoutBounds.height
	}

}
