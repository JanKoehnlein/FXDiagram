package de.fxdiagram.lib.anchors

import de.fxdiagram.core.anchors.Anchors
import javafx.geometry.Point2D
import javafx.scene.shape.Shape
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*

@FinalFieldsConstructor
class DiamondAnchors implements Anchors {

	static val CORNER_DELTA = 4

	val Shape shape

	override getAnchor(double x, double y) {
		val bounds = shape.localToRootDiagram(shape.boundsInLocal)
		if(bounds == null)
			return null
		val center = bounds.center
		if (abs(center.x - x) < CORNER_DELTA) {
			if (center.y < y)
				return new Point2D(center.x, bounds.maxY)
			else
				return new Point2D(center.x, bounds.minY)
		}
		if (abs(center.y - y) < CORNER_DELTA) {
			if (center.x < x)
				return new Point2D(bounds.maxX, center.y)
			else
				return new Point2D(bounds.minX, center.y)
		} else if (center.x < x) {
			if (center.y < y) {
				return getCrossing(center.x, bounds.maxY, bounds.maxX, center.y, x, y, center.x, center.y)
			} else {
				return getCrossing(center.x, bounds.minY, bounds.maxX, center.y, x, y, center.x, center.y)

			}
		} else {
			if (center.y < y) {
				return getCrossing(center.x, bounds.maxY, bounds.minX, center.y, x, y, center.x, center.y)

			} else {
				return getCrossing(center.x, bounds.minY, bounds.minX, center.y, x, y, center.x, center.y)
			}
		}
	}

	protected def getCrossing(double xa, double ya, double xb, double yb, double xc, double yc, double xd, double yd) {
		val lambda = ((ya - yc) * (xb - xa) - (xa - xc) * (yb - ya)) / ((yd - yc) * (xb - xa) - (xd - xc) * (yb - ya))
		return new Point2D(xc + lambda * (xd - xc), yc + lambda * (yd - yc))
	}
}
