package de.fxdiagram.core;

import de.fxdiagram.core.XDiagram;
import javafx.geometry.Insets;

/**
 * Interface for {@XNode}s that contain diagrams.
 */
@SuppressWarnings("all")
public interface XDiagramContainer {
  public abstract XDiagram getInnerDiagram();
  
  public abstract void setInnerDiagram(final XDiagram diagram);
  
  public abstract boolean isInnerDiagramActive();
  
  public abstract Insets getInsets();
}
