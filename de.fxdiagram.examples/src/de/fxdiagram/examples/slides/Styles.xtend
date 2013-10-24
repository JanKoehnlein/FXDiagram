package de.fxdiagram.examples.slides

import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment

class Styles {
	
	static def createText(String text, int fontSize) {
		createText(text, 'Gill Sans', fontSize)
	}
	
	static def createJungleText(String text, int fontSize) {
		createText(text, 'Chalkduster', fontSize)
	}
	
	static def createText(String text, String fontName, int fontSize) {
		new Text => [
			it.text = text.trim
			it.textAlignment = TextAlignment.CENTER
			it.font = new Font(fontName, fontSize)
			it.fill = jungleGreen()  
		]
	}
	
	def static jungleGreen() {
		Color.rgb(224, 237, 214)
	}
	
	def static jungleDarkGreen() {
		Color.rgb(161, 171, 74)
	}
	
	
}