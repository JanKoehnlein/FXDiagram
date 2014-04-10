package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;

@SuppressWarnings("all")
public abstract class AbstractConnectionMappingCall<T extends Object, ARG extends Object> {
  private boolean lazy = false;
  
  public boolean isLazy() {
    return this.lazy;
  }
  
  public boolean makeLazy() {
    return this.lazy = true;
  }
  
  public abstract ConnectionMapping<T> getConnectionMapping();
}
