package de.fxdiagram.lib.nodes;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@ModelNode({ "namespace", "name", "attributes", "operations" })
@SuppressWarnings("all")
public class ClassModel implements XModelProvider {
  @Override
  public boolean equals(final Object other) {
    if ((other instanceof ClassModel)) {
      boolean _and = false;
      boolean _and_1 = false;
      boolean _and_2 = false;
      boolean _and_3 = false;
      String _namespace = this.getNamespace();
      String _namespace_1 = ((ClassModel)other).getNamespace();
      boolean _equals = Objects.equal(_namespace, _namespace_1);
      if (!_equals) {
        _and_3 = false;
      } else {
        String _name = this.getName();
        String _name_1 = ((ClassModel)other).getName();
        boolean _equals_1 = Objects.equal(_name, _name_1);
        _and_3 = _equals_1;
      }
      if (!_and_3) {
        _and_2 = false;
      } else {
        String _fileName = this.getFileName();
        String _fileName_1 = ((ClassModel)other).getFileName();
        boolean _equals_2 = Objects.equal(_fileName, _fileName_1);
        _and_2 = _equals_2;
      }
      if (!_and_2) {
        _and_1 = false;
      } else {
        ObservableList<String> _attributes = this.getAttributes();
        ObservableList<String> _attributes_1 = ((ClassModel)other).getAttributes();
        boolean _equals_3 = Objects.equal(_attributes, _attributes_1);
        _and_1 = _equals_3;
      }
      if (!_and_1) {
        _and = false;
      } else {
        ObservableList<String> _operations = this.getOperations();
        ObservableList<String> _operations_1 = ((ClassModel)other).getOperations();
        boolean _equals_4 = Objects.equal(_operations, _operations_1);
        _and = _equals_4;
      }
      return _and;
    } else {
      return false;
    }
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(namespaceProperty, String.class);
    modelElement.addProperty(nameProperty, String.class);
    modelElement.addProperty(attributesProperty, String.class);
    modelElement.addProperty(operationsProperty, String.class);
  }
  
  private SimpleStringProperty namespaceProperty = new SimpleStringProperty(this, "namespace",_initNamespace());
  
  private static final String _initNamespace() {
    return "<default>";
  }
  
  public String getNamespace() {
    return this.namespaceProperty.get();
  }
  
  public void setNamespace(final String namespace) {
    this.namespaceProperty.set(namespace);
  }
  
  public StringProperty namespaceProperty() {
    return this.namespaceProperty;
  }
  
  private SimpleStringProperty nameProperty = new SimpleStringProperty(this, "name");
  
  public String getName() {
    return this.nameProperty.get();
  }
  
  public void setName(final String name) {
    this.nameProperty.set(name);
  }
  
  public StringProperty nameProperty() {
    return this.nameProperty;
  }
  
  private SimpleStringProperty fileNameProperty = new SimpleStringProperty(this, "fileName");
  
  public String getFileName() {
    return this.fileNameProperty.get();
  }
  
  public void setFileName(final String fileName) {
    this.fileNameProperty.set(fileName);
  }
  
  public StringProperty fileNameProperty() {
    return this.fileNameProperty;
  }
  
  private SimpleListProperty<String> attributesProperty = new SimpleListProperty<String>(this, "attributes",_initAttributes());
  
  private static final ObservableList<String> _initAttributes() {
    ObservableList<String> _observableArrayList = FXCollections.<String>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<String> getAttributes() {
    return this.attributesProperty.get();
  }
  
  public ListProperty<String> attributesProperty() {
    return this.attributesProperty;
  }
  
  private SimpleListProperty<String> operationsProperty = new SimpleListProperty<String>(this, "operations",_initOperations());
  
  private static final ObservableList<String> _initOperations() {
    ObservableList<String> _observableArrayList = FXCollections.<String>observableArrayList();
    return _observableArrayList;
  }
  
  public ObservableList<String> getOperations() {
    return this.operationsProperty.get();
  }
  
  public ListProperty<String> operationsProperty() {
    return this.operationsProperty;
  }
}
