package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.anchors.ArrowHead
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport
import de.fxdiagram.core.behavior.Behavior
import de.fxdiagram.core.behavior.DiagramNavigationBehavior
import de.fxdiagram.core.behavior.NavigationBehavior
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.core.extensions.InitializingMapListener
import de.fxdiagram.core.viewport.ViewportTransform
import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.transform.Transform

import static java.lang.Math.*
import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.layout.LayoutType
import de.fxdiagram.core.extensions.CoreExtensions

/**
 * A diagram with {@link XNode}s and {@link XConnection}s.
 * 
 * Being added to the {@link #nodes} or {@link #connections}, {@link XShapes} will get 
 * automatically be activated.
 * 
 * To find the {@link XDiagram} of an {@link XShape}, use the {@link CoreExtensions}.
 * 
 * Diagrams can be nested ({@link parentDiagram}).  
 * An optional {@link contentsInitializer} allows lazy population of the diagram.
 * Just like an {@link XShape}, an {@link XDiagram} can be customized with composable 
 * {@link Behavior}s.
 *  
 * A {@link viewportTransform} stores the current viewport of the diagram.
 */
@ModelNode('nodes', 'connections', 'parentDiagram')
class XDiagram extends Group implements XActivatable {
	
	@FxProperty ObservableList<XNode> nodes = observableArrayList
	@FxProperty ObservableList<XConnection> connections = observableArrayList

	@FxProperty ObservableMap<Node, Pos> fixedButtons = observableMap(newHashMap)

	@FxProperty(readOnly=true) boolean isActive
	@FxProperty(readOnly=true) boolean isRootDiagram
	@FxProperty boolean isLayoutOnActivate

	@FxProperty Paint backgroundPaint = Color.WHITE
	@FxProperty Paint foregroundPaint = Color.BLACK
	@FxProperty Paint connectionPaint = Color.gray(0.2)
	
	Group nodeLayer = new Group
	Group buttonLayer = new Group
	
	@FxProperty(readOnly=true) XDiagram parentDiagram

	(XDiagram)=>void contentsInitializer

	AuxiliaryLinesSupport auxiliaryLinesSupport
	
	ObservableMap<Class<? extends Behavior>, Behavior> behaviors = observableHashMap
	
	ViewportTransform viewportTransform
	
	boolean needsCentering = true

	new() {
		children += nodeLayer
		children += buttonLayer
		isRootDiagramProperty.set(true)
		parentProperty.addListener [
			property, oldValue, newValue |
			parentDiagramProperty.set(newValue.diagram)
			isRootDiagramProperty.set(parentDiagram == null)
		]
		isRootDiagramProperty.addListener [
			property, oldValue, newValue |
			if(newValue) 
				nodeLayer.children += connections
			else 
				nodeLayer.children -= connections
		]
		viewportTransform = new ViewportTransform
		transforms.setAll(viewportTransform.transform)
		transforms.addListener([ListChangeListener.Change<? extends Transform> change | 
			throw new IllegalStateException("Illegal attempt to change the transforms of an XDiagram")
		])
	}
	
	def getViewportTransform() {
		viewportTransform
	}
	
	override activate() {
		if(!isActive) {
			isActiveProperty.set(true);
			doActivate
		}
	}
	
	def void doActivate() {
		nodes.addInitializingListener(new InitializingListListener<XNode>() => [
			add = [
				initializeGraphics
				getNodeLayer.children += it
				if(XDiagram.this.isActive)
					it.activate
			]
			remove = [
				diagram.getNodeLayer.children -= it
			]
		])
		val arrowHeadListener = new InitializingListener<ArrowHead> => [
			set = [ 
				if(!connectionLayer.children.contains(it)) {
					initializeGraphics					
					connectionLayer.children += it
				}
			]
			unset = [ connectionLayer.children -= it ]
		]
		val labelListener = new InitializingListListener<XConnectionLabel> => [
			add = [ 
				if(!connectionLayer.children.contains(it)) 
					connectionLayer.children += it
			]
			remove = [ connectionLayer.children -= it ]
		]
		connections.addInitializingListener(new InitializingListListener<XConnection>() => [
			add = [
				initializeGraphics
				getConnectionLayer.children += it
				if(XDiagram.this.isActive)
					it.activate
				labelsProperty.addInitializingListener(labelListener)
				sourceArrowHeadProperty.addInitializingListener(arrowHeadListener)
				targetArrowHeadProperty.addInitializingListener(arrowHeadListener)
			]
			remove = [
				diagram.getConnectionLayer.children -= it
				labelsProperty.removeInitializingListener(labelListener)
				sourceArrowHeadProperty.removeInitializingListener(arrowHeadListener)
				targetArrowHeadProperty.removeInitializingListener(arrowHeadListener)
			]
			
		])
		auxiliaryLinesSupport = new AuxiliaryLinesSupport(this)
		contentsInitializer?.apply(this)
		if(getBehavior(NavigationBehavior) == null) 
			addBehavior(new DiagramNavigationBehavior(this))
		behaviors.addInitializingListener(new InitializingMapListener => [
			put = [ key, Behavior value | value.activate() ]
		])
		if(isLayoutOnActivate) {
			isLayoutOnActivate = false
			root.commandStack.execute(new Layouter().createLayoutCommand(LayoutType.DOT, this, 250.millis))
		}
	}
	
	def void centerDiagram(boolean useForce) {
		if(needsCentering || useForce) {
			layout
			if(layoutBounds.width * layoutBounds.height > 1) {
				val scale = min(1, min(scene.width / layoutBounds.width, scene.height / layoutBounds.height))
				viewportTransform.scale = scale
				val centerInScene = localToScene(boundsInLocal).center
				viewportTransform.setTranslate(0.5 * scene.width - centerInScene.x, 0.5 * scene.height - centerInScene.y)
			}
		}
		needsCentering = false
	}
		
	def <T extends Behavior> T getBehavior(Class<T> key) {
		behaviors.get(key) as T
	}
	
	def addBehavior(Behavior behavior) {
		behaviors.put(behavior.behaviorKey, behavior)
	}
	
	def removeBehavior(String key) {
		behaviors.remove(key)
	}
	
	protected def addArrowHead(Property<? extends Node> property, 
		ChangeListener<? super Node> listener) {
		if(property.value != null && !connectionLayer.children.contains(property.value))
			connectionLayer.children += property.value
		property.addListener(listener)
	} 
	
	def getAuxiliaryLinesSupport() {
		auxiliaryLinesSupport	
	}
	
	def Iterable<XShape> getAllShapes() {
		allChildren.filter(XShape)
	}

	def setContentsInitializer((XDiagram)=>void contentsInitializer) {
		this.contentsInitializer = contentsInitializer
	}
	
	def getNodeLayer() {
		nodeLayer
	}
	
	def getConnectionLayer() {
		if(isRootDiagram) 
			nodeLayer
		else 
			parentDiagram.nodeLayer		
	}
	
	def getButtonLayer() {
		buttonLayer
	}
}

	
