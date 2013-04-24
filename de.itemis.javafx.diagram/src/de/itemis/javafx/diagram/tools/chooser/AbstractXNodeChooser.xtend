package de.itemis.javafx.diagram.tools.chooser

import de.itemis.javafx.diagram.XNode
import de.itemis.javafx.diagram.tools.XDiagramTool
import java.util.List
import javafx.animation.Transition
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.input.ScrollEvent
import javafx.scene.input.SwipeEvent
import javafx.util.Duration

import static java.lang.Math.*

import static extension de.itemis.javafx.diagram.Extensions.*
import de.itemis.javafx.diagram.XConnection
import javafx.scene.input.KeyEvent
import javafx.scene.input.KeyCode

abstract class AbstractXNodeChooser implements XDiagramTool {

	XNode host

	Group group = new Group

	List<XNode> _nodes = newArrayList

	boolean isActive = false

	DoubleProperty _currentPosition = new SimpleDoubleProperty

	ChangeListener<Number> positionListener

	SpinTransition spinToPosition

	EventHandler<SwipeEvent> swipeHandler

	EventHandler<ScrollEvent> scrollHandler

	EventHandler<KeyEvent> keyHandler

	new(XNode host, Point2D position) {
		this.host = host
		group.layoutX = position.x
		group.layoutY = position.y
		positionListener = [ element, oldValue, newValue |
			val newVal = newValue.doubleValue
			setInterpolatedPosition(newVal)
		]
		spinToPosition = new SpinTransition(this)
		swipeHandler = [
			val direction = switch eventType {
				case SwipeEvent::SWIPE_DOWN:
					-1
				case SwipeEvent::SWIPE_RIGHT:
					-1
				default:
					1
			}
			spinToPosition.targetPositionDelta = direction * 10
		]
		scrollHandler = [
			if (eventType == ScrollEvent::SCROLL_FINISHED)
				spinToPosition.targetPosition = (currentPosition + 0.5) as int
			else
				currentPosition = currentPosition - deltaY / 100
		]
		keyHandler = [
			switch code {
				case KeyCode::CANCEL:
					cancel
				case KeyCode::ESCAPE:
					cancel
				case KeyCode::UP:
					spinToPosition.targetPositionDelta = -1
				case KeyCode::DOWN:
					spinToPosition.targetPositionDelta = 1
			}
		]
	}

	def operator_add(XNode node) {
		if (!isActive) {
			nodes += node
			group.children += node
		}
	}

	def operator_add(Iterable<XNode> nodes) {
		nodes.forEach[this += it]
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
						spinToPosition.targetPosition = nodes.indexOf(node)
					case 2: {
						nodeChosen(currentNode)
						diagram.restoreDefaultTool
					}
				}
			]
		]
		diagram.scene.addEventHandler(SwipeEvent::ANY, swipeHandler)
		diagram.scene.addEventHandler(ScrollEvent::ANY, scrollHandler)
		diagram.scene.addEventHandler(KeyEvent::KEY_PRESSED, keyHandler)
		_currentPosition.addListener(positionListener)
		true
	}

	override deactivate() {
		if (!isActive)
			return false
		isActive = false
		diagram.scene.removeEventHandler(KeyEvent::KEY_PRESSED, keyHandler)
		diagram.scene.removeEventHandler(ScrollEvent::ANY, scrollHandler)
		diagram.scene.removeEventHandler(SwipeEvent::ANY, swipeHandler)
		spinToPosition.stop
		_currentPosition.removeListener(positionListener)
		diagram.nodeLayer.children -= group
		true
	}

	protected def nodeChosen(XNode choice) {
		if (choice != null) {
			var bounds = choice.localToRoot(choice.layoutBounds)
			choice.transforms.clear
			group.children.remove(choice)
			diagram.addNode(choice)
			choice.layoutX = bounds.minX
			choice.layoutY = bounds.minY
			val connection = new XConnection(host, choice)
			diagram.addConnection(connection)
		}
	}
	
	protected def cancel() {
		diagram.restoreDefaultTool		
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
		_nodes
	}

	def getCurrentNode() {
		var currentPosition = (getCurrentPosition + 0.5) as int
		nodes.get(currentPosition)
	}

	def getDiagram() {
		host.rootDiagram
	}
}

class SpinTransition extends Transition {

	AbstractXNodeChooser tool

	double startPosition
	double endPosition

	new(AbstractXNodeChooser tool) {
		this.tool = tool
		interpolator = [alpha|1 - (1 - alpha) * (1 - alpha)]
		onFinished = [
			tool.currentPosition = endPosition
		]
	}

	def setTargetPositionDelta(int targetPositionDelta) {
		startPosition = tool.currentPosition
		endPosition = (startPosition + targetPositionDelta) as int
		while (endPosition < 0) {
			startPosition = startPosition + tool.nodes.size
			endPosition = endPosition + tool.nodes.size
		}
		setDuration(2000)
		playFromStart
	}

	def setTargetPosition(double targetPosition) {
		if (targetPosition < tool.nodes.size) {
			startPosition = tool.currentPosition
			endPosition = targetPosition as int
			if (abs(targetPosition - startPosition) > tool.nodes.size / 2) {
				if (targetPosition > startPosition)
					startPosition = startPosition + tool.nodes.size()
				else
					endPosition = endPosition + tool.nodes.size()
			}
			setDuration(1000)
			playFromStart
		}
	}

	protected def setDuration(double max) {
		val duration = min(max, abs((endPosition - startPosition) % tool.nodes.size) * 200)
		cycleDuration = Duration::millis(duration)
	}

	override protected interpolate(double alpha) {
		tool.currentPosition = interpolator.interpolate(startPosition, endPosition, alpha)
	}

}
