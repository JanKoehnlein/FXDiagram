package de.fxdiagram.annotations.test;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@ModelNode("fqn")
@SuppressWarnings("all")
public class Modeltest4 implements XModelProvider {
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(fqnProperty, String.class);
  }
  
  private SimpleListProperty<String> fqnProperty = new SimpleListProperty<String>(this, "fqn",_initFqn());
  
  private static final ObservableList<String> _initFqn() {
    ObservableList<String> _observableArrayList = FXCollections.<String>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<String> getFqn() {
    return this.fqnProperty.get();
  }
  
  public ListProperty<String> fqnProperty() {
    return this.fqnProperty;
  }
}
