package de.fxdiagram.core.extensions;

import com.google.common.base.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;

@SuppressWarnings("all")
public class StringExpressionExtensions {
  public static StringExpression operator_plus(final StringExpression left, final String right) {
    return Bindings.concat(left, right);
  }
  
  public static StringExpression operator_plus(final String left, final StringExpression right) {
    return Bindings.concat(left, right);
  }
  
  public static StringExpression operator_plus(final StringExpression left, final Object right) {
    return Bindings.concat(left, right);
  }
  
  public static StringExpression operator_plus(final Object left, final StringExpression right) {
    return Bindings.concat(left, right);
  }
  
  public static StringExpression operator_plus(final StringExpression left, final StringExpression right) {
    return Bindings.concat(left, right);
  }
  
  public static boolean operator_equals(final StringExpression left, final StringExpression right) {
    String _value = left.getValue();
    String _value_1 = right.getValue();
    boolean _equals = Objects.equal(_value, _value_1);
    return _equals;
  }
  
  public static boolean operator_equals(final StringExpression left, final String right) {
    String _value = left.getValue();
    boolean _equals = Objects.equal(_value, right);
    return _equals;
  }
  
  public static boolean operator_notEquals(final StringExpression left, final StringExpression right) {
    String _value = left.getValue();
    String _value_1 = right.getValue();
    boolean _notEquals = (!Objects.equal(_value, _value_1));
    return _notEquals;
  }
  
  public static boolean operator_notEquals(final StringExpression left, final String right) {
    String _value = left.getValue();
    boolean _notEquals = (!Objects.equal(_value, right));
    return _notEquals;
  }
}
