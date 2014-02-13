package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.model.ColorAdapter;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@Logging
@SuppressWarnings("all")
public class ModelFactory {
  private final Map<String,Class<ColorAdapter>> valueAdapters = new Function0<Map<String,Class<ColorAdapter>>>() {
    public Map<String,Class<ColorAdapter>> apply() {
      Map<String,Class<ColorAdapter>> _xsetliteral = null;
      String _name = Color.class.getName();
      Map<String,Class<ColorAdapter>> _tempMap = Maps.<String, Class<ColorAdapter>>newHashMap();
      _tempMap.put(_name, ColorAdapter.class);
      _xsetliteral = Collections.<String, Class<ColorAdapter>>unmodifiableMap(_tempMap);
      return _xsetliteral;
    }
  }.apply();
  
  protected ModelElement createElement(final String className, final ClassLoader classLoader) {
    try {
      ModelElement _xblockexpression = null;
      {
        final Class<ColorAdapter> valueAdapter = this.valueAdapters.get(className);
        ModelElement _xifexpression = null;
        boolean _notEquals = (!Objects.equal(valueAdapter, null));
        if (_notEquals) {
          _xifexpression = valueAdapter.newInstance();
        } else {
          ModelElement _xblockexpression_1 = null;
          {
            final Class<?> clazz = classLoader.loadClass(className);
            final Object node = clazz.newInstance();
            _xblockexpression_1 = this.createElement(node);
          }
          _xifexpression = _xblockexpression_1;
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected ModelElement createElement(final Object node) {
    ModelElement _xblockexpression = null;
    {
      boolean _equals = Objects.equal(node, null);
      if (_equals) {
        return null;
      }
      _xblockexpression = this.create(node);
    }
    return _xblockexpression;
  }
  
  protected ModelElement _create(final XModelProvider node) {
    ModelElementImpl _xblockexpression = null;
    {
      final ModelElementImpl element = new ModelElementImpl(node);
      node.populate(element);
      _xblockexpression = element;
    }
    return _xblockexpression;
  }
  
  protected ModelElement _create(final Text text) {
    ModelElementImpl _xblockexpression = null;
    {
      final ModelElementImpl element = new ModelElementImpl(text);
      StringProperty _textProperty = text.textProperty();
      element.<String>addProperty(_textProperty, String.class);
      _xblockexpression = element;
    }
    return _xblockexpression;
  }
  
  protected ModelElement _create(final Color color) {
    ColorAdapter _xblockexpression = null;
    {
      final ColorAdapter element = new ColorAdapter(color);
      _xblockexpression = element;
    }
    return _xblockexpression;
  }
  
  protected ModelElement _create(final Object object) {
    ModelElement _xblockexpression = null;
    {
      String _string = object.toString();
      String _plus = ("No model population strategy for " + _string);
      ModelFactory.LOG.severe(_plus);
      _xblockexpression = null;
    }
    return _xblockexpression;
  }
  
  protected ModelElement create(final Object text) {
    if (text instanceof Text) {
      return _create((Text)text);
    } else if (text instanceof Color) {
      return _create((Color)text);
    } else if (text instanceof XModelProvider) {
      return _create((XModelProvider)text);
    } else if (text != null) {
      return _create(text);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(text).toString());
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelFactory");
    ;
}
