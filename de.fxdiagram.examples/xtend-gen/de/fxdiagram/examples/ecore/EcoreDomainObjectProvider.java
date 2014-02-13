package de.fxdiagram.examples.ecore;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.ecore.EClassDescriptor;
import de.fxdiagram.examples.ecore.EReferenceDescriptor;
import de.fxdiagram.examples.ecore.ESuperTypeDescriptor;
import de.fxdiagram.examples.ecore.ESuperTypeHandle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

@ModelNode
@SuppressWarnings("all")
public class EcoreDomainObjectProvider implements DomainObjectProvider {
  public DomainObjectDescriptor createDescriptor(final Object domainObject) {
    boolean _matched = false;
    if (!_matched) {
      if (domainObject instanceof EClass) {
        _matched=true;
        return this.createEClassDescriptor(((EClass)domainObject));
      }
    }
    if (!_matched) {
      if (domainObject instanceof EReference) {
        _matched=true;
        return this.createEReferenceDescriptor(((EReference)domainObject));
      }
    }
    if (!_matched) {
      if (domainObject instanceof ESuperTypeHandle) {
        _matched=true;
        return this.createESuperClassDescriptor(((ESuperTypeHandle)domainObject));
      }
    }
    return null;
  }
  
  public EClassDescriptor createEClassDescriptor(final EClass object) {
    return new EClassDescriptor(object, this);
  }
  
  public EReferenceDescriptor createEReferenceDescriptor(final EReference object) {
    return new EReferenceDescriptor(object, this);
  }
  
  public ESuperTypeDescriptor createESuperClassDescriptor(final ESuperTypeHandle object) {
    return new ESuperTypeDescriptor(object, this);
  }
  
  public Object resolveDomainObject(final DomainObjectDescriptor descriptor) {
    String _id = descriptor.getId();
    final URI uri = URI.createURI(_id);
    URI _trimFragment = uri.trimFragment();
    String _string = _trimFragment.toString();
    final EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(_string);
    String _fragment = uri.fragment();
    final int posEquals = _fragment.indexOf("=");
    String _xifexpression = null;
    if ((posEquals == (-1))) {
      _xifexpression = uri.fragment();
    } else {
      String _fragment_1 = uri.fragment();
      _xifexpression = _fragment_1.substring(0, posEquals);
    }
    final String fragment = _xifexpression;
    Resource _eResource = ePackage.eResource();
    final EObject eObject = _eResource.getEObject(fragment);
    if ((descriptor instanceof ESuperTypeHandle)) {
      final EClass eClass = ((EClass) eObject);
      EList<EClass> _eAllSuperTypes = eClass.getEAllSuperTypes();
      String _fragment_2 = uri.fragment();
      String _substring = _fragment_2.substring((posEquals + 1));
      int _parseInt = Integer.parseInt(_substring);
      EClass _get = _eAllSuperTypes.get(_parseInt);
      return new ESuperTypeHandle(eClass, _get);
    } else {
      return eObject;
    }
  }
  
  public String getId(final EObject it) {
    URI _uRI = EcoreUtil.getURI(it);
    return _uRI.toString();
  }
  
  public String getFqn(final EClass it) {
    EPackage _ePackage = it.getEPackage();
    String _name = _ePackage.getName();
    String _plus = (_name + ".");
    String _name_1 = it.getName();
    return (_plus + _name_1);
  }
  
  public String getFqn(final EReference it) {
    EClass _eContainingClass = it.getEContainingClass();
    String _fqn = this.getFqn(_eContainingClass);
    String _plus = (_fqn + ".");
    String _name = it.getName();
    return (_plus + _name);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
