package de.fxdiagram.core.css

import javafx.geometry.Insets
import javafx.scene.paint.Color
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Paint

class JavaToCss {
	
	def static CharSequence toCss(Paint paint) {
		switch paint {
			Color:
				'''rgb(«(255*paint.red) as int», «(255*paint.green) as int», «(255*paint.blue) as int»)''' 
			LinearGradient: {
				'''
					linear-gradient(
						from «paint.startX * 100»% «paint.startY * 100»% 
						to «paint.endX * 100»% «paint.endY * 100»%, 
						«FOR stop: paint.stops SEPARATOR ', '»
							«stop.color.toCss» «stop.offset * 100»%
						«ENDFOR»
					)
				'''	
			}
			//	TODO: RadialGradient, ImagePattern
			default: 
				"gray"
		}
	}
	
	def static CharSequence toCss(Insets it) {
		'''«top» «right» «bottom» «left»'''
	}
}