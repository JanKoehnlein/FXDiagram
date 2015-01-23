package de.fxdiagram.core.behavior;

import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.CloseBehavior;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;

@SuppressWarnings("all")
public abstract class AbstractCloseBehavior implements CloseBehavior {
  public Class<? extends Behavior> getBehaviorKey() {
    return CloseBehavior.class;
  }
  
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActiveProperty.set(true);
  }
  
  protected Object doActivate() {
    return null;
  }
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
