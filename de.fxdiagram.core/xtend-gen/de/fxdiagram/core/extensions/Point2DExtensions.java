package de.fxdiagram.core.extensions;

import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import javafx.geometry.Point2D;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class Point2DExtensions {
  @Pure
  public static Point2D linear(final double fromX, final double fromY, final double toX, final double toY, final double lambda) {
    double _linear = Point2DExtensions.linear(fromX, toX, lambda);
    double _linear_1 = Point2DExtensions.linear(fromY, toY, lambda);
    return new Point2D(_linear, _linear_1);
  }
  
  @Pure
  public static Point2D linear(final Point2D from, final Point2D to, final double lambda) {
    double _x = from.getX();
    double _x_1 = to.getX();
    double _linear = Point2DExtensions.linear(_x, _x_1, lambda);
    double _y = from.getY();
    double _y_1 = to.getY();
    double _linear_1 = Point2DExtensions.linear(_y, _y_1, lambda);
    return new Point2D(_linear, _linear_1);
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
    return new Point2D(_multiply, _multiply_1);
  }
  
  @Pure
  public static Point2D operator_plus(final Point2D left, final Point2D right) {
    double _x = left.getX();
    double _x_1 = right.getX();
    double _plus = (_x + _x_1);
    double _y = left.getY();
    double _y_1 = right.getY();
    double _plus_1 = (_y + _y_1);
    return new Point2D(_plus, _plus_1);
  }
  
  @Pure
  public static Point2D operator_minus(final Point2D left, final Point2D right) {
    double _x = left.getX();
    double _x_1 = right.getX();
    double _minus = (_x - _x_1);
    double _y = left.getY();
    double _y_1 = right.getY();
    double _minus_1 = (_y - _y_1);
    return new Point2D(_minus, _minus_1);
  }
  
  @Pure
  public static Point2D operator_multiply(final Point2D left, final double right) {
    double _x = left.getX();
    double _multiply = (_x * right);
    double _y = left.getY();
    double _multiply_1 = (_y * right);
    return new Point2D(_multiply, _multiply_1);
  }
  
  @Pure
  public static Point2D operator_multiply(final double left, final Point2D right) {
    double _x = right.getX();
    double _multiply = (left * _x);
    double _y = right.getY();
    double _multiply_1 = (left * _y);
    return new Point2D(_multiply, _multiply_1);
  }
  
  @Pure
  public static Point2D operator_divide(final Point2D left, final double right) {
    double _x = left.getX();
    double _divide = (_x / right);
    double _y = left.getY();
    double _divide_1 = (_y / right);
    return new Point2D(_divide, _divide_1);
  }
  
  @Pure
  public static Point2D operator_divide(final double left, final Point2D right) {
    double _x = right.getX();
    double _divide = (left / _x);
    double _y = right.getY();
    double _divide_1 = (left / _y);
    return new Point2D(_divide, _divide_1);
  }
  
  @Pure
  public static double norm(final Point2D it) {
    double _x = it.getX();
    double _x_1 = it.getX();
    double _multiply = (_x * _x_1);
    double _y = it.getY();
    double _y_1 = it.getY();
    double _multiply_1 = (_y * _y_1);
    double _plus = (_multiply + _multiply_1);
    return Math.sqrt(_plus);
  }
  
  @Pure
  public static double norm(final double x, final double y) {
    return Math.sqrt(((x * x) + (y * y)));
  }
  
  @Pure
  public static double linear(final double x, final double y, final double lambda) {
    return (x + ((y - x) * lambda));
  }
  
  @Pure
  public static boolean isClockwise(final Point2D one, final Point2D two, final Point2D three) {
    double _x = one.getX();
    double _y = one.getY();
    double _x_1 = two.getX();
    double _y_1 = two.getY();
    double _x_2 = three.getX();
    double _y_2 = three.getY();
    return Point2DExtensions.isClockwise(_x, _y, _x_1, _y_1, _x_2, _y_2);
  }
  
  @Pure
  public static boolean isClockwise(final double x0, final double y0, final double x1, final double y1, final double x2, final double y2) {
    return (((((x1 - x0) * (y1 + y0)) + ((x2 - x1) * (y2 + y1))) + ((x0 - x2) * (y0 + y2))) > 0);
  }
  
  public final static int EPSILON_DEGREES = 5;
  
  private final static double COS_EPSILON = Math.cos(((Point2DExtensions.EPSILON_DEGREES * Math.PI) / 180));
  
  @Pure
  public static boolean areOnSameLine(final Point2D x0, final Point2D x1, final Point2D x2) {
    double _x = x0.getX();
    double _y = x0.getY();
    double _x_1 = x1.getX();
    double _y_1 = x1.getY();
    double _x_2 = x2.getX();
    double _y_2 = x2.getY();
    return Point2DExtensions.areOnSameLine(_x, _y, _x_1, _y_1, _x_2, _y_2);
  }
  
  @Pure
  public static boolean areOnSameLine(final double x0x, final double x0y, final double x1x, final double x1y, final double x2x, final double x2y) {
    final double d0x = (x0x - x1x);
    final double d0y = (x0y - x1y);
    final double norm0 = Point2DExtensions.norm(d0x, d0y);
    if ((norm0 < NumberExpressionExtensions.EPSILON)) {
      return true;
    }
    final double d1x = (x1x - x2x);
    final double d1y = (x1y - x2y);
    final double norm1 = Point2DExtensions.norm(d1x, d1y);
    if ((norm1 < NumberExpressionExtensions.EPSILON)) {
      return true;
    }
    final double cosTheta = ((((d0x * d1x) + (d0y * d1y)) / norm0) / norm1);
    double _abs = Math.abs(cosTheta);
    return (_abs > Point2DExtensions.COS_EPSILON);
  }
}
