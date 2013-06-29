package de.fxdiagram.core.behavior;

import de.fxdiagram.core.XActivatable;
import de.fxdiagram.core.XNode;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

@SuppressWarnings("all")
public abstract class AbstractBehavior implements XActivatable {
  private XNode host;
  
  public AbstractBehavior(final XNode host) {
    this.host = host;
  }
  
  public XNode getHost() {
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
