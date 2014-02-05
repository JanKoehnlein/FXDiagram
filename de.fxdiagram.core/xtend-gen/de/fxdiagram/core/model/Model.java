package de.fxdiagram.core.model;

import com.google.common.collect.Iterables;
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
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class Model {
  private Map<Object,ModelElement> index = new Function0<Map<Object,ModelElement>>() {
    public Map<Object,ModelElement> apply() {
      IdentityHashMap<Object,ModelElement> _identityHashMap = new IdentityHashMap<Object, ModelElement>();
      return _identityHashMap;
    }
  }.apply();
  
  private ChangeListener<Object> changeListener = new Function0<ChangeListener<Object>>() {
    public ChangeListener<Object> apply() {
      final ChangeListener<Object> _function = new ChangeListener<Object>() {
        public void changed(final ObservableValue<? extends Object> property, final Object oldValue, final Object newValue) {
          Model.this.notifyListeners(property, oldValue, newValue);
        }
      };
      return _function;
    }
  }.apply();
  
  private ListChangeListener<Object> listChangeListener = new Function0<ListChangeListener<Object>>() {
    public ListChangeListener<Object> apply() {
      final ListChangeListener<Object> _function = new ListChangeListener<Object>() {
        public void onChanged(final ListChangeListener.Change<? extends Object> change) {
          Model.this.notifyListListeners(change);
        }
      };
      return _function;
    }
  }.apply();
  
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
    ModelElement _get = this.index.get(node);
    return _get;
  }
  
  ModelElement addElement(final Object node) {
    ModelElement _xblockexpression = null;
    {
      final ModelElement element = this.modelFactory.createElement(node);
      this.index.put(node, element);
      this.modelSync.addElement(element);
      List<Property<? extends Object>> _children = element.getChildren();
      final Procedure1<Property<? extends Object>> _function = new Procedure1<Property<? extends Object>>() {
        public void apply(final Property<? extends Object> it) {
          Object _value = it.getValue();
          if (_value!=null) {
            Model.this.addElement(_value);
          }
          it.addListener(Model.this.changeListener);
        }
      };
      IterableExtensions.<Property<? extends Object>>forEach(_children, _function);
      List<ListProperty<? extends Object>> _listChildren = element.getListChildren();
      final Procedure1<ListProperty<? extends Object>> _function_1 = new Procedure1<ListProperty<? extends Object>>() {
        public void apply(final ListProperty<? extends Object> it) {
          ObservableList<? extends Object> _value = it.getValue();
          final Procedure1<Object> _function = new Procedure1<Object>() {
            public void apply(final Object it) {
              if (it!=null) {
                Model.this.addElement(it);
              }
            }
          };
          IterableExtensions.forEach(_value, _function);
          it.addListener(Model.this.listChangeListener);
        }
      };
      IterableExtensions.<ListProperty<? extends Object>>forEach(_listChildren, _function_1);
      List<Property<? extends Object>> _properties = element.getProperties();
      final Procedure1<Property<? extends Object>> _function_2 = new Procedure1<Property<? extends Object>>() {
        public void apply(final Property<? extends Object> it) {
          if (it!=null) {
            it.addListener(Model.this.changeListener);
          }
        }
      };
      IterableExtensions.<Property<? extends Object>>forEach(_properties, _function_2);
      List<ListProperty<? extends Object>> _listProperties = element.getListProperties();
      final Procedure1<ListProperty<? extends Object>> _function_3 = new Procedure1<ListProperty<? extends Object>>() {
        public void apply(final ListProperty<? extends Object> it) {
          if (it!=null) {
            it.addListener(Model.this.listChangeListener);
          }
        }
      };
      IterableExtensions.<ListProperty<? extends Object>>forEach(_listProperties, _function_3);
      _xblockexpression = (element);
    }
    return _xblockexpression;
  }
  
  ModelElement removeElement(final Object node) {
    ModelElement _xblockexpression = null;
    {
      final ModelElement element = this.index.remove(node);
      this.modelSync.removeElement(element);
      List<Property<? extends Object>> _children = element.getChildren();
      List<Property<? extends Object>> _properties = element.getProperties();
      Iterable<Property<? extends Object>> _plus = Iterables.<Property<? extends Object>>concat(_children, _properties);
      final Procedure1<Property<? extends Object>> _function = new Procedure1<Property<? extends Object>>() {
        public void apply(final Property<? extends Object> it) {
          it.removeListener(Model.this.changeListener);
        }
      };
      IterableExtensions.<Property<? extends Object>>forEach(_plus, _function);
      List<ListProperty<? extends Object>> _listChildren = element.getListChildren();
      List<ListProperty<? extends Object>> _listProperties = element.getListProperties();
      Iterable<ListProperty<? extends Object>> _plus_1 = Iterables.<ListProperty<? extends Object>>concat(_listChildren, _listProperties);
      final Procedure1<ListProperty<? extends Object>> _function_1 = new Procedure1<ListProperty<? extends Object>>() {
        public void apply(final ListProperty<? extends Object> it) {
          it.removeListener(Model.this.listChangeListener);
        }
      };
      IterableExtensions.<ListProperty<? extends Object>>forEach(_plus_1, _function_1);
      _xblockexpression = (element);
    }
    return _xblockexpression;
  }
  
  public boolean addModelChangeListener(final ModelChangeListener modelChangeListener) {
    boolean _add = this.modelChangeListeners.add(modelChangeListener);
    return _add;
  }
  
  public boolean removeModelChangeListener(final ModelChangeListener modelChangeListener) {
    boolean _remove = this.modelChangeListeners.remove(modelChangeListener);
    return _remove;
  }
  
  protected void notifyListListeners(final ListChangeListener.Change<? extends Object> change) {
    final ObservableList<? extends Object> list = change.getList();
    boolean _matched = false;
    if (!_matched) {
      if (list instanceof ListProperty) {
        _matched=true;
        final Procedure1<ModelChangeListener> _function = new Procedure1<ModelChangeListener>() {
          public void apply(final ModelChangeListener it) {
            it.listPropertyChanged(((ListProperty<? extends Object>)list), change);
          }
        };
        IterableExtensions.<ModelChangeListener>forEach(this.modelChangeListeners, _function);
      }
    }
  }
  
  protected void notifyListeners(final ObservableValue<? extends Object> property, final Object oldValue, final Object newValue) {
    boolean _matched = false;
    if (!_matched) {
      if (property instanceof Property) {
        _matched=true;
        final Procedure1<ModelChangeListener> _function = new Procedure1<ModelChangeListener>() {
          public void apply(final ModelChangeListener it) {
            it.propertyChanged(((Property<? extends Object>)property), oldValue, newValue);
          }
        };
        IterableExtensions.<ModelChangeListener>forEach(this.modelChangeListeners, _function);
      }
    }
  }
}
