package de.fxdiagram.lib.chooser;

import de.fxdiagram.core.XNode;
import de.fxdiagram.lib.chooser.AbstractBaseChooser;
import java.util.ArrayList;
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
    final EventHandler<ActionEvent> _function_1 = new EventHandler<ActionEvent>() {
      public void handle(final ActionEvent it) {
        tool.setCurrentPosition(ChooserTransition.this.endPosition);
      }
    };
    this.setOnFinished(_function_1);
  }
  
  public void setTargetPositionDelta(final int targetPositionDelta) {
    double _currentPosition = this.tool.getCurrentPosition();
    this.startPosition = _currentPosition;
    this.endPosition = ((int) (this.startPosition + targetPositionDelta));
    while ((this.endPosition < 0)) {
      {
        ArrayList<XNode> _nodes = this.tool.getNodes();
        int _size = _nodes.size();
        double _plus = (this.startPosition + _size);
        this.startPosition = _plus;
        ArrayList<XNode> _nodes_1 = this.tool.getNodes();
        int _size_1 = _nodes_1.size();
        double _plus_1 = (this.endPosition + _size_1);
        this.endPosition = _plus_1;
      }
    }
    this.setDuration(2000);
    this.playFromStart();
  }
  
  public void setTargetPosition(final double targetPosition) {
    ArrayList<XNode> _nodes = this.tool.getNodes();
    int _size = _nodes.size();
    boolean _lessThan = (targetPosition < _size);
    if (_lessThan) {
      double _currentPosition = this.tool.getCurrentPosition();
      this.startPosition = _currentPosition;
      this.endPosition = ((int) targetPosition);
      double _abs = Math.abs((targetPosition - this.startPosition));
      ArrayList<XNode> _nodes_1 = this.tool.getNodes();
      int _size_1 = _nodes_1.size();
      int _divide = (_size_1 / 2);
      boolean _greaterThan = (_abs > _divide);
      if (_greaterThan) {
        if ((targetPosition > this.startPosition)) {
          ArrayList<XNode> _nodes_2 = this.tool.getNodes();
          int _size_2 = _nodes_2.size();
          double _plus = (this.startPosition + _size_2);
          this.startPosition = _plus;
        } else {
          ArrayList<XNode> _nodes_3 = this.tool.getNodes();
          int _size_3 = _nodes_3.size();
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
    ArrayList<XNode> _nodes = this.tool.getNodes();
    int _size = _nodes.size();
    double _modulo = ((this.endPosition - this.startPosition) % _size);
    double _abs = Math.abs(_modulo);
    double _multiply = (_abs * 200);
    final double duration = Math.min(max, _multiply);
    Duration _millis = Duration.millis(duration);
    this.setCycleDuration(_millis);
  }
  
  protected void interpolate(final double alpha) {
    Interpolator _interpolator = this.getInterpolator();
    double _interpolate = _interpolator.interpolate(this.startPosition, this.endPosition, alpha);
    this.tool.setCurrentPosition(_interpolate);
  }
}
