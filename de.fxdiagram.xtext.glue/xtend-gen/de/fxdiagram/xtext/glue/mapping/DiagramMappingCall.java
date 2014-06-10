package de.fxdiagram.xtext.glue.mapping;

import de.fxdiagram.xtext.glue.mapping.AbstractMapping;
import de.fxdiagram.xtext.glue.mapping.DiagramMapping;
import de.fxdiagram.xtext.glue.mapping.MappingCall;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class DiagramMappingCall<T extends Object, ARG extends Object> implements MappingCall<T, ARG> {
  private final Function1<? super ARG, ? extends T> _selector;
  
  public Function1<? super ARG, ? extends T> getSelector() {
    return this._selector;
  }
  
  private final DiagramMapping<T> _diagramMapping;
  
  public DiagramMapping<T> getDiagramMapping() {
    return this._diagramMapping;
  }
  
  public AbstractMapping<T> getMapping() {
    return this.getDiagramMapping();
  }
  
  public DiagramMappingCall(final Function1<? super ARG, ? extends T> selector, final DiagramMapping<T> diagramMapping) {
    super();
    this._selector = selector;
    this._diagramMapping = diagramMapping;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this._selector== null) ? 0 : this._selector.hashCode());
    result = prime * result + ((this._diagramMapping== null) ? 0 : this._diagramMapping.hashCode());
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
    DiagramMappingCall other = (DiagramMappingCall) obj;
    if (this._selector == null) {
      if (other._selector != null)
        return false;
    } else if (!this._selector.equals(other._selector))
      return false;
    if (this._diagramMapping == null) {
      if (other._diagramMapping != null)
        return false;
    } else if (!this._diagramMapping.equals(other._diagramMapping))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
