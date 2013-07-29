package de.fxdiagram.lib.simple

import de.fxdiagram.core.XAbstractDiagram
import de.fxdiagram.core.XNestedDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.Text

import static extension de.fxdiagram.core.Extensions.*

class NestedDiagramNode extends XNode {

	String name

	RectangleBorderPane pane

	Node label

	XNestedDiagram innerDiagram

	static int nr = 0

	public static val (XAbstractDiagram)=>void dummyDiagramContent = [
		nodes += new SimpleNode("Inner " + nr) => [
			relocate(0,0)
		]
		nodes += new SimpleNode("Inner " + nr + 1) => [
			relocate(100,100)
		]
		nodes += new NestedDiagramNode("Nested " + nr + 2) => [
			it.relocate(50, 50)
		]
		nr = nr + 3
	]

	new(String name) {
		this(name, dummyDiagramContent)
	}

	new(String name, (XNestedDiagram)=>void diagramContents) {
		this.name = name
		pane = new RectangleBorderPane
		node = pane => [
			children += label = new Text => [
				text = name
				textOrigin = VPos.TOP
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
			children += new Group => [
				children += innerDiagram = new XNestedDiagram => [
					contentsInitializer = [
						it => diagramContents
						width = label.layoutBounds.width + 40 
						height = label.layoutBounds.height + 20
					]
				]
			]
		]
		key = name
	}
	
	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	override doActivate() {
		super.doActivate()
		diagram.subDiagrams += innerDiagram
		rootDiagram.boundsInParentProperty.addListener [ prop, oldVal, newVal |
			val bounds = localToScene(boundsInLocal)
			val area = bounds.width * bounds.height
			if (area <= 100000) {
				label.visible = true
				innerDiagram.visible = false
				pane.backgroundPaint = RectangleBorderPane.DEFAULT_BACKGROUND
			} else {
				label.visible = false
				innerDiagram.visible = true
				innerDiagram.activate
				pane.backgroundPaint = Color.WHITE
			}
		]
		val rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate
	}

	def protected createFill() {
		val stops = newArrayList(
			new Stop(0, Color.gray(0.6)),
			new Stop(1, Color.gray(0.9))
		)
		new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops)
	}

	override toString() {
		name
	}
}
