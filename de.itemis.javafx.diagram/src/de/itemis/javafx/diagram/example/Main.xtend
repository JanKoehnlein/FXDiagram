package de.itemis.javafx.diagram.example

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import de.itemis.javafx.diagram.XDiagram
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
		val diagram = new XDiagram
        val scene = new Scene(diagram.getRootPane, 400, 400)
        diagram.activate
        val source = new MyNode('source')
        val target = new MyNode('target')
        diagram.addNode(source)
        diagram.addNode(target)
        diagram.addConnection(new XConnection(source, target))
		scene
	}    
}