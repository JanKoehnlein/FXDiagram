package de.fxdiagram.core.viewport;

import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.TransformExtensions;
import de.fxdiagram.core.viewport.ViewportMemento;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import org.eclipse.xtend2.lib.StringConcatenation;

/**
 * Container for a {@link Transform} that allows basic 2D transformations.
 * 
 * As opposed to a plain {@link Affine} and the internal transform of a {@link Node}, this class
 * allows accumulative (relative) changes, e.g. rotate another x degrees.
 * 
 * As opposed to the {@link TransformExtensions} this class exposes scale, rotation and translation
 * as JavaFX {@link Property Properties}, such that clients can e.g. react on changes of the scale.
 */
@SuppressWarnings("all")
public class ViewportTransform {
  public final static double MIN_SCALE = NumberExpressionExtensions.EPSILON;
  
  private Affine transform;
  
  public ViewportTransform() {
    Affine _affine = new Affine();
    this.transform = _affine;
    DoubleProperty _txProperty = this.transform.txProperty();
    this.translateXProperty.bind(_txProperty);
    DoubleProperty _tyProperty = this.transform.tyProperty();
    this.translateYProperty.bind(_tyProperty);
  }
  
  public void translateRelative(final double deltaX, final double deltaY) {
    double _tx = this.transform.getTx();
    double _plus = (_tx + deltaX);
    this.transform.setTx(_plus);
    double _ty = this.transform.getTy();
    double _plus_1 = (_ty + deltaY);
    this.transform.setTy(_plus_1);
  }
  
  public void setTranslate(final double newX, final double newY) {
    this.transform.setTx(newX);
    this.transform.setTy(newY);
  }
  
  public void setTranslate(final Point2D newPoint) {
    double _x = newPoint.getX();
    this.transform.setTx(_x);
    double _y = newPoint.getY();
    this.transform.setTy(_y);
  }
  
  public void setTranslateX(final double newX) {
    this.transform.setTx(newX);
  }
  
  public void setTranslateY(final double newY) {
    this.transform.setTy(newY);
  }
  
  public double scaleRelative(final double deltaScale) {
    double _scale = this.getScale();
    double _multiply = (_scale * deltaScale);
    final double safeNewScale = Math.max(ViewportTransform.MIN_SCALE, _multiply);
    double _scale_1 = this.getScale();
    final double safeDeltaScale = (safeNewScale / _scale_1);
    double _rotate = this.getRotate();
    this.applyRotationAndScale(safeNewScale, _rotate);
    double _tx = this.transform.getTx();
    double _multiply_1 = (safeDeltaScale * _tx);
    this.transform.setTx(_multiply_1);
    double _ty = this.transform.getTy();
    double _multiply_2 = (safeDeltaScale * _ty);
    this.transform.setTy(_multiply_2);
    this.scaleProperty.set(safeNewScale);
    return safeDeltaScale;
  }
  
  public double setScale(final double newScale) {
    double _max = Math.max(ViewportTransform.MIN_SCALE, newScale);
    this.scaleProperty.set(_max);
    double _scale = this.getScale();
    double _rotate = this.getRotate();
    this.applyRotationAndScale(_scale, _rotate);
    return this.scaleProperty.get();
  }
  
  public void rotateRelative(final double deltaAngle, final double pivotX, final double pivotY) {
    double _rotate = this.getRotate();
    final double newAngle = (_rotate + deltaAngle);
    double _scale = this.getScale();
    this.applyRotationAndScale(_scale, newAngle);
    final double radians = Math.toRadians(deltaAngle);
    final double cos = Math.cos(radians);
    final double sin = Math.sin(radians);
    double _tx = this.transform.getTx();
    double _multiply = (cos * _tx);
    double _ty = this.transform.getTy();
    double _multiply_1 = (sin * _ty);
    double _plus = (_multiply + _multiply_1);
    double _plus_1 = (_plus + pivotX);
    double _minus = (_plus_1 - (cos * pivotX));
    double _minus_1 = (_minus - (sin * pivotY));
    this.transform.setTx(_minus_1);
    double _tx_1 = this.transform.getTx();
    double _multiply_2 = ((-sin) * _tx_1);
    double _ty_1 = this.transform.getTy();
    double _multiply_3 = (cos * _ty_1);
    double _plus_2 = (_multiply_2 + _multiply_3);
    double _plus_3 = (_plus_2 + pivotY);
    double _plus_4 = (_plus_3 + (sin * pivotX));
    double _minus_2 = (_plus_4 - (cos * pivotY));
    this.transform.setTy(_minus_2);
    this.rotateProperty.set(newAngle);
  }
  
