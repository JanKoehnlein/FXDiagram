package de.fxdiagram.core.model;

import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.XModelProvider;

@SuppressWarnings("all")
public interface DomainObjectProvider extends XModelProvider {
  public abstract <T extends Object> DomainObjectDescriptor createDescriptor(final T domainObject);
}
