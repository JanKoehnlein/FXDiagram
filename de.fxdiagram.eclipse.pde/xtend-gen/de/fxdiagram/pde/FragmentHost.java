package de.fxdiagram.pde;

import de.fxdiagram.pde.BundleDependency;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class FragmentHost extends BundleDependency {
  private final BundleDescription fragment;
  
  public BundleDependency.Kind getKind() {
    return BundleDependency.Kind.FRAGMENT_HOST;
  }
  
  public BundleDescription getDependency() {
    return this.fragment;
  }
  
  public VersionRange getVersionRange() {
    return VersionRange.emptyRange;
  }
  
  public boolean isReexport() {
    return false;
  }
  
  public boolean isOptional() {
    return false;
  }
  
  public FragmentHost(final BundleDescription owner, final BundleDescription fragment) {
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
  public BundleDescription getFragment() {
    return this.fragment;
  }
}
