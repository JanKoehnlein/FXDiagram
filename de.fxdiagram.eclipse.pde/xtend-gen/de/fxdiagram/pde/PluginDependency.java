package de.fxdiagram.pde;

import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public abstract class PluginDependency {
  private final IPluginModelBase owner;
  
  public abstract IPluginModelBase getDependency();
  
  public abstract boolean isReexport();
  
  public abstract boolean isOptional();
  
  public abstract VersionRange getVersionRange();
  
  public PluginDependency(final IPluginModelBase owner) {
    super();
    this.owner = owner;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.owner== null) ? 0 : this.owner.hashCode());
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
    PluginDependency other = (PluginDependency) obj;
    if (this.owner == null) {
      if (other.owner != null)
        return false;
    } else if (!this.owner.equals(other.owner))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("owner", this.owner);
    return b.toString();
  }
  
  @Pure
  public IPluginModelBase getOwner() {
    return this.owner;
  }
}
