package de.fxdiagram.annotations.test

import de.fxdiagram.annotations.properties.FxProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class ActiveAnnotationsTest {
	
	@FxProperty double myDouble

	@FxProperty String myString

	@FxProperty Object myObject = new Object

	@FxProperty ObservableList<Object> myList = FXCollections.observableArrayList

	@FxProperty(readOnly=true) double myReadOnlyDouble = 1.0

	@FxProperty(readOnly) String myReadOnlyString = ""

	@FxProperty(readOnly) Object myReadOnlyObject = new Object

	@FxProperty(readOnly) ObservableList<Object> myReadOnlyList = FXCollections.observableArrayList
} 

 