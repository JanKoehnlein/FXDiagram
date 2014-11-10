package de.fxdiagram.lib.chooser

import de.fxdiagram.core.XNode
import javafx.geometry.Point3D
import javafx.scene.Node
import javafx.scene.transform.Affine
import org.eclipse.xtend.lib.annotations.Accessors

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.TransformExtensions.*

class CubeChoice extends AbstractChoiceGraphics {

	@Accessors double spacing = 6.0

	double maxWidth
	
	override setInterpolatedPosition(double interpolatedPosition) {
		maxWidth = choiceNodes.fold(0.0, [a, b|max(a, b.layoutBounds.width)]) + spacing
		val angle = (interpolatedPosition - (interpolatedPosition as int)) * 90
		val leftNodeIndex = interpolatedPosition as int % choiceNodes.size
		applyTransform(leftNodeIndex, angle)
		val rightNodeIndex = (interpolatedPosition as int + 1) % choiceNodes.size
		applyTransform(rightNodeIndex, angle - 90)
		choiceNodes.forEach [ XNode node, int i |
			if (i != leftNodeIndex && i != rightNodeIndex)
				node.visible = false
		]
	}

	protected def applyTransform(int nodeIndex, double angle) {
		val node = choiceNodes.get(nodeIndex)
		if (abs(angle) > 86)
			node.visible = false
		else {
			val width = node.layoutBounds.width
			val height = node.layoutBounds.height
			val transform = new Affine
			transform.translate(-0.5 * width, -0.5 * height, - maxWidth * 0.5)
			transform.rotate(angle, new Point3D(0, 1, 0))
			transform.translate(0, 0, maxWidth * 0.5)
			node.visible = true
			node.transforms.setAll(transform)
		}
	}
	
	override relocateButtons(Node minusButton, Node plusButton) {
		val groupMaxWidthHalf = 0.5 * maxWidth * sqrt(2)
		minusButton.layoutX = choiceGroup.layoutX + groupMaxWidthHalf 
		minusButton.layoutY = choiceGroup.layoutY - 0.5 * minusButton.layoutBounds.height
		plusButton.layoutX = choiceGroup.layoutX - groupMaxWidthHalf - plusButton.layoutBounds.width 
		plusButton.layoutY = choiceGroup.layoutY - 0.5 * plusButton.layoutBounds.height
	}
	
}
