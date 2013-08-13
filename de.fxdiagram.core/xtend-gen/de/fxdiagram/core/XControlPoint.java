package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.XControlPointType;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.geometry.Point2DExtensions;
import de.fxdiagram.core.geometry.TransformExtensions;
import java.net.URL;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XControlPoint extends XShape {
  private MoveBehavior<XControlPoint> moveBehavior;
  
  private XControlPointType type;
  
  public XControlPoint() {
    this.setType(XControlPointType.CONTROL_POINT);
  }
  
  public XControlPointType getType() {
    return this.type;
  }
  
  public MoveBehavior<XControlPoint> setType(final XControlPointType type) {
    MoveBehavior<XControlPoint> _xblockexpression = null;
    {
      this.type = type;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(type,XControlPointType.ANCHOR)) {
          _matched=true;
          Circle _circle = new Circle();
          final Procedure1<Circle> _function = new Procedure1<Circle>() {
            public void apply(final Circle it) {
              it.setRadius(3);
              it.setStroke(Color.BLUE);
              it.setFill(Color.WHITE);
            }
          };
          Circle _doubleArrow = ObjectExtensions.<Circle>operator_doubleArrow(_circle, _function);
          this.setNode(_doubleArrow);
        }
      }
      if (!_matched) {
        if (Objects.equal(type,XControlPointType.CONTROL_POINT)) {
          _matched=true;
          Node _newMagnet = this.newMagnet();
          this.setNode(_newMagnet);
        }
      }
      if (!_matched) {
        if (Objects.equal(type,XControlPointType.INTERPOLATED)) {
          _matched=true;
          Circle _circle_1 = new Circle();
          final Procedure1<Circle> _function_1 = new Procedure1<Circle>() {
            public void apply(final Circle it) {
              it.setRadius(5);
              it.setStroke(Color.RED);
              it.setFill(Color.WHITE);
            }
          };
          Circle _doubleArrow_1 = ObjectExtensions.<Circle>operator_doubleArrow(_circle_1, _function_1);
          this.setNode(_doubleArrow_1);
        }
      }
      MoveBehavior<XControlPoint> _xifexpression = null;
      boolean _notEquals = (!Objects.equal(type, XControlPointType.ANCHOR));
      if (_notEquals) {
        MoveBehavior<XControlPoint> _moveBehavior = new MoveBehavior<XControlPoint>(this);
        MoveBehavior<XControlPoint> _moveBehavior_1 = this.moveBehavior = _moveBehavior;
        _xifexpression = _moveBehavior_1;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  protected void doActivate() {
    if (this.moveBehavior!=null) {
      this.moveBehavior.activate();
    }
  }
  
  public void selectionFeedback(final boolean isSelected) {
    if (isSelected) {
      final XControlPointType type = this.type;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(type,XControlPointType.CONTROL_POINT)) {
          _matched=true;
          Node _node = this.getNode();
          DropShadow _dropShadow = new DropShadow();
          _node.setEffect(_dropShadow);
        }
      }
      if (!_matched) {
        if (Objects.equal(type,XControlPointType.INTERPOLATED)) {
          _matched=true;
          Node _node_1 = this.getNode();
          ((Circle) _node_1).setFill(Color.RED);
        }
      }
    } else {
      final XControlPointType type_1 = this.type;
      boolean _matched_1 = false;
      if (!_matched_1) {
        if (Objects.equal(type_1,XControlPointType.CONTROL_POINT)) {
          _matched_1=true;
          Node _node_2 = this.getNode();
          _node_2.setEffect(null);
        }
      }
      if (!_matched_1) {
        if (Objects.equal(type_1,XControlPointType.INTERPOLATED)) {
          _matched_1=true;
          Node _node_3 = this.getNode();
          ((Circle) _node_3).setFill(Color.WHITE);
        }
      }
    }
  }
  
  public boolean isSelectable() {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(this.type, XControlPointType.ANCHOR));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _isSelectable = super.isSelectable();
      _and = (_notEquals && _isSelectable);
    }
    return _and;
  }
  
  public MoveBehavior<? extends XShape> getMoveBehavior() {
    return this.moveBehavior;
  }
  
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("XControlPoint at (");
    double _layoutX = this.getLayoutX();
    _builder.append(_layoutX, "");
    _builder.append(",");
    double _layoutY = this.getLayoutY();
    _builder.append(_layoutY, "");
    _builder.append(")");
    return _builder.toString();
  }
  
  public Boolean update(final List<XControlPoint> siblings) {
    Boolean _xifexpression = null;
    boolean _equals = Objects.equal(this.type, XControlPointType.CONTROL_POINT);
    if (_equals) {
      Boolean _xblockexpression = null;
      {
        final int index = siblings.indexOf(this);
        Boolean _xifexpression_1 = null;
        boolean _and = false;
        boolean _greaterThan = (index > 0);
        if (!_greaterThan) {
          _and = false;
        } else {
          int _size = siblings.size();
          int _minus = (_size - 1);
          boolean _lessThan = (index < _minus);
          _and = (_greaterThan && _lessThan);
        }
        if (_and) {
          boolean _xblockexpression_1 = false;
          {
            int _minus_1 = (index - 1);
            final XControlPoint predecessor = siblings.get(_minus_1);
            int _plus = (index + 1);
            final XControlPoint successor = siblings.get(_plus);
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
              double _plus_1 = (angle + 180);
              angle = _plus_1;
            }
            Affine _affine = new Affine();
            final Affine trafo = _affine;
            double _minus_2 = (-0.5);
            Node _node = this.getNode();
            Bounds _layoutBounds = _node.getLayoutBounds();
            double _width = _layoutBounds.getWidth();
            double _multiply = (_minus_2 * _width);
            Node _node_1 = this.getNode();
            Bounds _layoutBounds_1 = _node_1.getLayoutBounds();
            double _height = _layoutBounds_1.getHeight();
            double _minus_3 = (-_height);
            double _minus_4 = (_minus_3 - 5);
            TransformExtensions.translate(trafo, _multiply, _minus_4);
            TransformExtensions.rotate(trafo, angle);
            ObservableList<Transform> _transforms = this.getTransforms();
            boolean _setAll = _transforms.setAll(trafo);
            _xblockexpression_1 = (_setAll);
          }
          _xifexpression_1 = Boolean.valueOf(_xblockexpression_1);
        }
        _xblockexpression = (_xifexpression_1);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  protected Node newMagnet() {
    Group _group = new Group();
    final Procedure1<Group> _function = new Procedure1<Group>() {
      public void apply(final Group it) {
        try {
          ObservableList<Node> _children = it.getChildren();
          Class<? extends Group> _class = it.getClass();
          URL _resource = _class.getResource("/icons/Magnet.fxml");
          Node _load = FXMLLoader.<Node>load(_resource);
          _children.add(_load);
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    Group _doubleArrow = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
    return _doubleArrow;
  }
}
