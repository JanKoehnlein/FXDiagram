package de.fxdiagram.lib.media

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.lib.anchors.RoundedRectangleAnchors
import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import java.net.URL
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

import static de.fxdiagram.core.extensions.UriExtensions.*

import static extension de.fxdiagram.core.extensions.TooltipExtensions.*
import static extension javafx.util.Duration.*

@ModelNode(#['layoutX', 'layoutY', 'domainObject', 'width', 'height'])
class MovieNode extends FlipNode {

	@FxProperty@ReadOnly Media media

	StackPane pane = new RectangleBorderPane

	MediaPlayer player

	MediaView view = new MediaView

	Node controlBar

	int border = 10
	
	Text label

	new(String name) {
		super(name)
	}
	
	override doActivatePreview() {
		front = new RectangleBorderPane => [
			children += label = new Text => [
				text = key
				textOrigin = VPos.TOP
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
		]
		back = pane => [
			id = "pane"
			padding = new Insets(border, border, border, border)
			children += view 
			
		]
		stylesheets += toURI(this, 'MovieNode.css')
		super.doActivatePreview()
	}
	
	override doActivate() {
		super.doActivate()
		controlBar = createControlBar
		front.tooltip = 'Double-click to watch'
		back => [
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

	def setMovieUrl(URL movieUrl) {
		mediaProperty.set(new Media(movieUrl.toString))
		player = new MediaPlayer(getMedia)
		pane.visibleProperty.addListener [
			prop, oldVal, newVal |
			view.mediaPlayer = if(newVal) player else null
		]
	}

	def getPlayer() {
		player
	}

	def getView() {
		view
	}
}