package de.fxdiagram.core.annotations;

import de.fxdiagram.annotations.properties.FxProperty;
import de.fxdiagram.annotations.properties.Lazy;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class ActiveAnnotationsTest {
  @FxProperty
  @Lazy
  private Object myEagerObject = new Function0<Object>() {
    public Object apply() {
      Object _object = new Object();
      return _object;
    }
  }.apply();
  
  @FxProperty
  @Lazy
  private final ObservableList<Object> myEagerList = new Function0<ObservableList<Object>>() {
    public ObservableList<Object> apply() {
      ObservableList<Object> _observableArrayList = FXCollections.<Object>observableArrayList();
      return _observableArrayList;
    }
  }.apply();
  
  private SimpleDoubleProperty myDoubleProperty = new SimpleDoubleProperty(this, "myDouble",_initMyDouble());
  
  private static final double _initMyDouble() {
    return 1.0;
  }
  
  public double getMyDouble() {
    return this.myDoubleProperty.get();
    
  }
  
  public void setMyDouble(final double myDouble) {
    this.myDoubleProperty.set(myDouble);
    
  }
  
  public DoubleProperty myDoubleProperty() {
    return this.myDoubleProperty;
    
  }
  
  private SimpleStringProperty myStringProperty = new SimpleStringProperty(this, "myString",_initMyString());
  
  private static final String _initMyString() {
    return "";
  }
  
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
  
  private SimpleListProperty<Object> myListProperty = new SimpleListProperty<Object>(this, "myList",_initMyList());
  
  private static final ObservableList<Object> _initMyList() {
    ObservableList<Object> _observableArrayList = FXCollections.<Object>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<Object> getMyList() {
    return this.myListProperty.get();
    
  }
  
  public ListProperty<Object> myListProperty() {
    return this.myListProperty;
    
  }
  
  private ReadOnlyDoubleWrapper myReadOnlyDoubleProperty = new ReadOnlyDoubleWrapper(this, "myReadOnlyDouble",_initMyReadOnlyDouble());
  
  private static final double _initMyReadOnlyDouble() {
    return 1.0;
  }
  
  public double getMyReadOnlyDouble() {
    return this.myReadOnlyDoubleProperty.get();
    
  }
  
  public ReadOnlyDoubleProperty myReadOnlyDoubleProperty() {
    return this.myReadOnlyDoubleProperty.getReadOnlyProperty();
    
  }
  
  private ReadOnlyStringWrapper myReadOnlyStringProperty = new ReadOnlyStringWrapper(this, "myReadOnlyString",_initMyReadOnlyString());
  
  private static final String _initMyReadOnlyString() {
    return "";
  }
  
  public String getMyReadOnlyString() {
    return this.myReadOnlyStringProperty.get();
    
  }
  
  public ReadOnlyStringProperty myReadOnlyStringProperty() {
    return this.myReadOnlyStringProperty.getReadOnlyProperty();
    
  }
  
  private ReadOnlyObjectWrapper<Object> myReadOnlyObjectProperty = new ReadOnlyObjectWrapper<Object>(this, "myReadOnlyObject",_initMyReadOnlyObject());
  
  private static final Object _initMyReadOnlyObject() {
    Object _object = new Object();
    return _object;
  }
  
  public Object getMyReadOnlyObject() {
    return this.myReadOnlyObjectProperty.get();
    
  }
  
  public ReadOnlyObjectProperty<Object> myReadOnlyObjectProperty() {
    return this.myReadOnlyObjectProperty.getReadOnlyProperty();
    
  }
  
  private ReadOnlyListWrapper<Object> myReadOnlyListProperty = new ReadOnlyListWrapper<Object>(this, "myReadOnlyList",_initMyReadOnlyList());
  
  private static final ObservableList<Object> _initMyReadOnlyList() {
    ObservableList<Object> _observableArrayList = FXCollections.<Object>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<Object> getMyReadOnlyList() {
    return this.myReadOnlyListProperty.get();
    
  }
  
  public ReadOnlyListProperty<Object> myReadOnlyListProperty() {
    return this.myReadOnlyListProperty.getReadOnlyProperty();
    
  }
  
  private final static double DEFAULT_MYEAGERDOUBLE = 1.0;
  
  private SimpleDoubleProperty myEagerDoubleProperty;
  
  public double getMyEagerDouble() {
    return (this.myEagerDoubleProperty != null)? this.myEagerDoubleProperty.get() : DEFAULT_MYEAGERDOUBLE;
    
  }
  
  public void setMyEagerDouble(final double myEagerDouble) {
    this.myEagerDoubleProperty().set(myEagerDouble);
    
  }
  
  public DoubleProperty myEagerDoubleProperty() {
    if (this.myEagerDoubleProperty == null) { 
    	this.myEagerDoubleProperty = new SimpleDoubleProperty(this, "myEagerDouble", DEFAULT_MYEAGERDOUBLE);
    }
    return this.myEagerDoubleProperty;
    
  }
  
  private final static String DEFAULT_MYEAGERSTRING = "";
  
  private SimpleStringProperty myEagerStringProperty;
  
  public String getMyEagerString() {
    return (this.myEagerStringProperty != null)? this.myEagerStringProperty.get() : DEFAULT_MYEAGERSTRING;
    
  }
  
  public void setMyEagerString(final String myEagerString) {
    this.myEagerStringProperty().set(myEagerString);
    
  }
  
  public StringProperty myEagerStringProperty() {
    if (this.myEagerStringProperty == null) { 
    	this.myEagerStringProperty = new SimpleStringProperty(this, "myEagerString", DEFAULT_MYEAGERSTRING);
    }
    return this.myEagerStringProperty;
    
  }
  
  private SimpleObjectProperty<Object> myEagerObjectProperty;
  
  public Object getMyEagerObject() {
    return (this.myEagerObjectProperty != null)? this.myEagerObjectProperty.get() : this.myEagerObject;
    
  }
  
  public void setMyEagerObject(final Object myEagerObject) {
    if (myEagerObjectProperty != null) {
    	this.myEagerObjectProperty.set(myEagerObject);
    } else {
    	this.myEagerObject = myEagerObject;
    }
    
  }
  
  public ObjectProperty<Object> myEagerObjectProperty() {
    if (this.myEagerObjectProperty == null) { 
    	this.myEagerObjectProperty = new SimpleObjectProperty<Object>(this, "myEagerObject", this.myEagerObject);
    }
    return this.myEagerObjectProperty;
    
  }
  
  private SimpleListProperty<Object> myEagerListProperty;
  
  public ObservableList<Object> getMyEagerList() {
    return (this.myEagerListProperty != null)? this.myEagerListProperty.get() : this.myEagerList;
    
  }
  
  public ListProperty<Object> myEagerListProperty() {
    if (this.myEagerListProperty == null) { 
    	this.myEagerListProperty = new SimpleListProperty<Object>(this, "myEagerList", this.myEagerList);
    }
    return this.myEagerListProperty;
    
  }
  
  private SimpleDoubleProperty myImmutableDoubleProperty = new SimpleDoubleProperty(this, "myImmutableDouble",_initMyImmutableDouble());
  
  private static final double _initMyImmutableDouble() {
    return 1.0;
  }
  
  public double getMyImmutableDouble() {
    return this.myImmutableDoubleProperty.get();
    
  }
  
  public void setMyImmutableDouble(final double myImmutableDouble) {
    this.myImmutableDoubleProperty.set(myImmutableDouble);
    
  }
  
  public DoubleProperty myImmutableDoubleProperty() {
    return this.myImmutableDoubleProperty;
    
  }
  
  private SimpleStringProperty myImmutableStringProperty = new SimpleStringProperty(this, "myImmutableString",_initMyImmutableString());
  
  private static final String _initMyImmutableString() {
    return "";
  }
  
  public String getMyImmutableString() {
    return this.myImmutableStringProperty.get();
    
  }
  
  public void setMyImmutableString(final String myImmutableString) {
    this.myImmutableStringProperty.set(myImmutableString);
    
  }
  
  public StringProperty myImmutableStringProperty() {
    return this.myImmutableStringProperty;
    
  }
  
  private SimpleObjectProperty<Object> myImmutableObjectProperty = new SimpleObjectProperty<Object>(this, "myImmutableObject",_initMyImmutableObject());
  
  private static final Object _initMyImmutableObject() {
    Object _object = new Object();
    return _object;
  }
  
  public Object getMyImmutableObject() {
    return this.myImmutableObjectProperty.get();
    
  }
  
  public void setMyImmutableObject(final Object myImmutableObject) {
    this.myImmutableObjectProperty.set(myImmutableObject);
    
  }
  
  public ObjectProperty<Object> myImmutableObjectProperty() {
    return this.myImmutableObjectProperty;
    
  }
  
  private SimpleListProperty<Object> myImmutableListProperty = new SimpleListProperty<Object>(this, "myImmutableList",_initMyImmutableList());
  
  private static final ObservableList<Object> _initMyImmutableList() {
    ObservableList<Object> _observableArrayList = FXCollections.<Object>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<Object> getMyImmutableList() {
    return this.myImmutableListProperty.get();
    
  }
  
  public ListProperty<Object> myImmutableListProperty() {
    return this.myImmutableListProperty;
    
  }
}
