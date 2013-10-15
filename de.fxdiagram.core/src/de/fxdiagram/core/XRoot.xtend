package de.fxdiagram.core

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.tools.CompositeTool
import de.fxdiagram.core.tools.DiagramGestureTool
import de.fxdiagram.core.tools.MenuTool
import de.fxdiagram.core.tools.SelectionTool
import de.fxdiagram.core.tools.XDiagramTool
import java.util.List
import java.util.Map
import javafx.beans.value.ChangeListener
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.layout.Pane
import javafx.scene.transform.Affine

import static de.fxdiagram.core.extensions.NumberExpressionExtensions.*
import static java.lang.Math.*

import static extension de.fxdiagram.core.css.JavaToCss.*
import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.TransformExtensions.*

@Logging
class XRoot extends Parent implements XActivatable {
	
	@FxProperty @ReadOnly boolean isActive

	@FxProperty double diagramScale = 1.0
	
	@FxProperty @ReadOnly XDiagram diagram

	HeadsUpDisplay headsUpDisplay = new HeadsUpDisplay
	
	Pane diagramCanvas = new Pane
	
	public static val MIN_SCALE = EPSILON
	
	Affine diagramTransform
	
	List<XDiagramTool> tools = newArrayList
	
	CompositeTool defaultTool
	
	XDiagramTool currentTool
	
	new() {
		children += diagramCanvas
		children += headsUpDisplay
		defaultTool = new CompositeTool
		defaultTool += new SelectionTool(this)
		defaultTool += new DiagramGestureTool(this)
		defaultTool += new MenuTool(this)
		tools += defaultTool
	}
	
	def setDiagram(XDiagram newDiagram) {
		if(diagram != null)
			diagramCanvas.children -= diagram
		diagramProperty.set(newDiagram)
		diagramCanvas.children += diagram
		if(isActive)
			newDiagram.activate
		diagramTransform = new Affine
		if(diagram.transforms.empty) {
			centerDiagram()
		} else {
			diagram.transforms.forEach [diagramTransform.leftMultiply(it)]
			diagramScale = sqrt(diagramTransform.mxx * diagramTransform.mxx + diagramTransform.mxy * diagramTransform.mxy)
		}
		diagramCanvas.style = '''
			-fx-background-color: «diagram.backgroundPaint.toCss»;
			-fx-text-fill: «diagram.foregroundPaint.toCss»;
		'''
		diagram.transforms.setAll(diagramTransform)
		headsUpDisplay.children.clear
	}
	
	def centerDiagram() {
		val diagramBounds = diagram.layoutBounds
		if(diagramBounds.width * diagramBounds.height > 1) {
			val scale = max(XRoot.MIN_SCALE, min(1, min(scene.width / diagramBounds.width, scene.height / diagramBounds.height)))
			diagramScale = scale
			diagramTransform.scale(scale, scale)
			val centerInScene = diagram.localToScene(diagram.boundsInLocal).center
			diagramTransform.translate(0.5 * scene.width - centerInScene.x, 0.5 * scene.height - centerInScene.y)
		}
	}
		
	def getHeadsUpDisplay() {
		headsUpDisplay
	}
	
	def getDiagramCanvas() {
		diagramCanvas
	}
	
	def getDiagramTransform() {
		diagramTransform
	}
	
	override activate() {		
		if(!isActive)
			doActivate
		isActiveProperty.set(true)
	}
	
	def	doActivate() {
		diagram?.activate
		diagramCanvas => [
			prefWidthProperty.bind(scene.widthProperty)
			prefHeightProperty.bind(scene.heightProperty)
		]
		setCurrentTool(defaultTool)
	}
	
	def setCurrentTool(XDiagramTool tool) {
		var previousTool = currentTool
		if(previousTool != null) {
			if(!previousTool.deactivate)
				LOG.severe("Could not deactivate active tool")
		}
		currentTool = tool
		if(tool != null) {
			if(!tool.activate) {
				currentTool = previousTool
				if(!previousTool?.activate)
					LOG.severe("Could not reactivate tool")
			}
		}
	}

	def restoreDefaultTool() {
		setCurrentTool(defaultTool)
	}
	
	def getCurrentSelection() {
		diagram.allShapes.filter[isSelectable && selected]
	}
}

class HeadsUpDisplay extends Group {
	
	Map<Node, Pos> alignments = newHashMap

	ChangeListener<Number> sceneListener

	new() {
		sceneListener = [
			property, oldVlaue, newValue |
			children.forEach [ place ]
		]
		sceneProperty.addListener [
			property, oldVal, newVal |
			oldVal?.widthProperty?.removeListener(sceneListener)			
			oldVal?.heightProperty?.removeListener(sceneListener)			
			newVal?.widthProperty?.addListener(sceneListener)			
			newVal?.heightProperty?.addListener(sceneListener)			
		]
	}
	
	def add(Node child, Pos pos) {
		children += child
		alignments.put(child, pos)
		child.place
		child.boundsInParentProperty.addListener [
			property, oldValue, newValue | 
			if(child.parent != this) 
				property.removeListener(self)
			else
				child.place
		]
	} 
	
	protected def place(Node child) {
		val pos = alignments.get(child) ?: Pos.CENTER
		val bounds = child.boundsInParent
		child.layoutX = switch(pos.hpos) {
			case HPos.LEFT:
				0
			case HPos.RIGHT:
				child.scene.width - bounds.width
			default:
				0.5 * (child.scene.width - bounds.width) 
		}
		child.layoutY = switch(pos.vpos) {
			case VPos.TOP:
				0
			case VPos.BOTTOM:
				child.scene.height - bounds.height
			default:
				0.5 * (child.scene.height - bounds.height) 
		}
	}
}

