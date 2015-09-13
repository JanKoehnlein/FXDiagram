package de.fxdiagram.core.command;

import de.fxdiagram.core.extensions.DurationExtensions;
import javafx.animation.Transition;
import javafx.util.Duration;

@SuppressWarnings("all")
public class EmptyTransition extends Transition {
  public EmptyTransition() {
    Duration _millis = DurationExtensions.millis(0);
    super.setCycleDuration(_millis);
  }
  
  @Override
  protected void interpolate(final double frac) {
  }
}
