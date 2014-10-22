package de.fxdiagram.lib.simple

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.behavior.AbstractCloseBehavior
import de.fxdiagram.core.behavior.AbstractOpenBehavior
import de.fxdiagram.core.command.AnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.viewport.ViewportMemento
import de.fxdiagram.core.viewport.ViewportTransform
import de.fxdiagram.core.viewport.ViewportTransition
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import eu.hansolo.enzo.radialmenu.Symbol
import eu.hansolo.enzo.radialmenu.SymbolCanvas
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
import javafx.animation.SequentialTransition
import javafx.geometry.BoundingBox
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Cursor
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.util.Duration

import static java.lang.Math.*

import static extension de.fxdiagram.core.extensions.BoundsExtensions.*
import static extension de.fxdiagram.core.extensions.CoreExtensions.*
import static extension de.fxdiagram.core.extensions.DurationExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

@Logging
@ModelNode(#['innerDiagram'])
class OpenableDiagramNode extends XNode {
	
	@FxProperty XDiagram innerDiagram
	
	@FxProperty XDiagram parentDiagram 
	
	XRoot root
	
	RectangleBorderPane pane = new RectangleBorderPane
	
	Text textNode
	 
	double delayFactor = 0.1
	
	Point2D nodeCenterInDiagram
	
	DiagramScaler diagramScaler
	
	BoundingBox nodeBounds
	
	ViewportMemento viewportBeforeOpen
	
	boolean isOpen = false
	
	new(String name) {
		super(name)
	}
	
	new(DomainObjectDescriptor domainObject) {
		super(domainObject)
	}
	
	protected override createNode() {
		pane => [
			children += textNode = new Text => [
				textOrigin = VPos.TOP
				text = name
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
		]
	}
	
	override createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	override doActivate() {
		super.doActivate()
		pane.tooltip = "Double-click to open"
		cursor = Cursor.HAND
		textNode.text = name
		this.root = getRoot
		if(innerDiagram == null) {
			LOG.severe('Nested diagram not set. Deactivating open behavior')
		} else {
			node.onMouseClicked = [
				if(clickCount == 2) {
					openDiagram
				}
			]
		}
		val AbstractOpenBehavior openBehavior = [| openDiagram ]
		addBehavior(openBehavior)
	}
	
	 def getPane() {
		pane
	} 

	def getTextNode() {
		textNode
	}
	
	def openDiagram() {
		if(!isOpen)
			root.commandStack.execute(new OpenDiagramCommand(this))
		isOpen = true
	}
	
	protected def openDiagram(Duration duration) {
		viewportBeforeOpen = diagram.viewportTransform.createMemento
		nodeBounds = layoutBounds - new Insets(5,5,5,5)
		nodeCenterInDiagram = localToRootDiagram(nodeBounds.center)
		diagramScaler = new DiagramScaler(innerDiagram) => [
			width = nodeBounds.width
			height = nodeBounds.height
		]
		innerDiagram.opacity = 0
		pane.children.add(new Group => [
			children += innerDiagram
		])
		innerDiagram.activate
		val AbstractCloseBehavior closeBehavior = [| closeDiagram() ]
		innerDiagram.addBehavior(closeBehavior)
		innerDiagram.layout
		diagramScaler.activate
		// due to https://javafx-jira.kenai.com/browse/RT-37879
		// we have to manually layout the node in order to fix the bounds
		layout
		val initialScale = innerDiagram.localToScene(new BoundingBox(0,0,1,0)).width
		val diagramBoundsInLocal = innerDiagram.boundsInLocal
		val targetScale = max(ViewportTransform.MIN_SCALE, 
			min(1, 
				min(root.scene.width / diagramBoundsInLocal.width, 
					root.scene.height / diagramBoundsInLocal.height)) / initialScale) * root.viewportTransform.scale
		new ParallelTransition => [
			children += new ViewportTransition(root, nodeCenterInDiagram, targetScale) => [
				it.duration = duration
				onFinished = [
					diagramScaler.deactivate
					parentDiagram = root.diagram
					pane.children.setAll(textNode)
					val toParentButton = SymbolCanvas.getSymbol(Symbol.Type.ZOOM_OUT, 32, Color.GRAY) => [
						onMouseClicked = [
							root.headsUpDisplay.children -= target as Node
							closeDiagram
						]
						tooltip = "Parent diagram"
						
					]
					innerDiagram.fixedButtons.put(toParentButton, Pos.TOP_RIGHT)
					root.diagram = innerDiagram				
				]
			]
			children += new FadeTransition => [
				it.duration = (1 - delayFactor) * duration  
				fromValue = 1
				toValue = 0
				node = textNode
			]
			children += new FadeTransition => [
				delay = delayFactor * duration 
				it.duration = (1 - delayFactor) * duration
				fromValue = 0
				toValue = 1
				node = innerDiagram
			]
		]
	}
	
	protected def closeDiagram() {
		if(isOpen)
			root.commandStack.execute(new CloseDiagramCommand(this))
		isOpen = false
	}
	
	protected def closeDiagram(Duration duration) {
		val innerDiagramCenter = innerDiagram.nodes
				.map[layoutBounds.translate(layoutX, layoutY)]
				.reduce[b0, b1 | b0 + b1].center
		val phaseOne = new ViewportTransition(root, innerDiagramCenter, 1)
		val phaseTwo = new ParallelTransition => [
			children += new FadeTransition => [
				delay = delayFactor * duration
				it.duration = (1 - delayFactor) * duration 
				fromValue = 0
				toValue = 1
				node = textNode
			]
			children += new FadeTransition => [
				it.duration = (1 - delayFactor) * duration
				fromValue = 1
				toValue = 0
				node = innerDiagram
			]
		]
		new SequentialTransition => [
			children += phaseOne => [
				it.duration = duration * 0.3
				onFinished = [
					root.diagram = parentDiagram
					pane.children += new Group => [
						children += innerDiagram
					]
					diagramScaler.activate
					// due to https://javafx-jira.kenai.com/browse/RT-37879
					// we have to manually layout the node in order to fix the bounds
					layout
					phaseTwo.children.add(0, new ViewportTransition(root, viewportBeforeOpen, duration) => [
						onFinished = [
							diagramScaler.deactivate
							parentDiagram = root.diagram
							pane.children.setAll(textNode)
						]
					])
				]
			]
			children += phaseTwo
		]
	}
}

class OpenDiagramCommand implements AnimationCommand {
	
	OpenableDiagramNode host
	
	new(OpenableDiagramNode host) {
		this.host = host
	}
	
	override getExecuteAnimation(CommandContext context) {
		host.openDiagram(context.defaultExecuteDuration)
	}
	
	override getUndoAnimation(CommandContext context) {
		host.closeDiagram(context.defaultUndoDuration)
	}
	
	override getRedoAnimation(CommandContext context) {
		host.openDiagram(context.defaultUndoDuration)
	}
	
	override clearRedoStackOnExecute() {
		true
	}

	override skipViewportRestore() {
	}
} 

class CloseDiagramCommand implements AnimationCommand {
	
	OpenableDiagramNode host
	
	new(OpenableDiagramNode host) {
		this.host = host
	}
	
	override getExecuteAnimation(CommandContext context) {
		host.closeDiagram(context.defaultExecuteDuration)
	}
	
	override getUndoAnimation(CommandContext context) {
		host.openDiagram(context.defaultUndoDuration)
	}
	
	override getRedoAnimation(CommandContext context) {
		host.closeDiagram(context.defaultUndoDuration)
	}

	override clearRedoStackOnExecute() {
		true
	}
	
	override skipViewportRestore() {
	}
	
} 
