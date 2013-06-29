package de.fxdiagram.core.layout

import org.junit.Test
import javafx.scene.shape.Rectangle
import static org.junit.Assert.*
class ClippingTest {

	static val EPS = 1E-6

	@Test def boundsInLocalConsiderClipping() {
		val rect = new Rectangle => [
			x = 1
			y = 2
			width = 3
			height = 4
			clip = new Rectangle => [
				x = 2
				y = 3
				width = 1
				height = 1
			]
		]
		assertEquals(2, rect.boundsInLocal.minX, EPS)
		assertEquals(3, rect.boundsInLocal.minY, EPS)
		assertEquals(1, rect.boundsInLocal.width, EPS)
		assertEquals(1, rect.boundsInLocal.height, EPS)
	}
}