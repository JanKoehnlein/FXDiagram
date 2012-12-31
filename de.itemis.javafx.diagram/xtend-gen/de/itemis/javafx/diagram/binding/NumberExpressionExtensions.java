package de.itemis.javafx.diagram.binding;

import de.itemis.javafx.diagram.binding.DoubleConstant;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberExpressionBase;
import javafx.beans.value.ObservableNumberValue;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class NumberExpressionExtensions {
  private static double EPSILON = 1e-9;
  
  @Pure
  public static BooleanBinding operator_lessThan(final NumberExpressionBase a, final ObservableNumberValue b) {
    BooleanBinding _lessThan = a.lessThan(b);
    return _lessThan;
  }
  
  @Pure
  public static BooleanBinding operator_lessEqualsThan(final NumberExpressionBase a, final ObservableNumberValue b) {
    BooleanBinding _lessThanOrEqualTo = a.lessThanOrEqualTo(b);
    return _lessThanOrEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_greaterThan(final NumberExpressionBase a, final ObservableNumberValue b) {
    BooleanBinding _greaterThan = a.greaterThan(b);
    return _greaterThan;
  }
  
  @Pure
  public static BooleanBinding operator_greaterEqualsThan(final NumberExpressionBase a, final ObservableNumberValue b) {
    BooleanBinding _greaterThanOrEqualTo = a.greaterThanOrEqualTo(b);
    return _greaterThanOrEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_equals(final NumberExpressionBase a, final ObservableNumberValue b) {
    BooleanBinding _isEqualTo = a.isEqualTo(b);
    return _isEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_notEquals(final NumberExpressionBase a, final ObservableNumberValue b) {
    BooleanBinding _isNotEqualTo = a.isNotEqualTo(b);
    return _isNotEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_lessThan(final double a, final NumberExpressionBase b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    BooleanBinding _lessThan = _doubleConstant.lessThan(b);
    return _lessThan;
  }
  
  @Pure
  public static BooleanBinding operator_lessEqualsThan(final double a, final NumberExpressionBase b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    BooleanBinding _lessThanOrEqualTo = _doubleConstant.lessThanOrEqualTo(b);
    return _lessThanOrEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_greaterThan(final double a, final NumberExpressionBase b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    BooleanBinding _greaterThan = _doubleConstant.greaterThan(b);
    return _greaterThan;
  }
  
  @Pure
  public static BooleanBinding operator_greaterEqualsThan(final double a, final NumberExpressionBase b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    BooleanBinding _greaterThanOrEqualTo = _doubleConstant.greaterThanOrEqualTo(b);
    return _greaterThanOrEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_equals(final double a, final NumberExpressionBase b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    BooleanBinding _isEqualTo = _doubleConstant.isEqualTo(b);
    return _isEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_notEquals(final double a, final NumberExpressionBase b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    BooleanBinding _isNotEqualTo = _doubleConstant.isNotEqualTo(b);
    return _isNotEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_lessThan(final NumberExpressionBase a, final double b) {
    BooleanBinding _lessThan = a.lessThan(b);
    return _lessThan;
  }
  
  @Pure
  public static BooleanBinding operator_lessEqualsThan(final NumberExpressionBase a, final double b) {
    BooleanBinding _lessThanOrEqualTo = a.lessThanOrEqualTo(b);
    return _lessThanOrEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_greaterThan(final NumberExpressionBase a, final double b) {
    BooleanBinding _greaterThan = a.greaterThan(b);
    return _greaterThan;
  }
  
  @Pure
  public static BooleanBinding operator_greaterEqualsThan(final NumberExpressionBase a, final double b) {
    BooleanBinding _greaterThanOrEqualTo = a.greaterThanOrEqualTo(b);
    return _greaterThanOrEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_equals(final NumberExpressionBase a, final double b) {
    BooleanBinding _isEqualTo = a.isEqualTo(b, NumberExpressionExtensions.EPSILON);
    return _isEqualTo;
  }
  
  @Pure
  public static BooleanBinding operator_notEquals(final NumberExpressionBase a, final double b) {
    BooleanBinding _isNotEqualTo = a.isNotEqualTo(b, NumberExpressionExtensions.EPSILON);
    return _isNotEqualTo;
  }
}
