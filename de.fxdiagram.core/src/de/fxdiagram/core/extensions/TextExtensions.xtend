package de.fxdiagram.core.extensions

import com.sun.javafx.tk.Toolkit
import javafx.geometry.Dimension2D
import javafx.scene.text.Text

class TextExtensions {
	
	static def Dimension2D getOfflineDimension(Text it) {
		new Dimension2D(
			getOfflineWidth(it),
			getOfflineHeight(it))
	}
	
	def static getOfflineWidth(Text it) {
		fontLoader.computeStringWidth(text, font)
	}
	
	def static getOfflineHeight(Text it) {
		fontLoader.getFontMetrics(font).lineHeight
	}
	
	private static def getFontLoader() {
		Toolkit.toolkit.fontLoader
	}
}