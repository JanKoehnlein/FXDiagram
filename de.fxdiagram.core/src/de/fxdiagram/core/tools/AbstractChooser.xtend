package de.fxdiagram.core.tools

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XNode
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.geometry.Bounds
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.SwipeEvent

import static de.fxdiagram.core.extensions.ButtonExtensions.*
import static java.lang.Math.*
import static javafx.geometry.Side.*

import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.StringExpressionExtensions.*
import static extension javafx.util.Duration.*
import de.fxdiagram.core.model.DomainObjectHandle

abstract class AbstractChooser implements XDiagramTool {

	@FxProperty @ReadOnly boolean isActive = false

	@FxProperty Label filterLabel

	@FxProperty String filterString = ''

	@FxProperty double layoutDistance = 60

	DoubleProperty currentPositionProperty = new SimpleDoubleProperty(0.0)

	val visibleNodes = <XNode>newArrayList

	XNode host

	Group group = new Group

	val nodeMap = <String, XNode>newLinkedHashMap
	
	val node2choiceInfo = <XNode, DomainObjectHandle>newHashMap 
	
	var ChooserConnectionProvider connectionProvider = [
		host, choice, choiceInfo | new XConnection(host, choice) 
	]
	
	XNode currentChoice 
	
	XConnection currentConnection
	
	ChangeListener<Number> positionListener

	protected ChooserTransition spinToPosition

	EventHandler<SwipeEvent> swipeHandler 

	EventHandler<ScrollEvent> scrollHandler

	EventHandler<KeyEvent> keyHandler

	ChangeListener<String> filterChangeListener

	Pos layoutPosition

	Node plusButton

	Node minusButton

