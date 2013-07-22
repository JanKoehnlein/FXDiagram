package de.fxdiagram.core;

import com.google.common.base.Objects;
import de.fxdiagram.core.XControlPointType;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XControlPoint extends XShape {
  private MoveBehavior moveBehavior;
  
  private XControlPointType type;
  
  public XControlPoint() {
    this.setType(XControlPointType.CONTROL_POINT);
  }
  
  public XControlPointType getType() {
    return this.type;
  }
  
  public MoveBehavior setType(final XControlPointType type) {
    MoveBehavior _xblockexpression = null;
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
      if (!_matched) {
        if (Objects.equal(type,XControlPointType.INTERPOLATED)) {
          _matched=true;
          Circle _circle_2 = new Circle();
          final Procedure1<Circle> _function_2 = new Procedure1<Circle>() {
              public void apply(final Circle it) {
                it.setRadius(5);
                it.setStroke(Color.RED);
                it.setFill(Color.WHITE);
              }
            };
          Circle _doubleArrow_2 = ObjectExtensions.<Circle>operator_doubleArrow(_circle_2, _function_2);
          this.setNode(_doubleArrow_2);
        }
      }
      MoveBehavior _xifexpression = null;
      boolean _notEquals = (!Objects.equal(type, XControlPointType.ANCHOR));
      if (_notEquals) {
        MoveBehavior _moveBehavior = new MoveBehavior(this);
        MoveBehavior _moveBehavior_1 = this.moveBehavior = _moveBehavior;
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
            Circle _circle = XControlPoint.this.getCircle();
            _circle.setFill(Color.RED);
          } else {
            Circle _circle_1 = XControlPoint.this.getCircle();
            _circle_1.setFill(Color.WHITE);
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
  
  public MoveBehavior getMoveBehavior() {
    return this.moveBehavior;
  }
  
  public Circle getCircle() {
    Node _node = this.getNode();
    return ((Circle) _node);
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
}
