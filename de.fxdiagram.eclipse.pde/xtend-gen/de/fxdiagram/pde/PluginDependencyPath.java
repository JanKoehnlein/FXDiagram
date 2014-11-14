package de.fxdiagram.pde;

import com.google.common.collect.Iterables;
import de.fxdiagram.pde.PluginDependency;
import java.util.Collections;
import java.util.List;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class PluginDependencyPath {
  private final List<? extends PluginDependency> elements;
  
  public PluginDependencyPath() {
    this.elements = Collections.<PluginDependency>unmodifiableList(CollectionLiterals.<PluginDependency>newArrayList());
  }
  
  private PluginDependencyPath(final List<PluginDependency> elements) {
    this.elements = elements;
  }
  
  public PluginDependencyPath append(final PluginDependency element) {
    Iterable<PluginDependency> _plus = Iterables.<PluginDependency>concat(this.elements, Collections.<PluginDependency>unmodifiableList(CollectionLiterals.<PluginDependency>newArrayList(element)));
    List<PluginDependency> _list = IterableExtensions.<PluginDependency>toList(_plus);
    return new PluginDependencyPath(_list);
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
    PluginDependencyPath other = (PluginDependencyPath) obj;
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
  public List<? extends PluginDependency> getElements() {
    return this.elements;
  }
}
