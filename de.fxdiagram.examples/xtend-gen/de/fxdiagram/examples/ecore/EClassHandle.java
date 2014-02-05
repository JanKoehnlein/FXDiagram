package de.fxdiagram.examples.ecore;

import de.fxdiagram.core.model.DomainObjectHandleImpl;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class EClassHandle extends DomainObjectHandleImpl {
  public EClassHandle() {
  }
  
  public EClassHandle(final EClass eClass, @Extension final EcoreDomainObjectProvider provider) {
    super(provider.getId(eClass), provider.getFqn(eClass), provider);
  }
  
  public EClass getDomainObject() {
    Object _domainObject = super.getDomainObject();
    return ((EClass) _domainObject);
  }
}
