package de.fxdiagram.core.tools

import de.fxdiagram.core.XButton
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import de.fxdiagram.core.behavior.MoveBehavior
import de.fxdiagram.core.extensions.SoftTooltip
import java.util.Collection
import javafx.event.EventHandler
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

import static de.fxdiagram.core.extensions.TimerExtensions.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.ButtonExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import javafx.scene.Node
import de.fxdiagram.core.anchors.ArrowHead

class SelectionTool implements XDiagramTool {

	XRoot root

	EventHandler<MouseEvent> mousePressedHandler
	EventHandler<MouseEvent> mouseDraggedHandler
	EventHandler<MouseEvent> mouseReleasedHandler
	
	SoftTooltip positionTip 

	boolean isActionOnDiagram = false
	boolean hasDragged = false

	new(XRoot root) {
		this.root = root
		this.mousePressedHandler = [ event |
			val selection = root.currentSelection.toSet
			hasDragged = false
			if(event.target == root.diagramCanvas) {
				isActionOnDiagram = true
			} else if (!(event.targetButton instanceof XButton)) {
				val targetShape = event.targetShape
				if (targetShape?.isSelectable) {
					val targetWasSelected = targetShape.selected
					if(event.shortcutDown)
						targetShape.selected = ! targetShape.selected
					else 
						targetShape.select(event)
					if (targetWasSelected || event.shortcutDown) 
						selection.deselect[it.diagram != targetShape.diagram]
					else  
						selection.deselect[true]
					if(targetShape.selected)
						selection.add(targetShape)
					selection.forEach [
						getBehavior(MoveBehavior)?.mousePressed(event)
					]
					targetShape.getBehavior(MoveBehavior)?.mousePressed(event)
					updatePositionTooltip(selection, event.sceneX, event.sceneY)
					defer([|showPositionTooltip], 200.millis)
				}
				isActionOnDiagram = false
			}
		]
		this.mouseDraggedHandler = [
			hasDragged = true
			if(!isActionOnDiagram) {
				val selection = root.currentSelection
				for (shape : selection) 
					shape?.getBehavior(MoveBehavior)?.mouseDragged(it)
				root.diagram.auxiliaryLinesSupport?.show(selection)	
				updatePositionTooltip(selection, sceneX, sceneY)
				showPositionTooltip
				consume
			}
		]
		this.mouseReleasedHandler = [
			if(isActionOnDiagram && !hasDragged && it.button == MouseButton.PRIMARY)
				root.currentSelection.forEach[selected = false]
			root.diagram.auxiliaryLinesSupport?.hide()
			hidePositionTooltip
		]
	}
	
	protected def updatePositionTooltip(Iterable<? extends XShape> selection, double screenX, double screenY) {
		var selectionBounds = selection.map[localToRootDiagram(snapBounds)].reduce[a, b | a + b]
		if(selectionBounds != null) {
			val positionString = String.format("(%.3f : %.3f)", selectionBounds.minX, selectionBounds.minY)
			positionTip = positionTip ?: new SoftTooltip(root.headsUpDisplay, positionString)
			positionTip.setReferencePosition(screenX, screenY)
			positionTip.text = positionString
		}
	}
	
	protected def showPositionTooltip() {
		if(positionTip != null && !positionTip.showing) 
			positionTip.show
	}
	
	protected def hidePositionTooltip() {
		if(positionTip?.showing) 
			positionTip.hide
		positionTip = null			
	}
	
	protected def deselect(Collection<XShape> selection, (XShape)=>boolean filter) {
		val i = selection.iterator()
		while(i.hasNext) {
			val element = i.next
			if(filter.apply(element)) {
				element.selected = false				
				i.remove
			}
		}
	}

	override activate() {
		root.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler)
		root.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler)
		root.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler)
		true
	}

	override deactivate() {
		root.removeEventFilter(MouseEvent.MOUSE_PRESSED, mousePressedHandler)
		root.removeEventFilter(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler)
		root.removeEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler)
		true
	}
	
	private def getTargetShape(MouseEvent event) {
		val target = event.target
		if(target instanceof Node) 
			target.targetShape 
		else 
			null
	}
	
	private def XShape getTargetShape(Node it) {
		switch it {
			XShape case selectable: it
			ArrowHead: connection
			case null: null
			default: parent.targetShape
		}
	}
}
