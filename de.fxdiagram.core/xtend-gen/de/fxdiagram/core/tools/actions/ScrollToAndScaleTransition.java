package de.fxdiagram.core.tools.actions;

import de.fxdiagram.core.XDiagram;
import de.fxdiagram.core.XRoot;
import de.fxdiagram.core.extensions.TransformExtensions;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.transform.Affine;
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
  
  public ScrollToAndScaleTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale) {
    this.root = root;
    double _diagramScale = root.getDiagramScale();
    this.fromScale = _diagramScale;
    double _max = Math.max(XRoot.MIN_SCALE, targetScale);
    this.toScale = _max;
    Affine _diagramTransform = root.getDiagramTransform();
    double _tx = _diagramTransform.getTx();
    Affine _diagramTransform_1 = root.getDiagramTransform();
    double _ty = _diagramTransform_1.getTy();
    Point2D _point2D = new Point2D(_tx, _ty);
    this.fromTranslation = _point2D;
    final double rescale = (this.toScale / this.fromScale);
    Affine _diagramTransform_2 = root.getDiagramTransform();
    TransformExtensions.scale(_diagramTransform_2, rescale, rescale);
    XDiagram _diagram = root.getDiagram();
    final Point2D centerInScene = _diagram.localToScene(targetCenterInDiagram);
    Scene _scene = root.getScene();
    double _width = _scene.getWidth();
    double _multiply = (0.5 * _width);
    double _x = centerInScene.getX();
    double _minus = (_multiply - _x);
    Affine _diagramTransform_3 = root.getDiagramTransform();
    double _tx_1 = _diagramTransform_3.getTx();
    double _plus = (_minus + _tx_1);
    Scene _scene_1 = root.getScene();
    double _height = _scene_1.getHeight();
    double _multiply_1 = (0.5 * _height);
    double _y = centerInScene.getY();
    double _minus_1 = (_multiply_1 - _y);
    Affine _diagramTransform_4 = root.getDiagramTransform();
    double _ty_1 = _diagramTransform_4.getTy();
    double _plus_1 = (_minus_1 + _ty_1);
    Point2D _point2D_1 = new Point2D(_plus, _plus_1);
    this.toTranslation = _point2D_1;
    Affine _diagramTransform_5 = root.getDiagramTransform();
    double _divide = (1 / rescale);
    double _divide_1 = (1 / rescale);
    TransformExtensions.scale(_diagramTransform_5, _divide, _divide_1);
    Duration _millis = Duration.millis(500);
    this.setCycleDuration(_millis);
  }
  
  public void setDuration(final Duration duration) {
    this.setCycleDuration(duration);
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
    double _diagramScale = this.root.getDiagramScale();
    final double rescale = (scaleNow / _diagramScale);
    this.root.setDiagramScale(scaleNow);
    Affine _diagramTransform = this.root.getDiagramTransform();
    final Procedure1<Affine> _function = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.scale(it, rescale, rescale);
        it.setTx(txNow);
        it.setTy(tyNow);
      }
    };
    ObjectExtensions.<Affine>operator_doubleArrow(_diagramTransform, _function);
  }
}
