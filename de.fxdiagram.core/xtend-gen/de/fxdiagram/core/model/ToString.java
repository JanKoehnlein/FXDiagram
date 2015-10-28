package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.XModelProvider;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ToString {
  public static String toString(final XModelProvider modelProvider) {
    String _xblockexpression = null;
    {
      final ModelElementImpl modelElement = new ModelElementImpl(null);
      modelProvider.populate(modelElement);
      StringConcatenation _builder = new StringConcatenation();
      Class<? extends XModelProvider> _class = modelProvider.getClass();
      String _simpleName = _class.getSimpleName();
      _builder.append(_simpleName, "");
      _builder.append(" ");
      String _propertiesToString = ToString.getPropertiesToString(modelElement);
      _builder.append(_propertiesToString, "");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder.toString();
    }
    return _xblockexpression;
  }
  
  protected static String getPropertiesToString(final ModelElement it) {
    boolean _and = false;
    List<? extends Property<?>> _properties = it.getProperties();
    boolean _isEmpty = _properties.isEmpty();
    if (!_isEmpty) {
      _and = false;
    } else {
      List<? extends ListProperty<?>> _listProperties = it.getListProperties();
      boolean _isEmpty_1 = _listProperties.isEmpty();
      _and = _isEmpty_1;
    }
    if (_and) {
      return "";
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("[");
      _builder.newLine();
      {
        List<? extends ListProperty<?>> _listProperties_1 = it.getListProperties();
        final Function1<ListProperty<?>, Boolean> _function = (ListProperty<?> it_1) -> {
          boolean _isEmpty_2 = it_1.isEmpty();
          return Boolean.valueOf((!_isEmpty_2));
        };
        Iterable<? extends ListProperty<?>> _filter = IterableExtensions.filter(_listProperties_1, _function);
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
              String _string = value.toString();
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
        List<? extends Property<?>> _properties_1 = it.getProperties();
        final Function1<Property<?>, Boolean> _function_1 = (Property<?> it_1) -> {
          Object _value_1 = it_1.getValue();
          return Boolean.valueOf((!Objects.equal(_value_1, null)));
        };
        Iterable<? extends Property<?>> _filter_1 = IterableExtensions.filter(_properties_1, _function_1);
        for(final Property<?> p : _filter_1) {
          _builder.append("\t");
          String _name_1 = p.getName();
          _builder.append(_name_1, "\t");
          _builder.append(": ");
          Object _value_1 = p.getValue();
          String _string_1 = _value_1.toString();
          _builder.append(_string_1, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("]");
      _builder.newLine();
      return _builder.toString();
    }
  }
}
