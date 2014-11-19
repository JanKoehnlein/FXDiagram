package de.fxdiagram.pde;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public abstract class BundleDependency {
  public enum Kind {
    PACKAGE_IMPORT,
    
    REQUIRE_BUNDLE,
    
    FRAGMENT_HOST,
    
    UNQUALIFIED;
  }
  
  private final BundleDescription owner;
  
  public abstract BundleDescription getDependency();
  
  public abstract boolean isReexport();
  
  public abstract boolean isOptional();
  
  public abstract VersionRange getVersionRange();
  
  public abstract BundleDependency.Kind getKind();
  
  public BundleDependency(final BundleDescription owner) {
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
    BundleDependency other = (BundleDependency) obj;
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
  public BundleDescription getOwner() {
    return this.owner;
  }
}
