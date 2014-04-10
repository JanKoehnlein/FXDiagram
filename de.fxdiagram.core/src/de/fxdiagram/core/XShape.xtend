package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.behavior.Behavior
import de.fxdiagram.core.extensions.InitializingMapListener
import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableMap
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.input.MouseEvent

import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.annotations.logging.Logging
import javafx.beans.property.SimpleObjectProperty
import de.fxdiagram.core.extensions.InitializingListener

@Logging
abstract class XShape extends Parent implements XActivatable {

	ObjectProperty<Node> nodeProperty = new SimpleObjectProperty<Node>(this, 'node') 

	@FxProperty boolean selected
	
	@FxProperty@ReadOnly boolean isActive
	
	ObservableMap<Class<? extends Behavior>, Behavior> behaviors = observableHashMap
	
	def getNode() {
		if(nodeProperty.get == null) {
			val newNode = createNode
			if(newNode != null) {
				nodeProperty.set(newNode)
				children.add(newNode)
			}
		}
		nodeProperty.get
	}	
	
	def nodeProperty() {
		nodeProperty
	}
	
	protected def Node createNode()
	
	def initializeGraphics() {  
		if(getNode() == null)
			LOG.severe("Node is null")
	}

	override activate() {
		if(!isActive) {
			initializeGraphics
			doActivate
			isActiveProperty.set(true)
			selectedProperty.addInitializingListener (new InitializingListener => [
				set = [
					selectionFeedback(it)
					if(it)
						toFront
				]
			])
			behaviors.addInitializingListener(new InitializingMapListener => [
				put = [ key, Behavior value | value.activate ]
			])
		}
	}
	
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
