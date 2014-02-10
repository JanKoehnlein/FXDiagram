package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.model.Model;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelPersistence;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class ModelSave {
  private Map<ModelElement,String> idMap;
  
  private Set<ModelElement> alreadySerialized;
  
  private Model model;
  
  public void save(final Object root, final Writer out) {
    Model _model = new Model(root);
    this.model = _model;
    ModelElement _rootElement = this.model.getRootElement();
    boolean _notEquals = (!Objects.equal(_rootElement, null));
    if (_notEquals) {
      HashMap<ModelElement,String> _newHashMap = CollectionLiterals.<ModelElement, String>newHashMap();
      this.idMap = _newHashMap;
      ModelElement _rootElement_1 = this.model.getRootElement();
      this.createIDs(_rootElement_1, "");
      HashSet<ModelElement> _newHashSet = CollectionLiterals.<ModelElement>newHashSet();
      this.alreadySerialized = _newHashSet;
      Map<String,Boolean> _xsetliteral = null;
      Map<String,Boolean> _tempMap = Maps.<String, Boolean>newHashMap();
      _tempMap.put(JsonGenerator.PRETTY_PRINTING, Boolean.valueOf(true));
      _xsetliteral = Collections.<String, Boolean>unmodifiableMap(_tempMap);
      JsonGeneratorFactory _createGeneratorFactory = Json.createGeneratorFactory(_xsetliteral);
      JsonGenerator _createGenerator = _createGeneratorFactory.createGenerator(out);
      JsonGenerator _writeStartObject = _createGenerator.writeStartObject();
      ModelElement _rootElement_2 = this.model.getRootElement();
      JsonGenerator _write = this.write(_writeStartObject, _rootElement_2);
      JsonGenerator _writeEnd = _write.writeEnd();
      _writeEnd.close();
    }
  }
  
  protected void createIDs(final ModelElement it, final String id) {
    boolean _notEquals = (!Objects.equal(it, null));
    if (_notEquals) {
      this.idMap.put(it, id);
      List<Property<?>> _children = it.getChildren();
      for (final Property<?> property : _children) {
        Object _value = property.getValue();
        boolean _notEquals_1 = (!Objects.equal(_value, null));
        if (_notEquals_1) {
          Map<Object,ModelElement> _index = this.model.getIndex();
          Object _value_1 = property.getValue();
          ModelElement _get = _index.get(_value_1);
          String _name = property.getName();
          String _plus = ((id + "/") + _name);
          this.createIDs(_get, _plus);
        }
      }
      List<ListProperty<?>> _listChildren = it.getListChildren();
      for (final ListProperty<?> property_1 : _listChildren) {
        {
          int i = 0;
          ObservableList<?> _value_2 = property_1.getValue();
          for (final Object value : _value_2) {
            {
              boolean _notEquals_2 = (!Objects.equal(value, null));
              if (_notEquals_2) {
                Map<Object,ModelElement> _index_1 = this.model.getIndex();
                ModelElement _get_1 = _index_1.get(value);
                String _name_1 = property_1.getName();
                String _plus_1 = ((id + "/") + _name_1);
                String _plus_2 = (_plus_1 + ".");
                String _plus_3 = (_plus_2 + Integer.valueOf(i));
                this.createIDs(_get_1, _plus_3);
              }
              i = (i + 1);
            }
          }
        }
      }
    }
  }
  
  protected JsonGenerator write(final JsonGenerator gen, final ModelElement element) {
    boolean _add = this.alreadySerialized.add(element);
    boolean _not = (!_add);
    if (_not) {
      String _get = this.idMap.get(element);
      gen.write(_get);
    } else {
      gen.writeStartObject("__constructor");
      Object _node = element.getNode();
      Class<?> _class = _node.getClass();
      final String className = _class.getCanonicalName();
      gen.write("__class", className);
      List<Property<?>> _constructorProperties = element.getConstructorProperties();
      final Function1<Property<?>,Class<?>> _function = new Function1<Property<?>,Class<?>>() {
        public Class<?> apply(final Property<?> it) {
          return element.getType(it);
        }
      };
      final List<Class<?>> paramTypes = ListExtensions.<Property<?>, Class<?>>map(_constructorProperties, _function);
      Object _node_1 = element.getNode();
      Class<?> _class_1 = _node_1.getClass();
      final Constructor<?> constructor = ModelPersistence.findConstructor(_class_1, paramTypes);
      List<Property<?>> remainingChildren = null;
      boolean _notEquals = (!Objects.equal(constructor, null));
      if (_notEquals) {
        remainingChildren = Collections.<Property<?>>unmodifiableList(Lists.<Property<?>>newArrayList());
        List<Property<?>> _constructorProperties_1 = element.getConstructorProperties();
        boolean _isEmpty = _constructorProperties_1.isEmpty();
        boolean _not_1 = (!_isEmpty);
        if (_not_1) {
          gen.writeStartArray("__params");
          List<Property<?>> _constructorProperties_2 = element.getConstructorProperties();
          final Procedure1<Property<?>> _function_1 = new Procedure1<Property<?>>() {
            public void apply(final Property<?> it) {
              JsonGenerator _writeStartObject = gen.writeStartObject();
              Map<Object,ModelElement> _index = ModelSave.this.model.getIndex();
              Object _value = it.getValue();
              ModelElement _get = _index.get(_value);
              JsonGenerator _write = ModelSave.this.write(_writeStartObject, _get);
              _write.writeEnd();
            }
          };
          IterableExtensions.<Property<?>>forEach(_constructorProperties_2, _function_1);
          gen.writeEnd();
        }
      } else {
        final Function1<Class<?>,String> _function_2 = new Function1<Class<?>,String>() {
          public String apply(final Class<?> it) {
            return it.getSimpleName();
          }
        };
        List<String> _map = ListExtensions.<Class<?>, String>map(paramTypes, _function_2);
        String _join = IterableExtensions.join(_map, ",");
        String _plus = ((("Cannot find constructor " + className) + "(") + _join);
        String _plus_1 = (_plus + "). Using no-arg constructor instead.");
        ModelSave.LOG.warning(_plus_1);
        List<Property<?>> _constructorProperties_3 = element.getConstructorProperties();
        remainingChildren = _constructorProperties_3;
      }
      gen.writeEnd();
      List<ListProperty<?>> _listChildren = element.getListChildren();
      final Procedure1<ListProperty<?>> _function_3 = new Procedure1<ListProperty<?>>() {
        public void apply(final ListProperty<?> it) {
          boolean _isEmpty = it.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            String _name = it.getName();
            gen.writeStartArray(_name);
            ObservableList<?> _value = it.getValue();
            final Procedure1<Object> _function = new Procedure1<Object>() {
              public void apply(final Object it) {
                boolean _equals = Objects.equal(it, null);
                if (_equals) {
                  gen.writeNull();
                } else {
                  JsonGenerator _writeStartObject = gen.writeStartObject();
                  Map<Object,ModelElement> _index = ModelSave.this.model.getIndex();
                  ModelElement _get = _index.get(it);
                  JsonGenerator _write = ModelSave.this.write(_writeStartObject, _get);
                  _write.writeEnd();
                }
              }
            };
            IterableExtensions.forEach(_value, _function);
            gen.writeEnd();
          }
        }
      };
      IterableExtensions.<ListProperty<?>>forEach(_listChildren, _function_3);
      List<Property<?>> _children = element.getChildren();
      Iterable<Property<?>> _plus_2 = Iterables.<Property<?>>concat(remainingChildren, _children);
      final Procedure1<Property<?>> _function_4 = new Procedure1<Property<?>>() {
        public void apply(final Property<?> it) {
          Object _value = it.getValue();
          boolean _notEquals = (!Objects.equal(_value, null));
          if (_notEquals) {
            String _name = it.getName();
            JsonGenerator _writeStartObject = gen.writeStartObject(_name);
            Map<Object,ModelElement> _index = ModelSave.this.model.getIndex();
            Object _value_1 = it.getValue();
            ModelElement _get = _index.get(_value_1);
            JsonGenerator _write = ModelSave.this.write(_writeStartObject, _get);
            _write.writeEnd();
          }
        }
      };
      IterableExtensions.<Property<?>>forEach(_plus_2, _function_4);
      List<Property<?>> _properties = element.getProperties();
      final Procedure1<Property<?>> _function_5 = new Procedure1<Property<?>>() {
        public void apply(final Property<?> it) {
          Class<?> _type = element.getType(it);
          ModelSave.this.write(gen, it, _type);
        }
      };
      IterableExtensions.<Property<?>>forEach(_properties, _function_5);
      List<ListProperty<?>> _listProperties = element.getListProperties();
      final Procedure1<ListProperty<?>> _function_6 = new Procedure1<ListProperty<?>>() {
        public void apply(final ListProperty<?> it) {
          Class<?> _type = element.getType(it);
          ModelSave.this.write(gen, it, _type);
        }
      };
      IterableExtensions.<ListProperty<?>>forEach(_listProperties, _function_6);
    }
    return gen;
  }
  
  protected JsonGenerator write(final JsonGenerator gen, final Property<?> property, final Class<?> propertyType) {
    JsonGenerator _xifexpression = null;
    Object _value = property.getValue();
    boolean _notEquals = (!Objects.equal(_value, null));
    if (_notEquals) {
      JsonGenerator _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(propertyType,String.class)) {
          _matched=true;
          String _name = property.getName();
          String _value_1 = ((StringProperty) property).getValue();
          _switchResult = gen.write(_name, _value_1);
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Double.class)) {
          _matched=true;
          String _name_1 = property.getName();
          double _doubleValue = ((DoubleProperty) property).doubleValue();
          _switchResult = gen.write(_name_1, _doubleValue);
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Float.class)) {
          _matched=true;
          String _name_2 = property.getName();
          float _floatValue = ((FloatProperty) property).floatValue();
          _switchResult = gen.write(_name_2, _floatValue);
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Long.class)) {
          _matched=true;
          String _name_3 = property.getName();
          long _longValue = ((LongProperty) property).longValue();
          _switchResult = gen.write(_name_3, _longValue);
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Integer.class)) {
          _matched=true;
          String _name_4 = property.getName();
          int _intValue = ((IntegerProperty) property).intValue();
          _switchResult = gen.write(_name_4, _intValue);
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Boolean.class)) {
          _matched=true;
          String _name_5 = property.getName();
          Boolean _value_2 = ((BooleanProperty) property).getValue();
          boolean _booleanValue = _value_2.booleanValue();
          _switchResult = gen.write(_name_5, _booleanValue);
        }
      }
      if (!_matched) {
        boolean _isAssignableFrom = Enum.class.isAssignableFrom(propertyType);
        if (_isAssignableFrom) {
          _matched=true;
          String _name_6 = property.getName();
          Object _value_3 = property.getValue();
          String _string = _value_3.toString();
          _switchResult = gen.write(_name_6, _string);
        }
      }
      if (!_matched) {
        String _name_7 = property.getName();
        Map<Object,ModelElement> _index = this.model.getIndex();
        Object _value_4 = property.getValue();
        ModelElement _get = _index.get(_value_4);
        String _get_1 = this.idMap.get(_get);
        _switchResult = gen.write(_name_7, _get_1);
      }
      _xifexpression = _switchResult;
    }
    return _xifexpression;
  }
  
  protected JsonGenerator write(final JsonGenerator gen, final ListProperty<?> property, final Class<?> propertyType) {
    JsonGenerator _xblockexpression = null;
    {
      String _name = property.getName();
      gen.writeStartArray(_name);
      ObservableList<?> _value = property.getValue();
      for (final Object value : _value) {
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(propertyType,String.class)) {
            _matched=true;
            gen.write(((String) value));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Double.class)) {
            _matched=true;
            double _doubleValue = ((Double) value).doubleValue();
            gen.write(_doubleValue);
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Float.class)) {
            _matched=true;
            float _floatValue = ((Float) value).floatValue();
            gen.write(_floatValue);
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Long.class)) {
            _matched=true;
            long _longValue = ((Long) value).longValue();
            gen.write(_longValue);
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Integer.class)) {
            _matched=true;
            int _intValue = ((Integer) value).intValue();
            gen.write(_intValue);
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Boolean.class)) {
            _matched=true;
            boolean _booleanValue = ((Boolean) value).booleanValue();
            gen.write(_booleanValue);
          }
        }
        if (!_matched) {
          boolean _isAssignableFrom = Enum.class.isAssignableFrom(propertyType);
          if (_isAssignableFrom) {
            _matched=true;
            String _string = value.toString();
            gen.write(_string);
          }
        }
        if (!_matched) {
          Map<Object,ModelElement> _index = this.model.getIndex();
          ObservableList<?> _value_1 = property.getValue();
          ModelElement _get = _index.get(_value_1);
          String _get_1 = this.idMap.get(_get);
          gen.write(_get_1);
        }
      }
      _xblockexpression = gen.writeEnd();
    }
    return _xblockexpression;
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelSave");
    ;
}
