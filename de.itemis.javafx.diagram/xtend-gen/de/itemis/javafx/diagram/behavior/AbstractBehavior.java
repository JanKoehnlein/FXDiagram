package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.XActivatable;
import de.itemis.javafx.diagram.XNode;

@SuppressWarnings("all")
public abstract class AbstractBehavior implements XActivatable {
  private XNode host;
  
  private boolean isActive;
  
  public AbstractBehavior(final XNode host) {
    this.host = host;
  }
  
  public XNode getHost() {
    return this.host;
  }
  
  public void activate() {
    boolean _not = (!this.isActive);
    if (_not) {
      this.doActivate();
    }
    this.isActive = true;
  }
  
  protected abstract void doActivate();
}
