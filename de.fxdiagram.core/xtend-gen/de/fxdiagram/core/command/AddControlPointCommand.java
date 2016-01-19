package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class AddControlPointCommand extends AbstractCommand {
  private final XConnection connection;
  
  private final int index;
  
  private final Point2D newPoint;
  
  public static AddControlPointCommand createAddControlPointCommand(final XConnection connection, final Point2D localPosition) {
    XConnection.Kind _kind = connection.getKind();
    boolean _notEquals = (!Objects.equal(_kind, XConnection.Kind.POLYLINE));
    if (_notEquals) {
      return null;
    }
    final ObservableList<XControlPoint> controlPoints = connection.getControlPoints();
    int index = (-1);
    Point2D newPoint = null;
    int _size = controlPoints.size();
    int _minus = (_size - 2);
    IntegerRange _upTo = new IntegerRange(0, _minus);
    for (final Integer i : _upTo) {
      {
        XControlPoint _get = controlPoints.get((i).intValue());
        double _layoutX = _get.getLayoutX();
        XControlPoint _get_1 = controlPoints.get((i).intValue());
        double _layoutY = _get_1.getLayoutY();
        final Point2D segmentStart = new Point2D(_layoutX, _layoutY);
        XControlPoint _get_2 = controlPoints.get(((i).intValue() + 1));
        double _layoutX_1 = _get_2.getLayoutX();
        XControlPoint _get_3 = controlPoints.get(((i).intValue() + 1));
        double _layoutY_1 = _get_3.getLayoutY();
        final Point2D segmentEnd = new Point2D(_layoutX_1, _layoutY_1);
        boolean _or = false;
        double _distance = localPosition.distance(segmentStart);
        boolean _lessThan = (_distance < NumberExpressionExtensions.EPSILON);
        if (_lessThan) {
          _or = true;
        } else {
          double _distance_1 = localPosition.distance(segmentEnd);
          boolean _lessThan_1 = (_distance_1 < NumberExpressionExtensions.EPSILON);
          _or = _lessThan_1;
        }
        if (_or) {
          return null;
        }
        final Point2D delta0 = Point2DExtensions.operator_minus(localPosition, segmentStart);
        final Point2D delta1 = Point2DExtensions.operator_minus(segmentEnd, segmentStart);
        double _x = delta0.getX();
        double _x_1 = delta1.getX();
        double _multiply = (_x * _x_1);
        double _y = delta0.getY();
        double _y_1 = delta1.getY();
        double _multiply_1 = (_y * _y_1);
        double _plus = (_multiply + _multiply_1);
        double _x_2 = delta1.getX();
        double _x_3 = delta1.getX();
        double _multiply_2 = (_x_2 * _x_3);
        double _y_2 = delta1.getY();
        double _y_3 = delta1.getY();
        double _multiply_3 = (_y_2 * _y_3);
        double _plus_1 = (_multiply_2 + _multiply_3);
        final double projectionScale = (_plus / _plus_1);
        Point2D _multiply_4 = Point2DExtensions.operator_multiply(projectionScale, delta1);
        Point2D testPoint = Point2DExtensions.operator_plus(segmentStart, _multiply_4);
        final Point2D delta = Point2DExtensions.operator_minus(testPoint, localPosition);
        boolean _and = false;
        boolean _and_1 = false;
        double _norm = Point2DExtensions.norm(delta);
        boolean _lessThan_2 = (_norm < 1);
        if (!_lessThan_2) {
          _and_1 = false;
        } else {
          _and_1 = (projectionScale >= 0);
        }
        if (!_and_1) {
          _and = false;
        } else {
          _and = (projectionScale <= 1);
        }
        if (_and) {
          index = ((i).intValue() + 1);
          newPoint = testPoint;
        }
      }
    }
    if ((index == (-1))) {
      return null;
    } else {
      return new AddControlPointCommand(connection, index, newPoint);
    }
  }
  
  @Override
  public void execute(final CommandContext context) {
    XControlPoint _xControlPoint = new XControlPoint();
    final Procedure1<XControlPoint> _function = (XControlPoint it) -> {
      double _x = this.newPoint.getX();
      it.setLayoutX(_x);
      double _y = this.newPoint.getY();
      it.setLayoutY(_y);
      it.setType(XControlPoint.Type.DANGLING);
      it.setSelected(true);
    };
    final XControlPoint newControlPoint = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    _controlPoints.add(this.index, newControlPoint);
    this.connection.updateShapes();
    Parent _parent = newControlPoint.getParent();
    final Point2D mousePos = _parent.localToScreen(this.newPoint);
    MoveBehavior _behavior = newControlPoint.<MoveBehavior>getBehavior(MoveBehavior.class);
    double _x = mousePos.getX();
    double _y = mousePos.getY();
    _behavior.startDrag(_x, _y);
  }
  
  @Override
  public void undo(final CommandContext context) {
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    _controlPoints.remove(this.index);
    this.connection.updateShapes();
  }
  
  @Override
  public void redo(final CommandContext context) {
    this.execute(context);
  }
  
  public AddControlPointCommand(final XConnection connection, final int index, final Point2D newPoint) {
    super();
    this.connection = connection;
    this.index = index;
    this.newPoint = newPoint;
  }
}
