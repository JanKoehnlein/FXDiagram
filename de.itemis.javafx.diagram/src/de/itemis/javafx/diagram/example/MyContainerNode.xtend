package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XNestedDiagram
import de.itemis.javafx.diagram.XNode
import de.itemis.javafx.diagram.behavior.LevelOfDetailBehavior
import javafx.scene.control.Label
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle

import static extension de.itemis.javafx.diagram.example.MyContainerNode.*

class MyContainerNode extends XNode {

	AddRapidButtonBehavior rapidButtonBehavior

	LevelOfDetailBehavior levelOfDetailBehavior
	
	Label label
	
	XNestedDiagram innerDiagram
	
	static int nr = 0

	new(String name) {
		label = new Label => [
			text = name
		]
		innerDiagram = new XNestedDiagram => [
			contentsInitializer = [
				val innerNode = new MyContainerNode("Inner " + nr)
				nr = nr + 1
				addNode(innerNode)
				innerNode.relocate(96, 35)
			]
		]
		node = new StackPane => [
			val rectangle = new Rectangle => [
				width = 80
				height = 30
				fill = createFill
				stroke = Color::gray(0.5)
				strokeWidth = 1.2
				arcWidth = 12
				arcHeight = 12
			]
			children += rectangle
			children += label
			children += innerDiagram
			
			clip = new Rectangle => [
				width = rectangle.width + rectangle.strokeWidth
				height = rectangle.height + rectangle.strokeWidth			
			]
		]
		node.effect = new InnerShadow => [
			radius = 7
		]
	}
	
	override doActivate() {
		super.doActivate()
		rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate
		levelOfDetailBehavior = new LevelOfDetailBehavior(this, node as Pane, label)
		levelOfDetailBehavior.addChildForThreshold(10000.0, innerDiagram)
		levelOfDetailBehavior.activate
	}

	def protected createFill() {
		val stops = newArrayList(
			new Stop(0, Color::gray(0.6)), 
			new Stop(1, Color::gray(0.9))
		)
		new LinearGradient(0, 0, 1, 1, true, CycleMethod::NO_CYCLE, stops)
	}
	
	override toString() {
		label.text
	}
	
}
