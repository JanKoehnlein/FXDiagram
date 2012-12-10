package de.itemis.javafx.diagram.behavior;

import de.itemis.javafx.diagram.Diagram;
import de.itemis.javafx.diagram.ShapeContainer;
import de.itemis.javafx.diagram.behavior.AbstractBehavior;
import de.itemis.javafx.diagram.behavior.DragContext;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class SelectionBehavior extends AbstractBehavior {
  private BooleanProperty isSelected = new Function0<BooleanProperty>() {
    public BooleanProperty apply() {
      SimpleBooleanProperty _simpleBooleanProperty = new SimpleBooleanProperty();
      return _simpleBooleanProperty;
    }
  }.apply();
  
  private DragContext dragContext;
  
  public SelectionBehavior(final ShapeContainer host) {
    super(host);
  }
  
  public void activate(final Diagram diagram) {
    ShapeContainer _host = this.getHost();
    Node _node = _host.getNode();
    final Procedure1<MouseEvent> _function = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          SelectionBehavior.this.mousePressed(it);
          SelectionBehavior.this.isSelected.set(true);
        }
      };
    _node.setOnMousePressed(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function.apply(arg0);
        }
    });
    ShapeContainer _host_1 = this.getHost();
    Node _node_1 = _host_1.getNode();
    final Procedure1<MouseEvent> _function_1 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          boolean _and = false;
          boolean _and_1 = false;
          double _mouseAnchorX = SelectionBehavior.this.dragContext.getMouseAnchorX();
          double _screenX = it.getScreenX();
          boolean _equals = (_mouseAnchorX == _screenX);
          if (!_equals) {
            _and_1 = false;
          } else {
            double _mouseAnchorY = SelectionBehavior.this.dragContext.getMouseAnchorY();
            double _screenY = it.getScreenY();
            boolean _equals_1 = (_mouseAnchorY == _screenY);
            _and_1 = (_equals && _equals_1);
          }
          if (!_and_1) {
            _and = false;
          } else {
            boolean _isShortcutDown = it.isShortcutDown();
            _and = (_and_1 && _isShortcutDown);
          }
          if (_and) {
            boolean _isWasSeleceted = SelectionBehavior.this.dragContext.isWasSeleceted();
            boolean _not = (!_isWasSeleceted);
            SelectionBehavior.this.isSelected.set(_not);
          }
        }
      };
    _node_1.setOnMouseReleased(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_1.apply(arg0);
        }
    });
    ShapeContainer _host_2 = this.getHost();
    Node _node_2 = _host_2.getNode();
    final Procedure1<MouseEvent> _function_2 = new Procedure1<MouseEvent>() {
        public void apply(final MouseEvent it) {
          SelectionBehavior.this.mouseDragged(it);
        }
      };
    _node_2.setOnMouseDragged(new EventHandler<MouseEvent>() {
        public void handle(MouseEvent arg0) {
          _function_2.apply(arg0);
        }
    });
  }
  
  public DragContext mousePressed(final MouseEvent it) {
    double _screenX = it.getScreenX();
    double _screenY = it.getScreenY();
    ShapeContainer _host = this.getHost();
    double _translateX = _host.getTranslateX();
    ShapeContainer _host_1 = this.getHost();
    double _translateY = _host_1.getTranslateY();
    boolean _get = this.isSelected.get();
    DragContext _dragContext = new DragContext(_screenX, _screenY, _translateX, _translateY, _get);
    DragContext _dragContext_1 = this.dragContext = _dragContext;
    return _dragContext_1;
  }
  
  public void mouseDragged(final MouseEvent it) {
    ShapeContainer _host = this.getHost();
    double _initialX = this.dragContext.getInitialX();
    double _mouseAnchorX = this.dragContext.getMouseAnchorX();
    double _minus = (_initialX - _mouseAnchorX);
    double _screenX = it.getScreenX();
    double _plus = (_minus + _screenX);
    _host.setTranslateX(_plus);
    ShapeContainer _host_1 = this.getHost();
    double _initialY = this.dragContext.getInitialY();
    double _mouseAnchorY = this.dragContext.getMouseAnchorY();
    double _minus_1 = (_initialY - _mouseAnchorY);
    double _screenY = it.getScreenY();
    double _plus_1 = (_minus_1 + _screenY);
    _host_1.setTranslateY(_plus_1);
  }
  
  public BooleanProperty getSelectedProperty() {
    return this.isSelected;
  }
}
