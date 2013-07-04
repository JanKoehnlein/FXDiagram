package de.fxdiagram.core.tools.chooser

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import de.fxdiagram.core.tools.XDiagramTool
import javafx.animation.FadeTransition
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.SwipeEvent

import static java.lang.Math.*
import javafx.geometry.HPos
import javafx.geometry.VPos

import static extension de.fxdiagram.core.Extensions.*
import static extension de.fxdiagram.core.binding.StringExpressionExtensions.*
import static extension javafx.util.Duration.*
import javafx.geometry.Bounds

abstract class AbstractXNodeChooser implements XDiagramTool {

	@FxProperty @ReadOnly boolean isActive = false

	@FxProperty String filterString = ''

	@FxProperty double layoutDistance = 40

	DoubleProperty currentPositionProperty = new SimpleDoubleProperty(0.0)

	val visibleNodes = <XNode>newArrayList

	XNode host

	Group group = new Group

	val nodeMap = <String, XNode>newLinkedHashMap

	ChangeListener<Number> positionListener

	protected XNodeChooserTransition spinToPosition

	EventHandler<SwipeEvent> swipeHandler

	EventHandler<ScrollEvent> scrollHandler

	EventHandler<KeyEvent> keyHandler

	ChangeListener<String> filterChangeListener

	Label filterLabel

	Pos layoutPosition

	Button plusButton

	Button minusButton

