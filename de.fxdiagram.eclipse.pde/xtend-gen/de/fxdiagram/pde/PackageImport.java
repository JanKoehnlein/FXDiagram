package de.fxdiagram.pde;

import com.google.common.base.Objects;
import de.fxdiagram.pde.BundleDependency;
import org.eclipse.osgi.service.resolver.BaseDescription;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.ImportPackageSpecification;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.osgi.framework.Constants;

@Data
@SuppressWarnings("all")
public class PackageImport extends BundleDependency {
  private final ImportPackageSpecification packageImport;
  
  public BundleDependency.Kind getKind() {
    return BundleDependency.Kind.PACKAGE_IMPORT;
  }
  
  public BundleDescription getDependency() {
    BaseDescription _supplier = this.packageImport.getSupplier();
    BundleDescription _supplier_1 = null;
    if (_supplier!=null) {
      _supplier_1=_supplier.getSupplier();
    }
    return _supplier_1;
  }
  
  public boolean isReexport() {
    return false;
  }
  
  public VersionRange getVersionRange() {
    VersionRange _elvis = null;
    VersionRange _versionRange = this.packageImport.getVersionRange();
    if (_versionRange != null) {
      _elvis = _versionRange;
    } else {
      _elvis = VersionRange.emptyRange;
    }
    return _elvis;
  }
  
  public boolean isOptional() {
    boolean _xblockexpression = false;
    {
      final Object directive = this.packageImport.getDirective(Constants.RESOLUTION_DIRECTIVE);
      _xblockexpression = Objects.equal(directive, "optional");
    }
    return _xblockexpression;
  }
  
  public PackageImport(final BundleDescription owner, final ImportPackageSpecification packageImport) {
    super(owner);
    this.packageImport = packageImport;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.packageImport== null) ? 0 : this.packageImport.hashCode());
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
    PackageImport other = (PackageImport) obj;
    if (this.packageImport == null) {
      if (other.packageImport != null)
        return false;
    } else if (!this.packageImport.equals(other.packageImport))
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
  public ImportPackageSpecification getPackageImport() {
    return this.packageImport;
  }
}
