package de.fxdiagram.examples.java;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.examples.java.JavaProperty;
import de.fxdiagram.examples.java.JavaPropertyDescriptor;
import de.fxdiagram.examples.java.JavaSuperTypeDescriptor;
import de.fxdiagram.examples.java.JavaSuperTypeHandle;
import de.fxdiagram.examples.java.JavaTypeDescriptor;

@ModelNode
@SuppressWarnings("all")
public class JavaModelProvider implements DomainObjectProvider {
  @Override
  public DomainObjectDescriptor createDescriptor(final Object domainObject) {
    boolean _matched = false;
    if (domainObject instanceof Class) {
      _matched=true;
      return this.createJavaTypeDescriptor(((Class<?>)domainObject));
    }
    if (!_matched) {
      if (domainObject instanceof JavaProperty) {
        _matched=true;
        return this.createJavaPropertyDescriptor(((JavaProperty)domainObject));
      }
    }
    if (!_matched) {
      if (domainObject instanceof JavaSuperTypeHandle) {
        _matched=true;
        return this.createJavaSuperClassDescriptor(((JavaSuperTypeHandle)domainObject));
      }
    }
    return null;
  }
  
  public JavaSuperTypeDescriptor createJavaSuperClassDescriptor(final JavaSuperTypeHandle javaSuperType) {
    return new JavaSuperTypeDescriptor(javaSuperType, this);
  }
  
  public JavaPropertyDescriptor createJavaPropertyDescriptor(final JavaProperty property) {
    return new JavaPropertyDescriptor(property, this);
  }
  
  public JavaTypeDescriptor createJavaTypeDescriptor(final Class<?> clazz) {
    return new JavaTypeDescriptor(clazz, this);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
