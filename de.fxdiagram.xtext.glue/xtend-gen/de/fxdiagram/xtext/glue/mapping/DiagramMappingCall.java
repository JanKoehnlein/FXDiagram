package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class DiagramMappingCall<T extends Object, ARG extends Object> implements MappingCall<T, ARG> {
  private final Function1<? super ARG, ? extends T> selector;
  
  private final DiagramMapping<T> diagramMapping;
  
  public AbstractMapping<T> getMapping() {
    return this.diagramMapping;
  }
  
  public DiagramMappingCall(final Function1<? super ARG, ? extends T> selector, final DiagramMapping<T> diagramMapping) {
    super();
    this.selector = selector;
    this.diagramMapping = diagramMapping;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.selector== null) ? 0 : this.selector.hashCode());
    result = prime * result + ((this.diagramMapping== null) ? 0 : this.diagramMapping.hashCode());
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
    DiagramMappingCall<?, ?> other = (DiagramMappingCall<?, ?>) obj;
    if (this.selector == null) {
      if (other.selector != null)
        return false;
    } else if (!this.selector.equals(other.selector))
      return false;
    if (this.diagramMapping == null) {
      if (other.diagramMapping != null)
        return false;
    } else if (!this.diagramMapping.equals(other.diagramMapping))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("selector", this.selector);
    b.add("diagramMapping", this.diagramMapping);
    return b.toString();
  }
  
  @Pure
  public Function1<? super ARG, ? extends T> getSelector() {
    return this.selector;
  }
  
  @Pure
  public DiagramMapping<T> getDiagramMapping() {
    return this.diagramMapping;
  }
}
