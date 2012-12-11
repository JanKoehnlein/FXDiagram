package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.Activateable;
import de.itemis.javafx.diagram.XNode;

@SuppressWarnings("all")
public abstract class AbstractBehavior implements Activateable {
  private XNode host;
  
  public AbstractBehavior(final XNode host) {
    this.host = host;
  }
  
  public XNode getHost() {
    return this.host;
  }
}
