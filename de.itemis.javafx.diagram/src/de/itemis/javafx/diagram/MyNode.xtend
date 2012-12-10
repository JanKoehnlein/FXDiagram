package de.itemis.javafx.diagram

import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle
import javafx.scene.paint.Color
import javafx.scene.control.Label
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.paint.CycleMethod

class MyNode extends ShapeContainer {
	 new(String name) {
    	setNode(new StackPane => [
	    	children += new Rectangle => [
	    		width=80
	    		height=30
	    		fill=createFill
	    		stroke=Color::BLACK
	    	]
    		children += new Label => [
    			text = name
    		]
    	])
    }
    
    def protected createFill() {
    	val stops = newArrayList(new Stop(0, Color::gray(0.6)), new Stop(1, Color::gray(0.9)))
		new LinearGradient(0, 0, 1, 1, true, CycleMethod::NO_CYCLE, stops);
    }
}