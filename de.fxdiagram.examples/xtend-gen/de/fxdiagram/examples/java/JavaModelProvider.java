package de.fxdiagram.examples.java;

import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.examples.java.JavaProperty;
import de.fxdiagram.examples.java.JavaPropertyHandle;
import de.fxdiagram.examples.java.JavaSuperType;
import de.fxdiagram.examples.java.JavaSuperTypeHandle;
import de.fxdiagram.examples.java.JavaTypeHandle;
import org.eclipse.xtext.xbase.lib.Exceptions;

@ModelNode
@SuppressWarnings("all")
public class JavaModelProvider implements DomainObjectProvider {
  public DomainObjectHandle createDomainObjectHandle(final Object object) {
    boolean _matched = false;
    if (!_matched) {
      if (object instanceof Class) {
        _matched=true;
        return this.createJavaTypeHandle(((Class<?>)object));
      }
    }
    if (!_matched) {
      if (object instanceof JavaProperty) {
        _matched=true;
        return this.createJavaPropertyHandle(((JavaProperty)object));
      }
    }
    if (!_matched) {
      if (object instanceof JavaSuperType) {
        _matched=true;
        return this.createJavaSuperClassHandle(((JavaSuperType)object));
      }
    }
    return null;
  }
  
  public JavaSuperTypeHandle createJavaSuperClassHandle(final JavaSuperType javaSuperType) {
    return new JavaSuperTypeHandle(javaSuperType, this);
  }
  
  public JavaPropertyHandle createJavaPropertyHandle(final JavaProperty property) {
    return new JavaPropertyHandle(property, this);
  }
  
  public JavaTypeHandle createJavaTypeHandle(final Class<?> clazz) {
    return new JavaTypeHandle(clazz, this);
  }
  
  public Object resolveDomainObject(final DomainObjectHandle handle) {
    try {
      Object _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (handle instanceof JavaTypeHandle) {
          _matched=true;
          String _id = ((JavaTypeHandle)handle).getId();
          return Class.forName(_id);
        }
      }
      if (!_matched) {
        if (handle instanceof JavaPropertyHandle) {
          _matched=true;
          JavaProperty _xblockexpression = null;
          {
            String _id = ((JavaPropertyHandle)handle).getId();
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
        if (handle instanceof JavaSuperTypeHandle) {
          _matched=true;
          JavaSuperType _xblockexpression = null;
          {
            String _id = ((JavaSuperTypeHandle)handle).getId();
            final String[] split = _id.split("->");
            String _get = split[0];
            Class<?> _forName = Class.forName(_get);
            String _get_1 = split[1];
            Class<?> _forName_1 = Class.forName(_get_1);
            _xblockexpression = new JavaSuperType(_forName, _forName_1);
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
