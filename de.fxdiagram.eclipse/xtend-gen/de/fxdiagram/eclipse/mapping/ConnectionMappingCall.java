package de.fxdiagram.eclipse.mapping;

import de.fxdiagram.eclipse.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.eclipse.mapping.ConnectionMapping;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class ConnectionMappingCall<RESULT extends Object, ARG extends Object> extends AbstractConnectionMappingCall<RESULT, ARG> {
  private final Function1<? super ARG, ? extends RESULT> selector;
  
  private final ConnectionMapping<RESULT> connectionMapping;
  
  public ConnectionMappingCall(final Function1<? super ARG, ? extends RESULT> selector, final ConnectionMapping<RESULT> connectionMapping) {
    super();
    this.selector = selector;
    this.connectionMapping = connectionMapping;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
  public Function1<? super ARG, ? extends RESULT> getSelector() {
    return this.selector;
  }
  
  @Pure
  public ConnectionMapping<RESULT> getConnectionMapping() {
    return this.connectionMapping;
  }
}
