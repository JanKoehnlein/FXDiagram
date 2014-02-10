package de.fxdiagram.core.model;

import java.lang.reflect.Constructor;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ModelPersistence {
  public static Constructor<?> findConstructor(final Class<?> clazz, final List<Class<?>> givenParamTypes) {
    Constructor<?>[] _constructors = clazz.getConstructors();
    final Function1<Constructor<?>,Boolean> _function = new Function1<Constructor<?>,Boolean>() {
      public Boolean apply(final Constructor<?> it) {
        int _size = givenParamTypes.size();
        Class<?>[] _parameterTypes = it.getParameterTypes();
        int _length = _parameterTypes.length;
        boolean _equals = (_size == _length);
        if (_equals) {
          Class<?>[] _parameterTypes_1 = it.getParameterTypes();
          int _length_1 = _parameterTypes_1.length;
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length_1, true);
          for (final Integer i : _doubleDotLessThan) {
            Class<?>[] _parameterTypes_2 = it.getParameterTypes();
            Class<?> _get = _parameterTypes_2[(i).intValue()];
            Class<?> _get_1 = givenParamTypes.get((i).intValue());
            boolean _isAssignableFrom = _get.isAssignableFrom(_get_1);
            boolean _not = (!_isAssignableFrom);
            if (_not) {
              return Boolean.valueOf(false);
            }
          }
          return Boolean.valueOf(true);
        } else {
          return Boolean.valueOf(false);
        }
      }
    };
    return IterableExtensions.<Constructor<?>>findFirst(((Iterable<Constructor<?>>)Conversions.doWrapArray(_constructors)), _function);
  }
}
