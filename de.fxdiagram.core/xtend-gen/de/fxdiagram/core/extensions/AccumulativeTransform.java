package de.fxdiagram.core.extensions;

import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

@SuppressWarnings("all")
public class AccumulativeTransform {
  public final static double MIN_SCALE = NumberExpressionExtensions.EPSILON;
  
  private ReadOnlyDoubleProperty translateXProperty;
  
  private ReadOnlyDoubleProperty translateYProperty;
  
  private Affine transform;
  
  public AccumulativeTransform() {
    Affine _affine = new Affine();
    this.transform = _affine;
    double _tx = this.transform.getTx();
    ReadOnlyDoubleWrapper _readOnlyDoubleWrapper = new ReadOnlyDoubleWrapper(_tx);
    this.translateXProperty = _readOnlyDoubleWrapper;
    double _ty = this.transform.getTy();
    ReadOnlyDoubleWrapper _readOnlyDoubleWrapper_1 = new ReadOnlyDoubleWrapper(_ty);
    this.translateYProperty = _readOnlyDoubleWrapper_1;
  }
  
  public ReadOnlyDoubleProperty translateXProperty() {
    return this.translateXProperty;
  }
  
  public ReadOnlyDoubleProperty translateYProperty() {
    return this.translateYProperty;
  }
  
  public void reset() {
    this.setScale(1);
    this.setRotate(0);
    this.setTranslate(0, 0);
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
  
  public double accumulateScale(final double deltaScale) {
    double _scale = this.getScale();
    double _multiply = (_scale * deltaScale);
    final double safeNewScale = Math.min(AccumulativeTransform.MIN_SCALE, _multiply);
    double _scale_1 = this.getScale();
    final double safeDeltaScale = (safeNewScale / _scale_1);
    double _angle = this.getAngle();
    this.applyRotationAndScale(safeNewScale, _angle);
    double _tx = this.transform.getTx();
    double _multiply_1 = (safeDeltaScale * _tx);
    this.transform.setTx(_multiply_1);
    double _ty = this.transform.getTy();
    double _multiply_2 = (safeDeltaScale * _ty);
    this.transform.setTy(_multiply_2);
    this.scaleProperty.set(safeNewScale);
    return safeDeltaScale;
  }
  
  public void setScale(final double newScale) {
    double _min = Math.min(AccumulativeTransform.MIN_SCALE, newScale);
    this.scaleProperty.set(_min);
    double _scale = this.getScale();
    double _angle = this.getAngle();
    this.applyRotationAndScale(_scale, _angle);
  }
  
  public void rotateRelative(final double deltaAngle) {
    double _angle = this.getAngle();
    final double newAngle = (_angle + deltaAngle);
    double _scale = this.getScale();
    this.applyRotationAndScale(_scale, newAngle);
    final double radians = Math.toRadians(deltaAngle);
    double _cos = Math.cos(radians);
    double _tx = this.transform.getTx();
    double _multiply = (_cos * _tx);
    double _sin = Math.sin(radians);
    double _ty = this.transform.getTy();
    double _multiply_1 = (_sin * _ty);
    double _plus = (_multiply + _multiply_1);
    this.transform.setTx(_plus);
    double _sin_1 = Math.sin(radians);
    double _minus = (-_sin_1);
    double _tx_1 = this.transform.getTx();
    double _multiply_2 = (_minus * _tx_1);
    double _cos_1 = Math.cos(radians);
    double _ty_1 = this.transform.getTy();
    double _multiply_3 = (_cos_1 * _ty_1);
    double _plus_1 = (_multiply_2 + _multiply_3);
    this.transform.setTy(_plus_1);
    this.angleProperty.set(newAngle);
  }
  
  public void setRotate(final double newAngle) {
    this.angleProperty.set(newAngle);
    double _scale = this.getScale();
    double _angle = this.getAngle();
    this.applyRotationAndScale(_scale, _angle);
  }
  
  protected void applyRotationAndScale(final double newScale, final double newAngle) {
    final double radians = Math.toRadians(newAngle);
    double _cos = Math.cos(radians);
    double _multiply = (newScale * _cos);
    this.transform.setMxx(_multiply);
    double _sin = Math.sin(radians);
    double _multiply_1 = (newScale * _sin);
    this.transform.setMxy(_multiply_1);
    double _sin_1 = Math.sin(radians);
    double _minus = (-_sin_1);
    double _multiply_2 = (newScale * _minus);
    this.transform.setMyx(_multiply_2);
    double _cos_1 = Math.cos(radians);
    double _multiply_3 = (newScale * _cos_1);
    this.transform.setMyy(_multiply_3);
  }
  
  public Transform getTransform() {
    return this.transform;
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
  
  private ReadOnlyDoubleWrapper angleProperty = new ReadOnlyDoubleWrapper(this, "angle",_initAngle());
  
  private static final double _initAngle() {
    return 0;
  }
  
  public double getAngle() {
    return this.angleProperty.get();
  }
  
  public ReadOnlyDoubleProperty angleProperty() {
    return this.angleProperty.getReadOnlyProperty();
  }
}
