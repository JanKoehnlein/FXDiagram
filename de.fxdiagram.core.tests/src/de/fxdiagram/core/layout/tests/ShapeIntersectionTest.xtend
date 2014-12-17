package de.fxdiagram.core.layout.tests

import org.junit.Test
import javafx.scene.shape.Circle
import javafx.scene.shape.Polyline
import javafx.scene.shape.Shape

class ShapeIntersectionTest {
	@Test
	def void testCirclePolyline() {
		val circle = new Circle => [
			radius = 1
			fill = null
		]
		val line = new Polyline => [
			points += #[-1.0 ,-1.0 ,1.0 ,1.0]
		] 
		val intersect = Shape.intersect(circle, line)
		println(intersect)
	}
}