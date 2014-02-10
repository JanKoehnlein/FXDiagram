package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.behavior.Behavior
import javafx.collections.MapChangeListener
import javafx.collections.ObservableMap
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.input.MouseEvent

import static javafx.collections.FXCollections.*

abstract class XShape extends Parent implements XActivatable {

	@FxProperty@ReadOnly Node node

	@FxProperty boolean selected
	
	@FxProperty@ReadOnly boolean isActive
	@FxProperty@ReadOnly boolean isPreviewActive
	
	ObservableMap<Class<? extends Behavior>, Behavior> behaviors = observableHashMap
	
	protected def setNode(Node node) {
		nodeProperty.set(node)
		children.setAll(node)
	}
	
	override activate() {
		if(!isActive) {
			activatePreview()
			doActivate
			isActiveProperty.set(true)
			selectedProperty.addListener [
				property, oldVlaue, newValue |
				selectionFeedback(newValue)
				if(newValue)
					toFront
			]
			behaviors.values.forEach[activate]
			val MapChangeListener<Class<? extends Behavior>, Behavior> behaviorActivator = [
				change |
				if(isActive) {
					if(change.wasAdded)
						change.valueAdded.activate
				}
			]
			behaviors.addListener(behaviorActivator)
		}
	}
	
	def activatePreview() {
		if(!isPreviewActive) {
			doActivatePreview()
			isPreviewActiveProperty.set(true)
		}
	}
	
	protected def void doActivatePreview()
	
	protected def void doActivate()
	
	def <T extends Behavior> T getBehavior(Class<T> key) {
		behaviors.get(key) as T
	}
	
	def addBehavior(Behavior behavior) {
		behaviors.put(behavior.behaviorKey, behavior)
	}
	
	def removeBehavior(String key) {
		behaviors.remove(key)
	}
	
	def void selectionFeedback(boolean isSelected) {
	}
	
	def boolean isSelectable() {
		isActive
	}
	
	def select(MouseEvent it) {
		selected = true
	}

	def toggleSelect(MouseEvent it) {
		if (shortcutDown) {
			selected = !selected
		}
	}
	
	def getSnapBounds() {
		boundsInLocal
	}
}
