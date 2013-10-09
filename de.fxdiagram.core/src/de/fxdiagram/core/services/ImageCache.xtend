package de.fxdiagram.core.services

import java.io.ByteArrayInputStream
import java.util.Map
import javafx.scene.image.Image

class ImageCache {
	
	Map<String, Image> cache = newHashMap
	
	static ImageCache INSTANCE
	  
	static def get() {
		INSTANCE ?: (INSTANCE = new ImageCache)	
	}
	
	def getImage(String file, ClassLoader classLoader) {
		val cachedImage = cache.get(file)
		if (cachedImage != null) {
			cachedImage
		} else {
			val image = new Image(classLoader.getResourceAsStream(file))
			put(file, image)
			image
		}
	}
	
	def getImage(String file, ()=>byte[] dataAccess) {
		val cachedImage = cache.get(file)
		if (cachedImage != null) {
			cachedImage
		} else {
			val image = new Image(new ByteArrayInputStream(dataAccess.apply))
			put(file, image)
			image
		}
	}
	
	def containsImage(String file) {
		cache.containsKey(file)
	}
	
	def put(String key, Image image) {
		cache.put(key, image)
	}
}