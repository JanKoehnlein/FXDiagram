package de.fxdiagram.core.geometry

import javafx.scene.shape.CubicCurve
import javafx.geometry.Point2D
import javafx.scene.shape.QuadCurve

import static de.fxdiagram.core.geometry.Point2DExtensions.*

/**
 * De Casteljau algorithm
 */
class BezierExtensions {

	static def at(CubicCurve c, double t) {
		val abx = linear(c.startX, c.controlX1, t)
		val aby = linear(c.startY, c.controlY1, t)
		val bcx = linear(c.controlX1, c.controlX2, t)
		val bcy = linear(c.controlY1, c.controlY2, t)
		val cdx = linear(c.controlX2, c.endX, t)
		val cdy = linear(c.controlY2, c.endY, t)

		val acx = linear(abx, bcx, t)
		val acy = linear(aby, bcy, t)
		val bdx = linear(bcx, cdx, t)
		val bdy = linear(bcy, cdy, t)

		new Point2D(linear(acx, bdx, t), linear(acy, bdy, t))
	}

	static def at(QuadCurve c, double t) {
		val abx = linear(c.startX, c.controlX, t)
		val aby = linear(c.startY, c.controlY, t)
		val bcx = linear(c.controlX, c.endX, t)
		val bcy = linear(c.controlY, c.endY, t)

		new Point2D(linear(abx, bcx, t), linear(aby, bcy, t))
	}

	static def derivativeAt(CubicCurve c, double t) {
		val abx = linear(c.startX, c.controlX1, t)
		val aby = linear(c.startY, c.controlY1, t)
		val bcx = linear(c.controlX1, c.controlX2, t)
		val bcy = linear(c.controlY1, c.controlY2, t)
		val cdx = linear(c.controlX2, c.endX, t)
		val cdy = linear(c.controlY2, c.endY, t)

		val acx = linear(abx, bcx, t)
		val acy = linear(aby, bcy, t)
		val bdx = linear(bcx, cdx, t)
		val bdy = linear(bcy, cdy, t)

		new Point2D(bdx - acx, bdy - acy)
	}

	static def derivativeAt(QuadCurve c, double t) {
		val abx = linear(c.startX, c.controlX, t)
		val aby = linear(c.startY, c.controlY, t)
		val bcx = linear(c.controlX, c.endX, t)
		val bcy = linear(c.controlY, c.endY, t)

		new Point2D(bcx - abx, bcy - aby)
	}

}
