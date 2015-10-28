package de.fxdiagram.annotations.test;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.model.XModelProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@ModelNode("objects")
@SuppressWarnings("all")
public class Modeltest2 implements XModelProvider {
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(objectsProperty, DomainObjectDescriptor.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleListProperty<DomainObjectDescriptor> objectsProperty = new SimpleListProperty<DomainObjectDescriptor>(this, "objects",_initObjects());
  
  private static final ObservableList<DomainObjectDescriptor> _initObjects() {
    ObservableList<DomainObjectDescriptor> _observableArrayList = FXCollections.<DomainObjectDescriptor>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<DomainObjectDescriptor> getObjects() {
    return this.objectsProperty.get();
  }
  
  public ListProperty<DomainObjectDescriptor> objectsProperty() {
    return this.objectsProperty;
  }
}
