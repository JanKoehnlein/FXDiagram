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
    final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
      this.label2oldPosition.put(it, Double.valueOf(it.getPosition()));
      final Point2D oldLocation = this.connection.at(it.getPosition());
      final Function1<XControlPoint, Point2D> _function_1 = (XControlPoint it_1) -> {
        return ConnectionExtensions.toPoint2D(it_1);
      };
      final ConnectionExtensions.PointOnCurve newPointOnCurve = ConnectionExtensions.getNearestPointOnConnection(oldLocation, ListExtensions.<XControlPoint, Point2D>map(this.newControlPoints, _function_1), this.connection.getKind());
      this.label2newPosition.put(it, Double.valueOf(newPointOnCurve.getParameter()));
      it.setPosition(newPointOnCurve.getParameter());
    };
    this.connection.getLabels().forEach(_function);
    this.connection.getControlPoints().setAll(this.newControlPoints);
    this.splineShapeKeeperState = this.connection.getConnectionRouter().isSplineShapeKeeperEnabled();
    ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
    _connectionRouter.setSplineShapeKeeperEnabled(true);
    boolean _notEquals = (!Objects.equal(this.localMousePos, null));
    if (_notEquals) {
      final Point2D mousePos = this.connection.localToScreen(this.localMousePos);
      final Function1<XControlPoint, Boolean> _function_1 = (XControlPoint it) -> {
        return Boolean.valueOf(it.getSelected());
      };
      final Consumer<XControlPoint> _function_2 = (XControlPoint it) -> {
        MoveBehavior _behavior = it.<MoveBehavior>getBehavior(MoveBehavior.class);
        if (_behavior!=null) {
          _behavior.startDrag(mousePos.getX(), mousePos.getY());
        }
      };
      IterableExtensions.<XControlPoint>filter(this.newControlPoints, _function_1).forEach(_function_2);
    }
  }
  
  @Override
  public void undo(final CommandContext context) {
    final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
      it.setPosition((this.label2oldPosition.get(it)).doubleValue());
    };
    this.connection.getLabels().forEach(_function);
    this.connection.getControlPoints().setAll(this.oldControlPoints);
    ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
    _connectionRouter.setSplineShapeKeeperEnabled(this.splineShapeKeeperState);
  }
  
  @Override
  public void redo(final CommandContext context) {
    final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
      it.setPosition((this.label2newPosition.get(it)).doubleValue());
    };
    this.connection.getLabels().forEach(_function);
    this.connection.getControlPoints().setAll(this.newControlPoints);
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
