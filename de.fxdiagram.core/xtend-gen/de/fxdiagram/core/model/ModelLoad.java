package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.model.CrossRefData;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelFactory;
import de.fxdiagram.core.model.ModelPersistence;
import de.fxdiagram.core.model.ParseException;
import java.io.Reader;
import java.lang.reflect.Constructor;
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
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@Logging
@SuppressWarnings("all")
public class ModelLoad {
  private ModelFactory modelFactory;
  
  private Map<String,Object> idMap;
  
  private List<CrossRefData> crossRefs;
  
  public Object load(final Reader in) {
    ModelFactory _modelFactory = new ModelFactory();
    this.modelFactory = _modelFactory;
    ArrayList<CrossRefData> _newArrayList = CollectionLiterals.<CrossRefData>newArrayList();
    this.crossRefs = _newArrayList;
    HashMap<String,Object> _newHashMap = CollectionLiterals.<String, Object>newHashMap();
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
    try {
      Object _xblockexpression = null;
      {
        final JsonObject jsonConstructor = jsonObject.getJsonObject("__constructor");
        final String className = jsonConstructor.getString("__class");
        final ArrayList<Object> params = CollectionLiterals.<Object>newArrayList();
        boolean _containsKey = jsonConstructor.containsKey("__params");
        if (_containsKey) {
          JsonArray _jsonArray = jsonConstructor.getJsonArray("__params");
          final Iterable<JsonObject> jsonParams = Iterables.<JsonObject>filter(_jsonArray, JsonObject.class);
          int _size = IterableExtensions.size(jsonParams);
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
          for (final Integer i : _doubleDotLessThan) {
            JsonObject _get = ((JsonObject[])Conversions.unwrapArray(jsonParams, JsonObject.class))[(i).intValue()];
            Object _readNode = this.readNode(_get, ((currentID + "/__params.") + i));
            params.add(_readNode);
          }
        }
        final Class<?> clazz = Class.forName(className);
        final Function1<Object,Class<?>> _function = new Function1<Object,Class<?>>() {
          public Class<?> apply(final Object it) {
            return it.getClass();
          }
        };
        final List<Class<?>> paramTypes = ListExtensions.<Object, Class<?>>map(params, _function);
        final Constructor<?> constructor = ModelPersistence.findConstructor(clazz, paramTypes);
        Object _xifexpression = null;
        boolean _equals = Objects.equal(constructor, null);
        if (_equals) {
          Class<?> _xblockexpression_1 = null;
          {
            final Function1<Class<?>,String> _function_1 = new Function1<Class<?>,String>() {
              public String apply(final Class<?> it) {
                return it.getSimpleName();
              }
            };
            List<String> _map = ListExtensions.<Class<?>, String>map(paramTypes, _function_1);
            String _join = IterableExtensions.join(_map, ",");
            String _plus = ((("Couldn\'t find compatible constructor " + className) + "(") + _join);
            String _plus_1 = (_plus + ")");
            ModelLoad.LOG.warning(_plus_1);
            _xblockexpression_1 = clazz;
          }
          _xifexpression = _xblockexpression_1;
        } else {
          Object _xblockexpression_2 = null;
          {
            final Object node = constructor.newInstance(((Object[]) ((Object[])Conversions.unwrapArray(params, Object.class))));
            this.idMap.put(currentID, node);
            final ModelElement model = this.modelFactory.createElement(node);
            List<ListProperty<?>> _listChildren = model.getListChildren();
            final Procedure1<ListProperty<?>> _function_1 = new Procedure1<ListProperty<?>>() {
              public void apply(final ListProperty<?> it) {
                Class<?> _type = model.getType(it);
                ModelLoad.this.readListProperty(jsonObject, it, _type, currentID);
              }
            };
            IterableExtensions.<ListProperty<?>>forEach(_listChildren, _function_1);
            List<Property<?>> _children = model.getChildren();
            final Procedure1<Property<?>> _function_2 = new Procedure1<Property<?>>() {
              public void apply(final Property<?> it) {
                Class<?> _type = model.getType(it);
                ModelLoad.this.readProperty(jsonObject, it, _type, currentID);
              }
            };
            IterableExtensions.<Property<?>>forEach(_children, _function_2);
            List<Property<?>> _properties = model.getProperties();
            final Procedure1<Property<?>> _function_3 = new Procedure1<Property<?>>() {
              public void apply(final Property<?> it) {
                Class<?> _type = model.getType(it);
                ModelLoad.this.readProperty(jsonObject, it, _type, currentID);
              }
            };
            IterableExtensions.<Property<?>>forEach(_properties, _function_3);
            List<ListProperty<?>> _listProperties = model.getListProperties();
            final Procedure1<ListProperty<?>> _function_4 = new Procedure1<ListProperty<?>>() {
              public void apply(final ListProperty<?> it) {
                Class<?> _type = model.getType(it);
                ModelLoad.this.readListProperty(jsonObject, it, _type, currentID);
              }
            };
            IterableExtensions.<ListProperty<?>>forEach(_listProperties, _function_4);
            _xblockexpression_2 = node;
          }
          _xifexpression = _xblockexpression_2;
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
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
                  String _string_2 = ((JsonString) jsonValue).toString();
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
  
  protected Object resolveCrossReference(final CrossRefData crossRef) {
    try {
      Object _xblockexpression = null;
      {
        String _href = crossRef.getHref();
        final Object crossRefTarget = this.idMap.get(_href);
        Object _xifexpression = null;
        boolean _equals = Objects.equal(crossRefTarget, null);
        if (_equals) {
          String _href_1 = crossRef.getHref();
          String _plus = ("Cannot resolve href \'" + _href_1);
          String _plus_1 = (_plus + "\'");
          throw new ParseException(_plus_1);
        } else {
          Object _xifexpression_1 = null;
          int _index = crossRef.getIndex();
          boolean _equals_1 = (_index == (-1));
          if (_equals_1) {
            Property<?> _property = crossRef.getProperty();
            ((Property<Object>) _property).setValue(crossRefTarget);
          } else {
            Property<?> _property_1 = crossRef.getProperty();
            int _index_1 = crossRef.getIndex();
            _xifexpression_1 = ((ListProperty<Object>) _property_1).set(_index_1, crossRefTarget);
          }
          _xifexpression = _xifexpression_1;
        }
        _xblockexpression = _xifexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelLoad");
    ;
}
