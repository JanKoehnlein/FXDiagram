package de.fxdiagram.examples.java;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import de.fxdiagram.examples.java.Property;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class JavaTypeModel {
  private List<Constructor<? extends Object>> constructors;
  
  private List<Method> operations;
  
  private List<Property> properties = new Function0<List<Property>>() {
    public List<Property> apply() {
      ArrayList<Property> _newArrayList = CollectionLiterals.<Property>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private List<Property> references = new Function0<List<Property>>() {
    public List<Property> apply() {
      ArrayList<Property> _newArrayList = CollectionLiterals.<Property>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  private List<Class<? extends Object>> superTypes = new Function0<List<Class<? extends Object>>>() {
    public List<Class<? extends Object>> apply() {
      ArrayList<Class<? extends Object>> _newArrayList = CollectionLiterals.<Class<? extends Object>>newArrayList();
      return _newArrayList;
    }
  }.apply();
  
  public JavaTypeModel(final Class<? extends Object> javaType) {
    Constructor<? extends Object>[] _declaredConstructors = javaType.getDeclaredConstructors();
    final Function1<Constructor<? extends Object>,Boolean> _function = new Function1<Constructor<? extends Object>,Boolean>() {
      public Boolean apply(final Constructor<? extends Object> it) {
        int _modifiers = it.getModifiers();
        boolean _isPublic = Modifier.isPublic(_modifiers);
        return Boolean.valueOf(_isPublic);
      }
    };
    Iterable<Constructor<? extends Object>> _filter = IterableExtensions.<Constructor<? extends Object>>filter(((Iterable<Constructor<? extends Object>>)Conversions.doWrapArray(_declaredConstructors)), _function);
    List<Constructor<? extends Object>> _list = IterableExtensions.<Constructor<? extends Object>>toList(_filter);
    this.constructors = _list;
    final HashMultimap<String,Pair<Class<? extends Object>,Method>> propertyMethods = HashMultimap.<String, Pair<Class<?>,Method>>create();
    Method[] _declaredMethods = javaType.getDeclaredMethods();
    final Function1<Method,Boolean> _function_1 = new Function1<Method,Boolean>() {
      public Boolean apply(final Method it) {
        boolean _and = false;
        int _modifiers = it.getModifiers();
        boolean _isPublic = Modifier.isPublic(_modifiers);
        if (!_isPublic) {
          _and = false;
        } else {
          String _name = it.getName();
          boolean _startsWith = _name.startsWith("impl_");
          boolean _not = (!_startsWith);
          _and = (_isPublic && _not);
        }
        return Boolean.valueOf(_and);
      }
    };
    Iterable<Method> _filter_1 = IterableExtensions.<Method>filter(((Iterable<Method>)Conversions.doWrapArray(_declaredMethods)), _function_1);
    List<Method> _list_1 = IterableExtensions.<Method>toList(_filter_1);
    this.operations = _list_1;
    Method[] _declaredMethods_1 = javaType.getDeclaredMethods();
    final Function1<Method,Boolean> _function_2 = new Function1<Method,Boolean>() {
      public Boolean apply(final Method it) {
        int _modifiers = it.getModifiers();
        boolean _isPublic = Modifier.isPublic(_modifiers);
        return Boolean.valueOf(_isPublic);
      }
    };
    Iterable<Method> _filter_2 = IterableExtensions.<Method>filter(((Iterable<Method>)Conversions.doWrapArray(_declaredMethods_1)), _function_2);
    final Procedure1<Method> _function_3 = new Procedure1<Method>() {
      public void apply(final Method method) {
        final Pair<String,Class<? extends Object>> nameAndType = JavaTypeModel.this.getPropertyNameAndType(method);
        boolean _notEquals = (!Objects.equal(nameAndType, null));
        if (_notEquals) {
          String _key = nameAndType.getKey();
          Class<? extends Object> _value = nameAndType.getValue();
          Pair<Class<? extends Object>,Method> _mappedTo = Pair.<Class<? extends Object>, Method>of(_value, method);
          propertyMethods.put(_key, _mappedTo);
        }
      }
    };
    IterableExtensions.<Method>forEach(_filter_2, _function_3);
    Set<String> _keySet = propertyMethods.keySet();
    for (final String propertyName : _keySet) {
      {
        final Set<Pair<Class<? extends Object>,Method>> type2Method = propertyMethods.get(propertyName);
        final Function1<Pair<Class<? extends Object>,Method>,Class<? extends Object>> _function_4 = new Function1<Pair<Class<? extends Object>,Method>,Class<? extends Object>>() {
          public Class<? extends Object> apply(final Pair<Class<? extends Object>,Method> it) {
            Class<? extends Object> _key = it.getKey();
            return _key;
          }
        };
        Iterable<Class<? extends Object>> _map = IterableExtensions.<Pair<Class<? extends Object>,Method>, Class<? extends Object>>map(type2Method, _function_4);
        Iterable<Class<? extends Object>> _filterNull = IterableExtensions.<Class<? extends Object>>filterNull(_map);
        final Set<Class<? extends Object>> types = IterableExtensions.<Class<? extends Object>>toSet(_filterNull);
        int _size = types.size();
        boolean _equals = (_size == 1);
        if (_equals) {
          final Function1<Pair<Class<? extends Object>,Method>,Method> _function_5 = new Function1<Pair<Class<? extends Object>,Method>,Method>() {
            public Method apply(final Pair<Class<? extends Object>,Method> it) {
              Method _value = it.getValue();
              return _value;
            }
          };
          Iterable<Method> _map_1 = IterableExtensions.<Pair<Class<? extends Object>,Method>, Method>map(type2Method, _function_5);
          CollectionExtensions.<Method>removeAll(this.operations, _map_1);
          Class<? extends Object> _head = IterableExtensions.<Class<? extends Object>>head(types);
          boolean _isPrimitiveOrString = this.isPrimitiveOrString(_head);
          if (_isPrimitiveOrString) {
            Class<? extends Object> _head_1 = IterableExtensions.<Class<? extends Object>>head(types);
            Property _property = new Property(propertyName, _head_1);
            this.properties.add(_property);
          } else {
            Class<? extends Object> _head_2 = IterableExtensions.<Class<? extends Object>>head(types);
            Property _property_1 = new Property(propertyName, _head_2);
            this.references.add(_property_1);
          }
        } else {
          String _join = IterableExtensions.join(types, " ");
          InputOutput.<String>println(_join);
        }
      }
    }
    final Function1<Method,String> _function_4 = new Function1<Method,String>() {
      public String apply(final Method it) {
        String _name = it.getName();
        return _name;
      }
    };
    ListExtensions.<Method, String>sortInplaceBy(this.operations, _function_4);
    final Function1<Property,String> _function_5 = new Function1<Property,String>() {
      public String apply(final Property it) {
        String _name = it.getName();
        return _name;
      }
    };
    ListExtensions.<Property, String>sortInplaceBy(this.properties, _function_5);
    Class<? extends Object> _superclass = javaType.getSuperclass();
    boolean _notEquals = (!Objects.equal(_superclass, null));
    if (_notEquals) {
      Class<? extends Object> _superclass_1 = javaType.getSuperclass();
      this.superTypes.add(_superclass_1);
    }
    Class<? extends Object>[] _interfaces = javaType.getInterfaces();
    CollectionExtensions.<Class<? extends Object>>addAll(this.superTypes, _interfaces);
  }
  
  protected Pair<String,Class<? extends Object>> getPropertyNameAndType(final Method method) {
    Pair<String,Class<? extends Object>> _xblockexpression = null;
    {
      final String methodName = method.getName();
      Pair<String,Class<? extends Object>> _xifexpression = null;
      boolean _startsWith = methodName.startsWith("get");
      if (_startsWith) {
        Pair<String,Class<? extends Object>> _xifexpression_1 = null;
        Class<? extends Object>[] _parameterTypes = method.getParameterTypes();
        int _size = ((List<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes)).size();
        boolean _equals = (_size == 0);
        if (_equals) {
          String _substring = methodName.substring(3);
          String _firstLower = StringExtensions.toFirstLower(_substring);
          Class<? extends Object> _returnType = method.getReturnType();
          Pair<String,Class<? extends Object>> _mappedTo = Pair.<String, Class<? extends Object>>of(_firstLower, _returnType);
          _xifexpression_1 = _mappedTo;
        }
        _xifexpression = _xifexpression_1;
      } else {
        Pair<String,Class<? extends Object>> _xifexpression_2 = null;
        boolean _startsWith_1 = methodName.startsWith("set");
        if (_startsWith_1) {
          Pair<String,Class<? extends Object>> _xifexpression_3 = null;
          Class<? extends Object>[] _parameterTypes_1 = method.getParameterTypes();
          int _size_1 = ((List<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes_1)).size();
          boolean _equals_1 = (_size_1 == 1);
          if (_equals_1) {
            String _substring_1 = methodName.substring(3);
            String _firstLower_1 = StringExtensions.toFirstLower(_substring_1);
            Class<? extends Object>[] _parameterTypes_2 = method.getParameterTypes();
            Class<? extends Object> _head = IterableExtensions.<Class<? extends Object>>head(((Iterable<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes_2)));
            Pair<String,Class<? extends Object>> _mappedTo_1 = Pair.<String, Class<? extends Object>>of(_firstLower_1, _head);
            _xifexpression_3 = _mappedTo_1;
          }
          _xifexpression_2 = _xifexpression_3;
        } else {
          Pair<String,Class<? extends Object>> _xifexpression_4 = null;
          boolean _startsWith_2 = methodName.startsWith("is");
          if (_startsWith_2) {
            Pair<String,Class<? extends Object>> _xifexpression_5 = null;
            Class<? extends Object>[] _parameterTypes_3 = method.getParameterTypes();
            int _size_2 = ((List<Class<? extends Object>>)Conversions.doWrapArray(_parameterTypes_3)).size();
            boolean _equals_2 = (_size_2 == 0);
            if (_equals_2) {
              String _substring_2 = methodName.substring(2);
              String _firstLower_2 = StringExtensions.toFirstLower(_substring_2);
              Class<? extends Object> _returnType_1 = method.getReturnType();
              Pair<String,Class<? extends Object>> _mappedTo_2 = Pair.<String, Class<? extends Object>>of(_firstLower_2, _returnType_1);
              _xifexpression_5 = _mappedTo_2;
            }
            _xifexpression_4 = _xifexpression_5;
          } else {
            Pair<String,Class<? extends Object>> _xifexpression_6 = null;
            boolean _endsWith = methodName.endsWith("Property");
            if (_endsWith) {
              int _length = methodName.length();
              int _minus = (_length - 8);
              String _substring_3 = methodName.substring(0, _minus);
              Pair<String,Class<? extends Object>> _mappedTo_3 = Pair.<String, Class<? extends Object>>of(_substring_3, null);
              _xifexpression_6 = _mappedTo_3;
            } else {
              _xifexpression_6 = null;
            }
            _xifexpression_4 = _xifexpression_6;
          }
          _xifexpression_2 = _xifexpression_4;
        }
        _xifexpression = _xifexpression_2;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public List<Class<? extends Object>> getSuperTypes() {
    return this.superTypes;
  }
  
  public List<Constructor<? extends Object>> getConstructors() {
    return this.constructors;
  }
  
  public List<Method> getOperations() {
    return this.operations;
  }
  
  public List<Property> getProperties() {
    return this.properties;
  }
  
  public List<Property> getReferences() {
    return this.references;
  }
  
  public boolean isPrimitiveOrString(final Class<? extends Object> type) {
    boolean _or = false;
    boolean _isPrimitive = type.isPrimitive();
    if (_isPrimitive) {
      _or = true;
    } else {
      boolean _equals = Objects.equal(type, String.class);
      _or = (_isPrimitive || _equals);
    }
    return _or;
  }
}
