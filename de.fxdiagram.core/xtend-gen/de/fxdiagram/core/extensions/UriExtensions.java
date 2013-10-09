package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import java.net.URL;
import org.eclipse.core.internal.runtime.Activator;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class UriExtensions {
  public static String toURI(final Object context, final String file) {
    try {
      String _xblockexpression = null;
      {
        Class<? extends Object> _class = context.getClass();
        final URL resource = _class.getResource(file);
        String _xifexpression = null;
        Activator _default = Activator.getDefault();
        boolean _notEquals = (!Objects.equal(_default, null));
        if (_notEquals) {
          URL _fileURL = FileLocator.toFileURL(resource);
          String _externalForm = _fileURL.toExternalForm();
          _xifexpression = _externalForm;
        } else {
          String _externalForm_1 = resource.toExternalForm();
          _xifexpression = _externalForm_1;
        }
        _xblockexpression = (_xifexpression);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
