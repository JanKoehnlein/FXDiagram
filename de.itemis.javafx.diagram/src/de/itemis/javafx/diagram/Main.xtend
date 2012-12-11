package de.itemis.javafx.diagram

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class Main extends Application {
	
	def static main(String... args) {
		launch(args)
	}

    override start(Stage primaryStage) {
        primaryStage.setTitle("Diagram Demo")
        val diagram = createDiagram
        val scene = new Scene(diagram.getRootPane, 400, 400)
        diagram.activate
        primaryStage.scene = scene
        primaryStage.show
    }

	def createDiagram() {
		val diagram = new XDiagram
        val source = new MyNode('source')
        val target = new MyNode('target')
        diagram.addNode(source)
        diagram.addNode(target)
        diagram.addConnection(new XConnection(source, target))
		diagram
	}    
}