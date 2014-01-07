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
  
  private double fromAngle;
  
  private double toAngle;
  
  public ScrollToAndScaleTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale) {
    this(root, targetCenterInDiagram, targetScale, 0);
  }
  
  public ScrollToAndScaleTransition(final XRoot root, final Point2D targetCenterInDiagram, final double targetScale, final double targetAngle) {
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
    Affine _diagramTransform_2 = root.getDiagramTransform();
    double _mxx = _diagramTransform_2.getMxx();
    double _divide = (_mxx / this.fromScale);
    double _acos = Math.acos(_divide);
    this.fromAngle = _acos;
    this.toAngle = targetAngle;
    final double rescale = (this.toScale / this.fromScale);
    Affine _diagramTransform_3 = root.getDiagramTransform();
    final Procedure1<Affine> _function = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        TransformExtensions.scale(it, rescale, rescale);
        double _cos = Math.cos(ScrollToAndScaleTransition.this.toAngle);
        double _multiply = (ScrollToAndScaleTransition.this.toScale * _cos);
        it.setMxx(_multiply);
        double _sin = Math.sin(ScrollToAndScaleTransition.this.toAngle);
        double _multiply_1 = (ScrollToAndScaleTransition.this.toScale * _sin);
        it.setMxy(_multiply_1);
        double _sin_1 = Math.sin(ScrollToAndScaleTransition.this.toAngle);
        double _minus = (-_sin_1);
        double _multiply_2 = (ScrollToAndScaleTransition.this.toScale * _minus);
        it.setMyx(_multiply_2);
        double _cos_1 = Math.cos(ScrollToAndScaleTransition.this.toAngle);
        double _multiply_3 = (ScrollToAndScaleTransition.this.toScale * _cos_1);
        it.setMyy(_multiply_3);
      }
    };
    ObjectExtensions.<Affine>operator_doubleArrow(_diagramTransform_3, _function);
    XDiagram _diagram = root.getDiagram();
    final Point2D centerInScene = _diagram.localToScene(targetCenterInDiagram);
    Scene _scene = root.getScene();
    double _width = _scene.getWidth();
    double _multiply = (0.5 * _width);
    double _x = centerInScene.getX();
    double _minus = (_multiply - _x);
    Affine _diagramTransform_4 = root.getDiagramTransform();
    double _tx_1 = _diagramTransform_4.getTx();
    double _plus = (_minus + _tx_1);
    Scene _scene_1 = root.getScene();
    double _height = _scene_1.getHeight();
    double _multiply_1 = (0.5 * _height);
    double _y = centerInScene.getY();
    double _minus_1 = (_multiply_1 - _y);
    Affine _diagramTransform_5 = root.getDiagramTransform();
    double _ty_1 = _diagramTransform_5.getTy();
    double _plus_1 = (_minus_1 + _ty_1);
    Point2D _point2D_1 = new Point2D(_plus, _plus_1);
    this.toTranslation = _point2D_1;
    Affine _diagramTransform_6 = root.getDiagramTransform();
    final Procedure1<Affine> _function_1 = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        double _cos = Math.cos(ScrollToAndScaleTransition.this.fromAngle);
        double _multiply = (ScrollToAndScaleTransition.this.fromScale * _cos);
        it.setMxx(_multiply);
        double _sin = Math.sin(ScrollToAndScaleTransition.this.fromAngle);
        double _multiply_1 = (ScrollToAndScaleTransition.this.fromScale * _sin);
        it.setMxy(_multiply_1);
        double _sin_1 = Math.sin(ScrollToAndScaleTransition.this.fromAngle);
        double _minus = (-_sin_1);
        double _multiply_2 = (ScrollToAndScaleTransition.this.fromScale * _minus);
        it.setMyx(_multiply_2);
        double _cos_1 = Math.cos(ScrollToAndScaleTransition.this.fromAngle);
        double _multiply_3 = (ScrollToAndScaleTransition.this.fromScale * _cos_1);
        it.setMyy(_multiply_3);
        double _x = ScrollToAndScaleTransition.this.fromTranslation.getX();
        it.setTx(_x);
        double _y = ScrollToAndScaleTransition.this.fromTranslation.getY();
        it.setTx(_y);
      }
    };
    ObjectExtensions.<Affine>operator_doubleArrow(_diagramTransform_6, _function_1);
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
    this.root.setDiagramScale(scaleNow);
    Affine _diagramTransform = this.root.getDiagramTransform();
    final Procedure1<Affine> _function = new Procedure1<Affine>() {
      public void apply(final Affine it) {
        double _cos = Math.cos(angleNow);
        double _multiply = (scaleNow * _cos);
        it.setMxx(_multiply);
        double _sin = Math.sin(angleNow);
        double _multiply_1 = (scaleNow * _sin);
        it.setMxy(_multiply_1);
        double _sin_1 = Math.sin(angleNow);
        double _minus = (-_sin_1);
        double _multiply_2 = (scaleNow * _minus);
        it.setMyx(_multiply_2);
        double _cos_1 = Math.cos(angleNow);
        double _multiply_3 = (scaleNow * _cos_1);
        it.setMyy(_multiply_3);
        it.setTx(txNow);
        it.setTy(tyNow);
      }
    };
    ObjectExtensions.<Affine>operator_doubleArrow(_diagramTransform, _function);
  }
}
