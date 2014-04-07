package de.fxdiagram.examples.lcars

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.Mongo
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.CachedDomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectDescriptor
import de.fxdiagram.core.model.DomainObjectProvider
import org.bson.types.ObjectId

@ModelNode
class LcarsModelProvider implements DomainObjectProvider{
	
	DB db
	DBCollection lcars

	new() {
		val mongo = new Mongo
		db = mongo.getDB('startrek')
		lcars = db.getCollection('lcars')
	}
	
	def query(String fieldName, Object fieldValue) {
		(lcars.find(new BasicDBObject => [
			put(fieldName, fieldValue)
		]) as Iterable<DBObject>).toList
	}
	
	def protected <T> resolveDomainObject(DomainObjectDescriptor descriptor) {
		lcars.findOne(new BasicDBObject => [
			put("_id", new ObjectId(descriptor.id))
		])
	}
	
	override createDescriptor(Object it) {
		switch it {
			DBObject: return createLcarsEntryDescriptor
			String: return createLcarsConnectionDescriptor
			default:
				throw new IllegalArgumentException("LcarsModelProvider only knows about DBObjects")
		}
	}
	
	def createLcarsEntryDescriptor(DBObject it) {
		new LcarsEntryDescriptor(get('_id').toString, get('name').toString, this)
	}
	
	def createLcarsConnectionDescriptor(String fieldName) {
		// TODO replace String by a handle 
		new LcarsConnectionDescriptor(fieldName, this)
	}
}

@ModelNode(#['id', 'name', 'provider'])
class LcarsEntryDescriptor extends CachedDomainObjectDescriptor<DBObject> {
	
	new(String dbId, String name, LcarsModelProvider provider) {
		super(null, dbId, name, provider)
	}
	
	override resolveDomainObject() {
		(provider as LcarsModelProvider).resolveDomainObject(this)
	}
}

@ModelNode(#['id', 'name', 'provider'])
class LcarsConnectionDescriptor extends CachedDomainObjectDescriptor<String> {
	
	new(String fieldName, LcarsModelProvider provider) {
		super(fieldName, fieldName, fieldName, provider)  
	}
	
	override resolveDomainObject() {
		id
	}
}