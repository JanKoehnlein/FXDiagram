package de.fxdiagram.core.model;

import de.fxdiagram.core.model.ModelElementImpl;

/**
 * Something that provides a model, i.e. can be serializable.
 * 
 * @see {@link ModelNode}
 */
@SuppressWarnings("all")
public interface XModelProvider {
  public abstract void populate(final ModelElementImpl element);
}
