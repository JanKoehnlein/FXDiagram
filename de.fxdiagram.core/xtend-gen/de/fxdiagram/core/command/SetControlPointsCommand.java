package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class SetControlPointsCommand extends AbstractCommand {
  private final XConnection connection;
  
  private final List<XControlPoint> newControlPoints;
  
  private final Point2D localMousePos;
  
  private boolean splineShapeKeeperState;
  
  private ArrayList<XControlPoint> oldControlPoints;
  
  @Override
  public void execute(final CommandContext context) {
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    ArrayList<XControlPoint> _arrayList = new ArrayList<XControlPoint>(_controlPoints);
    this.oldControlPoints = _arrayList;
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
      final Function1<XControlPoint, Boolean> _function = (XControlPoint it) -> {
        return Boolean.valueOf(it.getSelected());
      };
      Iterable<XControlPoint> _filter = IterableExtensions.<XControlPoint>filter(this.newControlPoints, _function);
      final Consumer<XControlPoint> _function_1 = (XControlPoint it) -> {
        MoveBehavior _behavior = it.<MoveBehavior>getBehavior(MoveBehavior.class);
        double _x = mousePos.getX();
        double _y = mousePos.getY();
        _behavior.startDrag(_x, _y);
      };
      _filter.forEach(_function_1);
    }
  }
  
  @Override
  public void undo(final CommandContext context) {
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    _controlPoints.setAll(this.oldControlPoints);
    ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
    _connectionRouter.setSplineShapeKeeperEnabled(this.splineShapeKeeperState);
  }
  
  @Override
  public void redo(final CommandContext context) {
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
