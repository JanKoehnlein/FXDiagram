package de.fxdiagram.core.annotations.tests

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.Immutable
import de.fxdiagram.annotations.properties.Lazy
import de.fxdiagram.annotations.properties.ReadOnly
import javafx.collections.FXCollections

class ActiveAnnotationsTest {
	
	@FxProperty var myDouble = 1.0

	@FxProperty var myString = ""

	@FxProperty var myObject = new Object

	@FxProperty var myList = FXCollections.observableArrayList

	@FxProperty@ReadOnly val myReadOnlyDouble = 1.0

	@FxProperty@ReadOnly val myReadOnlyString = ""

	@FxProperty@ReadOnly val myReadOnlyObject = new Object

	@FxProperty@ReadOnly val myReadOnlyList = FXCollections.observableArrayList
	
	@FxProperty@Lazy val myEagerDouble = 1.0

	@FxProperty@Lazy val myEagerString = ""

	@FxProperty@Lazy var myEagerObject = new Object

	@FxProperty@Lazy val myEagerList = FXCollections.observableArrayList
	
	@FxProperty@Immutable val myImmutableDouble = 1.0

	@FxProperty@Immutable val myImmutableString = ""

	@FxProperty@Immutable val myImmutableObject = new Object

	@FxProperty@Immutable val myImmutableList = FXCollections.observableArrayList
	
} 

