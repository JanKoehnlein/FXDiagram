package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
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
      CoreExtensions.getRoot(this.getHost()).getCommandStack().execute(createCommand);
    }
  }
  
  protected SetControlPointsCommand createAddControlPointCommand(final Point2D localPosition) {
    SetControlPointsCommand _switchResult = null;
    XConnection.Kind _kind = this.getHost().getKind();
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
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
      return ConnectionExtensions.toPoint2D(it);
    };
    final ConnectionExtensions.PointOnCurve nearestPoint = ConnectionExtensions.getNearestPointOnPolyline(localPosition, ListExtensions.<XControlPoint, Point2D>map(this.getHost().getControlPoints(), _function));
    boolean _equals = Objects.equal(nearestPoint, null);
    if (_equals) {
      return null;
    } else {
      ObservableList<XControlPoint> _controlPoints = this.getHost().getControlPoints();
      final ArrayList<XControlPoint> newPoints = new ArrayList<XControlPoint>(_controlPoints);
      int _segmentIndex = nearestPoint.getSegmentIndex();
      int _plus = (_segmentIndex + 1);
      XControlPoint _xControlPoint = new XControlPoint();
      final Procedure1<XControlPoint> _function_1 = (XControlPoint it) -> {
        it.setLayoutX(nearestPoint.getPoint().getX());
        it.setLayoutY(nearestPoint.getPoint().getY());
        it.setType(XControlPoint.Type.DANGLING);
        it.setSelected(true);
      };
      XControlPoint _doubleArrow = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function_1);
      newPoints.add(_plus, _doubleArrow);
      XConnection _host = this.getHost();
      Point2D _point = nearestPoint.getPoint();
      return new SetControlPointsCommand(_host, newPoints, _point);
    }
  }
  
  protected SetControlPointsCommand createCommandForQuadCurve(final Point2D localPosition) {
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
      return ConnectionExtensions.toPoint2D(it);
    };
    final ConnectionExtensions.PointOnCurve nearestPoint = ConnectionExtensions.getNearestPointOnQuadraticSpline(localPosition, ListExtensions.<XControlPoint, Point2D>map(this.getHost().getControlPoints(), _function));
    Node _node = this.getHost().getNode();
    final QuadCurve splineSegment = ((QuadCurve[])Conversions.unwrapArray(Iterables.<QuadCurve>filter(((Group) _node).getChildren(), QuadCurve.class), QuadCurve.class))[nearestPoint.getSegmentIndex()];
    final List<QuadCurve> splitSegments = BezierExtensions.splitAt(splineSegment, nearestPoint.getLocalParameter());
    ObservableList<XControlPoint> _controlPoints = this.getHost().getControlPoints();
    final ArrayList<XControlPoint> oldControlPoints = new ArrayList<XControlPoint>(_controlPoints);
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
      this.add(it, seg0.getControlX(), seg0.getControlY(), XControlPoint.Type.CONTROL_POINT);
      final QuadCurve seg1 = IterableExtensions.<QuadCurve>last(splitSegments);
      XControlPoint _add = this.add(it, seg1.getStartX(), seg1.getStartY(), XControlPoint.Type.INTERPOLATED);
      final Procedure1<XControlPoint> _function_2 = (XControlPoint it_1) -> {
        it_1.setSelected(true);
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_add, _function_2);
      this.add(it, seg1.getControlX(), seg1.getControlY(), XControlPoint.Type.CONTROL_POINT);
    };
    ObjectExtensions.<ArrayList<XControlPoint>>operator_doubleArrow(newControlPoints, _function_1);
    int _cpIndex = cpIndex;
    cpIndex = (_cpIndex + 1);
    while ((cpIndex < oldControlPoints.size())) {
      int _plusPlus_1 = cpIndex++;
      XControlPoint _get_1 = oldControlPoints.get(_plusPlus_1);
      newControlPoints.add(_get_1);
    }
    XConnection _host = this.getHost();
    Point2D _localToScreen = this.getHost().localToScreen(localPosition);
    return new SetControlPointsCommand(_host, newControlPoints, _localToScreen);
  }
  
  protected SetControlPointsCommand createCommandForCubicCurve(final Point2D localPosition) {
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
      return ConnectionExtensions.toPoint2D(it);
    };
    final ConnectionExtensions.PointOnCurve nearestPoint = ConnectionExtensions.getNearestPointOnCubicSpline(localPosition, ListExtensions.<XControlPoint, Point2D>map(this.getHost().getControlPoints(), _function));
    Node _node = this.getHost().getNode();
    final CubicCurve splineSegment = ((CubicCurve[])Conversions.unwrapArray(Iterables.<CubicCurve>filter(((Group) _node).getChildren(), CubicCurve.class), CubicCurve.class))[nearestPoint.getSegmentIndex()];
    final List<CubicCurve> splitSegments = BezierExtensions.splitAt(splineSegment, nearestPoint.getLocalParameter());
    ObservableList<XControlPoint> _controlPoints = this.getHost().getControlPoints();
    final ArrayList<XControlPoint> oldControlPoints = new ArrayList<XControlPoint>(_controlPoints);
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
      this.add(it, seg0.getControlX1(), seg0.getControlY1(), XControlPoint.Type.CONTROL_POINT);
      this.add(it, seg0.getControlX2(), seg0.getControlY2(), XControlPoint.Type.CONTROL_POINT);
      final CubicCurve seg1 = IterableExtensions.<CubicCurve>last(splitSegments);
      XControlPoint _add = this.add(it, seg1.getStartX(), seg1.getStartY(), XControlPoint.Type.INTERPOLATED);
      final Procedure1<XControlPoint> _function_2 = (XControlPoint it_1) -> {
        it_1.setSelected(true);
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_add, _function_2);
      this.add(it, seg1.getControlX1(), seg1.getControlY1(), XControlPoint.Type.CONTROL_POINT);
      this.add(it, seg1.getControlX2(), seg1.getControlY2(), XControlPoint.Type.CONTROL_POINT);
    };
    ObjectExtensions.<ArrayList<XControlPoint>>operator_doubleArrow(newControlPoints, _function_1);
    int _cpIndex = cpIndex;
    cpIndex = (_cpIndex + 2);
    while ((cpIndex < oldControlPoints.size())) {
      int _plusPlus_1 = cpIndex++;
      XControlPoint _get_1 = oldControlPoints.get(_plusPlus_1);
      newControlPoints.add(_get_1);
    }
    XConnection _host = this.getHost();
    return new SetControlPointsCommand(_host, newControlPoints, localPosition);
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
