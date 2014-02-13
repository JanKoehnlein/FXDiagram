package de.fxdiagram.examples.java;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class JavaSuperTypeHandle {
  private final Class<?> _subType;
  
  public Class<?> getSubType() {
    return this._subType;
  }
  
  private final Class<?> _superType;
  
  public Class<?> getSuperType() {
    return this._superType;
  }
  
  public JavaSuperTypeHandle(final Class<?> subType, final Class<?> superType) {
    super();
    this._subType = subType;
    this._superType = superType;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_subType== null) ? 0 : _subType.hashCode());
    result = prime * result + ((_superType== null) ? 0 : _superType.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JavaSuperTypeHandle other = (JavaSuperTypeHandle) obj;
    if (_subType == null) {
      if (other._subType != null)
        return false;
    } else if (!_subType.equals(other._subType))
      return false;
    if (_superType == null) {
      if (other._superType != null)
        return false;
    } else if (!_superType.equals(other._superType))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
