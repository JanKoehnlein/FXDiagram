package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.XNode;
import de.itemis.javafx.diagram.behavior.AbstractBehavior;
import de.itemis.javafx.diagram.behavior.DragContext;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MoveBehavior extends AbstractBehavior {
  private DragContext dragContext;
  
  public MoveBehavior(final XNode host) {
    super(host);
  }
  
  public void activate() {
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
    double _translateX = _host.getTranslateX();
    XNode _host_1 = this.getHost();
    double _translateY = _host_1.getTranslateY();
    DragContext _dragContext = new DragContext(_screenX, _screenY, _translateX, _translateY);
    DragContext _dragContext_1 = this.dragContext = _dragContext;
    return _dragContext_1;
  }
  
  public void mouseDragged(final MouseEvent it) {
    XNode _host = this.getHost();
    double _initialX = this.dragContext.getInitialX();
    double _mouseAnchorX = this.dragContext.getMouseAnchorX();
    double _minus = (_initialX - _mouseAnchorX);
    double _screenX = it.getScreenX();
    double _plus = (_minus + _screenX);
    _host.setTranslateX(_plus);
    XNode _host_1 = this.getHost();
    double _initialY = this.dragContext.getInitialY();
    double _mouseAnchorY = this.dragContext.getMouseAnchorY();
    double _minus_1 = (_initialY - _mouseAnchorY);
    double _screenY = it.getScreenY();
    double _plus_1 = (_minus_1 + _screenY);
    _host_1.setTranslateY(_plus_1);
  }
}
