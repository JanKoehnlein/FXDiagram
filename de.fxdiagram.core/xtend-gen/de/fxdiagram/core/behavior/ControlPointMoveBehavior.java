package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ConnectionMemento;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.RemoveDanglingControlPointsCommand;
import de.fxdiagram.core.extensions.ConnectionExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Parent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ControlPointMoveBehavior extends MoveBehavior<XControlPoint> {
  public ControlPointMoveBehavior(final XControlPoint host) {
    super(host);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    final ChangeListener<Boolean> _function = (ObservableValue<? extends Boolean> p, Boolean o, Boolean newValue) -> {
      final XConnection connection = this.getConnection();
      if ((((!(newValue).booleanValue()) && (!Objects.equal(connection, null))) && (Objects.equal(connection.getKind(), XConnection.Kind.POLYLINE) || Objects.equal(connection.getKind(), XConnection.Kind.RECTILINEAR)))) {
        final Function1<XControlPoint, Boolean> _function_1 = (XControlPoint it) -> {
          XControlPoint.Type _type = it.getType();
          return Boolean.valueOf(Objects.equal(_type, XControlPoint.Type.DANGLING));
        };
        boolean _exists = IterableExtensions.<XControlPoint>exists(this.getSiblings(), _function_1);
        if (_exists) {
          CommandStack _commandStack = CoreExtensions.getRoot(this.getHost()).getCommandStack();
          RemoveDanglingControlPointsCommand _removeDanglingControlPointsCommand = new RemoveDanglingControlPointsCommand(connection);
          _commandStack.execute(_removeDanglingControlPointsCommand);
        }
      }
    };
    this.getHost().selectedProperty().addListener(_function);
  }
  
  @Override
  protected void dragTo(final Point2D newPositionInDiagram) {
    boolean _notEquals = (!Objects.equal(newPositionInDiagram, null));
    if (_notEquals) {
      double _x = newPositionInDiagram.getX();
      double _layoutX = this.getHost().getLayoutX();
      final double moveDeltaX = (_x - _layoutX);
      double _y = newPositionInDiagram.getY();
      double _layoutY = this.getHost().getLayoutY();
      final double moveDeltaY = (_y - _layoutY);
      super.dragTo(newPositionInDiagram);
      final ObservableList<XControlPoint> siblings = this.getSiblings();
      final int index = siblings.indexOf(this.getHost());
      XControlPoint.Type _type = this.getHost().getType();
      if (_type != null) {
        switch (_type) {
          case INTERPOLATED:
            this.adjustBoth(index, siblings, moveDeltaX, moveDeltaY);
            this.updateDangling(index, siblings);
            break;
          case DANGLING:
            this.updateDangling(index, siblings);
            break;
          case CONTROL_POINT:
            this.adjustLeft(index, siblings, moveDeltaX, moveDeltaY);
            this.adjustRight(index, siblings, moveDeltaX, moveDeltaY);
            break;
          default:
            break;
        }
      }
    }
  }
  
  protected void adjustBoth(final int index, final ObservableList<XControlPoint> siblings, final double moveDeltaX, final double moveDeltaY) {
    if (((index > 0) && (index < (siblings.size() - 1)))) {
      final XControlPoint predecessor = siblings.get((index - 1));
      final XControlPoint successor = siblings.get((index + 1));
      if ((Objects.equal(predecessor.getType(), XControlPoint.Type.CONTROL_POINT) && Objects.equal(successor.getType(), XControlPoint.Type.CONTROL_POINT))) {
        double _layoutX = successor.getLayoutX();
        double _layoutX_1 = predecessor.getLayoutX();
        final double dx0 = (_layoutX - _layoutX_1);
        double _layoutY = successor.getLayoutY();
        double _layoutY_1 = predecessor.getLayoutY();
        final double dy0 = (_layoutY - _layoutY_1);
        double _layoutX_2 = this.getHost().getLayoutX();
        double _layoutX_3 = predecessor.getLayoutX();
        final double dx1 = (_layoutX_2 - _layoutX_3);
        double _layoutY_2 = this.getHost().getLayoutY();
        double _layoutY_3 = predecessor.getLayoutY();
        final double dy1 = (_layoutY_2 - _layoutY_3);
        double _norm = Point2DExtensions.norm(dx0, dy0);
        double _norm_1 = Point2DExtensions.norm(dx1, dy1);
        final double normProd = (_norm * _norm_1);
        if ((normProd > (NumberExpressionExtensions.EPSILON * NumberExpressionExtensions.EPSILON))) {
          final double scalarProd = ((0.5 * ((dx0 * dx1) + (dy0 * dy1))) / normProd);
          final double orthoX = (dx1 - (scalarProd * dx0));
          final double orthoY = (dy1 - (scalarProd * dy0));
          double _norm_2 = Point2DExtensions.norm(orthoX, orthoY);
          boolean _greaterThan = (_norm_2 > NumberExpressionExtensions.EPSILON);
          if (_greaterThan) {
            boolean _selected = predecessor.getSelected();
            boolean _not = (!_selected);
            if (_not) {
              double _layoutX_4 = predecessor.getLayoutX();
              double _plus = (_layoutX_4 + orthoX);
              predecessor.setLayoutX(_plus);
              double _layoutY_4 = predecessor.getLayoutY();
              double _plus_1 = (_layoutY_4 + orthoY);
              predecessor.setLayoutY(_plus_1);
              this.adjustLeft((index - 1), siblings, moveDeltaX, moveDeltaY);
            }
            boolean _selected_1 = successor.getSelected();
            boolean _not_1 = (!_selected_1);
            if (_not_1) {
              double _layoutX_5 = successor.getLayoutX();
              double _plus_2 = (_layoutX_5 + orthoX);
              successor.setLayoutX(_plus_2);
              double _layoutY_5 = successor.getLayoutY();
              double _plus_3 = (_layoutY_5 + orthoY);
              successor.setLayoutY(_plus_3);
              this.adjustRight((index + 1), siblings, moveDeltaX, moveDeltaY);
            }
          }
        }
      } else {
        XConnection.Kind _kind = this.getConnection().getKind();
        boolean _equals = Objects.equal(_kind, XConnection.Kind.RECTILINEAR);
        if (_equals) {
          this.keepRectilinear(index, siblings);
          this.updateDangling(index, siblings);
          if ((index > 1)) {
            this.updateDangling((index - 1), siblings);
          }
          int _size = siblings.size();
          int _minus = (_size - 2);
          boolean _lessThan = (index < _minus);
          if (_lessThan) {
            this.updateDangling((index + 1), siblings);
          }
        }
      }
    }
  }
  
  protected void keepRectilinear(final int index, final List<XControlPoint> siblings) {
    Side _side = IterableExtensions.<XControlPoint>head(siblings).getSide();
    boolean _notEquals = (!Objects.equal(_side, null));
    if (_notEquals) {
      boolean _xifexpression = false;
      if (((index % 2) == 0)) {
        _xifexpression = IterableExtensions.<XControlPoint>head(siblings).getSide().isVertical();
      } else {
        boolean _isVertical = IterableExtensions.<XControlPoint>head(siblings).getSide().isVertical();
        _xifexpression = (!_isVertical);
      }
      final boolean incomingVertical = _xifexpression;
      final XControlPoint predecessor = siblings.get((index - 1));
      final XControlPoint current = siblings.get(index);
      final XControlPoint successor = siblings.get((index + 1));
      if (incomingVertical) {
        predecessor.setLayoutX(current.getLayoutX());
        successor.setLayoutY(current.getLayoutY());
      } else {
        predecessor.setLayoutY(current.getLayoutY());
        successor.setLayoutX(current.getLayoutX());
      }
    }
  }
  
  protected void adjustLeft(final int index, final List<XControlPoint> siblings, final double moveDeltaX, final double moveDeltaY) {
    if ((index > 1)) {
      final XControlPoint current = siblings.get(index);
      final XControlPoint predecessor = siblings.get((index - 1));
      final XControlPoint prepredecessor = siblings.get((index - 2));
      if ((Objects.equal(predecessor.getType(), XControlPoint.Type.INTERPOLATED) && Objects.equal(prepredecessor.getType(), XControlPoint.Type.CONTROL_POINT))) {
        boolean _selected = prepredecessor.getSelected();
        boolean _not = (!_selected);
        if (_not) {
          double _layoutX = predecessor.getLayoutX();
          double _layoutX_1 = current.getLayoutX();
          final double dx0 = (_layoutX - _layoutX_1);
          double _layoutY = predecessor.getLayoutY();
          double _layoutY_1 = current.getLayoutY();
          final double dy0 = (_layoutY - _layoutY_1);
          final double norm0 = Point2DExtensions.norm(dx0, dy0);
          if ((norm0 > NumberExpressionExtensions.EPSILON)) {
            double _layoutX_2 = prepredecessor.getLayoutX();
            double _layoutX_3 = predecessor.getLayoutX();
            final double dx1 = (_layoutX_2 - _layoutX_3);
            double _layoutY_2 = prepredecessor.getLayoutY();
            double _layoutY_3 = predecessor.getLayoutY();
            final double dy1 = (_layoutY_2 - _layoutY_3);
            double _norm = Point2DExtensions.norm(dx1, dy1);
            final double scale = (_norm / norm0);
            double _layoutX_4 = predecessor.getLayoutX();
            double _plus = (_layoutX_4 + (scale * dx0));
            prepredecessor.setLayoutX(_plus);
            double _layoutY_4 = predecessor.getLayoutY();
            double _plus_1 = (_layoutY_4 + (scale * dy0));
            prepredecessor.setLayoutY(_plus_1);
          }
          this.adjustLeft((index - 2), siblings, moveDeltaX, moveDeltaY);
        } else {
          boolean _selected_1 = predecessor.getSelected();
          boolean _not_1 = (!_selected_1);
          if (_not_1) {
            double _layoutX_5 = predecessor.getLayoutX();
            double _plus_2 = (_layoutX_5 + (0.5 * moveDeltaX));
            predecessor.setLayoutX(_plus_2);
            double _layoutY_5 = predecessor.getLayoutY();
            double _plus_3 = (_layoutY_5 + (0.5 * moveDeltaY));
            predecessor.setLayoutY(_plus_3);
          }
        }
      }
    }
  }
  
  protected void adjustRight(final int index, final List<XControlPoint> siblings, final double moveDeltaX, final double moveDeltaY) {
    int _size = siblings.size();
    int _minus = (_size - 2);
    boolean _lessThan = (index < _minus);
    if (_lessThan) {
      final XControlPoint current = siblings.get(index);
      final XControlPoint successor = siblings.get((index + 1));
      final XControlPoint postsuccessor = siblings.get((index + 2));
      if ((Objects.equal(successor.getType(), XControlPoint.Type.INTERPOLATED) && Objects.equal(postsuccessor.getType(), XControlPoint.Type.CONTROL_POINT))) {
        boolean _selected = postsuccessor.getSelected();
        boolean _not = (!_selected);
        if (_not) {
          double _layoutX = successor.getLayoutX();
          double _layoutX_1 = current.getLayoutX();
          final double dx0 = (_layoutX - _layoutX_1);
          double _layoutY = successor.getLayoutY();
          double _layoutY_1 = current.getLayoutY();
          final double dy0 = (_layoutY - _layoutY_1);
          final double norm0 = Point2DExtensions.norm(dx0, dy0);
          if ((norm0 > NumberExpressionExtensions.EPSILON)) {
            double _layoutX_2 = postsuccessor.getLayoutX();
            double _layoutX_3 = successor.getLayoutX();
            final double dx1 = (_layoutX_2 - _layoutX_3);
            double _layoutY_2 = postsuccessor.getLayoutY();
            double _layoutY_3 = successor.getLayoutY();
            final double dy1 = (_layoutY_2 - _layoutY_3);
            double _norm = Point2DExtensions.norm(dx1, dy1);
            final double scale = (_norm / norm0);
            double _layoutX_4 = successor.getLayoutX();
            double _plus = (_layoutX_4 + (scale * dx0));
            postsuccessor.setLayoutX(_plus);
            double _layoutY_4 = successor.getLayoutY();
            double _plus_1 = (_layoutY_4 + (scale * dy0));
            postsuccessor.setLayoutY(_plus_1);
          }
          this.adjustRight((index + 2), siblings, moveDeltaX, moveDeltaY);
        } else {
          boolean _selected_1 = successor.getSelected();
          boolean _not_1 = (!_selected_1);
          if (_not_1) {
            double _layoutX_5 = successor.getLayoutX();
            double _plus_2 = (_layoutX_5 + (0.5 * moveDeltaX));
            successor.setLayoutX(_plus_2);
            double _layoutY_5 = successor.getLayoutY();
            double _plus_3 = (_layoutY_5 + (0.5 * moveDeltaY));
            successor.setLayoutY(_plus_3);
          }
        }
      }
    }
  }
  
  protected void updateDangling(final int index, final List<XControlPoint> siblings) {
    if ((Objects.equal(this.getConnection().getKind(), XConnection.Kind.POLYLINE) || Objects.equal(this.getConnection().getKind(), XConnection.Kind.RECTILINEAR))) {
      final XControlPoint predecessor = siblings.get((index - 1));
      final XControlPoint candidate = siblings.get(index);
      final XControlPoint successor = siblings.get((index + 1));
      boolean _areOnSameLine = Point2DExtensions.areOnSameLine(predecessor.getLayoutX(), predecessor.getLayoutY(), candidate.getLayoutX(), candidate.getLayoutY(), successor.getLayoutX(), 
        successor.getLayoutY());
      if (_areOnSameLine) {
        candidate.setType(XControlPoint.Type.DANGLING);
      } else {
        candidate.setType(XControlPoint.Type.INTERPOLATED);
      }
    }
  }
  
  protected ObservableList<XControlPoint> getSiblings() {
    XConnection _connection = this.getConnection();
    ObservableList<XControlPoint> _controlPoints = null;
    if (_connection!=null) {
      _controlPoints=_connection.getControlPoints();
    }
    return _controlPoints;
  }
  
  protected XConnection getConnection() {
    Object _xblockexpression = null;
    {
      Parent _parent = this.getHost().getParent();
      XShape _containerShape = null;
      if (_parent!=null) {
        _containerShape=CoreExtensions.getContainerShape(_parent);
      }
      final XShape containerShape = _containerShape;
      Object _xifexpression = null;
      if ((containerShape instanceof XConnection)) {
        return ((XConnection)containerShape);
      } else {
        _xifexpression = null;
      }
      _xblockexpression = _xifexpression;
    }
    return ((XConnection)_xblockexpression);
  }
  
  private ConnectionMemento memento;
  
  @Override
  public void startDrag(final double screenX, final double screenY) {
    super.startDrag(screenX, screenY);
    XConnection _connection = this.getConnection();
    ConnectionMemento _connectionMemento = new ConnectionMemento(_connection);
    this.memento = _connectionMemento;
  }
  
  @Override
  public AnimationCommand createMoveCommand() {
    ConnectionMemento.MorphCommand _xblockexpression = null;
    {
      for (int i = 1; (i < (this.getConnection().getControlPoints().size() - 1)); i++) {
        {
          final XControlPoint cp = this.getConnection().getControlPoints().get(i);
          Point2D _point2D = ConnectionExtensions.toPoint2D(cp);
          int _size = this.memento.getControlPoints().size();
          int _minus = (_size - 1);
          Point2D _point2D_1 = ConnectionExtensions.toPoint2D(this.memento.getControlPoints().get(Math.min(i, _minus)));
          boolean _notEquals = (!Objects.equal(_point2D, _point2D_1));
          if (_notEquals) {
            cp.setManuallyPlaced(true);
          }
        }
      }
      _xblockexpression = this.memento.createChangeCommand();
    }
    return _xblockexpression;
  }
}
