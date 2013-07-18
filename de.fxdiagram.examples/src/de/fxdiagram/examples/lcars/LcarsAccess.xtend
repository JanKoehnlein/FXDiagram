package de.fxdiagram.examples.lcars

import com.mongodb.BasicDBObject
import com.mongodb.Mongo
import com.mongodb.DB
import com.mongodb.DBCollection
import java.util.Map
import javafx.scene.image.Image
import java.io.ByteArrayInputStream

class LcarsAccess {
	
	Mongo mongo
	
	DB db
	
	DBCollection lcars
	
	Map<String, Image> imageCache = newHashMap()
	
	static LcarsAccess INSTANCE
	
	static def get() {
		INSTANCE ?: (INSTANCE = new LcarsAccess)
	}
	
	new() {
		mongo = new Mongo
		db = mongo.getDB('startrek')
		lcars = db.getCollection('lcars')
	}
	
	def query(String fieldName, Object fieldValue) {
		lcars.find(new BasicDBObject => [
			put(fieldName, fieldValue)
		]).toList
	}
	
	def getImage(String url, byte[] data) {
		if(imageCache.containsKey(url)) {
			imageCache.get(url) 
		} else {
			val image = new Image(new ByteArrayInputStream(data))
			imageCache.put(url, image)
			image
		} 
	}
	
}