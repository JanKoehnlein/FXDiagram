package de.fxdiagram.examples.java;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.java.JavaProperty;
import de.fxdiagram.examples.java.JavaPropertyDescriptor;
import de.fxdiagram.examples.java.JavaSuperTypeDescriptor;
import de.fxdiagram.examples.java.JavaSuperTypeHandle;
import de.fxdiagram.examples.java.JavaTypeDescriptor;
import org.eclipse.xtext.xbase.lib.Exceptions;

@ModelNode
@SuppressWarnings("all")
public class JavaModelProvider implements DomainObjectProvider {
  public DomainObjectDescriptor createDescriptor(final Object domainObject) {
    boolean _matched = false;
    if (!_matched) {
      if (domainObject instanceof Class) {
        _matched=true;
        return this.createJavaTypeDescriptor(((Class<?>)domainObject));
      }
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
  
  public Object resolveDomainObject(final DomainObjectDescriptor descriptor) {
    try {
      Object _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (descriptor instanceof JavaTypeDescriptor) {
          _matched=true;
          String _id = ((JavaTypeDescriptor)descriptor).getId();
          return Class.forName(_id);
        }
      }
      if (!_matched) {
        if (descriptor instanceof JavaPropertyDescriptor) {
          _matched=true;
          JavaProperty _xblockexpression = null;
          {
            String _id = ((JavaPropertyDescriptor)descriptor).getId();
            final String[] split = _id.split(" ");
            String _get = split[1];
            String _get_1 = split[0];
            Class<?> _forName = Class.forName(_get_1);
            _xblockexpression = new JavaProperty(_get, _forName);
          }
          _switchResult = _xblockexpression;
        }
      }
      if (!_matched) {
        if (descriptor instanceof JavaSuperTypeDescriptor) {
          _matched=true;
          JavaSuperTypeHandle _xblockexpression = null;
          {
            String _id = ((JavaSuperTypeDescriptor)descriptor).getId();
            final String[] split = _id.split("->");
            String _get = split[0];
            Class<?> _forName = Class.forName(_get);
            String _get_1 = split[1];
            Class<?> _forName_1 = Class.forName(_get_1);
            _xblockexpression = new JavaSuperTypeHandle(_forName, _forName_1);
          }
          _switchResult = _xblockexpression;
        }
      }
      return _switchResult;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void populate(final ModelElementImpl modelElement) {
    
  }
}
