package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractNodeMappingCall;
import de.fxdiagram.xtext.glue.mapping.NodeMapping;
import java.util.List;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class MultiNodeMappingCall<T extends Object, U extends Object> extends AbstractNodeMappingCall<T> {
  private final Function1<? super U,? extends List<? extends T>> _selector;
  
  public Function1<? super U,? extends List<? extends T>> getSelector() {
    return this._selector;
  }
  
  private final NodeMapping<T> _nodeMapping;
  
  public NodeMapping<T> getNodeMapping() {
    return this._nodeMapping;
  }
  
  public MultiNodeMappingCall(final Function1<? super U,? extends List<? extends T>> selector, final NodeMapping<T> nodeMapping) {
    super();
    this._selector = selector;
    this._nodeMapping = nodeMapping;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((_selector== null) ? 0 : _selector.hashCode());
    result = prime * result + ((_nodeMapping== null) ? 0 : _nodeMapping.hashCode());
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
    MultiNodeMappingCall other = (MultiNodeMappingCall) obj;
    if (_selector == null) {
      if (other._selector != null)
        return false;
    } else if (!_selector.equals(other._selector))
      return false;
    if (_nodeMapping == null) {
      if (other._nodeMapping != null)
        return false;
    } else if (!_nodeMapping.equals(other._nodeMapping))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
