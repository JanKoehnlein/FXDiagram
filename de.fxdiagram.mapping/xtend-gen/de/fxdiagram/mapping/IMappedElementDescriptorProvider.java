package de.fxdiagram.mapping;

import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.IMappedElementDescriptor;

/**
 * A {@link DomainObjectProvider} that translates between domain objects with a mapping
 * and {@link IMappedElementDescriptor}s.
 */
@SuppressWarnings("all")
public interface IMappedElementDescriptorProvider extends DomainObjectProvider {
  public abstract <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<T> mapping);
}
