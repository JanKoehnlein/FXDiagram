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
		norm(translateX - other.translateX, translateY - other.translateY) 
			+ 10 * abs((scale - other.scale)/scale) + 10 * abs(rotate - other.rotate)
	}
	
}