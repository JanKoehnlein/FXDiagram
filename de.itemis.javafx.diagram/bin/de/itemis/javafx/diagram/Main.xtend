package de.itemis.javafx.diagram

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.shape.Rectangle
import javafx.stage.Stage

import static javafx.application.Application.*

class Main extends Application {
	def static main(String... args) {
		launch(args)
	}

    override start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!")
        val source = newShapeNode('source')
        val target = newShapeNode('target')
        val diagram = new Diagram
        diagram.addShape(source)
        diagram.addShape(target)
        diagram.addConnection(new Connection(source, target))
        primaryStage.scene = new Scene(diagram.getRootPane, 300, 250)
        primaryStage.show
    }
    
    def protected newShapeNode(String name) {
    	new ShapeContainer(new StackPane => [
	    	children += new Rectangle => [
	    		width=100
	    		height=30
	    		fill=createFill
	    		stroke=Color::BLACK
	    	]
    		children += new Label => [
    			text = name
    		]
    	])
    }
    
    def createFill() {
    	val stops = newArrayList(new Stop(0, Color::LIGHTGRAY), new Stop(1, Color::DARKGRAY))
		new LinearGradient(0, 0, 1, 1, true, CycleMethod::NO_CYCLE, stops);
    }
}