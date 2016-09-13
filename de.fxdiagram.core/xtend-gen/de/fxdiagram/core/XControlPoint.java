package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.annotations.properties.ModelNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.ControlPointMoveBehavior;
import de.fxdiagram.core.extensions.Point2DExtensions;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.images.Magnet;
import de.fxdiagram.core.model.ModelElementImpl;
import de.fxdiagram.core.model.ToString;
import de.fxdiagram.core.model.XModelProvider;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * A point belonging to an {@link XConnection}.
 * 
 * Its {@link Type} denotes
 * <ul>
 * <li>{@link Type#ANCHOR} an endpoint touching one of the connectied nodes,</li>
 * <li>{@link Type#INTERPOLATED} a point on the curve,</li>
 * <li>{@link Type#CONTROL_POINT} a point next to the curve controling its shape, e.g. for spline curves.</li>
 * <li>
 */
@ModelNode({ "layoutX", "layoutY", "type" })
@SuppressWarnings("all")
public class XControlPoint extends XShape implements XModelProvider {
  public enum Type {
    ANCHOR,
    
    INTERPOLATED,
    
    CONTROL_POINT,
    
    DANGLING;
  }
  
  @Override
  protected Node createNode() {
    XControlPoint.Type _type = this.getType();
    if (_type != null) {
      switch (_type) {
        case ANCHOR:
          Circle _circle = new Circle();
          final Procedure1<Circle> _function = (Circle it) -> {
            it.setRadius(3);
            it.setStroke(Color.BLUE);
            it.setFill(Color.WHITE);
          };
          return ObjectExtensions.<Circle>operator_doubleArrow(_circle, _function);
        case CONTROL_POINT:
          return new Magnet();
        case INTERPOLATED:
          Circle _circle_1 = new Circle();
          final Procedure1<Circle> _function_1 = (Circle it) -> {
            it.setRadius(5);
            it.setStroke(Color.RED);
            it.setFill(Color.WHITE);
          };
          return ObjectExtensions.<Circle>operator_doubleArrow(_circle_1, _function_1);
        case DANGLING:
          Circle _circle_2 = new Circle();
          final Procedure1<Circle> _function_2 = (Circle it) -> {
            it.setRadius(5);
            it.setStroke(Color.RED);
            it.setFill(Color.RED);
            it.setOpacity(0.5);
          };
          return ObjectExtensions.<Circle>operator_doubleArrow(_circle_2, _function_2);
        default:
          return null;
      }
    } else {
      return null;
    }
  }
  
  @Override
  protected void doActivate() {
    final ChangeListener<XControlPoint.Type> _function = (ObservableValue<? extends XControlPoint.Type> p, XControlPoint.Type o, XControlPoint.Type n) -> {
      ObservableList<Node> _children = this.getChildren();
      Node _node = this.getNode();
      _children.remove(_node);
      ObjectProperty<Node> _nodeProperty = this.nodeProperty();
      _nodeProperty.set(null);
      this.getNode();
      boolean _selected = this.getSelected();
      this.selectionFeedback(_selected);
    };
    this.typeProperty.addListener(_function);
    XControlPoint.Type _type = this.getType();
    boolean _notEquals = (!Objects.equal(_type, XControlPoint.Type.ANCHOR));
    if (_notEquals) {
      ControlPointMoveBehavior _controlPointMoveBehavior = new ControlPointMoveBehavior(this);
      this.addBehavior(_controlPointMoveBehavior);
    }
  }
  
  @Override
  public void selectionFeedback(final boolean isSelected) {
    if (isSelected) {
      XControlPoint.Type _type = this.getType();
      if (_type != null) {
        switch (_type) {
          case CONTROL_POINT:
            Node _node = this.getNode();
            DropShadow _dropShadow = new DropShadow();
            _node.setEffect(_dropShadow);
            break;
          case INTERPOLATED:
          case DANGLING:
            Node _node_1 = this.getNode();
            ((Circle) _node_1).setFill(Color.RED);
            break;
          default:
            break;
        }
      }
    } else {
      XControlPoint.Type _type_1 = this.getType();
      if (_type_1 != null) {
        switch (_type_1) {
          case CONTROL_POINT:
            Node _node_2 = this.getNode();
            _node_2.setEffect(null);
            break;
          case INTERPOLATED:
          case DANGLING:
            Node _node_3 = this.getNode();
            ((Circle) _node_3).setFill(Color.WHITE);
            break;
          default:
            break;
        }
      }
    }
  }
  
  @Override
  public boolean isSelectable() {
    return ((!Objects.equal(this.getType(), XControlPoint.Type.ANCHOR)) && super.isSelectable());
  }
  
  public Boolean update(final List<XControlPoint> siblings) {
    boolean _xifexpression = false;
    XControlPoint.Type _type = this.getType();
    boolean _equals = Objects.equal(_type, XControlPoint.Type.CONTROL_POINT);
    if (_equals) {
      boolean _xblockexpression = false;
      {
        final int index = siblings.indexOf(this);
        boolean _xifexpression_1 = false;
        if (((index > 0) && (index < (siblings.size() - 1)))) {
          boolean _xblockexpression_1 = false;
          {
            final XControlPoint predecessor = siblings.get((index - 1));
            final XControlPoint successor = siblings.get((index + 1));
            double _layoutX = successor.getLayoutX();
            double _layoutX_1 = predecessor.getLayoutX();
            final double dx = (_layoutX - _layoutX_1);
            double _layoutY = successor.getLayoutY();
            double _layoutY_1 = predecessor.getLayoutY();
            final double dy = (_layoutY - _layoutY_1);
            double _atan2 = Math.atan2(dy, dx);
            double angle = Math.toDegrees(_atan2);
            double _layoutX_2 = this.getLayoutX();
            double _layoutY_2 = this.getLayoutY();
            double _layoutX_3 = successor.getLayoutX();
            double _layoutY_3 = successor.getLayoutY();
            double _layoutX_4 = predecessor.getLayoutX();
            double _layoutY_4 = predecessor.getLayoutY();
            boolean _isClockwise = Point2DExtensions.isClockwise(_layoutX_2, _layoutY_2, _layoutX_3, _layoutY_3, _layoutX_4, _layoutY_4);
            if (_isClockwise) {
              angle = (angle + 180);
            }
            final Affine trafo = new Affine();
            Node _node = this.getNode();
            Bounds _layoutBounds = _node.getLayoutBounds();
            double _width = _layoutBounds.getWidth();
            double _multiply = ((-0.5) * _width);
            Node _node_1 = this.getNode();
            Bounds _layoutBounds_1 = _node_1.getLayoutBounds();
            double _height = _layoutBounds_1.getHeight();
            double _minus = (-_height);
            double _minus_1 = (_minus - 5);
            TransformExtensions.translate(trafo, _multiply, _minus_1);
            TransformExtensions.rotate(trafo, angle);
            ObservableList<Transform> _transforms = this.getTransforms();
            _xblockexpression_1 = _transforms.setAll(trafo);
          }
          _xifexpression_1 = _xblockexpression_1;
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      ObservableList<Transform> _transforms = this.getTransforms();
      _transforms.clear();
    }
    return Boolean.valueOf(_xifexpression);
  }
  
  public void populate(final ModelElementImpl modelElement) {
    modelElement.addProperty(layoutXProperty(), Double.class);
    modelElement.addProperty(layoutYProperty(), Double.class);
    modelElement.addProperty(typeProperty, XControlPoint.Type.class);
  }
  
  public String toString() {
    return ToString.toString(this);
  }
  
  private SimpleObjectProperty<XControlPoint.Type> typeProperty = new SimpleObjectProperty<XControlPoint.Type>(this, "type",_initType());
  
  private static final XControlPoint.Type _initType() {
    return XControlPoint.Type.CONTROL_POINT;
  }
  
  public XControlPoint.Type getType() {
    return this.typeProperty.get();
  }
  
  public void setType(final XControlPoint.Type type) {
    this.typeProperty.set(type);
  }
  
  public ObjectProperty<XControlPoint.Type> typeProperty() {
    return this.typeProperty;
  }
}
