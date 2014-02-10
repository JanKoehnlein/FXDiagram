package de.fxdiagram.core.extensions;

import javafx.util.Duration;

@SuppressWarnings("all")
public class DurationExtensions {
  public static Duration hours(final double hours) {
    return Duration.hours(hours);
  }
  
  public static Duration minutes(final double minutes) {
    return Duration.minutes(minutes);
  }
  
  public static Duration seconds(final double seconds) {
    return Duration.seconds(seconds);
  }
  
  public static Duration millis(final double millis) {
    return Duration.millis(millis);
  }
  
  public static Duration operator_plus(final Duration left, final Duration right) {
    return left.add(right);
  }
  
  public static Duration operator_minus(final Duration left, final Duration right) {
    return left.subtract(right);
  }
  
  public static Duration operator_multiply(final Duration left, final double right) {
    return left.multiply(right);
  }
  
  public static Duration operator_multiply(final double left, final Duration right) {
    return right.multiply(left);
  }
  
  public static Duration operator_divide(final Duration left, final double right) {
    return left.divide(right);
  }
  
  public static Duration operator_divide(final double left, final Duration right) {
    return right.divide(left);
  }
}
