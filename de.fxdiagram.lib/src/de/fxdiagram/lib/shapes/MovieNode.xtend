package de.fxdiagram.lib.shapes

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ReadOnly
import de.fxdiagram.core.XNode
import de.fxdiagram.core.export.SvgExportable
import de.fxdiagram.core.export.SvgExporter
import javafx.geometry.Insets
import javafx.scene.SnapshotParameters
import javafx.scene.image.WritableImage
import javafx.scene.layout.StackPane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.transform.Scale

import static java.lang.Math.*

import static extension de.fxdiagram.core.Extensions.*
import static extension de.fxdiagram.core.transform.TransformExtensions.*
import java.net.URL

class MovieNode extends XNode implements SvgExportable {

	static int instanceCount
	 
	@FxProperty@ReadOnly Media media

	MediaPlayer player

	MediaView view
	
	int border = 10

	new() {
		node = new StackPane => [
			style = "-fx-background-color: #888888;"
			padding = new Insets(border, border, border, border)
			children += view = new MediaView
		]
		key = 'MovieNode' + instanceCount
		instanceCount = instanceCount + 1
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
		view.mediaPlayer = player = new MediaPlayer(media)
		onMouseClicked = [
			switch player.status {
				case MediaPlayer.Status.PLAYING:
					player.pause
				case MediaPlayer.Status.PAUSED:
					player.play
				case MediaPlayer.Status.READY:
					player.play
				case MediaPlayer.Status.STOPPED:
					player.play
			}
		]
	}
	
	def getPlayer() {
		player
	}
	
	def getView() {
		view
	}
	
	override toSvgElement(extension SvgExporter exporter) {
	 	val mediaScale = min(view.fitWidth / media.width, view.fitHeight / media.height)
	 	val imageWidth = (media.width + 2 * border / mediaScale) as int
		val imageHeight = (media.height + 2 * border / mediaScale) as int
		val image = new WritableImage(imageWidth, imageHeight)
	 	val scale = imageWidth / layoutBounds.width
	 	val t = localToDiagramTransform * new Scale(scale, scale, scale) 
	 	snapshot(new SnapshotParameters => [
	 		transform = t
	 	], image)
	 	toSvgImage(this, image)
	}
}
