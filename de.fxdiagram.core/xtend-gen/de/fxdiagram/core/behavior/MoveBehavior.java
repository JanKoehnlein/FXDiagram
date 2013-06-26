package de.fxdiagram.core.behavior;

import de.fxdiagram.core.Extensions;
import de.fxdiagram.core.XAbstractDiagram;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.AbstractBehavior;
import de.fxdiagram.core.behavior.DragContext;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("all")
public class MoveBehavior extends AbstractBehavior {
  private DragContext dragContext;
  
  public MoveBehavior(final XNode host) {
    super(host);
  }
  
  public void doActivate() {
    XNode _host = this.getHost();
    Node _node = _host.getNode();
    final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          MoveBehavior.this.mousePressed(it);
        }
      };
    _node.setOnMousePressed(_function);
    XNode _host_1 = this.getHost();
    Node _node_1 = _host_1.getNode();
    final EventHandler<MouseEvent> _function_1 = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent it) {
          MoveBehavior.this.mouseDragged(it);
        }
      };
    _node_1.setOnMouseDragged(_function_1);
  }
  
  public DragContext mousePressed(final MouseEvent it) {
    double _screenX = it.getScreenX();
    double _screenY = it.getScreenY();
    XNode _host = this.getHost();
    XAbstractDiagram _diagram = Extensions.getDiagram(_host);
    XNode _host_1 = this.getHost();
    double _layoutX = _host_1.getLayoutX();
    XNode _host_2 = this.getHost();
    double _layoutY = _host_2.getLayoutY();
    Point2D _localToScene = _diagram.localToScene(_layoutX, _layoutY);
    DragContext _dragContext = new DragContext(_screenX, _screenY, _localToScene);
    DragContext _dragContext_1 = this.dragContext = _dragContext;
    return _dragContext_1;
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
    Point2D _point2D = new Point2D(_minus, _minus_1);
    final Point2D newPositionInScene = _point2D;
    XNode _host = this.getHost();
    XAbstractDiagram _diagram = Extensions.getDiagram(_host);
    final Point2D newPositionInDiagram = _diagram.sceneToLocal(newPositionInScene);
    XNode _host_1 = this.getHost();
    double _x_1 = newPositionInDiagram.getX();
    double _y_1 = newPositionInDiagram.getY();
    _host_1.relocate(_x_1, _y_1);
  }
}
