package de.fxdiagram.examples.java;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class JavaProperty {
  private final String _name;
  
  public String getName() {
    return this._name;
  }
  
  private final Class<?> _type;
  
  public Class<?> getType() {
    return this._type;
  }
  
  public JavaProperty(final String name, final Class<?> type) {
    super();
    this._name = name;
    this._type = type;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_name== null) ? 0 : _name.hashCode());
    result = prime * result + ((_type== null) ? 0 : _type.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    JavaProperty other = (JavaProperty) obj;
    if (_name == null) {
      if (other._name != null)
        return false;
    } else if (!_name.equals(other._name))
      return false;
    if (_type == null) {
      if (other._type != null)
        return false;
    } else if (!_type.equals(other._type))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
