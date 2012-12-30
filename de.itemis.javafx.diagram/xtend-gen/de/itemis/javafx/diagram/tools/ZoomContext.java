package de.itemis.javafx.diagram.tools;

import javafx.geometry.Point2D;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ZoomContext {
  private final double _initialScale;
  
  public double getInitialScale() {
    return this._initialScale;
  }
  
  private final Point2D _initialDiagramPos;
  
  public Point2D getInitialDiagramPos() {
    return this._initialDiagramPos;
  }
  
  public ZoomContext(final double initialScale, final Point2D initialDiagramPos) {
    super();
    this._initialScale = initialScale;
    this._initialDiagramPos = initialDiagramPos;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (Double.doubleToLongBits(_initialScale) ^ (Double.doubleToLongBits(_initialScale) >>> 32));
    result = prime * result + ((_initialDiagramPos== null) ? 0 : _initialDiagramPos.hashCode());
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
    ZoomContext other = (ZoomContext) obj;
    if (Double.doubleToLongBits(other._initialScale) != Double.doubleToLongBits(_initialScale))
      return false;
    if (_initialDiagramPos == null) {
      if (other._initialDiagramPos != null)
        return false;
    } else if (!_initialDiagramPos.equals(other._initialDiagramPos))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
