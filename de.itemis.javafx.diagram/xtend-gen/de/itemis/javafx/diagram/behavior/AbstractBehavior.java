package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.Diagram;
import de.itemis.javafx.diagram.ShapeContainer;

@SuppressWarnings("all")
public abstract class AbstractBehavior {
  private ShapeContainer host;
  
  public AbstractBehavior(final ShapeContainer host) {
    this.host = host;
  }
  
  public ShapeContainer getHost() {
    return this.host;
  }
  
  protected abstract void activate(final Diagram diagram);
}
