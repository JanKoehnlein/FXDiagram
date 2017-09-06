package de.fxdiagram.core.behavior;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.OpenBehavior;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.eclipse.xtext.xbase.lib.Exceptions;

@Logging
@SuppressWarnings("all")
public abstract class AbstractOpenBehavior implements OpenBehavior {
  @Override
  public void activate() {
    boolean _isActive = this.getIsActive();
    boolean _not = (!_isActive);
    if (_not) {
      try {
        this.doActivate();
        this.isActiveProperty.set(true);
      } catch (final Throwable _t) {
        if (_t instanceof Exception) {
          final Exception exc = (Exception)_t;
          AbstractOpenBehavior.LOG.severe(exc.getMessage());
          exc.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return OpenBehavior.class;
  }
  
  protected Object doActivate() {
    return null;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.behavior.AbstractOpenBehavior");
    ;
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
