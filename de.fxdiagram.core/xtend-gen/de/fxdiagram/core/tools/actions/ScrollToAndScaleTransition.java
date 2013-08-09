package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XRootDiagram;
import de.fxdiagram.core.geometry.TransformExtensions;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.transform.Affine;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ScrollToAndScaleTransition extends Transition {
  private XRootDiagram diagram;
  
  private double fromScale;
  
  private double toScale;
  
  private Point2D fromTranslation;
  
  private Point2D toTranslation;
  
  public ScrollToAndScaleTransition(final XRootDiagram diagram, final Point2D targetCenterInDiagram, final double targetScale) {
    this.diagram = diagram;
    double _scale = diagram.getScale();
    this.fromScale = _scale;
    double _max = Math.max(XRootDiagram.MIN_SCALE, targetScale);
    this.toScale = _max;
    Affine _canvasTransform = diagram.getCanvasTransform();
    double _tx = _canvasTransform.getTx();
    Affine _canvasTransform_1 = diagram.getCanvasTransform();
    double _ty = _canvasTransform_1.getTy();
    Point2D _point2D = new Point2D(_tx, _ty);
    this.fromTranslation = _point2D;
    final double rescale = (this.toScale / this.fromScale);
    Affine _canvasTransform_2 = diagram.getCanvasTransform();
    TransformExtensions.scale(_canvasTransform_2, rescale, rescale);
    final Point2D centerInScene = diagram.localToScene(targetCenterInDiagram);
    Scene _scene = diagram.getScene();
    double _width = _scene.getWidth();
    double _multiply = (0.5 * _width);
    double _x = centerInScene.getX();
    double _minus = (_multiply - _x);
    Affine _canvasTransform_3 = diagram.getCanvasTransform();
    double _tx_1 = _canvasTransform_3.getTx();
    double _plus = (_minus + _tx_1);
    Scene _scene_1 = diagram.getScene();
    double _height = _scene_1.getHeight();
    double _multiply_1 = (0.5 * _height);
    double _y = centerInScene.getY();
    double _minus_1 = (_multiply_1 - _y);
    Affine _canvasTransform_4 = diagram.getCanvasTransform();
    double _ty_1 = _canvasTransform_4.getTy();
    double _plus_1 = (_minus_1 + _ty_1);
    Point2D _point2D_1 = new Point2D(_plus, _plus_1);
    this.toTranslation = _point2D_1;
    Affine _canvasTransform_5 = diagram.getCanvasTransform();
    double _divide = (1 / rescale);
    double _divide_1 = (1 / rescale);
    TransformExtensions.scale(_canvasTransform_5, _divide, _divide_1);
    Duration _millis = Duration.millis(500);
    this.setCycleDuration(_millis);
  }
  
  protected void interpolate(final double frac) {
    double _minus = (1 - frac);
    double _multiply = (_minus * this.fromScale);
    double _multiply_1 = (frac * this.toScale);
    final double scaleNow = (_multiply + _multiply_1);
    double _minus_1 = (1 - frac);
    double _x = this.fromTranslation.getX();
    double _multiply_2 = (_minus_1 * _x);
    double _x_1 = this.toTranslation.getX();
    double _multiply_3 = (frac * _x_1);
    final double txNow = (_multiply_2 + _multiply_3);
    double _minus_2 = (1 - frac);
    double _y = this.fromTranslation.getY();
    double _multiply_4 = (_minus_2 * _y);
    double _y_1 = this.toTranslation.getY();
    double _multiply_5 = (frac * _y_1);
    final double tyNow = (_multiply_4 + _multiply_5);
    double _scale = this.diagram.getScale();
    final double rescale = (scaleNow / _scale);
    this.diagram.setScale(scaleNow);
    Affine _canvasTransform = this.diagram.getCanvasTransform();
    final Procedure1<Affine> _function = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.scale(it, rescale, rescale);
        it.setTx(txNow);
        it.setTy(tyNow);
      }
    };
    ObjectExtensions.<Affine>operator_doubleArrow(_canvasTransform, _function);
  }
}
