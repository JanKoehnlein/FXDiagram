package de.fxdiagram.examples.slides.democamp

import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.examples.slides.ClickThroughSlide
import de.fxdiagram.examples.slides.Slide
import javafx.scene.paint.Color
import javafx.scene.text.Text

import static de.fxdiagram.examples.lcars.LcarsExtensions.*
import javafx.scene.text.TextAlignment

class DemoCampSlideFactory {
	
	def static createSlide(String slideName) {
		new Slide(slideName, backgroundImage)
	} 
	
	def static createSlideWithText(String slideName, String text, int fontSize) {
		new Slide(slideName, backgroundImage) => [
			stackPane.children += createText(text.trim, fontSize) => [
				textAlignment = TextAlignment.CENTER
			]
		]
	} 
	
	static def createClickThroughSlide(String slideName) {
		new ClickThroughSlide(slideName, backgroundImage) => [
			initializeGraphics
		]
	}
	
	def static createText(String text, double size) {
		new Text => [
			it.text = text
			font = lcarsFont(size)
			fill = textColor
		]
	}
	
	def static getTextColor() {
		Color.rgb(238, 191, 171)
	}
	
	def static getDarkTextColor() {
		Color.rgb(156, 124, 114)
	}
	
	def static getBackgroundImage() {
		ImageCache.get().getImage(DemoCampSlideFactory, 'images/planet.jpg')
	}
	
}