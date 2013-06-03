package de.itemis.javafx.diagram.tools.chooser

import de.itemis.javafx.diagram.XNode
import javafx.geometry.Point2D
import javafx.scene.effect.Effect
import javafx.scene.effect.InnerShadow
import javafx.scene.transform.Transform

import static java.lang.Math.*

class CarusselChooser extends AbstractXNodeChooser {

	@Property double spacing = 6

	Effect currentNodeEffect  
	 
	new(XNode host, Point2D position) {
		super(host, position)
		currentNodeEffect = new InnerShadow
	}
	
	override activate() {
		super.activate()
	}

 	protected override setInterpolatedPosition(double interpolatedPosition) {
		val maxHeight = nodes.fold(0.0, [a, b|max(a, b.layoutBounds.height)]) + spacing
		val angle = PI / nodes.size
		val radius = maxHeight / 2 / sin(angle)
		for (i : 0 ..< nodes.size) {
			val node = nodes.get(i)
			val nodeCenterAngle = 2 * PI * (i - interpolatedPosition) / nodes.size
			if (cos(nodeCenterAngle) < 0)
				node.visible = false
			else {
				node.visible = true
				node.transforms.clear
				val scaleY = cos(nodeCenterAngle)
				val scaleX = (scaleY + 0.5) / 1.5
				node.transforms.add(Transform.translate(0, radius * sin(nodeCenterAngle - angle)))
				node.transforms.add(Transform.scale(scaleX, scaleY))
				node.transforms.add(Transform.translate(- node.layoutBounds.width / 2, spacing / 2))
				node.opacity = scaleY * scaleY * scaleY
				if(abs(nodeCenterAngle) < angle)
					node.effect = currentNodeEffect
				else 
					node.effect = null
			}
		}
	}	
	
	override protected nodeChosen(XNode choice) {
		choice.effect = null
		super.nodeChosen(choice)
	}
	
}

