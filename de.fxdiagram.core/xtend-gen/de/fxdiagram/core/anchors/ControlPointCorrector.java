package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.extensions.ConnectionExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Makes sure that the outer control points don't lie within the arrow head
 */
@SuppressWarnings("all")
public class ControlPointCorrector {
  public void correctControlPoints(final XConnection connection) {
    final ObservableList<XControlPoint> controlPoints = connection.getControlPoints();
    int _size = controlPoints.size();
    boolean _greaterThan = (_size > 2);
    if (_greaterThan) {
      ArrowHead _sourceArrowHead = connection.getSourceArrowHead();
      boolean _notEquals = (!Objects.equal(_sourceArrowHead, null));
      if (_notEquals) {
        ConnectionRouter _connectionRouter = connection.getConnectionRouter();
        XNode _source = connection.getSource();
        XControlPoint _head = IterableExtensions.<XControlPoint>head(controlPoints);
        final Point2D arrowTip = _connectionRouter.getNearestAnchor(_source, _head, null);
        XControlPoint _get = controlPoints.get(1);
        XControlPoint _head_1 = IterableExtensions.<XControlPoint>head(controlPoints);
        Point2D _point2D = ConnectionExtensions.toPoint2D(_head_1);
        ArrowHead _sourceArrowHead_1 = connection.getSourceArrowHead();
        this.correct(_get, _point2D, arrowTip, _sourceArrowHead_1);
      }
      ArrowHead _targetArrowHead = connection.getTargetArrowHead();
      boolean _notEquals_1 = (!Objects.equal(_targetArrowHead, null));
      if (_notEquals_1) {
        ConnectionRouter _connectionRouter_1 = connection.getConnectionRouter();
        XNode _target = connection.getTarget();
        XControlPoint _last = IterableExtensions.<XControlPoint>last(controlPoints);
        final Point2D arrowTip_1 = _connectionRouter_1.getNearestAnchor(_target, _last, null);
        int _size_1 = controlPoints.size();
        int _minus = (_size_1 - 2);
        XControlPoint _get_1 = controlPoints.get(_minus);
        XControlPoint _last_1 = IterableExtensions.<XControlPoint>last(controlPoints);
        Point2D _point2D_1 = ConnectionExtensions.toPoint2D(_last_1);
        ArrowHead _targetArrowHead_1 = connection.getTargetArrowHead();
        this.correct(_get_1, _point2D_1, arrowTip_1, _targetArrowHead_1);
      }
    }
  }
  
  protected void correct(final XControlPoint criticalPoint, final Point2D arrowEnd, final Point2D arrowTip, final ArrowHead arrowHead) {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(arrowTip, null));
    if (!_notEquals) {
      _and = false;
    } else {
      Point2D _point2D = ConnectionExtensions.toPoint2D(criticalPoint);
      Point2D _minus = Point2DExtensions.operator_minus(_point2D, arrowTip);
      double _norm = Point2DExtensions.norm(_minus);
      double _lineCut = arrowHead.getLineCut();
      boolean _lessThan = (_norm < _lineCut);
      _and = _lessThan;
    }
    if (_and) {
      Point2D delta = Point2DExtensions.operator_minus(arrowTip, arrowEnd);
      double _norm_1 = Point2DExtensions.norm(delta);
      Point2D _divide = Point2DExtensions.operator_divide(delta, _norm_1);
      delta = _divide;
      DoubleProperty _layoutXProperty = criticalPoint.layoutXProperty();
      double _x = arrowEnd.getX();
      double _x_1 = delta.getX();
      double _minus_1 = (_x - _x_1);
      CoreExtensions.<Number>setSafely(_layoutXProperty, Double.valueOf(_minus_1));
      DoubleProperty _layoutYProperty = criticalPoint.layoutYProperty();
      double _y = arrowEnd.getY();
      double _y_1 = delta.getY();
      double _minus_2 = (_y - _y_1);
      CoreExtensions.<Number>setSafely(_layoutYProperty, Double.valueOf(_minus_2));
    }
  }
}
