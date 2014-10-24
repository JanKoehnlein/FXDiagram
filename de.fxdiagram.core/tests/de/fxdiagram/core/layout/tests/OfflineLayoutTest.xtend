package de.fxdiagram.core.layout.tests

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Stage

class OfflineLayoutTest extends Application {

	def static void main(String[] args) {
		launch()
	}
	
	override start(Stage primaryStage) throws Exception {
		val text = new Text('foo')
		val root = new VBox => [
			spacing = 10
			children += text
			children += new Text('barbar')
		]
		val scene = new Scene(root)
		root.applyCss
//		root.layout
		root.autosize
		println(root.width+' '+root.height)
	}
		
}