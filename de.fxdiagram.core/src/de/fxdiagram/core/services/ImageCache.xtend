package de.fxdiagram.core.services

import java.io.ByteArrayInputStream
import java.util.Map
import javafx.scene.image.Image

import static de.fxdiagram.core.extensions.ClassLoaderExtensions.*

class ImageCache {
	
	Map<String, Image> cache = newHashMap
	
	static ImageCache INSTANCE
	  
	static def get() {
		INSTANCE ?: (INSTANCE = new ImageCache)	
	}
	
	def getImage(Object context, String file) {
		val cachedImage = cache.get(file)
		if (cachedImage != null) {
			cachedImage
		} else {
			val image = new Image(toURI(context, file))
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