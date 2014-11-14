package de.fxdiagram.pde;

import de.fxdiagram.pde.PluginDependency;
import de.fxdiagram.pde.PluginUtil;
import org.eclipse.osgi.service.resolver.BaseDescription;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class RequireBundle extends PluginDependency {
  private final BundleSpecification required;
  
  public IPluginModelBase getDependency() {
    BaseDescription _supplier = this.required.getSupplier();
    BundleDescription _supplier_1 = _supplier.getSupplier();
    return PluginUtil.getPlugin(_supplier_1);
  }
  
  public VersionRange getVersionRange() {
    VersionRange _elvis = null;
    VersionRange _versionRange = this.required.getVersionRange();
    if (_versionRange != null) {
      _elvis = _versionRange;
    } else {
      _elvis = VersionRange.emptyRange;
    }
    return _elvis;
  }
  
  public boolean isReexport() {
    return this.required.isExported();
  }
  
  public boolean isOptional() {
    return this.required.isOptional();
  }
  
  public RequireBundle(final IPluginModelBase owner, final BundleSpecification required) {
    super(owner);
    this.required = required;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.required== null) ? 0 : this.required.hashCode());
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
    RequireBundle other = (RequireBundle) obj;
    if (this.required == null) {
      if (other.required != null)
        return false;
    } else if (!this.required.equals(other.required))
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
  public BundleSpecification getRequired() {
    return this.required;
  }
}
