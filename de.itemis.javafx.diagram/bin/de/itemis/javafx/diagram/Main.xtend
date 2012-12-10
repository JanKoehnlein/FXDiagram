package de.itemis.javafx.diagram

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

import static javafx.application.Application.*

class Main extends Application {
	def static main(String... args) {
		launch(args)
	}

    override start(Stage primaryStage) {
        primaryStage.setTitle("Diagram Demo")
        primaryStage.scene = new Scene(diagram.getRootPane, 300, 250)
        primaryStage.show
    }

	def getDiagram() {
		val diagram = new Diagram
        val source = new MyNode('source')
        val target = new MyNode('target')
        diagram.addShape(source)
        diagram.addShape(target)
        diagram.addConnection(new Connection(source, target))
		diagram
	}    
   
}