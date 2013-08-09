package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRootDiagram;

@SuppressWarnings("all")
public interface DiagramAction {
  public abstract void perform(final XRootDiagram rootDiagram);
}
