package de.fxdiagram.xtext.glue.mapping;

@SuppressWarnings("all")
public abstract class BaseMapping<T extends Object> {
  private Class<T> _typeGuard;
  
  public Class<T> getTypeGuard() {
    return this._typeGuard;
  }
  
  public void setTypeGuard(final Class<T> typeGuard) {
    this._typeGuard = typeGuard;
  }
  
  public BaseMapping(final Class<T> typeGuard) {
    this.setTypeGuard(typeGuard);
  }
  
  public boolean isApplicable(final Object domainObject) {
    Class<T> _typeGuard = this.getTypeGuard();
    return _typeGuard.isInstance(domainObject);
  }
}