	new(XNode host, Pos layoutPosition, boolean hasButtons) {
		this.host = host
		this.layoutPosition = layoutPosition
		positionListener = [ element, oldValue, newValue |
			val newVal = newValue.doubleValue
			interpolatedPosition = newVal % getNodes.size
		]
		spinToPosition = new ChooserTransition(this)
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
			if(!it.shortcutDown) {
				if (eventType == ScrollEvent.SCROLL_FINISHED)
					spinToPosition.targetPosition = (getCurrentPosition + 0.5) as int
				else
					currentPosition = getCurrentPosition - (deltaX + deltaY) / 100
			}
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
					nodeChosen(getCurrentNode)
					host.root.restoreDefaultTool
				}
				case KeyCode.BACK_SPACE: {
					val oldFilter = getFilterString
					if (!oldFilter.empty)
						filterString = oldFilter.substring(0, oldFilter.length - 1)
				}
				default: {
					filterString = getFilterString + text
				}
			}
		]
		filterChangeListener = [ property, oldValue, newValue |
			calculateVisibleNodes
		]
		if (hasButtons) {
			val isVertical = layoutPosition.hpos != HPos.CENTER && layoutPosition.hpos != null
			minusButton = (if (isVertical) 
					getArrowButton(BOTTOM, 'previous')
				else 
					getArrowButton(RIGHT, "previous")
				) => [
					onMouseClicked = [
						spinToPosition.targetPositionDelta = -1
					]
				]
			plusButton = (if(isVertical) 
					getArrowButton(TOP, 'next')
				else 
					getArrowButton(LEFT, 'next')
				) => [
					onMouseClicked = [
						spinToPosition.targetPositionDelta = 1
					]
				]
		}
		filterLabel = new Label => [
			textProperty.bind("Filter: " + filterStringProperty + "")
		]
	}

	def addChoice(XNode node) {
		addChoice(node, node.domainObject)
	}
	
	def addChoice(XNode node, DomainObjectHandle choiceInfo) {
		if (!nodeMap.containsKey(node.key)) {
			nodeMap.put(node.key, node)
			node.activatePreview
			node.layout
			calculateVisibleNodes
			group.children += node
			if(choiceInfo != null)
				node2choiceInfo.put(node, choiceInfo)
			true
		} else {
			false
		}
	}

	def void setConnectionProvider(ChooserConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider
	}

	override activate() {
		if (getIsActive || getNodes.empty)
			return false
		isActiveProperty.set(true)
		diagram.buttonLayer.children += group
		currentPosition = 0
		if (getNodes.size == 1) {
			nodeChosen(nodes.head)
			return false
		}
		blurDiagram = true

		if (getNodes.size != 0) 
			interpolatedPosition = 0
		getNodes.forEach [ node |
			node.onMouseClicked = [
				switch (clickCount) {
					case 1:
						spinToPosition.targetPosition = nodes.toList.indexOf(node)
					case 2: {
						nodeChosen(getCurrentNode)
						host.root.restoreDefaultTool
					}
				}
			]
		]
		diagram.scene.addEventHandler(SwipeEvent.ANY, swipeHandler)
		diagram.scene.addEventHandler(ScrollEvent.ANY, scrollHandler)
		diagram.scene.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		currentPositionProperty.addListener(positionListener)
		filterStringProperty.addListener(filterChangeListener)
		host.root.headsUpDisplay.add(filterLabel, Pos.BOTTOM_LEFT)
		filterLabel => [
			textFill = diagram.foregroundPaint
			toFront
		]
		if (minusButton != null) {
			diagram.buttonLayer.children += plusButton
			diagram.buttonLayer.children += minusButton
			val ChangeListener<Bounds> relocateButtons_0 = [ prop, oldVal, newVal |
				relocateButtons(minusButton, plusButton)
			]
			val ChangeListener<Number> relocateButtons_1 = [ prop, oldVal, newVal |
				relocateButtons(minusButton, plusButton)
			]
			minusButton.layoutBoundsProperty.addListener(relocateButtons_0)
			plusButton.layoutBoundsProperty.addListener(relocateButtons_0)
			group.layoutBoundsProperty.addListener(relocateButtons_0)
			group.layoutXProperty.addListener(relocateButtons_1)
			group.layoutYProperty.addListener(relocateButtons_1)
			relocateButtons(minusButton, plusButton)
		}
		true
	}

	override deactivate() {
		if (!getIsActive)
			return false
		removeConnection(currentConnection)
		host.root.headsUpDisplay.children -= filterLabel
		isActiveProperty.set(false)
		diagram.scene.removeEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		diagram.scene.removeEventHandler(ScrollEvent.ANY, scrollHandler)
		diagram.scene.removeEventHandler(SwipeEvent.ANY, swipeHandler)
		spinToPosition.stop
		blurDiagram = false
		if (minusButton != null) {
			diagram.buttonLayer.children -= minusButton
			diagram.buttonLayer.children -= plusButton
		}
		diagram.buttonLayer.children -= group
		true
	}

	protected def nodeChosen(XNode choice) {
		if (choice != null) {
			getNodes.forEach[onMouseClicked = null]
			group.children.remove(choice)
			var existingChoice = diagram.nodes.findFirst[key == choice.key]
			if(existingChoice == null) {
				existingChoice = choice
				val unlayoutedBounds = choice.layoutBounds
				choice.effect = null
				var center = group.localToDiagram(0, 0)
				choice.transforms.clear
				diagram.nodes += choice
				choice.layout
				val bounds = choice.layoutBounds
				choice.layoutX = center.x - 0.5 * bounds.width 
				choice.layoutY = center.y - 0.5 * bounds.height
				switch layoutPosition.hpos {
					case HPos.LEFT:
						choice.layoutX = choice.layoutX - 0.5 * (bounds.width - unlayoutedBounds.width)
					case HPos.RIGHT:
						choice.layoutX = choice.layoutX + 0.5 * (bounds.width - unlayoutedBounds.width)
				}
				switch layoutPosition.vpos {
					case VPos.TOP:
						choice.layoutY = choice.layoutY - 0.5 * (bounds.height - unlayoutedBounds.height)
					case VPos.BOTTOM:
						choice.layoutY = choice.layoutY + 0.5 * (bounds.height - unlayoutedBounds.height)
				}
			}
			connectChoice(existingChoice, node2choiceInfo.get(choice))
			currentConnection = null
		}
	}
	
	protected def connectChoice(XNode choice, DomainObjectHandle choiceInfo) {
		if(isActive && choice !== currentChoice) {
			currentChoice = choice
			val newConnection = connectionProvider.getConnection(host, choice, choiceInfo)
			if(newConnection != currentConnection) {
				removeConnection(currentConnection)
				currentConnection = newConnection
				if(newConnection != null && !diagram.connections.contains(newConnection)) 
					diagram.connections += newConnection
			}
			choice.toFront
			currentConnection.toFront
		}
		currentConnection
	}
	
	protected def removeConnection(XConnection connection) {
		if(connection != null) {
			diagram.connections -= connection
			connection.source.outgoingConnections.remove(connection)
			connection.target.incomingConnections.remove(connection)
		}
	}

	protected def setBlurDiagram(boolean isBlur) {
		new ParallelTransition => [
			for (layer : #[host.getRootDiagram.nodeLayer, host.getRootDiagram.connectionLayer])
				children += new FadeTransition => [
					node = layer
					toValue = if(isBlur) 0.3 else 1
					duration = 300.millis
					play
				]
		]
	}

	protected def cancel() {
		host.root.restoreDefaultTool
	}

	protected def void setInterpolatedPosition(double interpolatedPosition) {
		doSetInterpolatedPosition(interpolatedPosition)
		if(!nodes.empty) {
			val choice = nodes.get(((getCurrentPosition + 0.5) as int) % nodes.size)			
			connectChoice(choice, node2choiceInfo.get(choice))
		}
	}

	protected def void doSetInterpolatedPosition(double interpolatedPosition)

	def getCurrentPosition() {
		var result = currentPositionProperty.get % getNodes.size
		if (result < 0)
			result = result + ((result / getNodes.size) as int + 1) * getNodes.size
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
		getNodes.get(currentPosition)
	}

	def diagram() {
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
			if (entry.value.matchesFilter) {
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
			case HPos.LEFT: host.layoutX - getLayoutDistance - 0.5 * maxWidth
			case HPos.RIGHT: host.layoutX + host.layoutBounds.width + getLayoutDistance + 0.5 * maxWidth
			default: host.layoutX + 0.5 * host.layoutBounds.width
		}
		group.layoutY = switch layoutPosition.vpos {
			case VPos.TOP: host.layoutY - getLayoutDistance - 0.5 * maxHeight
			case VPos.BOTTOM: host.layoutY + host.layoutBounds.height + getLayoutDistance + 0.5 * maxHeight
			default: host.layoutY + 0.5 * host.layoutBounds.height
		}
		interpolatedPosition = getCurrentPosition
		spinToPosition.resetTargetPosition
	}

	def protected matchesFilter(XNode node) {
		if (filterString.toLowerCase == filterString)
			node.key.toLowerCase.contains(filterString)
		else
			node.key.contains(filterString)
	}

	def void relocateButtons(Node minusButton, Node plusButton) {
	}
}
