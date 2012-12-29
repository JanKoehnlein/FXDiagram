package de.itemis.javafx.diagram.example

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import de.itemis.javafx.diagram.XRootDiagram

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
        val scene = new Scene(diagram, 400, 400)
        diagram.activate()
        val source = new MyContainerNode('source')
//        val target = new MyContainerNode('target')
        diagram.addNode(source)
//        diagram.addNode(target)
//        val connection = new XConnection(source, target)
//		diagram.addConnection(connection)
		scene
	}    
}