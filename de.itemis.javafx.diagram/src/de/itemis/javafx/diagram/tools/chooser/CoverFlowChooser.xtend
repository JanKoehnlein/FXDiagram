package de.itemis.javafx.diagram.tools.chooser

import de.itemis.javafx.diagram.XNode
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.scene.transform.Affine

import static extension de.itemis.javafx.diagram.transform.PerspectiveExtensions.*
import static java.lang.Math.*

import static extension de.itemis.javafx.diagram.transform.TransformExtensions.*

class CoverFlowChooser extends AbstractXNodeChooser {
	
	@Property double angle = 60 
	@Property double deltaZ = 70 
	@Property double deltaX = 20 
	@Property double gap = 120
	
	double maxWidth
	
	new(XNode host, Point2D position) {
		super(host, position)
	}
	
	override activate() {
		maxWidth = nodes.fold(0.0, [a, b|max(a, b.layoutBounds.width)])
		super.activate()
	}
	
	override protected setInterpolatedPosition(double interpolatedPosition) {
		val leftIndex = (interpolatedPosition as int) % nodes.size()
		val rightIndex = (leftIndex + 1) % nodes.size()
		for(i: 0..<leftIndex) 
			nodes.get(i).transform(i, interpolatedPosition, true, 1)
		for(i: nodes.size()>..rightIndex+1) 
			nodes.get(i).transform(i, interpolatedPosition, false, 1)
		nodes.get(rightIndex).transform(rightIndex, interpolatedPosition, false, rightIndex - interpolatedPosition)
		nodes.get(leftIndex).transform(leftIndex, interpolatedPosition, true, interpolatedPosition - leftIndex)
	}
	
	protected def transform(XNode node, int i, double interpolatedPosition, boolean isLeft, double fraction) {
		val distanceFromSelection = abs(i-interpolatedPosition)
		val opacity = 1 - 0.2 * distanceFromSelection
		if(opacity < 0)
			node.visible = false
		else {
			node.opacity = opacity
			node.visible = true
		}
		if(distanceFromSelection < 1E-5) {
			nodes.get(i).effect = null
			node.layoutX = - 0.5 * node.layoutBounds.width
			node.layoutY = - 0.5 * node.layoutBounds.height
		} else {
			node.layoutX = 0
			node.layoutY = 0
			val trafo = new Affine()
			val direction = if(isLeft) -1 else 1
			trafo.translate(-0.5 * nodes.get(i).layoutBounds.width, -0.5 * nodes.get(i).layoutBounds.height, 0)
			trafo.rotate(direction * angle * fraction, new Point3D(0,1,0))
			trafo.translate((i - interpolatedPosition) * deltaX + 0.5 * fraction * direction * gap, 0, fraction * deltaZ + 200)
			nodes.get(i).effect = nodes.get(i).layoutBounds.mapPerspective(trafo, 200);
		}
		nodes.get(i).toFront
	}
	
}