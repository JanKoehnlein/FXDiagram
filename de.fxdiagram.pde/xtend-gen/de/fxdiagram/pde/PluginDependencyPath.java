package de.fxdiagram.pde;

import com.google.common.collect.Iterables;
import java.util.Collections;
import java.util.List;
import org.eclipse.pde.core.plugin.IPluginImport;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class PluginDependencyPath {
  @Data
  public static class PathElement {
    private final IPluginImport pluginImport;
    
    private final IPluginModelBase plugin;
    
    public PathElement(final IPluginImport pluginImport, final IPluginModelBase plugin) {
      super();
      this.pluginImport = pluginImport;
      this.plugin = plugin;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.pluginImport== null) ? 0 : this.pluginImport.hashCode());
      result = prime * result + ((this.plugin== null) ? 0 : this.plugin.hashCode());
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
      PluginDependencyPath.PathElement other = (PluginDependencyPath.PathElement) obj;
      if (this.pluginImport == null) {
        if (other.pluginImport != null)
          return false;
      } else if (!this.pluginImport.equals(other.pluginImport))
        return false;
      if (this.plugin == null) {
        if (other.plugin != null)
          return false;
      } else if (!this.plugin.equals(other.plugin))
        return false;
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("pluginImport", this.pluginImport);
      b.add("plugin", this.plugin);
      return b.toString();
    }
    
    @Pure
    public IPluginImport getPluginImport() {
      return this.pluginImport;
    }
    
    @Pure
    public IPluginModelBase getPlugin() {
      return this.plugin;
    }
  }
  
  private final List<? extends PluginDependencyPath.PathElement> elements;
  
  public PluginDependencyPath() {
    this.elements = Collections.<PluginDependencyPath.PathElement>unmodifiableList(CollectionLiterals.<PluginDependencyPath.PathElement>newArrayList());
  }
  
  private PluginDependencyPath(final List<PluginDependencyPath.PathElement> elements) {
    this.elements = elements;
  }
  
  public PluginDependencyPath append(final PluginDependencyPath.PathElement element) {
    Iterable<PluginDependencyPath.PathElement> _plus = Iterables.<PluginDependencyPath.PathElement>concat(this.elements, Collections.<PluginDependencyPath.PathElement>unmodifiableList(CollectionLiterals.<PluginDependencyPath.PathElement>newArrayList(element)));
    List<PluginDependencyPath.PathElement> _list = IterableExtensions.<PluginDependencyPath.PathElement>toList(_plus);
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
  public List<? extends PluginDependencyPath.PathElement> getElements() {
    return this.elements;
  }
}
