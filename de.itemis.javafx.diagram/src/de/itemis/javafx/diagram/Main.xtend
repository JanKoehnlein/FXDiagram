package de.itemis.javafx.diagram

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import de.itemis.javafx.diagram.tools.ZoomTool
import de.itemis.javafx.diagram.tools.SelectionTool

class Main extends Application {
	
	def static main(String... args) {
		launch(args)
	}

    override start(Stage primaryStage) {
        primaryStage.setTitle("Diagram Demo")
        val diagram = getDiagram
        val scene = new Scene(diagram.getRootPane, 300, 250)
        primaryStage.scene = scene
		new ZoomTool(diagram)   
		new SelectionTool(diagram)     
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