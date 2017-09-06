package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.extensions.ClassLoaderExtensions;
import de.fxdiagram.core.model.Model;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelRepairer;
import de.fxdiagram.core.tools.actions.SaveAction;
import java.io.Writer;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Pair;

/**
 * Functionality of a {@link SaveAction}
 */
@Logging
@SuppressWarnings("all")
public class ModelSave {
  private Map<ModelElement, String> idMap;
  
  private Model model;
  
  public void save(final Object root, final Writer out) {
    new ModelRepairer().repair(root);
    Model _model = new Model(root);
    this.model = _model;
    ModelElement _rootElement = this.model.getRootElement();
    boolean _notEquals = (!Objects.equal(_rootElement, null));
    if (_notEquals) {
      this.idMap = CollectionLiterals.<ModelElement, String>newHashMap();
      Pair<String, Boolean> _mappedTo = Pair.<String, Boolean>of(JsonGenerator.PRETTY_PRINTING, Boolean.valueOf(true));
      this.write(Json.createGeneratorFactory(Collections.<String, Boolean>unmodifiableMap(CollectionLiterals.<String, Boolean>newHashMap(_mappedTo))).createGenerator(out), this.model.getRootElement(), null, "").close();
    }
  }
  
  protected JsonGenerator write(final JsonGenerator gen, final ModelElement element, final String propertyName, final String currentId) {
    boolean _equals = Objects.equal(element, null);
    if (_equals) {
      gen.writeNull(propertyName);
    } else {
      final String cachedId = this.idMap.get(element);
      boolean _notEquals = (!Objects.equal(cachedId, null));
      if (_notEquals) {
        boolean _notEquals_1 = (!Objects.equal(propertyName, null));
        if (_notEquals_1) {
          gen.write(propertyName, cachedId);
        } else {
          gen.write(cachedId);
        }
      } else {
        this.idMap.put(element, currentId);
        final String className = ClassLoaderExtensions.serialize(element.getNode().getClass());
        boolean _notEquals_2 = (!Objects.equal(propertyName, null));
        if (_notEquals_2) {
          gen.writeStartObject(propertyName);
        } else {
          gen.writeStartObject();
        }
        gen.write("__class", className);
        final Consumer<Property<?>> _function = (Property<?> it) -> {
          this.write(gen, it, element.getType(it), currentId);
        };
        element.getProperties().forEach(_function);
        final Consumer<ListProperty<?>> _function_1 = (ListProperty<?> it) -> {
          this.write(gen, it, element.getType(it), currentId);
        };
        element.getListProperties().forEach(_function_1);
        gen.writeEnd();
      }
    }
    return gen;
  }
  
  protected JsonGenerator write(final JsonGenerator gen, final Property<?> property, final Class<?> propertyType, final String currentId) {
    JsonGenerator _xifexpression = null;
    if (((!Objects.equal(property.getValue(), null)) && (!property.isBound()))) {
      JsonGenerator _switchResult = null;
      boolean _matched = false;
      if (Objects.equal(propertyType, String.class)) {
        _matched=true;
        _switchResult = gen.write(property.getName(), ((StringProperty) property).getValue());
      }
      if (!_matched) {
        if (Objects.equal(propertyType, Double.class)) {
          _matched=true;
          _switchResult = gen.write(property.getName(), ((DoubleProperty) property).doubleValue());
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType, Float.class)) {
          _matched=true;
          _switchResult = gen.write(property.getName(), ((FloatProperty) property).floatValue());
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType, Long.class)) {
          _matched=true;
          _switchResult = gen.write(property.getName(), ((LongProperty) property).longValue());
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType, Integer.class)) {
          _matched=true;
          _switchResult = gen.write(property.getName(), ((IntegerProperty) property).intValue());
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType, Boolean.class)) {
          _matched=true;
          _switchResult = gen.write(property.getName(), ((BooleanProperty) property).getValue().booleanValue());
        }
      }
      if (!_matched) {
        boolean _isAssignableFrom = Enum.class.isAssignableFrom(propertyType);
        if (_isAssignableFrom) {
          _matched=true;
          _switchResult = gen.write(property.getName(), property.getValue().toString());
        }
      }
      if (!_matched) {
        ModelElement _get = this.model.getIndex().get(property.getValue());
        String _name = property.getName();
        String _name_1 = property.getName();
        String _plus = ((currentId + "/") + _name_1);
        _switchResult = this.write(gen, _get, _name, _plus);
      }
      _xifexpression = _switchResult;
    }
    return _xifexpression;
  }
  
  protected JsonGenerator write(final JsonGenerator gen, final ListProperty<?> property, final Class<?> propertyType, final String currentId) {
    JsonGenerator _xblockexpression = null;
    {
      gen.writeStartArray(property.getName());
      int _size = property.getValue().size();
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
      for (final Integer i : _doubleDotLessThan) {
        {
          final Object value = property.getValue().get((i).intValue());
          boolean _matched = false;
          if (Objects.equal(propertyType, String.class)) {
            _matched=true;
            gen.write(((String) value));
          }
          if (!_matched) {
            if (Objects.equal(propertyType, Double.class)) {
              _matched=true;
              gen.write(((Double) value).doubleValue());
            }
          }
          if (!_matched) {
            if (Objects.equal(propertyType, Float.class)) {
              _matched=true;
              gen.write(((Float) value).floatValue());
            }
          }
          if (!_matched) {
            if (Objects.equal(propertyType, Long.class)) {
              _matched=true;
              gen.write(((Long) value).longValue());
            }
          }
          if (!_matched) {
            if (Objects.equal(propertyType, Integer.class)) {
              _matched=true;
              gen.write(((Integer) value).intValue());
            }
          }
          if (!_matched) {
            if (Objects.equal(propertyType, Boolean.class)) {
              _matched=true;
              gen.write(((Boolean) value).booleanValue());
            }
          }
          if (!_matched) {
            boolean _isAssignableFrom = Enum.class.isAssignableFrom(propertyType);
            if (_isAssignableFrom) {
              _matched=true;
              gen.write(value.toString());
            }
          }
          if (!_matched) {
            boolean _containsKey = this.model.getIndex().containsKey(value);
            if (_containsKey) {
              ModelElement _get = this.model.getIndex().get(value);
              String _name = property.getName();
              String _plus = ((currentId + "/") + _name);
              String _plus_1 = (_plus + ".");
              String _plus_2 = (_plus_1 + i);
              this.write(gen, _get, 
                null, _plus_2);
            }
          }
        }
      }
      _xblockexpression = gen.writeEnd();
    }
    return _xblockexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelSave");
    ;
}
