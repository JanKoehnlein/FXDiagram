package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@ModelNode({ "id", "name", "provider" })
@SuppressWarnings("all")
public class DomainObjectDescriptorImpl<T extends Object> implements DomainObjectDescriptor {
  private T cachedDomainObject;
  
  public DomainObjectDescriptorImpl(final String id, final String name, final DomainObjectProvider provider) {
    this.idProperty.set(id);
    this.nameProperty.set(name);
    this.providerProperty.set(provider);
  }
  
  public T getDomainObject() {
    boolean _equals = Objects.equal(this.cachedDomainObject, null);
    if (_equals) {
      DomainObjectProvider _provider = this.getProvider();
      Object _resolveDomainObject = _provider.resolveDomainObject(this);
      this.cachedDomainObject = ((T) _resolveDomainObject);
    }
    return this.cachedDomainObject;
  }
  
  public boolean equals(final Object obj) {
    boolean _and = false;
    boolean _and_1 = false;
    boolean _notEquals = (!Objects.equal(obj, null));
    if (!_notEquals) {
      _and_1 = false;
    } else {
      Class<? extends DomainObjectDescriptorImpl> _class = this.getClass();
      Class<?> _class_1 = obj.getClass();
      boolean _equals = Objects.equal(_class, _class_1);
      _and_1 = _equals;
    }
    if (!_and_1) {
      _and = false;
    } else {
      String _id = this.getId();
      String _id_1 = ((DomainObjectDescriptor) obj).getId();
      boolean _equals_1 = _id.equals(_id_1);
      _and = _equals_1;
    }
    return _and;
  }
  
  public int hashCode() {
    String _id = this.getId();
    return _id.hashCode();
  }
  
  /**
   * Automatically generated by @ModelNode. Needed for deserialization.
   */
  public DomainObjectDescriptorImpl() {
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(idProperty, String.class);
    modelElement.addProperty(nameProperty, String.class);
    modelElement.addProperty(providerProperty, DomainObjectProvider.class);
  }
  
  private SimpleObjectProperty<DomainObjectProvider> providerProperty = new SimpleObjectProperty<DomainObjectProvider>(this, "provider");
  
  public DomainObjectProvider getProvider() {
    return this.providerProperty.get();
  }
  
  public void setProvider(final DomainObjectProvider provider) {
    this.providerProperty.set(provider);
  }
  
  public ObjectProperty<DomainObjectProvider> providerProperty() {
    return this.providerProperty;
  }
  
  private SimpleStringProperty idProperty = new SimpleStringProperty(this, "id");
  
  public String getId() {
    return this.idProperty.get();
  }
  
  public void setId(final String id) {
    this.idProperty.set(id);
  }
  
  public StringProperty idProperty() {
    return this.idProperty;
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
}