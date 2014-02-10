package de.fxdiagram.lib.nodes;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.AbstractOpenBehavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.model.DomainObjectHandle;
import de.fxdiagram.core.model.ModelElement;
import de.fxdiagram.core.model.ModelLoad;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@ModelNode
@SuppressWarnings("all")
public class FlipNode extends XNode {
  private Node front;
  
  private Node back;
  
  private Group pane = new Group();
  
  private boolean isCurrentFront = true;
  
  private EventHandler<MouseEvent> clickHandler;
  
  public FlipNode(final String name) {
    super(name);
  }
  
  public FlipNode(final DomainObjectHandle domainObject) {
    super(domainObject);
  }
  
  public void doActivatePreview() {
    super.doActivatePreview();
    this.setNode(this.pane);
  }
  
  public void doActivate() {
    super.doActivate();
    boolean _equals = Objects.equal(this.front, null);
    if (_equals) {
      throw new IllegalStateException("FlipNode.front not set");
    }
    boolean _equals_1 = Objects.equal(this.back, null);
    if (_equals_1) {
      throw new IllegalStateException("FlipNode.back not set");
    }
    this.setCursor(Cursor.HAND);
    this.setFlipOnDoubleClick(true);
    final AbstractOpenBehavior _function = new AbstractOpenBehavior() {
      public void open() {
        FlipNode.this.flip(true);
      }
    };
    final AbstractOpenBehavior openBehavior = _function;
    this.addBehavior(openBehavior);
  }
  
  public void setFlipOnDoubleClick(final boolean isFlipOnDoubleClick) {
    EventHandler<MouseEvent> _xifexpression = null;
    if (isFlipOnDoubleClick) {
      final EventHandler<MouseEvent> _function = new EventHandler<MouseEvent>() {
        public void handle(final MouseEvent event) {
          int _clickCount = event.getClickCount();
          boolean _equals = (_clickCount == 2);
          if (_equals) {
            boolean _and = false;
            boolean _notEquals = (!Objects.equal(FlipNode.this.front, null));
            if (!_notEquals) {
              _and = false;
            } else {
              boolean _notEquals_1 = (!Objects.equal(FlipNode.this.back, null));
              _and = _notEquals_1;
            }
            if (_and) {
              boolean _isHorizontal = FlipNode.this.isHorizontal(event);
              FlipNode.this.flip(_isHorizontal);
            }
          }
        }
      };
      _xifexpression = _function;
    } else {
      _xifexpression = null;
    }
    this.clickHandler = _xifexpression;
    this.setOnMouseClicked(this.clickHandler);
  }
  
