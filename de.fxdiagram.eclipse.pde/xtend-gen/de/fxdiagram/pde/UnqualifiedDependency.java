package de.fxdiagram.pde;

import de.fxdiagram.pde.BundleDependency;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;
import org.osgi.framework.Version;

@Data
@SuppressWarnings("all")
public class UnqualifiedDependency extends BundleDependency {
  private final BundleDescription dependency;
  
  public boolean isReexport() {
    return false;
  }
  
  public boolean isOptional() {
    return false;
  }
  
  public VersionRange getVersionRange() {
    Version _version = this.dependency.getVersion();
    Version _version_1 = this.dependency.getVersion();
    return new VersionRange(_version, true, _version_1, true);
  }
  
  public BundleDependency.Kind getKind() {
    return BundleDependency.Kind.UNQUALIFIED;
  }
  
  public UnqualifiedDependency(final BundleDescription owner, final BundleDescription dependency) {
    super(owner);
    this.dependency = dependency;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.dependency== null) ? 0 : this.dependency.hashCode());
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
    UnqualifiedDependency other = (UnqualifiedDependency) obj;
    if (this.dependency == null) {
      if (other.dependency != null)
        return false;
    } else if (!this.dependency.equals(other.dependency))
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
  public BundleDescription getDependency() {
    return this.dependency;
  }
}
