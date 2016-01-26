package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.EmptyTransition;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ConnectionLabelMoveBehavior extends MoveBehavior<XConnectionLabel> {
  private double initialPosition = (-1);
  
  public ConnectionLabelMoveBehavior(final XConnectionLabel label) {
    super(label);
  }
  
  @Override
  protected boolean hasMoved() {
    boolean _xifexpression = false;
    XConnectionLabel _host = this.getHost();
    XConnection _connection = _host.getConnection();
    XConnection.Kind _kind = _connection.getKind();
    boolean _notEquals = (!Objects.equal(_kind, XConnection.Kind.POLYLINE));
    if (_notEquals) {
      _xifexpression = super.hasMoved();
    } else {
      XConnectionLabel _host_1 = this.getHost();
      double _position = _host_1.getPosition();
      _xifexpression = (this.initialPosition != _position);
    }
    return _xifexpression;
  }
  
  @Override
  public void mouseDragged(final MouseEvent it) {
    XConnectionLabel _host = this.getHost();
    final XConnection connection = _host.getConnection();
    XConnection.Kind _kind = connection.getKind();
    boolean _notEquals = (!Objects.equal(_kind, XConnection.Kind.POLYLINE));
    if (_notEquals) {
      super.mouseDragged(it);
    } else {
      final ObservableList<XControlPoint> controlPoints = connection.getControlPoints();
      int _size = controlPoints.size();
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        double _sceneX = it.getSceneX();
        double _sceneY = it.getSceneY();
        final Point2D mouseInLocal = connection.sceneToLocal(_sceneX, _sceneY);
        double currentShortestDistance = Double.MAX_VALUE;
        int currentSegmentIndex = (-1);
        double currentBestK = (-1.0);
        int _size_1 = controlPoints.size();
        int _minus = (_size_1 - 2);
        IntegerRange _upTo = new IntegerRange(0, _minus);
        for (final Integer i : _upTo) {
          {
            XControlPoint _get = controlPoints.get((i).intValue());
            final Point2D segStart = this.toPoint2D(_get);
            XControlPoint _get_1 = controlPoints.get(((i).intValue() + 1));
            final Point2D segEnd = this.toPoint2D(_get_1);
            final Point2D segDirection = Point2DExtensions.operator_minus(segEnd, segStart);
            final double segLength = Point2DExtensions.norm(segDirection);
            if ((segLength > NumberExpressionExtensions.EPSILON)) {
              final Point2D otherDirection = Point2DExtensions.operator_minus(mouseInLocal, segStart);
              double _x = segDirection.getX();
              double _x_1 = otherDirection.getX();
              double _multiply = (_x * _x_1);
              double _y = segDirection.getY();
              double _y_1 = otherDirection.getY();
              double _multiply_1 = (_y * _y_1);
              double _plus = (_multiply + _multiply_1);
              final double lambda = (_plus / (segLength * segLength));
              double _max = Math.max(0, lambda);
              final double boundedLambda = Math.min(_max, 1);
              final Point2D nearestOnSeg = Point2DExtensions.linear(segStart, segEnd, boundedLambda);
              Point2D _minus_1 = Point2DExtensions.operator_minus(nearestOnSeg, mouseInLocal);
              final double dist = Point2DExtensions.norm(_minus_1);
              if ((dist < currentShortestDistance)) {
                currentSegmentIndex = (i).intValue();
                currentShortestDistance = dist;
                int _size_2 = controlPoints.size();
                int _minus_2 = (_size_2 - 1);
                double _divide = ((boundedLambda + (i).intValue()) / _minus_2);
                currentBestK = _divide;
              }
            }
          }
        }
        if ((currentSegmentIndex != (-1))) {
          XConnectionLabel _host_1 = this.getHost();
          _host_1.setPosition(currentBestK);
        }
      }
    }
  }
  
  private Point2D toPoint2D(final XControlPoint it) {
    double _layoutX = it.getLayoutX();
    double _layoutY = it.getLayoutY();
    return new Point2D(_layoutX, _layoutY);
  }
  
  @Override
  public void startDrag(final double screenX, final double screenY) {
    XConnectionLabel _host = this.getHost();
    XConnection _connection = _host.getConnection();
    XConnection.Kind _kind = _connection.getKind();
    boolean _notEquals = (!Objects.equal(_kind, XConnection.Kind.POLYLINE));
    if (_notEquals) {
      super.startDrag(screenX, screenY);
    } else {
      XConnectionLabel _host_1 = this.getHost();
      double _position = _host_1.getPosition();
      this.initialPosition = _position;
    }
  }
  
  @Override
  public boolean getManuallyPlaced() {
    return false;
  }
  
  @Override
  protected AnimationCommand createMoveCommand() {
    abstract class __ConnectionLabelMoveBehavior_1 extends AbstractAnimationCommand {
      final __ConnectionLabelMoveBehavior_1 _this__ConnectionLabelMoveBehavior_1 = this;
      
      double oldPosition;
      
      double newPosition;
    }
    
    AnimationCommand _xifexpression = null;
    XConnectionLabel _host = this.getHost();
    XConnection _connection = _host.getConnection();
    XConnection.Kind _kind = _connection.getKind();
    boolean _notEquals = (!Objects.equal(_kind, XConnection.Kind.POLYLINE));
    if (_notEquals) {
      _xifexpression = super.createMoveCommand();
    } else {
      _xifexpression = new __ConnectionLabelMoveBehavior_1() {
        @Override
        public Animation createExecuteAnimation(final CommandContext context) {
          EmptyTransition _xblockexpression = null;
          {
            this.oldPosition = ConnectionLabelMoveBehavior.this.initialPosition;
            XConnectionLabel _host = ConnectionLabelMoveBehavior.this.getHost();
            double _position = _host.getPosition();
            this.newPosition = _position;
            _xblockexpression = new EmptyTransition();
          }
          return _xblockexpression;
        }
        
        @Override
        public Animation createUndoAnimation(final CommandContext context) {
          Timeline _timeline = new Timeline();
          final Procedure1<Timeline> _function = (Timeline it) -> {
            ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
            Duration _defaultUndoDuration = context.getDefaultUndoDuration();
            XConnectionLabel _host = ConnectionLabelMoveBehavior.this.getHost();
            DoubleProperty _positionProperty = _host.positionProperty();
            KeyValue _keyValue = new <Number>KeyValue(_positionProperty, Double.valueOf(this.oldPosition));
            KeyFrame _keyFrame = new KeyFrame(_defaultUndoDuration, _keyValue);
            _keyFrames.add(_keyFrame);
          };
          return ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
        }
        
        @Override
        public Animation createRedoAnimation(final CommandContext context) {
          Timeline _timeline = new Timeline();
          final Procedure1<Timeline> _function = (Timeline it) -> {
            ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
            Duration _defaultUndoDuration = context.getDefaultUndoDuration();
            XConnectionLabel _host = ConnectionLabelMoveBehavior.this.getHost();
            DoubleProperty _positionProperty = _host.positionProperty();
            KeyValue _keyValue = new <Number>KeyValue(_positionProperty, Double.valueOf(this.newPosition));
            KeyFrame _keyFrame = new KeyFrame(_defaultUndoDuration, _keyValue);
            _keyFrames.add(_keyFrame);
          };
          return ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
        }
      };
    }
    return _xifexpression;
  }
}
