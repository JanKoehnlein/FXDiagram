package de.fxdiagram.core.behavior;

import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.behavior.Behavior;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.Node;
import org.eclipse.xtext.xbase.lib.Exceptions;

@Logging
@SuppressWarnings("all")
public abstract class AbstractHostBehavior<T extends Node> implements Behavior {
  private T host;
  
  public AbstractHostBehavior(final T host) {
    this.host = host;
  }
  
  public T getHost() {
    return this.host;
  }
  
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
          AbstractHostBehavior.LOG.severe(exc.getMessage());
          exc.printStackTrace();
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    }
  }
  
  protected abstract void doActivate();
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.behavior.AbstractHostBehavior");
    ;
  
  private ReadOnlyBooleanWrapper isActiveProperty = new ReadOnlyBooleanWrapper(this, "isActive");
  
  public boolean getIsActive() {
    return this.isActiveProperty.get();
  }
  
  public ReadOnlyBooleanProperty isActiveProperty() {
    return this.isActiveProperty.getReadOnlyProperty();
  }
}
