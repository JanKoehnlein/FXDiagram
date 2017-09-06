package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.core.model.CrossRefData;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelFactory;
import de.fxdiagram.core.model.ModelRepairer;
import de.fxdiagram.core.model.ParseException;
import de.fxdiagram.core.model.XModelProvider;
import de.fxdiagram.core.tools.actions.LoadAction;
import java.io.Reader;
import java.util.List;
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
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Functionality of a {@link LoadAction}.
 */
@Logging
@SuppressWarnings("all")
public class ModelLoad {
  private ModelFactory modelFactory;
  
  private Map<String, ModelElement> idMap;
  
  private List<CrossRefData> crossRefs;
  
  public Object load(final Reader in) {
    ModelFactory _modelFactory = new ModelFactory();
    this.modelFactory = _modelFactory;
    this.crossRefs = CollectionLiterals.<CrossRefData>newArrayList();
    this.idMap = CollectionLiterals.<String, ModelElement>newHashMap();
    final JsonReader reader = Json.createReader(in);
    final JsonObject jsonObject = reader.readObject();
    final Object rootNode = this.readNode(jsonObject, "");
    final Consumer<CrossRefData> _function = (CrossRefData it) -> {
      this.resolveCrossReference(it);
    };
    this.crossRefs.forEach(_function);
    new ModelRepairer().repair(rootNode);
    final Function1<ModelElement, Object> _function_1 = (ModelElement it) -> {
      return it.getNode();
    };
    final Consumer<XModelProvider> _function_2 = (XModelProvider it) -> {
      it.postLoad();
    };
    Iterables.<XModelProvider>filter(IterableExtensions.<ModelElement, Object>map(this.idMap.values(), _function_1), XModelProvider.class).forEach(_function_2);
    return rootNode;
  }
  
  protected Object readNode(final JsonObject jsonObject, final String currentID) {
    Object _xblockexpression = null;
    {
      final String className = jsonObject.getString("__class");
      final ModelElement model = this.modelFactory.createElement(className);
      this.idMap.put(currentID, model);
      final Consumer<Property<?>> _function = (Property<?> it) -> {
        this.readProperty(jsonObject, it, model.getType(it), currentID);
      };
      model.getProperties().forEach(_function);
      final Consumer<ListProperty<?>> _function_1 = (ListProperty<?> it) -> {
        this.readListProperty(jsonObject, it, model.getType(it), currentID);
      };
      model.getListProperties().forEach(_function_1);
      _xblockexpression = model.getNode();
    }
    return _xblockexpression;
  }
  
