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
import javafx.scene.Node
import javax.json.Json
import javax.json.stream.JsonParser

import static javax.json.stream.JsonParser.Event.*

@Logging
class ModelLoad {

	ModelFactory modelFactory

	Map<String, Object> idMap
	 
	List<CrossRefData> crossRefs

	def Object load(Reader in) {
		modelFactory = new ModelFactory
		crossRefs = newArrayList
		idMap = newHashMap
		val parser = Json.createParser(in)
		if (parser.hasNext) {
			parser.consume(START_OBJECT)
			val node = parseNode(parser, '')
			for(crossRef: crossRefs) {
				val crossRefTarget = idMap.get(crossRef.href)
				if(crossRefTarget == null)
					LOG.severe("Cannot resolve href '" + crossRef.href +"'")
				else if(crossRef.index == -1)
					(crossRef.property as Property<Object>).value = crossRefTarget
				else
					(crossRef.property as ListProperty<Object>).set(crossRef.index, crossRefTarget) 	
			}
			return node
		}
		return null
	}

	protected def Object parseNode(JsonParser it, String currentID) {
		consume(KEY_NAME)
		if (string != "class")
			throw new ParseException("Error parsing JSON file")
		consume(VALUE_STRING)
		val clazz = Class.forName(string)
		val node = clazz.newInstance
		idMap.put(currentID, node)
		val model = modelFactory.createElement(node)
		while(hasNext) {
			val event = next
			switch event {
				case KEY_NAME:
					parseProperty(node, model, string, currentID)
				case END_OBJECT:
					return node
				default:
					throw new ParseException("Invalid token " + event.toString + " at " + location)
			}
		}
		return node
	}

	protected def parseProperty(JsonParser it, Object node, ModelElement model, String propertyName, String currentID) {
		val property = (model.properties + model.children).findFirst[name == propertyName]
		if (property != null) {
			parseValue(node, property, model.getType(property), currentID)
		} else {
			val listProperty = (model.listProperties + model.listChildren).findFirst[name == propertyName]
			if (listProperty == null)
				throw new ParseException(
					"Cannot find property '" + propertyName + "' in class " + node.class.name + " at " + location)
			else
				parseListValue(node, listProperty, model.getType(listProperty), currentID)
		}
	}

	def parseListValue(JsonParser it, Object node, ListProperty<?> property, Class<?> componentType, String currentID) {
		consume(START_ARRAY)
		var index = 0
		while (hasNext) {
			val event = next
			switch componentType {
				case String: {
					consume(VALUE_STRING)
					(property as List<String>) += string
				}
				case Double: {
					consume(VALUE_NUMBER)
					(property as List<Double>) += bigDecimal.doubleValue
				}
				case Float: {
					consume(VALUE_NUMBER)
					(property as List<Float>) += bigDecimal.floatValue
				}
				case Long: {
					consume(VALUE_NUMBER)
					(property as List<Long>) += ^long
				}
				case Integer: {
					consume(VALUE_NUMBER)
					(property as List<Integer>) += ^int
				}
				case Boolean: {
					(property as List<Boolean>) += switch event {
						case VALUE_TRUE:
							true
						case VALUE_FALSE:
							false
						default:
							throw new ParseException("Expected boolean value but got " + event.name + " " + location)
					}
				}
				case Enum.isAssignableFrom(componentType): {
					switch event {
						case VALUE_NULL: {
						}
						case VALUE_STRING: {
							(property as List<Object>) += Enum.valueOf(componentType as Class<? extends Enum>, string)
						}
						default:
							throw new ParseException("Expected enum value but got " + event + " at " + location)
					}
				}
				default: {
					switch event {
						case VALUE_NULL: {
							(property as List<Node>) += null
						}
						case VALUE_STRING:
							crossRefs.add(new CrossRefData(string, property, index))
						case START_OBJECT:
							(property as List<Object>) += parseNode(currentID + '/' + property.name + '.' + index)
						case END_ARRAY: 
							return
						default:
							throw new ParseException("Expected object but got " + event + " at " + location)
					}
				}
			}
			index = index + 1
		}
		return
	}

	def void parseValue(JsonParser it, Object node, Property<?> property, Class<?> propertyType, String currentID) {
		switch propertyType {
			case String: {
				consume(VALUE_STRING)
				(property as StringProperty).value = string
			}
			case Double: {
				consume(VALUE_NUMBER)
				(property as DoubleProperty).value = bigDecimal.doubleValue
			}
			case Float: {
				consume(VALUE_NUMBER)
				(property as FloatProperty).value = bigDecimal.floatValue
			}
			case Long: {
				consume(VALUE_NUMBER)
				(property as LongProperty).value = ^long
			}
			case Integer: {
				consume(VALUE_NUMBER)
				(property as IntegerProperty).value = ^int
			}
			case Boolean: {
				(property as BooleanProperty).value = switch event: next {
					case VALUE_TRUE:
						true
					case VALUE_FALSE:
						false
					default:
						throw new ParseException("Expected boolean value but got " + event.name + " " + location)
				}
			}
			case Enum.isAssignableFrom(propertyType): {
				switch event: next {
					case VALUE_NULL: {
					}
					case VALUE_STRING: {
						(property as Property<Object>).value = Enum.valueOf(propertyType as Class<? extends Enum>,
							string)
					}
					default:
						throw new ParseException("Expected enum value but got " + event + " at " + location)
				}
			}
			default: {
				switch event: next {
					case VALUE_NULL: {
					}
					case VALUE_STRING:
						crossRefs.add(new CrossRefData(string, property,  -1))
					case START_OBJECT:
						(property as Property<Object>).value = parseNode(currentID + '/' + property.name)
					default:
						throw new ParseException("Expected object but got " + event + " at " + location)
				}
			}
		}
	}

	protected def consume(JsonParser parser, JsonParser.Event event) {
		val next = parser.next()
		if (next != event)
			throw new ParseException("Expected " + event + " but got " + next + " at " + parser.location)
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
