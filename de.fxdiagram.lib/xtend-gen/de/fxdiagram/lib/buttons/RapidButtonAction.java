package de.fxdiagram.lib.buttons;

import de.fxdiagram.lib.buttons.RapidButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

@SuppressWarnings("all")
public abstract class RapidButtonAction {
  public abstract void perform(final RapidButton button);
  
  private SimpleBooleanProperty enabledProperty = new SimpleBooleanProperty(this, "enabled",_initEnabled());
  
  private static final boolean _initEnabled() {
    return true;
  }
  
  public boolean getEnabled() {
    return this.enabledProperty.get();
  }
  
  public void setEnabled(final boolean enabled) {
    this.enabledProperty.set(enabled);
  }
  
  public BooleanProperty enabledProperty() {
    return this.enabledProperty;
  }
}
