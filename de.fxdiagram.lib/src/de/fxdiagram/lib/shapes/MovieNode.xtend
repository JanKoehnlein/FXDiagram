package de.fxdiagram.lib.shapes

import de.fxdiagram.core.XNode
import java.net.URL
import javafx.geometry.Insets
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.media.MediaView
import javafx.scene.layout.StackPane

class MovieNode extends XNode {

	Media media

	MediaPlayer player

	MediaView view
	
	int border = 10

	new(URL movieUrl) {
		node = new StackPane => [
			style = "-fx-background-color: #888888;"
			padding = new Insets(border, border, border, border)
			children += view = new MediaView => [
				media = new Media(movieUrl.toString)
				mediaPlayer = player = new MediaPlayer(media)
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
			]
		]
	}
	
	override setWidth(double width) {
		view.fitWidth = width - 2 * border
	}

	override setHeight(double height) {
		view.fitHeight = height - 2 * border
	}
	
	def getMedia() {
		media
	}
	
	def getPlayer() {
		player
	}
	
	def getView() {
		view
	}
}