  public void rotateRelative(final double deltaAngle) {
    double _rotate = this.getRotate();
    final double newAngle = (_rotate + deltaAngle);
    double _scale = this.getScale();
    this.applyRotationAndScale(_scale, newAngle);
    final double radians = Math.toRadians(deltaAngle);
    final double cos = Math.cos(radians);
    final double sin = Math.sin(radians);
    double _tx = this.transform.getTx();
    double _multiply = (cos * _tx);
    double _ty = this.transform.getTy();
    double _multiply_1 = (sin * _ty);
    double _plus = (_multiply + _multiply_1);
    this.transform.setTx(_plus);
    double _tx_1 = this.transform.getTx();
    double _multiply_2 = ((-sin) * _tx_1);
    double _ty_1 = this.transform.getTy();
    double _multiply_3 = (cos * _ty_1);
    double _plus_1 = (_multiply_2 + _multiply_3);
    this.transform.setTy(_plus_1);
    this.rotateProperty.set(newAngle);
  }
  
  public void setRotate(final double newAngle) {
    this.rotateProperty.set(newAngle);
    double _scale = this.getScale();
    double _rotate = this.getRotate();
    this.applyRotationAndScale(_scale, _rotate);
  }
  
  protected void applyRotationAndScale(final double newScale, final double newAngle) {
    final double radians = Math.toRadians(newAngle);
    final double cos = Math.cos(radians);
    final double sin = Math.sin(radians);
    this.transform.setMxx((newScale * cos));
    this.transform.setMxy((newScale * sin));
    this.transform.setMyx((newScale * (-sin)));
    this.transform.setMyy((newScale * cos));
  }
  
  public Transform getTransform() {
    return this.transform;
  }
  
  public ViewportMemento createMemento() {
    double _translateX = this.getTranslateX();
    double _translateY = this.getTranslateY();
    double _scale = this.getScale();
    double _rotate = this.getRotate();
    return new ViewportMemento(_translateX, _translateY, _scale, _rotate);
  }
  
  public void applyMemento(final ViewportMemento it) {
    double _translateX = it.getTranslateX();
    this.setTranslateX(_translateX);
    double _translateY = it.getTranslateY();
    this.setTranslateY(_translateY);
    double _scale = it.getScale();
    this.setScale(_scale);
    double _rotate = it.getRotate();
    this.setRotate(_rotate);
  }
  
  public String toString() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("ViewportTransform [translateX=");
    double _translateX = this.getTranslateX();
    _builder.append(_translateX, "");
    _builder.append(", translateY=");
    double _translateY = this.getTranslateY();
    _builder.append(_translateY, "");
    _builder.append(", scale=");
    double _scale = this.getScale();
    _builder.append(_scale, "");
    _builder.append(", rotate=");
    double _rotate = this.getRotate();
    _builder.append(_rotate, "");
    _builder.append("]");
    return _builder.toString();
  }
  
  private ReadOnlyDoubleWrapper scaleProperty = new ReadOnlyDoubleWrapper(this, "scale",_initScale());
  
  private static final double _initScale() {
    return 1;
  }
  
  public double getScale() {
    return this.scaleProperty.get();
  }
  
  public ReadOnlyDoubleProperty scaleProperty() {
    return this.scaleProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyDoubleWrapper rotateProperty = new ReadOnlyDoubleWrapper(this, "rotate",_initRotate());
  
  private static final double _initRotate() {
    return 0;
  }
  
  public double getRotate() {
    return this.rotateProperty.get();
  }
  
  public ReadOnlyDoubleProperty rotateProperty() {
    return this.rotateProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyDoubleWrapper translateXProperty = new ReadOnlyDoubleWrapper(this, "translateX");
  
  public double getTranslateX() {
    return this.translateXProperty.get();
  }
  
  public ReadOnlyDoubleProperty translateXProperty() {
    return this.translateXProperty.getReadOnlyProperty();
  }
  
  private ReadOnlyDoubleWrapper translateYProperty = new ReadOnlyDoubleWrapper(this, "translateY");
  
  public double getTranslateY() {
    return this.translateYProperty.get();
  }
  
  public ReadOnlyDoubleProperty translateYProperty() {
    return this.translateYProperty.getReadOnlyProperty();
  }
}
