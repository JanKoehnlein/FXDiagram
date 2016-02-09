package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.extensions.BezierExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class ConnectionExtensions {
  @Data
  public static class PointOnCurve {
    private final Point2D point;
    
    private final double localParameter;
    
    private final double parameter;
    
    private final int segmentIndex;
    
    private final double distance;
    
    public boolean isBetterThan(final ConnectionExtensions.PointOnCurve other) {
      boolean _or = false;
      boolean _equals = Objects.equal(other, null);
      if (_equals) {
        _or = true;
      } else {
        _or = (this.distance < other.distance);
      }
      return _or;
    }
    
    public PointOnCurve(final Point2D point, final double localParameter, final double parameter, final int segmentIndex, final double distance) {
      super();
      this.point = point;
      this.localParameter = localParameter;
      this.parameter = parameter;
      this.segmentIndex = segmentIndex;
      this.distance = distance;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.point== null) ? 0 : this.point.hashCode());
      result = prime * result + (int) (Double.doubleToLongBits(this.localParameter) ^ (Double.doubleToLongBits(this.localParameter) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.parameter) ^ (Double.doubleToLongBits(this.parameter) >>> 32));
      result = prime * result + this.segmentIndex;
      result = prime * result + (int) (Double.doubleToLongBits(this.distance) ^ (Double.doubleToLongBits(this.distance) >>> 32));
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
      ConnectionExtensions.PointOnCurve other = (ConnectionExtensions.PointOnCurve) obj;
      if (this.point == null) {
        if (other.point != null)
          return false;
      } else if (!this.point.equals(other.point))
        return false;
      if (Double.doubleToLongBits(other.localParameter) != Double.doubleToLongBits(this.localParameter))
        return false; 
      if (Double.doubleToLongBits(other.parameter) != Double.doubleToLongBits(this.parameter))
        return false; 
      if (other.segmentIndex != this.segmentIndex)
        return false;
      if (Double.doubleToLongBits(other.distance) != Double.doubleToLongBits(this.distance))
        return false; 
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("point", this.point);
      b.add("localParameter", this.localParameter);
      b.add("parameter", this.parameter);
      b.add("segmentIndex", this.segmentIndex);
      b.add("distance", this.distance);
      return b.toString();
    }
    
    @Pure
    public Point2D getPoint() {
      return this.point;
    }
    
    @Pure
    public double getLocalParameter() {
      return this.localParameter;
    }
    
    @Pure
    public double getParameter() {
      return this.parameter;
    }
    
    @Pure
    public int getSegmentIndex() {
      return this.segmentIndex;
    }
    
    @Pure
    public double getDistance() {
      return this.distance;
    }
  }
  
  public static ConnectionExtensions.PointOnCurve getNearestPointOnConnection(final Point2D pointInLocal, final List<Point2D> controlPoints, final XConnection.Kind kind) {
    ConnectionExtensions.PointOnCurve _switchResult = null;
    if (kind != null) {
      switch (kind) {
        case POLYLINE:
          _switchResult = ConnectionExtensions.getNearestPointOnPolyline(pointInLocal, controlPoints);
          break;
        case QUAD_CURVE:
          _switchResult = ConnectionExtensions.getNearestPointOnQuadraticSpline(pointInLocal, controlPoints);
          break;
        case CUBIC_CURVE:
          _switchResult = ConnectionExtensions.getNearestPointOnCubicSpline(pointInLocal, controlPoints);
          break;
        default:
          break;
      }
    }
    return _switchResult;
  }
  
  public static ConnectionExtensions.PointOnCurve getNearestPointOnCubicSpline(final Point2D pointInLocal, final List<Point2D> controlPoints) {
    int _size = controlPoints.size();
    int _minus = (_size - 1);
    int _modulo = (_minus % 3);
    boolean _notEquals = (_modulo != 0);
    if (_notEquals) {
      int _size_1 = controlPoints.size();
      String _plus = ("Invalid number of points for a cubic spline curve: " + Integer.valueOf(_size_1));
      throw new IllegalArgumentException(_plus);
    }
    int _size_2 = controlPoints.size();
    int _minus_1 = (_size_2 - 1);
    final int numSegments = (_minus_1 / 3);
    ConnectionExtensions.PointOnCurve bestMatch = null;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, numSegments, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Point2D start = controlPoints.get((3 * (i).intValue()));
        final Point2D control0 = controlPoints.get(((3 * (i).intValue()) + 1));
        final Point2D control1 = controlPoints.get(((3 * (i).intValue()) + 2));
        final Point2D end = controlPoints.get(((3 * (i).intValue()) + 3));
        double _x = start.getX();
        double _y = start.getY();
        double _x_1 = control0.getX();
        double _y_1 = control0.getY();
        double _x_2 = control1.getX();
        double _y_2 = control1.getY();
        double _x_3 = end.getX();
        double _y_3 = end.getY();
        final CubicCurve curve = new CubicCurve(_x, _y, _x_1, _y_1, _x_2, _y_2, _x_3, _y_3);
        final Function1<Double, Point2D> _function = (Double it) -> {
          return BezierExtensions.at(curve, (it).doubleValue());
        };
        final ConnectionExtensions.PointOnCurve match = ConnectionExtensions.findNearestPoint(_function, pointInLocal, (i).intValue(), numSegments);
        boolean _isBetterThan = match.isBetterThan(bestMatch);
        if (_isBetterThan) {
          bestMatch = match;
        }
      }
    }
    return bestMatch;
  }
  
  public static ConnectionExtensions.PointOnCurve getNearestPointOnQuadraticSpline(final Point2D pointInLocal, final List<Point2D> controlPoints) {
    int _size = controlPoints.size();
    int _minus = (_size - 1);
    int _modulo = (_minus % 2);
    boolean _notEquals = (_modulo != 0);
    if (_notEquals) {
      int _size_1 = controlPoints.size();
      String _plus = ("Invalid number of points for a quadratic spline curve: " + Integer.valueOf(_size_1));
      throw new IllegalArgumentException(_plus);
    }
    int _size_2 = controlPoints.size();
    int _minus_1 = (_size_2 - 1);
    final int numSegments = (_minus_1 / 2);
    ConnectionExtensions.PointOnCurve bestMatch = null;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, numSegments, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Point2D start = controlPoints.get((2 * (i).intValue()));
        final Point2D control = controlPoints.get(((2 * (i).intValue()) + 1));
        final Point2D end = controlPoints.get(((2 * (i).intValue()) + 2));
        double _x = start.getX();
        double _y = start.getY();
        double _x_1 = control.getX();
        double _y_1 = control.getY();
        double _x_2 = end.getX();
        double _y_2 = end.getY();
        final QuadCurve curve = new QuadCurve(_x, _y, _x_1, _y_1, _x_2, _y_2);
        final Function1<Double, Point2D> _function = (Double it) -> {
          return BezierExtensions.at(curve, (it).doubleValue());
        };
        final ConnectionExtensions.PointOnCurve match = ConnectionExtensions.findNearestPoint(_function, pointInLocal, (i).intValue(), numSegments);
        boolean _isBetterThan = match.isBetterThan(bestMatch);
        if (_isBetterThan) {
          bestMatch = match;
        }
      }
    }
    return bestMatch;
  }
  
  protected static ConnectionExtensions.PointOnCurve findNearestPoint(final Function1<? super Double, ? extends Point2D> curve, final Point2D pointInLocal, final int segmentIndex, final int numSegments) {
    double left = 0.0;
    double right = 1.0;
    Point2D _apply = curve.apply(Double.valueOf(left));
    Point2D _minus = Point2DExtensions.operator_minus(_apply, pointInLocal);
    double distLeft = Point2DExtensions.norm(_minus);
    Point2D _apply_1 = curve.apply(Double.valueOf(right));
    Point2D _minus_1 = Point2DExtensions.operator_minus(_apply_1, pointInLocal);
    double distRight = Point2DExtensions.norm(_minus_1);
    double mid = 0;
    Point2D midPoint = null;
    double distMid = 0;
    while (((right - left) > Math.sqrt(NumberExpressionExtensions.EPSILON))) {
      {
        double _linear = Point2DExtensions.linear(left, right, 0.5);
        mid = _linear;
        Point2D _apply_2 = curve.apply(Double.valueOf(mid));
        midPoint = _apply_2;
        Point2D _minus_2 = Point2DExtensions.operator_minus(midPoint, pointInLocal);
        double _norm = Point2DExtensions.norm(_minus_2);
        distMid = _norm;
        if ((distMid < NumberExpressionExtensions.EPSILON)) {
          return new ConnectionExtensions.PointOnCurve(midPoint, mid, ((mid + segmentIndex) / numSegments), segmentIndex, distMid);
        }
        Point2D _apply_3 = curve.apply(Double.valueOf((mid - NumberExpressionExtensions.EPSILON)));
        Point2D _minus_3 = Point2DExtensions.operator_minus(_apply_3, pointInLocal);
        double _norm_1 = Point2DExtensions.norm(_minus_3);
        final double dLeftMid = (_norm_1 - distMid);
        Point2D _apply_4 = curve.apply(Double.valueOf((mid + NumberExpressionExtensions.EPSILON)));
        Point2D _minus_4 = Point2DExtensions.operator_minus(_apply_4, pointInLocal);
        double _norm_2 = Point2DExtensions.norm(_minus_4);
        final double dRightMid = (_norm_2 - distMid);
        if ((dLeftMid < 0)) {
          if ((dRightMid < 0)) {
            return new ConnectionExtensions.PointOnCurve(midPoint, mid, ((mid + segmentIndex) / numSegments), segmentIndex, distMid);
          } else {
            right = mid;
            distRight = distMid;
          }
        } else {
          if ((dRightMid < 0)) {
            left = mid;
            distLeft = distMid;
          } else {
            return new ConnectionExtensions.PointOnCurve(midPoint, mid, ((mid + segmentIndex) / numSegments), segmentIndex, distMid);
          }
        }
      }
    }
    return new ConnectionExtensions.PointOnCurve(midPoint, mid, ((mid + segmentIndex) / numSegments), segmentIndex, distMid);
  }
  
  public static ConnectionExtensions.PointOnCurve getNearestPointOnPolyline(final Point2D pointInLocal, final List<Point2D> controlPoints) {
    int _size = controlPoints.size();
    final double numSegments = (_size - 1.0);
    ConnectionExtensions.PointOnCurve bestMatch = null;
    int _size_1 = controlPoints.size();
    int _minus = (_size_1 - 2);
    IntegerRange _upTo = new IntegerRange(0, _minus);
    for (final Integer i : _upTo) {
      {
        final Point2D segmentStart = controlPoints.get((i).intValue());
        final Point2D segmentEnd = controlPoints.get(((i).intValue() + 1));
        double _distance = pointInLocal.distance(segmentStart);
        boolean _lessThan = (_distance < NumberExpressionExtensions.EPSILON);
        if (_lessThan) {
          double _distance_1 = pointInLocal.distance(segmentStart);
          return new ConnectionExtensions.PointOnCurve(segmentStart, 0, ((i).intValue() / numSegments), (i).intValue(), _distance_1);
        }
        double _distance_2 = pointInLocal.distance(segmentEnd);
        boolean _lessThan_1 = (_distance_2 < NumberExpressionExtensions.EPSILON);
        if (_lessThan_1) {
          double _distance_3 = pointInLocal.distance(segmentEnd);
          return new ConnectionExtensions.PointOnCurve(segmentEnd, 1, (((i).intValue() + 1) / numSegments), (i).intValue(), _distance_3);
        }
        final Point2D delta0 = Point2DExtensions.operator_minus(pointInLocal, segmentStart);
        final Point2D delta1 = Point2DExtensions.operator_minus(segmentEnd, segmentStart);
        double _x = delta1.getX();
        double _x_1 = delta1.getX();
        double _multiply = (_x * _x_1);
        double _y = delta1.getY();
        double _y_1 = delta1.getY();
        double _multiply_1 = (_y * _y_1);
        final double scale = (_multiply + _multiply_1);
        if ((scale > NumberExpressionExtensions.EPSILON)) {
          double _x_2 = delta0.getX();
          double _x_3 = delta1.getX();
          double _multiply_2 = (_x_2 * _x_3);
          double _y_2 = delta0.getY();
          double _y_3 = delta1.getY();
          double _multiply_3 = (_y_2 * _y_3);
          double _plus = (_multiply_2 + _multiply_3);
          final double projectionScale = (_plus / scale);
          Point2D _multiply_4 = Point2DExtensions.operator_multiply(projectionScale, delta1);
          Point2D testPoint = Point2DExtensions.operator_plus(segmentStart, _multiply_4);
          final Point2D delta = Point2DExtensions.operator_minus(testPoint, pointInLocal);
          if (((projectionScale >= 0) && (projectionScale <= 1))) {
            double _norm = Point2DExtensions.norm(delta);
            final ConnectionExtensions.PointOnCurve match = new ConnectionExtensions.PointOnCurve(testPoint, projectionScale, (((i).intValue() + projectionScale) / numSegments), (i).intValue(), _norm);
            boolean _isBetterThan = match.isBetterThan(bestMatch);
            if (_isBetterThan) {
              bestMatch = match;
            }
          }
        }
      }
    }
    return bestMatch;
  }
  
  public static Point2D toPoint2D(final XControlPoint controlPoint) {
    double _layoutX = controlPoint.getLayoutX();
    double _layoutY = controlPoint.getLayoutY();
    return new Point2D(_layoutX, _layoutY);
  }
}
