package de.fxdiagram.eclipse.mapping;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.w3c.dom.EntityReference;

/**
 * The value of an {@link EntityReference} in the context of an {@link EObject}.
 */
@Data
@SuppressWarnings("all")
public class ESetting<ECLASS extends EObject> {
  private final ECLASS owner;
  
  private final EReference reference;
  
  private final int index;
  
  public Object getTarget() {
    Object _xifexpression = null;
    boolean _isMany = this.reference.isMany();
    if (_isMany) {
      Object _eGet = this.owner.eGet(this.reference);
      _xifexpression = ((List<?>) _eGet).get(this.index);
    } else {
      _xifexpression = this.owner.eGet(this.reference);
    }
    return _xifexpression;
  }
  
  public ESetting(final ECLASS owner, final EReference reference, final int index) {
    super();
    this.owner = owner;
    this.reference = reference;
    this.index = index;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.owner== null) ? 0 : this.owner.hashCode());
    result = prime * result + ((this.reference== null) ? 0 : this.reference.hashCode());
    result = prime * result + this.index;
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
    ESetting<?> other = (ESetting<?>) obj;
    if (this.owner == null) {
      if (other.owner != null)
        return false;
    } else if (!this.owner.equals(other.owner))
      return false;
    if (this.reference == null) {
      if (other.reference != null)
        return false;
    } else if (!this.reference.equals(other.reference))
      return false;
    if (other.index != this.index)
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("owner", this.owner);
    b.add("reference", this.reference);
    b.add("index", this.index);
    return b.toString();
  }
  
  @Pure
  public ECLASS getOwner() {
    return this.owner;
  }
  
  @Pure
  public EReference getReference() {
    return this.reference;
  }
  
  @Pure
  public int getIndex() {
    return this.index;
  }
}
