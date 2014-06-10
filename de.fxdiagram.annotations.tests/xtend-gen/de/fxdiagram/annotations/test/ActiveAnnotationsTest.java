package de.fxdiagram.annotations.test;

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

@SuppressWarnings("all")
public class ActiveAnnotationsTest {
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
}
