package de.fxdiagram.examples.slides.eclipsecon

import de.fxdiagram.core.services.ImageCache
import de.fxdiagram.examples.slides.ClickThroughSlide
import de.fxdiagram.examples.slides.Slide
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment

class EclipseConSlideFactory {
	
	static def createSlide(String slideName) {
		new Slide(slideName, backgroundImage)
	} 

	static def createSlide(String text, int fontSize) {
		new Slide(text, backgroundImage)=> [
			getStackPane => [
				children += createText(text, fontSize)
			]
		]
	} 
	
	static def createClickThroughSlide(String slideName) {
		new ClickThroughSlide(slideName, backgroundImage)
	}
	
	static def getBackgroundImage() {
		ImageCache.get().getImage(EclipseConSlideFactory, 'images/jungle.jpg')
	}
	
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
	
	static def jungleGreen() {
		Color.rgb(224, 237, 214)
	}
	
	static def jungleDarkGreen() {
		Color.rgb(161, 171, 74)
	}
	
	static def jungleDarkestGreen() {
		Color.rgb(107, 114, 51)
	}
}