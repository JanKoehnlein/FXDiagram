package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.SetControlPointsCommand;
import de.fxdiagram.core.extensions.BezierExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class AddControlPointCommand extends AbstractCommand {
  private final XConnection connection;
  
  private final int index;
  
  private final Point2D newPoint;
  
  public static AbstractCommand createAddControlPointCommand(final XConnection connection, final Point2D localPosition) {
    AbstractCommand _switchResult = null;
    XConnection.Kind _kind = connection.getKind();
    if (_kind != null) {
      switch (_kind) {
        case POLYLINE:
          _switchResult = AddControlPointCommand.createCommandForPolyline(connection, localPosition);
          break;
        case QUAD_CURVE:
          _switchResult = AddControlPointCommand.createCommandForQuadCurve(connection, localPosition);
          break;
        case CUBIC_CURVE:
          _switchResult = AddControlPointCommand.createCommandForCubicCurve(connection, localPosition);
          break;
        default:
          _switchResult = null;
          break;
      }
    } else {
      _switchResult = null;
    }
    return _switchResult;
  }
  
  protected static AddControlPointCommand createCommandForPolyline(final XConnection connection, final Point2D localPosition) {
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
  
  protected static SetControlPointsCommand createCommandForQuadCurve(final XConnection connection, final Point2D localPosition) {
    Node _node = connection.getNode();
    ObservableList<Node> _children = ((Group) _node).getChildren();
    Iterable<QuadCurve> _filter = Iterables.<QuadCurve>filter(_children, QuadCurve.class);
    final List<QuadCurve> splineSegments = IterableExtensions.<QuadCurve>toList(_filter);
    final Function1<QuadCurve, Boolean> _function = (QuadCurve it) -> {
      return Boolean.valueOf(it.contains(localPosition));
    };
    final QuadCurve splineSegment = IterableExtensions.<QuadCurve>findFirst(splineSegments, _function);
    boolean _equals = Objects.equal(splineSegment, null);
    if (_equals) {
      return null;
    }
    final double t = BezierExtensions.findT(splineSegment, localPosition);
    final List<QuadCurve> splitSegments = BezierExtensions.splitAt(splineSegment, t);
    final int segmentIndex = splineSegments.indexOf(splineSegment);
    ObservableList<XControlPoint> _controlPoints = connection.getControlPoints();
    final ArrayList<XControlPoint> oldControlPoints = new ArrayList<XControlPoint>(_controlPoints);
    final ArrayList<XControlPoint> newControlPoints = CollectionLiterals.<XControlPoint>newArrayList();
    int cpIndex = 0;
    for (int i = 0; (i < segmentIndex); i++) {
      {
        int _plusPlus = cpIndex++;
        XControlPoint _get = oldControlPoints.get(_plusPlus);
        newControlPoints.add(_get);
        int _plusPlus_1 = cpIndex++;
        XControlPoint _get_1 = oldControlPoints.get(_plusPlus_1);
        newControlPoints.add(_get_1);
      }
    }
    int _plusPlus = cpIndex++;
    XControlPoint _get = oldControlPoints.get(_plusPlus);
    newControlPoints.add(_get);
    final Procedure1<ArrayList<XControlPoint>> _function_1 = (ArrayList<XControlPoint> it) -> {
      final QuadCurve seg0 = IterableExtensions.<QuadCurve>head(splitSegments);
      double _controlX = seg0.getControlX();
      double _controlY = seg0.getControlY();
      AddControlPointCommand.add(it, _controlX, _controlY, XControlPoint.Type.CONTROL_POINT);
      final QuadCurve seg1 = IterableExtensions.<QuadCurve>last(splitSegments);
      double _startX = seg1.getStartX();
      double _startY = seg1.getStartY();
      XControlPoint _add = AddControlPointCommand.add(it, _startX, _startY, XControlPoint.Type.INTERPOLATED);
      final Procedure1<XControlPoint> _function_2 = (XControlPoint it_1) -> {
        it_1.setSelected(true);
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_add, _function_2);
      double _controlX_1 = seg1.getControlX();
      double _controlY_1 = seg1.getControlY();
      AddControlPointCommand.add(it, _controlX_1, _controlY_1, XControlPoint.Type.CONTROL_POINT);
    };
    ObjectExtensions.<ArrayList<XControlPoint>>operator_doubleArrow(newControlPoints, _function_1);
    int _cpIndex = cpIndex;
    cpIndex = (_cpIndex + 1);
    while ((cpIndex < oldControlPoints.size())) {
      int _plusPlus_1 = cpIndex++;
      XControlPoint _get_1 = oldControlPoints.get(_plusPlus_1);
      newControlPoints.add(_get_1);
    }
    Point2D _localToScreen = connection.localToScreen(localPosition);
    return new SetControlPointsCommand(connection, newControlPoints, _localToScreen);
  }
  
  protected static SetControlPointsCommand createCommandForCubicCurve(final XConnection connection, final Point2D localPosition) {
    Node _node = connection.getNode();
    ObservableList<Node> _children = ((Group) _node).getChildren();
    Iterable<CubicCurve> _filter = Iterables.<CubicCurve>filter(_children, CubicCurve.class);
    final List<CubicCurve> splineSegments = IterableExtensions.<CubicCurve>toList(_filter);
    final Function1<CubicCurve, Boolean> _function = (CubicCurve it) -> {
      return Boolean.valueOf(it.contains(localPosition));
    };
    final CubicCurve splineSegment = IterableExtensions.<CubicCurve>findFirst(splineSegments, _function);
    boolean _equals = Objects.equal(splineSegment, null);
    if (_equals) {
      return null;
    }
    final double t = BezierExtensions.findT(splineSegment, localPosition);
    final List<CubicCurve> splitSegments = BezierExtensions.splitAt(splineSegment, t);
    final int segmentIndex = splineSegments.indexOf(splineSegment);
    ObservableList<XControlPoint> _controlPoints = connection.getControlPoints();
    final ArrayList<XControlPoint> oldControlPoints = new ArrayList<XControlPoint>(_controlPoints);
    final ArrayList<XControlPoint> newControlPoints = CollectionLiterals.<XControlPoint>newArrayList();
    int cpIndex = 0;
    for (int i = 0; (i < segmentIndex); i++) {
      {
        int _plusPlus = cpIndex++;
        XControlPoint _get = oldControlPoints.get(_plusPlus);
        newControlPoints.add(_get);
        int _plusPlus_1 = cpIndex++;
        XControlPoint _get_1 = oldControlPoints.get(_plusPlus_1);
        newControlPoints.add(_get_1);
        int _plusPlus_2 = cpIndex++;
        XControlPoint _get_2 = oldControlPoints.get(_plusPlus_2);
        newControlPoints.add(_get_2);
      }
    }
    int _plusPlus = cpIndex++;
    XControlPoint _get = oldControlPoints.get(_plusPlus);
    newControlPoints.add(_get);
    final Procedure1<ArrayList<XControlPoint>> _function_1 = (ArrayList<XControlPoint> it) -> {
      final CubicCurve seg0 = IterableExtensions.<CubicCurve>head(splitSegments);
      double _controlX1 = seg0.getControlX1();
      double _controlY1 = seg0.getControlY1();
      AddControlPointCommand.add(it, _controlX1, _controlY1, XControlPoint.Type.CONTROL_POINT);
      double _controlX2 = seg0.getControlX2();
      double _controlY2 = seg0.getControlY2();
      AddControlPointCommand.add(it, _controlX2, _controlY2, XControlPoint.Type.CONTROL_POINT);
      final CubicCurve seg1 = IterableExtensions.<CubicCurve>last(splitSegments);
      double _startX = seg1.getStartX();
      double _startY = seg1.getStartY();
      XControlPoint _add = AddControlPointCommand.add(it, _startX, _startY, XControlPoint.Type.INTERPOLATED);
      final Procedure1<XControlPoint> _function_2 = (XControlPoint it_1) -> {
        it_1.setSelected(true);
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_add, _function_2);
      double _controlX1_1 = seg1.getControlX1();
      double _controlY1_1 = seg1.getControlY1();
      AddControlPointCommand.add(it, _controlX1_1, _controlY1_1, XControlPoint.Type.CONTROL_POINT);
      double _controlX2_1 = seg1.getControlX2();
      double _controlY2_1 = seg1.getControlY2();
      AddControlPointCommand.add(it, _controlX2_1, _controlY2_1, XControlPoint.Type.CONTROL_POINT);
    };
    ObjectExtensions.<ArrayList<XControlPoint>>operator_doubleArrow(newControlPoints, _function_1);
    int _cpIndex = cpIndex;
    cpIndex = (_cpIndex + 2);
    while ((cpIndex < oldControlPoints.size())) {
      int _plusPlus_1 = cpIndex++;
      XControlPoint _get_1 = oldControlPoints.get(_plusPlus_1);
      newControlPoints.add(_get_1);
    }
    return new SetControlPointsCommand(connection, newControlPoints, localPosition);
  }
  
  protected static XControlPoint add(final List<XControlPoint> controlPoints, final double x, final double y, final XControlPoint.Type cpType) {
    XControlPoint _xblockexpression = null;
    {
      XControlPoint _xControlPoint = new XControlPoint();
      final Procedure1<XControlPoint> _function = (XControlPoint it) -> {
        it.setLayoutX(x);
        it.setLayoutY(y);
        it.setType(cpType);
      };
      final XControlPoint newPoint = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
      controlPoints.add(newPoint);
      _xblockexpression = newPoint;
    }
    return _xblockexpression;
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
    if (_behavior!=null) {
      double _x = mousePos.getX();
      double _y = mousePos.getY();
      _behavior.startDrag(_x, _y);
    }
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