	new(XNode host, Pos layoutPosition, boolean hasButtons) {
		this.host = host
		this.layoutPosition = layoutPosition
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
					spinToPosition.targetPositionDelta = 1
				case KeyCode.LEFT:
					spinToPosition.targetPositionDelta = -1
				case KeyCode.DOWN:
					spinToPosition.targetPositionDelta = -1
				case KeyCode.RIGHT:
					spinToPosition.targetPositionDelta = 1
				case KeyCode.ENTER: {
					nodeChosen(currentNode)
					host.rootDiagram.restoreDefaultTool
				}
				case KeyCode.BACK_SPACE: {
					val oldFilter = filterString
					if (!oldFilter.empty)
						filterString = oldFilter.substring(0, oldFilter.length - 1)
				}
				default: {
					filterString = filterString + text
				}
			}
		]
		filterChangeListener = [ property, oldValue, newValue |
			calculateVisibleNodes
		]
		if(hasButtons) {
			val isVertical = layoutPosition.hpos != HPos.CENTER && layoutPosition.hpos != null 
			minusButton = new Button => [
				id = if(isVertical) 'button-down' else 'button-right'
				text = id
				onAction = [
					spinToPosition.targetPositionDelta = -1
				]
				focusTraversable = false
			]
			plusButton = new Button => [
				id = if(isVertical) 'button-up' else 'button-left'
				text = id
				onAction = [
					spinToPosition.targetPositionDelta = 1
				]
				focusTraversable = false
			]
		}
	}

	def operator_add(XNode node) {
		if (!nodeMap.containsKey(node.key)) {
			nodeMap.put(node.key, node)
			node.layout
			calculateVisibleNodes
			group.children += node
			true
		} else {
			false
		}
	}

	def operator_add(Iterable<? extends XNode> nodes) {
		nodes.map[this += it].reduce[a, b|a || b]
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
		nodes.map[this -= it].reduce[a, b|a || b]
	}

	override activate() {
		if (isActive || nodes.empty)
			return false
		isActiveProperty.set(true)
		diagram.buttonLayer.children += group
		new FadeTransition => [
			node = diagram.nodeLayer
			toValue = 0.3
			duration = 300.millis
			play
		]
		
		if(minusButton != null) {
			diagram.buttonLayer.children += plusButton
			diagram.buttonLayer.children += minusButton
			val ChangeListener<Bounds> relocateButtons_0 = [
				prop, oldVal, newVal | relocateButtons(minusButton, plusButton)
			]  
			val ChangeListener<Number> relocateButtons_1 = [
				prop, oldVal, newVal | relocateButtons(minusButton, plusButton) 
			]  
			minusButton.layoutBoundsProperty.addListener(relocateButtons_0)
			plusButton.layoutBoundsProperty.addListener(relocateButtons_0)
			group.layoutBoundsProperty.addListener(relocateButtons_0)
			group.layoutXProperty.addListener(relocateButtons_1)
			group.layoutYProperty.addListener(relocateButtons_1)
		}
		currentPosition = 0
		if (nodes.size == 1) {
			nodeChosen(nodes.head)
			return false
		}
		if (nodes.size != 0) {
			interpolatedPosition = 0
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
		currentPositionProperty.addListener(positionListener)
		filterStringProperty.addListener(filterChangeListener)
		filterLabel = new Label => [
			textProperty.bind("Filter: " + filterStringProperty + "")
		]
		host.rootDiagram.buttonLayer.children += filterLabel
		true
	}

	override deactivate() {
		if (!isActive)
			return false
		host.rootDiagram.buttonLayer.children -= filterLabel
		isActiveProperty.set(false)
		diagram.scene.removeEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		diagram.scene.removeEventHandler(ScrollEvent.ANY, scrollHandler)
		diagram.scene.removeEventHandler(SwipeEvent.ANY, swipeHandler)
		spinToPosition.stop
		new FadeTransition => [
			node = diagram.nodeLayer
			toValue = 1
			duration = 300.millis
			play
		]
		if(minusButton != null) {
			diagram.buttonLayer.children -= minusButton
			diagram.buttonLayer.children -= plusButton
		}
		diagram.buttonLayer.children -= group		
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

	protected def void setInterpolatedPosition(double interpolatedPosition)

	def getCurrentPosition() {
		var result = currentPositionProperty.get % nodes.size
		if (result < 0)
			result = result + ((result / nodes.size) as int + 1) * nodes.size
		result
	}

	def setCurrentPosition(double currentPosition) {
		currentPositionProperty.set(currentPosition)
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
	
	protected def getGroup() {
		group
	}

	protected def calculateVisibleNodes() {
		var currentVisibleIndex = 0
		var currentVisibleNode = visibleNodes.head
		var mapIndex = 0
		var maxWidth = 0.0
		var maxHeight = 0.0
		for (entry : nodeMap.entrySet) {
			if (entry.key.contains(filterString)) {
				if (currentVisibleNode != entry.value)
					visibleNodes.add(currentVisibleIndex, entry.value)
				val layoutBounds = entry.value.layoutBounds
				maxWidth = max(maxWidth, layoutBounds.width)
				maxHeight = max(maxHeight, layoutBounds.height)
				currentVisibleIndex = currentVisibleIndex + 1
				currentVisibleNode = if(currentVisibleIndex < visibleNodes.size) visibleNodes.get(currentVisibleIndex) else null
			} else {
				if (currentVisibleNode == entry.value) {
					visibleNodes.remove(currentVisibleIndex)
					currentVisibleNode.visible = false
					currentVisibleNode = if (currentVisibleIndex < visibleNodes.size)
						visibleNodes.get(currentVisibleIndex)
					else
						null
				}
			}
			mapIndex = mapIndex + 1
		}
		group.layoutX = switch layoutPosition.hpos {
			case HPos.LEFT: host.layoutX - layoutDistance - 0.5 * maxWidth
			case HPos.RIGHT: host.layoutX + host.layoutBounds.width + layoutDistance + 0.5 * maxWidth
			default: host.layoutX + 0.5 * host.layoutBounds.width
		}
		group.layoutY = switch layoutPosition.vpos {
			case VPos.TOP: host.layoutY - layoutDistance - 0.5 * maxHeight
			case VPos.BOTTOM: host.layoutY + host.layoutBounds.height + layoutDistance + 0.5 * maxHeight
			default: host.layoutY + 0.5 * host.layoutBounds.height
		}
		interpolatedPosition = currentPosition
		spinToPosition.resetTargetPosition
	}

	def void relocateButtons(Button minusButton, Button plusButton) {
	}
}
