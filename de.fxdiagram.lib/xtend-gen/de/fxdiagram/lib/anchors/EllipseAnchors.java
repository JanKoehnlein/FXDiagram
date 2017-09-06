package de.fxdiagram.lib.anchors;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.AbstractAnchors;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Side;

@SuppressWarnings("all")
public class EllipseAnchors extends AbstractAnchors {
  public EllipseAnchors(final XNode host) {
    super(host);
  }
  
  @Override
  public Point2D getAnchor(final double x, final double y) {
    final Bounds bounds = this.getBoundsInRoot();
    if ((bounds == null)) {
      return null;
    }
    final Point2D center = BoundsExtensions.center(bounds);
    double _y = center.getY();
    double _minus = (y - _y);
    double _x = center.getX();
    double _minus_1 = (x - _x);
    final double angle = Math.atan2(_minus, _minus_1);
    double _width = bounds.getWidth();
    double _multiply = (0.5 * _width);
    double _cos = Math.cos(angle);
    double _multiply_1 = (_multiply * _cos);
    double _height = bounds.getHeight();
    double _multiply_2 = (0.5 * _height);
    double _sin = Math.sin(angle);
    double _multiply_3 = (_multiply_2 * _sin);
    Point2D _point2D = new Point2D(_multiply_1, _multiply_3);
    return Point2DExtensions.operator_plus(center, _point2D);
  }
  
  @Override
  public Point2D getManhattanAnchor(final double x, final double y, final Side side) {
    final Bounds bounds = this.getBoundsInRoot();
    final Point2D center = BoundsExtensions.center(bounds);
    if (side != null) {
      switch (side) {
        case TOP:
          double _y = center.getY();
          double _x = center.getX();
          double _minus = (_x - x);
          double _y_1 = this.getY(_minus, bounds);
          double _minus_1 = (_y - _y_1);
          return new Point2D(x, _minus_1);
        case BOTTOM:
          double _y_2 = center.getY();
          double _x_1 = center.getX();
          double _minus_2 = (_x_1 - x);
          double _y_3 = this.getY(_minus_2, bounds);
          double _plus = (_y_2 + _y_3);
          return new Point2D(x, _plus);
        case LEFT:
          double _x_2 = center.getX();
          double _y_4 = center.getY();
          double _minus_3 = (_y_4 - y);
          double _x_3 = this.getX(_minus_3, bounds);
          double _minus_4 = (_x_2 - _x_3);
          return new Point2D(_minus_4, y);
        case RIGHT:
          double _x_4 = center.getX();
          double _y_5 = center.getY();
          double _minus_5 = (_y_5 - y);
          double _x_5 = this.getX(_minus_5, bounds);
          double _plus_1 = (_x_4 + _x_5);
          return new Point2D(_plus_1, y);
        default:
          break;
      }
    }
    return null;
  }
  
  protected double getX(final double y, final Bounds radius) {
    double _width = radius.getWidth();
    double _multiply = (0.5 * _width);
    double _height = radius.getHeight();
    double _height_1 = radius.getHeight();
    double _multiply_1 = (_height * _height_1);
    double _divide = ((4 * (y * y)) / _multiply_1);
    double _minus = (1 - _divide);
    double _sqrt = Math.sqrt(_minus);
    return (_multiply * _sqrt);
  }
  
  protected double getY(final double x, final Bounds radius) {
    double _height = radius.getHeight();
    double _multiply = (0.5 * _height);
    double _width = radius.getWidth();
    double _width_1 = radius.getWidth();
    double _multiply_1 = (_width * _width_1);
    double _divide = ((4 * (x * x)) / _multiply_1);
    double _minus = (1 - _divide);
    double _sqrt = Math.sqrt(_minus);
    return (_multiply * _sqrt);
  }
}
