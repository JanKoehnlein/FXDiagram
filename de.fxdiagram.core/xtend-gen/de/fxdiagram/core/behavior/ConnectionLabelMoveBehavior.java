package de.fxdiagram.core.behavior;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.command.EmptyTransition;
import de.fxdiagram.core.extensions.BezierExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
    XConnection.Kind _kind = connection.getKind();
    if (_kind != null) {
      switch (_kind) {
        case POLYLINE:
          this.dragOnPolyline(mouseInLocal, connection);
          break;
        case QUAD_CURVE:
          final Function2<QuadCurve, Double, Point2D> _function = (QuadCurve $0, Double $1) -> {
            return BezierExtensions.at($0, ($1).doubleValue());
          };
          this.<QuadCurve>dragOnSpline(mouseInLocal, connection, QuadCurve.class, _function);
          break;
        case CUBIC_CURVE:
          final Function2<CubicCurve, Double, Point2D> _function_1 = (CubicCurve $0, Double $1) -> {
            return BezierExtensions.at($0, ($1).doubleValue());
          };
          this.<CubicCurve>dragOnSpline(mouseInLocal, connection, CubicCurve.class, _function_1);
          break;
        default:
          super.mouseDragged(it);
          break;
      }
    } else {
      super.mouseDragged(it);
    }
  }
  
  protected void dragOnPolyline(final Point2D mouseInLocal, final XConnection connection) {
    final ObservableList<XControlPoint> controlPoints = connection.getControlPoints();
    int _size = controlPoints.size();
    boolean _greaterThan = (_size > 1);
    if (_greaterThan) {
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
        XConnectionLabel _host = this.getHost();
        _host.setPosition(currentBestK);
      }
    }
  }
  
  protected <T extends Shape> void dragOnSpline(final Point2D mouseInLocal, final XConnection connection, final Class<T> curveType, final Function2<? super T, ? super Double, ? extends Point2D> at) {
    Node _node = connection.getNode();
    ObservableList<Node> _children = ((Group) _node).getChildren();
    final Iterable<T> curves = Iterables.<T>filter(_children, curveType);
    double currentBestDist = Double.MAX_VALUE;
    double currentBestK = (-1.0);
    int _size = IterableExtensions.size(curves);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        double left = 0.0;
        double right = 1.0;
        final T curve = ((T[])Conversions.unwrapArray(curves, Shape.class))[(i).intValue()];
        Point2D _apply = at.apply(curve, Double.valueOf(left));
        Point2D _minus = Point2DExtensions.operator_minus(_apply, mouseInLocal);
        double currentDistLeft = Point2DExtensions.norm(_minus);
        Point2D _apply_1 = at.apply(curve, Double.valueOf(right));
        Point2D _minus_1 = Point2DExtensions.operator_minus(_apply_1, mouseInLocal);
        double currentDistRight = Point2DExtensions.norm(_minus_1);
        while (((right - left) > NumberExpressionExtensions.EPSILON)) {
          {
            final double mid = ((left + right) / 2);
            Point2D _apply_2 = at.apply(curve, Double.valueOf(mid));
            Point2D _minus_2 = Point2DExtensions.operator_minus(_apply_2, mouseInLocal);
            final double distMid = Point2DExtensions.norm(_minus_2);
            if ((currentDistRight < currentDistLeft)) {
              left = mid;
              currentDistLeft = distMid;
            } else {
              right = mid;
              currentDistRight = distMid;
            }
          }
        }
        if ((currentDistLeft < currentBestDist)) {
          currentBestDist = currentDistLeft;
          int _size_1 = IterableExtensions.size(curves);
          double _divide = ((left + (i).intValue()) / _size_1);
          currentBestK = _divide;
        }
        XConnectionLabel _host = this.getHost();
        _host.setPosition(currentBestK);
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
