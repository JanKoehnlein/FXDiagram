package de.fxdiagram.examples.slides

import de.fxdiagram.core.behavior.AbstractHostBehavior
import de.fxdiagram.core.behavior.NavigationBehavior
import javafx.animation.FadeTransition
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.shape.Rectangle

import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.animation.Transition
import java.util.Map

class ClickThroughSlide extends Slide {
	
	Pane pane = new Pane
	Node currentNode 
	Map<Node, Transition> revealTransitions = newHashMap
	
	new(String name) {
		super(name)
		stackPane.children += pane
	}
	
	override doActivate() {
		super.doActivate()
		pane => [
			setPrefSize(scene.width, scene.height)
			clip = new Rectangle(0, 0, scene.width, scene.height)
			children.tail.forEach[opacity = 0]
		]
		currentNode = pane.children.head
		addBehavior(new RevealBehavior(this))
	}

	def setRevealTransition(Node node, Transition transition) {
		revealTransitions.put(node, transition)
	}

	def getRevealTransition(Node childNode) {
		if(revealTransitions.containsKey(childNode)) 
			revealTransitions.get(childNode) 
		else new FadeTransition() => [
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
			node.revealTransition.play
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