package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XShape;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

@SuppressWarnings("all")
public abstract class AbstractBehavior<T extends XShape> implements XActivatable {
  private T host;
  
  public AbstractBehavior(final T host) {
    this.host = host;
  }
  
  public T getHost() {
    return this.host;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
  }
  
  protected abstract void doActivate();
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
