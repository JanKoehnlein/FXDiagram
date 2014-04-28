package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import java.net.URL;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleReference;

@SuppressWarnings("all")
public class ClassLoaderExtensions {
  public static String toURI(final Object context, final String file) {
    String _xblockexpression = null;
    {
      Class<?> _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (context instanceof Class) {
          _matched=true;
          _switchResult = ((Class<?>)context);
        }
      }
      if (!_matched) {
        _switchResult = context.getClass();
      }
      final URL resource = _switchResult.getResource(file);
      _xblockexpression = ClassLoaderExtensions.toURI(resource);
    }
    return _xblockexpression;
  }
  
  protected static String toURI(final URL resource) {
    try {
      String _xifexpression = null;
      boolean _isEquinox = ClassLoaderExtensions.isEquinox();
      if (_isEquinox) {
        URL _fileURL = FileLocator.toFileURL(resource);
        _xifexpression = _fileURL.toExternalForm();
      } else {
        _xifexpression = resource.toExternalForm();
      }
      return _xifexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static String toURI(final ClassLoader classLoader, final String path) {
    URL _resource = classLoader.getResource(path);
    return ClassLoaderExtensions.toURI(_resource);
  }
  
  public static Node fxmlNode(final Object context, final String file) {
    try {
      String _uRI = ClassLoaderExtensions.toURI(context, file);
      URL _uRL = new URL(_uRI);
      return FXMLLoader.<Node>load(_uRL);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static String serialize(final Class<?> clazz) {
    boolean _isEquinox = ClassLoaderExtensions.isEquinox();
    if (_isEquinox) {
      final ClassLoader classLoader = clazz.getClassLoader();
      if ((classLoader instanceof BundleReference)) {
        Bundle _bundle = ((BundleReference)classLoader).getBundle();
        String _symbolicName = _bundle.getSymbolicName();
        String _plus = (_symbolicName + ":");
        String _canonicalName = clazz.getCanonicalName();
        return (_plus + _canonicalName);
      }
    }
    return clazz.getCanonicalName();
  }
  
  public static Class<?> deserialize(final String classLoaderClass) {
    try {
      final String[] split = classLoaderClass.split(":");
      String _xifexpression = null;
      int _size = ((List<String>)Conversions.doWrapArray(split)).size();
      boolean _equals = (_size == 2);
      if (_equals) {
        _xifexpression = split[0];
      } else {
        _xifexpression = null;
      }
      final String bundleName = _xifexpression;
      String _xifexpression_1 = null;
      int _size_1 = ((List<String>)Conversions.doWrapArray(split)).size();
      boolean _equals_1 = (_size_1 == 2);
      if (_equals_1) {
        _xifexpression_1 = split[1];
      } else {
        _xifexpression_1 = classLoaderClass;
      }
      final String className = _xifexpression_1;
      boolean _and = false;
      boolean _notEquals = (!Objects.equal(bundleName, null));
      if (!_notEquals) {
        _and = false;
      } else {
        boolean _isEquinox = ClassLoaderExtensions.isEquinox();
        _and = _isEquinox;
      }
      if (_and) {
        final Bundle bundle = Platform.getBundle(bundleName);
        boolean _notEquals_1 = (!Objects.equal(bundle, null));
        if (_notEquals_1) {
          return bundle.loadClass(className);
        }
      } else {
        ClassLoader _classLoader = ClassLoaderExtensions.class.getClassLoader();
        return _classLoader.loadClass(className);
      }
      return null;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static Object getClassLoader(final String bundleName) {
    return null;
  }
  
  public static boolean isEquinox() {
    Activator _default = Activator.getDefault();
    return (!Objects.equal(_default, null));
  }
}
