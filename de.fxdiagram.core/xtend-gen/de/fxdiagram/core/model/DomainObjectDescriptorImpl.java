package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.eclipse.xtext.xbase.lib.Functions.Function1;

@SuppressWarnings("all")
public abstract class DomainObjectDescriptorImpl<T extends Object> implements DomainObjectDescriptor {
  public DomainObjectDescriptorImpl() {
  }
  
  public DomainObjectDescriptorImpl(final String id, final String name, final DomainObjectProvider provider) {
    this.idProperty.set(id);
    this.nameProperty.set(name);
    this.providerProperty.set(provider);
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
  
  public abstract <U extends Object> U withDomainObject(final Function1<? super T,? extends U> lambda);
  
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
