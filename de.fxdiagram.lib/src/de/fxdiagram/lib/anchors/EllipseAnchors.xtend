package de.fxdiagram.lib.anchors

import de.fxdiagram.core.XNode
import de.fxdiagram.core.anchors.Anchors
import javafx.geometry.Point2D
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.Point2DExtensions.*

@FinalFieldsConstructor
class EllipseAnchors implements Anchors {

	val XNode host

	override getAnchor(double x, double y) {
		val bounds = host.node.localToRootDiagram(host.node.boundsInLocal)
		if(bounds == null)
			return null
		val center = bounds.center
		val angle = atan2(y - center.y, x - center.x)		
		return center + new Point2D(0.5 * bounds.width * cos(angle), 0.5 * bounds.height * sin(angle)) 
	}
}