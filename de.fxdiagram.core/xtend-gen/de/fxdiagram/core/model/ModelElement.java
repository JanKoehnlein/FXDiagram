package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class ModelElement {
  private Object node;
  
  private List<Property<?>> constructorProperties;
  
  private List<Property<?>> properties;
  
  private List<ListProperty<?>> listProperties;
  
  private List<Property<?>> children;
  
  private List<ListProperty<?>> listChildren;
  
  private Map<String,Class<?>> propertyTypes = CollectionLiterals.<String, Class<?>>newHashMap();
  
  public ModelElement(final Object node) {
    this.node = node;
  }
  
  public <T extends Object> Class<?> addConstructorProperty(final Property<T> property, final Class<? extends T> propertyType) {
    Class<?> _xifexpression = null;
    boolean _notEquals = (!Objects.equal(property, null));
    if (_notEquals) {
      Class<?> _xblockexpression = null;
      {
        boolean _equals = Objects.equal(this.constructorProperties, null);
        if (_equals) {
          ArrayList<Property<?>> _newArrayList = CollectionLiterals.<Property<?>>newArrayList();
          this.constructorProperties = _newArrayList;
        }
        this.constructorProperties.add(property);
        String _name = property.getName();
        _xblockexpression = this.propertyTypes.put(_name, propertyType);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  public <T extends Object> Class<?> addProperty(final Property<T> property, final Class<? extends T> propertyType) {
    Class<?> _xifexpression = null;
    boolean _notEquals = (!Objects.equal(property, null));
    if (_notEquals) {
      Class<?> _xblockexpression = null;
      {
        boolean _equals = Objects.equal(this.properties, null);
        if (_equals) {
          ArrayList<Property<?>> _newArrayList = CollectionLiterals.<Property<?>>newArrayList();
          this.properties = _newArrayList;
        }
        this.properties.add(property);
        String _name = property.getName();
        _xblockexpression = this.propertyTypes.put(_name, propertyType);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  public <T extends Object> Class<?> addProperty(final ListProperty<T> listProperty, final Class<? extends T> componentType) {
    Class<?> _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.listProperties, null);
      if (_equals) {
        ArrayList<ListProperty<?>> _newArrayList = CollectionLiterals.<ListProperty<?>>newArrayList();
        this.listProperties = _newArrayList;
      }
      this.listProperties.add(listProperty);
      String _name = listProperty.getName();
      _xblockexpression = this.propertyTypes.put(_name, componentType);
    }
    return _xblockexpression;
  }
  
  public <T extends Object> Class<?> addChildProperty(final Property<T> child, final Class<? extends T> propertyType) {
    Class<?> _xifexpression = null;
    boolean _notEquals = (!Objects.equal(child, null));
    if (_notEquals) {
      Class<?> _xblockexpression = null;
      {
        boolean _equals = Objects.equal(this.children, null);
        if (_equals) {
          ArrayList<Property<?>> _newArrayList = CollectionLiterals.<Property<?>>newArrayList();
          this.children = _newArrayList;
        }
        this.children.add(child);
        String _name = child.getName();
        _xblockexpression = this.propertyTypes.put(_name, propertyType);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  public <T extends Object> Class<?> addChildProperty(final ListProperty<T> listChild, final Class<? extends T> componentType) {
    Class<?> _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.listChildren, null);
      if (_equals) {
        ArrayList<ListProperty<?>> _newArrayList = CollectionLiterals.<ListProperty<?>>newArrayList();
        this.listChildren = _newArrayList;
      }
      this.listChildren.add(listChild);
      String _name = listChild.getName();
      _xblockexpression = this.propertyTypes.put(_name, componentType);
    }
    return _xblockexpression;
  }
  
  public List<Property<?>> getConstructorProperties() {
    List<Property<?>> _elvis = null;
    if (this.constructorProperties != null) {
      _elvis = this.constructorProperties;
    } else {
      List<Property<?>> _emptyList = CollectionLiterals.<Property<?>>emptyList();
      _elvis = _emptyList;
    }
    return _elvis;
  }
  
  public List<Property<?>> getChildren() {
    List<Property<?>> _elvis = null;
    if (this.children != null) {
      _elvis = this.children;
    } else {
      List<Property<?>> _emptyList = CollectionLiterals.<Property<?>>emptyList();
      _elvis = _emptyList;
    }
    return _elvis;
  }
  
  public List<ListProperty<?>> getListChildren() {
    List<ListProperty<?>> _elvis = null;
    if (this.listChildren != null) {
      _elvis = this.listChildren;
    } else {
      List<ListProperty<?>> _emptyList = CollectionLiterals.<ListProperty<?>>emptyList();
      _elvis = _emptyList;
    }
    return _elvis;
  }
  
  public List<Property<?>> getProperties() {
    List<Property<?>> _elvis = null;
    if (this.properties != null) {
      _elvis = this.properties;
    } else {
      List<Property<?>> _emptyList = CollectionLiterals.<Property<?>>emptyList();
      _elvis = _emptyList;
    }
    return _elvis;
  }
  
  public List<ListProperty<?>> getListProperties() {
    List<ListProperty<?>> _elvis = null;
    if (this.listProperties != null) {
      _elvis = this.listProperties;
    } else {
      List<ListProperty<?>> _emptyList = CollectionLiterals.<ListProperty<?>>emptyList();
      _elvis = _emptyList;
    }
    return _elvis;
  }
  
  public Class<?> getType(final Property<?> property) {
    String _name = property.getName();
    return this.propertyTypes.get(_name);
  }
  
  public Object getNode() {
    return this.node;
  }
}
