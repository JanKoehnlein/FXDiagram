package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.AccumulativeTransform2D;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ScrollToAndScaleTransition extends Transition {
  private XRoot root;
  
  private double fromScale;
  
  private double toScale;
  
  private Point2D fromTranslation;
  
  private Point2D toTranslation;
  
  private double fromAngle;
  
  private double toAngle;
  
  public ScrollToAndScaleTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale) {
    this(root, targetCenterInDiagram, targetScale, 0);
  }
  
  public ScrollToAndScaleTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale, final double targetAngle) {
    this.root = root;
    AccumulativeTransform2D _diagramTransform = root.getDiagramTransform();
    double _scale = _diagramTransform.getScale();
    this.fromScale = _scale;
    double _max = Math.max(AccumulativeTransform2D.MIN_SCALE, targetScale);
    this.toScale = _max;
    AccumulativeTransform2D _diagramTransform_1 = root.getDiagramTransform();
    double _translateX = _diagramTransform_1.getTranslateX();
    AccumulativeTransform2D _diagramTransform_2 = root.getDiagramTransform();
    double _translateY = _diagramTransform_2.getTranslateY();
    Point2D _point2D = new Point2D(_translateX, _translateY);
    this.fromTranslation = _point2D;
    AccumulativeTransform2D _diagramTransform_3 = root.getDiagramTransform();
    double _rotate = _diagramTransform_3.getRotate();
    this.fromAngle = _rotate;
    this.toAngle = targetAngle;
    AccumulativeTransform2D _diagramTransform_4 = root.getDiagramTransform();
    final Procedure1<AccumulativeTransform2D> _function = new Procedure1<AccumulativeTransform2D>() {
      public void apply(final AccumulativeTransform2D it) {
        it.scaleRelative((ScrollToAndScaleTransition.this.toScale / ScrollToAndScaleTransition.this.fromScale));
        it.setRotate(ScrollToAndScaleTransition.this.toAngle);
      }
    };
    ObjectExtensions.<AccumulativeTransform2D>operator_doubleArrow(_diagramTransform_4, _function);
    XDiagram _diagram = root.getDiagram();
    final Point2D centerInScene = _diagram.localToScene(targetCenterInDiagram);
    Scene _scene = root.getScene();
    double _width = _scene.getWidth();
    double _multiply = (0.5 * _width);
    double _x = centerInScene.getX();
    double _minus = (_multiply - _x);
    AccumulativeTransform2D _diagramTransform_5 = root.getDiagramTransform();
    double _translateX_1 = _diagramTransform_5.getTranslateX();
    double _plus = (_minus + _translateX_1);
    Scene _scene_1 = root.getScene();
    double _height = _scene_1.getHeight();
    double _multiply_1 = (0.5 * _height);
    double _y = centerInScene.getY();
    double _minus_1 = (_multiply_1 - _y);
    AccumulativeTransform2D _diagramTransform_6 = root.getDiagramTransform();
    double _translateY_1 = _diagramTransform_6.getTranslateY();
    double _plus_1 = (_minus_1 + _translateY_1);
    Point2D _point2D_1 = new Point2D(_plus, _plus_1);
    this.toTranslation = _point2D_1;
    AccumulativeTransform2D _diagramTransform_7 = root.getDiagramTransform();
    final Procedure1<AccumulativeTransform2D> _function_1 = new Procedure1<AccumulativeTransform2D>() {
      public void apply(final AccumulativeTransform2D it) {
        it.setScale(ScrollToAndScaleTransition.this.fromScale);
        it.setRotate(ScrollToAndScaleTransition.this.fromAngle);
        it.setTranslate(ScrollToAndScaleTransition.this.fromTranslation);
      }
    };
    ObjectExtensions.<AccumulativeTransform2D>operator_doubleArrow(_diagramTransform_7, _function_1);
    Duration _millis = Duration.millis(500);
    this.setCycleDuration(_millis);
  }
  
  public void setDuration(final Duration duration) {
    this.setCycleDuration(duration);
  }
  
  protected void interpolate(final double frac) {
    final double scaleNow = (((1 - frac) * this.fromScale) + (frac * this.toScale));
    final double angleNow = (((1 - frac) * this.fromAngle) + (frac * this.toAngle));
    double _x = this.fromTranslation.getX();
    double _multiply = ((1 - frac) * _x);
    double _x_1 = this.toTranslation.getX();
    double _multiply_1 = (frac * _x_1);
    final double txNow = (_multiply + _multiply_1);
    double _y = this.fromTranslation.getY();
    double _multiply_2 = ((1 - frac) * _y);
    double _y_1 = this.toTranslation.getY();
    double _multiply_3 = (frac * _y_1);
    final double tyNow = (_multiply_2 + _multiply_3);
    AccumulativeTransform2D _diagramTransform = this.root.getDiagramTransform();
    final Procedure1<AccumulativeTransform2D> _function = new Procedure1<AccumulativeTransform2D>() {
      public void apply(final AccumulativeTransform2D it) {
        it.setRotate(angleNow);
        it.setScale(scaleNow);
        it.setTranslateX(txNow);
        it.setTranslateY(tyNow);
      }
    };
    ObjectExtensions.<AccumulativeTransform2D>operator_doubleArrow(_diagramTransform, _function);
  }
}
