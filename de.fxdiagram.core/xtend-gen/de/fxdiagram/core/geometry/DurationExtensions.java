package de.fxdiagram.core.geometry;

import javafx.util.Duration;

@SuppressWarnings("all")
public class DurationExtensions {
  public static Duration hours(final double hours) {
    Duration _hours = Duration.hours(hours);
    return _hours;
  }
  
  public static Duration minutes(final double minutes) {
    Duration _minutes = Duration.minutes(minutes);
    return _minutes;
  }
  
  public static Duration seconds(final double seconds) {
    Duration _seconds = Duration.seconds(seconds);
    return _seconds;
  }
  
  public static Duration millis(final double millis) {
    Duration _millis = Duration.millis(millis);
    return _millis;
  }
  
  public static Duration operator_plus(final Duration left, final Duration right) {
    Duration _add = left.add(right);
    return _add;
  }
  
  public static Duration operator_minus(final Duration left, final Duration right) {
    Duration _subtract = left.subtract(right);
    return _subtract;
  }
  
  public static Duration operator_multiply(final Duration left, final double right) {
    Duration _multiply = left.multiply(right);
    return _multiply;
  }
  
  public static Duration operator_multiply(final double left, final Duration right) {
    Duration _multiply = right.multiply(left);
    return _multiply;
  }
  
  public static Duration operator_divide(final Duration left, final double right) {
    Duration _divide = left.divide(right);
    return _divide;
  }
  
  public static Duration operator_divide(final double left, final Duration right) {
    Duration _divide = right.divide(left);
    return _divide;
  }
}
