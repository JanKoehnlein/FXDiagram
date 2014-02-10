package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class UriExtensions {
  public static String toURI(final Object context, final String file) {
    try {
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
        String _xifexpression = null;
        Activator _default = Activator.getDefault();
        boolean _notEquals = (!Objects.equal(_default, null));
        if (_notEquals) {
          URL _fileURL = FileLocator.toFileURL(resource);
          _xifexpression = _fileURL.toExternalForm();
        } else {
          _xifexpression = resource.toExternalForm();
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static Node fxmlNode(final Object context, final String file) {
    try {
      String _uRI = UriExtensions.toURI(context, file);
      URL _uRL = new URL(_uRI);
      return FXMLLoader.<Node>load(_uRL);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
