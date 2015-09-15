package de.fxdiagram.mapping;

import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.AbstractMapping;
import de.fxdiagram.mapping.MappingCall;

@SuppressWarnings("all")
public abstract class AbstractLabelMappingCall<RESULT extends Object, ARG extends Object> implements MappingCall<RESULT, ARG> {
  public abstract AbstractLabelMapping<RESULT> getLabelMapping();
  
  @Override
  public AbstractMapping<RESULT> getMapping() {
    return this.getLabelMapping();
  }
}
