package de.fxdiagram.core.extensions;

import de.fxdiagram.core.extensions.SoftTooltip;
import javafx.application.Platform;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class TooltipTimer implements Runnable {
  private SoftTooltip tooltip;
  
  private boolean isRunning;
  
  private long endTime;
  
  public TooltipTimer(final SoftTooltip behavior) {
    this.tooltip = behavior;
    Thread _thread = new Thread(this);
    _thread.start();
    this.isRunning = true;
    this.restart();
  }
  
  public boolean stop() {
    boolean _isRunning = this.isRunning = false;
    return _isRunning;
  }
  
  public long restart() {
    long _currentTimeMillis = System.currentTimeMillis();
    Duration _delay = this.tooltip.getDelay();
    double _millis = _delay.toMillis();
    long _plus = (_currentTimeMillis + ((long) _millis));
    long _endTime = this.endTime = _plus;
    return _endTime;
  }
  
  public void run() {
    try {
      long delay = 0;
      boolean _dowhile = false;
      do {
        {
          long _currentTimeMillis = System.currentTimeMillis();
          long _minus = (this.endTime - _currentTimeMillis);
          Thread.sleep(_minus);
          boolean _not = (!this.isRunning);
          if (_not) {
            return;
          }
          long _currentTimeMillis_1 = System.currentTimeMillis();
          long _minus_1 = (this.endTime - _currentTimeMillis_1);
          delay = _minus_1;
        }
        boolean _greaterThan = (delay > 0);
        _dowhile = _greaterThan;
      } while(_dowhile);
      if (this.isRunning) {
        final Runnable _function = new Runnable() {
          public void run() {
            TooltipTimer.this.tooltip.show();
          }
        };
        Platform.runLater(_function);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
