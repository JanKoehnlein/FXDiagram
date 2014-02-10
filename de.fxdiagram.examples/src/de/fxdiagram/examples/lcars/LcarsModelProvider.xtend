package de.fxdiagram.examples.lcars

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.DBCollection
import com.mongodb.DBObject
import com.mongodb.Mongo
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.model.DomainObjectHandle
import de.fxdiagram.core.model.DomainObjectHandleImpl
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
	
	override resolveDomainObject(DomainObjectHandle handle) {
		lcars.findOne(new BasicDBObject => [
			put("_id", new ObjectId(handle.id))
		])
	}
	
	override createDomainObjectHandle(Object it) {
		switch it {
			DBObject: return createLcarsEntryHandle
			String: return createLcarsConnectionHandle
			default:
				throw new IllegalArgumentException("LcarsModelProvider only knows about DBObjects")
		}
	}
	
	def createLcarsEntryHandle(DBObject it) {
		new LcarsEntryHandle(get('_id').toString, get('name').toString, this)
	}
	
	def createLcarsConnectionHandle(String fieldName) {
		// TODO replace String by an ID 
		new LcarsConnectionHandle(fieldName, this)
	}

}

@ModelNode(#['id', 'key', 'provider'])
class LcarsEntryHandle extends DomainObjectHandleImpl {
	
	new(String dbId, String name, LcarsModelProvider provider) {
		super(dbId, name, provider)
	}
	
	override getDomainObject() {
		super.domainObject as DBObject
	}
}

@ModelNode(#['id', 'key', 'provider'])
class LcarsConnectionHandle extends DomainObjectHandleImpl {
	
	new(String fieldName, LcarsModelProvider provider) {
		super(fieldName, fieldName, provider)  
	}
}