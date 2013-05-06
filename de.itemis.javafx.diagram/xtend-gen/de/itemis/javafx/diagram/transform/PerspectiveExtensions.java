package de.itemis.javafx.diagram.transform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import de.itemis.javafx.diagram.transform.TransformExtensions;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class PerspectiveExtensions {
  public static PerspectiveTransform mapPerspective(final Bounds source, final Transform transform, final double screenDistance) {
    double _minX = source.getMinX();
    double _minY = source.getMinY();
    Point3D _point3D = new Point3D(_minX, _minY, 0);
    double _maxX = source.getMaxX();
    double _minY_1 = source.getMinY();
    Point3D _point3D_1 = new Point3D(_maxX, _minY_1, 0);
    double _maxX_1 = source.getMaxX();
    double _maxY = source.getMaxY();
    Point3D _point3D_2 = new Point3D(_maxX_1, _maxY, 0);
    double _minX_1 = source.getMinX();
    double _maxY_1 = source.getMaxY();
    Point3D _point3D_3 = new Point3D(_minX_1, _maxY_1, 0);
    List<Point3D> _xlistliteral = null;
    Builder<Point3D> _builder = ImmutableList.builder();
    _builder.add(_point3D);
    _builder.add(_point3D_1);
    _builder.add(_point3D_2);
    _builder.add(_point3D_3);
    _xlistliteral = _builder.build();
    final List<Point3D> points = _xlistliteral;
    final Function1<Point3D,Point2D> _function = new Function1<Point3D,Point2D>() {
        public Point2D apply(final Point3D it) {
          Point3D _multiply = TransformExtensions.operator_multiply(transform, it);
          Point2D _mapPerspective = PerspectiveExtensions.mapPerspective(_multiply, screenDistance);
          return _mapPerspective;
        }
      };
    final List<Point2D> mappedPoints = ListExtensions.<Point3D, Point2D>map(points, _function);
    boolean _isClockwise = PerspectiveExtensions.isClockwise(mappedPoints);
    if (_isClockwise) {
      Point2D _get = mappedPoints.get(0);
      double _x = _get.getX();
      Point2D _get_1 = mappedPoints.get(0);
      double _y = _get_1.getY();
      Point2D _get_2 = mappedPoints.get(1);
      double _x_1 = _get_2.getX();
      Point2D _get_3 = mappedPoints.get(1);
      double _y_1 = _get_3.getY();
      Point2D _get_4 = mappedPoints.get(2);
      double _x_2 = _get_4.getX();
      Point2D _get_5 = mappedPoints.get(2);
      double _y_2 = _get_5.getY();
      Point2D _get_6 = mappedPoints.get(3);
      double _x_3 = _get_6.getX();
      Point2D _get_7 = mappedPoints.get(3);
      double _y_3 = _get_7.getY();
      PerspectiveTransform _perspectiveTransform = new PerspectiveTransform(_x, _y, _x_1, _y_1, _x_2, _y_2, _x_3, _y_3);
      return _perspectiveTransform;
    } else {
      return null;
    }
  }
  
  public static boolean isClockwise(final List<Point2D> mappedPoints) {
    int _length = ((Object[])Conversions.unwrapArray(mappedPoints, Object.class)).length;
    boolean _lessThan = (_length < 3);
    if (_lessThan) {
      return false;
    }
    Point2D _get = mappedPoints.get(1);
    double _x = _get.getX();
    Point2D _get_1 = mappedPoints.get(0);
    double _x_1 = _get_1.getX();
    double _minus = (_x - _x_1);
    Point2D _get_2 = mappedPoints.get(1);
    double _y = _get_2.getY();
    Point2D _get_3 = mappedPoints.get(0);
    double _y_1 = _get_3.getY();
    double _plus = (_y + _y_1);
    double _multiply = (_minus * _plus);
    Point2D _get_4 = mappedPoints.get(2);
    double _x_2 = _get_4.getX();
    Point2D _get_5 = mappedPoints.get(1);
    double _x_3 = _get_5.getX();
    double _minus_1 = (_x_2 - _x_3);
    Point2D _get_6 = mappedPoints.get(2);
    double _y_2 = _get_6.getY();
    Point2D _get_7 = mappedPoints.get(1);
    double _y_3 = _get_7.getY();
    double _plus_1 = (_y_2 + _y_3);
    double _multiply_1 = (_minus_1 * _plus_1);
    double _plus_2 = (_multiply + _multiply_1);
    return (_plus_2 <= 0);
  }
  
  public static Point2D mapPerspective(final Point3D point, final double screenDistance) {
    double _x = point.getX();
    double _multiply = (screenDistance * _x);
    double _z = point.getZ();
    double _divide = (_multiply / _z);
    double _y = point.getY();
    double _multiply_1 = (screenDistance * _y);
    double _z_1 = point.getZ();
    double _divide_1 = (_multiply_1 / _z_1);
    Point2D _point2D = new Point2D(_divide, _divide_1);
    return _point2D;
  }
}
