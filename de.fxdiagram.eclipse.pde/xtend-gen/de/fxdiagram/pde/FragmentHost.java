package de.fxdiagram.pde;

import de.fxdiagram.pde.PluginDependency;
import de.fxdiagram.pde.PluginUtil;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.plugin.IFragment;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class FragmentHost extends PluginDependency {
  private final IFragment fragment;
  
  public IPluginModelBase getDependency() {
    String _pluginId = this.fragment.getPluginId();
    String _version = this.fragment.getVersion();
    return PluginUtil.findPlugin(_pluginId, _version);
  }
  
  public VersionRange getVersionRange() {
    String _pluginVersion = this.fragment.getPluginVersion();
    return new VersionRange(_pluginVersion);
  }
  
  public boolean isReexport() {
    return false;
  }
  
  public boolean isOptional() {
    return false;
  }
  
  public FragmentHost(final IPluginModelBase owner, final IFragment fragment) {
    super(owner);
    this.fragment = fragment;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.fragment== null) ? 0 : this.fragment.hashCode());
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
    if (!super.equals(obj))
      return false;
    FragmentHost other = (FragmentHost) obj;
    if (this.fragment == null) {
      if (other.fragment != null)
        return false;
    } else if (!this.fragment.equals(other.fragment))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    String result = new ToStringBuilder(this)
    	.addAllFields()
    	.toString();
    return result;
  }
  
  @Pure
  public IFragment getFragment() {
    return this.fragment;
  }
}
