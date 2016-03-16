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
import de.fxdiagram.core.extensions.ConnectionExtensions;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
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
    double _sceneX = it.getSceneX();
    double _sceneY = it.getSceneY();
    final Point2D mouseInLocal = connection.sceneToLocal(_sceneX, _sceneY);
    ObservableList<XControlPoint> _controlPoints = connection.getControlPoints();
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it_1) -> {
      return ConnectionExtensions.toPoint2D(it_1);
    };
    List<Point2D> _map = ListExtensions.<XControlPoint, Point2D>map(_controlPoints, _function);
    XConnection.Kind _kind = connection.getKind();
    final ConnectionExtensions.PointOnCurve nearestPoint = ConnectionExtensions.getNearestPointOnConnection(mouseInLocal, _map, _kind);
    boolean _notEquals = (!Objects.equal(nearestPoint, null));
    if (_notEquals) {
      XConnectionLabel _host_1 = this.getHost();
      double _parameter = nearestPoint.getParameter();
      _host_1.setPosition(_parameter);
    }
  }
  
  @Override
  public void startDrag(final double screenX, final double screenY) {
    XConnectionLabel _host = this.getHost();
    double _position = _host.getPosition();
    this.initialPosition = _position;
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
    
    return new __ConnectionLabelMoveBehavior_1() {
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
}
