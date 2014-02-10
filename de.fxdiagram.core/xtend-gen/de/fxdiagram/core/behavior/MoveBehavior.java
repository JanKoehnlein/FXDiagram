package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.behavior.DragContext;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("all")
public class MoveBehavior<T extends XShape> extends AbstractHostBehavior<T> {
  private DragContext dragContext;
  
  public MoveBehavior(final T host) {
    super(host);
  }
  
  public void doActivate() {
    T _host = this.getHost();
    Node _node = _host.getNode();
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        MoveBehavior.this.mousePressed(it);
      }
    };
    _node.setOnMousePressed(_function);
    T _host_1 = this.getHost();
    Node _node_1 = _host_1.getNode();
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        MoveBehavior.this.mouseDragged(it);
      }
    };
    _node_1.setOnMouseDragged(_function_1);
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return MoveBehavior.class;
  }
  
  public DragContext mousePressed(final MouseEvent it) {
    DragContext _xblockexpression = null;
    {
      T _host = this.getHost();
      Parent _parent = _host.getParent();
      T _host_1 = this.getHost();
      double _layoutX = _host_1.getLayoutX();
      T _host_2 = this.getHost();
      double _layoutY = _host_2.getLayoutY();
      final Point2D initialPositionInScene = _parent.localToScene(_layoutX, _layoutY);
      double _screenX = it.getScreenX();
      double _screenY = it.getScreenY();
      DragContext _dragContext = new DragContext(_screenX, _screenY, initialPositionInScene);
      _xblockexpression = this.dragContext = _dragContext;
    }
    return _xblockexpression;
  }
  
  public void mouseDragged(final MouseEvent it) {
    Point2D _initialPosInScene = this.dragContext.getInitialPosInScene();
    double _x = _initialPosInScene.getX();
    double _screenX = it.getScreenX();
    double _plus = (_x + _screenX);
    double _mouseAnchorX = this.dragContext.getMouseAnchorX();
    double _minus = (_plus - _mouseAnchorX);
    Point2D _initialPosInScene_1 = this.dragContext.getInitialPosInScene();
    double _y = _initialPosInScene_1.getY();
    double _screenY = it.getScreenY();
    double _plus_1 = (_y + _screenY);
    double _mouseAnchorY = this.dragContext.getMouseAnchorY();
    double _minus_1 = (_plus_1 - _mouseAnchorY);
    final Point2D newPositionInScene = new Point2D(_minus, _minus_1);
    T _host = this.getHost();
    Parent _parent = _host.getParent();
    final Point2D newPositionInDiagram = _parent.sceneToLocal(newPositionInScene);
    boolean _notEquals = (!Objects.equal(newPositionInDiagram, null));
    if (_notEquals) {
      T _host_1 = this.getHost();
      double _x_1 = newPositionInDiagram.getX();
      _host_1.setLayoutX(_x_1);
      T _host_2 = this.getHost();
      double _y_1 = newPositionInDiagram.getY();
      _host_2.setLayoutY(_y_1);
    }
  }
}
