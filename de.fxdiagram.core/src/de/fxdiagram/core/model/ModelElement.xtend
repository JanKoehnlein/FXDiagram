package de.fxdiagram.core.model

import java.util.List
import java.util.Map
import javafx.beans.property.ListProperty
import javafx.beans.property.Property

interface ModelElement {
	
	def List<? extends Property<?>> getProperties()

	def List<? extends ListProperty<?>> getListProperties()
	
	def Class<?> getType(Property<?> property)

	def boolean isPrimitive(Property<?> property)
	
	def Object getNode()
	
}

class ModelElementImpl implements ModelElement {

	Object node

	List<Property<?>> properties

	List<ListProperty<?>> listProperties

	Map<String, Class<?>> propertyTypes = newHashMap

	new(Object node) {
		this.node = node
	}
	
	def addProperty(Property<?> property, Class<?> propertyType) {
		// TODO handle null
		if (property != null) {
			if (properties == null)
				properties = newArrayList
			properties += property
			propertyTypes.put(property.name, propertyType)
		}
	}

	def addProperty(ListProperty<?> listProperty, Class<?> componentType) {
		if (listProperties == null)
			listProperties = newArrayList
		listProperties += listProperty
		propertyTypes.put(listProperty.name, componentType)
	}

	override getProperties() {
		properties ?: emptyList
	}

	override getListProperties() {
		listProperties ?: emptyList
	}
	
	override Class<?> getType(Property<?> property) {
		propertyTypes.get(property.name)
	}

	override isPrimitive(Property<?> property) {
		val type = property.type
		return #{Double, Float, Integer, Long, Boolean, String}.contains(type)
			|| Enum.isAssignableFrom(type) 	
	}
	
	override getNode() {
		node
	}
}
