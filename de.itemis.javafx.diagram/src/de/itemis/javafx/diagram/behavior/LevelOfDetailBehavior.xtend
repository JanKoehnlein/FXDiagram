package de.itemis.javafx.diagram.behavior

import de.itemis.javafx.diagram.XActivatable
import de.itemis.javafx.diagram.XNode
import java.util.List
import javafx.geometry.Bounds
import javafx.scene.Node

import static extension de.itemis.javafx.diagram.Extensions.*

class LevelOfDetailBehavior extends AbstractBehavior {

	List<Double> thresholds = newArrayList
	
	List<Node> children = newArrayList 

	new(XNode host, Node defaultChild) {
		super(host)
		children += defaultChild
		if(defaultChild instanceof XActivatable)
			(defaultChild as XActivatable).activate
		defaultChild.visible = true
	}
	
	override doActivate() {
		host.rootDiagram.boundsInParentProperty.addListener [
			bounds, oldBounds, newBounds |
			val newIndex = host.localToScene(host.boundsInLocal).value.childIndexForValue
			val child = children.get(newIndex)
			if(child instanceof XActivatable)
				(child as XActivatable).activate
			children.forEach[visible = it == child]
		]		
	}
	
	def addChildForThreshold(double threshold, Node child) {
		val childIndex = threshold.childIndexForValue
		thresholds.add(childIndex, threshold)
		children.add(childIndex + 1, child)
		child.visible = false
	}
	
	def protected getValue(Bounds bounds) {
		if(bounds != null) 
			bounds.width * bounds.height
		else 
			0.0
	}
	
	def protected getChildIndexForValue(double value) {
		var index = 0
		for(threshold: thresholds) {
			if(threshold > value)
				return index
			else
				index = index + 1	
		}
		index
	}
}