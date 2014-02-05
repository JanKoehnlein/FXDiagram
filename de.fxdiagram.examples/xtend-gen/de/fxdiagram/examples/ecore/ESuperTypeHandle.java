package de.fxdiagram.examples.ecore;

import de.fxdiagram.core.model.DomainObjectHandleImpl;
import de.fxdiagram.examples.ecore.ESuperType;
import de.fxdiagram.examples.ecore.EcoreDomainObjectProvider;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class ESuperTypeHandle extends DomainObjectHandleImpl {
  public ESuperTypeHandle() {
  }
  
  public ESuperTypeHandle(final ESuperType it, @Extension final EcoreDomainObjectProvider provider) {
    super(((provider.getId(it.getSubType()) + "=") + Integer.valueOf(it.getSubType().getEAllSuperTypes().indexOf(it.getSuperType()))), 
      ((provider.getId(it.getSubType()) + "=") + Integer.valueOf(it.getSubType().getEAllSuperTypes().indexOf(it.getSuperType()))), provider);
  }
  
  public ESuperType getDomainObject() {
    Object _domainObject = super.getDomainObject();
    return ((ESuperType) _domainObject);
  }
}
