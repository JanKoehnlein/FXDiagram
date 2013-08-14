package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRoot;

@SuppressWarnings("all")
public interface DiagramAction {
  public abstract void perform(final XRoot root);
}
