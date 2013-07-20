package de.fxdiagram.examples

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.examples.java.JavaTypeNode
import de.fxdiagram.examples.lcars.LcarsAccess
import de.fxdiagram.examples.lcars.LcarsNode
import de.fxdiagram.lib.media.BrowserNode
import de.fxdiagram.lib.media.ImageNode
import de.fxdiagram.lib.media.MovieNode
import de.fxdiagram.lib.media.RecursiveImageNode
import de.fxdiagram.lib.simple.NestedDiagramNode
import de.fxdiagram.lib.simple.SimpleNode
import java.net.URL
import javafx.application.Application
import javafx.concurrent.Task
import javafx.geometry.Rectangle2D
import javafx.scene.PerspectiveCamera
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.stage.Stage

class Main extends Application {

	def static main(String... args) {
		launch(args)
	}

	override start(Stage it) {
		title = "FX Diagram Demo"
		scene = createScene
		show
	}

	def createScene() {
		val root = new XRoot
		val scene = new Scene(root, 1024, 768)
		scene.setCamera(new PerspectiveCamera)

		val diagram = root.diagram
		diagram.activate()
		
		val source = new NestedDiagramNode('source') => [
			layoutX = 280
			layoutY = 170
			width = 80
			height = 30
		]
		diagram.addNode(source)

		val target = new SimpleNode('target') => [
			layoutX = 280
			layoutY = 280
			width = 80
			height = 30
		]
		diagram.addNode(target)

		val connection = new XConnection(source, target)
		val connectionLabel = new XConnectionLabel(connection)
		connectionLabel.text.text = 'label'
		diagram.addConnection(connection)

		val target2 = new SimpleNode('target2') => [
			layoutX = 400
			layoutY = 240
			width = 80
			height = 30
		]
		diagram.addNode(target2)

		val connection2 = new XConnection(source, target2)
		val connectionLabel2 = new XConnectionLabel(connection2)
		connectionLabel2.text.text = 'label2'
		diagram.addConnection(connection2)

		val connection3 = new XConnection(target, target2)
		val connectionLabel3 = new XConnectionLabel(connection3)
		connectionLabel3.text.text = 'label3'
		diagram.addConnection(connection3)

		val image = new ImageNode => [
			image = new Image("media/seltsam.jpg", true)
			layoutX = 100
			layoutY = 100
			width = 100
		]
		diagram.addNode(image)

		val movie = new MovieNode => [
			movieUrl = this.class.classLoader.getResource("media/ScreenFlow.mp4")
			width = 640
			height = 360
			view.viewport = new Rectangle2D(0, 60, 640, 360)
			layoutX = 100
			layoutY = 200
		]
		diagram.addNode(movie)

		val recursive = new RecursiveImageNode(new Image("media/seltsam.jpg", true), 10, 0, 0.5) => [
			width = 120
			height = 90
		]
		diagram.addNode(recursive)
		
		val browser = new BrowserNode => [
			width = 120
			height = 160
			layoutX = 100
			layoutY = 500
			pageUrl = new URL("http://koehnlein.blogspot.de/")
		]
		diagram.addNode(browser)
		
		val brickBreakerNode = new BrickBreakerNode => [
			width = 640
			height = 480
			layoutX = 500
			layoutY = 100
		]
		diagram.addNode(brickBreakerNode)

		val javaTypeNode = new JavaTypeNode => [
			javaType = Button
			width = 160
			height = 120
			layoutX = 500
			layoutY = 200
		]
		diagram.addNode(javaTypeNode)

		val kirk = LcarsAccess.get.query('name', 'James T. Kirk').get(0)
		diagram.addNode(new LcarsNode(kirk) => [
			width = 120
		])
		val Task<Void> task = [|
			new Layouter
			null
		]
		task.run
		scene
	}
}
