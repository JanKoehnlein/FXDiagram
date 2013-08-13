package de.fxdiagram.core.geometry;

import javafx.geometry.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class Point2DExtensions {
  @Pure
  public static Point2D linear(final double fromX, final double fromY, final double toX, final double toY, final double lambda) {
    double _linear = Point2DExtensions.linear(fromX, toX, lambda);
    double _linear_1 = Point2DExtensions.linear(fromY, toY, lambda);
    Point2D _point2D = new Point2D(_linear, _linear_1);
    return _point2D;
  }
  
  @Pure
  public static Point2D linear(final Point2D from, final Point2D to, final double lambda) {
    double _x = from.getX();
    double _x_1 = to.getX();
    double _linear = Point2DExtensions.linear(_x, _x_1, lambda);
    double _y = from.getY();
    double _y_1 = to.getY();
    double _linear_1 = Point2DExtensions.linear(_y, _y_1, lambda);
    Point2D _point2D = new Point2D(_linear, _linear_1);
    return _point2D;
  }
  
  @Pure
  public static Point2D center(final Point2D from, final Point2D to, final double lambda) {
    double _x = from.getX();
    double _x_1 = to.getX();
    double _plus = (_x + _x_1);
    double _multiply = (0.5 * _plus);
    double _y = from.getY();
    double _y_1 = to.getY();
    double _plus_1 = (_y + _y_1);
    double _multiply_1 = (0.5 * _plus_1);
    Point2D _point2D = new Point2D(_multiply, _multiply_1);
    return _point2D;
  }
  
  @Pure
  public static Point2D operator_plus(final Point2D left, final Point2D right) {
    double _x = left.getX();
    double _x_1 = right.getX();
    double _plus = (_x + _x_1);
    double _y = left.getY();
    double _y_1 = right.getY();
    double _plus_1 = (_y + _y_1);
    Point2D _point2D = new Point2D(_plus, _plus_1);
    return _point2D;
  }
  
  @Pure
  public static Point2D operator_minus(final Point2D left, final Point2D right) {
    double _x = left.getX();
    double _x_1 = right.getX();
    double _minus = (_x - _x_1);
    double _y = left.getY();
    double _y_1 = right.getY();
    double _minus_1 = (_y - _y_1);
    Point2D _point2D = new Point2D(_minus, _minus_1);
    return _point2D;
  }
  
  @Pure
  public static double linear(final double x, final double y, final double lambda) {
    double _minus = (y - x);
    double _multiply = (_minus * lambda);
    double _plus = (x + _multiply);
    return _plus;
  }
  
  @Pure
  public static boolean isClockwise(final Point2D one, final Point2D two, final Point2D three) {
    double _x = one.getX();
    double _y = one.getY();
    double _x_1 = two.getX();
    double _y_1 = two.getY();
    double _x_2 = three.getX();
    double _y_2 = three.getY();
    boolean _isClockwise = Point2DExtensions.isClockwise(_x, _y, _x_1, _y_1, _x_2, _y_2);
    return _isClockwise;
  }
  
  @Pure
  public static boolean isClockwise(final double x0, final double y0, final double x1, final double y1, final double x2, final double y2) {
    double _minus = (x1 - x0);
    double _plus = (y1 + y0);
    double _multiply = (_minus * _plus);
    double _minus_1 = (x2 - x1);
    double _plus_1 = (y2 + y1);
    double _multiply_1 = (_minus_1 * _plus_1);
    double _plus_2 = (_multiply + _multiply_1);
    double _minus_2 = (x0 - x2);
    double _plus_3 = (y0 + y2);
    double _multiply_2 = (_minus_2 * _plus_3);
    double _plus_4 = (_plus_2 + _multiply_2);
    boolean _greaterThan = (_plus_4 > 0);
    return _greaterThan;
  }
}
