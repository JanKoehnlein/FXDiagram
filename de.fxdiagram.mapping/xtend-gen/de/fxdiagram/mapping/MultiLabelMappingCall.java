package de.fxdiagram.mapping;

import de.fxdiagram.mapping.AbstractLabelMapping;
import de.fxdiagram.mapping.AbstractLabelMappingCall;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class MultiLabelMappingCall<RESULT extends Object, ARG extends Object> extends AbstractLabelMappingCall<RESULT, ARG> {
  private final Function1<? super ARG, ? extends Iterable<? extends RESULT>> selector;
  
  private final AbstractLabelMapping<RESULT> labelMapping;
  
  public MultiLabelMappingCall(final Function1<? super ARG, ? extends Iterable<? extends RESULT>> selector, final AbstractLabelMapping<RESULT> labelMapping) {
    super();
    this.selector = selector;
    this.labelMapping = labelMapping;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.selector== null) ? 0 : this.selector.hashCode());
    result = prime * result + ((this.labelMapping== null) ? 0 : this.labelMapping.hashCode());
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
    MultiLabelMappingCall<?, ?> other = (MultiLabelMappingCall<?, ?>) obj;
    if (this.selector == null) {
      if (other.selector != null)
        return false;
    } else if (!this.selector.equals(other.selector))
      return false;
    if (this.labelMapping == null) {
      if (other.labelMapping != null)
        return false;
    } else if (!this.labelMapping.equals(other.labelMapping))
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
  public Function1<? super ARG, ? extends Iterable<? extends RESULT>> getSelector() {
    return this.selector;
  }
  
  @Pure
  public AbstractLabelMapping<RESULT> getLabelMapping() {
    return this.labelMapping;
  }
}
