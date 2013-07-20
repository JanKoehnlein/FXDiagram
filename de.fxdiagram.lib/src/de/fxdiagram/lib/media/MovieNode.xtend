package de.fxdiagram.lib.media

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.XNode
import de.fxdiagram.core.export.SvgExportable
import de.fxdiagram.core.export.SvgExporter
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
import javafx.scene.SnapshotParameters
import javafx.scene.control.Button
import javafx.scene.image.WritableImage
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.text.Text
import javafx.scene.transform.Scale

import static java.lang.Math.*

import static extension de.fxdiagram.core.Extensions.*
import static extension de.fxdiagram.core.geometry.TransformExtensions.*
import static extension javafx.util.Duration.*

class MovieNode extends XNode implements SvgExportable {

	@FxProperty@ReadOnly Media media

	StackPane pane

	MediaPlayer player

	MediaView view

	Node controlBar

	int border = 10

	new() {
		controlBar = createControlBar
		node = new FlipNode => [
			front = new RectangleBorderPane => [
				children += new Text => [
					text = "Movie"
					textOrigin = VPos.TOP
					StackPane.setMargin(it, new Insets(10, 20, 10, 20))
				]
			]
			back = pane = new StackPane => [
				id = "pane"
				padding = new Insets(border, border, border, border)
				children += view = new MediaView 
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
			]
		]
		stylesheets += "de/fxdiagram/lib/media/MovieNode.css"
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

	override toSvgElement(extension SvgExporter exporter) {
		val mediaScale = min(view.fitWidth / getMedia.width, view.fitHeight / getMedia.height)
		val imageWidth = (getMedia.width + 2 * border / mediaScale) as int
		val imageHeight = (getMedia.height + 2 * border / mediaScale) as int
		val image = new WritableImage(imageWidth, imageHeight)
		val scale = imageWidth / layoutBounds.width
		val t = localToDiagramTransform * new Scale(scale, scale, scale)
		snapshot(
			new SnapshotParameters => [
				transform = t
			], image)
		toSvgImage(this, image)
	}
}
