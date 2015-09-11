package de.fxdiagram.lib.chooser

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import de.fxdiagram.core.command.AddRemoveCommand
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.tools.XDiagramTool
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.geometry.Bounds
import javafx.geometry.Point2D
import javafx.geometry.Pos
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
import de.fxdiagram.core.tools.actions.RevealAction

/**
 * Interactive {@link XDiagramTool} to add new nodes and edges the diagram in a user
 * friendly way.
 * 
 * A {@link ChoiceGraphics} describes the visual effect and behavior to show and select
 * the candidates.
 * 
 * Currently there are two concrete subclasses: {@link NodeChooser} to add unrelated
 * nodes, e.g. to initially populate the diagram, and {@link ConnectedNodeChooser} to 
 * add nodes connected to a given host node, e.g. for exploration diagrams. To use them
 * you usually
 * <ol>
 * <li>Create an instance with the appropriate {@link ChoiceGraphics},</li>
 * <li>Add choices using {@link #addChoice},</li>
 * <li>Make it the current tool by calling {@link XRoot#setCurrentTool}.</li>
 * </ol>
 */
abstract class AbstractBaseChooser implements XDiagramTool {

	@FxProperty(readOnly=true) boolean isActive = false

	@FxProperty Label filterLabel

	@FxProperty String filterString = ''

	DoubleProperty currentPositionProperty = new SimpleDoubleProperty(0.0)

	val visibleNodes = <XNode>newArrayList

	Group group = new Group

	val nodeMap = <String, XNode>newLinkedHashMap
	
	val node2choiceInfo = <XNode, DomainObjectDescriptor>newHashMap 
	
	ChangeListener<Number> positionListener

	protected ChooserTransition spinToPosition

	EventHandler<SwipeEvent> swipeHandler 

	EventHandler<ScrollEvent> scrollHandler

	EventHandler<KeyEvent> keyHandler
	EventHandler<KeyEvent> keyTypedHandler

	ChangeListener<String> filterChangeListener

	val ChoiceGraphics graphics

	Node plusButton

	Node minusButton

	new(ChoiceGraphics graphics, boolean isVertical) {
		this.graphics = graphics
		graphics.chooser = this
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
					root.restoreDefaultTool
				}
				case KeyCode.BACK_SPACE: {
					val oldFilter = getFilterString
					if (!oldFilter.empty)
						filterString = oldFilter.substring(0, oldFilter.length - 1)
				}
				default: {}
			}
		]
		keyTypedHandler = [
			filterString = getFilterString + new String(character.toCharArray.filter[it > 31])
		]
		filterChangeListener = [ property, oldValue, newValue |
			calculateVisibleNodes
		]
		if (graphics.hasButtons) {
			minusButton = (if(isVertical) 
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

	abstract def XRoot getRoot()
	
	abstract def XDiagram getDiagram()
	
	abstract def Point2D getPosition()

	def addChoice(XNode node) {
		addChoice(node, node.domainObjectDescriptor)
	}
	
	def addChoice(XNode node, DomainObjectDescriptor choiceInfo) {
		if (!nodeMap.containsKey(node.name)) {
			nodeMap.put(node.name, node)
			node.initializeGraphics
			node.autosize
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
	
	protected def getChoiceInfo(XNode choice) {
		node2choiceInfo.get(choice)
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
						root.restoreDefaultTool
						consume
					}
				}
			]
		]
		diagram.scene.addEventHandler(SwipeEvent.ANY, swipeHandler)
		diagram.scene.addEventHandler(ScrollEvent.ANY, scrollHandler)
		diagram.scene.addEventHandler(KeyEvent.KEY_PRESSED, keyHandler)
		diagram.scene.addEventHandler(KeyEvent.KEY_TYPED, keyTypedHandler)
		currentPositionProperty.addListener(positionListener)
		filterStringProperty.addListener(filterChangeListener)
		root.headsUpDisplay.add(filterLabel, Pos.BOTTOM_LEFT)
		filterLabel => [
			textFill = diagram.foregroundPaint
			toFront
		]
		if (minusButton != null) {
			diagram.buttonLayer.children += plusButton
			diagram.buttonLayer.children += minusButton
			val ChangeListener<Bounds> relocateButtons_0 = [ prop, oldVal, newVal |
				graphics.relocateButtons(minusButton, plusButton)
			]
			val ChangeListener<Number> relocateButtons_1 = [ prop, oldVal, newVal |
				graphics.relocateButtons(minusButton, plusButton)
			]
			minusButton.layoutBoundsProperty.addListener(relocateButtons_0)
			plusButton.layoutBoundsProperty.addListener(relocateButtons_0)
			group.layoutBoundsProperty.addListener(relocateButtons_0)
			group.layoutXProperty.addListener(relocateButtons_1)
			group.layoutYProperty.addListener(relocateButtons_1)
			graphics.relocateButtons(minusButton, plusButton)
		}
		new RevealAction(getNodes).perform(root)
		true
	}

	override deactivate() {
		if (!getIsActive)
			return false
		root.headsUpDisplay.children -= filterLabel
		isActiveProperty.set(false)
		diagram.scene.removeEventHandler(KeyEvent.KEY_TYPED, keyTypedHandler)
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
			val shapesToAdd = <XShape>newArrayList
			var existingChoice = diagram.nodes.findFirst[domainObjectDescriptor == choice.domainObjectDescriptor]
			if(existingChoice == null) {
				existingChoice = choice
				val unlayoutedBounds = choice.layoutBounds
				choice.effect = null
				var center = group.localToDiagram(0, 0)
				choice.transforms.clear
				choice.autosize
				choice.layout
				val bounds = choice.layoutBounds
				choice.layoutX = center.x - 0.5 * bounds.width 
				choice.layoutY = center.y - 0.5 * bounds.height
				graphics.nodeChosen(choice)
				adjustNewNode(choice, 
					bounds.width - unlayoutedBounds.width,
					bounds.height - unlayoutedBounds.height)
				shapesToAdd += choice
			}
			shapesToAdd += getAdditionalShapesToAdd(existingChoice, node2choiceInfo.get(choice))
			root.commandStack.execute(AddRemoveCommand.newAddCommand(diagram, shapesToAdd))
		}
	}
	
	protected def adjustNewNode(XNode choice, double widthDelta, double heightDelta) {
		choice.layoutX = choice.layoutX - 0.5 * widthDelta
		choice.layoutY = choice.layoutY - 0.5 * heightDelta
	}
	
	protected def Iterable<? extends XShape> getAdditionalShapesToAdd(XNode choice, DomainObjectDescriptor choiceInfo) {
		#[]
	}
	
	protected def setBlurDiagram(boolean isBlur) {
		new ParallelTransition => [
			for (layer : #[diagram.nodeLayer, diagram.connectionLayer])
				children += new FadeTransition => [
					node = layer
					toValue = if(isBlur) 0.3 else 1
					duration = 300.millis
					play
				]
		]
	}

	protected def cancel() {
		root.restoreDefaultTool
	}

	protected def void setInterpolatedPosition(double interpolatedPosition) {
		graphics.interpolatedPosition = interpolatedPosition
	}

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
		alignGroup(group, maxWidth, maxHeight)
		interpolatedPosition = getCurrentPosition
		spinToPosition.resetTargetPosition
	}

	protected def alignGroup(Group node, double maxWidth, double maxHeight) {
		group.layoutX = position.x 
		group.layoutY = position.y 
	}

	def protected matchesFilter(XNode node) {
		if (filterString.toLowerCase == filterString)
			node.name.toLowerCase.contains(filterString)
		else
			node.name.contains(filterString)
	}

}