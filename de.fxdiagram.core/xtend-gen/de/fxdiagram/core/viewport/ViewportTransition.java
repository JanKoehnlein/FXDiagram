package de.fxdiagram.core.viewport;

import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransform;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ViewportTransition extends Transition {
  private XRoot root;
  
  private ViewportMemento from;
  
  private ViewportMemento to;
  
  public ViewportTransition(final XRoot root, final ViewportMemento toMemento, final Duration maxDuration) {
    this.root = root;
    this.from = root.getViewportTransform().createMemento();
    this.to = toMemento;
    this.setMaxDuration(maxDuration);
  }
  
  public ViewportTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale) {
    this(root, targetCenterInDiagram, targetScale, 0);
  }
  
  public ViewportTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale, final double targetAngle) {
    this.root = root;
    this.from = root.getViewportTransform().createMemento();
    this.to = this.calculateTargetMemento(targetCenterInDiagram, targetScale, targetAngle);
    this.setCycleDuration(this.getDefaultDuration());
  }
  
  public Duration getDefaultDuration() {
    return Duration.seconds(0.5);
  }
  
  public void setDuration(final Duration duration) {
    this.setCycleDuration(duration);
  }
  
  public void setMaxDuration(final Duration maxDuration) {
    double _min = Math.min(maxDuration.toMillis(), this.getDefaultDuration().toMillis());
    Duration _duration = new Duration(_min);
    this.setCycleDuration(_duration);
  }
  
  public ViewportMemento getFrom() {
    return this.from;
  }
  
  public ViewportMemento getTo() {
    return this.to;
  }
  
  @Override
  protected void interpolate(final double frac) {
    ViewportTransform _viewportTransform = this.root.getViewportTransform();
    final Procedure1<ViewportTransform> _function = (ViewportTransform it) -> {
      double _rotate = this.from.getRotate();
      double _multiply = ((1 - frac) * _rotate);
      double _rotate_1 = this.to.getRotate();
      double _multiply_1 = (frac * _rotate_1);
      double _plus = (_multiply + _multiply_1);
      it.setRotate(_plus);
      double _scale = this.from.getScale();
      double _multiply_2 = ((1 - frac) * _scale);
      double _scale_1 = this.to.getScale();
      double _multiply_3 = (frac * _scale_1);
      double _plus_1 = (_multiply_2 + _multiply_3);
      it.setScale(_plus_1);
      double _translateX = this.from.getTranslateX();
      double _multiply_4 = ((1 - frac) * _translateX);
      double _translateX_1 = this.to.getTranslateX();
      double _multiply_5 = (frac * _translateX_1);
      double _plus_2 = (_multiply_4 + _multiply_5);
      it.setTranslateX(_plus_2);
      double _translateY = this.from.getTranslateY();
      double _multiply_6 = ((1 - frac) * _translateY);
      double _translateY_1 = this.to.getTranslateY();
      double _multiply_7 = (frac * _translateY_1);
      double _plus_3 = (_multiply_6 + _multiply_7);
      it.setTranslateY(_plus_3);
    };
    ObjectExtensions.<ViewportTransform>operator_doubleArrow(_viewportTransform, _function);
  }
  
  public ViewportMemento calculateTargetMemento(final Point2D targetCenterInDiagram, final double targetScale, final double targetAngle) {
    final double toScale = Math.max(ViewportTransform.MIN_SCALE, targetScale);
    ViewportTransform _viewportTransform = this.root.getViewportTransform();
    final Procedure1<ViewportTransform> _function = (ViewportTransform it) -> {
      double _scale = this.from.getScale();
      double _divide = (toScale / _scale);
      it.scaleRelative(_divide);
      it.setRotate(targetAngle);
    };
    ObjectExtensions.<ViewportTransform>operator_doubleArrow(_viewportTransform, _function);
    final Point2D centerInScene = this.root.getDiagram().localToScene(targetCenterInDiagram);
    double _width = this.root.getScene().getWidth();
    double _multiply = (0.5 * _width);
    double _x = centerInScene.getX();
    double _minus = (_multiply - _x);
    double _translateX = this.root.getViewportTransform().getTranslateX();
    double _plus = (_minus + _translateX);
    double _height = this.root.getScene().getHeight();
    double _multiply_1 = (0.5 * _height);
    double _y = centerInScene.getY();
    double _minus_1 = (_multiply_1 - _y);
    double _translateY = this.root.getViewportTransform().getTranslateY();
    double _plus_1 = (_minus_1 + _translateY);
    final Point2D toTranslation = new Point2D(_plus, _plus_1);
    this.root.getViewportTransform().applyMemento(this.from);
    double _x_1 = toTranslation.getX();
    double _y_1 = toTranslation.getY();
    return new ViewportMemento(_x_1, _y_1, toScale, targetAngle);
  }
}
