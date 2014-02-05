package de.fxdiagram.core.model

import java.util.List
import java.util.Map
import javafx.beans.property.ListProperty
import javafx.beans.property.Property

class ModelElement {

	Object node

	List<Property<?>> properties

	List<ListProperty<?>> listProperties

	List<Property<?>> children

	List<ListProperty<?>> listChildren

	Map<String, Class<?>> propertyTypes = newHashMap

	new(Object node) {
		this.node = node
	}

	def <T> addProperty(Property<T> property, Class<? extends T> propertyType) {
		// TODO handle null
		if (property != null) {
			if (properties == null)
				properties = newArrayList
			properties += property
			propertyTypes.put(property.name, propertyType)
		}
	}

	def <T> addProperty(ListProperty<T> listProperty, Class<? extends T> componentType) {
		if (listProperties == null)
			listProperties = newArrayList
		listProperties += listProperty
		propertyTypes.put(listProperty.name, componentType)
	}

	def <T> addChildProperty(Property<T> child, Class<? extends T> propertyType) {
		// TODO handle null
		if (child != null) {
			if (children == null)
				children = newArrayList
			children += child
			propertyTypes.put(child.name, propertyType)
		}
	}

	def <T> addChildProperty(ListProperty<T> listChild, Class<? extends T> componentType) {
		if (listChildren == null)
			listChildren = newArrayList
		listChildren += listChild
		propertyTypes.put(listChild.name, componentType)
	}

	def getChildren() {
		children ?: emptyList
	}

	def getListChildren() {
		listChildren ?: emptyList
	}

	def getProperties() {
		properties ?: emptyList
	}

	def getListProperties() {
		listProperties ?: emptyList
	}
	
	def Class<?> getType(Property<?> property) {
		propertyTypes.get(property.name)
	}

	def getNode() {
		node
	}
}
