package de.fxdiagram.examples.ecore;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ESuperTypeHandle {
  private final EClass _subType;
  
  private final EClass _superType;
  
  public ESuperTypeHandle(final EClass subType, final EClass superType) {
    super();
    this._subType = subType;
    this._superType = superType;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._subType== null) ? 0 : this._subType.hashCode());
    result = prime * result + ((this._superType== null) ? 0 : this._superType.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ESuperTypeHandle other = (ESuperTypeHandle) obj;
    if (this._subType == null) {
      if (other._subType != null)
        return false;
    } else if (!this._subType.equals(other._subType))
      return false;
    if (this._superType == null) {
      if (other._superType != null)
        return false;
    } else if (!this._superType.equals(other._superType))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
  
  @Pure
  public EClass getSubType() {
    return this._subType;
  }
  
  @Pure
  public EClass getSuperType() {
    return this._superType;
  }
}
