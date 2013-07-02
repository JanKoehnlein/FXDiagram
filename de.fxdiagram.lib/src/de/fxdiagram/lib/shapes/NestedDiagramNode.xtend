package de.fxdiagram.lib.shapes

import de.fxdiagram.core.XAbstractDiagram
import de.fxdiagram.core.XNestedDiagram
import de.fxdiagram.core.XNode
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.Text

import static extension de.fxdiagram.core.Extensions.*

class NestedDiagramNode extends XNode {

	String name

	Pane pane

	Node label

	XNestedDiagram innerDiagram

	static int nr = 0

	public static val (XAbstractDiagram)=>void dummyDiagramContent = [
		addNode(
			new SimpleNode("Inner " + nr) => [
				relocate(0,0)
			])
		addNode(
			new SimpleNode("Inner " + nr + 1) => [
				relocate(100,100)
			])
		addNode(new NestedDiagramNode("Nested " + nr + 2) => [
				it.relocate(50, 50)
			])
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
					style = "-fx-background-color: white;"
					contentsInitializer = [
						it => diagramContents
						width = label.layoutBounds.width + 40 
						height = label.layoutBounds.height + 20
					]
				]
				StackPane.setMargin(it, new Insets(3, 3, 3, 3))
			]
		]
		key = name
	}

	override doActivate() {
		super.doActivate()
		rootDiagram.boundsInParentProperty.addListener [ prop, oldVal, newVal |
			val bounds = localToScene(boundsInLocal)
			val area = bounds.width * bounds.height
			if (area <= 100000) {
				label.visible = true
				innerDiagram.visible = false
			} else {
				label.visible = false
				innerDiagram.visible = true
				innerDiagram.activate					
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
