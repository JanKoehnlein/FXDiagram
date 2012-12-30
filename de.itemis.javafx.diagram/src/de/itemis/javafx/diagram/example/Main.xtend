package de.itemis.javafx.diagram.example

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import de.itemis.javafx.diagram.XRootDiagram
import de.itemis.javafx.diagram.XConnection

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
        diagram.activate()
        val source = new MyContainerNode('source')
        val target = new MyContainerNode('target')
        val connection = new XConnection(source, target)
        source.layoutX = 280
        source.layoutY = 170
        target.layoutX = 280
        target.layoutY = 280
        diagram.addNode(source)
        diagram.addNode(target)
		diagram.addConnection(connection)
		scene
	}    
}