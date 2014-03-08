package de.fxdiagram.annotations.test

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.Immutable
import de.fxdiagram.annotations.properties.Lazy
import de.fxdiagram.annotations.properties.ReadOnly
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class ActiveAnnotationsTest {
	
	@FxProperty double myDouble

	@FxProperty String myString

	@FxProperty Object myObject = new Object

	@FxProperty ObservableList<Object> myList = FXCollections.observableArrayList

	@FxProperty@ReadOnly double myReadOnlyDouble = 1.0

	@FxProperty@ReadOnly String myReadOnlyString = ""

	@FxProperty@ReadOnly Object myReadOnlyObject = new Object

	@FxProperty@ReadOnly ObservableList<Object> myReadOnlyList = FXCollections.observableArrayList
	
	@FxProperty@Lazy double myLazyDouble = 1.0

	@FxProperty@Lazy String mylazyString = ""

	@FxProperty@Lazy Object myLazyObject = new Object

	@FxProperty@Lazy ObservableList<Object> myLazyList = FXCollections.observableArrayList
	
	@FxProperty@Immutable double myImmutableDouble = 1.0

	@FxProperty@Immutable String myImmutableString = ""

	@FxProperty@Immutable Object myImmutableObject = new Object

	@FxProperty@Immutable ObservableList<Object> myImmutableList = FXCollections.observableArrayList
	
} 

