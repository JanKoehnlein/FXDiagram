package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.model.CrossRefData;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelFactory;
import de.fxdiagram.core.model.ParseException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class ModelLoad {
  private ModelFactory modelFactory;
  
  private Map<String,ModelElement> idMap;
  
  private List<CrossRefData> crossRefs;
  
  public Object load(final Reader in) {
    ModelFactory _modelFactory = new ModelFactory();
    this.modelFactory = _modelFactory;
    ArrayList<CrossRefData> _newArrayList = CollectionLiterals.<CrossRefData>newArrayList();
    this.crossRefs = _newArrayList;
    HashMap<String,ModelElement> _newHashMap = CollectionLiterals.<String, ModelElement>newHashMap();
    this.idMap = _newHashMap;
    final JsonReader reader = Json.createReader(in);
    final JsonObject jsonObject = reader.readObject();
    final Object node = this.readNode(jsonObject, "");
    final Procedure1<CrossRefData> _function = new Procedure1<CrossRefData>() {
      public void apply(final CrossRefData it) {
        ModelLoad.this.resolveCrossReference(it);
      }
    };
    IterableExtensions.<CrossRefData>forEach(this.crossRefs, _function);
    return node;
  }
  
  protected Object readNode(final JsonObject jsonObject, final String currentID) {
    Object _xblockexpression = null;
    {
      final String className = jsonObject.getString("__class");
      final ModelElement model = this.modelFactory.createElement(className);
      this.idMap.put(currentID, model);
      List<? extends Property<?>> _properties = model.getProperties();
      final Procedure1<Property<?>> _function = new Procedure1<Property<?>>() {
        public void apply(final Property<?> it) {
          Class<?> _type = model.getType(it);
          ModelLoad.this.readProperty(jsonObject, it, _type, currentID);
        }
      };
      IterableExtensions.forEach(_properties, _function);
      List<? extends ListProperty<?>> _listProperties = model.getListProperties();
      final Procedure1<ListProperty<?>> _function_1 = new Procedure1<ListProperty<?>>() {
        public void apply(final ListProperty<?> it) {
          Class<?> _type = model.getType(it);
          ModelLoad.this.readListProperty(jsonObject, it, _type, currentID);
        }
      };
      IterableExtensions.forEach(_listProperties, _function_1);
      _xblockexpression = model.getNode();
    }
    return _xblockexpression;
  }
  
  protected Boolean readProperty(final JsonObject it, final Property<?> property, final Class<?> propertyType, final String currentID) {
    try {
      boolean _xifexpression = false;
      String _name = property.getName();
      boolean _containsKey = it.containsKey(_name);
      if (_containsKey) {
        boolean _switchResult = false;
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(propertyType,String.class)) {
            _matched=true;
            String _name_1 = property.getName();
            String _string = it.getString(_name_1);
            ((StringProperty) property).setValue(_string);
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Double.class)) {
            _matched=true;
            String _name_2 = property.getName();
            JsonNumber _jsonNumber = it.getJsonNumber(_name_2);
            double _doubleValue = _jsonNumber.doubleValue();
            ((DoubleProperty) property).setValue(Double.valueOf(_doubleValue));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Float.class)) {
            _matched=true;
            String _name_3 = property.getName();
            JsonNumber _jsonNumber_1 = it.getJsonNumber(_name_3);
            double _doubleValue_1 = _jsonNumber_1.doubleValue();
            ((FloatProperty) property).setValue(Double.valueOf(_doubleValue_1));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Long.class)) {
            _matched=true;
            String _name_4 = property.getName();
            JsonNumber _jsonNumber_2 = it.getJsonNumber(_name_4);
            long _longValue = _jsonNumber_2.longValue();
            ((LongProperty) property).setValue(Long.valueOf(_longValue));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Integer.class)) {
            _matched=true;
            String _name_5 = property.getName();
            int _int = it.getInt(_name_5);
            ((IntegerProperty) property).setValue(Integer.valueOf(_int));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType,Boolean.class)) {
            _matched=true;
            String _name_6 = property.getName();
            boolean _boolean = it.getBoolean(_name_6);
            ((BooleanProperty) property).setValue(Boolean.valueOf(_boolean));
          }
        }
        if (!_matched) {
          boolean _isAssignableFrom = Enum.class.isAssignableFrom(propertyType);
          if (_isAssignableFrom) {
            _matched=true;
            String _name_7 = property.getName();
            String _string_1 = it.getString(_name_7);
            Enum _valueOf = Enum.<Enum>valueOf(((Class<Enum>) propertyType), _string_1);
            ((Property<Object>) property).setValue(_valueOf);
          }
        }
        if (!_matched) {
          boolean _xblockexpression = false;
          {
            String _name_8 = property.getName();
            final JsonValue value = it.get(_name_8);
            boolean _switchResult_1 = false;
            JsonValue.ValueType _valueType = value.getValueType();
            switch (_valueType) {
              case STRING:
                boolean _xblockexpression_1 = false;
                {
                  String _name_9 = property.getName();
                  String _string_2 = it.getString(_name_9);
                  final CrossRefData crossRefData = new CrossRefData(_string_2, property, (-1));
                  _xblockexpression_1 = this.crossRefs.add(crossRefData);
                }
                _switchResult_1 = _xblockexpression_1;
                break;
              case OBJECT:
                String _name_9 = property.getName();
                String _plus = ((currentID + "/") + _name_9);
                Object _readNode = this.readNode(((JsonObject) value), _plus);
                ((Property<Object>) property).setValue(_readNode);
                break;
              default:
                throw new ParseException(("Expected object but got " + value));
            }
            _xblockexpression = _switchResult_1;
          }
          _switchResult = _xblockexpression;
        }
        _xifexpression = _switchResult;
      }
      return _xifexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected void readListProperty(final JsonObject it, final ListProperty<?> property, final Class<?> componentType, final String currentID) {
    try {
      String _name = property.getName();
      boolean _containsKey = it.containsKey(_name);
      if (_containsKey) {
        String _name_1 = property.getName();
        final JsonArray jsonValues = it.getJsonArray(_name_1);
        int _size = jsonValues.size();
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
        for (final Integer i : _doubleDotLessThan) {
          {
            final JsonValue jsonValue = jsonValues.get((i).intValue());
            boolean _matched = false;
            if (!_matched) {
              if (Objects.equal(componentType,String.class)) {
                _matched=true;
                String _string = ((JsonString) jsonValue).toString();
                ((List<String>) property).add(_string);
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType,Double.class)) {
                _matched=true;
                double _doubleValue = ((JsonNumber) jsonValue).doubleValue();
                ((List<Double>) property).add(Double.valueOf(_doubleValue));
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType,Float.class)) {
                _matched=true;
                double _doubleValue_1 = ((JsonNumber) jsonValue).doubleValue();
                ((List<Float>) property).add(Float.valueOf(((float) _doubleValue_1)));
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType,Long.class)) {
                _matched=true;
                long _longValue = ((JsonNumber) jsonValue).longValue();
                ((List<Long>) property).add(Long.valueOf(_longValue));
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType,Integer.class)) {
                _matched=true;
                int _intValue = ((JsonNumber) jsonValue).intValue();
                ((List<Integer>) property).add(Integer.valueOf(_intValue));
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType,Boolean.class)) {
                _matched=true;
                boolean _equals = Objects.equal(jsonValue, JsonValue.TRUE);
                ((List<Boolean>) property).add(Boolean.valueOf(_equals));
              }
            }
            if (!_matched) {
              boolean _isAssignableFrom = Enum.class.isAssignableFrom(componentType);
              if (_isAssignableFrom) {
                _matched=true;
                String _string_1 = ((JsonString) jsonValue).toString();
                Enum _valueOf = Enum.<Enum>valueOf(((Class<Enum>) componentType), _string_1);
                ((ListProperty<Object>) property).add(_valueOf);
              }
            }
            if (!_matched) {
              JsonValue.ValueType _valueType = jsonValue.getValueType();
              switch (_valueType) {
                case STRING:
                  String _string_2 = ((JsonString) jsonValue).getString();
                  final CrossRefData crossRefData = new CrossRefData(_string_2, property, (i).intValue());
                  this.crossRefs.add(crossRefData);
                  break;
                case OBJECT:
                  String _name_2 = property.getName();
                  String _plus = ((currentID + "/") + _name_2);
                  String _plus_1 = (_plus + ".");
                  String _plus_2 = (_plus_1 + i);
                  Object _readNode = this.readNode(((JsonObject) jsonValue), _plus_2);
                  ((List<Object>) property).add(_readNode);
                  break;
                default:
                  throw new ParseException(("Expected object but got " + jsonValue));
              }
            }
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected void resolveCrossReference(final CrossRefData crossRef) {
    try {
      String _href = crossRef.getHref();
      ModelElement _get = this.idMap.get(_href);
      Object _node = null;
      if (_get!=null) {
        _node=_get.getNode();
      }
      final Object crossRefTarget = _node;
      boolean _equals = Objects.equal(crossRefTarget, null);
      if (_equals) {
        String _href_1 = crossRef.getHref();
        String _plus = ("Cannot resolve href \'" + _href_1);
        String _plus_1 = (_plus + "\'");
        throw new ParseException(_plus_1);
      } else {
        int _index = crossRef.getIndex();
        boolean _equals_1 = (_index == (-1));
        if (_equals_1) {
          Property<?> _property = crossRef.getProperty();
          ((Property<Object>) _property).setValue(crossRefTarget);
        } else {
          Property<?> _property_1 = crossRef.getProperty();
          int _index_1 = crossRef.getIndex();
          ((ListProperty<Object>) _property_1).add(_index_1, crossRefTarget);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelLoad");
    ;
}