  public void flip(final boolean isHorizontal) {
    double _layoutX = this.front.getLayoutX();
    Bounds _layoutBounds = this.back.getLayoutBounds();
    double _width = _layoutBounds.getWidth();
    Bounds _layoutBounds_1 = this.front.getLayoutBounds();
    double _width_1 = _layoutBounds_1.getWidth();
    double _minus = (_width - _width_1);
    double _divide = (_minus / 2);
    double _minus_1 = (_layoutX - _divide);
    Bounds _layoutBounds_2 = this.back.getLayoutBounds();
    double _minX = _layoutBounds_2.getMinX();
    double _minus_2 = (_minus_1 - _minX);
    this.back.setLayoutX(_minus_2);
    double _layoutY = this.front.getLayoutY();
    Bounds _layoutBounds_3 = this.back.getLayoutBounds();
    double _height = _layoutBounds_3.getHeight();
    Bounds _layoutBounds_4 = this.front.getLayoutBounds();
    double _height_1 = _layoutBounds_4.getHeight();
    double _minus_3 = (_height - _height_1);
    double _divide_1 = (_minus_3 / 2);
    double _minus_4 = (_layoutY - _divide_1);
    Bounds _layoutBounds_5 = this.back.getLayoutBounds();
    double _minY = _layoutBounds_5.getMinY();
    double _minus_5 = (_minus_4 - _minY);
    this.back.setLayoutY(_minus_5);
    Point3D _xifexpression = null;
    if (isHorizontal) {
      _xifexpression = new Point3D(1, 0, 0);
    } else {
      _xifexpression = new Point3D(0, 1, 0);
    }
    final Point3D turnAxis = _xifexpression;
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = new Procedure1<SequentialTransition>() {
      public void apply(final SequentialTransition it) {
        ObservableList<Animation> _children = it.getChildren();
        RotateTransition _rotateTransition = new RotateTransition();
        final Procedure1<RotateTransition> _function = new Procedure1<RotateTransition>() {
          public void apply(final RotateTransition it) {
            Node _currentVisible = FlipNode.this.getCurrentVisible();
            it.setNode(_currentVisible);
            Duration _millis = Duration.millis(250);
            it.setDuration(_millis);
            it.setAxis(turnAxis);
            it.setFromAngle(0);
            it.setToAngle(90);
            final EventHandler<ActionEvent> _function = new EventHandler<ActionEvent>() {
              public void handle(final ActionEvent it) {
                Node _currentVisible = FlipNode.this.getCurrentVisible();
                _currentVisible.setVisible(false);
                FlipNode.this.isCurrentFront = (!FlipNode.this.isCurrentFront);
                Node _currentVisible_1 = FlipNode.this.getCurrentVisible();
                _currentVisible_1.setVisible(true);
              }
            };
            it.setOnFinished(_function);
          }
        };
        RotateTransition _doubleArrow = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition, _function);
        _children.add(_doubleArrow);
        ObservableList<Animation> _children_1 = it.getChildren();
        RotateTransition _rotateTransition_1 = new RotateTransition();
        final Procedure1<RotateTransition> _function_1 = new Procedure1<RotateTransition>() {
          public void apply(final RotateTransition it) {
            Node _currentInvisible = FlipNode.this.getCurrentInvisible();
            it.setNode(_currentInvisible);
            Duration _millis = Duration.millis(250);
            it.setDuration(_millis);
            it.setAxis(turnAxis);
            it.setFromAngle(90);
            it.setToAngle(0);
          }
        };
        RotateTransition _doubleArrow_1 = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition_1, _function_1);
        _children_1.add(_doubleArrow_1);
        it.play();
      }
    };
    ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
  }
  
  protected boolean isHorizontal(final MouseEvent event) {
    double _sceneX = event.getSceneX();
    double _sceneY = event.getSceneY();
    final Point2D clickInScene = new Point2D(_sceneX, _sceneY);
    Node _currentVisible = this.getCurrentVisible();
    final Point2D clickInLocal = _currentVisible.sceneToLocal(clickInScene);
    Bounds _boundsInLocal = this.getBoundsInLocal();
    final Point2D center = BoundsExtensions.center(_boundsInLocal);
    double _x = clickInLocal.getX();
    double _x_1 = center.getX();
    double _minus = (_x - _x_1);
    double _y = clickInLocal.getY();
    double _y_1 = center.getY();
    double _minus_1 = (_y - _y_1);
    final Point3D direction = new Point3D(_minus, _minus_1, 0);
    boolean _or = false;
    double _x_2 = direction.getX();
    double _x_3 = direction.getX();
    double _multiply = (_x_2 * _x_3);
    double _y_2 = direction.getY();
    double _y_3 = direction.getY();
    double _multiply_1 = (_y_2 * _y_3);
    double _plus = (_multiply + _multiply_1);
    boolean _lessThan = (_plus < NumberExpressionExtensions.EPSILON);
    if (_lessThan) {
      _or = true;
    } else {
      double _x_4 = direction.getX();
      double _abs = Math.abs(_x_4);
      double _y_4 = direction.getY();
      double _abs_1 = Math.abs(_y_4);
      boolean _lessThan_1 = (_abs < _abs_1);
      _or = _lessThan_1;
    }
    return _or;
  }
  
  public void setFront(final Node front) {
    this.front = front;
    ObservableList<Node> _children = this.pane.getChildren();
    _children.add(front);
    front.setVisible(this.isCurrentFront);
  }
  
  public Node getFront() {
    return this.front;
  }
  
  public void setBack(final Node back) {
    this.back = back;
    ObservableList<Node> _children = this.pane.getChildren();
    _children.add(back);
    back.setVisible((!this.isCurrentFront));
  }
  
  public Node getBack() {
    return this.back;
  }
  
  public Node getCurrentVisible() {
    Node _xifexpression = null;
    if (this.isCurrentFront) {
      _xifexpression = this.front;
    } else {
      _xifexpression = this.back;
    }
    return _xifexpression;
  }
  
  public Node getCurrentInvisible() {
    Node _xifexpression = null;
    if (this.isCurrentFront) {
      _xifexpression = this.back;
    } else {
      _xifexpression = this.front;
    }
    return _xifexpression;
  }
  
  /**
   * Automatically generated by @ModelNode. Used in model deserialization.
   */
  public FlipNode(final ModelLoad modelLoad) {
    super(modelLoad);
  }
  
  public void populate(final ModelElement modelElement) {
    
  }
}
