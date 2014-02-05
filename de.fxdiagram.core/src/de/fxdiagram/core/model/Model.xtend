package de.fxdiagram.core.model

import java.util.IdentityHashMap
import java.util.List
import java.util.Map
import javafx.beans.property.ListProperty
import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener

class Model {

	Map<Object, ModelElement> index = new IdentityHashMap

	ChangeListener<Object> changeListener = [ property, oldValue, newValue |
		notifyListeners(property, oldValue, newValue)
	]

	ListChangeListener<Object> listChangeListener = [ change |
		notifyListListeners(change)
	]

	List<ModelChangeListener> modelChangeListeners = newArrayList

	ModelElement rootElement

	ModelFactory modelFactory

	ModelSync modelSync

	new(Object rootNode) {
		modelFactory = new ModelFactory
		modelSync = new ModelSync(this)
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
		val element = modelFactory.createElement(node)
		index.put(node, element)
		modelSync.addElement(element)
		element.children.forEach [
			value?.addElement
			addListener(changeListener)
		]
		element.listChildren.forEach[
			value.forEach[it?.addElement]
			addListener(listChangeListener)
		]
		element.properties.forEach[it?.addListener(changeListener)]
		element.listProperties.forEach[it?.addListener(listChangeListener)]
		element
	}

	package def removeElement(Object node) {
		val element = index.remove(node)
		modelSync.removeElement(element)
		(element.children + element.properties).forEach[removeListener(changeListener)]
		(element.listChildren + element.listProperties).forEach[removeListener(listChangeListener)]
		element
	}

	def addModelChangeListener(ModelChangeListener modelChangeListener) {
		modelChangeListeners += modelChangeListener
	}

	def removeModelChangeListener(ModelChangeListener modelChangeListener) {
		modelChangeListeners -= modelChangeListener
	}

	protected def notifyListListeners(ListChangeListener.Change<?> change) {
		val list = change.list
		switch (list) {
			ListProperty<?>: modelChangeListeners.forEach[listPropertyChanged(list, change)]
		}
	}

	protected def notifyListeners(ObservableValue<?> property, Object oldValue, Object newValue) {
		switch property {
			Property<?>:
				modelChangeListeners.forEach[propertyChanged(property, oldValue, newValue)]
		}
	}
}
