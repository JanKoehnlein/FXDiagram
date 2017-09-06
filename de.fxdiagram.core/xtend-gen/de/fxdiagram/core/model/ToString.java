package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import java.util.Arrays;
import java.util.Map;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ToString {
  public static String toString(final XModelProvider modelProvider) {
    return ToString.toString(modelProvider, CollectionLiterals.<XModelProvider, Integer>newHashMap());
  }
  
  protected static String _toString(final XModelProvider modelProvider, final Map<XModelProvider, Integer> seen) {
    String _xblockexpression = null;
    {
      final Integer number = seen.get(modelProvider);
      String _xifexpression = null;
      boolean _equals = Objects.equal(number, null);
      if (_equals) {
        String _xblockexpression_1 = null;
        {
          final int newNumber = seen.size();
          seen.put(modelProvider, Integer.valueOf(newNumber));
          final ModelElementImpl modelElement = new ModelElementImpl(null);
          modelProvider.populate(modelElement);
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("#");
          _builder.append(newNumber);
          _builder.append(" ");
          String _simpleName = modelProvider.getClass().getSimpleName();
          _builder.append(_simpleName);
          _builder.append(" ");
          String _propertiesToString = ToString.getPropertiesToString(modelElement, seen);
          _builder.append(_propertiesToString);
          _builder.newLineIfNotEmpty();
          _xblockexpression_1 = _builder.toString();
        }
        _xifexpression = _xblockexpression_1;
      } else {
        StringConcatenation _builder = new StringConcatenation();
        String _simpleName = modelProvider.getClass().getSimpleName();
        _builder.append(_simpleName);
        _builder.append(" #");
        _builder.append(number);
        _builder.newLineIfNotEmpty();
        _xifexpression = _builder.toString();
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  protected static String _toString(final Object element, final Map<XModelProvider, Integer> seen) {
    return element.toString();
  }
  
  protected static String getPropertiesToString(final ModelElement it, final Map<XModelProvider, Integer> seen) {
    if ((it.getProperties().isEmpty() && it.getListProperties().isEmpty())) {
      return "";
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("[");
      _builder.newLine();
      {
        final Function1<ListProperty<?>, Boolean> _function = (ListProperty<?> it_1) -> {
          boolean _isEmpty = it_1.isEmpty();
          return Boolean.valueOf((!_isEmpty));
        };
        Iterable<? extends ListProperty<?>> _filter = IterableExtensions.filter(it.getListProperties(), _function);
        for(final ListProperty<?> lp : _filter) {
          _builder.append("\t");
          String _name = lp.getName();
          _builder.append(_name, "\t");
          _builder.append(": [");
          _builder.newLineIfNotEmpty();
          {
            ObservableList<?> _value = lp.getValue();
            for(final Object value : _value) {
              _builder.append("\t");
              _builder.append("\t");
              String _string = ToString.toString(value, seen);
              _builder.append(_string, "\t\t");
              _builder.newLineIfNotEmpty();
            }
          }
          _builder.append("\t");
          _builder.append("]");
          _builder.newLine();
        }
      }
      {
        final Function1<Property<?>, Boolean> _function_1 = (Property<?> it_1) -> {
          Object _value_1 = it_1.getValue();
          return Boolean.valueOf((!Objects.equal(_value_1, null)));
        };
        Iterable<? extends Property<?>> _filter_1 = IterableExtensions.filter(it.getProperties(), _function_1);
        for(final Property<?> p : _filter_1) {
          _builder.append("\t");
          String _name_1 = p.getName();
          _builder.append(_name_1, "\t");
          _builder.append(": ");
          String _string_1 = ToString.toString(p.getValue(), seen);
          _builder.append(_string_1, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("]");
      _builder.newLine();
      return _builder.toString();
    }
  }
  
  protected static String toString(final Object modelProvider, final Map<XModelProvider, Integer> seen) {
    if (modelProvider instanceof XModelProvider) {
      return _toString((XModelProvider)modelProvider, seen);
    } else if (modelProvider != null) {
      return _toString(modelProvider, seen);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(modelProvider, seen).toString());
    }
  }
}
