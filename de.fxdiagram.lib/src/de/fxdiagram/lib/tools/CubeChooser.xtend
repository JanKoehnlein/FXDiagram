package de.fxdiagram.lib.tools

import de.fxdiagram.core.XNode
import javafx.geometry.Point3D
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.transform.Affine

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.TransformExtensions.*
import de.fxdiagram.core.tools.AbstractChooser

class CubeChooser extends AbstractChooser {

	@Property var spacing = 6.0

	double maxWidth

	new(XNode host, Pos layoutPosition) {
		super(host, layoutPosition, true)
	}

	override activate() {
		super.activate()
	}

	override protected doSetInterpolatedPosition(double interpolatedPosition) {
		maxWidth = nodes.fold(0.0, [a, b|max(a, b.layoutBounds.width)]) + spacing
		val angle = (interpolatedPosition - (interpolatedPosition as int)) * 90
		val leftNodeIndex = interpolatedPosition as int % nodes.size
		applyTransform(leftNodeIndex, angle)
		val rightNodeIndex = (interpolatedPosition as int + 1) % nodes.size
		applyTransform(rightNodeIndex, angle - 90)
		nodes.forEach [ XNode node, int i |
			if (i != leftNodeIndex && i != rightNodeIndex)
				node.visible = false
		]
	}

	protected def applyTransform(int nodeIndex, double angle) {
		val node = nodes.get(nodeIndex)
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
		minusButton.layoutX = group.layoutX + groupMaxWidthHalf 
		minusButton.layoutY = group.layoutY - 0.5 * minusButton.layoutBounds.height
		plusButton.layoutX = group.layoutX - groupMaxWidthHalf - plusButton.layoutBounds.width 
		plusButton.layoutY = group.layoutY - 0.5 * plusButton.layoutBounds.height
	}
	
}
