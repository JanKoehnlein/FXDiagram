package de.fxdiagram.core.layout

import javafx.scene.Group
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle
import org.junit.Test

import static org.junit.Assert.*
import de.fxdiagram.core.XNode
import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.paint.Color

import static extension de.fxdiagram.core.debug.Debug.*
import javafx.scene.Parent

class UnderstandigLayout extends Application {

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

	@Test def groupLyingAboutItsSizes() {
		val stackPane = new StackPane => [
			children += new GroupWithFixedSize => [
				children += new Rectangle => [
					width = 3
					height = 3
					relocate(-1, -1)
				]
			]
		]
		assertEquals(1, stackPane.minWidth(-1), EPS)
		assertEquals(1, stackPane.minHeight(-1), EPS)
		assertEquals(1, stackPane.prefWidth(-1), EPS)
		assertEquals(1, stackPane.prefHeight(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxWidth(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxHeight(-1), EPS)

		stackPane.layout
		assertEquals(1, stackPane.minWidth(-1), EPS)
		assertEquals(1, stackPane.minHeight(-1), EPS)
		assertEquals(1, stackPane.prefWidth(-1), EPS)
		assertEquals(1, stackPane.prefHeight(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxWidth(-1), EPS)
		assertEquals(Double.MAX_VALUE, stackPane.maxHeight(-1), EPS)
	}

	def static main(String... args) {
		launch()
	}

	override start(Stage stage) throws Exception {
		val innerRectangle = new Rectangle
		val stackPane = new StackPane => [
			children += new GroupWithFixedSize => [
				children += innerRectangle => [
					width = 300
					height = 300
				]
			]
			children += new Rectangle => [
				width = 100
				height = 100
				relocate(250, 250)
				fill = Color.GREEN
			]
		]
		stage.scene = new Scene(stackPane, 640, 480)
		stage.show
		stackPane.dumpLayout
		stackPane.children.head.dumpLayout
		stackPane.children.last.dumpLayout
		innerRectangle.dumpBounds
	} //	INFO: Bounds of Rectangle:
//	(-200.0:-200.0) BoundingBox [minX:0.0, minY:0.0, minZ:0.0, width:300.0, height:300.0, depth:0.0, maxX:300.0, maxY:300.0, maxZ:0.0]
//	in GroupWithFixedSize: (-400.0:-400.0) BoundingBox [minX:-200.0, minY:-200.0, minZ:0.0, width:300.0, height:300.0, depth:0.0, maxX:100.0, maxY:100.0, maxZ:0.0]
//	in Group: (-400.0:-400.0) BoundingBox [minX:-200.0, minY:-200.0, minZ:0.0, width:300.0, height:300.0, depth:0.0, maxX:100.0, maxY:100.0, maxZ:0.0]
}

class GroupWithFixedSize extends Group {

	override isResizable() {
		true
	}
	
	override minWidth(double arg0) {
		new Exception().printStackTrace
		100
	}

	override minHeight(double arg0) {
		new Exception().printStackTrace
		100
	}

	override prefWidth(double arg0) {
		new Exception().printStackTrace
		100
	}

	override prefHeight(double arg0) {
		new Exception().printStackTrace
		100
	}

	override maxWidth(double arg0) {
		new Exception().printStackTrace
		100
	}

	override maxHeight(double arg0) {
		new Exception().printStackTrace
		100
	}
}
