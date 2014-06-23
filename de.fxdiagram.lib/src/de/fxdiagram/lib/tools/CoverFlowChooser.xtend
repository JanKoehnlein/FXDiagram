package de.fxdiagram.lib.tools

import de.fxdiagram.core.XNode
import de.fxdiagram.core.tools.AbstractChooser
import javafx.geometry.Point3D
import javafx.geometry.Side
import javafx.scene.effect.ColorAdjust
import javafx.scene.effect.Reflection
import javafx.scene.paint.Color
import javafx.scene.transform.Affine

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.TransformExtensions.*

class CoverFlowChooser extends AbstractChooser {

	@Property double angle = 60
	@Property double deltaX = 20
	
	double gap

	new(XNode host, Side layoutPosition) {
		super(host, layoutPosition, false)
	}

	override activate() {
		super.activate()
	}

	override protected doSetInterpolatedPosition(double interpolatedPosition) {
		if(nodes.size != 0) {
			gap = nodes.map[layoutBounds.width].reduce[a,b|a+b] / nodes.size
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
				transformNode(i, interpolatedPosition, true, 1)
			for (i : nodes.size() >.. rightIndex + 1)
				transformNode(i, interpolatedPosition, false, 1)
			transformNode(rightIndex, interpolatedPosition, false, rightIndex - interpolatedPosition)
			transformNode(leftIndex, interpolatedPosition, true, interpolatedPosition - leftIndex)
			if(rightIndex > 0 && abs(leftIndex - interpolatedPosition) > abs(rightIndex - interpolatedPosition))
				nodes.get(rightIndex).toFront
		}
	}

	protected def transformNode(int i, double interpolatedPosition, boolean isLeft, double fraction) {
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
				trafo.translate(-0.5 * node.layoutBounds.width, -0.5 * node.layoutBounds.height, 0)
				trafo.rotate(direction * angle * fraction, new Point3D(0, 1, 0))
				trafo.translate((i - interpolatedPosition) * deltaX + 0.5 * fraction * direction * gap, 0, -fraction)
				node => [
					node.transforms.setAll(trafo)
					node.toFront
					node.effect = new ColorAdjust => [
						if(diagram.backgroundPaint == Color.BLACK)
							brightness = opacity - 1
						else 
							brightness = 1 - opacity
						input = new Reflection
					]
				]
			}
		}
	}
}
