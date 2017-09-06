package de.fxdiagram.core.command;

import de.fxdiagram.core.extensions.DurationExtensions;
import javafx.animation.Transition;

@SuppressWarnings("all")
public class EmptyTransition extends Transition {
  public EmptyTransition() {
    super.setCycleDuration(DurationExtensions.millis(0));
  }
  
  @Override
  protected void interpolate(final double frac) {
  }
}
