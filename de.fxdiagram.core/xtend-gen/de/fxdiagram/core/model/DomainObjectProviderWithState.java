package de.fxdiagram.core.model;

import de.fxdiagram.core.model.DomainObjectProvider;

@SuppressWarnings("all")
public interface DomainObjectProviderWithState extends DomainObjectProvider {
  public abstract void copyState(final DomainObjectProviderWithState other);
}
