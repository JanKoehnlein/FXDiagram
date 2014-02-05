package de.fxdiagram.examples.java;

import de.fxdiagram.core.model.DomainObjectHandleImpl;
import de.fxdiagram.examples.java.JavaModelProvider;

@SuppressWarnings("all")
public class JavaTypeHandle extends DomainObjectHandleImpl {
  public JavaTypeHandle() {
  }
  
  public JavaTypeHandle(final Class<? extends Object> javaClass, final JavaModelProvider provider) {
    super(javaClass.getCanonicalName(), javaClass.getCanonicalName(), provider);
  }
  
  public Class<? extends Object> getDomainObject() {
    Object _domainObject = super.getDomainObject();
    return ((Class<?>) _domainObject);
  }
}
