package de.fxdiagram.core.command;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.CommandContext;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class AddControlPointCommand extends AbstractCommand {
  private final XConnection connection;
  
  private final int index;
  
  private final Point2D newPoint;
  
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
