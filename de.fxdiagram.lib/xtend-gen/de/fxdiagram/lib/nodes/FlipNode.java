package de.fxdiagram.lib.nodes;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.logging.Logging;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.behavior.AbstractOpenBehavior;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.model.DomainObjectDescriptor;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import java.util.logging.Logger;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * An {@link XNode} with a front and a back side that can be flipped with a 3D effect
 * on click.
 */
@Logging
@ModelNode
@SuppressWarnings("all")
public class FlipNode extends XNode {
  private Node front;
  
  private Node back;
  
  private Group pane;
  
  private boolean isCurrentFront = true;
  
  public FlipNode() {
  }
  
  public FlipNode(final String name) {
    super(name);
  }
  
  public FlipNode(final DomainObjectDescriptor domainObject) {
    super(domainObject);
  }
  
  @Override
  protected Node createNode() {
    Group _group = new Group();
    return this.pane = _group;
  }
  
  @Override
  public void initializeGraphics() {
    super.initializeGraphics();
    boolean _equals = Objects.equal(this.front, null);
    if (_equals) {
      FlipNode.LOG.severe("FlipNode.front not set");
    }
    boolean _equals_1 = Objects.equal(this.back, null);
    if (_equals_1) {
      FlipNode.LOG.severe("FlipNode.back not set");
    }
  }
  
  @Override
  public void doActivate() {
    super.doActivate();
    this.setCursor(Cursor.HAND);
    this.registerOnClick();
    final AbstractOpenBehavior _function = new AbstractOpenBehavior() {
      @Override
      public void open() {
        FlipNode.this.flip(true);
      }
    };
    final AbstractOpenBehavior openBehavior = _function;
    this.addBehavior(openBehavior);
  }
  
  public void registerOnClick() {
    final EventHandler<MouseEvent> _function = (MouseEvent it) -> {
      boolean _and = false;
      MouseButton _button = it.getButton();
      boolean _equals = Objects.equal(_button, MouseButton.PRIMARY);
      if (!_equals) {
        _and = false;
      } else {
        int _clickCount = it.getClickCount();
        boolean _equals_1 = (_clickCount == 2);
        _and = _equals_1;
      }
      if (_and) {
        boolean _and_1 = false;
        boolean _notEquals = (!Objects.equal(this.front, null));
        if (!_notEquals) {
          _and_1 = false;
        } else {
          boolean _notEquals_1 = (!Objects.equal(this.back, null));
          _and_1 = _notEquals_1;
        }
        if (_and_1) {
          boolean _isHorizontal = this.isHorizontal(it);
          this.flip(_isHorizontal);
        }
      }
    };
    this.setOnMouseClicked(_function);
  }
  
  public void flip(final boolean isHorizontal) {
    if (this.isCurrentFront) {
      this.alignFaces(this.front, this.back);
    } else {
      this.alignFaces(this.back, this.front);
    }
    Point3D _xifexpression = null;
    if (isHorizontal) {
      _xifexpression = new Point3D(1, 0, 0);
    } else {
      _xifexpression = new Point3D(0, 1, 0);
    }
    final Point3D turnAxis = _xifexpression;
    SequentialTransition _sequentialTransition = new SequentialTransition();
    final Procedure1<SequentialTransition> _function = (SequentialTransition it) -> {
      ObservableList<Animation> _children = it.getChildren();
      RotateTransition _rotateTransition = new RotateTransition();
      final Procedure1<RotateTransition> _function_1 = (RotateTransition it_1) -> {
        Node _currentVisible = this.getCurrentVisible();
        it_1.setNode(_currentVisible);
        Duration _millis = Duration.millis(250);
        it_1.setDuration(_millis);
        it_1.setAxis(turnAxis);
        it_1.setFromAngle(0);
        it_1.setToAngle(90);
        final EventHandler<ActionEvent> _function_2 = (ActionEvent it_2) -> {
          Node _currentVisible_1 = this.getCurrentVisible();
          _currentVisible_1.setVisible(false);
          this.isCurrentFront = (!this.isCurrentFront);
          Node _currentVisible_2 = this.getCurrentVisible();
          _currentVisible_2.setVisible(true);
        };
        it_1.setOnFinished(_function_2);
      };
      RotateTransition _doubleArrow = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition, _function_1);
      _children.add(_doubleArrow);
      ObservableList<Animation> _children_1 = it.getChildren();
      RotateTransition _rotateTransition_1 = new RotateTransition();
      final Procedure1<RotateTransition> _function_2 = (RotateTransition it_1) -> {
        Node _currentInvisible = this.getCurrentInvisible();
        it_1.setNode(_currentInvisible);
        Duration _millis = Duration.millis(250);
        it_1.setDuration(_millis);
        it_1.setAxis(turnAxis);
        it_1.setFromAngle(90);
        it_1.setToAngle(0);
      };
      RotateTransition _doubleArrow_1 = ObjectExtensions.<RotateTransition>operator_doubleArrow(_rotateTransition_1, _function_2);
      _children_1.add(_doubleArrow_1);
      it.play();
    };
    ObjectExtensions.<SequentialTransition>operator_doubleArrow(_sequentialTransition, _function);
  }
  
  public void alignFaces(final Node fixed, final Node toBeAligned) {
    double _layoutX = fixed.getLayoutX();
    Bounds _layoutBounds = toBeAligned.getLayoutBounds();
    double _width = _layoutBounds.getWidth();
    Bounds _layoutBounds_1 = fixed.getLayoutBounds();
    double _width_1 = _layoutBounds_1.getWidth();
    double _minus = (_width - _width_1);
    double _divide = (_minus / 2);
    double _minus_1 = (_layoutX - _divide);
    Bounds _layoutBounds_2 = toBeAligned.getLayoutBounds();
    double _minX = _layoutBounds_2.getMinX();
    double _minus_2 = (_minus_1 - _minX);
    toBeAligned.setLayoutX(_minus_2);
    double _layoutY = fixed.getLayoutY();
    Bounds _layoutBounds_3 = toBeAligned.getLayoutBounds();
    double _height = _layoutBounds_3.getHeight();
    Bounds _layoutBounds_4 = fixed.getLayoutBounds();
    double _height_1 = _layoutBounds_4.getHeight();
    double _minus_3 = (_height - _height_1);
    double _divide_1 = (_minus_3 / 2);
    double _minus_4 = (_layoutY - _divide_1);
    Bounds _layoutBounds_5 = toBeAligned.getLayoutBounds();
    double _minY = _layoutBounds_5.getMinY();
    double _minus_5 = (_minus_4 - _minY);
    toBeAligned.setLayoutY(_minus_5);
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
    boolean _notEquals = (!Objects.equal(this.front, null));
    if (_notEquals) {
      ObservableList<Node> _children = this.pane.getChildren();
      _children.remove(this.front);
    }
    this.front = front;
    ObservableList<Node> _children_1 = this.pane.getChildren();
    _children_1.add(front);
    front.setVisible(this.isCurrentFront);
  }
  
  public Node getFront() {
    return this.front;
  }
  
  public void setBack(final Node back) {
    boolean _notEquals = (!Objects.equal(this.back, null));
    if (_notEquals) {
      ObservableList<Node> _children = this.pane.getChildren();
      _children.remove(this.back);
    }
    this.back = back;
    ObservableList<Node> _children_1 = this.pane.getChildren();
    _children_1.add(back);
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
  
  private static Logger LOG = Logger.getLogger("de.fxdiagram.lib.nodes.FlipNode");
    ;
  
  public void populate(final ModelElementImpl modelElement) {
    super.populate(modelElement);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
}
