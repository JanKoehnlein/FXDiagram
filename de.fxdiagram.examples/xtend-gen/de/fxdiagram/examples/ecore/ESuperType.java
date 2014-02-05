package de.fxdiagram.examples.ecore;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ESuperType {
  private final EClass _subType;
  
  public EClass getSubType() {
    return this._subType;
  }
  
  private final EClass _superType;
  
  public EClass getSuperType() {
    return this._superType;
  }
  
  public ESuperType(final EClass subType, final EClass superType) {
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
    ESuperType other = (ESuperType) obj;
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
