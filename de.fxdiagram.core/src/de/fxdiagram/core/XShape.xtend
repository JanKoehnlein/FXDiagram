package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.behavior.Behavior
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.core.extensions.InitializingMapListener
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableMap
import javafx.geometry.Dimension2D
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.input.MouseEvent

import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*

/**
 * Common superclass of {@link XNode}, {@link XConnection} and {@link XControlPoint}.
 * 
 * <ul>
 * <li>Handles activation and FX node creation.</li>
 * <li>Allows composing {@link Behavior}s.</li>
 * <li>Supports selection.</li>
 * </ul>
 */
@Logging
abstract class XShape extends Parent implements XActivatable {

	ObjectProperty<Node> nodeProperty = new SimpleObjectProperty<Node>(this, 'node') 

	@FxProperty boolean selected
	
	@FxProperty(readOnly=true) boolean isActive
	
	ObservableMap<Class<? extends Behavior>, Behavior> behaviors = observableHashMap
	
	def getNode() {
		if(nodeProperty.get == null) {
			val newNode = createNode
			if(newNode != null) {
				nodeProperty.set(newNode)
				addNodeAsChild(newNode)
			}
		}
		nodeProperty.get
	}
	
	protected def addNodeAsChild(Node newNode) {
		children.add(newNode)
	}	
	
	def nodeProperty() {
		nodeProperty
	}
	
	/**
	 * Override this to create your FX nodes for the non-active shape, e.g. as used by 
	 * node choosers. 
	 * There is no connection to any scene graph yet, so {@link CoreExtensions} will return 
	 * <code>null</code>.
	 */
	protected def Node createNode()
	
	/**
	 * Override this to update your FX node, e.g. on connection or on activation.
	 * You are now connected to the scenegraph, and can safely use {@link CoreExtensions}
	 * to access the diagram/root.
	 * 
	 * This method may be called multiple times.
	 */
	def initializeGraphics() { 
		if(getNode() == null)
			LOG.severe("Node is null")
	}
	
	/**
	 * Don't override this unless you know what you're doing. 
	 * Override {@link #doActivate} instead.
	 */
	override activate() {
		if(!isActive) {
			try {
				isActiveProperty.set(true)
				initializeGraphics
				doActivate
				selectedProperty.addInitializingListener (new InitializingListener => [
					set = [
						selectionFeedback(it)
					]
				])
				behaviors.addInitializingListener(new InitializingMapListener => [
					put = [ key, Behavior value | value.activate ]
				])
			} catch (Exception exc) {
				LOG.severe(exc.message)
				exc.printStackTrace
			}
		}
	}
	
	/**
	 * Override this to add behavior and register event listeners on activation. 
	 */
	protected def void doActivate()
	
	def <T extends Behavior> T getBehavior(Class<T> key) {
		behaviors.get(key) as T
	}
	
	def addBehavior(Behavior behavior) {
		behaviors.put(behavior.behaviorKey, behavior)
		if(isActive)
			behavior.activate
	}
	
	def removeBehavior(Class<? extends Behavior> key) {
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

	/**
	 * The 'real' bounds for layout calculation, i.e. without any scaling caused by selection. 
	 */
	def getSnapBounds() {
		boundsInLocal
	}
	
	def getAutoLayoutDimension() {
		new Dimension2D(snapBounds.width, snapBounds.height)
	}
}
