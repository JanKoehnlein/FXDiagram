package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.AbstractHostBehavior;
import de.fxdiagram.core.behavior.Behavior;
import de.fxdiagram.core.command.CommandStack;
import de.fxdiagram.core.command.MoveCommand;
import de.fxdiagram.core.extensions.CoreExtensions;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

@SuppressWarnings("all")
public class MoveBehavior<T extends XShape> extends AbstractHostBehavior<T> {
  @Data
  public static class DragContext {
    private final double _initialX;
    
    public double getInitialX() {
      return this._initialX;
    }
    
    private final double _initialY;
    
    public double getInitialY() {
      return this._initialY;
    }
    
    private final double _mouseAnchorX;
    
    public double getMouseAnchorX() {
      return this._mouseAnchorX;
    }
    
    private final double _mouseAnchorY;
    
    public double getMouseAnchorY() {
      return this._mouseAnchorY;
    }
    
    private final Point2D _initialPosInScene;
    
    public Point2D getInitialPosInScene() {
      return this._initialPosInScene;
    }
    
    public DragContext(final double initialX, final double initialY, final double mouseAnchorX, final double mouseAnchorY, final Point2D initialPosInScene) {
      super();
      this._initialX = initialX;
      this._initialY = initialY;
      this._mouseAnchorX = mouseAnchorX;
      this._mouseAnchorY = mouseAnchorY;
      this._initialPosInScene = initialPosInScene;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (Double.doubleToLongBits(this._initialX) ^ (Double.doubleToLongBits(this._initialX) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this._initialY) ^ (Double.doubleToLongBits(this._initialY) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this._mouseAnchorX) ^ (Double.doubleToLongBits(this._mouseAnchorX) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this._mouseAnchorY) ^ (Double.doubleToLongBits(this._mouseAnchorY) >>> 32));
      result = prime * result + ((this._initialPosInScene== null) ? 0 : this._initialPosInScene.hashCode());
      return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      DragContext other = (DragContext) obj;
      if (Double.doubleToLongBits(other._initialX) != Double.doubleToLongBits(this._initialX))
        return false;
      if (Double.doubleToLongBits(other._initialY) != Double.doubleToLongBits(this._initialY))
        return false;
      if (Double.doubleToLongBits(other._mouseAnchorX) != Double.doubleToLongBits(this._mouseAnchorX))
        return false;
      if (Double.doubleToLongBits(other._mouseAnchorY) != Double.doubleToLongBits(this._mouseAnchorY))
        return false;
      if (this._initialPosInScene == null) {
        if (other._initialPosInScene != null)
          return false;
      } else if (!this._initialPosInScene.equals(other._initialPosInScene))
        return false;
      return true;
    }
    
    @Override
    public String toString() {
      String result = new ToStringHelper().toString(this);
      return result;
    }
  }
  
  private MoveBehavior.DragContext dragContext;
  
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
    T _host_2 = this.getHost();
    Node _node_2 = _host_2.getNode();
    final EventHandler<MouseEvent> _function_2 = new EventHandler<MouseEvent>() {
      public void handle(final MouseEvent it) {
        boolean _or = false;
        double _initialX = MoveBehavior.this.dragContext.getInitialX();
        T _host = MoveBehavior.this.getHost();
        double _layoutX = _host.getLayoutX();
        boolean _notEquals = (_initialX != _layoutX);
        if (_notEquals) {
          _or = true;
        } else {
          double _initialY = MoveBehavior.this.dragContext.getInitialY();
          T _host_1 = MoveBehavior.this.getHost();
          double _layoutY = _host_1.getLayoutY();
          boolean _notEquals_1 = (_initialY != _layoutY);
          _or = _notEquals_1;
        }
        if (_or) {
          T _host_2 = MoveBehavior.this.getHost();
          XRoot _root = CoreExtensions.getRoot(_host_2);
          CommandStack _commandStack = _root.getCommandStack();
          T _host_3 = MoveBehavior.this.getHost();
          double _initialX_1 = MoveBehavior.this.dragContext.getInitialX();
          double _initialY_1 = MoveBehavior.this.dragContext.getInitialY();
          T _host_4 = MoveBehavior.this.getHost();
          double _layoutX_1 = _host_4.getLayoutX();
          T _host_5 = MoveBehavior.this.getHost();
          double _layoutY_1 = _host_5.getLayoutY();
          MoveCommand _moveCommand = new MoveCommand(_host_3, _initialX_1, _initialY_1, _layoutX_1, _layoutY_1);
          _commandStack.execute(_moveCommand);
        }
      }
    };
    _node_2.setOnMouseReleased(_function_2);
  }
  
  public Class<? extends Behavior> getBehaviorKey() {
    return MoveBehavior.class;
  }
  
  public MoveBehavior.DragContext mousePressed(final MouseEvent it) {
    MoveBehavior.DragContext _xblockexpression = null;
    {
      T _host = this.getHost();
      Parent _parent = _host.getParent();
      T _host_1 = this.getHost();
      double _layoutX = _host_1.getLayoutX();
      T _host_2 = this.getHost();
      double _layoutY = _host_2.getLayoutY();
      final Point2D initialPositionInScene = _parent.localToScene(_layoutX, _layoutY);
      T _host_3 = this.getHost();
      double _layoutX_1 = _host_3.getLayoutX();
      T _host_4 = this.getHost();
      double _layoutY_1 = _host_4.getLayoutY();
      double _screenX = it.getScreenX();
      double _screenY = it.getScreenY();
      MoveBehavior.DragContext _dragContext = new MoveBehavior.DragContext(_layoutX_1, _layoutY_1, _screenX, _screenY, initialPositionInScene);
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
