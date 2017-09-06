package de.fxdiagram.core.extensions;

import javafx.application.Platform;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class TimerExtensions {
  public static void defer(final Runnable runnable, final Duration time) {
    final Runnable _function = () -> {
      try {
        double _millis = time.toMillis();
        Thread.sleep(((long) _millis));
        Platform.runLater(runnable);
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    new Thread(_function).start();
  }
}
