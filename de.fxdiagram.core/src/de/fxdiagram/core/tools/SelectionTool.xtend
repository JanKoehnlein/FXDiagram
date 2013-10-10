package de.fxdiagram.core.tools

import de.fxdiagram.core.XControlPoint
import de.fxdiagram.core.XRapidButton
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.XShape
import java.util.Collection
import javafx.event.EventHandler
import javafx.scene.control.Tooltip
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent

import static de.fxdiagram.core.extensions.TimerExtensions.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*

class SelectionTool implements XDiagramTool {

	XRoot root

	EventHandler<MouseEvent> mousePressedHandler
	EventHandler<MouseEvent> mouseDraggedHandler
	EventHandler<MouseEvent> mouseReleasedHandler
	
	Tooltip positionTip 

	new(XRoot root) {
		this.root = root
		this.mousePressedHandler = [ event |
			val selection = root.currentSelection.toSet
			if(event.target == root.diagramCanvas && event.button == MouseButton.PRIMARY) {
				selection.deselect[true]
			} else if (!(event.targetButton instanceof XRapidButton)) {
				val targetShape = event.targetShape
				if (targetShape?.isSelectable) {
					if (!targetShape.selected && !event.shortcutDown) {
						val skip = switch targetShape {
							XControlPoint: targetShape.parent.containerShape
							default: null
						}
						selection.deselect[it != skip]
					} 
					selection.deselect[it.diagram != targetShape.diagram]
					if(event.shortcutDown)
						targetShape.toggleSelect(event)
					else 
						targetShape.select(event)
					if(targetShape.selected)
						selection.add(targetShape)
					selection.forEach [
						moveBehavior?.mousePressed(event)
					]
					targetShape.moveBehavior?.mousePressed(event)
					updatePositionTooltip(selection, event.screenX, event.screenY)
					defer([|showPositionTooltip], 200.millis)
				}
			}
		]
		this.mouseDraggedHandler = [
			val selection = root.currentSelection
			for (shape : selection) 
				shape?.moveBehavior?.mouseDragged(it)
			root.diagram.auxiliaryLinesSupport?.show(selection)	
			updatePositionTooltip(selection, screenX, screenY)
			showPositionTooltip
			consume
		]
		this.mouseReleasedHandler = [
			root.diagram.auxiliaryLinesSupport?.hide()
			hidePositionTooltip
		]
	}
	
	protected def updatePositionTooltip(Iterable<? extends XShape> selection, double screenX, double screenY) {
		var selectionBounds = selection.map[localToRootDiagram(snapBounds)].reduce[a, b | a + b]
		if(selectionBounds != null) {
			positionTip = positionTip ?: new Tooltip
			val positionString = String.format("(%.3f : %.3f)", selectionBounds.minX, selectionBounds.minY)
			positionTip.text = positionString
			positionTip.x = screenX + 10
			positionTip.y = screenY - 40	
		}
	}
	
	protected def showPositionTooltip() {
		if(positionTip != null && !positionTip.showing) 
			positionTip.show(root.scene.window)
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
}
