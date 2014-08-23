package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractNodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class NodeMappingCall<T extends Object, ARG extends Object> extends AbstractNodeMappingCall<T, ARG> {
  private final Function1<? super ARG, ? extends T> selector;
  
  private final NodeMapping<T> nodeMapping;
  
  public NodeMappingCall(final Function1<? super ARG, ? extends T> selector, final NodeMapping<T> nodeMapping) {
    super();
    this.selector = selector;
    this.nodeMapping = nodeMapping;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.selector== null) ? 0 : this.selector.hashCode());
    result = prime * result + ((this.nodeMapping== null) ? 0 : this.nodeMapping.hashCode());
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
    NodeMappingCall<?, ?> other = (NodeMappingCall<?, ?>) obj;
    if (this.selector == null) {
      if (other.selector != null)
        return false;
    } else if (!this.selector.equals(other.selector))
      return false;
    if (this.nodeMapping == null) {
      if (other.nodeMapping != null)
        return false;
    } else if (!this.nodeMapping.equals(other.nodeMapping))
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
  public Function1<? super ARG, ? extends T> getSelector() {
    return this.selector;
  }
  
  @Pure
  public NodeMapping<T> getNodeMapping() {
    return this.nodeMapping;
  }
}
