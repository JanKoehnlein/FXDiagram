package de.fxdiagram.examples

import brickbreaker.Config
import brickbreaker.Main.MainFrame
import de.fxdiagram.lib.nodes.FlipNode
import de.fxdiagram.lib.nodes.RectangleBorderPane
import javafx.geometry.Insets
import javafx.geometry.VPos
import javafx.scene.Group
import javafx.scene.layout.StackPane
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text

import static extension de.fxdiagram.core.extensions.DoubleExpressionExtensions.*
import static extension de.fxdiagram.core.extensions.TooltipExtensions.*

class BrickBreakerNode extends FlipNode {

	new() {
		front = new RectangleBorderPane => [
			children += new Text => [
				text = "BrickBreaker"
				textOrigin = VPos.TOP
				StackPane.setMargin(it, new Insets(10, 20, 10, 20))
			]
			tooltip = 'Double-click to play'
		]
		back = new Group => [
			children += new Group => [
				children += createRoot
				scaleXProperty.bind(widthProperty / Config.SCREEN_WIDTH)
				scaleYProperty.bind(heightProperty / Config.SCREEN_HEIGHT)
			]
		]
	}

	def createRoot() {
		Config.initialize
		val root = new Group => [
			clip = new Rectangle => [
				width = Config.SCREEN_WIDTH
				height = Config.SCREEN_HEIGHT
			]
		]

		// some reflection hassle to deal with private members 
		val main = new brickbreaker.Main
		val constructor = MainFrame.getDeclaredConstructor(brickbreaker.Main, Group)
		constructor.accessible = true
		val mainFrame = constructor.newInstance(main, root)
		val mainFrameField = brickbreaker.Main.declaredFields.filter[name == "mainFrame"].head
		mainFrameField.accessible = true
		mainFrameField.set(main, mainFrame)
		mainFrame.changeState(MainFrame.SPLASH)
		root
	}
	
}
