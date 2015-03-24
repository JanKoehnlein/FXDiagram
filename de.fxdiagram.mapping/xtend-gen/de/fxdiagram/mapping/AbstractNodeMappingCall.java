package de.fxdiagram.mapping;

import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.MappingCall;
import de.fxdiagram.mapping.NodeMapping;

@SuppressWarnings("all")
public abstract class AbstractNodeMappingCall<RESULT extends Object, ARG extends Object> implements MappingCall<RESULT, ARG> {
  public abstract NodeMapping<RESULT> getNodeMapping();
  
  @Override
  public AbstractMapping<RESULT> getMapping() {
    return this.getNodeMapping();
  }
}
