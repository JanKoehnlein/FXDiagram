package de.fxdiagram.pde;

import de.fxdiagram.pde.BundleDependency;
import org.eclipse.osgi.service.resolver.BaseDescription;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.HostSpecification;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class FragmentHost extends BundleDependency {
  private final BundleDescription host;
  
  private final HostSpecification hostSpecification;
  
  public BundleDependency.Kind getKind() {
    return BundleDependency.Kind.FRAGMENT_HOST;
  }
  
  public BundleDescription getDependency() {
    BaseDescription _supplier = this.hostSpecification.getSupplier();
    BundleDescription _supplier_1 = null;
    if (_supplier!=null) {
      _supplier_1=_supplier.getSupplier();
    }
    return _supplier_1;
  }
  
  public VersionRange getVersionRange() {
    VersionRange _elvis = null;
    VersionRange _versionRange = this.hostSpecification.getVersionRange();
    if (_versionRange != null) {
      _elvis = _versionRange;
    } else {
      _elvis = VersionRange.emptyRange;
    }
    return _elvis;
  }
  
  public boolean isReexport() {
    return false;
  }
  
  public boolean isOptional() {
    return false;
  }
  
  public FragmentHost(final BundleDescription owner, final BundleDescription host, final HostSpecification hostSpecification) {
    super(owner);
    this.host = host;
    this.hostSpecification = hostSpecification;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.host== null) ? 0 : this.host.hashCode());
    result = prime * result + ((this.hostSpecification== null) ? 0 : this.hostSpecification.hashCode());
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
    if (this.host == null) {
      if (other.host != null)
        return false;
    } else if (!this.host.equals(other.host))
      return false;
    if (this.hostSpecification == null) {
      if (other.hostSpecification != null)
        return false;
    } else if (!this.hostSpecification.equals(other.hostSpecification))
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
  public BundleDescription getHost() {
    return this.host;
  }
  
  @Pure
  public HostSpecification getHostSpecification() {
    return this.hostSpecification;
  }
}
