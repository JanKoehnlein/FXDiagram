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
      boolean _or = false;
      boolean _and = false;
      boolean _equals = Objects.equal(((EReferenceWithOpposite)obj).to, this.to);
      if (!_equals) {
        _and = false;
      } else {
        boolean _equals_1 = Objects.equal(((EReferenceWithOpposite)obj).fro, this.fro);
        _and = _equals_1;
      }
      if (_and) {
        _or = true;
      } else {
        boolean _and_1 = false;
        boolean _equals_2 = Objects.equal(((EReferenceWithOpposite)obj).fro, this.to);
        if (!_equals_2) {
          _and_1 = false;
        } else {
          boolean _equals_3 = Objects.equal(((EReferenceWithOpposite)obj).to, this.fro);
          _and_1 = _equals_3;
        }
        _or = _and_1;
      }
      _xifexpression = _or;
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
