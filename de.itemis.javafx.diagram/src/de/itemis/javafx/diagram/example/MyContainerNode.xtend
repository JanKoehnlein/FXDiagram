package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XActivatable
import de.itemis.javafx.diagram.XNestedDiagram
import de.itemis.javafx.diagram.XNode
import de.itemis.javafx.diagram.behavior.LevelOfDetailBehavior
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.effect.InnerShadow
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle

class MyContainerNode extends XNode {

	static int nr = 0

    String name
    
	Node label
	Node innerDiagram

	new(String name) {
		this.name = name
		label = new StackPane => [
			effect = new InnerShadow => [
				radius = 7
			]
			children += createRectangle => [
				fill = createFill
			]
			children += new Label => [
				text = name
			]
		]
		innerDiagram = new ActivateableStackPane => [
			children += createRectangle => [
				fill = Color::WHITE
			] 
			children += new XNestedDiagram => [
				contentsInitializer = [
					val innerNode = new MyContainerNode("Inner " + nr)
					nr = nr + 1
					addNode(innerNode)
					innerNode.relocate(96, 35)
				]
			]
			clip = createRectangle => [
				width = width + 2 * strokeWidth
				height = height + 2 * strokeWidth
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
		levelOfDetailBehavior.addChildForThreshold(20000.0, innerDiagram)
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

	override protected createMouseOverEffect() {
		null
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
