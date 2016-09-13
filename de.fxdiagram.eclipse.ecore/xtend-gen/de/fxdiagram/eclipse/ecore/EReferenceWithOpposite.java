package de.fxdiagram.eclipse.ecore;

import com.google.common.base.Objects;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtend.lib.annotations.AccessorType;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.Pure;

@FinalFieldsConstructor
@Accessors(AccessorType.PUBLIC_GETTER)
@SuppressWarnings("all")
public class EReferenceWithOpposite {
  private final EReference to;
  
  private final EReference fro;
  
  public EReferenceWithOpposite(final EReference to) {
    this.to = to;
    EReference _eOpposite = to.getEOpposite();
    this.fro = _eOpposite;
  }
  
  @Override
  public boolean equals(final Object obj) {
    boolean _xifexpression = false;
    if ((obj instanceof EReferenceWithOpposite)) {
      _xifexpression = ((Objects.equal(((EReferenceWithOpposite)obj).to, this.to) && Objects.equal(((EReferenceWithOpposite)obj).fro, this.fro)) || (Objects.equal(((EReferenceWithOpposite)obj).fro, this.to) && Objects.equal(((EReferenceWithOpposite)obj).to, this.fro)));
    } else {
      _xifexpression = false;
    }
    return _xifexpression;
  }
  
  public EReferenceWithOpposite(final EReference to, final EReference fro) {
    super();
    this.to = to;
    this.fro = fro;
  }
  
  @Pure
  public EReference getTo() {
    return this.to;
  }
  
  @Pure
  public EReference getFro() {
    return this.fro;
  }
}
