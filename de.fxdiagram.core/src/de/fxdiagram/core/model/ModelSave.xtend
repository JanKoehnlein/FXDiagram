package de.fxdiagram.core.model

import de.fxdiagram.annotations.logging.Logging
import java.io.Writer
import java.util.List
import java.util.Map
import java.util.Set
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

import static extension de.fxdiagram.core.model.ModelPersistence.*

@Logging
class ModelSave {

	Map<ModelElement, String> idMap

	Set<ModelElement> alreadySerialized

	Model model

	def void save(Object root, Writer out) {
		model = new Model(root)
		if (model.rootElement != null) {
			idMap = newHashMap
			createIDs(model.rootElement, "")
			alreadySerialized = newHashSet
			Json.createGeneratorFactory(#{JsonGenerator.PRETTY_PRINTING -> true})
				.createGenerator(out)
				.writeStartObject
				.write(model.rootElement)
				.writeEnd
				.close
		}
	}

	protected def void createIDs(ModelElement it, String id) {
		if (it != null) {
			idMap.put(it, id)
			for (property : children) {
				if (property.value != null)
					createIDs(model.index.get(property.value), id + "/" + property.name)
			}
			for (property : listChildren) {
				var i = 0
				for (value : property.value) {
					if (value != null)
						createIDs(model.index.get(value), id + "/" + property.name + "." + i)
					i = i + 1
				}
			}
		}
	}

	protected def JsonGenerator write(JsonGenerator gen, ModelElement element) {
		if (!alreadySerialized.add(element)) {
			gen.write(idMap.get(element))
		} else {
			gen.writeStartObject('__constructor')
			val className = element.node.class.canonicalName
			gen.write("__class", className)
			val paramTypes = element.constructorProperties.map[element.getType(it)]
			val constructor = element.node.class.findConstructor(paramTypes)
			var List<Property<?>> remainingChildren
			if(constructor != null) {
				remainingChildren = #[]
				if(!element.constructorProperties.empty) {
					gen.writeStartArray('__params')
					element.constructorProperties.forEach [
						gen .writeStartObject()
							.write(model.index.get(value))
							.writeEnd
					]
					gen.writeEnd
				}
			} else {
				LOG.warning('Cannot find constructor ' + className + '(' + paramTypes.map[simpleName].join(',') + '). Using no-arg constructor instead.')
				remainingChildren = element.constructorProperties
			}
			gen.writeEnd;
			element.listChildren.forEach [
				if(!empty) {
					gen.writeStartArray(name)
					value.forEach [
						if (it == null) {
							gen.writeNull
						} else {
							gen
								.writeStartObject
								.write(model.index.get(it))
								.writeEnd
						}
					]
					gen.writeEnd
				}
			]
			(remainingChildren + element.children).forEach [
				if (value != null) {
					gen
						.writeStartObject(name)
						.write(model.index.get(value))
						.writeEnd
				}
			]
			element.properties.forEach[write(gen, it, element.getType(it))]
			element.listProperties.forEach[write(gen, it, element.getType(it))]
		}
		return gen
	}

	protected def write(JsonGenerator gen, Property<?> property, Class<?> propertyType) {
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
					gen.write(property.name, idMap.get(model.index.get(property.value)))
			}
		}
	}

	protected def write(JsonGenerator gen, ListProperty<?> property, Class<?> propertyType) {
		gen.writeStartArray(property.name)
		for (value : property.value) {
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
					gen.write(idMap.get(model.index.get(property.value)))
			}
		}
		gen.writeEnd()
	}
}

