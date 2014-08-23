package de.fxdiagram.examples.java;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class JavaSuperTypeHandle {
  private final Class<?> subType;
  
  private final Class<?> superType;
  
  public JavaSuperTypeHandle(final Class<?> subType, final Class<?> superType) {
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
    JavaSuperTypeHandle other = (JavaSuperTypeHandle) obj;
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
  public Class<?> getSubType() {
    return this.subType;
  }
  
  @Pure
  public Class<?> getSuperType() {
    return this.superType;
  }
}
