package de.fxdiagram.examples.ecore;

import com.google.common.base.Objects;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class EReferenceKey {
  private EReference left;
  
  private EReference right;
  
  public EReferenceKey(final EReference left) {
    this.left = left;
    EReference _elvis = null;
    EReference _eOpposite = left.getEOpposite();
    if (_eOpposite != null) {
      _elvis = _eOpposite;
    } else {
      _elvis = ObjectExtensions.<EReference>operator_elvis(_eOpposite, left);
    }
    this.right = _elvis;
  }
  
  public EReference getLeft() {
    return this.left;
  }
  
  public EReference getRight() {
    return this.right;
  }
  
  public int hashCode() {
    int _hashCode = this.left.hashCode();
    int _hashCode_1 = this.right.hashCode();
    int _plus = (_hashCode + _hashCode_1);
    return _plus;
  }
  
  public boolean equals(final Object other) {
    boolean _matched = false;
    if (!_matched) {
      if (other instanceof EReferenceKey) {
        _matched=true;
        boolean _or = false;
        boolean _and = false;
        boolean _equals = Objects.equal(((EReferenceKey)other).left, this.left);
        if (!_equals) {
          _and = false;
        } else {
          boolean _equals_1 = Objects.equal(((EReferenceKey)other).right, this.right);
          _and = (_equals && _equals_1);
        }
        if (_and) {
          _or = true;
        } else {
          boolean _and_1 = false;
          boolean _equals_2 = Objects.equal(((EReferenceKey)other).left, this.right);
          if (!_equals_2) {
            _and_1 = false;
          } else {
            boolean _equals_3 = Objects.equal(((EReferenceKey)other).right, this.left);
            _and_1 = (_equals_2 && _equals_3);
          }
          _or = (_and || _and_1);
        }
        return _or;
      }
    }
    return false;
  }
  
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("EReferenceKey ");
    _builder.append(this.left, "");
    {
      boolean _notEquals = (!Objects.equal(this.right, this.left));
      if (_notEquals) {
        _builder.append(" / ");
        _builder.append(this.right, "");
      }
    }
    return _builder.toString();
  }
}
