package de.fxdiagram.core.model

import javafx.beans.value.ChangeListener
import javafx.collections.ListChangeListener

class ModelSync {

	Model model

	ChangeListener<Object> childrenListener = [ property, oldValue, newValue |
		if (oldValue != null)
			model.removeElement(oldValue)
		if (newValue != null)
			model.addElement(newValue)
	]

	ListChangeListener<Object> childrenListListener = [ change |
		while (change.next) {
			if (change.wasAdded)
				change.addedSubList.forEach[model.addElement(it)]
			if (change.wasRemoved)
				change.removed.forEach[model.removeElement(it)]
		}
	]

	new(Model model) {
		this.model = model
	}

	protected def void addElement(ModelElement element) {
		element.children.forEach[addListener(childrenListener)]
		element.listChildren.forEach[addListener(childrenListListener)]
	}

	protected def void removeElement(ModelElement element) {
		element.children.forEach[removeListener(childrenListener)]
		element.listChildren.forEach[removeListener(childrenListListener)]
	}
}
