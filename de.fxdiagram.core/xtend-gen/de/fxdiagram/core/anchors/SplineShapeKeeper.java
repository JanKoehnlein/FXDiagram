package de.fxdiagram.core.anchors;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
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
  
  protected Point2D adjustControlPointsToNodeMove() {
    Point2D _xifexpression = null;
    if ((((((this.connection.getIsActive() && (!Objects.equal(this.connection.getKind(), XConnection.Kind.POLYLINE))) && (this.getControlPoints().size() > 2)) && (!Objects.equal(this.lastSourceCenter, null))) && (!Objects.equal(this.lastTargetCenter, null))) && (!IterableExtensions.<XControlPoint>exists(this.getControlPoints(), ((Function1<XControlPoint, Boolean>) (XControlPoint it) -> {
      return Boolean.valueOf(((it.getSelected() || it.layoutXProperty().isBound()) || it.layoutYProperty().isBound()));
    }))))) {
      Point2D _xblockexpression = null;
      {
        final Point2D newSourceCenter = this.midPoint(this.connection.getSource());
        final Point2D newTargetCenter = this.midPoint(this.connection.getTarget());
        Point2D oldFixedPoint = this.lastTargetCenter;
        Point2D newFixedPoint = newTargetCenter;
        int _size = this.getControlPoints().size();
        int _minus = (_size - 1);
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
        for (final Integer i : _doubleDotLessThan) {
          this.adjust(this.getControlPoints().get((i).intValue()), oldFixedPoint, this.lastSourceCenter, newFixedPoint, newSourceCenter);
        }
        this.lastSourceCenter = newSourceCenter;
        _xblockexpression = this.lastTargetCenter = newTargetCenter;
      }
      _xifexpression = _xblockexpression;
    } else {
      Point2D _xblockexpression_1 = null;
      {
        this.lastSourceCenter = this.midPoint(this.connection.getSource());
        _xblockexpression_1 = this.lastTargetCenter = this.midPoint(this.connection.getTarget());
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
      cp.setLayoutX(newPointOnLine.getX());
      cp.setLayoutY(newPointOnLine.getY());
    } else {
      double _y_6 = newDelta.getY();
      double _x_6 = newDelta.getX();
      double _minus_1 = (-_x_6);
      Point2D _point2D = new Point2D(_y_6, _minus_1);
      final Point2D orthoNewDelta = Point2DExtensions.operator_multiply(theta, _point2D);
      double _x_7 = newPointOnLine.getX();
      double _x_8 = orthoNewDelta.getX();
      double _plus_1 = (_x_7 + _x_8);
      cp.setLayoutX(_plus_1);
      double _y_7 = newPointOnLine.getY();
      double _y_8 = orthoNewDelta.getY();
      double _plus_2 = (_y_7 + _y_8);
      cp.setLayoutY(_plus_2);
    }
  }
  
  protected Point2D midPoint(final XNode node) {
    return CoreExtensions.localToRootDiagram(node, BoundsExtensions.center(node.getNode().getBoundsInLocal()));
  }
  
  public SplineShapeKeeper(final XConnection connection) {
    super();
    this.connection = connection;
  }
}
