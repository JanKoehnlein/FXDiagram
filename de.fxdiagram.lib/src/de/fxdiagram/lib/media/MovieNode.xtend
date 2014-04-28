package de.fxdiagram.lib.media

import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.services.ResourceDescriptor
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.animation.FadeTransition
import javafx.animation.Interpolator
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.text.Text


import static extension de.fxdiagram.core.extensions.TooltipExtensions.*
import static extension javafx.util.Duration.*
import static de.fxdiagram.core.extensions.ClassLoaderExtensions.*

@ModelNode(#['layoutX', 'layoutY', 'domainObject', 'width', 'height'])
class MovieNode extends FlipNode {

	Media media

	StackPane pane = new RectangleBorderPane

	MediaPlayer player

	MediaView view = new MediaView

	Node controlBar

	int border = 10
	
	new(ResourceDescriptor movieDescriptor) {
		super(movieDescriptor)
	}
	
	protected override createNode() {
		val node = super.createNode
		media = new Media((domainObject as ResourceDescriptor).toURI)
		player = new MediaPlayer(media)
		front = new RectangleBorderPane => [
			children += new Text => [
				text = name
				textOrigin = VPos.TOP
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
		]
		back = pane => [
			id = "pane"
			padding = new Insets(border, border, border, border)
			children += view
		]
		node
	}
	
	override initializeGraphics() {
		super.initializeGraphics()
		stylesheets += toURI(this, 'MovieNode.css')
	}
	
	override doActivate() {
		super.doActivate()
		front.tooltip = 'Double-click to watch'
		pane.visibleProperty.addListener [
			prop, oldVal, newVal |
			view.mediaPlayer = if(newVal) player else null
		] 
		controlBar = createControlBar
		pane => [
			onMouseEntered = [
				new FadeTransition => [
					node = controlBar
					toValue = 1.0
					duration = 200.millis
					interpolator = Interpolator.EASE_OUT
					play
				]
			]
			onMouseExited = [
				new FadeTransition => [
					node = controlBar
					toValue = 0
					duration = 200.millis
					interpolator = Interpolator.EASE_OUT
					play
				]
			]
			children += new Group => [
				children += controlBar
				StackPane.setAlignment(it, Pos.BOTTOM_CENTER)
			]
			tooltip = 'Double-click to close'
		]
	}

	override protected createAnchors() {
		new RoundedRectangleAnchors(this, 12, 12)
	}
	
	protected def createControlBar() {
		new HBox => [
			id = "controlbar"
			spacing = 0
			alignment = Pos.CENTER
			children += new Button => [
				id = "back-button"
				text = "Back"
				onAction = [ player.seek(ZERO) ]
			]
			children += new Button => [
				id = "stop-button"
				text = "Stop"
				onAction = [ player.stop ]
			]
			children += new Button => [
				id = "play-button"
				text = "Play"
				onAction = [ player.play ]
			]
			children += new Button => [
				id = "pause-button"
				text = "Pause"
				onAction = [ player.pause ]
			]
			children += new Button => [
				id = "forward-button"
				text = "Forward"
				onAction = [ player.seek((player.currentTime.toSeconds + 10).seconds) ]
			]
			opacity = 0
		]
	}
	
	override setWidth(double width) {
		super.width = width
		view.fitWidth = width - 2 * border
	}

	override setHeight(double height) {
		super.height = height
		view.fitHeight = height - 2 * border
	}

	def getPlayer() {
		player
	}

	def getView() {
		view
	}
}