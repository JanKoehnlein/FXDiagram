package de.fxdiagram.core.model;

import de.fxdiagram.core.model.DomainObjectProvider;

/**
 * A {@link DomainObjectProvider} that stores some application specific state,
 * that needs to be transfered to another instance, e.g. the current diagram's
 * classloader when another diagram is loaded.
 */
@SuppressWarnings("all")
public interface DomainObjectProviderWithState extends DomainObjectProvider {
  public abstract void copyState(final DomainObjectProviderWithState from);
}
