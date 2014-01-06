package de.fxdiagram.examples.slides.democamp

import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.examples.slides.Slide
import javafx.scene.paint.Color
import javafx.scene.text.Text

import static de.fxdiagram.examples.lcars.LcarsExtensions.*

class DemoCampSlideFactory {
	
	def static createSlide(String slideName) {
		new Slide(slideName, backgroundImage)
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