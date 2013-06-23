package de.itemis.javafx.diagram.example

import de.itemis.javafx.diagram.XConnection
import de.itemis.javafx.diagram.XConnectionLabel
import de.itemis.javafx.diagram.XRootDiagram
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.PerspectiveCamera

class Main extends Application {
	
	def static main(String... args) {
		launch(args)
	}

    override start(Stage primaryStage) {
        primaryStage.setTitle("Diagram Demo")
        primaryStage.scene = createScene
        primaryStage.show
    }

	def createScene() {
		val diagram = new XRootDiagram
        val scene = new Scene(diagram, 640, 480)
        scene.setCamera(new PerspectiveCamera)
        diagram.activate()
        val source = new MyContainerNode('source') => [
        	layoutX = 280
        	layoutY = 170
        ]
        val target = new MyContainerNode('target') => [
	        layoutX = 280
	        layoutY = 280
        ]
        val connection = new XConnection(source, target)
        val connectionLabel = new XConnectionLabel(connection)
		connectionLabel.text = 'label'
        diagram.addNode(source)
        diagram.addNode(target)
		diagram.addConnection(connection)
		scene
	}    
}