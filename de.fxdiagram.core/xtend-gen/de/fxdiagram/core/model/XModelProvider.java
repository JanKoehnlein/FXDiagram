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
  
  /**
   * Implementing classes can return <code>true</code> if the specific implementation should not be serialized.
   */
  public default boolean isTransient() {
    return false;
  }
  
  public abstract void postLoad();
}
