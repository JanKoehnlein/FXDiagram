package de.fxdiagram.core.layout

import javafx.scene.Group
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle
import org.junit.Test

import static org.junit.Assert.*
import de.fxdiagram.core.XNode

class UnderstandigLayout {
	
	static val EPS = 1e-6
	
	@Test def groupSizes() {
		val group = new Group => [
			children += new Rectangle => [
				width = 1
				height = 2
				relocate(-3, -4)
			]
		]
		assertEquals(1, group.minWidth(-1), EPS)
		assertEquals(2, group.minHeight(-1), EPS)
		assertEquals(1, group.prefWidth(-1), EPS)
		assertEquals(2, group.prefHeight(-1), EPS)
		assertEquals(1, group.maxWidth(-1), EPS)
		assertEquals(2, group.maxHeight(-1), EPS)

		assertEquals(1, group.minWidth(10), EPS)
		assertEquals(2, group.minHeight(10), EPS)
		assertEquals(1, group.prefWidth(10), EPS)
		assertEquals(2, group.prefHeight(10), EPS)
		assertEquals(1, group.minWidth(10), EPS)
		assertEquals(2, group.minHeight(10), EPS)
	}
	
	@Test def stackPaneSizes() {
		val stackPane = new StackPane => [
			children += new Rectangle => [
				width = 1
				height = 2
				relocate(-3, -4)
			]
		]
		assertEquals(1, stackPane.minWidth(-1), EPS)
		assertEquals(2, stackPane.minHeight(-1), EPS)
		assertEquals(1, stackPane.prefWidth(-1), EPS)
		assertEquals(2, stackPane.prefHeight(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxWidth(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxHeight(-1), EPS)
		
		assertEquals(1, stackPane.minWidth(10), EPS)
		assertEquals(2, stackPane.minHeight(10), EPS)
		assertEquals(1, stackPane.prefWidth(10), EPS)
		assertEquals(2, stackPane.prefHeight(10), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxWidth(10), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxHeight(10), EPS)
		
		stackPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE)
		assertEquals(1, stackPane.maxWidth(-1), EPS)
		assertEquals(2, stackPane.maxHeight(-1), EPS)
		assertEquals(1, stackPane.maxWidth(10), EPS)
		assertEquals(2, stackPane.maxHeight(10), EPS)
	}
	
	@Test def stackPaneWithAnXNodeSizes() {
		val stackPane = new StackPane => [
			children += new XNode => [
				width = 1
				height = 2
				relocate(-3, -4)
			]
		]
		assertEquals(1, stackPane.minWidth(-1), EPS)
		assertEquals(2, stackPane.minHeight(-1), EPS)
		assertEquals(1, stackPane.prefWidth(-1), EPS)
		assertEquals(2, stackPane.prefHeight(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxWidth(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxHeight(-1), EPS)
		
		assertEquals(1, stackPane.minWidth(10), EPS)
		assertEquals(2, stackPane.minHeight(10), EPS)
		assertEquals(1, stackPane.prefWidth(10), EPS)
		assertEquals(2, stackPane.prefHeight(10), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxWidth(10), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxHeight(10), EPS)
		
		stackPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE)
		assertEquals(1, stackPane.maxWidth(-1), EPS)
		assertEquals(2, stackPane.maxHeight(-1), EPS)
		assertEquals(1, stackPane.maxWidth(10), EPS)
		assertEquals(2, stackPane.maxHeight(10), EPS)
	}
	
	@Test def groupLayout() {
		val group = new Group => [
			children += new Rectangle => [
				width = 2
				height = 1
			]
			children += new Rectangle => [
				width = 1
				height = 2
				relocate(-3, -4)
			]
		]
		assertEquals(0, group.children.get(0).layoutX, EPS)
		assertEquals(-3, group.children.get(1).layoutX, EPS)
		// the group takes layoutX/Y into account when calculating the width
		assertEquals(5, group.minWidth(-1), EPS)
		assertEquals(5, group.minHeight(-1), EPS)
		assertEquals(5, group.prefWidth(-1), EPS)
		assertEquals(5, group.prefHeight(-1), EPS)
		assertEquals(5, group.maxWidth(-1), EPS)
		assertEquals(5, group.maxHeight(-1), EPS)
		
		group.layout
		// layout does not touch any layout positions
		assertEquals(0, group.children.get(0).layoutX, EPS)
		assertEquals(-3, group.children.get(1).layoutX, EPS)
		assertEquals(5, group.minWidth(-1), EPS)
		assertEquals(5, group.minHeight(-1), EPS)
		assertEquals(5, group.prefWidth(-1), EPS)
		assertEquals(5, group.prefHeight(-1), EPS)
		assertEquals(5, group.maxWidth(-1), EPS)
		assertEquals(5, group.maxHeight(-1), EPS)
	}
	
	@Test def stackPaneLayout() {
		val stackPane = new StackPane => [
			children += new Rectangle => [
				width = 2
				height = 1
			]
			children += new Rectangle => [
				width = 1
				height = 2
				relocate(-3, -4)
			]
		]
		assertEquals(0, stackPane.children.get(0).layoutX, EPS)
		assertEquals(-3, stackPane.children.get(1).layoutX, EPS)
		// the stack pane ignores layoutX/Y when calculating the width
		assertEquals(2, stackPane.minWidth(-1), EPS)
		assertEquals(2, stackPane.minHeight(-1), EPS)
		assertEquals(2, stackPane.prefWidth(-1), EPS)
		assertEquals(2, stackPane.prefHeight(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxWidth(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxHeight(-1), EPS)
		
		stackPane.layout
		// layout centers the children around (0, 0)
		assertEquals(-1, stackPane.children.get(0).layoutX, EPS)
		assertEquals(-0.5, stackPane.children.get(1).layoutX, EPS)
		assertEquals(2, stackPane.minWidth(-1), EPS)
		assertEquals(2, stackPane.minHeight(-1), EPS)
		assertEquals(2, stackPane.prefWidth(-1), EPS)
		assertEquals(2, stackPane.prefHeight(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxWidth(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxHeight(-1), EPS)
	}

}