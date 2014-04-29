package de.fxdiagram.lib.simple

import de.fxdiagram.annotations.logging.Logging
import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.behavior.AbstractCloseBehavior
import de.fxdiagram.core.behavior.AbstractOpenBehavior
import de.fxdiagram.core.command.AbstractAnimationCommand
import de.fxdiagram.core.command.CommandContext
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.viewport.ViewportTransform
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.RectangleBorderPane
import eu.hansolo.enzo.radialmenu.Symbol
import eu.hansolo.enzo.radialmenu.SymbolCanvas
import javafx.animation.FadeTransition
import javafx.animation.ParallelTransition
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
import de.fxdiagram.core.viewport.ViewportTransition

@Logging
@ModelNode(#['innerDiagram'])
class OpenableDiagramNode extends XNode {
	
	@FxProperty XDiagram innerDiagram
	
	@FxProperty XDiagram parentDiagram 
	
	XRoot root
	
	RectangleBorderPane pane = new RectangleBorderPane
	
	Text textNode
	
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
		root.commandStack.execute(OpenCloseDiagramCommand.newOpenCommand(this))
	}
}

@Logging
class OpenCloseDiagramCommand extends AbstractAnimationCommand {
	
	double delayFactor = 0.1
	
	boolean isOpenCommand
	
	extension OpenDiagramParameters params
	
	static def newOpenCommand(OpenableDiagramNode node) {
		new OpenCloseDiagramCommand(true, node.calculateParams)
	}
	
	static def newCloseCommand(XRoot root, OpenDiagramParameters params) {
		new OpenCloseDiagramCommand(false, params) 
	}
	
	protected new(boolean isOpenCommand, OpenDiagramParameters params) {
		this.isOpenCommand = isOpenCommand
		this.params = params
	}
	
	protected static def calculateParams(OpenableDiagramNode host) {
		val nodeBounds = host.layoutBounds - new Insets(5,5,5,5)
		val nodeCenterInDiagram = host.localToRootDiagram(nodeBounds.center)
		val diagramScaler = new DiagramScaler(host.innerDiagram) => [
			width = nodeBounds.width
			height = nodeBounds.height
		]
		new OpenDiagramParameters(host, host.root, diagramScaler, nodeCenterInDiagram)		
	}
	
	protected def openDiagram(Duration duration) {
		host.innerDiagram.opacity = 0
		host.pane.children.add(new Group => [
			children += host.innerDiagram
		])
		host.innerDiagram.activate
		val AbstractCloseBehavior closeBehavior = [| root.commandStack.execute(newCloseCommand(root, params)) ]
		host.innerDiagram.addBehavior(closeBehavior)
		host.innerDiagram.layout
		diagramScaler.activate
		val initialScale = host.innerDiagram.localToScene(new BoundingBox(0,0,1,0)).width
		val diagramBoundsInLocal = host.innerDiagram.boundsInLocal
		val targetScale = max(ViewportTransform.MIN_SCALE, 
			min(1, 
				min(root.scene.width / diagramBoundsInLocal.width, 
					root.scene.height / diagramBoundsInLocal.height)) / initialScale) * root.diagramTransform.scale
		new ParallelTransition => [
			children += new ViewportTransition(root, nodeCenterInDiagram, targetScale) => [
				it.duration = duration
				onFinished = [
					diagramScaler.deactivate
					host.parentDiagram = root.diagram
					host.pane.children.setAll(host.textNode)
					val toParentButton = SymbolCanvas.getSymbol(Symbol.Type.ZOOM_OUT, 32, Color.GRAY) => [
						onMouseClicked = [
							root.headsUpDisplay.children -= target as Node
							root.commandStack.execute(newCloseCommand(root, params))
						]
						tooltip = "Parent diagram"
						
					]
					host.innerDiagram.fixedButtons.put(toParentButton, Pos.TOP_RIGHT)
					root.diagram = host.innerDiagram				
				]
			]
			children += new FadeTransition => [
				it. duration = (1 - delayFactor) * duration  
				fromValue = 1
				toValue = 0
				node = host.textNode
			]
			children += new FadeTransition => [
				delay = delayFactor * duration 
				it.duration = (1 - delayFactor) * duration
				fromValue = 0
				toValue = 1
				node = host.innerDiagram
			]
		]
	}
	
	protected def closeDiagram(Duration duration) {
		root.diagram = host.parentDiagram
		host.pane.children += new Group => [
			children += host.innerDiagram
		]
		host.innerDiagram.activate
		host.innerDiagram.layout
		diagramScaler.activate
		new ParallelTransition => [
			children += new ViewportTransition(root, nodeCenterInDiagram, 1) => [
				it.duration = duration
				onFinished = [
					diagramScaler.deactivate
					host.parentDiagram = root.diagram
					host.pane.children.setAll(host.textNode)
				]
			]
			children += new FadeTransition => [
				delay = delayFactor * duration
				it.duration = (1 - delayFactor) * duration 
				fromValue = 0
				toValue = 1
				node = host.textNode
			]
			children += new FadeTransition => [
				it.duration = (1 - delayFactor) * duration
				fromValue = 1
				toValue = 0
				node = host.innerDiagram
			]
		]
	}
	
	override createExecuteAnimation(CommandContext context) {
		if(isOpenCommand)
			openDiagram(context.defaultExecuteDuration)
		else 
			closeDiagram(context.defaultExecuteDuration)
	}
	
	override createUndoAnimation(CommandContext context) {
		if(isOpenCommand)
			closeDiagram(context.defaultUndoDuration)
		else 
			openDiagram(context.defaultUndoDuration)
	}
	
	override createRedoAnimation(CommandContext context) {
		if(isOpenCommand)
			openDiagram(context.defaultUndoDuration)
		else 
			closeDiagram(context.defaultUndoDuration)
	}

	protected def isDiagramOpen() {
		root.diagram == host.innerDiagram
	}
} 

@Data 
class OpenDiagramParameters {
	OpenableDiagramNode host
	XRoot root
	DiagramScaler diagramScaler
	Point2D nodeCenterInDiagram
}
