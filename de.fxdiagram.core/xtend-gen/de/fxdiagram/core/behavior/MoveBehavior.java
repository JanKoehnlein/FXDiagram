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
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@SuppressWarnings("all")
public class MoveBehavior<T extends XShape> extends AbstractHostBehavior<T> {
  @Data
  public static class DragContext {
    private final double initialX;
    
    private final double initialY;
    
    private final double mouseAnchorX;
    
    private final double mouseAnchorY;
    
    private final Point2D initialPosInScene;
    
    public DragContext(final double initialX, final double initialY, final double mouseAnchorX, final double mouseAnchorY, final Point2D initialPosInScene) {
      super();
      this.initialX = initialX;
      this.initialY = initialY;
      this.mouseAnchorX = mouseAnchorX;
      this.mouseAnchorY = mouseAnchorY;
      this.initialPosInScene = initialPosInScene;
    }
    
    @Override
    @Pure
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (Double.doubleToLongBits(this.initialX) ^ (Double.doubleToLongBits(this.initialX) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.initialY) ^ (Double.doubleToLongBits(this.initialY) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.mouseAnchorX) ^ (Double.doubleToLongBits(this.mouseAnchorX) >>> 32));
      result = prime * result + (int) (Double.doubleToLongBits(this.mouseAnchorY) ^ (Double.doubleToLongBits(this.mouseAnchorY) >>> 32));
      result = prime * result + ((this.initialPosInScene== null) ? 0 : this.initialPosInScene.hashCode());
      return result;
    }
    
    @Override
    @Pure
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      MoveBehavior.DragContext other = (MoveBehavior.DragContext) obj;
      if (Double.doubleToLongBits(other.initialX) != Double.doubleToLongBits(this.initialX))
        return false; 
      if (Double.doubleToLongBits(other.initialY) != Double.doubleToLongBits(this.initialY))
        return false; 
      if (Double.doubleToLongBits(other.mouseAnchorX) != Double.doubleToLongBits(this.mouseAnchorX))
        return false; 
      if (Double.doubleToLongBits(other.mouseAnchorY) != Double.doubleToLongBits(this.mouseAnchorY))
        return false; 
      if (this.initialPosInScene == null) {
        if (other.initialPosInScene != null)
          return false;
      } else if (!this.initialPosInScene.equals(other.initialPosInScene))
        return false;
      return true;
    }
    
    @Override
    @Pure
    public String toString() {
      ToStringBuilder b = new ToStringBuilder(this);
      b.add("initialX", this.initialX);
      b.add("initialY", this.initialY);
      b.add("mouseAnchorX", this.mouseAnchorX);
      b.add("mouseAnchorY", this.mouseAnchorY);
      b.add("initialPosInScene", this.initialPosInScene);
      return b.toString();
    }
    
    @Pure
    public double getInitialX() {
      return this.initialX;
    }
    
    @Pure
    public double getInitialY() {
      return this.initialY;
    }
    
    @Pure
    public double getMouseAnchorX() {
      return this.mouseAnchorX;
    }
    
    @Pure
    public double getMouseAnchorY() {
      return this.mouseAnchorY;
    }
    
    @Pure
    public Point2D getInitialPosInScene() {
      return this.initialPosInScene;
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
        boolean _notEquals = (!Objects.equal(MoveBehavior.this.dragContext, null));
        if (_notEquals) {
          boolean _or = false;
          T _host = MoveBehavior.this.getHost();
          double _layoutX = _host.getLayoutX();
          boolean _notEquals_1 = (MoveBehavior.this.dragContext.initialX != _layoutX);
          if (_notEquals_1) {
            _or = true;
          } else {
            T _host_1 = MoveBehavior.this.getHost();
            double _layoutY = _host_1.getLayoutY();
            boolean _notEquals_2 = (MoveBehavior.this.dragContext.initialY != _layoutY);
            _or = _notEquals_2;
          }
          if (_or) {
            T _host_2 = MoveBehavior.this.getHost();
            XRoot _root = CoreExtensions.getRoot(_host_2);
            CommandStack _commandStack = _root.getCommandStack();
            T _host_3 = MoveBehavior.this.getHost();
            T _host_4 = MoveBehavior.this.getHost();
            double _layoutX_1 = _host_4.getLayoutX();
            T _host_5 = MoveBehavior.this.getHost();
            double _layoutY_1 = _host_5.getLayoutY();
            MoveCommand _moveCommand = new MoveCommand(_host_3, 
              MoveBehavior.this.dragContext.initialX, MoveBehavior.this.dragContext.initialY, _layoutX_1, _layoutY_1);
            _commandStack.execute(_moveCommand);
          }
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
    double _x = this.dragContext.initialPosInScene.getX();
    double _screenX = it.getScreenX();
    double _plus = (_x + _screenX);
    double _minus = (_plus - this.dragContext.mouseAnchorX);
    double _y = this.dragContext.initialPosInScene.getY();
    double _screenY = it.getScreenY();
    double _plus_1 = (_y + _screenY);
    double _minus_1 = (_plus_1 - this.dragContext.mouseAnchorY);
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
