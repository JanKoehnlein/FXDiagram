package de.fxdiagram.core.model;

import de.fxdiagram.core.model.ModelElement;

@SuppressWarnings("all")
public interface ValueAdapter<T extends Object> extends ModelElement {
  public abstract T getValueObject();
}
