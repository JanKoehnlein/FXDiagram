package de.fxdiagram.examples.lcars

import java.util.Map
import javafx.scene.paint.Color
import javafx.scene.text.Font

class LcarsExtensions {
	
	static Map<Double, Font> cache = newHashMap
	
	static def lcarsFont(double size) {
		val cachedFont = cache.get(size)
		if(cachedFont != null)
			return cachedFont
		val input = LcarsExtensions.classLoader.getResourceAsStream("de/fxdiagram/examples/lcars/LCARSGTJ3.ttf")
		val font = Font.loadFont(input, size)
		cache.put(size, font)
		font	
	}
	
	public static val ORANGE = rgbColor(251,134,9) 
	public static val DARKBLUE =  rgbColor(135, 132,194)
	public static val FLESH = rgbColor(253,193,137) 
	public static val DARKFLESH = rgbColor(251,135,84) 

	public static val VIOLET = rgbColor(190,131,192) 
	public static val RED = rgbColor(192,80,85) 
	public static val BLUE = rgbColor(136, 130, 254) 
	public static val MAGENTA = rgbColor(190,78,134)
	
	private static def rgbColor(int red, int green, int blue) {
		new Color(red / 255.0, green/255.0, blue/255.0, 1)
	}
}