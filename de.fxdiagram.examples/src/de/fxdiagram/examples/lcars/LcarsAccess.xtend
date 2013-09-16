package de.fxdiagram.examples.lcars

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.Mongo

class LcarsAccess {
	
	Mongo mongo
	
	DB db
	
	DBCollection lcars
	
	new() {
		mongo = new Mongo
		db = mongo.getDB('startrek')
		lcars = db.getCollection('lcars')
	}
	
	def query(String fieldName, Object fieldValue) {
		(lcars.find(new BasicDBObject => [
			put(fieldName, fieldValue)
		]) as Iterable<DBObject>).toList
	}
}