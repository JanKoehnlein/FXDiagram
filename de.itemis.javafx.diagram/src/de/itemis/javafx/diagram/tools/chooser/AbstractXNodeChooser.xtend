package de.itemis.javafx.diagram.tools.chooser

import de.itemis.javafx.diagram.XConnection
import de.itemis.javafx.diagram.XNode
import de.itemis.javafx.diagram.tools.XDiagramTool
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.SwipeEvent

import static extension de.itemis.javafx.diagram.Extensions.*
import static extension de.itemis.javafx.diagram.binding.StringExpressionExtensions.*

abstract class AbstractXNodeChooser implements XDiagramTool {

	XNode host

	Group group = new Group

	val nodeMap = <String, XNode> newLinkedHashMap
	
	boolean isActive = false

	DoubleProperty _currentPosition = new SimpleDoubleProperty

	ChangeListener<Number> positionListener

	protected XNodeChooserTransition spinToPosition

	EventHandler<SwipeEvent> swipeHandler

	EventHandler<ScrollEvent> scrollHandler

	EventHandler<KeyEvent> keyHandler
	
	ChangeListener<String> filterChangeListener
	
	val visibleNodes = <XNode> newArrayList

	StringProperty _filterString = new SimpleStringProperty('')
	
	Label filterLabel

	new(XNode host, Point2D center) {
		this.host = host
		group.layoutX = center.x
		group.layoutY = center.y
		positionListener = [ element, oldValue, newValue |
			val newVal = newValue.doubleValue
			setInterpolatedPosition(newVal % nodes.size)
		]
		spinToPosition = new XNodeChooserTransition(this)
		swipeHandler = [
			val direction = switch eventType {
				case SwipeEvent.SWIPE_DOWN:
					-1
				case SwipeEvent.SWIPE_RIGHT:
					-1
				default:
					1
			}
			spinToPosition.targetPositionDelta = direction * 10
		]
		scrollHandler = [
			if (eventType == ScrollEvent.SCROLL_FINISHED)
				spinToPosition.targetPosition = (currentPosition + 0.5) as int
			else
				currentPosition = currentPosition - (deltaX + deltaY) / 100
		]
		keyHandler = [
			switch code {
				case KeyCode.CANCEL:
					cancel
				case KeyCode.ESCAPE:
					cancel
				case KeyCode.UP:
					spinToPosition.targetPositionDelta = -1
				case KeyCode.LEFT:
					spinToPosition.targetPositionDelta = -1
				case KeyCode.DOWN:
					spinToPosition.targetPositionDelta = 1
				case KeyCode.RIGHT:
					spinToPosition.targetPositionDelta = 1
				case KeyCode.ENTER: {
					nodeChosen(currentNode)
					host.rootDiagram.restoreDefaultTool
				}
				case KeyCode.BACK_SPACE: {
					val oldFilter = _filterString.get
					if(!oldFilter.empty)
						_filterString.set(oldFilter.substring(0, oldFilter.length-1))
					}
				default: {
					_filterString.set(_filterString.get + text)
				}
			}
		]
		filterChangeListener = [
			property, oldValue, newValue | calculateVisibleNodes
		]
	}

	def operator_add(XNode node) {
		if(!nodeMap.containsKey(node.key)) {
			nodeMap.put(node.key, node)
			calculateVisibleNodes
			group.children += node
			true
		} else {
			false
		}
	}

	def operator_add(Iterable<XNode> nodes) {
		nodes.map[this += it].reduce[a, b | a || b]
	}

	def operator_remove(XNode node) {
		if (nodeMap.remove(node.key) != null) {
			group.children += node
			visibleNodes.remove(node)
			calculateVisibleNodes
			true	
		} else {
			false
		} 
	}

	def operator_remove(Iterable<XNode> nodes) {
		nodes.map[this -= it].reduce[a, b | a || b]
	}

	override activate() {
		if (isActive)
			return false
		if (nodes.empty)
			return false
		isActive = true
		diagram.nodeLayer.children += group
		_currentPosition.set(0)
		interpolatedPosition = 0
		if(nodes.size == 1) {
			nodeChosen(nodes.head)
			return false
		}
		nodes.forEach [ node |
			node.onMouseClicked = [
				switch (clickCount) {
					case 1:
						spinToPosition.targetPosition = nodes.toList.indexOf(node)
					case 2: {
						nodeChosen(currentNode)
						host.rootDiagram.restoreDefaultTool
					}
				}
			]
		]
		diagram.scene.addEventHandler(SwipeEvent.ANY, swipeHandler)
		diagram.scene.addEventHandler(ScrollEvent.ANY, scrollHandler)
		diagram.scene.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		_currentPosition.addListener(positionListener)
		_filterString.addListener(filterChangeListener)
		filterLabel = new Label => [
			textProperty.bind("Filter: " +  _filterString + "")
		]
		host.rootDiagram.buttonLayer.children += filterLabel
		true
	}

	override deactivate() {
		if (!isActive)
			return false
		host.rootDiagram.buttonLayer.children -= filterLabel
		isActive = false
		diagram.scene.removeEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		diagram.scene.removeEventHandler(ScrollEvent.ANY, scrollHandler)
		diagram.scene.removeEventHandler(SwipeEvent.ANY, swipeHandler)
		spinToPosition.stop
		diagram.nodeLayer.children -= group
		true
	}

	protected def nodeChosen(XNode choice) {
		if (choice != null) {
			nodes.forEach[onMouseClicked = null]
			choice.effect = null
			var center = group.localToDiagram(0, 0)
			choice.transforms.clear
			group.children.remove(choice)
			diagram.addNode(choice)
			val bounds = choice.layoutBounds
			choice.layoutX = center.x - 0.5 * bounds.width
			choice.layoutY = center.y - 0.5 * bounds.height
			val connection = new XConnection(host, choice)
			diagram.addConnection(connection)
		}
	}
	
	protected def cancel() {
		host.rootDiagram.restoreDefaultTool		
	}

	protected abstract def void setInterpolatedPosition(double interpolatedPosition)

	def getCurrentPosition() {
		var result = _currentPosition.value % nodes.size
		if (result < 0)
			result = result + ((result / nodes.size) as int + 1) * nodes.size
		result
	}

	def setCurrentPosition(double value) {
		_currentPosition.set(value)
	}

	def getNodes() {
		visibleNodes
	}

	def getCurrentNode() {
		var currentPosition = (getCurrentPosition + 0.5) as int
		nodes.get(currentPosition)
	}

	def getDiagram() {
		host.diagram
	}
	
	def calculateVisibleNodes() {
		var currentVisibleIndex = 0
		var currentVisibleNode = visibleNodes.head
		var mapIndex = 0
		for(entry: nodeMap.entrySet) {
			if(entry.key.contains(_filterString.get)) {
				if(currentVisibleNode != entry.value)  
					visibleNodes.add(currentVisibleIndex, entry.value)
				currentVisibleIndex = currentVisibleIndex + 1
				currentVisibleNode = if (currentVisibleIndex < visibleNodes.size) visibleNodes.get(currentVisibleIndex) else null
			} else {
				if(currentVisibleNode == entry.value) {
					visibleNodes.remove(currentVisibleIndex)
					currentVisibleNode.visible = false
					currentVisibleNode = if (currentVisibleIndex < visibleNodes.size) visibleNodes.get(currentVisibleIndex) else null
				}
			}
			mapIndex = mapIndex + 1
		}
		interpolatedPosition = _currentPosition.get
		spinToPosition.resetTargetPosition
	}
}


