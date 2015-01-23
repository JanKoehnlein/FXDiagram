package de.fxdiagram.core.model;

import com.google.common.base.Objects;
import de.fxdiagram.core.model.ModelElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;

@SuppressWarnings("all")
public class ModelElementImpl implements ModelElement {
  private Object node;
  
  private List<Property<?>> properties;
  
  private List<ListProperty<?>> listProperties;
  
  private Map<String, Class<?>> propertyTypes = CollectionLiterals.<String, Class<?>>newHashMap();
  
  public ModelElementImpl(final Object node) {
    this.node = node;
  }
  
  public Class<?> addProperty(final Property<?> property, final Class<?> propertyType) {
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
  
  public Class<?> addProperty(final ListProperty<?> listProperty, final Class<?> componentType) {
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
  
  public List<? extends Property<?>> getProperties() {
    List<? extends Property<?>> _elvis = null;
    if (this.properties != null) {
      _elvis = this.properties;
    } else {
      List<Property<?>> _emptyList = CollectionLiterals.<Property<?>>emptyList();
      _elvis = _emptyList;
    }
    return _elvis;
  }
  
  public List<? extends ListProperty<?>> getListProperties() {
    List<? extends ListProperty<?>> _elvis = null;
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
  
  public boolean isPrimitive(final Property<?> property) {
    final Class<?> type = this.getType(property);
    boolean _or = false;
    boolean _contains = Collections.<Class<? extends Object>>unmodifiableSet(CollectionLiterals.<Class<? extends Object>>newHashSet(Double.class, Float.class, Integer.class, Long.class, Boolean.class, String.class)).contains(type);
    if (_contains) {
      _or = true;
    } else {
      boolean _isAssignableFrom = Enum.class.isAssignableFrom(type);
      _or = _isAssignableFrom;
    }
    return _or;
  }
  
  public Object getNode() {
    return this.node;
  }
}
