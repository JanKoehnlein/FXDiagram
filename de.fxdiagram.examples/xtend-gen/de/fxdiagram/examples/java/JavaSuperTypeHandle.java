package de.fxdiagram.examples.java;

import de.fxdiagram.core.model.DomainObjectHandleImpl;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaSuperType;

@SuppressWarnings("all")
public class JavaSuperTypeHandle extends DomainObjectHandleImpl {
  public JavaSuperTypeHandle() {
  }
  
  public JavaSuperTypeHandle(final JavaSuperType it, final JavaModelProvider provider) {
    super(((it.getSubType().getCanonicalName() + "->") + it.getSuperType().getCanonicalName()), 
      ((it.getSubType().getCanonicalName() + "->") + it.getSuperType().getCanonicalName()), provider);
  }
  
  public JavaSuperType getDomainObject() {
    Object _domainObject = super.getDomainObject();
    return ((JavaSuperType) _domainObject);
  }
}
