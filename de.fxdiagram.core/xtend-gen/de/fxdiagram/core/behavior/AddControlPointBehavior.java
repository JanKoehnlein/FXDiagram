package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.AddControlPointCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.SetControlPointsCommand;
import de.fxdiagram.core.extensions.BezierExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class AddControlPointBehavior extends AbstractHostBehavior<XConnection> {
  public AddControlPointBehavior(final XConnection connection) {
    super(connection);
  }
  
  @Override
  protected void doActivate() {
  }
  
  @Override
  public Class<? extends Behavior> getBehaviorKey() {
    return AddControlPointBehavior.class;
  }
  
  public void addControlPoint(final Point2D newPointLocalPosition) {
    final AbstractCommand createCommand = this.createAddControlPointCommand(newPointLocalPosition);
    boolean _notEquals = (!Objects.equal(createCommand, null));
    if (_notEquals) {
      XConnection _host = this.getHost();
      XRoot _root = CoreExtensions.getRoot(_host);
      CommandStack _commandStack = _root.getCommandStack();
      _commandStack.execute(createCommand);
    }
  }
  
  protected AbstractCommand createAddControlPointCommand(final Point2D localPosition) {
    AbstractCommand _switchResult = null;
    XConnection _host = this.getHost();
    XConnection.Kind _kind = _host.getKind();
    if (_kind != null) {
      switch (_kind) {
        case POLYLINE:
          _switchResult = this.createCommandForPolyline(localPosition);
          break;
        case QUAD_CURVE:
          _switchResult = this.createCommandForQuadCurve(localPosition);
          break;
        case CUBIC_CURVE:
          _switchResult = this.createCommandForCubicCurve(localPosition);
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
  
  protected AddControlPointCommand createCommandForPolyline(final Point2D localPosition) {
    XConnection _host = this.getHost();
    final ObservableList<XControlPoint> controlPoints = _host.getControlPoints();
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
      XConnection _host_1 = this.getHost();
      return new AddControlPointCommand(_host_1, index, newPoint);
    }
  }
  
  protected SetControlPointsCommand createCommandForQuadCurve(final Point2D localPosition) {
    XConnection _host = this.getHost();
    Node _node = _host.getNode();
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
    XConnection _host_1 = this.getHost();
    ObservableList<XControlPoint> _controlPoints = _host_1.getControlPoints();
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
      this.add(it, _controlX, _controlY, XControlPoint.Type.CONTROL_POINT);
      final QuadCurve seg1 = IterableExtensions.<QuadCurve>last(splitSegments);
      double _startX = seg1.getStartX();
      double _startY = seg1.getStartY();
      XControlPoint _add = this.add(it, _startX, _startY, XControlPoint.Type.INTERPOLATED);
      final Procedure1<XControlPoint> _function_2 = (XControlPoint it_1) -> {
        it_1.setSelected(true);
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_add, _function_2);
      double _controlX_1 = seg1.getControlX();
      double _controlY_1 = seg1.getControlY();
      this.add(it, _controlX_1, _controlY_1, XControlPoint.Type.CONTROL_POINT);
    };
    ObjectExtensions.<ArrayList<XControlPoint>>operator_doubleArrow(newControlPoints, _function_1);
    int _cpIndex = cpIndex;
    cpIndex = (_cpIndex + 1);
    while ((cpIndex < oldControlPoints.size())) {
      int _plusPlus_1 = cpIndex++;
      XControlPoint _get_1 = oldControlPoints.get(_plusPlus_1);
      newControlPoints.add(_get_1);
    }
    XConnection _host_2 = this.getHost();
    XConnection _host_3 = this.getHost();
    Point2D _localToScreen = _host_3.localToScreen(localPosition);
    return new SetControlPointsCommand(_host_2, newControlPoints, _localToScreen);
  }
  
  protected SetControlPointsCommand createCommandForCubicCurve(final Point2D localPosition) {
    XConnection _host = this.getHost();
    Node _node = _host.getNode();
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
    XConnection _host_1 = this.getHost();
    ObservableList<XControlPoint> _controlPoints = _host_1.getControlPoints();
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
      this.add(it, _controlX1, _controlY1, XControlPoint.Type.CONTROL_POINT);
      double _controlX2 = seg0.getControlX2();
      double _controlY2 = seg0.getControlY2();
      this.add(it, _controlX2, _controlY2, XControlPoint.Type.CONTROL_POINT);
      final CubicCurve seg1 = IterableExtensions.<CubicCurve>last(splitSegments);
      double _startX = seg1.getStartX();
      double _startY = seg1.getStartY();
      XControlPoint _add = this.add(it, _startX, _startY, XControlPoint.Type.INTERPOLATED);
      final Procedure1<XControlPoint> _function_2 = (XControlPoint it_1) -> {
        it_1.setSelected(true);
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_add, _function_2);
      double _controlX1_1 = seg1.getControlX1();
      double _controlY1_1 = seg1.getControlY1();
      this.add(it, _controlX1_1, _controlY1_1, XControlPoint.Type.CONTROL_POINT);
      double _controlX2_1 = seg1.getControlX2();
      double _controlY2_1 = seg1.getControlY2();
      this.add(it, _controlX2_1, _controlY2_1, XControlPoint.Type.CONTROL_POINT);
    };
    ObjectExtensions.<ArrayList<XControlPoint>>operator_doubleArrow(newControlPoints, _function_1);
    int _cpIndex = cpIndex;
    cpIndex = (_cpIndex + 2);
    while ((cpIndex < oldControlPoints.size())) {
      int _plusPlus_1 = cpIndex++;
      XControlPoint _get_1 = oldControlPoints.get(_plusPlus_1);
      newControlPoints.add(_get_1);
    }
    XConnection _host_2 = this.getHost();
    return new SetControlPointsCommand(_host_2, newControlPoints, localPosition);
  }
  
  protected XControlPoint add(final List<XControlPoint> controlPoints, final double x, final double y, final XControlPoint.Type cpType) {
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
}
