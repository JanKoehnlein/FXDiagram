package de.fxdiagram.core;

import de.fxdiagram.core.XShape;
import de.fxdiagram.core.behavior.MoveBehavior;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class XControlPoint extends XShape {
  private MoveBehavior moveBehavior;
  
  public XControlPoint() {
    Circle _circle = new Circle();
    final Procedure1<Circle> _function = new Procedure1<Circle>() {
        public void apply(final Circle it) {
          it.setRadius(5);
          it.setStroke(Color.RED);
          it.setFill(Color.WHITE);
        }
      };
    Circle _doubleArrow = ObjectExtensions.<Circle>operator_doubleArrow(_circle, _function);
    this.setNode(_doubleArrow);
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
    MoveBehavior _moveBehavior = new MoveBehavior(this);
    this.moveBehavior = _moveBehavior;
    this.moveBehavior.activate();
  }
  
  public MoveBehavior getMoveBehavior() {
    return this.moveBehavior;
  }
  
  public Circle getCircle() {
    Node _node = this.getNode();
    return ((Circle) _node);
  }
}
