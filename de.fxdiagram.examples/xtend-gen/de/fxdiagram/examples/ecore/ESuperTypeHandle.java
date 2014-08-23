package de.fxdiagram.examples.ecore;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class ESuperTypeHandle {
  private final EClass subType;
  
  private final EClass superType;
  
  public ESuperTypeHandle(final EClass subType, final EClass superType) {
    super();
    this.subType = subType;
    this.superType = superType;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.subType== null) ? 0 : this.subType.hashCode());
    result = prime * result + ((this.superType== null) ? 0 : this.superType.hashCode());
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
    if (this.subType == null) {
      if (other.subType != null)
        return false;
    } else if (!this.subType.equals(other.subType))
      return false;
    if (this.superType == null) {
      if (other.superType != null)
        return false;
    } else if (!this.superType.equals(other.superType))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("subType", this.subType);
    b.add("superType", this.superType);
    return b.toString();
  }
  
  @Pure
  public EClass getSubType() {
    return this.subType;
  }
  
  @Pure
  public EClass getSuperType() {
    return this.superType;
  }
}
