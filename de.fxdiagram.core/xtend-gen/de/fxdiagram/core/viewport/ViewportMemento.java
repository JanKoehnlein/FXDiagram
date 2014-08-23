package de.fxdiagram.core.viewport;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Pure;

@Data
@SuppressWarnings("all")
public class ViewportMemento {
  private final double translateX;
  
  private final double translateY;
  
  private final double scale;
  
  private final double rotate;
  
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("ViewportMemento [translateX=");
    _builder.append(this.translateX, "");
    _builder.append(", translateY=");
    _builder.append(this.translateY, "");
    _builder.append(", scale=");
    _builder.append(this.scale, "");
    _builder.append(", rotate=");
    _builder.append(this.rotate, "");
    _builder.append("]");
    return _builder.toString();
  }
  
  public ViewportMemento(final double translateX, final double translateY, final double scale, final double rotate) {
    super();
    this.translateX = translateX;
    this.translateY = translateY;
    this.scale = scale;
    this.rotate = rotate;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (Double.doubleToLongBits(this.translateX) ^ (Double.doubleToLongBits(this.translateX) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(this.translateY) ^ (Double.doubleToLongBits(this.translateY) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(this.scale) ^ (Double.doubleToLongBits(this.scale) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(this.rotate) ^ (Double.doubleToLongBits(this.rotate) >>> 32));
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
    ViewportMemento other = (ViewportMemento) obj;
    if (Double.doubleToLongBits(other.translateX) != Double.doubleToLongBits(this.translateX))
      return false; 
    if (Double.doubleToLongBits(other.translateY) != Double.doubleToLongBits(this.translateY))
      return false; 
    if (Double.doubleToLongBits(other.scale) != Double.doubleToLongBits(this.scale))
      return false; 
    if (Double.doubleToLongBits(other.rotate) != Double.doubleToLongBits(this.rotate))
      return false; 
    return true;
  }
  
  @Pure
  public double getTranslateX() {
    return this.translateX;
  }
  
  @Pure
  public double getTranslateY() {
    return this.translateY;
  }
  
  @Pure
  public double getScale() {
    return this.scale;
  }
  
  @Pure
  public double getRotate() {
    return this.rotate;
  }
}
