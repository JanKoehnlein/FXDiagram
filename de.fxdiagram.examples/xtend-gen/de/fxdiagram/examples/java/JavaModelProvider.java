package de.fxdiagram.examples.java;

import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.DomainObjectProvider;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.examples.java.JavaProperty;
import de.fxdiagram.examples.java.JavaPropertyHandle;
import de.fxdiagram.examples.java.JavaSuperType;
import de.fxdiagram.examples.java.JavaSuperTypeHandle;
import de.fxdiagram.examples.java.JavaTypeHandle;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class JavaModelProvider implements DomainObjectProvider {
  public DomainObjectHandle createDomainObjectHandle(final Object object) {
    boolean _matched = false;
    if (!_matched) {
      if (object instanceof Class) {
        _matched=true;
        return this.createJavaTypeHandle(((Class<? extends Object>)object));
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
    JavaSuperTypeHandle _javaSuperTypeHandle = new JavaSuperTypeHandle(javaSuperType, this);
    return _javaSuperTypeHandle;
  }
  
  public JavaPropertyHandle createJavaPropertyHandle(final JavaProperty property) {
    JavaPropertyHandle _javaPropertyHandle = new JavaPropertyHandle(property, this);
    return _javaPropertyHandle;
  }
  
  public JavaTypeHandle createJavaTypeHandle(final Class<? extends Object> clazz) {
    JavaTypeHandle _javaTypeHandle = new JavaTypeHandle(clazz, this);
    return _javaTypeHandle;
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
            Class<? extends Object> _forName = Class.forName(_get_1);
            JavaProperty _javaProperty = new JavaProperty(_get, _forName);
            _xblockexpression = (_javaProperty);
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
            Class<? extends Object> _forName = Class.forName(_get);
            String _get_1 = split[1];
            Class<? extends Object> _forName_1 = Class.forName(_get_1);
            JavaSuperType _javaSuperType = new JavaSuperType(_forName, _forName_1);
            _xblockexpression = (_javaSuperType);
          }
          _switchResult = _xblockexpression;
        }
      }
      return _switchResult;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void populate(final ModelElement element) {
  }
}
