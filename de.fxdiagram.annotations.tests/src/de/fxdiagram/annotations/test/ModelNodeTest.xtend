package de.fxdiagram.annotations.test

import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.junit.Test

class ModelNodeTest {
	@Test
	def void testLeaveImplicitConstructor() {
		assertCompilesTo('''
			import de.fxdiagram.annotations.properties.ModelNode
			
			@ModelNode
			class Foo {
			}
		''', '''
			import de.fxdiagram.annotations.properties.ModelNode;
			import de.fxdiagram.core.model.ModelElementImpl;
			import de.fxdiagram.core.model.XModelProvider;
			
			@ModelNode
			@SuppressWarnings("all")
			public class Foo implements XModelProvider {
			  public void populate(final ModelElementImpl modelElement) {
			    
			  }
			}
		''')
	}

	@Test
	def void testCreateNoArgConstructor() {
		assertCompilesTo('''
			import de.fxdiagram.annotations.properties.ModelNode
			
			@ModelNode
			class Foo {
				new(String name) {}
			}
		''', '''
			import de.fxdiagram.annotations.properties.ModelNode;
			import de.fxdiagram.core.model.ModelElementImpl;
			import de.fxdiagram.core.model.XModelProvider;
			
			@ModelNode
			@SuppressWarnings("all")
			public class Foo implements XModelProvider {
			  public Foo(final String name) {
			  }
			  
			  /**
			   * Automatically generated by @ModelNode. Needed for deserialization.
			   */
			  public Foo() {
			  }
			  
			  public void populate(final ModelElementImpl modelElement) {
			    
			  }
			}
		''')
	}
	
	@Test 
	def void testProperties() {
		assertCompilesTo('''
			import de.fxdiagram.annotations.properties.ModelNode
			import javafx.beans.property.BooleanProperty
			import javafx.beans.property.FloatProperty
			import javafx.beans.property.IntegerProperty
			import javafx.beans.property.LongProperty
			import javafx.beans.property.ObjectProperty
			import javafx.beans.property.StringProperty
			import javafx.scene.shape.Polygon
			import javafx.beans.property.ListProperty
			
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
		''', '''
			import de.fxdiagram.annotations.properties.ModelNode;
			import de.fxdiagram.core.model.ModelElementImpl;
			import de.fxdiagram.core.model.XModelProvider;
			import javafx.beans.property.BooleanProperty;
			import javafx.beans.property.FloatProperty;
			import javafx.beans.property.IntegerProperty;
			import javafx.beans.property.ListProperty;
			import javafx.beans.property.LongProperty;
			import javafx.beans.property.ObjectProperty;
			import javafx.beans.property.StringProperty;
			import javafx.scene.shape.Polygon;
			
			@ModelNode({ "layoutX", "string", "integer", "long", "bool", "unprecise", "names", "selfRef" })
			@SuppressWarnings("all")
			public abstract class ModelTest extends Polygon implements XModelProvider {
			  private IntegerProperty integer;
			  
			  private LongProperty longProperty;
			  
			  private BooleanProperty bool;
			  
			  private FloatProperty unprecise;
			  
			  private ObjectProperty<String> stringProperty;
			  
			  private StringProperty string1;
			  
			  private ObjectProperty<ModelTest> selfRef;
			  
			  private ListProperty<String> names;
			  
			  public void populate(final ModelElementImpl modelElement) {
			    modelElement.addProperty(layoutXProperty(), Double.class);
			    modelElement.addProperty(stringProperty, String.class);
			    modelElement.addProperty(integer, Integer.class);
			    modelElement.addProperty(longProperty, Long.class);
			    modelElement.addProperty(bool, Boolean.class);
			    modelElement.addProperty(unprecise, Float.class);
			    modelElement.addProperty(names, String.class);
			    modelElement.addProperty(selfRef, ModelTest.class);
			  }
			}
		''')
	}
	
