package de.itemis.javafx.diagram.behavior

import de.itemis.javafx.diagram.XNode
import java.util.List
import javafx.beans.value.ChangeListener
import javafx.geometry.Bounds
import javafx.scene.Node
import javafx.scene.layout.Pane

import static extension de.itemis.javafx.diagram.Extensions.*
import de.itemis.javafx.diagram.XActivatable

class LevelOfDetailBehavior extends AbstractBehavior {

	Pane parent

	List<Double> thresholds = newArrayList
	
	List<Node> children = newArrayList 

	new(XNode host, Pane parent, Node defaultChild) {
		super(host)
		this.parent = parent
		children += defaultChild
		if(defaultChild instanceof XActivatable)
			(defaultChild as XActivatable).activate
		defaultChild.visible = true
	}
	
	override doActivate() {
		val ChangeListener<Bounds> boundsListener = [
			bounds, oldBounds, newBounds |
			val newIndex = host.localToScene(host.boundsInLocal).value.childIndexForValue
			val child = children.get(newIndex)
			if(child instanceof XActivatable)
				(child as XActivatable).activate
			children.forEach[visible = it == child]
		]
		host.diagram.boundsInParentProperty.addListener(boundsListener)		
	}
	
	def addChildForThreshold(double threshold, Node child) {
		val childIndex = threshold.childIndexForValue
		thresholds.add(childIndex, threshold)
		children.add(childIndex + 1, child)
		child.visible = false
	}
	
	def protected getValue(Bounds bounds) {
		val absBounds = host?.localToRoot(bounds)
		if(absBounds != null) { 
			val area = absBounds.width * absBounds.height
			area
		} else 
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