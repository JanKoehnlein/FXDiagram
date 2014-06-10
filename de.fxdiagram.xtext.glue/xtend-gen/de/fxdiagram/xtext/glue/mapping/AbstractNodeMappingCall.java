package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;

@SuppressWarnings("all")
public abstract class AbstractNodeMappingCall<T extends Object, ARG extends Object> implements MappingCall<T, ARG> {
  public abstract NodeMapping<T> getNodeMapping();
  
  public AbstractMapping<T> getMapping() {
    return this.getNodeMapping();
  }
}
