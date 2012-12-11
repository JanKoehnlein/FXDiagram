package de.itemis.javafx.diagram.behavior;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class DragContext {
  private final double _mouseAnchorX;
  
  public double getMouseAnchorX() {
    return this._mouseAnchorX;
  }
  
  private final double _mouseAnchorY;
  
  public double getMouseAnchorY() {
    return this._mouseAnchorY;
  }
  
  private final double _initialX;
  
  public double getInitialX() {
    return this._initialX;
  }
  
  private final double _initialY;
  
  public double getInitialY() {
    return this._initialY;
  }
  
  public DragContext(final double mouseAnchorX, final double mouseAnchorY, final double initialX, final double initialY) {
    super();
    this._mouseAnchorX = mouseAnchorX;
    this._mouseAnchorY = mouseAnchorY;
    this._initialX = initialX;
    this._initialY = initialY;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (Double.doubleToLongBits(_mouseAnchorX) ^ (Double.doubleToLongBits(_mouseAnchorX) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(_mouseAnchorY) ^ (Double.doubleToLongBits(_mouseAnchorY) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(_initialX) ^ (Double.doubleToLongBits(_initialX) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(_initialY) ^ (Double.doubleToLongBits(_initialY) >>> 32));
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
    DragContext other = (DragContext) obj;
    if (Double.doubleToLongBits(other._mouseAnchorX) != Double.doubleToLongBits(_mouseAnchorX))
      return false;
    if (Double.doubleToLongBits(other._mouseAnchorY) != Double.doubleToLongBits(_mouseAnchorY))
      return false;
    if (Double.doubleToLongBits(other._initialX) != Double.doubleToLongBits(_initialX))
      return false;
    if (Double.doubleToLongBits(other._initialY) != Double.doubleToLongBits(_initialY))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
