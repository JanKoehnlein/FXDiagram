package de.fxdiagram.core.layout

import org.junit.Test
import javafx.scene.layout.Pane
import javafx.scene.Group
import static junit.framework.Assert.*
import javafx.scene.shape.Rectangle
import javafx.scene.layout.StackPane

class LocalCoodinateTest {
	@Test
	def void paneWidthIsLocal() {
		val pane = new TestPane		
		assertEquals(100.0, pane.boundsInLocal.width)
		assertEquals(100.0, pane.layoutBounds.width)
	}
	
	@Test
	def void scaleIsLocalToParent() {
		val pane = new TestPane		
		new Group => [
			children += pane
		]
		pane.scaleX = 0.5
		assertEquals(100.0, pane.boundsInLocal.width)
		assertEquals(50.0, pane.boundsInParent.width)
	}
	
	@Test
	def void layoutXIsInNotLayoutBounds() {
		val pane = new TestPane		
		pane.layoutX = 100
		assertEquals(0.0, pane.boundsInLocal.minX)
	}
	
	@Test
	def void stackPane() {
		val r0 = new Rectangle => [
			width = 5
			height = 1
		]
		val r1 = new Rectangle => [
			width = 10
			height = 5
		]
		println(r0.layoutBounds)
		println(r0.layoutX)
		println(r0.layoutY)
		println(r1.layoutBounds)
		println(r1.layoutX)
		println(r1.layoutY)
		val pane = new StackPane => [
			children += r0
			children += r1
			layout
		]
		println(r0.layoutBounds)
		println(r0.layoutX)
		println(r0.layoutY)
		println(r1.layoutBounds)
		println(r1.layoutX)
		println(r1.layoutY)
		println(pane.layoutBounds)
		println(pane.layoutX)
		println(pane.layoutY)
		println(pane.boundsInLocal)
		
	}
	
	@Test 
	def testBoundInLocal() {
		
	}
	
}

class TestPane extends Pane {
	
	new() {
		width = 100
	}
}