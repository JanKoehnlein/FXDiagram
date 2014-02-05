package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import de.fxdiagram.core.model.Model;
import de.fxdiagram.core.model.ModelElement;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

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
      List<Property<? extends Object>> _children = it.getChildren();
      for (final Property<? extends Object> property : _children) {
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
      List<ListProperty<? extends Object>> _listChildren = it.getListChildren();
      for (final ListProperty<? extends Object> property_1 : _listChildren) {
        {
          int i = 0;
          ObservableList<? extends Object> _value_2 = property_1.getValue();
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
      Object _node = element.getNode();
      Class<? extends Object> _class = _node.getClass();
      String _canonicalName = _class.getCanonicalName();
      gen.write("class", _canonicalName);
      List<Property<? extends Object>> _properties = element.getProperties();
      final Procedure1<Property<? extends Object>> _function = new Procedure1<Property<? extends Object>>() {
        public void apply(final Property<? extends Object> it) {
          Class<? extends Object> _type = element.getType(it);
          ModelSave.this.write(gen, it, _type);
        }
      };
      IterableExtensions.<Property<? extends Object>>forEach(_properties, _function);
      List<ListProperty<? extends Object>> _listProperties = element.getListProperties();
      final Procedure1<ListProperty<? extends Object>> _function_1 = new Procedure1<ListProperty<? extends Object>>() {
        public void apply(final ListProperty<? extends Object> it) {
          Class<? extends Object> _type = element.getType(it);
          ModelSave.this.write(gen, it, _type);
        }
      };
      IterableExtensions.<ListProperty<? extends Object>>forEach(_listProperties, _function_1);
      List<Property<? extends Object>> _children = element.getChildren();
      final Procedure1<Property<? extends Object>> _function_2 = new Procedure1<Property<? extends Object>>() {
        public void apply(final Property<? extends Object> it) {
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
      IterableExtensions.<Property<? extends Object>>forEach(_children, _function_2);
      List<ListProperty<? extends Object>> _listChildren = element.getListChildren();
      final Procedure1<ListProperty<? extends Object>> _function_3 = new Procedure1<ListProperty<? extends Object>>() {
        public void apply(final ListProperty<? extends Object> it) {
          boolean _isEmpty = it.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            String _name = it.getName();
            gen.writeStartArray(_name);
            ObservableList<? extends Object> _value = it.getValue();
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
      IterableExtensions.<ListProperty<? extends Object>>forEach(_listChildren, _function_3);
    }
    return gen;
  }
  
  protected JsonGenerator write(final JsonGenerator gen, final Property<? extends Object> property, final Class<? extends Object> propertyType) {
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
          JsonGenerator _write = gen.write(_name, _value_1);
          _switchResult = _write;
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Double.class)) {
          _matched=true;
          String _name_1 = property.getName();
          double _doubleValue = ((DoubleProperty) property).doubleValue();
          JsonGenerator _write_1 = gen.write(_name_1, _doubleValue);
          _switchResult = _write_1;
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Float.class)) {
          _matched=true;
          String _name_2 = property.getName();
          float _floatValue = ((FloatProperty) property).floatValue();
          JsonGenerator _write_2 = gen.write(_name_2, _floatValue);
          _switchResult = _write_2;
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Long.class)) {
          _matched=true;
          String _name_3 = property.getName();
          long _longValue = ((LongProperty) property).longValue();
          JsonGenerator _write_3 = gen.write(_name_3, _longValue);
          _switchResult = _write_3;
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Integer.class)) {
          _matched=true;
          String _name_4 = property.getName();
          int _intValue = ((IntegerProperty) property).intValue();
          JsonGenerator _write_4 = gen.write(_name_4, _intValue);
          _switchResult = _write_4;
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Boolean.class)) {
          _matched=true;
          String _name_5 = property.getName();
          Boolean _value_2 = ((BooleanProperty) property).getValue();
          boolean _booleanValue = _value_2.booleanValue();
          JsonGenerator _write_5 = gen.write(_name_5, _booleanValue);
          _switchResult = _write_5;
        }
      }
      if (!_matched) {
        boolean _isAssignableFrom = Enum.class.isAssignableFrom(propertyType);
        if (_isAssignableFrom) {
          _matched=true;
          String _name_6 = property.getName();
          Object _value_3 = property.getValue();
          String _string = _value_3.toString();
          JsonGenerator _write_6 = gen.write(_name_6, _string);
          _switchResult = _write_6;
        }
      }
      if (!_matched) {
        String _name_7 = property.getName();
        Map<Object,ModelElement> _index = this.model.getIndex();
        Object _value_4 = property.getValue();
        ModelElement _get = _index.get(_value_4);
        String _get_1 = this.idMap.get(_get);
        JsonGenerator _write_7 = gen.write(_name_7, _get_1);
        _switchResult = _write_7;
      }
      _xifexpression = _switchResult;
    }
    return _xifexpression;
  }
  
  protected JsonGenerator write(final JsonGenerator gen, final ListProperty<? extends Object> property, final Class<? extends Object> propertyType) {
    JsonGenerator _xblockexpression = null;
    {
      String _name = property.getName();
      gen.writeStartArray(_name);
      ObservableList<? extends Object> _value = property.getValue();
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
          ObservableList<? extends Object> _value_1 = property.getValue();
          ModelElement _get = _index.get(_value_1);
          String _get_1 = this.idMap.get(_get);
          gen.write(_get_1);
        }
      }
      JsonGenerator _writeEnd = gen.writeEnd();
      _xblockexpression = (_writeEnd);
    }
    return _xblockexpression;
  }
}
