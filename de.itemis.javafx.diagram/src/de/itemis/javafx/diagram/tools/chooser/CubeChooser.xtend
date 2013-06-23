package de.itemis.javafx.diagram.tools.chooser

import de.itemis.javafx.diagram.XNode
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.scene.transform.Affine

import static java.lang.Math.*

import static extension de.itemis.javafx.diagram.transform.TransformExtensions.*

class CubeChooser extends AbstractXNodeChooser {
	
	@Property var spacing = 6.0
	@Property var distance = 250.0
	@Property var screenDistance = 250.0
	
	new(XNode host, Point2D position) {
		super(host, position)
	}
	
	override activate() {
		super.activate()
	}
	
	override protected setInterpolatedPosition(double interpolatedPosition) {
		val maxWidth = nodes.fold(0.0, [a, b|max(a, b.layoutBounds.width)]) + spacing
		val angle = (interpolatedPosition - (interpolatedPosition as int)) * 90 
		val leftNodeIndex = interpolatedPosition as int % nodes.size
		applyTransform(leftNodeIndex, angle, maxWidth)
		val rightNodeIndex = (interpolatedPosition as int + 1) % nodes.size
		applyTransform(rightNodeIndex, angle - 90, maxWidth)
		nodes.forEach[XNode node, int i | 
			if(i != leftNodeIndex && i != rightNodeIndex) 
				node.visible = false
		]
	}
	
	protected def applyTransform(int nodeIndex, double angle, double maxWidth) {
		val node = nodes.get(nodeIndex)
		if(abs(angle) > 86) 
			node.visible = false
		else {			
			val width = node.layoutBounds.width
			val height = node.layoutBounds.height
			val transform = new Affine
			transform.translate(-0.5 * width, -0.5 * height, - maxWidth * 0.5)
			transform.rotate(angle, new Point3D(0,1,0))
			transform.translate(0, 0, maxWidth * 0.5)
			node.visible = true
			node.transforms.clear
			node.transforms += transform
		}
	} 
	
}