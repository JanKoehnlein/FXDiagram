package de.fxdiagram.core.behavior;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.MoveCommand;
import de.fxdiagram.core.command.ParallelAnimationCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("all")
public class ControlPointMoveBehavior extends MoveBehavior<XControlPoint> {
  public ControlPointMoveBehavior(final XControlPoint host) {
    super(host);
  }
  
  @Override
  public void activate() {
    super.activate();
    XControlPoint _host = this.getHost();
    BooleanProperty _selectedProperty = _host.selectedProperty();
    final ChangeListener<Boolean> _function = (ObservableValue<? extends Boolean> p, Boolean o, Boolean newValue) -> {
      final XConnection connection = this.getConnection();
      boolean _and = false;
      boolean _and_1 = false;
      if (!(!(newValue).booleanValue())) {
        _and_1 = false;
      } else {
        boolean _notEquals = (!Objects.equal(connection, null));
        _and_1 = _notEquals;
      }
      if (!_and_1) {
        _and = false;
      } else {
        XConnection.Kind _kind = connection.getKind();
        boolean _equals = Objects.equal(_kind, XConnection.Kind.POLYLINE);
        _and = _equals;
      }
      if (_and) {
        final ObservableList<XControlPoint> siblings = this.getSiblings();
        XControlPoint _host_1 = this.getHost();
        final int index = siblings.indexOf(_host_1);
        boolean _and_2 = false;
        if (!(index > 0)) {
          _and_2 = false;
        } else {
          int _size = siblings.size();
          int _minus = (_size - 1);
          boolean _lessThan = (index < _minus);
          _and_2 = _lessThan;
        }
        if (_and_2) {
          final XControlPoint predecessor = siblings.get((index - 1));
          final XControlPoint successor = siblings.get((index + 1));
          double _layoutX = predecessor.getLayoutX();
          double _layoutY = predecessor.getLayoutY();
          XControlPoint _host_2 = this.getHost();
          double _layoutX_1 = _host_2.getLayoutX();
          XControlPoint _host_3 = this.getHost();
          double _layoutY_1 = _host_3.getLayoutY();
          double _layoutX_2 = successor.getLayoutX();
          double _layoutY_2 = successor.getLayoutY();
          boolean _areOnSameLine = Point2DExtensions.areOnSameLine(_layoutX, _layoutY, _layoutX_1, _layoutY_1, _layoutX_2, _layoutY_2);
          if (_areOnSameLine) {
            XControlPoint _host_4 = this.getHost();
            XRoot _root = CoreExtensions.getRoot(_host_4);
            CommandStack _commandStack = _root.getCommandStack();
            _commandStack.execute(new AbstractCommand() {
              @Override
              public void execute(final CommandContext context) {
                siblings.remove(index);
              }
              
              @Override
              public void undo(final CommandContext context) {
                XControlPoint _host = ControlPointMoveBehavior.this.getHost();
                siblings.add(index, _host);
              }
              
              @Override
              public void redo(final CommandContext context) {
                siblings.remove(index);
              }
            });
          }
        }
      }
    };
    _selectedProperty.addListener(_function);
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
    boolean _and = false;
    if (!(index > 0)) {
      _and = false;
    } else {
      int _size = siblings.size();
      int _minus = (_size - 1);
      boolean _lessThan = (index < _minus);
      _and = _lessThan;
    }
    if (_and) {
      final XControlPoint predecessor = siblings.get((index - 1));
      final XControlPoint successor = siblings.get((index + 1));
      boolean _and_1 = false;
      XControlPoint.Type _type = predecessor.getType();
      boolean _equals = Objects.equal(_type, XControlPoint.Type.CONTROL_POINT);
      if (!_equals) {
        _and_1 = false;
      } else {
        XControlPoint.Type _type_1 = successor.getType();
        boolean _equals_1 = Objects.equal(_type_1, XControlPoint.Type.CONTROL_POINT);
        _and_1 = _equals_1;
      }
      if (_and_1) {
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
      }
    }
  }
  
