package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.ModelElement;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

@SuppressWarnings("all")
public class StringHandle implements DomainObjectHandle {
  public StringHandle() {
  }
  
  public StringHandle(final String key) {
    this.setKey(key);
  }
  
  public void setKey(final String key) {
    String _key = this.getKey();
    boolean _notEquals = (!Objects.equal(_key, null));
    if (_notEquals) {
      IllegalStateException _illegalStateException = new IllegalStateException("Cannot reset the key on a StringHandle");
      throw _illegalStateException;
    }
    this.keyProperty.set(key);
  }
  
  public void populate(final ModelElement it) {
    it.<String>addProperty(this.keyProperty, String.class);
  }
  
  public String getId() {
    String _key = this.getKey();
    return _key;
  }
  
  public Object getDomainObject() {
    String _key = this.getKey();
    return _key;
  }
  
  private ReadOnlyStringWrapper keyProperty = new ReadOnlyStringWrapper(this, "key",_initKey());
  
  private static final String _initKey() {
    return null;
  }
  
  public String getKey() {
    return this.keyProperty.get();
  }
  
  public ReadOnlyStringProperty keyProperty() {
    return this.keyProperty.getReadOnlyProperty();
  }
}
