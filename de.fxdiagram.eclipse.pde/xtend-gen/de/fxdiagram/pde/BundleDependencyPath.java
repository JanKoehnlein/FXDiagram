package de.fxdiagram.pde;

import com.google.common.collect.Iterables;
import de.fxdiagram.pde.BundleDependency;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class BundleDependencyPath {
  private final List<? extends BundleDependency> elements;
  
  public BundleDependencyPath() {
    this.elements = Collections.<BundleDependency>unmodifiableList(CollectionLiterals.<BundleDependency>newArrayList());
  }
  
  private BundleDependencyPath(final List<BundleDependency> elements) {
    this.elements = elements;
  }
  
  public BundleDependencyPath append(final BundleDependency element) {
    List<BundleDependency> _list = IterableExtensions.<BundleDependency>toList(Iterables.<BundleDependency>concat(this.elements, Collections.<BundleDependency>unmodifiableList(CollectionLiterals.<BundleDependency>newArrayList(element))));
    return new BundleDependencyPath(_list);
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.elements== null) ? 0 : this.elements.hashCode());
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
    BundleDependencyPath other = (BundleDependencyPath) obj;
    if (this.elements == null) {
      if (other.elements != null)
        return false;
    } else if (!this.elements.equals(other.elements))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("elements", this.elements);
    return b.toString();
  }
  
  @Pure
  public List<? extends BundleDependency> getElements() {
    return this.elements;
  }
}
