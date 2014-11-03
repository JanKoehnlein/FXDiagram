package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.IMappedElementDescriptor;

@SuppressWarnings("all")
public interface IMappedElementDescriptorProvider extends DomainObjectProvider {
  public abstract <T extends Object> IMappedElementDescriptor<T> createMappedElementDescriptor(final T domainObject, final AbstractMapping<T> mapping);
}
