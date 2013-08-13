package de.fxdiagram.core.geometry;

import javafx.geometry.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class Point2DExtensions {
  public static Point2D linear(final double fromX, final double fromY, final double toX, final double toY, final double lambda) {
    double _linear = Point2DExtensions.linear(fromX, toX, lambda);
    double _linear_1 = Point2DExtensions.linear(fromY, toY, lambda);
    Point2D _point2D = new Point2D(_linear, _linear_1);
    return _point2D;
  }
  
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
}
