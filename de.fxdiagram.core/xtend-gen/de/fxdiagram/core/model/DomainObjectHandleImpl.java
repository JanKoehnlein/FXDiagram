package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElement;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

@SuppressWarnings("all")
public class DomainObjectHandleImpl implements DomainObjectHandle {
  private Object cachedDomainObject;
  
  public DomainObjectHandleImpl() {
  }
  
  public DomainObjectHandleImpl(final String id, final String key, final DomainObjectProvider provider) {
    this.idProperty.set(id);
    this.keyProperty.set(key);
    this.providerProperty.set(provider);
  }
  
  public Object getDomainObject() {
    boolean _equals = Objects.equal(this.cachedDomainObject, null);
    if (_equals) {
      DomainObjectProvider _provider = this.getProvider();
      Object _resolveDomainObject = _provider.resolveDomainObject(this);
      this.cachedDomainObject = _resolveDomainObject;
    }
    return this.cachedDomainObject;
  }
  
  public void populate(final ModelElement it) {
    it.<DomainObjectProvider>addProperty(this.providerProperty, DomainObjectProvider.class);
    it.<String>addProperty(this.idProperty, String.class);
    it.<String>addProperty(this.keyProperty, String.class);
  }
  
  private ReadOnlyObjectWrapper<DomainObjectProvider> providerProperty = new ReadOnlyObjectWrapper<DomainObjectProvider>(this, "provider");
  
  public DomainObjectProvider getProvider() {
    return this.providerProperty.get();
  }
  
  public ReadOnlyObjectProperty<DomainObjectProvider> providerProperty() {
    return this.providerProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper idProperty = new ReadOnlyStringWrapper(this, "id");
  
  public String getId() {
    return this.idProperty.get();
  }
  
  public ReadOnlyStringProperty idProperty() {
    return this.idProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyStringWrapper keyProperty = new ReadOnlyStringWrapper(this, "key");
  
  public String getKey() {
    return this.keyProperty.get();
  }
  
  public ReadOnlyStringProperty keyProperty() {
    return this.keyProperty.getReadOnlyProperty();
  }
}
