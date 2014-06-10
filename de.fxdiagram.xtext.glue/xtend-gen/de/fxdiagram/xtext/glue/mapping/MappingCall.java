package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;

@SuppressWarnings("all")
public interface MappingCall<T extends Object, ARG extends Object> {
  public abstract AbstractMapping<T> getMapping();
}
