package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.anchors.ArrowHead;
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
        final Point2D arrowTip = connection.getConnectionRouter().getNearestAnchor(connection.getSource(), IterableExtensions.<XControlPoint>head(controlPoints), null);
        this.correct(controlPoints.get(1), ConnectionExtensions.toPoint2D(IterableExtensions.<XControlPoint>head(controlPoints)), arrowTip, connection.getSourceArrowHead());
      }
      ArrowHead _targetArrowHead = connection.getTargetArrowHead();
      boolean _notEquals_1 = (!Objects.equal(_targetArrowHead, null));
      if (_notEquals_1) {
        final Point2D arrowTip_1 = connection.getConnectionRouter().getNearestAnchor(connection.getTarget(), IterableExtensions.<XControlPoint>last(controlPoints), null);
        int _size_1 = controlPoints.size();
        int _minus = (_size_1 - 2);
        this.correct(controlPoints.get(_minus), ConnectionExtensions.toPoint2D(IterableExtensions.<XControlPoint>last(controlPoints)), arrowTip_1, connection.getTargetArrowHead());
      }
    }
  }
  
  protected void correct(final XControlPoint criticalPoint, final Point2D arrowEnd, final Point2D arrowTip, final ArrowHead arrowHead) {
    if (((!Objects.equal(arrowTip, null)) && (Point2DExtensions.norm(Point2DExtensions.operator_minus(ConnectionExtensions.toPoint2D(criticalPoint), arrowTip)) < arrowHead.getLineCut()))) {
      Point2D delta = Point2DExtensions.operator_minus(arrowTip, arrowEnd);
      double _norm = Point2DExtensions.norm(delta);
      Point2D _divide = Point2DExtensions.operator_divide(delta, _norm);
      delta = _divide;
      DoubleProperty _layoutXProperty = criticalPoint.layoutXProperty();
      double _x = arrowEnd.getX();
      double _x_1 = delta.getX();
      double _minus = (_x - _x_1);
      CoreExtensions.<Number>setSafely(_layoutXProperty, Double.valueOf(_minus));
      DoubleProperty _layoutYProperty = criticalPoint.layoutYProperty();
      double _y = arrowEnd.getY();
      double _y_1 = delta.getY();
      double _minus_1 = (_y - _y_1);
      CoreExtensions.<Number>setSafely(_layoutYProperty, Double.valueOf(_minus_1));
    }
  }
}
