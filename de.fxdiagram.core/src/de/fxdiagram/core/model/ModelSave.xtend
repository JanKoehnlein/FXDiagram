package de.fxdiagram.core.model

import de.fxdiagram.annotations.logging.Logging
import java.io.Writer
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
import javax.json.stream.JsonGenerator

@Logging
class ModelSave {

	Map<ModelElement, String> idMap

	Model model

	def void save(Object root, Model model, Writer out) {
		this.model = model
		if (model.rootElement != null) {
			idMap = newHashMap
			Json.createGeneratorFactory(#{JsonGenerator.PRETTY_PRINTING -> true})
				.createGenerator(out)
				.write(model.rootElement, null, '')
				.close
		}
	}

	protected def JsonGenerator write(JsonGenerator gen, ModelElement element, String propertyName, String currentId) {
		if(element == null) {
			gen.writeNull(propertyName)
		} else {
			val cachedId = idMap.get(element)
			if (cachedId != null) {
				if(propertyName != null)
					gen.write(propertyName, cachedId)
				else
					gen.write(cachedId)
			} else {
				idMap.put(element, currentId)
				val className = element.node.class.canonicalName
				if(propertyName != null)
					gen.writeStartObject(propertyName)
				else
					gen.writeStartObject()
				gen.write("__class", className)
				element.properties.forEach[write(gen, it, element.getType(it), currentId)]
				element.listProperties.forEach[write(gen, it, element.getType(it), currentId)]
				gen.writeEnd
			}
		}
		return gen
	}

	protected def write(JsonGenerator gen, Property<?> property, Class<?> propertyType, String currentId) {
		if(property.value != null) {
			switch propertyType {
				case String:
					gen.write(property.name, (property as StringProperty).value)
				case Double:
					gen.write(property.name, (property as DoubleProperty).doubleValue)
				case Float:
					gen.write(property.name, (property as FloatProperty).floatValue)
				case Long:
					gen.write(property.name, (property as LongProperty).longValue)
				case Integer:
					gen.write(property.name, (property as IntegerProperty).intValue)
				case Boolean:
					gen.write(property.name, (property as BooleanProperty).value.booleanValue)
				case Enum.isAssignableFrom(propertyType):
					gen.write(property.name, property.value.toString)
				default:
					gen.write(model.index.get(property.value), 
						property.name, 
						currentId + '/' + property.name)
			}
		}
	}

	protected def write(JsonGenerator gen, ListProperty<?> property, Class<?> propertyType, String currentId) {
		gen.writeStartArray(property.name)
		for (i: 0..<property.value.size) {
			val value = property.value.get(i)
			switch propertyType {
				case String:
					gen.write(value as String)
				case Double:
					gen.write((value as Double).doubleValue)
				case Float:
					gen.write((value as Float).floatValue)
				case Long:
					gen.write((value as Long).longValue)
				case Integer:
					gen.write((value as Integer).intValue)
				case Boolean:
					gen.write((value as Boolean).booleanValue)
				case Enum.isAssignableFrom(propertyType):
					gen.write(value.toString)
				default:
					gen.write(model.index.get(value), 
						null, 
						currentId + '/' + property.name + '.' + i)
			}
		}
		gen.writeEnd()
	}
}

