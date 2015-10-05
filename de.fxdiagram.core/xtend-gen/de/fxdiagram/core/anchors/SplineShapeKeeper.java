package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class SplineShapeKeeper {
  private Point2D lastSourceCenter;
  
  private Point2D lastTargetCenter;
  
  private final XConnection connection;
  
  public Point2D reset() {
    Point2D _xblockexpression = null;
    {
      this.lastSourceCenter = null;
      _xblockexpression = this.lastTargetCenter = null;
    }
    return _xblockexpression;
  }
  
  protected ObservableList<XControlPoint> getControlPoints() {
    return this.connection.getControlPoints();
  }
  
  protected Object adjustControlPointsToNodeMove() {
    Point2D _xifexpression = null;
    boolean _and = false;
    boolean _and_1 = false;
    boolean _and_2 = false;
    boolean _and_3 = false;
    boolean _and_4 = false;
    boolean _isActive = this.connection.getIsActive();
    if (!_isActive) {
      _and_4 = false;
    } else {
      XConnection.Kind _kind = this.connection.getKind();
      boolean _notEquals = (!Objects.equal(_kind, XConnection.Kind.POLYLINE));
      _and_4 = _notEquals;
    }
    if (!_and_4) {
      _and_3 = false;
    } else {
      ObservableList<XControlPoint> _controlPoints = this.getControlPoints();
      int _size = _controlPoints.size();
      boolean _greaterThan = (_size > 2);
      _and_3 = _greaterThan;
    }
    if (!_and_3) {
      _and_2 = false;
    } else {
      boolean _notEquals_1 = (!Objects.equal(this.lastSourceCenter, null));
      _and_2 = _notEquals_1;
    }
    if (!_and_2) {
      _and_1 = false;
    } else {
      boolean _notEquals_2 = (!Objects.equal(this.lastTargetCenter, null));
      _and_1 = _notEquals_2;
    }
    if (!_and_1) {
      _and = false;
    } else {
      ObservableList<XControlPoint> _controlPoints_1 = this.getControlPoints();
      final Function1<XControlPoint, Boolean> _function = (XControlPoint it) -> {
        boolean _or = false;
        boolean _or_1 = false;
        boolean _selected = it.getSelected();
        if (_selected) {
          _or_1 = true;
        } else {
          DoubleProperty _layoutXProperty = it.layoutXProperty();
          boolean _isBound = _layoutXProperty.isBound();
          _or_1 = _isBound;
        }
        if (_or_1) {
          _or = true;
        } else {
          DoubleProperty _layoutYProperty = it.layoutYProperty();
          boolean _isBound_1 = _layoutYProperty.isBound();
          _or = _isBound_1;
        }
        return Boolean.valueOf(_or);
      };
      boolean _exists = IterableExtensions.<XControlPoint>exists(_controlPoints_1, _function);
      boolean _not = (!_exists);
      _and = _not;
    }
    if (_and) {
      Point2D _xblockexpression = null;
      {
        XNode _source = this.connection.getSource();
        final Point2D newSourceCenter = this.midPoint(_source);
        XNode _target = this.connection.getTarget();
        final Point2D newTargetCenter = this.midPoint(_target);
        Point2D oldFixedPoint = this.lastTargetCenter;
        Point2D newFixedPoint = newTargetCenter;
        ObservableList<XControlPoint> _controlPoints_2 = this.getControlPoints();
        int _size_1 = _controlPoints_2.size();
        int _minus = (_size_1 - 1);
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
        for (final Integer i : _doubleDotLessThan) {
          ObservableList<XControlPoint> _controlPoints_3 = this.getControlPoints();
          XControlPoint _get = _controlPoints_3.get((i).intValue());
          this.adjust(_get, oldFixedPoint, this.lastSourceCenter, newFixedPoint, newSourceCenter);
        }
        this.lastSourceCenter = newSourceCenter;
        _xblockexpression = this.lastTargetCenter = newTargetCenter;
      }
      _xifexpression = _xblockexpression;
    } else {
      Point2D _xblockexpression_1 = null;
      {
        XNode _source = this.connection.getSource();
        Point2D _midPoint = this.midPoint(_source);
        this.lastSourceCenter = _midPoint;
        XNode _target = this.connection.getTarget();
        Point2D _midPoint_1 = this.midPoint(_target);
        _xblockexpression_1 = this.lastTargetCenter = _midPoint_1;
      }
      _xifexpression = _xblockexpression_1;
    }
    return _xifexpression;
  }
  
  protected void adjust(final XControlPoint cp, final Point2D oldFixed, final Point2D oldCenter, final Point2D newFixed, final Point2D newCenter) {
    final Point2D oldDelta = Point2DExtensions.operator_minus(oldCenter, oldFixed);
    double _layoutX = cp.getLayoutX();
    double _layoutY = cp.getLayoutY();
    final Point2D cpPoint = new Point2D(_layoutX, _layoutY);
    final Point2D oldCpDelta = Point2DExtensions.operator_minus(cpPoint, oldFixed);
    double _x = oldDelta.getX();
    double _x_1 = oldDelta.getX();
    double _multiply = (_x * _x_1);
    double _y = oldDelta.getY();
    double _y_1 = oldDelta.getY();
    double _multiply_1 = (_y * _y_1);
    final double oldDeltaNorm2 = (_multiply + _multiply_1);
    if ((oldDeltaNorm2 < NumberExpressionExtensions.EPSILON)) {
      return;
    }
    double _x_2 = oldDelta.getX();
    double _x_3 = oldCpDelta.getX();
    double _multiply_2 = (_x_2 * _x_3);
    double _y_2 = oldDelta.getY();
    double _y_3 = oldCpDelta.getY();
    double _multiply_3 = (_y_2 * _y_3);
    double _plus = (_multiply_2 + _multiply_3);
    final double lambda = (_plus / oldDeltaNorm2);
    double _y_4 = oldDelta.getY();
    double _x_4 = oldCpDelta.getX();
    double _multiply_4 = (_y_4 * _x_4);
    double _x_5 = oldDelta.getX();
    double _y_5 = oldCpDelta.getY();
    double _multiply_5 = (_x_5 * _y_5);
    double _minus = (_multiply_4 - _multiply_5);
    final double theta = (_minus / oldDeltaNorm2);
    final Point2D newDelta = Point2DExtensions.operator_minus(newCenter, newFixed);
    Point2D _multiply_6 = Point2DExtensions.operator_multiply(lambda, newDelta);
    final Point2D newPointOnLine = Point2DExtensions.operator_plus(newFixed, _multiply_6);
    double _norm = Point2DExtensions.norm(newDelta);
    boolean _lessThan = (_norm < NumberExpressionExtensions.EPSILON);
    if (_lessThan) {
      double _x_6 = newPointOnLine.getX();
      cp.setLayoutX(_x_6);
      double _y_6 = newPointOnLine.getY();
      cp.setLayoutY(_y_6);
    } else {
      double _y_7 = newDelta.getY();
      double _x_7 = newDelta.getX();
      double _minus_1 = (-_x_7);
      Point2D _point2D = new Point2D(_y_7, _minus_1);
      final Point2D orthoNewDelta = Point2DExtensions.operator_multiply(theta, _point2D);
      double _x_8 = newPointOnLine.getX();
      double _x_9 = orthoNewDelta.getX();
      double _plus_1 = (_x_8 + _x_9);
      cp.setLayoutX(_plus_1);
      double _y_8 = newPointOnLine.getY();
      double _y_9 = orthoNewDelta.getY();
      double _plus_2 = (_y_8 + _y_9);
      cp.setLayoutY(_plus_2);
    }
  }
  
  protected Point2D midPoint(final XNode node) {
    Node _node = node.getNode();
    Bounds _boundsInLocal = _node.getBoundsInLocal();
    Point2D _center = BoundsExtensions.center(_boundsInLocal);
    return CoreExtensions.localToRootDiagram(node, _center);
  }
  
  public SplineShapeKeeper(final XConnection connection) {
    super();
    this.connection = connection;
  }
}
