package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.eclipse.mapping.AbstractMapping;
import de.fxdiagram.eclipse.mapping.MappingCall;
import de.fxdiagram.eclipse.mapping.NodeMapping;

@SuppressWarnings("all")
public abstract class AbstractNodeMappingCall<RESULT extends Object, ARG extends Object> implements MappingCall<RESULT, ARG> {
  public abstract NodeMapping<RESULT> getNodeMapping();
  
  @Override
  public AbstractMapping<RESULT> getMapping() {
    return this.getNodeMapping();
  }
}
