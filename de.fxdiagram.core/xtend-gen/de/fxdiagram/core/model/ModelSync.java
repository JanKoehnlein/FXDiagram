package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.Model;
import de.fxdiagram.core.model.ModelElement;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ModelSync {
  private Model model;
  
  private ChangeListener<Object> childrenListener = new ChangeListener<Object>() {
    public void changed(final ObservableValue<?> property, final Object oldValue, final Object newValue) {
      boolean _notEquals = (!Objects.equal(oldValue, null));
      if (_notEquals) {
        ModelSync.this.model.removeElement(oldValue);
      }
      boolean _notEquals_1 = (!Objects.equal(newValue, null));
      if (_notEquals_1) {
        ModelSync.this.model.addElement(newValue);
      }
    }
  };
  
  private ListChangeListener<Object> childrenListListener = new ListChangeListener<Object>() {
    public void onChanged(final ListChangeListener.Change<?> change) {
      boolean _next = change.next();
      boolean _while = _next;
      while (_while) {
        {
          boolean _wasAdded = change.wasAdded();
          if (_wasAdded) {
            List<?> _addedSubList = change.getAddedSubList();
            final Procedure1<Object> _function = new Procedure1<Object>() {
              public void apply(final Object it) {
                ModelSync.this.model.addElement(it);
              }
            };
            IterableExtensions.forEach(_addedSubList, _function);
          }
          boolean _wasRemoved = change.wasRemoved();
          if (_wasRemoved) {
            List<?> _removed = change.getRemoved();
            final Procedure1<Object> _function_1 = new Procedure1<Object>() {
              public void apply(final Object it) {
                ModelSync.this.model.removeElement(it);
              }
            };
            IterableExtensions.forEach(_removed, _function_1);
          }
        }
        boolean _next_1 = change.next();
        _while = _next_1;
      }
    }
  };
  
  public ModelSync(final Model model) {
    this.model = model;
  }
  
  protected void addElement(final ModelElement element) {
    List<Property<?>> _properties = element.getProperties();
    final Procedure1<Property<?>> _function = new Procedure1<Property<?>>() {
      public void apply(final Property<?> it) {
        it.addListener(ModelSync.this.childrenListener);
      }
    };
    IterableExtensions.<Property<?>>forEach(_properties, _function);
    List<ListProperty<?>> _listProperties = element.getListProperties();
    final Procedure1<ListProperty<?>> _function_1 = new Procedure1<ListProperty<?>>() {
      public void apply(final ListProperty<?> it) {
        it.addListener(ModelSync.this.childrenListListener);
      }
    };
    IterableExtensions.<ListProperty<?>>forEach(_listProperties, _function_1);
  }
  
  protected void removeElement(final ModelElement element) {
    List<Property<?>> _properties = element.getProperties();
    final Procedure1<Property<?>> _function = new Procedure1<Property<?>>() {
      public void apply(final Property<?> it) {
        it.removeListener(ModelSync.this.childrenListener);
      }
    };
    IterableExtensions.<Property<?>>forEach(_properties, _function);
    List<ListProperty<?>> _listProperties = element.getListProperties();
    final Procedure1<ListProperty<?>> _function_1 = new Procedure1<ListProperty<?>>() {
      public void apply(final ListProperty<?> it) {
        it.removeListener(ModelSync.this.childrenListListener);
      }
    };
    IterableExtensions.<ListProperty<?>>forEach(_listProperties, _function_1);
  }
}
