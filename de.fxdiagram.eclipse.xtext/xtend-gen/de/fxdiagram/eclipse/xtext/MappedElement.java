package de.fxdiagram.eclipse.xtext;

import de.fxdiagram.mapping.AbstractMapping;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class MappedElement<ECLASS_OR_ESETTING extends Object> {
  private final ECLASS_OR_ESETTING element;
  
  private final AbstractMapping<ECLASS_OR_ESETTING> mapping;
  
  public MappedElement(final ECLASS_OR_ESETTING element, final AbstractMapping<ECLASS_OR_ESETTING> mapping) {
    super();
    this.element = element;
    this.mapping = mapping;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.element== null) ? 0 : this.element.hashCode());
    result = prime * result + ((this.mapping== null) ? 0 : this.mapping.hashCode());
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
    MappedElement<?> other = (MappedElement<?>) obj;
    if (this.element == null) {
      if (other.element != null)
        return false;
    } else if (!this.element.equals(other.element))
      return false;
    if (this.mapping == null) {
      if (other.mapping != null)
        return false;
    } else if (!this.mapping.equals(other.mapping))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("element", this.element);
    b.add("mapping", this.mapping);
    return b.toString();
  }
  
  @Pure
  public ECLASS_OR_ESETTING getElement() {
    return this.element;
  }
  
  @Pure
  public AbstractMapping<ECLASS_OR_ESETTING> getMapping() {
    return this.mapping;
  }
}
