package de.fxdiagram.core.model

import javafx.beans.property.ListProperty
import javafx.beans.property.Property
import javafx.collections.ListChangeListener

interface ModelChangeListener {
	
	def void propertyChanged(Property<?> property, Object oldVal, Object newVal)
	
	def void listPropertyChanged(ListProperty<?> property, ListChangeListener.Change<?> change)
}