	@Test
	def void testFxProperty() {
		assertCompilesTo('''
			import de.fxdiagram.annotations.properties.FxProperty
			import de.fxdiagram.annotations.properties.ModelNode
			import javafx.collections.FXCollections
			import javafx.collections.ObservableList
			
			@ModelNode(#['myDouble', 'myString', 'myObject', 'myList'])
			class ActiveAnnotationsTest {
				@FxProperty double myDouble
				@FxProperty String myString
				@FxProperty Object myObject = new Object
				@FxProperty ObservableList<String> myList = FXCollections.observableArrayList
			}
		''', '''
			import de.fxdiagram.annotations.properties.ModelNode;
			import de.fxdiagram.core.model.ModelElementImpl;
			import de.fxdiagram.core.model.XModelProvider;
			import javafx.beans.property.DoubleProperty;
			import javafx.beans.property.ListProperty;
			import javafx.beans.property.ObjectProperty;
			import javafx.beans.property.SimpleDoubleProperty;
			import javafx.beans.property.SimpleListProperty;
			import javafx.beans.property.SimpleObjectProperty;
			import javafx.beans.property.SimpleStringProperty;
			import javafx.beans.property.StringProperty;
			import javafx.collections.FXCollections;
			import javafx.collections.ObservableList;
			
			@ModelNode({ "myDouble", "myString", "myObject", "myList" })
			@SuppressWarnings("all")
			public class ActiveAnnotationsTest implements XModelProvider {
			  public void populate(final ModelElementImpl modelElement) {
			    modelElement.addProperty(myDoubleProperty, Double.class);
			    modelElement.addProperty(myStringProperty, String.class);
			    modelElement.addProperty(myObjectProperty, Object.class);
			    modelElement.addProperty(myListProperty, String.class);
			  }
			  
			  private SimpleDoubleProperty myDoubleProperty = new SimpleDoubleProperty(this, "myDouble");
			  
			  public double getMyDouble() {
			    return this.myDoubleProperty.get();
			  }
			  
			  public void setMyDouble(final double myDouble) {
			    this.myDoubleProperty.set(myDouble);
			  }
			  
			  public DoubleProperty myDoubleProperty() {
			    return this.myDoubleProperty;
			  }
			  
			  private SimpleStringProperty myStringProperty = new SimpleStringProperty(this, "myString");
			  
			  public String getMyString() {
			    return this.myStringProperty.get();
			  }
			  
			  public void setMyString(final String myString) {
			    this.myStringProperty.set(myString);
			  }
			  
			  public StringProperty myStringProperty() {
			    return this.myStringProperty;
			  }
			  
			  private SimpleObjectProperty<Object> myObjectProperty = new SimpleObjectProperty<Object>(this, "myObject",_initMyObject());
			  
			  private static final Object _initMyObject() {
			    Object _object = new Object();
			    return _object;
			  }
			  
			  public Object getMyObject() {
			    return this.myObjectProperty.get();
			  }
			  
			  public void setMyObject(final Object myObject) {
			    this.myObjectProperty.set(myObject);
			  }
			  
			  public ObjectProperty<Object> myObjectProperty() {
			    return this.myObjectProperty;
			  }
			  
			  private SimpleListProperty<String> myListProperty = new SimpleListProperty<String>(this, "myList",_initMyList());
			  
			  private static final ObservableList<String> _initMyList() {
			    ObservableList<String> _observableArrayList = FXCollections.<String>observableArrayList();
			    return _observableArrayList;
			  }
			  
			  public ObservableList<String> getMyList() {
			    return this.myListProperty.get();
			  }
			  
			  public ListProperty<String> myListProperty() {
			    return this.myListProperty;
			  }
			}
		''')
	}
	
	@Test
	def void testInherit() {
		assertCompilesTo('''
			import de.fxdiagram.annotations.properties.ModelNode
			import javafx.beans.property.BooleanProperty
			
			@ModelNode(#['bool'])
			class Foo {
				BooleanProperty bool
			}

			@ModelNode
			class Bar extends Foo {
			}
		''', '''
			MULTIPLE FILES WERE GENERATED

			File 1 : Bar.java
			
			import de.fxdiagram.annotations.properties.ModelNode;
			import de.fxdiagram.core.model.ModelElementImpl;
			
			@ModelNode
			@SuppressWarnings("all")
			public class Bar extends Foo {
			  public void populate(final ModelElementImpl modelElement) {
			    super.populate(modelElement);
			  }
			}
			
			File 2 : Foo.java
			
			import de.fxdiagram.annotations.properties.ModelNode;
			import de.fxdiagram.core.model.ModelElementImpl;
			import de.fxdiagram.core.model.XModelProvider;
			import javafx.beans.property.BooleanProperty;
			
			@ModelNode({ "bool" })
			@SuppressWarnings("all")
			public class Foo implements XModelProvider {
			  private BooleanProperty bool;
			  
			  public void populate(final ModelElementImpl modelElement) {
			    modelElement.addProperty(bool, Boolean.class);
			  }
			}
			
		''')
	}
	
	@Test
	def void testNoInherit() {
		assertCompilesTo('''
			import de.fxdiagram.annotations.properties.ModelNode
			import javafx.beans.property.BooleanProperty
			
			@ModelNode(#['bool'])
			class Foo {
				BooleanProperty bool
			}

			@ModelNode(inherit=false)
			class Bar extends Foo {
			}
		''', '''
			MULTIPLE FILES WERE GENERATED

			File 1 : Bar.java
			
			import de.fxdiagram.annotations.properties.ModelNode;
			import de.fxdiagram.core.model.ModelElementImpl;
			
			@ModelNode(inherit = false)
			@SuppressWarnings("all")
			public class Bar extends Foo {
			  public void populate(final ModelElementImpl modelElement) {
			    
			  }
			}
			
			File 2 : Foo.java
			
			import de.fxdiagram.annotations.properties.ModelNode;
			import de.fxdiagram.core.model.ModelElementImpl;
			import de.fxdiagram.core.model.XModelProvider;
			import javafx.beans.property.BooleanProperty;
			
			@ModelNode({ "bool" })
			@SuppressWarnings("all")
			public class Foo implements XModelProvider {
			  private BooleanProperty bool;
			  
			  public void populate(final ModelElementImpl modelElement) {
			    modelElement.addProperty(bool, Boolean.class);
			  }
			}
			
		''')
	}
	
	protected def assertCompilesTo(CharSequence source, CharSequence target) {
		val compilerTester = XtendCompilerTester.newXtendCompilerTester(class)
		compilerTester.assertCompilesTo(source, target)
	}
}