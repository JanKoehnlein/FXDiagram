package de.fxdiagram.examples.ecore;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.DomainObjectHandleImpl;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class EReferenceHandle extends DomainObjectHandleImpl {
  public EReferenceHandle() {
  }
  
  public EReferenceHandle(final EReference eReference, @Extension final EcoreDomainObjectProvider provider) {
    super(provider.getId(eReference), provider.getFqn(eReference), provider);
  }
  
  public EReference getDomainObject() {
    Object _domainObject = super.getDomainObject();
    return ((EReference) _domainObject);
  }
  
  public int hashCode() {
    EReference _domainObject = this.getDomainObject();
    int _hashCode = _domainObject.hashCode();
    EReference _elvis = null;
    EReference _domainObject_1 = this.getDomainObject();
    EReference _eOpposite = _domainObject_1.getEOpposite();
    if (_eOpposite != null) {
      _elvis = _eOpposite;
    } else {
      EReference _domainObject_2 = this.getDomainObject();
      _elvis = ObjectExtensions.<EReference>operator_elvis(_eOpposite, _domainObject_2);
    }
    int _hashCode_1 = _elvis.hashCode();
    int _plus = (_hashCode + _hashCode_1);
    return _plus;
  }
  
  public boolean equals(final Object other) {
    if ((other instanceof EReferenceHandle)) {
      boolean _or = false;
      EReference _domainObject = ((EReferenceHandle)other).getDomainObject();
      EReference _domainObject_1 = this.getDomainObject();
      boolean _equals = Objects.equal(_domainObject, _domainObject_1);
      if (_equals) {
        _or = true;
      } else {
        EReference _domainObject_2 = ((EReferenceHandle)other).getDomainObject();
        EReference _domainObject_3 = this.getDomainObject();
        EReference _eOpposite = _domainObject_3.getEOpposite();
        boolean _equals_1 = Objects.equal(_domainObject_2, _eOpposite);
        _or = (_equals || _equals_1);
      }
      return _or;
    } else {
      return false;
    }
  }
}
