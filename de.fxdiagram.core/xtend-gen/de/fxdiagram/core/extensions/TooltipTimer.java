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
    this.isRunning = false;
  }
  
  public boolean stop() {
    boolean _isRunning = this.isRunning = false;
    return _isRunning;
  }
  
  public long restart() {
    long _xblockexpression = (long) 0;
    {
      if ((!this.isRunning)) {
        this.isRunning = true;
        Thread _thread = new Thread(this);
        _thread.start();
      }
      long _currentTimeMillis = System.currentTimeMillis();
      Duration _delay = this.tooltip.getDelay();
      double _millis = _delay.toMillis();
      long _plus = (_currentTimeMillis + ((long) _millis));
      long _endTime = this.endTime = _plus;
      _xblockexpression = (_endTime);
    }
    return _xblockexpression;
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
          if ((!this.isRunning)) {
            return;
          }
          long _currentTimeMillis_1 = System.currentTimeMillis();
          long _minus_1 = (this.endTime - _currentTimeMillis_1);
          delay = _minus_1;
        }
        _dowhile = (delay > 0);
      } while(_dowhile);
      if (this.isRunning) {
        final Runnable _function = new Runnable() {
          public void run() {
            TooltipTimer.this.tooltip.trigger();
          }
        };
        Platform.runLater(_function);
      }
      this.isRunning = false;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
