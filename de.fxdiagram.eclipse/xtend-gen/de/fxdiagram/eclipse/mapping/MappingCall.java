package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.eclipse.mapping.AbstractMapping;

@SuppressWarnings("all")
public interface MappingCall<RESULT extends Object, ARG extends Object> {
  public abstract AbstractMapping<RESULT> getMapping();
}
