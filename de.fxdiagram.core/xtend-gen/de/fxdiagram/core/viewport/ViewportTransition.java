package de.fxdiagram.core.viewport;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.viewport.ViewportMemento;
import de.fxdiagram.core.viewport.ViewportTransform;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ViewportTransition extends Transition {
  private XRoot root;
  
  private ViewportMemento from;
  
  private ViewportMemento to;
  
  public ViewportTransition(final XRoot root, final ViewportMemento toMemento, final Duration duration) {
    this.root = root;
    ViewportTransform _diagramTransform = root.getDiagramTransform();
    ViewportMemento _createMemento = _diagramTransform.createMemento();
    this.from = _createMemento;
    this.to = toMemento;
    double _millis = duration.toMillis();
    double _dist = this.from.dist(this.to);
    double _min = Math.min(_millis, _dist);
    Duration _duration = new Duration(_min);
    this.setCycleDuration(_duration);
  }
  
  public ViewportTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale) {
    this(root, targetCenterInDiagram, targetScale, 0);
  }
  
  public ViewportTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale, final double targetAngle) {
    this.root = root;
    ViewportTransform _diagramTransform = root.getDiagramTransform();
    ViewportMemento _createMemento = _diagramTransform.createMemento();
    this.from = _createMemento;
    final double toScale = Math.max(ViewportTransform.MIN_SCALE, targetScale);
    ViewportTransform _diagramTransform_1 = root.getDiagramTransform();
    final Procedure1<ViewportTransform> _function = new Procedure1<ViewportTransform>() {
      public void apply(final ViewportTransform it) {
        double _scale = ViewportTransition.this.from.getScale();
        double _divide = (toScale / _scale);
        it.scaleRelative(_divide);
        it.setRotate(targetAngle);
      }
    };
    ObjectExtensions.<ViewportTransform>operator_doubleArrow(_diagramTransform_1, _function);
    XDiagram _diagram = root.getDiagram();
    final Point2D centerInScene = _diagram.localToScene(targetCenterInDiagram);
    Scene _scene = root.getScene();
    double _width = _scene.getWidth();
    double _multiply = (0.5 * _width);
    double _x = centerInScene.getX();
    double _minus = (_multiply - _x);
    ViewportTransform _diagramTransform_2 = root.getDiagramTransform();
    double _translateX = _diagramTransform_2.getTranslateX();
    double _plus = (_minus + _translateX);
    Scene _scene_1 = root.getScene();
    double _height = _scene_1.getHeight();
    double _multiply_1 = (0.5 * _height);
    double _y = centerInScene.getY();
    double _minus_1 = (_multiply_1 - _y);
    ViewportTransform _diagramTransform_3 = root.getDiagramTransform();
    double _translateY = _diagramTransform_3.getTranslateY();
    double _plus_1 = (_minus_1 + _translateY);
    final Point2D toTranslation = new Point2D(_plus, _plus_1);
    ViewportTransform _diagramTransform_4 = root.getDiagramTransform();
    _diagramTransform_4.applyMemento(this.from);
    double _x_1 = toTranslation.getX();
    double _y_1 = toTranslation.getY();
    ViewportMemento _viewportMemento = new ViewportMemento(_x_1, _y_1, toScale, targetAngle);
    this.to = _viewportMemento;
    Duration _millis = Duration.millis(500);
    this.setCycleDuration(_millis);
  }
  
  public void setDuration(final Duration duration) {
    this.setCycleDuration(duration);
  }
  
  protected void interpolate(final double frac) {
    ViewportTransform _diagramTransform = this.root.getDiagramTransform();
    final Procedure1<ViewportTransform> _function = new Procedure1<ViewportTransform>() {
      public void apply(final ViewportTransform it) {
        double _rotate = ViewportTransition.this.from.getRotate();
        double _multiply = ((1 - frac) * _rotate);
        double _rotate_1 = ViewportTransition.this.to.getRotate();
        double _multiply_1 = (frac * _rotate_1);
        double _plus = (_multiply + _multiply_1);
        it.setRotate(_plus);
        double _scale = ViewportTransition.this.from.getScale();
        double _multiply_2 = ((1 - frac) * _scale);
        double _scale_1 = ViewportTransition.this.to.getScale();
        double _multiply_3 = (frac * _scale_1);
        double _plus_1 = (_multiply_2 + _multiply_3);
        it.setScale(_plus_1);
        double _translateX = ViewportTransition.this.from.getTranslateX();
        double _multiply_4 = ((1 - frac) * _translateX);
        double _translateX_1 = ViewportTransition.this.to.getTranslateX();
        double _multiply_5 = (frac * _translateX_1);
        double _plus_2 = (_multiply_4 + _multiply_5);
        it.setTranslateX(_plus_2);
        double _translateY = ViewportTransition.this.from.getTranslateY();
        double _multiply_6 = ((1 - frac) * _translateY);
        double _translateY_1 = ViewportTransition.this.to.getTranslateY();
        double _multiply_7 = (frac * _translateY_1);
        double _plus_3 = (_multiply_6 + _multiply_7);
        it.setTranslateY(_plus_3);
      }
    };
    ObjectExtensions.<ViewportTransform>operator_doubleArrow(_diagramTransform, _function);
  }
}
