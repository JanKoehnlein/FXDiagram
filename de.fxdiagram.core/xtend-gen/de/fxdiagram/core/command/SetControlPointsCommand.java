package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.ConnectionExtensions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class SetControlPointsCommand extends AbstractCommand {
  private final XConnection connection;
  
  private final List<XControlPoint> newControlPoints;
  
  private final Point2D localMousePos;
  
  private boolean splineShapeKeeperState;
  
  private List<XControlPoint> oldControlPoints;
  
  private Map<XConnectionLabel, Double> label2oldPosition = CollectionLiterals.<XConnectionLabel, Double>newHashMap();
  
  private Map<XConnectionLabel, Double> label2newPosition = CollectionLiterals.<XConnectionLabel, Double>newHashMap();
  
  @Override
  public void execute(final CommandContext context) {
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    ArrayList<XControlPoint> _arrayList = new ArrayList<XControlPoint>(_controlPoints);
    this.oldControlPoints = _arrayList;
    ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
      double _position = it.getPosition();
      this.label2oldPosition.put(it, Double.valueOf(_position));
      double _position_1 = it.getPosition();
      final Point2D oldLocation = this.connection.at(_position_1);
      final Function1<XControlPoint, Point2D> _function_1 = (XControlPoint it_1) -> {
        return ConnectionExtensions.toPoint2D(it_1);
      };
      List<Point2D> _map = ListExtensions.<XControlPoint, Point2D>map(this.newControlPoints, _function_1);
      XConnection.Kind _kind = this.connection.getKind();
      final ConnectionExtensions.PointOnCurve newPointOnCurve = ConnectionExtensions.getNearestPointOnConnection(oldLocation, _map, _kind);
      double _parameter = newPointOnCurve.getParameter();
      this.label2newPosition.put(it, Double.valueOf(_parameter));
      double _parameter_1 = newPointOnCurve.getParameter();
      it.setPosition(_parameter_1);
    };
    _labels.forEach(_function);
    ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
    _controlPoints_1.setAll(this.newControlPoints);
    ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
    boolean _isSplineShapeKeeperEnabled = _connectionRouter.isSplineShapeKeeperEnabled();
    this.splineShapeKeeperState = _isSplineShapeKeeperEnabled;
    ConnectionRouter _connectionRouter_1 = this.connection.getConnectionRouter();
    _connectionRouter_1.setSplineShapeKeeperEnabled(true);
    boolean _notEquals = (!Objects.equal(this.localMousePos, null));
    if (_notEquals) {
      final Point2D mousePos = this.connection.localToScreen(this.localMousePos);
      final Function1<XControlPoint, Boolean> _function_1 = (XControlPoint it) -> {
        return Boolean.valueOf(it.getSelected());
      };
      Iterable<XControlPoint> _filter = IterableExtensions.<XControlPoint>filter(this.newControlPoints, _function_1);
      final Consumer<XControlPoint> _function_2 = (XControlPoint it) -> {
        MoveBehavior _behavior = it.<MoveBehavior>getBehavior(MoveBehavior.class);
        if (_behavior!=null) {
          double _x = mousePos.getX();
          double _y = mousePos.getY();
          _behavior.startDrag(_x, _y);
        }
      };
      _filter.forEach(_function_2);
    }
  }
  
  @Override
  public void undo(final CommandContext context) {
    ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
      Double _get = this.label2oldPosition.get(it);
      it.setPosition((_get).doubleValue());
    };
    _labels.forEach(_function);
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    _controlPoints.setAll(this.oldControlPoints);
    ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
    _connectionRouter.setSplineShapeKeeperEnabled(this.splineShapeKeeperState);
  }
  
  @Override
  public void redo(final CommandContext context) {
    ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
      Double _get = this.label2newPosition.get(it);
      it.setPosition((_get).doubleValue());
    };
    _labels.forEach(_function);
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    _controlPoints.setAll(this.newControlPoints);
    ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
    _connectionRouter.setSplineShapeKeeperEnabled(true);
  }
  
  public SetControlPointsCommand(final XConnection connection, final List<XControlPoint> newControlPoints, final Point2D localMousePos) {
    super();
    this.connection = connection;
    this.newControlPoints = newControlPoints;
    this.localMousePos = localMousePos;
  }
}
