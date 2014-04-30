package de.fxdiagram.core.model

import java.util.IdentityHashMap
import java.util.Map

class Model {

	Map<Object, ModelElement> index = new IdentityHashMap

	ModelElement rootElement

	ModelFactory modelFactory

	new(Object rootNode) {
		modelFactory = new ModelFactory
		rootElement = addElement(rootNode)
	}

	package def getIndex() {
		index
	}

	def getRootElement() {
		rootElement
	}

	def getModelElement(XModelProvider node) {
		index.get(node)
	}

	package def ModelElement addElement(Object node) {
		val existingElement = index.get(node)
		if(existingElement != null) 
			return existingElement
		val element = existingElement ?: modelFactory.createElement(node)
		index.put(node, element)
		element.properties.forEach [
			if(!element.isPrimitive(it))
				value?.addElement
		]
		element.listProperties.forEach [
			if(!element.isPrimitive(it))
				value.forEach[it?.addElement]
		]
		element
	}
}
