package de.fxdiagram.lib.chooser

import javafx.geometry.Point3D
import javafx.scene.effect.ColorAdjust
import javafx.scene.effect.Reflection
import javafx.scene.paint.Color
import javafx.scene.transform.Affine
import org.eclipse.xtend.lib.annotations.Accessors

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.TransformExtensions.*

/**
 * A 3D effect to select a candidate node from a list similar to Apple's cover flow.
 */
class CoverFlowChoice extends AbstractChoiceGraphics {

	@Accessors double angle = 60
	@Accessors double deltaX = 20
	
	double gap
	
	override setInterpolatedPosition(double interpolatedPosition) {
		if(choiceNodes.size != 0) {
			gap = choiceNodes.map[layoutBounds.width].reduce[a,b|a+b] / choiceNodes.size
			val currentIndex = (interpolatedPosition as int) % choiceNodes.size()
			val leftIndex = if (currentIndex == choiceNodes.size - 1)
					currentIndex - 1
				else
					currentIndex
			val rightIndex = if (currentIndex == choiceNodes.size - 1)
					currentIndex
				else
					currentIndex + 1
			for (i : 0 ..< leftIndex)
				transformNode(i, interpolatedPosition, true, 1)
			for (i : choiceNodes.size() >.. rightIndex + 1)
				transformNode(i, interpolatedPosition, false, 1)
			transformNode(rightIndex, interpolatedPosition, false, rightIndex - interpolatedPosition)
			transformNode(leftIndex, interpolatedPosition, true, interpolatedPosition - leftIndex)
			if(rightIndex > 0 && abs(leftIndex - interpolatedPosition) > abs(rightIndex - interpolatedPosition))
				choiceNodes.get(rightIndex).toFront
		}
	}

	protected def transformNode(int i, double interpolatedPosition, boolean isLeft, double fraction) {
		if (i >= 0) {
			val node = choiceNodes.get(i)
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
						if(chooser.diagram.backgroundPaint == Color.BLACK)
							brightness = opacity - 1
						else 
							brightness = 1 - opacity
						input = new Reflection
					]
				]
			}
		}
	}
	
	override hasButtons() {
		false
	}
	
}
