package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.XModelProvider;
import java.util.Arrays;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Text;

@Logging
@SuppressWarnings("all")
public class ModelFactory {
  protected ModelElement createElement(final Object node) {
    ModelElement _xblockexpression = null;
    {
      boolean _equals = Objects.equal(node, null);
      if (_equals) {
        return null;
      }
      ModelElement _modelElement = new ModelElement(node);
      final ModelElement element = _modelElement;
      this.populate(node, element);
      _xblockexpression = (element);
    }
    return _xblockexpression;
  }
  
  protected Object _populate(final XModelProvider node, final ModelElement it) {
    node.populate(it);
    return null;
  }
  
  protected Class<? extends Object> _populate(final Text text, final ModelElement it) {
    StringProperty _textProperty = text.textProperty();
    Class<? extends Object> _addProperty = it.<String>addProperty(_textProperty, String.class);
    return _addProperty;
  }
  
  protected Object _populate(final Object object, final ModelElement it) {
    Object _xblockexpression = null;
    {
      String _string = object.toString();
      String _plus = ("No model population strategy for " + _string);
      ModelFactory.LOG.severe(_plus);
      _xblockexpression = (null);
    }
    return _xblockexpression;
  }
  
  protected Object populate(final Object text, final ModelElement it) {
    if (text instanceof Text) {
      return _populate((Text)text, it);
    } else if (text instanceof XModelProvider) {
      return _populate((XModelProvider)text, it);
    } else if (text != null) {
      return _populate(text, it);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(text, it).toString());
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelFactory");
    ;
}
