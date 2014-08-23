package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class ConnectionMappingCall<T extends Object, ARG extends Object> extends AbstractConnectionMappingCall<T, ARG> {
  private final Function1<? super ARG, ? extends T> selector;
  
  private final ConnectionMapping<T> connectionMapping;
  
  public ConnectionMappingCall(final Function1<? super ARG, ? extends T> selector, final ConnectionMapping<T> connectionMapping) {
    super();
    this.selector = selector;
    this.connectionMapping = connectionMapping;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((this.selector== null) ? 0 : this.selector.hashCode());
    result = prime * result + ((this.connectionMapping== null) ? 0 : this.connectionMapping.hashCode());
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
    ConnectionMappingCall<?, ?> other = (ConnectionMappingCall<?, ?>) obj;
    if (this.selector == null) {
      if (other.selector != null)
        return false;
    } else if (!this.selector.equals(other.selector))
      return false;
    if (this.connectionMapping == null) {
      if (other.connectionMapping != null)
        return false;
    } else if (!this.connectionMapping.equals(other.connectionMapping))
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
  public ConnectionMapping<T> getConnectionMapping() {
    return this.connectionMapping;
  }
}
