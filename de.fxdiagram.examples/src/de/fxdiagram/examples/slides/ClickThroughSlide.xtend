package de.fxdiagram.examples.slides

import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.behavior.NavigationBehavior
import javafx.animation.FadeTransition
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.image.Image
import javafx.scene.shape.Rectangle

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import de.fxdiagram.annotations.properties.ModelNode

@ModelNode
class ClickThroughSlide extends Slide {
	
	Group pane 
	Node currentNode 
	
	new(String name) {
		super(name)
	}
	
	new(String name, Image backgroundImage) {
		super(name, backgroundImage)
	}
	
	override initializeGraphics() {
		super.initializeGraphics()
		pane => [
			children.tail.forEach[opacity = 0]
		]
		currentNode = pane.children.head
	}
	
	protected override createNode() {
		val node = super.createNode()
		node.children += pane = new Group
		node
	}
	
	override doActivate() {
		super.doActivate()
		pane.clip = new Rectangle(0, 0, scene.width, scene.height)
		addBehavior(new RevealBehavior(this))
	}

	def getRevealTransition(Node childNode) {
		new FadeTransition() => [
			node = childNode
			fromValue = 0
			toValue = 1
			duration = 200.millis
		]
	}

	def next() {
		val children = pane.children
		if(children.empty) {
			return false
		} else {
			val nextIndex = children.indexOf(currentNode) + 1
			if(nextIndex == children.size) 
				return false
			currentNode = children.get(nextIndex)
			currentNode.revealTransition.play
			return true
		}
	}
	
	def previous() {
		val children = pane.children
		if(children.empty) {
			return false
		} else {
			val previousIndex = children.indexOf(currentNode) -1
			if(previousIndex < 0) 
				return false
			currentNode.opacity = 0
			currentNode = children.get(previousIndex)
			return true
		}
	}
	
	def getPane() {
		pane
	}
}

class RevealBehavior extends AbstractHostBehavior<ClickThroughSlide> implements NavigationBehavior {
	
	new(ClickThroughSlide slide) {
		super(slide)
	}
	
	override protected doActivate() {
	}
	
	override getBehaviorKey() {
		NavigationBehavior
	}
	
	override next() {
		host.next
	}
	
	override previous() {
		host.previous
	}
}