package de.fxdiagram.examples.ecore;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.examples.ecore.EClassDescriptor;
import de.fxdiagram.examples.ecore.EReferenceDescriptor;
import de.fxdiagram.examples.ecore.ESuperTypeDescriptor;
import de.fxdiagram.examples.ecore.ESuperTypeHandle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

@ModelNode
@SuppressWarnings("all")
public class EcoreDomainObjectProvider implements DomainObjectProvider {
  @Override
  public DomainObjectDescriptor createDescriptor(final Object domainObject) {
    boolean _matched = false;
    if (domainObject instanceof EClass) {
      _matched=true;
      return this.createEClassDescriptor(((EClass)domainObject));
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
  
  public String getId(final EObject it) {
    return EcoreUtil.getURI(it).toString();
  }
  
  public String getFqn(final EClass it) {
    String _name = it.getEPackage().getName();
    String _plus = (_name + ".");
    String _name_1 = it.getName();
    return (_plus + _name_1);
  }
  
  public String getFqn(final EReference it) {
    String _fqn = this.getFqn(it.getEContainingClass());
    String _plus = (_fqn + ".");
    String _name = it.getName();
    return (_plus + _name);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
  
  public void postLoad() {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
