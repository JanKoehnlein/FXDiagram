package de.fxdiagram.core.model;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.ModelElement;

/**
 * Something that provides a model.
 * 
 * @see {@link ModelNode}
 */
@SuppressWarnings("all")
public interface XModelProvider {
  public abstract void populate(final ModelElement element);
}
