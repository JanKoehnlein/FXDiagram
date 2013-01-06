package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.Extensions;
import de.itemis.javafx.diagram.XAbstractDiagram;
import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.AbstractBehavior;
import de.itemis.javafx.diagram.behavior.DragContext;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MoveBehavior extends AbstractBehavior {
  private DragContext dragContext;
  
  public MoveBehavior(final XNode host) {
    super(host);
  }
  
  public void doActivate() {
    XNode _host = this.getHost();
    Node _node = _host.getNode();
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          MoveBehavior.this.mousePressed(it);
        }
      };
    _node.setOnMousePressed(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function.apply(arg0);
        }
    });
    XNode _host_1 = this.getHost();
    Node _node_1 = _host_1.getNode();
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          MoveBehavior.this.mouseDragged(it);
        }
      };
    _node_1.setOnMouseDragged(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
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
