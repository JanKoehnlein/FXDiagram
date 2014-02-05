package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class ModelElement {
  private Object node;
  
  private List<Property<? extends Object>> properties;
  
  private List<ListProperty<? extends Object>> listProperties;
  
  private List<Property<? extends Object>> children;
  
  private List<ListProperty<? extends Object>> listChildren;
  
  private Map<String,Class<? extends Object>> propertyTypes = CollectionLiterals.<String, Class<? extends Object>>newHashMap();
  
  public ModelElement(final Object node) {
    this.node = node;
  }
  
  public <T extends Object> Class<? extends Object> addProperty(final Property<T> property, final Class<? extends T> propertyType) {
    Class<? extends Object> _xifexpression = null;
    boolean _notEquals = (!Objects.equal(property, null));
    if (_notEquals) {
      Class<? extends Object> _xblockexpression = null;
      {
        boolean _equals = Objects.equal(this.properties, null);
        if (_equals) {
          ArrayList<Property<? extends Object>> _newArrayList = CollectionLiterals.<Property<? extends Object>>newArrayList();
          this.properties = _newArrayList;
        }
        this.properties.add(property);
        String _name = property.getName();
        Class<? extends Object> _put = this.propertyTypes.put(_name, propertyType);
        _xblockexpression = (_put);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  public <T extends Object> Class<? extends Object> addProperty(final ListProperty<T> listProperty, final Class<? extends T> componentType) {
    Class<? extends Object> _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.listProperties, null);
      if (_equals) {
        ArrayList<ListProperty<? extends Object>> _newArrayList = CollectionLiterals.<ListProperty<? extends Object>>newArrayList();
        this.listProperties = _newArrayList;
      }
      this.listProperties.add(listProperty);
      String _name = listProperty.getName();
      Class<? extends Object> _put = this.propertyTypes.put(_name, componentType);
      _xblockexpression = (_put);
    }
    return _xblockexpression;
  }
  
  public <T extends Object> Class<? extends Object> addChildProperty(final Property<T> child, final Class<? extends T> propertyType) {
    Class<? extends Object> _xifexpression = null;
    boolean _notEquals = (!Objects.equal(child, null));
    if (_notEquals) {
      Class<? extends Object> _xblockexpression = null;
      {
        boolean _equals = Objects.equal(this.children, null);
        if (_equals) {
          ArrayList<Property<? extends Object>> _newArrayList = CollectionLiterals.<Property<? extends Object>>newArrayList();
          this.children = _newArrayList;
        }
        this.children.add(child);
        String _name = child.getName();
        Class<? extends Object> _put = this.propertyTypes.put(_name, propertyType);
        _xblockexpression = (_put);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  public <T extends Object> Class<? extends Object> addChildProperty(final ListProperty<T> listChild, final Class<? extends T> componentType) {
    Class<? extends Object> _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.listChildren, null);
      if (_equals) {
        ArrayList<ListProperty<? extends Object>> _newArrayList = CollectionLiterals.<ListProperty<? extends Object>>newArrayList();
        this.listChildren = _newArrayList;
      }
      this.listChildren.add(listChild);
      String _name = listChild.getName();
      Class<? extends Object> _put = this.propertyTypes.put(_name, componentType);
      _xblockexpression = (_put);
    }
    return _xblockexpression;
  }
  
  public List<Property<? extends Object>> getChildren() {
    List<Property<? extends Object>> _elvis = null;
    if (this.children != null) {
      _elvis = this.children;
    } else {
      List<Property<? extends Object>> _emptyList = CollectionLiterals.<Property<? extends Object>>emptyList();
      _elvis = ObjectExtensions.<List<Property<? extends Object>>>operator_elvis(
        this.children, _emptyList);
    }
    return _elvis;
  }
  
  public List<ListProperty<? extends Object>> getListChildren() {
    List<ListProperty<? extends Object>> _elvis = null;
    if (this.listChildren != null) {
      _elvis = this.listChildren;
    } else {
      List<ListProperty<? extends Object>> _emptyList = CollectionLiterals.<ListProperty<? extends Object>>emptyList();
      _elvis = ObjectExtensions.<List<ListProperty<? extends Object>>>operator_elvis(
        this.listChildren, _emptyList);
    }
    return _elvis;
  }
  
  public List<Property<? extends Object>> getProperties() {
    List<Property<? extends Object>> _elvis = null;
    if (this.properties != null) {
      _elvis = this.properties;
    } else {
      List<Property<? extends Object>> _emptyList = CollectionLiterals.<Property<? extends Object>>emptyList();
      _elvis = ObjectExtensions.<List<Property<? extends Object>>>operator_elvis(
        this.properties, _emptyList);
    }
    return _elvis;
  }
  
  public List<ListProperty<? extends Object>> getListProperties() {
    List<ListProperty<? extends Object>> _elvis = null;
    if (this.listProperties != null) {
      _elvis = this.listProperties;
    } else {
      List<ListProperty<? extends Object>> _emptyList = CollectionLiterals.<ListProperty<? extends Object>>emptyList();
      _elvis = ObjectExtensions.<List<ListProperty<? extends Object>>>operator_elvis(
        this.listProperties, _emptyList);
    }
    return _elvis;
  }
  
  public Class<? extends Object> getType(final Property<? extends Object> property) {
    String _name = property.getName();
    Class<? extends Object> _get = this.propertyTypes.get(_name);
    return _get;
  }
  
  public Object getNode() {
    return this.node;
  }
}