  protected Boolean readProperty(final JsonObject it, final Property<?> property, final Class<?> propertyType, final String currentID) {
    try {
      boolean _xifexpression = false;
      boolean _containsKey = it.containsKey(property.getName());
      if (_containsKey) {
        boolean _switchResult = false;
        boolean _matched = false;
        if (Objects.equal(propertyType, String.class)) {
          _matched=true;
          ((StringProperty) property).setValue(it.getString(property.getName()));
        }
        if (!_matched) {
          if (Objects.equal(propertyType, Double.class)) {
            _matched=true;
            ((DoubleProperty) property).setValue(Double.valueOf(it.getJsonNumber(property.getName()).doubleValue()));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType, Float.class)) {
            _matched=true;
            ((FloatProperty) property).setValue(Double.valueOf(it.getJsonNumber(property.getName()).doubleValue()));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType, Long.class)) {
            _matched=true;
            ((LongProperty) property).setValue(Long.valueOf(it.getJsonNumber(property.getName()).longValue()));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType, Integer.class)) {
            _matched=true;
            ((IntegerProperty) property).setValue(Integer.valueOf(it.getInt(property.getName())));
          }
        }
        if (!_matched) {
          if (Objects.equal(propertyType, Boolean.class)) {
            _matched=true;
            ((BooleanProperty) property).setValue(Boolean.valueOf(it.getBoolean(property.getName())));
          }
        }
        if (!_matched) {
          boolean _isAssignableFrom = Enum.class.isAssignableFrom(propertyType);
          if (_isAssignableFrom) {
            _matched=true;
            ((Property<Object>) property).setValue(Enum.<Enum>valueOf(((Class<Enum>) propertyType), it.getString(property.getName())));
          }
        }
        if (!_matched) {
          boolean _xblockexpression = false;
          {
            final JsonValue value = it.get(property.getName());
            boolean _switchResult_1 = false;
            JsonValue.ValueType _valueType = value.getValueType();
            if (_valueType != null) {
              switch (_valueType) {
                case STRING:
                  boolean _xblockexpression_1 = false;
                  {
                    String _string = it.getString(property.getName());
                    final CrossRefData crossRefData = new CrossRefData(_string, property, (-1));
                    _xblockexpression_1 = this.crossRefs.add(crossRefData);
                  }
                  _switchResult_1 = _xblockexpression_1;
                  break;
                case OBJECT:
                  String _name = property.getName();
                  String _plus = ((currentID + "/") + _name);
                  ((Property<Object>) property).setValue(this.readNode(((JsonObject) value), _plus));
                  break;
                default:
                  throw new ParseException(("Expected object but got " + value));
              }
            } else {
              throw new ParseException(("Expected object but got " + value));
            }
            _xblockexpression = _switchResult_1;
          }
          _switchResult = _xblockexpression;
        }
        _xifexpression = _switchResult;
      }
      return Boolean.valueOf(_xifexpression);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected void readListProperty(final JsonObject it, final ListProperty<?> property, final Class<?> componentType, final String currentID) {
    try {
      boolean _containsKey = it.containsKey(property.getName());
      if (_containsKey) {
        final JsonArray jsonValues = it.getJsonArray(property.getName());
        int _size = jsonValues.size();
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
        for (final Integer i : _doubleDotLessThan) {
          {
            final JsonValue jsonValue = jsonValues.get((i).intValue());
            boolean _matched = false;
            if (Objects.equal(componentType, String.class)) {
              _matched=true;
              String _string = ((JsonString) jsonValue).getString();
              ((List<String>) property).add(_string);
            }
            if (!_matched) {
              if (Objects.equal(componentType, Double.class)) {
                _matched=true;
                double _doubleValue = ((JsonNumber) jsonValue).doubleValue();
                ((List<Double>) property).add(Double.valueOf(_doubleValue));
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType, Float.class)) {
                _matched=true;
                double _doubleValue_1 = ((JsonNumber) jsonValue).doubleValue();
                ((List<Float>) property).add(Float.valueOf(((float) _doubleValue_1)));
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType, Long.class)) {
                _matched=true;
                long _longValue = ((JsonNumber) jsonValue).longValue();
                ((List<Long>) property).add(Long.valueOf(_longValue));
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType, Integer.class)) {
                _matched=true;
                int _intValue = ((JsonNumber) jsonValue).intValue();
                ((List<Integer>) property).add(Integer.valueOf(_intValue));
              }
            }
            if (!_matched) {
              if (Objects.equal(componentType, Boolean.class)) {
                _matched=true;
                boolean _equals = Objects.equal(jsonValue, JsonValue.TRUE);
                ((List<Boolean>) property).add(Boolean.valueOf(_equals));
              }
            }
            if (!_matched) {
              boolean _isAssignableFrom = Enum.class.isAssignableFrom(componentType);
              if (_isAssignableFrom) {
                _matched=true;
                Enum _valueOf = Enum.<Enum>valueOf(((Class<Enum>) componentType), ((JsonString) jsonValue).toString());
                ((ListProperty<Object>) property).add(_valueOf);
              }
            }
            if (!_matched) {
              JsonValue.ValueType _valueType = jsonValue.getValueType();
              if (_valueType != null) {
                switch (_valueType) {
                  case STRING:
                    String _string_1 = ((JsonString) jsonValue).getString();
                    final CrossRefData crossRefData = new CrossRefData(_string_1, property, (i).intValue());
                    this.crossRefs.add(crossRefData);
                    break;
                  case OBJECT:
                    String _name = property.getName();
                    String _plus = ((currentID + "/") + _name);
                    String _plus_1 = (_plus + ".");
                    String _plus_2 = (_plus_1 + i);
                    Object _readNode = this.readNode(((JsonObject) jsonValue), _plus_2);
                    ((List<Object>) property).add(_readNode);
                    break;
                  default:
                    throw new ParseException(("Expected object but got " + jsonValue));
                }
              } else {
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
      ModelElement _get = this.idMap.get(crossRef.getHref());
      Object _node = null;
      if (_get!=null) {
        _node=_get.getNode();
      }
      final Object crossRefTarget = _node;
      boolean _equals = Objects.equal(crossRefTarget, null);
      if (_equals) {
        String _href = crossRef.getHref();
        String _plus = ("Cannot resolve href \'" + _href);
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
          ((ListProperty<Object>) _property_1).add(crossRef.getIndex(), crossRefTarget);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.core.model.ModelLoad");
    ;
}
