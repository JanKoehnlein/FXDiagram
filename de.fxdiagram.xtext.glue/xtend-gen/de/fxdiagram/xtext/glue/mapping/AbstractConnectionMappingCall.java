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
  
  private String _role;
  
  public String getRole() {
    return this._role;
  }
  
  public void setRole(final String role) {
    this._role = role;
  }
  
  public abstract ConnectionMapping<T> getConnectionMapping();
}
