package de.fxdiagram.core.services;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class ResourceHandle {
  private final String name;
  
  private final Class<?> context;
  
  private final String relativePath;
  
  public ResourceHandle(final String name, final Class<?> context, final String relativePath) {
    super();
    this.name = name;
    this.context = context;
    this.relativePath = relativePath;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.name== null) ? 0 : this.name.hashCode());
    result = prime * result + ((this.context== null) ? 0 : this.context.hashCode());
    result = prime * result + ((this.relativePath== null) ? 0 : this.relativePath.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ResourceHandle other = (ResourceHandle) obj;
    if (this.name == null) {
      if (other.name != null)
        return false;
    } else if (!this.name.equals(other.name))
      return false;
    if (this.context == null) {
      if (other.context != null)
        return false;
    } else if (!this.context.equals(other.context))
      return false;
    if (this.relativePath == null) {
      if (other.relativePath != null)
        return false;
    } else if (!this.relativePath.equals(other.relativePath))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("name", this.name);
    b.add("context", this.context);
    b.add("relativePath", this.relativePath);
    return b.toString();
  }
  
  @Pure
  public String getName() {
    return this.name;
  }
  
  @Pure
  public Class<?> getContext() {
    return this.context;
  }
  
  @Pure
  public String getRelativePath() {
    return this.relativePath;
  }
}