  protected void adjustLeft(final int index, final List<XControlPoint> siblings, final double moveDeltaX, final double moveDeltaY) {
    if ((index > 1)) {
      final XControlPoint current = siblings.get(index);
      final XControlPoint predecessor = siblings.get((index - 1));
      final XControlPoint prepredecessor = siblings.get((index - 2));
      boolean _and = false;
      XControlPoint.Type _type = predecessor.getType();
      boolean _equals = Objects.equal(_type, XControlPoint.Type.INTERPOLATED);
      if (!_equals) {
        _and = false;
      } else {
        XControlPoint.Type _type_1 = prepredecessor.getType();
        boolean _equals_1 = Objects.equal(_type_1, XControlPoint.Type.CONTROL_POINT);
        _and = _equals_1;
      }
      if (_and) {
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
      boolean _and = false;
      XControlPoint.Type _type = successor.getType();
      boolean _equals = Objects.equal(_type, XControlPoint.Type.INTERPOLATED);
      if (!_equals) {
        _and = false;
      } else {
        XControlPoint.Type _type_1 = postsuccessor.getType();
        boolean _equals_1 = Objects.equal(_type_1, XControlPoint.Type.CONTROL_POINT);
        _and = _equals_1;
      }
      if (_and) {
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
    final XControlPoint predecessor = siblings.get((index - 1));
    final XControlPoint successor = siblings.get((index + 1));
    double _layoutX = predecessor.getLayoutX();
    double _layoutY = predecessor.getLayoutY();
    XControlPoint _host = this.getHost();
    double _layoutX_1 = _host.getLayoutX();
    XControlPoint _host_1 = this.getHost();
    double _layoutY_1 = _host_1.getLayoutY();
    double _layoutX_2 = successor.getLayoutX();
    double _layoutY_2 = successor.getLayoutY();
    boolean _areOnSameLine = Point2DExtensions.areOnSameLine(_layoutX, _layoutY, _layoutX_1, _layoutY_1, _layoutX_2, _layoutY_2);
    if (_areOnSameLine) {
      XControlPoint _host_2 = this.getHost();
      _host_2.setType(XControlPoint.Type.DANGLING);
    } else {
      XControlPoint _host_3 = this.getHost();
      _host_3.setType(XControlPoint.Type.INTERPOLATED);
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
  
  private Map<XControlPoint, Point2D> initialPositions;
  
  @Override
  public void mousePressed(final MouseEvent it) {
    super.mousePressed(it);
    ObservableList<XControlPoint> _siblings = this.getSiblings();
    final Function<XControlPoint, Point2D> _function = (XControlPoint it_1) -> {
      double _layoutX = it_1.getLayoutX();
      double _layoutY = it_1.getLayoutY();
      return new Point2D(_layoutX, _layoutY);
    };
    ImmutableMap<XControlPoint, Point2D> _map = Maps.<XControlPoint, Point2D>toMap(_siblings, _function);
    this.initialPositions = _map;
  }
  
  @Override
  protected AnimationCommand createMoveCommand() {
    ParallelAnimationCommand _xblockexpression = null;
    {
      final ParallelAnimationCommand pac = new ParallelAnimationCommand();
      Set<Map.Entry<XControlPoint, Point2D>> _entrySet = this.initialPositions.entrySet();
      final Consumer<Map.Entry<XControlPoint, Point2D>> _function = (Map.Entry<XControlPoint, Point2D> it) -> {
        boolean _or = false;
        XControlPoint _key = it.getKey();
        double _layoutX = _key.getLayoutX();
        Point2D _value = it.getValue();
        double _x = _value.getX();
        boolean _notEquals = (_layoutX != _x);
        if (_notEquals) {
          _or = true;
        } else {
          XControlPoint _key_1 = it.getKey();
          double _layoutY = _key_1.getLayoutY();
          Point2D _value_1 = it.getValue();
          double _y = _value_1.getY();
          boolean _notEquals_1 = (_layoutY != _y);
          _or = _notEquals_1;
        }
        if (_or) {
          XControlPoint _key_2 = it.getKey();
          Point2D _value_2 = it.getValue();
          double _x_1 = _value_2.getX();
          Point2D _value_3 = it.getValue();
          double _y_1 = _value_3.getY();
          XControlPoint _key_3 = it.getKey();
          double _layoutX_1 = _key_3.getLayoutX();
          XControlPoint _key_4 = it.getKey();
          double _layoutY_1 = _key_4.getLayoutY();
          MoveCommand _moveCommand = new MoveCommand(_key_2, _x_1, _y_1, _layoutX_1, _layoutY_1);
          pac.operator_add(_moveCommand);
        }
      };
      _entrySet.forEach(_function);
      _xblockexpression = pac;
    }
    return _xblockexpression;
  }
}
