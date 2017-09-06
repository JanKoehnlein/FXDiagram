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
  public boolean hasMoved() {
    boolean _switchResult = false;
    XConnection.Kind _kind = this.getHost().getConnection().getKind();
    if (_kind != null) {
      switch (_kind) {
        case POLYLINE:
        case RECTILINEAR:
          double _position = this.getHost().getPosition();
          _switchResult = (this.initialPosition != _position);
          break;
        default:
          _switchResult = super.hasMoved();
          break;
      }
    } else {
      _switchResult = super.hasMoved();
    }
    return _switchResult;
  }
  
  @Override
  public void mouseDragged(final MouseEvent it) {
    final XConnection connection = this.getHost().getConnection();
    final Point2D mouseInLocal = connection.sceneToLocal(it.getSceneX(), it.getSceneY());
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it_1) -> {
      return ConnectionExtensions.toPoint2D(it_1);
    };
    final ConnectionExtensions.PointOnCurve nearestPoint = ConnectionExtensions.getNearestPointOnConnection(mouseInLocal, ListExtensions.<XControlPoint, Point2D>map(connection.getControlPoints(), _function), connection.getKind());
    boolean _notEquals = (!Objects.equal(nearestPoint, null));
    if (_notEquals) {
      XConnectionLabel _host = this.getHost();
      _host.setPosition(nearestPoint.getParameter());
    }
  }
  
  @Override
  public void startDrag(final double screenX, final double screenY) {
    this.initialPosition = this.getHost().getPosition();
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
          this.newPosition = ConnectionLabelMoveBehavior.this.getHost().getPosition();
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
          DoubleProperty _positionProperty = ConnectionLabelMoveBehavior.this.getHost().positionProperty();
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
          DoubleProperty _positionProperty = ConnectionLabelMoveBehavior.this.getHost().positionProperty();
          KeyValue _keyValue = new <Number>KeyValue(_positionProperty, Double.valueOf(this.newPosition));
          KeyFrame _keyFrame = new KeyFrame(_defaultUndoDuration, _keyValue);
          _keyFrames.add(_keyFrame);
        };
        return ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function);
      }
    };
  }
}
