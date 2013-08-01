package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.XControlPointType;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.geometry.TransformExtensions;
import de.fxdiagram.core.services.ImageCache;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtend2.lib.StringConcatenation;
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
          ImageView _imageView = new ImageView();
          final Procedure1<ImageView> _function_1 = new Procedure1<ImageView>() {
            public void apply(final ImageView it) {
              ImageCache _get = ImageCache.get();
              Image _image = _get.getImage("icons/magnet.png");
              it.setImage(_image);
            }
          };
          ImageView _doubleArrow_1 = ObjectExtensions.<ImageView>operator_doubleArrow(_imageView, _function_1);
          this.setNode(_doubleArrow_1);
        }
      }
      if (!_matched) {
        if (Objects.equal(type,XControlPointType.INTERPOLATED)) {
          _matched=true;
          Circle _circle_1 = new Circle();
          final Procedure1<Circle> _function_2 = new Procedure1<Circle>() {
            public void apply(final Circle it) {
              it.setRadius(5);
              it.setStroke(Color.RED);
              it.setFill(Color.WHITE);
            }
          };
          Circle _doubleArrow_2 = ObjectExtensions.<Circle>operator_doubleArrow(_circle_1, _function_2);
          this.setNode(_doubleArrow_2);
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
    BooleanProperty _selectedProperty = this.selectedProperty();
    final ChangeListener<Boolean> _function = new ChangeListener<Boolean>() {
      public void changed(final ObservableValue<? extends Boolean> prop, final Boolean oldVal, final Boolean newVal) {
        if ((newVal).booleanValue()) {
          final XControlPointType type = XControlPoint.this.type;
          boolean _matched = false;
          if (!_matched) {
            if (Objects.equal(type,XControlPointType.CONTROL_POINT)) {
              _matched=true;
              Node _node = XControlPoint.this.getNode();
              DropShadow _dropShadow = new DropShadow();
              _node.setEffect(_dropShadow);
            }
          }
          if (!_matched) {
            if (Objects.equal(type,XControlPointType.INTERPOLATED)) {
              _matched=true;
              Node _node_1 = XControlPoint.this.getNode();
              ((Circle) _node_1).setFill(Color.RED);
            }
          }
        } else {
          final XControlPointType type_1 = XControlPoint.this.type;
          boolean _matched_1 = false;
          if (!_matched_1) {
            if (Objects.equal(type_1,XControlPointType.CONTROL_POINT)) {
              _matched_1=true;
              Node _node_2 = XControlPoint.this.getNode();
              _node_2.setEffect(null);
            }
          }
          if (!_matched_1) {
            if (Objects.equal(type_1,XControlPointType.INTERPOLATED)) {
              _matched_1=true;
              Node _node_3 = XControlPoint.this.getNode();
              ((Circle) _node_3).setFill(Color.WHITE);
            }
          }
        }
      }
    };
    _selectedProperty.addListener(_function);
    if (this.moveBehavior!=null) {
      this.moveBehavior.activate();
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
            boolean _isClockwise = this.isClockwise(_layoutX_2, _layoutY_2, _layoutX_3, _layoutY_3, _layoutX_4, _layoutY_4);
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
  
  protected boolean isClockwise(final double x0, final double y0, final double x1, final double y1, final double x2, final double y2) {
    double _minus = (x1 - x0);
    double _plus = (y1 + y0);
    double _multiply = (_minus * _plus);
    double _minus_1 = (x2 - x1);
    double _plus_1 = (y2 + y1);
    double _multiply_1 = (_minus_1 * _plus_1);
    double _plus_2 = (_multiply + _multiply_1);
    double _minus_2 = (x0 - x2);
    double _plus_3 = (y0 + y2);
    double _multiply_2 = (_minus_2 * _plus_3);
    double _plus_4 = (_plus_2 + _multiply_2);
    boolean _greaterThan = (_plus_4 > 0);
    return _greaterThan;
  }
}
