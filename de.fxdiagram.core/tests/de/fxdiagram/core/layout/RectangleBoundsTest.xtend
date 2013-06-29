package de.fxdiagram.core.layout

import org.junit.Test
import static org.junit.Assert.*

import javafx.scene.shape.Rectangle

class RectangleBoundsTest {
	@Test
	def testBoundInLocalEvent() {
		val result = newArrayList
		val r = new Rectangle => [
			boundsInLocalProperty.addListener [ 
				result.add(new Object)
			]
		]
		r.width = 7
		assertFalse(result.isEmpty)
	}
	
}