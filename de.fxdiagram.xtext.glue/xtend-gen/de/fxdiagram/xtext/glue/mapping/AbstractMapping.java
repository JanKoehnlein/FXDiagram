package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.XDiagramConfig;

@SuppressWarnings("all")
public abstract class AbstractMapping<T extends Object> {
  private String id;
  
  private Class<T> _typeGuard;
  
  public Class<T> getTypeGuard() {
    return this._typeGuard;
  }
  
  public void setTypeGuard(final Class<T> typeGuard) {
    this._typeGuard = typeGuard;
  }
  
  private XDiagramConfig _config;
  
  public XDiagramConfig getConfig() {
    return this._config;
  }
  
  public void setConfig(final XDiagramConfig config) {
    this._config = config;
  }
  
  public AbstractMapping(final String id, final Class<T> typeGuard) {
    this.id = id;
    this.setTypeGuard(typeGuard);
  }
  
  public AbstractMapping(final Class<T> typeGuard) {
    String _simpleName = typeGuard.getSimpleName();
    this.id = _simpleName;
    this.setTypeGuard(typeGuard);
  }
  
  public boolean isApplicable(final Object domainObject) {
    Class<T> _typeGuard = this.getTypeGuard();
    return _typeGuard.isInstance(domainObject);
  }
  
  public String getID() {
    return this.id;
  }
}
