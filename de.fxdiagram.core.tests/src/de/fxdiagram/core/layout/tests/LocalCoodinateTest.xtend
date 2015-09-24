package de.fxdiagram.core.layout.tests

import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle
import org.junit.Test

import static org.junit.Assert.*

class LocalCoodinateTest {
	
	val EPSILON = 1e-9
	
	@Test
	def void paneWidthIsLocal() {
		val pane = new TestPane		
		assertEquals(100.0, pane.boundsInLocal.width, EPSILON)
		assertEquals(100.0, pane.layoutBounds.width, EPSILON)
	}
	
	@Test
	def void scaleIsLocalToParent() {
		val pane = new TestPane		
		new Group => [
			children += pane
		]
		pane.scaleX = 0.5
		assertEquals(100.0, pane.boundsInLocal.width, EPSILON)
		assertEquals(50.0, pane.boundsInParent.width, EPSILON)
	}
	
	@Test
	def void layoutXIsInNotLayoutBounds() {
		val pane = new TestPane		
		pane.layoutX = 100
		assertEquals(0.0, pane.boundsInLocal.minX, EPSILON)
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
	
	
	@Test def void stackPaneToRoot() {
		val rect = new Rectangle(-100, -100, 1,1)
		val group = new Group
		val pane = new StackPane
		new Group => [
			children += pane => [
				layoutX = 10
				layoutY = 10
				children += group => [
					managed = false
					children += rect
				]
			]
		]
		println(rect.boundsInLocal)
		println(rect.localToParent(rect.boundsInLocal))
		println(rect.localToParent(rect.boundsInLocal))
		println(group.localToParent(rect.localToParent(rect.boundsInLocal)))
		println(pane.localToParent(group.localToParent(rect.localToParent(rect.boundsInLocal))))
	}
}

class TestPane extends Pane {
	
	new() {
		width = 100
	}
}