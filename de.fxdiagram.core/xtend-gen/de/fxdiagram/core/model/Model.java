package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.ModelChangeListener;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelFactory;
import de.fxdiagram.core.model.ModelSync;
import de.fxdiagram.core.model.XModelProvider;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Model {
  private Map<Object,ModelElement> index = new IdentityHashMap<Object, ModelElement>();
  
  private ChangeListener<Object> changeListener = new ChangeListener<Object>() {
    public void changed(final ObservableValue<?> property, final Object oldValue, final Object newValue) {
      Model.this.notifyListeners(property, oldValue, newValue);
    }
  };
  
  private ListChangeListener<Object> listChangeListener = new ListChangeListener<Object>() {
    public void onChanged(final ListChangeListener.Change<?> change) {
      Model.this.notifyListListeners(change);
    }
  };
  
  private List<ModelChangeListener> modelChangeListeners = CollectionLiterals.<ModelChangeListener>newArrayList();
  
  private ModelElement rootElement;
  
  private ModelFactory modelFactory;
  
  private ModelSync modelSync;
  
  public Model(final Object rootNode) {
    ModelFactory _modelFactory = new ModelFactory();
    this.modelFactory = _modelFactory;
    ModelSync _modelSync = new ModelSync(this);
    this.modelSync = _modelSync;
    ModelElement _addElement = this.addElement(rootNode);
    this.rootElement = _addElement;
  }
  
  Map<Object,ModelElement> getIndex() {
    return this.index;
  }
  
  public ModelElement getRootElement() {
    return this.rootElement;
  }
  
  public ModelElement getModelElement(final XModelProvider node) {
    return this.index.get(node);
  }
  
  ModelElement addElement(final Object node) {
    ModelElement _xblockexpression = null;
    {
      final ModelElement element = this.getOrCreateModelElement(node);
      List<Property<?>> _properties = element.getProperties();
      final Procedure1<Property<?>> _function = new Procedure1<Property<?>>() {
        public void apply(final Property<?> it) {
          boolean _isPrimitive = element.isPrimitive(it);
          boolean _not = (!_isPrimitive);
          if (_not) {
            Object _value = it.getValue();
            if (_value!=null) {
              Model.this.addElement(_value);
            }
          }
          it.addListener(Model.this.changeListener);
        }
      };
      IterableExtensions.<Property<?>>forEach(_properties, _function);
      List<ListProperty<?>> _listProperties = element.getListProperties();
      final Procedure1<ListProperty<?>> _function_1 = new Procedure1<ListProperty<?>>() {
        public void apply(final ListProperty<?> it) {
          boolean _isPrimitive = element.isPrimitive(it);
          boolean _not = (!_isPrimitive);
          if (_not) {
            ObservableList<?> _value = it.getValue();
            final Procedure1<Object> _function = new Procedure1<Object>() {
              public void apply(final Object it) {
                if (it!=null) {
                  Model.this.addElement(it);
                }
              }
            };
            IterableExtensions.forEach(_value, _function);
          }
          it.addListener(Model.this.listChangeListener);
        }
      };
      IterableExtensions.<ListProperty<?>>forEach(_listProperties, _function_1);
      _xblockexpression = element;
    }
    return _xblockexpression;
  }
  
  protected ModelElement getOrCreateModelElement(final Object node) {
    final ModelElement existingElement = this.index.get(node);
    boolean _equals = Objects.equal(existingElement, null);
    if (_equals) {
      ModelElement _elvis = null;
      if (existingElement != null) {
        _elvis = existingElement;
      } else {
        ModelElement _createElement = this.modelFactory.createElement(node);
        _elvis = _createElement;
      }
      final ModelElement element = _elvis;
      this.index.put(node, element);
      this.modelSync.addElement(element);
      return element;
    } else {
      return existingElement;
    }
  }
  
  ModelElement removeElement(final Object node) {
    ModelElement _xblockexpression = null;
    {
      final ModelElement element = this.index.remove(node);
      this.modelSync.removeElement(element);
      List<Property<?>> _properties = element.getProperties();
      final Procedure1<Property<?>> _function = new Procedure1<Property<?>>() {
        public void apply(final Property<?> it) {
          it.removeListener(Model.this.changeListener);
        }
      };
      IterableExtensions.<Property<?>>forEach(_properties, _function);
      List<ListProperty<?>> _listProperties = element.getListProperties();
      final Procedure1<ListProperty<?>> _function_1 = new Procedure1<ListProperty<?>>() {
        public void apply(final ListProperty<?> it) {
          it.removeListener(Model.this.listChangeListener);
        }
      };
      IterableExtensions.<ListProperty<?>>forEach(_listProperties, _function_1);
      _xblockexpression = element;
    }
    return _xblockexpression;
  }
  
  public boolean addModelChangeListener(final ModelChangeListener modelChangeListener) {
    return this.modelChangeListeners.add(modelChangeListener);
  }
  
  public boolean removeModelChangeListener(final ModelChangeListener modelChangeListener) {
    return this.modelChangeListeners.remove(modelChangeListener);
  }
  
  protected void notifyListListeners(final ListChangeListener.Change<?> change) {
    final ObservableList<?> list = change.getList();
    boolean _matched = false;
    if (!_matched) {
      if (list instanceof ListProperty) {
        _matched=true;
        final Procedure1<ModelChangeListener> _function = new Procedure1<ModelChangeListener>() {
          public void apply(final ModelChangeListener it) {
            it.listPropertyChanged(((ListProperty<?>)list), change);
          }
        };
        IterableExtensions.<ModelChangeListener>forEach(this.modelChangeListeners, _function);
      }
    }
  }
  
  protected void notifyListeners(final ObservableValue<?> property, final Object oldValue, final Object newValue) {
    boolean _matched = false;
    if (!_matched) {
      if (property instanceof Property) {
        _matched=true;
        final Procedure1<ModelChangeListener> _function = new Procedure1<ModelChangeListener>() {
          public void apply(final ModelChangeListener it) {
            it.propertyChanged(((Property<?>)property), oldValue, newValue);
          }
        };
        IterableExtensions.<ModelChangeListener>forEach(this.modelChangeListeners, _function);
      }
    }
  }
}
