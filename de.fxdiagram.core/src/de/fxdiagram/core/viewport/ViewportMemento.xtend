package de.fxdiagram.core.viewport

import static de.fxdiagram.core.extensions.Point2DExtensions.*
import static java.lang.Math.*

@Data
class ViewportMemento {
	double translateX
	double translateY
	double scale
	double rotate
	
	def double dist(ViewportMemento other) {
		val delta = norm(translateX - other.translateX, translateY - other.translateY)
		val deltaScale = 500 * log(max(scale, other.scale) / min(scale, other.scale))
		val deltaAngle =  7 * abs(rotate - other.rotate)
		println(delta + " " + deltaScale + " " + deltaAngle)
		delta + deltaScale + deltaAngle
	}
	
}