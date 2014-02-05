package de.fxdiagram.core.model;

import de.fxdiagram.core.model.ModelElement;

@SuppressWarnings("all")
public interface XModelProvider {
  public abstract void populate(final ModelElement element);
}
