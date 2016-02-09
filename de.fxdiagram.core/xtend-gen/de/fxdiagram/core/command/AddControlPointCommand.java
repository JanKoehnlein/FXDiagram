package de.fxdiagram.core.command;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.ConnectionExtensions;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class AddControlPointCommand extends AbstractCommand {
  private final XConnection connection;
  
  private final int index;
  
  private final Point2D newPoint;
  
  private XControlPoint newControlPoint;
  
  private Map<XConnectionLabel, Double> label2oldPosition = CollectionLiterals.<XConnectionLabel, Double>newHashMap();
  
  private Map<XConnectionLabel, Double> label2newPosition = CollectionLiterals.<XConnectionLabel, Double>newHashMap();
  
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
    XControlPoint _doubleArrow = ObjectExtensions.<XControlPoint>operator_doubleArrow(_xControlPoint, _function);
    this.newControlPoint = _doubleArrow;
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    final ArrayList<XControlPoint> newControlPoints = new ArrayList<XControlPoint>(_controlPoints);
    newControlPoints.add(this.index, this.newControlPoint);
    ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function_1 = (XConnectionLabel it) -> {
      double _position = it.getPosition();
      this.label2oldPosition.put(it, Double.valueOf(_position));
      double _position_1 = it.getPosition();
      final Point2D oldLocation = this.connection.at(_position_1);
      XConnection.Kind _kind = this.connection.getKind();
      final ConnectionExtensions.PointOnCurve newPointOnCurve = ConnectionExtensions.getNearestPointOnConnection(oldLocation, newControlPoints, _kind);
      double _parameter = newPointOnCurve.getParameter();
      this.label2newPosition.put(it, Double.valueOf(_parameter));
      double _parameter_1 = newPointOnCurve.getParameter();
      it.setPosition(_parameter_1);
    };
    _labels.forEach(_function_1);
    ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
    _controlPoints_1.add(this.index, this.newControlPoint);
    this.connection.updateShapes();
    Parent _parent = this.newControlPoint.getParent();
    final Point2D mousePos = _parent.localToScreen(this.newPoint);
    MoveBehavior _behavior = this.newControlPoint.<MoveBehavior>getBehavior(MoveBehavior.class);
    if (_behavior!=null) {
      double _x = mousePos.getX();
      double _y = mousePos.getY();
      _behavior.startDrag(_x, _y);
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
    _controlPoints.remove(this.index);
    this.connection.updateShapes();
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
    _controlPoints.add(this.index, this.newControlPoint);
    this.connection.updateShapes();
    Parent _parent = this.newControlPoint.getParent();
    final Point2D mousePos = _parent.localToScreen(this.newPoint);
    MoveBehavior _behavior = this.newControlPoint.<MoveBehavior>getBehavior(MoveBehavior.class);
    if (_behavior!=null) {
      double _x = mousePos.getX();
      double _y = mousePos.getY();
      _behavior.startDrag(_x, _y);
    }
  }
  
  public AddControlPointCommand(final XConnection connection, final int index, final Point2D newPoint) {
    super();
    this.connection = connection;
    this.index = index;
    this.newPoint = newPoint;
  }
}
