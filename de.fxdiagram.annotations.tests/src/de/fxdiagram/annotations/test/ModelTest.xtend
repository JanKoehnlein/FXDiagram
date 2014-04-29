package de.fxdiagram.annotations.test

import de.fxdiagram.annotations.properties.FxProperty
import de.fxdiagram.annotations.properties.ModelNode
import de.fxdiagram.core.XNode
import de.fxdiagram.core.model.DomainObjectDescriptor
import javafx.beans.property.BooleanProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ListProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.StringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.shape.Polygon

@ModelNode(#['layoutX', 'string', 'integer', 'long', 'bool', 'unprecise', 'names', 'selfRef']) 
abstract class ModelTest extends Polygon {
	IntegerProperty integer
	LongProperty longProperty
	BooleanProperty bool
	FloatProperty unprecise 
	ObjectProperty<String> stringProperty	
	StringProperty string1
	ObjectProperty<ModelTest> selfRef
	ListProperty<String> names	
}

@ModelNode(#['objects']) 
class Modeltest2 {
	@FxProperty ObservableList<DomainObjectDescriptor> objects = FXCollections.observableArrayList
} 

@ModelNode(inherit=false) 
class Modeltest3 extends XNode {
}  