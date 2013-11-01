package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.auxlines.AuxiliaryLinesSupport
import de.fxdiagram.core.behavior.Behavior
import de.fxdiagram.core.behavior.DiagramNavigationBehavior
import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener
import javafx.collections.ListChangeListener.Change
import javafx.collections.MapChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

import static javafx.collections.FXCollections.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import de.fxdiagram.core.behavior.NavigationBehavior

class XDiagram extends Group implements XActivatable {
	
	@FxProperty ObservableList<XNode> nodes = observableArrayList
	@FxProperty ObservableList<XConnection> connections = observableArrayList
	@FxProperty ObservableList<XRapidButton> buttons = observableArrayList

	@FxProperty ObservableMap<Node, Pos> fixedButtons = observableMap(newHashMap)

	@FxProperty @ReadOnly boolean isActive
	@FxProperty @ReadOnly boolean isRootDiagram
	@FxProperty Paint backgroundPaint = Color.WHITE
	@FxProperty Paint foregroundPaint = Color.BLACK
	@FxProperty Paint connectionPaint = Color.gray(0.2)
	
	Group nodeLayer = new Group
	Group buttonLayer = new Group
	
	XDiagram parentDiagram

	(XDiagram)=>void contentsInitializer

	AuxiliaryLinesSupport auxiliaryLinesSupport
	
	ObservableMap<Class<? extends Behavior>, Behavior> behaviors = observableHashMap
	
	new() {
		children += nodeLayer
		children += buttonLayer
		parentProperty.addListener [
			property, oldValue, newValue |
			parentDiagram = newValue.diagram
			isRootDiagramProperty.set(parentDiagram == null)
		]
		isRootDiagramProperty.addListener [
			property, oldValue, newValue |
			if(newValue) 
				nodeLayer.children += connections
			else 
				nodeLayer.children -= connections
		]
	}
	
	override activate() {
		if (!isActive) {
			isActiveProperty.set(true)
			doActivate
		}
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

	def void doActivate() {
		val ChangeListener<Node> arrowHeadListener = [
			prop, oldVal, newVal |
			if(oldVal != null)
				connectionLayer.children -= oldVal
			if(newVal != null)
				connectionLayer.children += newVal
		]
		val ListChangeListener<? super XConnectionLabel> labelListener = [
			change |
			while(change.next) {
				if(change.wasAdded)
					connectionLayer.children += change.addedSubList
				if(change.wasRemoved)
					connectionLayer.children -= change.removed
			}
		]
		nodes.addListener(new XDiagramChildrenListener<XNode>(this, nodeLayer))
		connections.addListener(new XDiagramChildrenListener<XConnection>(this, connectionLayer))
		buttons.addListener(new XDiagramChildrenListener<XRapidButton>(this, buttonLayer))
		connectionLayer.children.addListener [
			Change<? extends Node> change | 
			while(change.next) {
				if(change.wasAdded) {
					val addedConnections = change.addedSubList.filter(XConnection)
					addedConnections.forEach [
						labels.forEach[
							if(!connectionLayer.children.contains(it))
								connectionLayer.children.add(it)
						]
						labelsProperty.addListener(labelListener)
						addArrowHead(targetArrowHeadProperty, arrowHeadListener) 
						addArrowHead(sourceArrowHeadProperty, arrowHeadListener) 
					]
				}
				if(change.wasRemoved) {
					val removedConnections = change.removed.filter(XConnection)
					removedConnections.forEach [
						connectionLayer.children.removeAll(labels)
						labelsProperty.removeListener(labelListener)
						removeArrowHead(targetArrowHeadProperty, arrowHeadListener) 
						removeArrowHead(sourceArrowHeadProperty, arrowHeadListener) 
					]
				}
			}
		]
		(nodes + connections + buttons).forEach[activate]
		auxiliaryLinesSupport = new AuxiliaryLinesSupport(this)
		contentsInitializer?.apply(this)
		if(getBehavior(NavigationBehavior) == null) 
			addBehavior(new DiagramNavigationBehavior(this))
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
	
	protected def removeArrowHead(Property<? extends Node> property, 
		ChangeListener<? super Node> listener) {
		if(property.value != null)
			connectionLayer.children -= property.value
		property.removeListener(listener)
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
	
	def getParentDiagram() {
		parentDiagram
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

class XDiagramChildrenListener<T extends Node & XActivatable> implements ListChangeListener<T> {
	
	Group layer
	XDiagram diagram
	
	new(XDiagram diagram, Group layer) {
		this.layer = layer
		this.diagram = diagram
	}
	
	override onChanged(ListChangeListener.Change<? extends T> change) {
		while(change.next) {
			if(change.wasAdded)
				change.addedSubList.forEach [
					layer.children += it
					if(diagram.isActive)
						// Xtend bug https://bugs.eclipse.org/bugs/show_bug.cgi?id=413708
						(it as XActivatable).activate
				]
			if(change.wasRemoved) 
				change.removed.forEach [
					layer.children -= it
				]
		}
	}
}
	