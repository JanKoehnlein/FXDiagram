package de.fxdiagram.core.tools.chooser

import de.fxdiagram.core.XNode
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.scene.effect.ColorAdjust
import javafx.scene.effect.Reflection
import javafx.scene.transform.Affine

import static java.lang.Math.*

import static extension de.fxdiagram.core.geometry.TransformExtensions.*

class CoverFlowChooser extends AbstractXNodeChooser {

	@Property double angle = 60
	@Property double deltaX = 20
	@Property double gap = 120

	new(XNode host, Point2D position) {
		super(host, position)
	}

	override activate() {
		super.activate()
	}

	override protected setInterpolatedPosition(double interpolatedPosition) {
		val leftIndex = (interpolatedPosition as int) % nodes.size()
		val rightIndex = (leftIndex + 1) % nodes.size()
		for (i : 0 ..< leftIndex)
			transformNode(i, interpolatedPosition, true, 1)
		for (i : nodes.size() >.. rightIndex + 1)
			transformNode(i, interpolatedPosition, false, 1)
		transformNode(rightIndex, interpolatedPosition, false, rightIndex - interpolatedPosition)
		transformNode(leftIndex, interpolatedPosition, true, interpolatedPosition - leftIndex)
	}

	protected def transformNode(int i, double interpolatedPosition, boolean isLeft, double fraction) {
		val node = nodes.get(i)
		val distanceFromSelection = abs(i - interpolatedPosition)
		val opacity = 1 - 0.2 * distanceFromSelection
		if (opacity < 0) {
			node.visible = false
		} else {
			node.visible = true
			val trafo = new Affine
			val direction = if(isLeft) -1 else 1
			trafo.translate(-0.5 * node.layoutBounds.width, -0.5 * node.layoutBounds.height, 0)
			trafo.rotate(direction * angle * fraction, new Point3D(0, 1, 0))
			trafo.translate((i - interpolatedPosition) * deltaX + 0.5 * fraction * direction * gap, 0, -fraction)
			node.transforms.clear
			node.transforms += trafo
			node.toFront
			node.effect = new ColorAdjust => [
				brightness = 1 - opacity
				input = new Reflection
			]
		}
	}

}
