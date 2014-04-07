package de.fxdiagram.annotations.test;

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
}
