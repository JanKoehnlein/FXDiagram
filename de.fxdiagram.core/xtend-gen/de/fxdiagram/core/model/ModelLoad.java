package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.model.CrossRefData;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelFactory;
import de.fxdiagram.core.model.ParseException;
import java.io.Reader;
import java.math.BigDecimal;
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
import javafx.scene.Node;
import javax.json.Json;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

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
    final JsonParser parser = Json.createParser(in);
    boolean _hasNext = parser.hasNext();
    if (_hasNext) {
      this.consume(parser, JsonParser.Event.START_OBJECT);
      final Object node = this.parseNode(parser, "");
      for (final CrossRefData crossRef : this.crossRefs) {
        {
          String _href = crossRef.getHref();
          final Object crossRefTarget = this.idMap.get(_href);
          boolean _equals = Objects.equal(crossRefTarget, null);
          if (_equals) {
            String _href_1 = crossRef.getHref();
            String _plus = ("Cannot resolve href \'" + _href_1);
            String _plus_1 = (_plus + "\'");
            ModelLoad.LOG.severe(_plus_1);
          } else {
            int _index = crossRef.getIndex();
            boolean _equals_1 = (_index == (-1));
            if (_equals_1) {
              Property<? extends Object> _property = crossRef.getProperty();
              ((Property<Object>) _property).setValue(crossRefTarget);
            } else {
              Property<? extends Object> _property_1 = crossRef.getProperty();
              int _index_1 = crossRef.getIndex();
              ((ListProperty<Object>) _property_1).set(_index_1, crossRefTarget);
            }
          }
        }
      }
      return node;
    }
    return null;
  }
  
  protected Object parseNode(final JsonParser it, final String currentID) {
    try {
      this.consume(it, JsonParser.Event.KEY_NAME);
      String _string = it.getString();
      boolean _notEquals = (!Objects.equal(_string, "class"));
      if (_notEquals) {
        ParseException _parseException = new ParseException("Error parsing JSON file");
        throw _parseException;
      }
      this.consume(it, JsonParser.Event.VALUE_STRING);
      String _string_1 = it.getString();
      final Class<? extends Object> clazz = Class.forName(_string_1);
      final Object node = clazz.newInstance();
      this.idMap.put(currentID, node);
      final ModelElement model = this.modelFactory.createElement(node);
      boolean _hasNext = it.hasNext();
      boolean _while = _hasNext;
      while (_while) {
        {
          final JsonParser.Event event = it.next();
          boolean _matched = false;
          if (!_matched) {
            if (Objects.equal(event,JsonParser.Event.KEY_NAME)) {
              _matched=true;
              String _string_2 = it.getString();
              this.parseProperty(it, node, model, _string_2, currentID);
            }
          }
          if (!_matched) {
            if (Objects.equal(event,JsonParser.Event.END_OBJECT)) {
              _matched=true;
              return node;
            }
          }
          if (!_matched) {
            String _string_3 = event.toString();
            String _plus = ("Invalid token " + _string_3);
            String _plus_1 = (_plus + " at ");
            JsonLocation _location = it.getLocation();
            String _plus_2 = (_plus_1 + _location);
            ParseException _parseException_1 = new ParseException(_plus_2);
            throw _parseException_1;
          }
        }
        boolean _hasNext_1 = it.hasNext();
        _while = _hasNext_1;
      }
      return node;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected void parseProperty(final JsonParser it, final Object node, final ModelElement model, final String propertyName, final String currentID) {
    try {
      List<Property<? extends Object>> _properties = model.getProperties();
      List<Property<? extends Object>> _children = model.getChildren();
      Iterable<Property<? extends Object>> _plus = Iterables.<Property<? extends Object>>concat(_properties, _children);
      final Function1<Property<? extends Object>,Boolean> _function = new Function1<Property<? extends Object>,Boolean>() {
        public Boolean apply(final Property<? extends Object> it) {
          String _name = it.getName();
          boolean _equals = Objects.equal(_name, propertyName);
          return Boolean.valueOf(_equals);
        }
      };
      final Property<? extends Object> property = IterableExtensions.<Property<? extends Object>>findFirst(_plus, _function);
      boolean _notEquals = (!Objects.equal(property, null));
      if (_notEquals) {
        Class<? extends Object> _type = model.getType(property);
        this.parseValue(it, node, property, _type, currentID);
      } else {
        List<ListProperty<? extends Object>> _listProperties = model.getListProperties();
        List<ListProperty<? extends Object>> _listChildren = model.getListChildren();
        Iterable<ListProperty<? extends Object>> _plus_1 = Iterables.<ListProperty<? extends Object>>concat(_listProperties, _listChildren);
        final Function1<ListProperty<? extends Object>,Boolean> _function_1 = new Function1<ListProperty<? extends Object>,Boolean>() {
          public Boolean apply(final ListProperty<? extends Object> it) {
            String _name = it.getName();
            boolean _equals = Objects.equal(_name, propertyName);
            return Boolean.valueOf(_equals);
          }
        };
        final ListProperty<? extends Object> listProperty = IterableExtensions.<ListProperty<? extends Object>>findFirst(_plus_1, _function_1);
        boolean _equals = Objects.equal(listProperty, null);
        if (_equals) {
          Class<? extends Object> _class = node.getClass();
          String _name = _class.getName();
          String _plus_2 = ((("Cannot find property \'" + propertyName) + "\' in class ") + _name);
          String _plus_3 = (_plus_2 + " at ");
          JsonLocation _location = it.getLocation();
          String _plus_4 = (_plus_3 + _location);
          ParseException _parseException = new ParseException(_plus_4);
          throw _parseException;
        } else {
          Class<? extends Object> _type_1 = model.getType(listProperty);
          this.parseListValue(it, node, listProperty, _type_1, currentID);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void parseListValue(final JsonParser it, final Object node, final ListProperty<? extends Object> property, final Class<? extends Object> componentType, final String currentID) {
    try {
      this.consume(it, JsonParser.Event.START_ARRAY);
      int index = 0;
      boolean _hasNext = it.hasNext();
      boolean _while = _hasNext;
      while (_while) {
        {
          final JsonParser.Event event = it.next();
          boolean _matched = false;
          if (!_matched) {
            if (Objects.equal(componentType,String.class)) {
              _matched=true;
              this.consume(it, JsonParser.Event.VALUE_STRING);
              String _string = it.getString();
              ((List<String>) property).add(_string);
            }
          }
          if (!_matched) {
            if (Objects.equal(componentType,Double.class)) {
              _matched=true;
              this.consume(it, JsonParser.Event.VALUE_NUMBER);
              BigDecimal _bigDecimal = it.getBigDecimal();
              double _doubleValue = _bigDecimal.doubleValue();
              ((List<Double>) property).add(Double.valueOf(_doubleValue));
            }
          }
          if (!_matched) {
            if (Objects.equal(componentType,Float.class)) {
              _matched=true;
              this.consume(it, JsonParser.Event.VALUE_NUMBER);
              BigDecimal _bigDecimal_1 = it.getBigDecimal();
              float _floatValue = _bigDecimal_1.floatValue();
              ((List<Float>) property).add(Float.valueOf(_floatValue));
            }
          }
          if (!_matched) {
            if (Objects.equal(componentType,Long.class)) {
              _matched=true;
              this.consume(it, JsonParser.Event.VALUE_NUMBER);
              long _long = it.getLong();
              ((List<Long>) property).add(Long.valueOf(_long));
            }
          }
          if (!_matched) {
            if (Objects.equal(componentType,Integer.class)) {
              _matched=true;
              this.consume(it, JsonParser.Event.VALUE_NUMBER);
              int _int = it.getInt();
              ((List<Integer>) property).add(Integer.valueOf(_int));
            }
          }
          if (!_matched) {
            if (Objects.equal(componentType,Boolean.class)) {
              _matched=true;
              boolean _switchResult_1 = false;
              boolean _matched_1 = false;
              if (!_matched_1) {
                if (Objects.equal(event,JsonParser.Event.VALUE_TRUE)) {
                  _matched_1=true;
                  _switchResult_1 = true;
                }
              }
              if (!_matched_1) {
                if (Objects.equal(event,JsonParser.Event.VALUE_FALSE)) {
                  _matched_1=true;
                  _switchResult_1 = false;
                }
              }
              if (!_matched_1) {
                String _name = event.name();
                String _plus = ("Expected boolean value but got " + _name);
                String _plus_1 = (_plus + " ");
                JsonLocation _location = it.getLocation();
                String _plus_2 = (_plus_1 + _location);
                ParseException _parseException = new ParseException(_plus_2);
                throw _parseException;
              }
              ((List<Boolean>) property).add(Boolean.valueOf(_switchResult_1));
            }
          }
          if (!_matched) {
            boolean _isAssignableFrom = Enum.class.isAssignableFrom(componentType);
            if (_isAssignableFrom) {
              _matched=true;
              boolean _matched_2 = false;
              if (!_matched_2) {
                if (Objects.equal(event,JsonParser.Event.VALUE_NULL)) {
                  _matched_2=true;
                }
              }
              if (!_matched_2) {
                if (Objects.equal(event,JsonParser.Event.VALUE_STRING)) {
                  _matched_2=true;
                  String _string_1 = it.getString();
                  Enum _valueOf = Enum.valueOf(((Class<? extends Enum>) componentType), _string_1);
                  ((List<Object>) property).add(_valueOf);
                }
              }
              if (!_matched_2) {
                JsonLocation _location_1 = it.getLocation();
                String _plus_3 = ((("Expected enum value but got " + event) + " at ") + _location_1);
                ParseException _parseException_1 = new ParseException(_plus_3);
                throw _parseException_1;
              }
            }
          }
          if (!_matched) {
            boolean _matched_3 = false;
            if (!_matched_3) {
              if (Objects.equal(event,JsonParser.Event.VALUE_NULL)) {
                _matched_3=true;
                Iterables.<Node>addAll(((List<Node>) property), null);
              }
            }
            if (!_matched_3) {
              if (Objects.equal(event,JsonParser.Event.VALUE_STRING)) {
                _matched_3=true;
                String _string_2 = it.getString();
                CrossRefData _crossRefData = new CrossRefData(_string_2, property, index);
                this.crossRefs.add(_crossRefData);
              }
            }
            if (!_matched_3) {
              if (Objects.equal(event,JsonParser.Event.START_OBJECT)) {
                _matched_3=true;
                String _name_1 = property.getName();
                String _plus_4 = ((currentID + "/") + _name_1);
                String _plus_5 = (_plus_4 + ".");
                String _plus_6 = (_plus_5 + Integer.valueOf(index));
                Object _parseNode = this.parseNode(it, _plus_6);
                ((List<Object>) property).add(_parseNode);
              }
            }
            if (!_matched_3) {
              if (Objects.equal(event,JsonParser.Event.END_ARRAY)) {
                _matched_3=true;
                return;
              }
            }
            if (!_matched_3) {
              JsonLocation _location_2 = it.getLocation();
              String _plus_7 = ((("Expected object but got " + event) + " at ") + _location_2);
              ParseException _parseException_2 = new ParseException(_plus_7);
              throw _parseException_2;
            }
          }
          index = (index + 1);
        }
        boolean _hasNext_1 = it.hasNext();
        _while = _hasNext_1;
      }
      return;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void parseValue(final JsonParser it, final Object node, final Property<? extends Object> property, final Class<? extends Object> propertyType, final String currentID) {
    try {
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(propertyType,String.class)) {
          _matched=true;
          this.consume(it, JsonParser.Event.VALUE_STRING);
          String _string = it.getString();
          ((StringProperty) property).setValue(_string);
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Double.class)) {
          _matched=true;
          this.consume(it, JsonParser.Event.VALUE_NUMBER);
          BigDecimal _bigDecimal = it.getBigDecimal();
          double _doubleValue = _bigDecimal.doubleValue();
          ((DoubleProperty) property).setValue(Double.valueOf(_doubleValue));
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Float.class)) {
          _matched=true;
          this.consume(it, JsonParser.Event.VALUE_NUMBER);
          BigDecimal _bigDecimal_1 = it.getBigDecimal();
          float _floatValue = _bigDecimal_1.floatValue();
          ((FloatProperty) property).setValue(Float.valueOf(_floatValue));
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Long.class)) {
          _matched=true;
          this.consume(it, JsonParser.Event.VALUE_NUMBER);
          long _long = it.getLong();
          ((LongProperty) property).setValue(Long.valueOf(_long));
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Integer.class)) {
          _matched=true;
          this.consume(it, JsonParser.Event.VALUE_NUMBER);
          int _int = it.getInt();
          ((IntegerProperty) property).setValue(Integer.valueOf(_int));
        }
      }
      if (!_matched) {
        if (Objects.equal(propertyType,Boolean.class)) {
          _matched=true;
          boolean _switchResult_1 = false;
          JsonParser.Event _next = it.next();
          final JsonParser.Event event = _next;
          boolean _matched_1 = false;
          if (!_matched_1) {
            if (Objects.equal(event,JsonParser.Event.VALUE_TRUE)) {
              _matched_1=true;
              _switchResult_1 = true;
            }
          }
          if (!_matched_1) {
            if (Objects.equal(event,JsonParser.Event.VALUE_FALSE)) {
              _matched_1=true;
              _switchResult_1 = false;
            }
          }
          if (!_matched_1) {
            String _name = event.name();
            String _plus = ("Expected boolean value but got " + _name);
            String _plus_1 = (_plus + " ");
            JsonLocation _location = it.getLocation();
            String _plus_2 = (_plus_1 + _location);
            ParseException _parseException = new ParseException(_plus_2);
            throw _parseException;
          }
          ((BooleanProperty) property).setValue(Boolean.valueOf(_switchResult_1));
        }
      }
      if (!_matched) {
        boolean _isAssignableFrom = Enum.class.isAssignableFrom(propertyType);
        if (_isAssignableFrom) {
          _matched=true;
          JsonParser.Event _next_1 = it.next();
          final JsonParser.Event event_1 = _next_1;
          boolean _matched_2 = false;
          if (!_matched_2) {
            if (Objects.equal(event_1,JsonParser.Event.VALUE_NULL)) {
              _matched_2=true;
            }
          }
          if (!_matched_2) {
            if (Objects.equal(event_1,JsonParser.Event.VALUE_STRING)) {
              _matched_2=true;
              String _string_1 = it.getString();
              Enum _valueOf = Enum.valueOf(((Class<? extends Enum>) propertyType), _string_1);
              ((Property<Object>) property).setValue(_valueOf);
            }
          }
          if (!_matched_2) {
            JsonLocation _location_1 = it.getLocation();
            String _plus_3 = ((("Expected enum value but got " + event_1) + " at ") + _location_1);
            ParseException _parseException_1 = new ParseException(_plus_3);
            throw _parseException_1;
          }
        }
      }
      if (!_matched) {
        JsonParser.Event _next_2 = it.next();
        final JsonParser.Event event_2 = _next_2;
        boolean _matched_3 = false;
        if (!_matched_3) {
          if (Objects.equal(event_2,JsonParser.Event.VALUE_NULL)) {
            _matched_3=true;
          }
        }
        if (!_matched_3) {
          if (Objects.equal(event_2,JsonParser.Event.VALUE_STRING)) {
            _matched_3=true;
            String _string_2 = it.getString();
            CrossRefData _crossRefData = new CrossRefData(_string_2, property, (-1));
            this.crossRefs.add(_crossRefData);
          }
        }
        if (!_matched_3) {
          if (Objects.equal(event_2,JsonParser.Event.START_OBJECT)) {
            _matched_3=true;
            String _name_1 = property.getName();
            String _plus_4 = ((currentID + "/") + _name_1);
            Object _parseNode = this.parseNode(it, _plus_4);
            ((Property<Object>) property).setValue(_parseNode);
          }
        }
        if (!_matched_3) {
          JsonLocation _location_2 = it.getLocation();
          String _plus_5 = ((("Expected object but got " + event_2) + " at ") + _location_2);
          ParseException _parseException_2 = new ParseException(_plus_5);
          throw _parseException_2;
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected void consume(final JsonParser parser, final JsonParser.Event event) {
    try {
      final JsonParser.Event next = parser.next();
      boolean _notEquals = (!Objects.equal(next, event));
      if (_notEquals) {
        JsonLocation _location = parser.getLocation();
        String _plus = ((((("Expected " + event) + " but got ") + next) + " at ") + _location);
        ParseException _parseException = new ParseException(_plus);
        throw _parseException;
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelLoad");
    ;
}
