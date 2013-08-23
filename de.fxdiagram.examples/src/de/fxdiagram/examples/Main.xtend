package de.fxdiagram.examples

import de.fxdiagram.core.XConnection
import de.fxdiagram.core.XConnectionKind
import de.fxdiagram.core.XConnectionLabel
import de.fxdiagram.core.XDiagram
import de.fxdiagram.core.XRoot
import de.fxdiagram.core.layout.Layouter
import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.examples.java.JavaTypeNode
import de.fxdiagram.examples.lcars.LcarsDiagram
import de.fxdiagram.examples.neonsign.NeonSignNode
import de.fxdiagram.lib.media.BrowserNode
import de.fxdiagram.lib.media.ImageNode
import de.fxdiagram.lib.media.MovieNode
import de.fxdiagram.lib.media.RecursiveImageNode
import de.fxdiagram.lib.simple.LevelOfDetailDiagramNode
import de.fxdiagram.lib.simple.OpenableDiagramNode
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
		root.activate
		val diagram = new XDiagram
		root.diagram = diagram
		
		val source = new LevelOfDetailDiagramNode('LOD diagram', createDummyDiagram) => [
			layoutX = 280
			layoutY = 170
			width = 80
			height = 30
		]
		diagram.nodes += source

		val target = new SimpleNode('Simple Node') => [
			layoutX = 280
			layoutY = 280
			width = 80
			height = 30
		]
		diagram.nodes += target

		val connection = new XConnection(source, target) => [
			kind = XConnectionKind.QUAD_CURVE
		]
		new XConnectionLabel(connection) => [
			text.text = 'quadratic'
		]
		diagram.connections += connection

		val target2 = new OpenableDiagramNode('Openable', createDummyDiagram) => [
			layoutX = 400
			layoutY = 240
			width = 80
			height = 30
		]
		diagram.nodes += target2

		val connection2 = new XConnection(source, target2) => [
			kind = XConnectionKind.CUBIC_CURVE
		]
		val connectionLabel2 = new XConnectionLabel(connection2)
		connectionLabel2.text.text = 'cubic'
		diagram.connections += connection2

		val connection3 = new XConnection(target, target2)
		val connectionLabel3 = new XConnectionLabel(connection3)
		connectionLabel3.text.text = 'polyline'
		diagram.connections += connection3

		val image = new ImageNode => [
			image = new Image("media/seltsam.jpg", true)
			layoutX = 100
			layoutY = 100
			width = 100
		]
		diagram.nodes += image

		val movie = new MovieNode => [
			movieUrl = this.class.classLoader.getResource("media/ScreenFlow.mp4")
			width = 640
			height = 360
			view.viewport = new Rectangle2D(0, 60, 640, 360)
			layoutX = 100
			layoutY = 200
		]
		diagram.nodes += movie

		val recursive = new RecursiveImageNode(ImageCache.get.getImage("media/seltsam.jpg"), 10, 0, 0.3) => [
			width = 120
			height = 90
		]
		diagram.nodes += recursive
		
		val browser = new BrowserNode => [
			width = 120
			height = 160
			layoutX = 100
			layoutY = 500
			pageUrl = new URL("http://koehnlein.blogspot.de/")
		]
		diagram.nodes += browser
		
		val brickBreakerNode = new BrickBreakerNode => [
			width = 640
			height = 480
			layoutX = 500
			layoutY = 100
		]
		diagram.nodes += brickBreakerNode

		val javaTypeNode = new JavaTypeNode => [
			javaType = Button
			width = 160
			height = 120
			layoutX = 500
			layoutY = 200
		]
		diagram.nodes += javaTypeNode

		val neonSignNode = new NeonSignNode => [
			layoutX = 500
			layoutY = 10
			width = 80
			height = 30
		]
		diagram.nodes += neonSignNode

		diagram.nodes += new OpenableDiagramNode('LCARS', new LcarsDiagram) => [
			layoutX = 300
			layoutY = 300
			width = 80
			height = 30
		]

		val Task<Void> task = [|
			new Layouter
			null
		]
		task.run
		root.centerDiagram
		scene
	}
	
	int nr = 0

	protected def XDiagram createDummyDiagram() {
		new XDiagram => [
			contentsInitializer = [
				nodes += new SimpleNode("Inner " + nr) => [
					relocate(0,0)
				]
				nodes += new SimpleNode("Inner " + nr + 1) => [
					relocate(100,100)
				]
				nodes += new LevelOfDetailDiagramNode("Nested " + nr + 2, createDummyDiagram) => [
					it.relocate(50, 50)
				]
			]
			nr = nr + 3
		]
	}
}
