package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.anchors.ArrowHead
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport
import de.fxdiagram.core.behavior.Behavior
import de.fxdiagram.core.behavior.DiagramNavigationBehavior
import de.fxdiagram.core.behavior.NavigationBehavior
import de.fxdiagram.core.extensions.AccumulativeTransform2D
import de.fxdiagram.core.extensions.InitializingListListener
import de.fxdiagram.core.extensions.InitializingListener
import de.fxdiagram.core.extensions.InitializingMapListener
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

@ModelNode(#['nodes', 'connections', 'parentDiagram'])
class XDiagram extends Group implements XActivatable {
	
	@FxProperty ObservableList<XNode> nodes = observableArrayList
	@FxProperty ObservableList<XConnection> connections = observableArrayList
	@FxProperty ObservableList<XRapidButton> buttons = observableArrayList

	@FxProperty ObservableMap<Node, Pos> fixedButtons = observableMap(newHashMap)

	@FxProperty @ReadOnly boolean isActive
	@FxProperty @ReadOnly boolean isPreviewActive
	@FxProperty @ReadOnly boolean isRootDiagram

	@FxProperty Paint backgroundPaint = Color.WHITE
	@FxProperty Paint foregroundPaint = Color.BLACK
	@FxProperty Paint connectionPaint = Color.gray(0.2)
	
	Group nodeLayer = new Group
	Group buttonLayer = new Group
	
	@FxProperty /* @ReadOnly */ XDiagram parentDiagram

	(XDiagram)=>void contentsInitializer

	AuxiliaryLinesSupport auxiliaryLinesSupport
	
	ObservableMap<Class<? extends Behavior>, Behavior> behaviors = observableHashMap
	
	AccumulativeTransform2D canvasTransform
	
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
		canvasTransform = new AccumulativeTransform2D
		transforms.setAll(canvasTransform.transform)
		transforms.addListener([ListChangeListener.Change<Transform> change | 
			throw new IllegalStateException("Illegal attempt to change the transforms of an XDiagram")
		])
	}
	
	def getCanvasTransform() {
		canvasTransform
	}
	
	override activate() {
		if(!isActive) {
			isActiveProperty.set(true);
			doActivate
		}
	}
	
	def void doActivate() {
		nodes.addInitializingListener(new XDiagramChildrenListener<XNode>(this, nodeLayer))
		connections.addInitializingListener(new XDiagramChildrenListener<XConnection>(this, connectionLayer))
		buttons.addInitializingListener(new XDiagramChildrenListener<XRapidButton>(this, buttonLayer))
		val arrowHeadListener = new InitializingListener<ArrowHead> => [
			set = [ 
				if(!connectionLayer.children.contains(it)) 
					connectionLayer.children += it
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
		connectionLayer.children.addInitializingListener(new InitializingListListener  => [
			add = [ 
				if (it instanceof XConnection) {
					labelsProperty.addInitializingListener(labelListener)
					sourceArrowHeadProperty.addInitializingListener(arrowHeadListener)
					targetArrowHeadProperty.addInitializingListener(arrowHeadListener)
				} 
			]
			remove = [ 
				if(it instanceof XConnection) {
					labelsProperty.removeInitializingListener(labelListener)
					sourceArrowHeadProperty.removeInitializingListener(arrowHeadListener)
					targetArrowHeadProperty.removeInitializingListener(arrowHeadListener)
				} 
			]
		])
		auxiliaryLinesSupport = new AuxiliaryLinesSupport(this)
		contentsInitializer?.apply(this)
		if(getBehavior(NavigationBehavior) == null) 
			addBehavior(new DiagramNavigationBehavior(this))
		behaviors.addInitializingListener(new InitializingMapListener => [
			put = [ key, Behavior value | value.activate() ]
			remove = [ key, value | value.activate() ]
		])
	}
	
	def void centerDiagram(boolean useForce) {
		if(needsCentering|| useForce) {
			layout
			if(layoutBounds.width * layoutBounds.height > 1) {
				val scale = min(1, min(scene.width / layoutBounds.width, scene.height / layoutBounds.height))
				canvasTransform.scale = scale
				val centerInScene = localToScene(boundsInLocal).center
				canvasTransform.setTranslate(0.5 * scene.width - centerInScene.x, 0.5 * scene.height - centerInScene.y)
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

class XDiagramChildrenListener<T extends Node & XActivatable> extends InitializingListListener<T> {
	
	Group layer
	XDiagram diagram
	
	new(XDiagram diagram, Group layer) {
		this.layer = layer
		this.diagram = diagram
		add = [
			if (it instanceof XShape)
				it.activatePreview
			layer.children += it
			if(diagram.isActive)
				it.activate
		]
		remove = [
			layer.children -= it
		]
	}
}
	
