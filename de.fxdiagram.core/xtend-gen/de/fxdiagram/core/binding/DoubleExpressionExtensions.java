package de.fxdiagram.core.binding;

import de.fxdiagram.core.binding.DoubleConstant;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ObservableNumberValue;
import org.eclipse.xtext.xbase.lib.Pure;

@SuppressWarnings("all")
public class DoubleExpressionExtensions {
  @Pure
  public static DoubleBinding operator_plus(final DoubleExpression a, final ObservableNumberValue b) {
    DoubleBinding _add = a.add(b);
    return _add;
  }
  
  @Pure
  public static DoubleBinding operator_minus(final DoubleExpression a) {
    DoubleBinding _negate = a.negate();
    return _negate;
  }
  
  @Pure
  public static DoubleBinding operator_minus(final DoubleExpression a, final ObservableNumberValue b) {
    DoubleBinding _subtract = a.subtract(b);
    return _subtract;
  }
  
  @Pure
  public static DoubleBinding operator_multiply(final DoubleExpression a, final ObservableNumberValue b) {
    DoubleBinding _multiply = a.multiply(b);
    return _multiply;
  }
  
  @Pure
  public static DoubleBinding operator_divide(final DoubleExpression a, final ObservableNumberValue b) {
    DoubleBinding _divide = a.divide(b);
    return _divide;
  }
  
  @Pure
  public static DoubleBinding operator_plus(final double a, final DoubleExpression b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    DoubleBinding _add = _doubleConstant.add(b);
    return _add;
  }
  
  @Pure
  public static DoubleBinding operator_minus(final double a, final DoubleExpression b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    DoubleBinding _subtract = _doubleConstant.subtract(b);
    return _subtract;
  }
  
  @Pure
  public static DoubleBinding operator_multiply(final double a, final DoubleExpression b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    DoubleBinding _multiply = _doubleConstant.multiply(b);
    return _multiply;
  }
  
  @Pure
  public static DoubleBinding operator_divide(final double a, final DoubleExpression b) {
    DoubleConstant _doubleConstant = new DoubleConstant(a);
    DoubleBinding _divide = _doubleConstant.divide(b);
    return _divide;
  }
  
  @Pure
  public static DoubleBinding operator_plus(final DoubleExpression a, final double b) {
    DoubleBinding _add = a.add(b);
    return _add;
  }
  
  @Pure
  public static DoubleBinding operator_minus(final DoubleExpression a, final double b) {
    DoubleBinding _subtract = a.subtract(b);
    return _subtract;
  }
  
  @Pure
  public static DoubleBinding operator_multiply(final DoubleExpression a, final double b) {
    DoubleBinding _multiply = a.multiply(b);
    return _multiply;
  }
  
  @Pure
  public static DoubleBinding operator_divide(final DoubleExpression a, final double b) {
    DoubleBinding _divide = a.divide(b);
    return _divide;
  }
}
