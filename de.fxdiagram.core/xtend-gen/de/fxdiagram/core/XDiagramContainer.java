package de.fxdiagram.core;

import de.fxdiagram.core.XDiagram;
import javafx.geometry.Insets;

/**
 * Interface for {@XNode}s that contain diagrams.
 */
@SuppressWarnings("all")
public interface XDiagramContainer {
  public abstract XDiagram getInnerDiagram();
  
  public abstract boolean isInnerDiagramActive();
  
  public abstract Insets getInsets();
}
