package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractConnectionMappingCall;
import de.fxdiagram.xtext.glue.mapping.ConnectionMapping;
import java.util.List;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class MultiConnectionMappingCall<T extends Object, U extends Object> extends AbstractConnectionMappingCall<T> {
  private final Function1<? super U,? extends List<? extends T>> _selector;
  
  public Function1<? super U,? extends List<? extends T>> getSelector() {
    return this._selector;
  }
  
  private final ConnectionMapping<T> _connectionMapping;
  
  public ConnectionMapping<T> getConnectionMapping() {
    return this._connectionMapping;
  }
  
  public MultiConnectionMappingCall(final Function1<? super U,? extends List<? extends T>> selector, final ConnectionMapping<T> connectionMapping) {
    super();
    this._selector = selector;
    this._connectionMapping = connectionMapping;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((_selector== null) ? 0 : _selector.hashCode());
    result = prime * result + ((_connectionMapping== null) ? 0 : _connectionMapping.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    if (!super.equals(obj))
      return false;
    MultiConnectionMappingCall other = (MultiConnectionMappingCall) obj;
    if (_selector == null) {
      if (other._selector != null)
        return false;
    } else if (!_selector.equals(other._selector))
      return false;
    if (_connectionMapping == null) {
      if (other._connectionMapping != null)
        return false;
    } else if (!_connectionMapping.equals(other._connectionMapping))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
