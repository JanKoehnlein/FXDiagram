package de.fxdiagram.examples.java;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import de.fxdiagram.examples.java.JavaProperty;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class JavaTypeModel {
  private List<Constructor<?>> constructors;
  
  private List<Method> operations;
  
  private List<JavaProperty> properties = CollectionLiterals.<JavaProperty>newArrayList();
  
  private List<JavaProperty> references = CollectionLiterals.<JavaProperty>newArrayList();
  
  private List<Class<?>> superTypes = CollectionLiterals.<Class<?>>newArrayList();
  
  public JavaTypeModel(final Class<?> javaType) {
    final Function1<Constructor<?>, Boolean> _function = (Constructor<?> it) -> {
      return Boolean.valueOf(Modifier.isPublic(it.getModifiers()));
    };
    this.constructors = IterableExtensions.<Constructor<?>>toList(IterableExtensions.<Constructor<?>>filter(((Iterable<Constructor<?>>)Conversions.doWrapArray(javaType.getDeclaredConstructors())), _function));
    final HashMultimap<String, Pair<Class<?>, Method>> propertyMethods = HashMultimap.<String, Pair<Class<?>, Method>>create();
    final Function1<Method, Boolean> _function_1 = (Method it) -> {
      return Boolean.valueOf((Modifier.isPublic(it.getModifiers()) && (!it.getName().startsWith("impl_"))));
    };
    this.operations = IterableExtensions.<Method>toList(IterableExtensions.<Method>filter(((Iterable<Method>)Conversions.doWrapArray(javaType.getDeclaredMethods())), _function_1));
    final Function1<Method, Boolean> _function_2 = (Method it) -> {
      return Boolean.valueOf(Modifier.isPublic(it.getModifiers()));
    };
    final Consumer<Method> _function_3 = (Method method) -> {
      final Pair<String, Class<?>> nameAndType = this.getPropertyNameAndType(method);
      if ((nameAndType != null)) {
        Class<?> _value = nameAndType.getValue();
        Pair<Class<?>, Method> _mappedTo = Pair.<Class<?>, Method>of(_value, method);
        propertyMethods.put(nameAndType.getKey(), _mappedTo);
      }
    };
    IterableExtensions.<Method>filter(((Iterable<Method>)Conversions.doWrapArray(javaType.getDeclaredMethods())), _function_2).forEach(_function_3);
    Set<String> _keySet = propertyMethods.keySet();
    for (final String propertyName : _keySet) {
      {
        final Set<Pair<Class<?>, Method>> type2Method = propertyMethods.get(propertyName);
        final Function1<Pair<Class<?>, Method>, Class<?>> _function_4 = (Pair<Class<?>, Method> it) -> {
          return it.getKey();
        };
        final Set<Class<?>> types = IterableExtensions.<Class<?>>toSet(IterableExtensions.<Class<?>>filterNull(IterableExtensions.<Pair<Class<?>, Method>, Class<?>>map(type2Method, _function_4)));
        int _size = types.size();
        boolean _equals = (_size == 1);
        if (_equals) {
          final Function1<Pair<Class<?>, Method>, Method> _function_5 = (Pair<Class<?>, Method> it) -> {
            return it.getValue();
          };
          CollectionExtensions.<Method>removeAll(this.operations, IterableExtensions.<Pair<Class<?>, Method>, Method>map(type2Method, _function_5));
          boolean _isPrimitiveOrString = this.isPrimitiveOrString(IterableExtensions.<Class<?>>head(types));
          if (_isPrimitiveOrString) {
            Class<?> _head = IterableExtensions.<Class<?>>head(types);
            JavaProperty _javaProperty = new JavaProperty(propertyName, _head);
            this.properties.add(_javaProperty);
          } else {
            Class<?> _head_1 = IterableExtensions.<Class<?>>head(types);
            JavaProperty _javaProperty_1 = new JavaProperty(propertyName, _head_1);
            this.references.add(_javaProperty_1);
          }
        } else {
          InputOutput.<String>println(IterableExtensions.join(types, " "));
        }
      }
    }
    final Function1<Method, String> _function_4 = (Method it) -> {
      return it.getName();
    };
    ListExtensions.<Method, String>sortInplaceBy(this.operations, _function_4);
    final Function1<JavaProperty, String> _function_5 = (JavaProperty it) -> {
      return it.getName();
    };
    ListExtensions.<JavaProperty, String>sortInplaceBy(this.properties, _function_5);
    Class<?> _superclass = javaType.getSuperclass();
    boolean _tripleNotEquals = (_superclass != null);
    if (_tripleNotEquals) {
      this.superTypes.add(javaType.getSuperclass());
    }
    CollectionExtensions.<Class<?>>addAll(this.superTypes, javaType.getInterfaces());
  }
  
  protected Pair<String, Class<?>> getPropertyNameAndType(final Method method) {
    Pair<String, Class<?>> _xblockexpression = null;
    {
      final String methodName = method.getName();
      Pair<String, Class<?>> _xifexpression = null;
      boolean _startsWith = methodName.startsWith("get");
      if (_startsWith) {
        Pair<String, Class<?>> _xifexpression_1 = null;
        int _size = ((List<Class<?>>)Conversions.doWrapArray(method.getParameterTypes())).size();
        boolean _equals = (_size == 0);
        if (_equals) {
          String _firstLower = StringExtensions.toFirstLower(methodName.substring(3));
          Class<?> _returnType = method.getReturnType();
          _xifexpression_1 = Pair.<String, Class<?>>of(_firstLower, _returnType);
        }
        _xifexpression = _xifexpression_1;
      } else {
        Pair<String, Class<?>> _xifexpression_2 = null;
        boolean _startsWith_1 = methodName.startsWith("set");
        if (_startsWith_1) {
          Pair<String, Class<?>> _xifexpression_3 = null;
          int _size_1 = ((List<Class<?>>)Conversions.doWrapArray(method.getParameterTypes())).size();
          boolean _equals_1 = (_size_1 == 1);
          if (_equals_1) {
            String _firstLower_1 = StringExtensions.toFirstLower(methodName.substring(3));
            Class<?> _head = IterableExtensions.<Class<?>>head(((Iterable<Class<?>>)Conversions.doWrapArray(method.getParameterTypes())));
            _xifexpression_3 = Pair.<String, Class<?>>of(_firstLower_1, _head);
          }
          _xifexpression_2 = _xifexpression_3;
        } else {
          Pair<String, Class<?>> _xifexpression_4 = null;
          boolean _startsWith_2 = methodName.startsWith("is");
          if (_startsWith_2) {
            Pair<String, Class<?>> _xifexpression_5 = null;
            int _size_2 = ((List<Class<?>>)Conversions.doWrapArray(method.getParameterTypes())).size();
            boolean _equals_2 = (_size_2 == 0);
            if (_equals_2) {
              String _firstLower_2 = StringExtensions.toFirstLower(methodName.substring(2));
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
              String _substring = methodName.substring(0, _minus);
              _xifexpression_6 = Pair.<String, Class<?>>of(_substring, null);
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
    return (type.isPrimitive() || Objects.equal(type, String.class));
  }
}
