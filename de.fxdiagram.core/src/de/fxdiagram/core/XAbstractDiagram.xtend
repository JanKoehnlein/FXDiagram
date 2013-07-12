package de.fxdiagram.core

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import java.util.List
import javafx.scene.Group
import javafx.scene.Parent

/**
 * A diagram is a group, as such not resizable from the outside. 
 */
abstract class XAbstractDiagram extends Parent implements XActivatable { 

	// TODO: convert to properties
	List<XNode> nodes = newArrayList
	List<XConnection> connections = newArrayList
	List<XRapidButton> buttons = newArrayList

	@FxProperty @ReadOnly boolean isActive

	def Group getNodeLayer()

	def Group getConnectionLayer()
	
	def Group getButtonLayer()

	override activate() {
		if (!isActive)
			doActivate
		isActiveProperty.set(true)
	}

	def void doActivate() {
		isActiveProperty.set(true)
		(nodes + connections + buttons).forEach[activate]
	}

	def addNode(XNode node) {
		nodeLayer.children += node
		internalAddNode(node)
		if (isActive)
			node.activate
	}

	def internalAddNode(XNode node) {
		nodes += node
	}

	def addConnection(XConnection connection) {
		connectionLayer.children += connection
		if (connection.label != null)
			connectionLayer.children += connection.label
		internalAddConnection(connection)
		if (isActive)
			connection.activate
	}

	def internalAddConnection(XConnection connection) {
		connections += connection
	}

	def addButton(XRapidButton button) {
		buttonLayer.children += button
		internalAddButton(button)
		if (isActive)
			button.activate
	}

	def internalAddButton(XRapidButton button) {
		buttons += button
	}

	def removeNode(XNode node) {
		nodeLayer.children -= node
		internalRemoveNode(node)
	}

	def internalRemoveNode(XNode node) {
		nodes -= node
	}

	def removeConnection(XConnection connection) {
		connectionLayer.children -= connection
		if (connection.label != null)
			connectionLayer.children -= connection.label
		internalRemoveConnection(connection)
	}

	def internalRemoveConnection(XConnection connection) {
		connections -= connection
	}

	def removeButton(XRapidButton button) {
		buttonLayer.children -= button
		internalRemoveButton(button)
	}

	def internalRemoveButton(XRapidButton button) {
		buttons -= button
	}

	def getNodes() {
		nodes
	}

	def getButtons() {
		buttons
	}

	def getConnections() {
		connections
	}
}
