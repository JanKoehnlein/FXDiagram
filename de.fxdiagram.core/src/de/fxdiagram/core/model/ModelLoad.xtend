package de.fxdiagram.core.model

import de.fxdiagram.annotations.logging.Logging
import java.io.Reader
import java.util.List
import java.util.Map
import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ListProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.Property
import javafx.beans.property.StringProperty
import javax.json.Json
import javax.json.JsonNumber
import javax.json.JsonObject
import javax.json.JsonString
import javax.json.JsonValue

import static javax.json.JsonValue.ValueType.*
import org.eclipse.xtend.lib.annotations.Data

@Logging
class ModelLoad {

	ModelFactory modelFactory

	Map<String, ModelElement> idMap
	 
	List<CrossRefData> crossRefs
	
	def Object load(Reader in) {
		modelFactory = new ModelFactory
		crossRefs = newArrayList
		idMap = newHashMap
		val reader = Json.createReader(in)
		val jsonObject = reader.readObject
		val node = readNode(jsonObject, '')
		crossRefs.forEach[resolveCrossReference]
		return node
	}
	
	protected def Object readNode(JsonObject jsonObject, String currentID) {
		val className = jsonObject.getString('__class')
		val model = modelFactory.createElement(className)
		idMap.put(currentID, model)
		model.properties.forEach [
			jsonObject.readProperty(it, model.getType(it), currentID)
		]		
		model.listProperties.forEach [
			jsonObject.readListProperty(it, model.getType(it), currentID)
		]		
		model.node
	}

	protected def readProperty(JsonObject it, Property<?> property, Class<?> propertyType, String currentID) {
		if(containsKey(property.name)) {
			switch propertyType {
				case String: 
					(property as StringProperty).value = getString(property.name)
				case Double: 
					(property as DoubleProperty).value = getJsonNumber(property.name).doubleValue
				case Float: 
					(property as FloatProperty).value = getJsonNumber(property.name).doubleValue
				case Long: 
					(property as LongProperty).value = getJsonNumber(property.name).longValue
				case Integer: 
					(property as IntegerProperty).value = getInt(property.name)
				case Boolean: 
					(property as BooleanProperty).value = getBoolean(property.name)
				case Enum.isAssignableFrom(propertyType): {
					(property as Property<Object>).value = Enum.valueOf(propertyType as Class<Enum>, getString(property.name))
				}
				default: {
					val value = get(property.name)
					switch value.valueType {
						case STRING: {
							val crossRefData = new CrossRefData(getString(property.name), property,  -1)
							crossRefs.add(crossRefData)
						}
						case OBJECT:
							(property as Property<Object>).value = (value as JsonObject).readNode(currentID + "/" + property.name)
						default:
							throw new ParseException("Expected object but got " + value)
					}
				}
			}
		}
	}

	protected def readListProperty(JsonObject it, ListProperty<?> property, Class<?> componentType, String currentID) {
		if(containsKey(property.name)) {
			val jsonValues = getJsonArray(property.name)
			for(i: 0..<jsonValues.size) {
				val jsonValue = jsonValues.get(i)
				switch componentType {
					case String: 
						(property as List<String>) += (jsonValue as JsonString).toString
					case Double: 
						(property as List<Double>) += (jsonValue as JsonNumber).doubleValue
					case Float: 
						(property as List<Float>) += (jsonValue as JsonNumber).doubleValue as float
					case Long: 
						(property as List<Long>) += (jsonValue as JsonNumber).longValue
					case Integer: 
						(property as List<Integer>) += (jsonValue as JsonNumber).intValue
					case Boolean: 
						(property as List<Boolean>) += jsonValue == JsonValue.TRUE
					case Enum.isAssignableFrom(componentType): {
						(property as ListProperty<Object>) += Enum.valueOf(componentType as Class<Enum>, (jsonValue as JsonString).toString)
					}
					default: {
						switch jsonValue.valueType {
							case STRING: {
								val crossRefData = new CrossRefData((jsonValue as JsonString).string, property, i)
								crossRefs.add(crossRefData)
							}
							case OBJECT:
								(property as List<Object>) += (jsonValue as JsonObject).readNode(currentID + '/' + property.name + '.' + i)
							default:
								throw new ParseException("Expected object but got " + jsonValue)
						}
					}
				}
			}
		}
	}
	
	protected def resolveCrossReference(CrossRefData crossRef) {
		val crossRefTarget = idMap.get(crossRef.href)?.node
		if(crossRefTarget == null)
			throw new ParseException("Cannot resolve href '" + crossRef.href +"'")
		else if(crossRef.index == -1)
			(crossRef.property as Property<Object>).value = crossRefTarget
		else 
			(crossRef.property as ListProperty<Object>).add(crossRef.index, crossRefTarget)
	}
}

@Data 
class CrossRefData {
	String href
	Property<?> property
	int index
}

class ParseException extends Exception {

	new(String message) {
		super(message)
	}

}
