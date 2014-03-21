package de.fxdiagram.core.viewport;

import de.fxdiagram.core.extensions.Point2DExtensions;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@Data
@SuppressWarnings("all")
public class ViewportMemento {
  private final double _translateX;
  
  public double getTranslateX() {
    return this._translateX;
  }
  
  private final double _translateY;
  
  public double getTranslateY() {
    return this._translateY;
  }
  
  private final double _scale;
  
  public double getScale() {
    return this._scale;
  }
  
  private final double _rotate;
  
  public double getRotate() {
    return this._rotate;
  }
  
  public double dist(final ViewportMemento other) {
    double _translateX = this.getTranslateX();
    double _translateX_1 = other.getTranslateX();
    double _minus = (_translateX - _translateX_1);
    double _translateY = this.getTranslateY();
    double _translateY_1 = other.getTranslateY();
    double _minus_1 = (_translateY - _translateY_1);
    double _norm = Point2DExtensions.norm(_minus, _minus_1);
    double _scale = this.getScale();
    double _scale_1 = other.getScale();
    double _minus_2 = (_scale - _scale_1);
    double _scale_2 = this.getScale();
    double _divide = (_minus_2 / _scale_2);
    double _abs = Math.abs(_divide);
    double _multiply = (10 * _abs);
    double _plus = (_norm + _multiply);
    double _rotate = this.getRotate();
    double _rotate_1 = other.getRotate();
    double _minus_3 = (_rotate - _rotate_1);
    double _abs_1 = Math.abs(_minus_3);
    double _multiply_1 = (10 * _abs_1);
    return (_plus + _multiply_1);
  }
  
  public ViewportMemento(final double translateX, final double translateY, final double scale, final double rotate) {
    super();
    this._translateX = translateX;
    this._translateY = translateY;
    this._scale = scale;
    this._rotate = rotate;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (Double.doubleToLongBits(_translateX) ^ (Double.doubleToLongBits(_translateX) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(_translateY) ^ (Double.doubleToLongBits(_translateY) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(_scale) ^ (Double.doubleToLongBits(_scale) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(_rotate) ^ (Double.doubleToLongBits(_rotate) >>> 32));
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
    ViewportMemento other = (ViewportMemento) obj;
    if (Double.doubleToLongBits(other._translateX) != Double.doubleToLongBits(_translateX))
      return false;
    if (Double.doubleToLongBits(other._translateY) != Double.doubleToLongBits(_translateY))
      return false;
    if (Double.doubleToLongBits(other._scale) != Double.doubleToLongBits(_scale))
      return false;
    if (Double.doubleToLongBits(other._rotate) != Double.doubleToLongBits(_rotate))
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
