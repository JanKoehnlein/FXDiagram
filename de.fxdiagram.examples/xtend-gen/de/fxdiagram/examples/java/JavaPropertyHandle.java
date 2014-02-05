package de.fxdiagram.examples.java;

import de.fxdiagram.core.model.DomainObjectHandleImpl;
import de.fxdiagram.examples.java.JavaModelProvider;
import de.fxdiagram.examples.java.JavaProperty;

@SuppressWarnings("all")
public class JavaPropertyHandle extends DomainObjectHandleImpl {
  public JavaPropertyHandle() {
  }
  
  public JavaPropertyHandle(final JavaProperty it, final JavaModelProvider provider) {
    super(((it.getType().getCanonicalName() + " ") + it.getName()), ((it.getType().getCanonicalName() + " ") + it.getName()), provider);
  }
  
  public JavaProperty getDomainObject() {
    Object _domainObject = super.getDomainObject();
    return ((JavaProperty) _domainObject);
  }
}
