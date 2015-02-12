package de.fxdiagram.core.model;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.XModelProvider;

/**
 * Translates between {@link DomainObjectDescriptor}s and domain objects.
 * 
 * @see DomainObjectDescriptor
 */
@SuppressWarnings("all")
public interface DomainObjectProvider extends XModelProvider {
  public abstract <T extends Object> DomainObjectDescriptor createDescriptor(final T domainObject);
}
