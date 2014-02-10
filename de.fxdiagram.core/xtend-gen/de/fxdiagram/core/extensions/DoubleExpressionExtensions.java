package de.fxdiagram.core.extensions;

import de.fxdiagram.core.extensions.DoubleConstant;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ObservableNumberValue;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class DoubleExpressionExtensions {
  @Pure
  public static DoubleBinding operator_plus(final DoubleExpression a, final ObservableNumberValue b) {
    return a.add(b);
  }
  
  @Pure
  public static DoubleBinding operator_minus(final DoubleExpression a) {
    return a.negate();
  }
  
  @Pure
  public static DoubleBinding operator_minus(final DoubleExpression a, final ObservableNumberValue b) {
    return a.subtract(b);
  }
  
  @Pure
  public static DoubleBinding operator_multiply(final DoubleExpression a, final ObservableNumberValue b) {
    return a.multiply(b);
  }
  
  @Pure
  public static DoubleBinding operator_divide(final DoubleExpression a, final ObservableNumberValue b) {
    return a.divide(b);
  }
  
  @Pure
  public static DoubleBinding operator_plus(final double a, final DoubleExpression b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    return _doubleConstant.add(b);
  }
  
  @Pure
  public static DoubleBinding operator_minus(final double a, final DoubleExpression b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    return _doubleConstant.subtract(b);
  }
  
  @Pure
  public static DoubleBinding operator_multiply(final double a, final DoubleExpression b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    return _doubleConstant.multiply(b);
  }
  
  @Pure
  public static DoubleBinding operator_divide(final double a, final DoubleExpression b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    return _doubleConstant.divide(b);
  }
  
  @Pure
  public static DoubleBinding operator_plus(final DoubleExpression a, final double b) {
    return a.add(b);
  }
  
  @Pure
  public static DoubleBinding operator_minus(final DoubleExpression a, final double b) {
    return a.subtract(b);
  }
  
  @Pure
  public static DoubleBinding operator_multiply(final DoubleExpression a, final double b) {
    return a.multiply(b);
  }
  
  @Pure
  public static DoubleBinding operator_divide(final DoubleExpression a, final double b) {
    return a.divide(b);
  }
}
