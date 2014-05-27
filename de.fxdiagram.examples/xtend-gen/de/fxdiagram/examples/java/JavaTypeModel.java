package de.fxdiagram.examples.java;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import de.fxdiagram.examples.java.JavaProperty;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class JavaTypeModel {
  private List<Constructor<?>> constructors;
  
  private List<Method> operations;
  
  private List<JavaProperty> properties = CollectionLiterals.<JavaProperty>newArrayList();
  
  private List<JavaProperty> references = CollectionLiterals.<JavaProperty>newArrayList();
  
  private List<Class<?>> superTypes = CollectionLiterals.<Class<?>>newArrayList();
  
  public JavaTypeModel(final Class<?> javaType) {
    Constructor<?>[] _declaredConstructors = javaType.getDeclaredConstructors();
    final Function1<Constructor<?>, Boolean> _function = new Function1<Constructor<?>, Boolean>() {
      public Boolean apply(final Constructor<?> it) {
        int _modifiers = it.getModifiers();
        return Boolean.valueOf(Modifier.isPublic(_modifiers));
      }
    };
    Iterable<Constructor<?>> _filter = IterableExtensions.<Constructor<?>>filter(((Iterable<Constructor<?>>)Conversions.doWrapArray(_declaredConstructors)), _function);
    List<Constructor<?>> _list = IterableExtensions.<Constructor<?>>toList(_filter);
    this.constructors = _list;
    final HashMultimap<String, Pair<Class<?>, Method>> propertyMethods = HashMultimap.<String, Pair<Class<?>, Method>>create();
    Method[] _declaredMethods = javaType.getDeclaredMethods();
    final Function1<Method, Boolean> _function_1 = new Function1<Method, Boolean>() {
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
          _and = _not;
        }
        return Boolean.valueOf(_and);
      }
    };
    Iterable<Method> _filter_1 = IterableExtensions.<Method>filter(((Iterable<Method>)Conversions.doWrapArray(_declaredMethods)), _function_1);
    List<Method> _list_1 = IterableExtensions.<Method>toList(_filter_1);
    this.operations = _list_1;
    Method[] _declaredMethods_1 = javaType.getDeclaredMethods();
    final Function1<Method, Boolean> _function_2 = new Function1<Method, Boolean>() {
      public Boolean apply(final Method it) {
        int _modifiers = it.getModifiers();
        return Boolean.valueOf(Modifier.isPublic(_modifiers));
      }
    };
    Iterable<Method> _filter_2 = IterableExtensions.<Method>filter(((Iterable<Method>)Conversions.doWrapArray(_declaredMethods_1)), _function_2);
    final Procedure1<Method> _function_3 = new Procedure1<Method>() {
      public void apply(final Method method) {
        final Pair<String, Class<?>> nameAndType = JavaTypeModel.this.getPropertyNameAndType(method);
        boolean _notEquals = (!Objects.equal(nameAndType, null));
        if (_notEquals) {
          String _key = nameAndType.getKey();
          Class<?> _value = nameAndType.getValue();
          Pair<Class<?>, Method> _mappedTo = Pair.<Class<?>, Method>of(_value, method);
          propertyMethods.put(_key, _mappedTo);
        }
      }
    };
    IterableExtensions.<Method>forEach(_filter_2, _function_3);
    Set<String> _keySet = propertyMethods.keySet();
    for (final String propertyName : _keySet) {
      {
        final Set<Pair<Class<?>, Method>> type2Method = propertyMethods.get(propertyName);
        final Function1<Pair<Class<?>, Method>, Class<?>> _function_4 = new Function1<Pair<Class<?>, Method>, Class<?>>() {
          public Class<?> apply(final Pair<Class<?>, Method> it) {
            return it.getKey();
          }
        };
        Iterable<Class<?>> _map = IterableExtensions.<Pair<Class<?>, Method>, Class<?>>map(type2Method, _function_4);
        Iterable<Class<?>> _filterNull = IterableExtensions.<Class<?>>filterNull(_map);
        final Set<Class<?>> types = IterableExtensions.<Class<?>>toSet(_filterNull);
        int _size = types.size();
        boolean _equals = (_size == 1);
        if (_equals) {
          final Function1<Pair<Class<?>, Method>, Method> _function_5 = new Function1<Pair<Class<?>, Method>, Method>() {
            public Method apply(final Pair<Class<?>, Method> it) {
              return it.getValue();
            }
          };
          Iterable<Method> _map_1 = IterableExtensions.<Pair<Class<?>, Method>, Method>map(type2Method, _function_5);
          CollectionExtensions.<Method>removeAll(this.operations, _map_1);
          Class<?> _head = IterableExtensions.<Class<?>>head(types);
          boolean _isPrimitiveOrString = this.isPrimitiveOrString(_head);
          if (_isPrimitiveOrString) {
            Class<?> _head_1 = IterableExtensions.<Class<?>>head(types);
            JavaProperty _javaProperty = new JavaProperty(propertyName, _head_1);
            this.properties.add(_javaProperty);
          } else {
            Class<?> _head_2 = IterableExtensions.<Class<?>>head(types);
            JavaProperty _javaProperty_1 = new JavaProperty(propertyName, _head_2);
            this.references.add(_javaProperty_1);
          }
        } else {
          String _join = IterableExtensions.join(types, " ");
          InputOutput.<String>println(_join);
        }
      }
    }
    final Function1<Method, String> _function_4 = new Function1<Method, String>() {
      public String apply(final Method it) {
        return it.getName();
      }
    };
    ListExtensions.<Method, String>sortInplaceBy(this.operations, _function_4);
    final Function1<JavaProperty, String> _function_5 = new Function1<JavaProperty, String>() {
      public String apply(final JavaProperty it) {
        return it.getName();
      }
    };
    ListExtensions.<JavaProperty, String>sortInplaceBy(this.properties, _function_5);
    Class<?> _superclass = javaType.getSuperclass();
    boolean _notEquals = (!Objects.equal(_superclass, null));
    if (_notEquals) {
      Class<?> _superclass_1 = javaType.getSuperclass();
      this.superTypes.add(_superclass_1);
    }
    Class<?>[] _interfaces = javaType.getInterfaces();
    CollectionExtensions.<Class<?>>addAll(this.superTypes, _interfaces);
  }
  
  protected Pair<String, Class<?>> getPropertyNameAndType(final Method method) {
    Pair<String, Class<?>> _xblockexpression = null;
    {
      final String methodName = method.getName();
      Pair<String, Class<?>> _xifexpression = null;
      boolean _startsWith = methodName.startsWith("get");
      if (_startsWith) {
        Pair<String, Class<?>> _xifexpression_1 = null;
        Class<?>[] _parameterTypes = method.getParameterTypes();
        int _size = ((List<Class<?>>)Conversions.doWrapArray(_parameterTypes)).size();
        boolean _equals = (_size == 0);
        if (_equals) {
          String _substring = methodName.substring(3);
          String _firstLower = StringExtensions.toFirstLower(_substring);
          Class<?> _returnType = method.getReturnType();
          _xifexpression_1 = Pair.<String, Class<?>>of(_firstLower, _returnType);
        }
        _xifexpression = _xifexpression_1;
      } else {
        Pair<String, Class<?>> _xifexpression_2 = null;
        boolean _startsWith_1 = methodName.startsWith("set");
        if (_startsWith_1) {
          Pair<String, Class<?>> _xifexpression_3 = null;
          Class<?>[] _parameterTypes_1 = method.getParameterTypes();
          int _size_1 = ((List<Class<?>>)Conversions.doWrapArray(_parameterTypes_1)).size();
          boolean _equals_1 = (_size_1 == 1);
          if (_equals_1) {
            String _substring_1 = methodName.substring(3);
            String _firstLower_1 = StringExtensions.toFirstLower(_substring_1);
            Class<?>[] _parameterTypes_2 = method.getParameterTypes();
            Class<?> _head = IterableExtensions.<Class<?>>head(((Iterable<Class<?>>)Conversions.doWrapArray(_parameterTypes_2)));
            _xifexpression_3 = Pair.<String, Class<?>>of(_firstLower_1, _head);
          }
          _xifexpression_2 = _xifexpression_3;
        } else {
          Pair<String, Class<?>> _xifexpression_4 = null;
          boolean _startsWith_2 = methodName.startsWith("is");
          if (_startsWith_2) {
            Pair<String, Class<?>> _xifexpression_5 = null;
            Class<?>[] _parameterTypes_3 = method.getParameterTypes();
            int _size_2 = ((List<Class<?>>)Conversions.doWrapArray(_parameterTypes_3)).size();
            boolean _equals_2 = (_size_2 == 0);
            if (_equals_2) {
              String _substring_2 = methodName.substring(2);
              String _firstLower_2 = StringExtensions.toFirstLower(_substring_2);
              Class<?> _returnType_1 = method.getReturnType();
              _xifexpression_5 = Pair.<String, Class<?>>of(_firstLower_2, _returnType_1);
            }
            _xifexpression_4 = _xifexpression_5;
          } else {
            Pair<String, Class<?>> _xifexpression_6 = null;
            boolean _endsWith = methodName.endsWith("Property");
            if (_endsWith) {
              int _length = methodName.length();
              int _minus = (_length - 8);
              String _substring_3 = methodName.substring(0, _minus);
              _xifexpression_6 = Pair.<String, Class<?>>of(_substring_3, null);
            } else {
              _xifexpression_6 = null;
            }
            _xifexpression_4 = _xifexpression_6;
          }
          _xifexpression_2 = _xifexpression_4;
        }
        _xifexpression = _xifexpression_2;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public List<Class<?>> getSuperTypes() {
    return this.superTypes;
  }
  
  public List<Constructor<?>> getConstructors() {
    return this.constructors;
  }
  
  public List<Method> getOperations() {
    return this.operations;
  }
  
  public List<JavaProperty> getProperties() {
    return this.properties;
  }
  
  public List<JavaProperty> getReferences() {
    return this.references;
  }
  
  public boolean isPrimitiveOrString(final Class<?> type) {
    boolean _or = false;
    boolean _isPrimitive = type.isPrimitive();
    if (_isPrimitive) {
      _or = true;
    } else {
      boolean _equals = Objects.equal(type, String.class);
      _or = _equals;
    }
    return _or;
  }
}
