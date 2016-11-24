package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XRoot;
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class ControlPointMoveBehavior extends MoveBehavior<XControlPoint> {
  private double lastMouseX;
  
  private double lastMouseY;
  
  public ControlPointMoveBehavior(final XControlPoint host) {
    super(host);
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    XControlPoint _host = this.getHost();
    BooleanProperty _selectedProperty = _host.selectedProperty();
    final ChangeListener<Boolean> _function = (ObservableValue<? extends Boolean> p, Boolean o, Boolean newValue) -> {
      final XConnection connection = this.getConnection();
      if ((((!(newValue).booleanValue()) && (!Objects.equal(connection, null))) && (Objects.equal(connection.getKind(), XConnection.Kind.POLYLINE) || Objects.equal(connection.getKind(), XConnection.Kind.RECTILINEAR)))) {
        ObservableList<XControlPoint> _siblings = this.getSiblings();
        final Function1<XControlPoint, Boolean> _function_1 = (XControlPoint it) -> {
          XControlPoint.Type _type = it.getType();
          return Boolean.valueOf(Objects.equal(_type, XControlPoint.Type.DANGLING));
        };
        boolean _exists = IterableExtensions.<XControlPoint>exists(_siblings, _function_1);
        if (_exists) {
          XControlPoint _host_1 = this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host_1);
          CommandStack _commandStack = _root.getCommandStack();
          RemoveDanglingControlPointsCommand _removeDanglingControlPointsCommand = new RemoveDanglingControlPointsCommand(connection);
          _commandStack.execute(_removeDanglingControlPointsCommand);
        }
      }
    };
    _selectedProperty.addListener(_function);
  }
  
  @Override
  public void mouseDragged(final MouseEvent it) {
    super.mouseDragged(it);
    double _sceneX = it.getSceneX();
    this.lastMouseX = _sceneX;
    double _sceneY = it.getSceneY();
    this.lastMouseY = _sceneY;
  }
  
  @Override
  protected void dragTo(final Point2D newPositionInDiagram) {
    boolean _notEquals = (!Objects.equal(newPositionInDiagram, null));
    if (_notEquals) {
      double _x = newPositionInDiagram.getX();
      XControlPoint _host = this.getHost();
      double _layoutX = _host.getLayoutX();
      final double moveDeltaX = (_x - _layoutX);
      double _y = newPositionInDiagram.getY();
      XControlPoint _host_1 = this.getHost();
      double _layoutY = _host_1.getLayoutY();
      final double moveDeltaY = (_y - _layoutY);
      super.dragTo(newPositionInDiagram);
      final ObservableList<XControlPoint> siblings = this.getSiblings();
      XControlPoint _host_2 = this.getHost();
      final int index = siblings.indexOf(_host_2);
      XControlPoint _host_3 = this.getHost();
      XControlPoint.Type _type = _host_3.getType();
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
        XControlPoint _host = this.getHost();
        double _layoutX_2 = _host.getLayoutX();
        double _layoutX_3 = predecessor.getLayoutX();
        final double dx1 = (_layoutX_2 - _layoutX_3);
        XControlPoint _host_1 = this.getHost();
        double _layoutY_2 = _host_1.getLayoutY();
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
        XConnection _connection = this.getConnection();
        XConnection.Kind _kind = _connection.getKind();
        boolean _equals = Objects.equal(_kind, XConnection.Kind.RECTILINEAR);
        if (_equals) {
          XControlPoint _get = siblings.get(index);
          this.keepRectilinear(_get, predecessor, moveDeltaX, moveDeltaY);
          XControlPoint _get_1 = siblings.get(index);
          this.keepRectilinear(_get_1, successor, moveDeltaX, moveDeltaY);
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
  
  protected void keepRectilinear(final XControlPoint moved, final XControlPoint dependent, final double moveDeltaX, final double moveDeltaY) {
    double _layoutX = dependent.getLayoutX();
    double _layoutX_1 = moved.getLayoutX();
    double _minus = (_layoutX_1 - moveDeltaX);
    double _minus_1 = (_layoutX - _minus);
    double _abs = Math.abs(_minus_1);
    double _layoutY = dependent.getLayoutY();
    double _layoutY_1 = moved.getLayoutY();
    double _minus_2 = (_layoutY_1 - moveDeltaY);
    double _minus_3 = (_layoutY - _minus_2);
    double _abs_1 = Math.abs(_minus_3);
    boolean _lessThan = (_abs < _abs_1);
    if (_lessThan) {
      double _layoutX_2 = moved.getLayoutX();
      dependent.setLayoutX(_layoutX_2);
    } else {
      double _layoutY_2 = moved.getLayoutY();
      dependent.setLayoutY(_layoutY_2);
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
      double _layoutX = predecessor.getLayoutX();
      double _layoutY = predecessor.getLayoutY();
      double _layoutX_1 = candidate.getLayoutX();
      double _layoutY_1 = candidate.getLayoutY();
      double _layoutX_2 = successor.getLayoutX();
      double _layoutY_2 = successor.getLayoutY();
      boolean _areOnSameLine = Point2DExtensions.areOnSameLine(_layoutX, _layoutY, _layoutX_1, _layoutY_1, _layoutX_2, _layoutY_2);
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
      XControlPoint _host = this.getHost();
      Parent _parent = _host.getParent();
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
          XConnection _connection = this.getConnection();
          ObservableList<XControlPoint> _controlPoints = _connection.getControlPoints();
          final XControlPoint cp = _controlPoints.get(i);
          Point2D _point2D = ConnectionExtensions.toPoint2D(cp);
          List<XControlPoint> _controlPoints_1 = this.memento.getControlPoints();
          List<XControlPoint> _controlPoints_2 = this.memento.getControlPoints();
          int _size = _controlPoints_2.size();
          int _minus = (_size - 1);
          int _min = Math.min(i, _minus);
          XControlPoint _get = _controlPoints_1.get(_min);
          Point2D _point2D_1 = ConnectionExtensions.toPoint2D(_get);
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
