package de.fxdiagram.core.model;

import javafx.beans.property.Property;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class CrossRefData {
  private final String href;
  
  private final Property<?> property;
  
  private final int index;
  
  public CrossRefData(final String href, final Property<?> property, final int index) {
    super();
    this.href = href;
    this.property = property;
    this.index = index;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.href== null) ? 0 : this.href.hashCode());
    result = prime * result + ((this.property== null) ? 0 : this.property.hashCode());
    result = prime * result + this.index;
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
    CrossRefData other = (CrossRefData) obj;
    if (this.href == null) {
      if (other.href != null)
        return false;
    } else if (!this.href.equals(other.href))
      return false;
    if (this.property == null) {
      if (other.property != null)
        return false;
    } else if (!this.property.equals(other.property))
      return false;
    if (other.index != this.index)
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("href", this.href);
    b.add("property", this.property);
    b.add("index", this.index);
    return b.toString();
  }
  
  @Pure
  public String getHref() {
    return this.href;
  }
  
  @Pure
  public Property<?> getProperty() {
    return this.property;
  }
  
  @Pure
  public int getIndex() {
    return this.index;
  }
}
