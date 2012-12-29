package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.XActivatable;
import de.itemis.javafx.diagram.XNode;

@SuppressWarnings("all")
public abstract class AbstractBehavior implements XActivatable {
  private XNode host;
  
  private boolean _isActive;
  
  public boolean isIsActive() {
    return this._isActive;
  }
  
  public void setIsActive(final boolean isActive) {
    this._isActive = isActive;
  }
  
  public AbstractBehavior(final XNode host) {
    this.host = host;
  }
  
  public XNode getHost() {
    return this.host;
  }
  
  public void activate() {
    boolean _isIsActive = this.isIsActive();
    boolean _not = (!_isIsActive);
    if (_not) {
      this.doActivate();
    }
    this.setIsActive(true);
  }
  
  protected abstract void doActivate();
}
