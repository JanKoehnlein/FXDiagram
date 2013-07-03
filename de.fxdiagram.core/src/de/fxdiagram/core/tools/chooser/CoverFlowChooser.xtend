package de.fxdiagram.core.tools.chooser

import de.fxdiagram.core.XNode
import javafx.geometry.Point3D
import javafx.geometry.Pos
import javafx.scene.effect.ColorAdjust
import javafx.scene.effect.Reflection
import javafx.scene.transform.Affine

import static java.lang.Math.*

import static extension de.fxdiagram.core.geometry.TransformExtensions.*

class CoverFlowChooser extends AbstractXNodeChooser {

	@Property double angle = 60
	@Property double deltaX = 20

	new(XNode host, Pos layoutPosition) {
		super(host, layoutPosition)
	}

	override activate() {
		super.activate()
	}

	override protected setInterpolatedPosition(double interpolatedPosition) {
		if(nodes.size == 0)
			return
		val gap = nodes.map[layoutBounds.width].reduce[a,b|a+b] / nodes.size
		val currentIndex = (interpolatedPosition as int) % nodes.size()
		val leftIndex = if (currentIndex == nodes.size - 1)
				currentIndex - 1
			else
				currentIndex
		val rightIndex = if (currentIndex == nodes.size - 1)
				currentIndex
			else
				currentIndex + 1
		for (i : 0 ..< leftIndex)
			transformNode(i, interpolatedPosition, true, 1, gap)
		for (i : nodes.size() >.. rightIndex + 1)
			transformNode(i, interpolatedPosition, false, 1, gap)
		transformNode(rightIndex, interpolatedPosition, false, rightIndex - interpolatedPosition, gap)
		transformNode(leftIndex, interpolatedPosition, true, interpolatedPosition - leftIndex, gap)
		if(rightIndex > 0 && abs(leftIndex - interpolatedPosition) > abs(rightIndex - interpolatedPosition))
			nodes.get(rightIndex).toFront
	}

	protected def transformNode(int i, double interpolatedPosition, boolean isLeft, double fraction, double gap) {
		if (i >= 0) {
			val node = nodes.get(i)
			val distanceFromSelection = abs(i - interpolatedPosition)
			val opacity = 1 - 0.2 * distanceFromSelection
			if (opacity < 0) {
				node.visible = false
			} else {
				node.visible = true
				val trafo = new Affine
				val direction = if(isLeft) -1 else 1
				node.transforms.clear
				trafo.translate(-0.5 * node.layoutBounds.width, -0.5 * node.layoutBounds.height, 0)
				trafo.rotate(direction * angle * fraction, new Point3D(0, 1, 0))
				trafo.translate((i - interpolatedPosition) * deltaX + 0.5 * fraction * direction * gap, 0, -fraction)
				node.transforms += trafo
				node.toFront
				node.effect = new ColorAdjust => [
					brightness = 1 - opacity
					input = new Reflection
				]
			}
		}
	}

}
