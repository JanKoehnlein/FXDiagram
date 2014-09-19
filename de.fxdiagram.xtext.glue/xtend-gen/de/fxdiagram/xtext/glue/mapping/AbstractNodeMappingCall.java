package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;

@SuppressWarnings("all")
public abstract class AbstractNodeMappingCall<RESULT extends Object, ARG extends Object> implements MappingCall<RESULT, ARG> {
  public abstract NodeMapping<RESULT> getNodeMapping();
  
  public AbstractMapping<RESULT> getMapping() {
    return this.getNodeMapping();
  }
}
