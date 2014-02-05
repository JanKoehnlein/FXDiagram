package de.fxdiagram.examples.ecore;

import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.examples.ecore.EClassHandle;
import de.fxdiagram.examples.ecore.EReferenceHandle;
import de.fxdiagram.examples.ecore.ESuperType;
import de.fxdiagram.examples.ecore.ESuperTypeHandle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

@SuppressWarnings("all")
public class EcoreDomainObjectProvider implements DomainObjectProvider {
  public DomainObjectHandle createDomainObjectHandle(final Object object) {
    boolean _matched = false;
    if (!_matched) {
      if (object instanceof EClass) {
        _matched=true;
        return this.createEClassHandle(((EClass)object));
      }
    }
    if (!_matched) {
      if (object instanceof EReference) {
        _matched=true;
        return this.createEReferenceHandle(((EReference)object));
      }
    }
    if (!_matched) {
      if (object instanceof ESuperType) {
        _matched=true;
        return this.createESuperClassHandle(((ESuperType)object));
      }
    }
    return null;
  }
  
  public EClassHandle createEClassHandle(final EClass object) {
    EClassHandle _eClassHandle = new EClassHandle(object, this);
    return _eClassHandle;
  }
  
  public EReferenceHandle createEReferenceHandle(final EReference object) {
    EReferenceHandle _eReferenceHandle = new EReferenceHandle(object, this);
    return _eReferenceHandle;
  }
  
  public ESuperTypeHandle createESuperClassHandle(final ESuperType object) {
    ESuperTypeHandle _eSuperTypeHandle = new ESuperTypeHandle(object, this);
    return _eSuperTypeHandle;
  }
  
  public Object resolveDomainObject(final DomainObjectHandle handle) {
    String _id = handle.getId();
    final URI uri = URI.createURI(_id);
    URI _trimFragment = uri.trimFragment();
    String _string = _trimFragment.toString();
    final EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(_string);
    String _fragment = uri.fragment();
    final int posEquals = _fragment.indexOf("=");
    String _xifexpression = null;
    if ((posEquals == (-1))) {
      String _fragment_1 = uri.fragment();
      _xifexpression = _fragment_1;
    } else {
      String _fragment_2 = uri.fragment();
      String _substring = _fragment_2.substring(0, posEquals);
      _xifexpression = _substring;
    }
    final String fragment = _xifexpression;
    Resource _eResource = ePackage.eResource();
    final EObject eObject = _eResource.getEObject(fragment);
    if ((handle instanceof ESuperTypeHandle)) {
      final EClass eClass = ((EClass) eObject);
      EList<EClass> _eAllSuperTypes = eClass.getEAllSuperTypes();
      String _fragment_3 = uri.fragment();
      String _substring_1 = _fragment_3.substring((posEquals + 1));
      int _parseInt = Integer.parseInt(_substring_1);
      EClass _get = _eAllSuperTypes.get(_parseInt);
      ESuperType _eSuperType = new ESuperType(eClass, _get);
      return _eSuperType;
    } else {
      return eObject;
    }
  }
  
  public void populate(final ModelElement element) {
  }
  
  public String getId(final EObject it) {
    URI _uRI = EcoreUtil.getURI(it);
    String _string = _uRI.toString();
    return _string;
  }
  
  public String getFqn(final EClass it) {
    EPackage _ePackage = it.getEPackage();
    String _name = _ePackage.getName();
    String _plus = (_name + ".");
    String _name_1 = it.getName();
    String _plus_1 = (_plus + _name_1);
    return _plus_1;
  }
  
  public String getFqn(final EReference it) {
    EClass _eContainingClass = it.getEContainingClass();
    String _fqn = this.getFqn(_eContainingClass);
    String _plus = (_fqn + ".");
    String _name = it.getName();
    String _plus_1 = (_plus + _name);
    return _plus_1;
  }
}
