package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XActivatable
import de.itemis.javafx.diagram.XNestedDiagram
import de.itemis.javafx.diagram.XNode
import de.itemis.javafx.diagram.behavior.LevelOfDetailBehavior
import javafx.geometry.VPos
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

class MyContainerNode extends XNode {

	static int nr = 0

    String name
    
	Node label
	Node innerDiagram

	new(String name) {
		this.name = name
		label = new StackPane => [
			children += createRectangle => [
				fill = createFill
			]
			children += new Text => [
				text = name
				textOrigin = VPos::TOP
			]
		]
		innerDiagram = new ActivateableStackPane => [
			children += createRectangle => [
				fill = Color::WHITE
			] 
			children += new XNestedDiagram => [
				scale = 0.1
				contentsInitializer = [
					val innerNode = new MyContainerNode("Inner " + nr)
					nr = nr + 1
					addNode(innerNode)
					innerNode.relocate(370, 150)
				]
			]
			clip = createRectangle => [
				x = strokeWidth
				y = strokeWidth
				width = width
				height = height
			]
		]
		node = new StackPane => [
			children += label
			children += innerDiagram
		]
	}
	
	override doActivate() {
		super.doActivate()
		val rapidButtonBehavior = new AddRapidButtonBehavior(this)
		rapidButtonBehavior.activate
		val levelOfDetailBehavior = new LevelOfDetailBehavior(this, label)
		levelOfDetailBehavior.addChildForThreshold(100000.0, innerDiagram)
		levelOfDetailBehavior.activate
	}
	
	protected def createRectangle() {
		new Rectangle => [
				width = 80
				height = 30
				stroke = Color::gray(0.5)
				strokeWidth = 1.2
				arcWidth = 12
				arcHeight = 12
		] 
	}

	def protected createFill() {
		val stops = newArrayList(
			new Stop(0, Color::gray(0.6)), 
			new Stop(1, Color::gray(0.9))
		)
		new LinearGradient(0, 0, 1, 1, true, CycleMethod::NO_CYCLE, stops)
	}
	
	override toString() {
		name
	}
}

class ActivateableStackPane extends StackPane implements XActivatable {
	
	override activate() {
		children.filter(typeof(XActivatable)).forEach[activate]
		visibleProperty.addListener [
			element, oldVal, newVal | children.forEach[visible = newVal]
		] 
	}
	
}
