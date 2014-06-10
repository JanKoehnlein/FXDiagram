package de.fxdiagram.core.services;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ResourceHandle {
  private final String _name;
  
  public String getName() {
    return this._name;
  }
  
  private final Class<?> _context;
  
  public Class<?> getContext() {
    return this._context;
  }
  
  private final String _relativePath;
  
  public String getRelativePath() {
    return this._relativePath;
  }
  
  public ResourceHandle(final String name, final Class<?> context, final String relativePath) {
    super();
    this._name = name;
    this._context = context;
    this._relativePath = relativePath;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._name== null) ? 0 : this._name.hashCode());
    result = prime * result + ((this._context== null) ? 0 : this._context.hashCode());
    result = prime * result + ((this._relativePath== null) ? 0 : this._relativePath.hashCode());
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
    ResourceHandle other = (ResourceHandle) obj;
    if (this._name == null) {
      if (other._name != null)
        return false;
    } else if (!this._name.equals(other._name))
      return false;
    if (this._context == null) {
      if (other._context != null)
        return false;
    } else if (!this._context.equals(other._context))
      return false;
    if (this._relativePath == null) {
      if (other._relativePath != null)
        return false;
    } else if (!this._relativePath.equals(other._relativePath))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
