package de.fxdiagram.lib.example

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XNode
import de.fxdiagram.core.XRootDiagram
import javafx.application.Application
import javafx.geometry.Rectangle2D
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Stage

class Main extends Application {

	def static main(String... args) {
		launch(args)
	}

	override start(Stage primaryStage) {
		primaryStage.setTitle("FX Diagram Demo")
		primaryStage.scene = createScene
		primaryStage.show
	}

	def createScene() {
		val diagram = new XRootDiagram
		val scene = new Scene(diagram, 640, 480)
		scene.setCamera(new PerspectiveCamera)
		diagram.activate()
		
		val source = new de.fxdiagram.lib.shapes.NestedDiagramNode('source') => [
			layoutX = 280
			layoutY = 170
			width = 80
			height = 30
		]
		diagram.addNode(source)

		val target = new de.fxdiagram.lib.shapes.SimpleNode('target') => [
			layoutX = 280
			layoutY = 280
			width = 80
			height = 30
		]
		diagram.addNode(target)

		val connection = new XConnection(source, target)
		val connectionLabel = new XConnectionLabel(connection)
		connectionLabel.text = 'label'
		diagram.addConnection(connection)

		val image = new XNode => [
			node = new ImageView => [
				image = new Image("media/seltsam.jpg", true)
				preserveRatio = true
				fitWidth = 100
				fitHeight = 75
			]
			layoutX = 100
			layoutY = 100
		]
		diagram.addNode(image)

		val movie = new de.fxdiagram.lib.shapes.MovieNode(class.classLoader.getResource("media/ScreenFlow.mp4")) => [
			width = 160
			height = 90
			getView.viewport = new Rectangle2D(0, 60, 640, 360)
			layoutX = 100
			layoutY = 200
		]
		diagram.addNode(movie)

		val recursive = new de.fxdiagram.lib.shapes.RecursiveImageNode(new Image("media/seltsam.jpg", true), 10, 0, 0.5) => [
			width = 120
			height = 90
		]
		diagram.addNode(recursive)
		scene
	}
}
