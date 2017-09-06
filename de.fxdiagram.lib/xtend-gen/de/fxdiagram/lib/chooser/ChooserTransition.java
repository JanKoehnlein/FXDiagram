package de.fxdiagram.lib.chooser;

import de.fxdiagram.lib.chooser.AbstractBaseChooser;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

@SuppressWarnings("all")
public class ChooserTransition extends Transition {
  private AbstractBaseChooser tool;
  
  private double startPosition;
  
  private double endPosition;
  
  public ChooserTransition(final AbstractBaseChooser tool) {
    this.tool = tool;
    final Interpolator _function = new Interpolator() {
      @Override
      protected double curve(final double alpha) {
        return (1 - ((1 - alpha) * (1 - alpha)));
      }
    };
    this.setInterpolator(_function);
    final EventHandler<ActionEvent> _function_1 = (ActionEvent it) -> {
      tool.setCurrentPosition(this.endPosition);
    };
    this.setOnFinished(_function_1);
  }
  
  public void setTargetPositionDelta(final int targetPositionDelta) {
    this.startPosition = this.tool.getCurrentPosition();
    this.endPosition = ((int) (this.startPosition + targetPositionDelta));
    while ((this.endPosition < 0)) {
      {
        int _size = this.tool.getNodes().size();
        double _plus = (this.startPosition + _size);
        this.startPosition = _plus;
        int _size_1 = this.tool.getNodes().size();
        double _plus_1 = (this.endPosition + _size_1);
        this.endPosition = _plus_1;
      }
    }
    this.setDuration(2000);
    this.playFromStart();
  }
  
  public void setTargetPosition(final double targetPosition) {
    int _size = this.tool.getNodes().size();
    boolean _lessThan = (targetPosition < _size);
    if (_lessThan) {
      this.startPosition = this.tool.getCurrentPosition();
      this.endPosition = ((int) targetPosition);
      double _abs = Math.abs((targetPosition - this.startPosition));
      int _size_1 = this.tool.getNodes().size();
      int _divide = (_size_1 / 2);
      boolean _greaterThan = (_abs > _divide);
      if (_greaterThan) {
        if ((targetPosition > this.startPosition)) {
          int _size_2 = this.tool.getNodes().size();
          double _plus = (this.startPosition + _size_2);
          this.startPosition = _plus;
        } else {
          int _size_3 = this.tool.getNodes().size();
          double _plus_1 = (this.endPosition + _size_3);
          this.endPosition = _plus_1;
        }
      }
      this.setDuration(1000);
      this.playFromStart();
    }
  }
  
  public void resetTargetPosition() {
    this.setTargetPosition(this.endPosition);
  }
  
  protected void setDuration(final double max) {
    int _size = this.tool.getNodes().size();
    double _modulo = ((this.endPosition - this.startPosition) % _size);
    double _abs = Math.abs(_modulo);
    double _multiply = (_abs * 200);
    final double duration = Math.min(max, _multiply);
    this.setCycleDuration(Duration.millis(duration));
  }
  
  @Override
  protected void interpolate(final double alpha) {
    this.tool.setCurrentPosition(this.getInterpolator().interpolate(this.startPosition, this.endPosition, alpha));
  }
}
