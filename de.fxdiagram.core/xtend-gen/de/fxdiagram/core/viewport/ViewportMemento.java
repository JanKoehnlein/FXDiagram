package de.fxdiagram.core.viewport;

import de.fxdiagram.core.extensions.Point2DExtensions;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtend2.lib.StringConcatenation;

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
    double _xblockexpression = (double) 0;
    {
      double _translateX = this.getTranslateX();
      double _translateX_1 = other.getTranslateX();
      double _minus = (_translateX - _translateX_1);
      double _translateY = this.getTranslateY();
      double _translateY_1 = other.getTranslateY();
      double _minus_1 = (_translateY - _translateY_1);
      final double delta = Point2DExtensions.norm(_minus, _minus_1);
      double _scale = this.getScale();
      double _scale_1 = other.getScale();
      double _max = Math.max(_scale, _scale_1);
      double _scale_2 = this.getScale();
      double _scale_3 = other.getScale();
      double _min = Math.min(_scale_2, _scale_3);
      double _divide = (_max / _min);
      double _log = Math.log(_divide);
      final double deltaScale = (500 * _log);
      double _rotate = this.getRotate();
      double _rotate_1 = other.getRotate();
      double _minus_2 = (_rotate - _rotate_1);
      double _abs = Math.abs(_minus_2);
      final double deltaAngle = (7 * _abs);
      _xblockexpression = ((delta + deltaScale) + deltaAngle);
    }
    return _xblockexpression;
  }
  
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("ViewportMemento [translateX=");
    double _translateX = this.getTranslateX();
    _builder.append(_translateX, "");
    _builder.append(", translateY=");
    double _translateY = this.getTranslateY();
    _builder.append(_translateY, "");
    _builder.append(", scale=");
    double _scale = this.getScale();
    _builder.append(_scale, "");
    _builder.append(", rotate=");
    double _rotate = this.getRotate();
    _builder.append(_rotate, "");
    _builder.append("]");
    return _builder.toString();
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
    result = prime * result + (int) (Double.doubleToLongBits(this._translateX) ^ (Double.doubleToLongBits(this._translateX) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(this._translateY) ^ (Double.doubleToLongBits(this._translateY) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(this._scale) ^ (Double.doubleToLongBits(this._scale) >>> 32));
    result = prime * result + (int) (Double.doubleToLongBits(this._rotate) ^ (Double.doubleToLongBits(this._rotate) >>> 32));
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
    if (Double.doubleToLongBits(other._translateX) != Double.doubleToLongBits(this._translateX))
      return false;
    if (Double.doubleToLongBits(other._translateY) != Double.doubleToLongBits(this._translateY))
      return false;
    if (Double.doubleToLongBits(other._scale) != Double.doubleToLongBits(this._scale))
      return false;
    if (Double.doubleToLongBits(other._rotate) != Double.doubleToLongBits(this._rotate))
      return false;
    return true;
  }
}
