package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.SetControlPointsCommand;
import de.fxdiagram.core.extensions.BezierExtensions;
import de.fxdiagram.core.extensions.ConnectionExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
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
    final SetControlPointsCommand createCommand = this.createAddControlPointCommand(newPointLocalPosition);
    boolean _notEquals = (!Objects.equal(createCommand, null));
    if (_notEquals) {
      XConnection _host = this.getHost();
      XRoot _root = CoreExtensions.getRoot(_host);
      CommandStack _commandStack = _root.getCommandStack();
      _commandStack.execute(createCommand);
    }
  }
  
  protected SetControlPointsCommand createAddControlPointCommand(final Point2D localPosition) {
    SetControlPointsCommand _switchResult = null;
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
  
  protected SetControlPointsCommand createCommandForPolyline(final Point2D localPosition) {
    XConnection _host = this.getHost();
    ObservableList<XControlPoint> _controlPoints = _host.getControlPoints();
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
      return ConnectionExtensions.toPoint2D(it);
    };
    List<Point2D> _map = ListExtensions.<XControlPoint, Point2D>map(_controlPoints, _function);
    final ConnectionExtensions.PointOnCurve nearestPoint = ConnectionExtensions.getNearestPointOnPolyline(localPosition, _map);
    boolean _equals = Objects.equal(nearestPoint, null);
    if (_equals) {
      return null;
    } else {
      XConnection _host_1 = this.getHost();
      ObservableList<XControlPoint> _controlPoints_1 = _host_1.getControlPoints();
      final ArrayList<XControlPoint> newPoints = new ArrayList<XControlPoint>(_controlPoints_1);
      int _segmentIndex = nearestPoint.getSegmentIndex();
      int _plus = (_segmentIndex + 1);
      XControlPoint _xControlPoint = new XControlPoint();
      final Procedure1<XControlPoint> _function_1 = (XControlPoint it) -> {
        Point2D _point = nearestPoint.getPoint();
        double _x = _point.getX();
        it.setLayoutX(_x);
        Point2D _point_1 = nearestPoint.getPoint();
        double _y = _point_1.getY();
        it.setLayoutY(_y);
        it.setType(XControlPoint.Type.DANGLING);
        it.setSelected(true);
      };
      XControlPoint _doubleArrow = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function_1);
      newPoints.add(_plus, _doubleArrow);
      XConnection _host_2 = this.getHost();
      Point2D _point = nearestPoint.getPoint();
      return new SetControlPointsCommand(_host_2, newPoints, _point);
    }
  }
  
  protected SetControlPointsCommand createCommandForQuadCurve(final Point2D localPosition) {
    XConnection _host = this.getHost();
    ObservableList<XControlPoint> _controlPoints = _host.getControlPoints();
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
      return ConnectionExtensions.toPoint2D(it);
    };
    List<Point2D> _map = ListExtensions.<XControlPoint, Point2D>map(_controlPoints, _function);
    final ConnectionExtensions.PointOnCurve nearestPoint = ConnectionExtensions.getNearestPointOnQuadraticSpline(localPosition, _map);
    XConnection _host_1 = this.getHost();
    Node _node = _host_1.getNode();
    ObservableList<Node> _children = ((Group) _node).getChildren();
    Iterable<QuadCurve> _filter = Iterables.<QuadCurve>filter(_children, QuadCurve.class);
    int _segmentIndex = nearestPoint.getSegmentIndex();
    final QuadCurve splineSegment = ((QuadCurve[])Conversions.unwrapArray(_filter, QuadCurve.class))[_segmentIndex];
    double _localParameter = nearestPoint.getLocalParameter();
    final List<QuadCurve> splitSegments = BezierExtensions.splitAt(splineSegment, _localParameter);
    XConnection _host_2 = this.getHost();
    ObservableList<XControlPoint> _controlPoints_1 = _host_2.getControlPoints();
    final ArrayList<XControlPoint> oldControlPoints = new ArrayList<XControlPoint>(_controlPoints_1);
    final ArrayList<XControlPoint> newControlPoints = CollectionLiterals.<XControlPoint>newArrayList();
    int cpIndex = 0;
    for (int i = 0; (i < nearestPoint.getSegmentIndex()); i++) {
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
    XConnection _host_3 = this.getHost();
    XConnection _host_4 = this.getHost();
    Point2D _localToScreen = _host_4.localToScreen(localPosition);
    return new SetControlPointsCommand(_host_3, newControlPoints, _localToScreen);
  }
  
  protected SetControlPointsCommand createCommandForCubicCurve(final Point2D localPosition) {
    XConnection _host = this.getHost();
    ObservableList<XControlPoint> _controlPoints = _host.getControlPoints();
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
      return ConnectionExtensions.toPoint2D(it);
    };
    List<Point2D> _map = ListExtensions.<XControlPoint, Point2D>map(_controlPoints, _function);
    final ConnectionExtensions.PointOnCurve nearestPoint = ConnectionExtensions.getNearestPointOnCubicSpline(localPosition, _map);
    XConnection _host_1 = this.getHost();
    Node _node = _host_1.getNode();
    ObservableList<Node> _children = ((Group) _node).getChildren();
    Iterable<CubicCurve> _filter = Iterables.<CubicCurve>filter(_children, CubicCurve.class);
    int _segmentIndex = nearestPoint.getSegmentIndex();
    final CubicCurve splineSegment = ((CubicCurve[])Conversions.unwrapArray(_filter, CubicCurve.class))[_segmentIndex];
    double _localParameter = nearestPoint.getLocalParameter();
    final List<CubicCurve> splitSegments = BezierExtensions.splitAt(splineSegment, _localParameter);
    XConnection _host_2 = this.getHost();
    ObservableList<XControlPoint> _controlPoints_1 = _host_2.getControlPoints();
    final ArrayList<XControlPoint> oldControlPoints = new ArrayList<XControlPoint>(_controlPoints_1);
    final ArrayList<XControlPoint> newControlPoints = CollectionLiterals.<XControlPoint>newArrayList();
    int cpIndex = 0;
    for (int i = 0; (i < nearestPoint.getSegmentIndex()); i++) {
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
    XConnection _host_3 = this.getHost();
    return new SetControlPointsCommand(_host_3, newControlPoints, localPosition);
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
