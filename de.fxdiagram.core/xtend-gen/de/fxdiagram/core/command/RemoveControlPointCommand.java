package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.ConnectionExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.DurationExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Will not remove anchor points.
 */
@FinalFieldsConstructor
@SuppressWarnings("all")
public class RemoveControlPointCommand extends AbstractAnimationCommand {
  private final XConnection connection;
  
  private final List<XControlPoint> selectedPoints;
  
  private XConnection.Kind fromKind;
  
  private XConnection.Kind toKind;
  
  private List<Point2D> fromPoints;
  
  private List<Point2D> toPoints;
  
  private Map<Integer, XControlPoint> removedPoints;
  
  protected Map<Integer, XControlPoint> initialize() {
    Map<Integer, XControlPoint> _xblockexpression = null;
    {
      XConnection.Kind _kind = this.connection.getKind();
      this.fromKind = _kind;
      ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
      final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
        double _layoutX = it.getLayoutX();
        double _layoutY = it.getLayoutY();
        return new Point2D(_layoutX, _layoutY);
      };
      List<Point2D> _map = ListExtensions.<XControlPoint, Point2D>map(_controlPoints, _function);
      ArrayList<Point2D> _newArrayList = CollectionLiterals.<Point2D>newArrayList(((Point2D[])Conversions.unwrapArray(_map, Point2D.class)));
      this.fromPoints = _newArrayList;
      final Set<XControlPoint> removePoints = this.calculateControlPointsToRemove(this.selectedPoints);
      final ObservableList<XControlPoint> controlPoints = this.connection.getControlPoints();
      int _size = controlPoints.size();
      int _size_1 = removePoints.size();
      int _minus = (_size - _size_1);
      boolean _equals = (_minus == 2);
      if (_equals) {
        this.toKind = XConnection.Kind.POLYLINE;
      } else {
        XConnection.Kind _kind_1 = this.connection.getKind();
        this.toKind = _kind_1;
      }
      ArrayList<Point2D> _calculateToPoints = this.calculateToPoints(removePoints);
      this.toPoints = _calculateToPoints;
      final Function1<XControlPoint, Integer> _function_1 = (XControlPoint it) -> {
        return Integer.valueOf(controlPoints.indexOf(it));
      };
      Map<Integer, XControlPoint> _map_1 = IterableExtensions.<Integer, XControlPoint>toMap(removePoints, _function_1);
      _xblockexpression = this.removedPoints = _map_1;
    }
    return _xblockexpression;
  }
  
  protected Set<XControlPoint> calculateControlPointsToRemove(final List<XControlPoint> selectedPoints) {
    final ObservableList<XControlPoint> controlPoints = this.connection.getControlPoints();
    final Set<XControlPoint> removePoints = IterableExtensions.<XControlPoint>toSet(selectedPoints);
    final XConnection.Kind fromKind = this.fromKind;
    if (fromKind != null) {
      switch (fromKind) {
        case QUAD_CURVE:
          final Consumer<XControlPoint> _function = (XControlPoint it) -> {
            final int i = controlPoints.indexOf(it);
            int _size = controlPoints.size();
            int _minus = (_size - 2);
            boolean _lessThan = (i < _minus);
            if (_lessThan) {
              XControlPoint _get = controlPoints.get((i + 1));
              removePoints.add(_get);
            } else {
              XControlPoint _get_1 = controlPoints.get((i - 1));
              removePoints.add(_get_1);
            }
          };
          selectedPoints.forEach(_function);
          break;
        case CUBIC_CURVE:
          final Consumer<XControlPoint> _function_1 = (XControlPoint it) -> {
            final int i = controlPoints.indexOf(it);
            int _size = controlPoints.size();
            int _minus = (_size - 3);
            boolean _lessThan = (i < _minus);
            if (_lessThan) {
              XControlPoint _get = controlPoints.get((i + 1));
              removePoints.add(_get);
              XControlPoint _get_1 = controlPoints.get((i + 2));
              removePoints.add(_get_1);
            } else {
              int _size_1 = controlPoints.size();
              int _minus_1 = (_size_1 - 2);
              XControlPoint _get_2 = controlPoints.get(_minus_1);
              removePoints.add(_get_2);
              int _size_2 = controlPoints.size();
              int _minus_2 = (_size_2 - 3);
              XControlPoint _get_3 = controlPoints.get(_minus_2);
              removePoints.add(_get_3);
              int _size_3 = controlPoints.size();
              int _minus_3 = (_size_3 - 4);
              XControlPoint _get_4 = controlPoints.get(_minus_3);
              removePoints.add(_get_4);
            }
          };
          selectedPoints.forEach(_function_1);
          break;
        default:
          break;
      }
    }
    XControlPoint _head = IterableExtensions.<XControlPoint>head(controlPoints);
    removePoints.remove(_head);
    XControlPoint _last = IterableExtensions.<XControlPoint>last(controlPoints);
    removePoints.remove(_last);
    return removePoints;
  }
  
  protected ArrayList<Point2D> calculateToPoints(final Set<XControlPoint> removePoints) {
    final ObservableList<XControlPoint> controlPoints = this.connection.getControlPoints();
    final ArrayList<Point2D> toPoints = CollectionLiterals.<Point2D>newArrayList();
    XControlPoint lastRemaining = null;
    int segmentRemoveCount = 0;
    int _size = controlPoints.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final XControlPoint controlPoint = controlPoints.get((i).intValue());
        boolean _contains = removePoints.contains(controlPoint);
        if (_contains) {
          segmentRemoveCount++;
        } else {
          if ((segmentRemoveCount > 0)) {
            boolean _or = false;
            XControlPoint _head = IterableExtensions.<XControlPoint>head(controlPoints);
            boolean _notEquals = (!Objects.equal(lastRemaining, _head));
            if (_notEquals) {
              _or = true;
            } else {
              XControlPoint _last = IterableExtensions.<XControlPoint>last(controlPoints);
              boolean _notEquals_1 = (!Objects.equal(controlPoint, _last));
              _or = _notEquals_1;
            }
            final boolean hasRemainingControlPoints = _or;
            Point2D _xifexpression = null;
            XControlPoint _head_1 = IterableExtensions.<XControlPoint>head(controlPoints);
            boolean _equals = Objects.equal(lastRemaining, _head_1);
            if (_equals) {
              Point2D _xblockexpression = null;
              {
                Point2D _xifexpression_1 = null;
                if (hasRemainingControlPoints) {
                  double _layoutX = controlPoint.getLayoutX();
                  double _layoutY = controlPoint.getLayoutY();
                  _xifexpression_1 = new Point2D(_layoutX, _layoutY);
                } else {
                  XNode _target = this.connection.getTarget();
                  _xifexpression_1 = this.midPoint(_target);
                }
                final Point2D anchorRefPoint = _xifexpression_1;
                ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
                XNode _source = this.connection.getSource();
                ArrowHead _sourceArrowHead = this.connection.getSourceArrowHead();
                _xblockexpression = _connectionRouter.getNearestAnchor(_source, anchorRefPoint, _sourceArrowHead);
              }
              _xifexpression = _xblockexpression;
            } else {
              double _layoutX = lastRemaining.getLayoutX();
              double _layoutY = lastRemaining.getLayoutY();
              _xifexpression = new Point2D(_layoutX, _layoutY);
            }
            final Point2D segmentStart = _xifexpression;
            Point2D _xifexpression_1 = null;
            XControlPoint _last_1 = IterableExtensions.<XControlPoint>last(controlPoints);
            boolean _equals_1 = Objects.equal(controlPoint, _last_1);
            if (_equals_1) {
              Point2D _xblockexpression_1 = null;
              {
                Point2D _xifexpression_2 = null;
                if (hasRemainingControlPoints) {
                  double _layoutX_1 = lastRemaining.getLayoutX();
                  double _layoutY_1 = lastRemaining.getLayoutY();
                  _xifexpression_2 = new Point2D(_layoutX_1, _layoutY_1);
                } else {
                  XNode _source = this.connection.getSource();
                  _xifexpression_2 = this.midPoint(_source);
                }
                final Point2D anchorRefPoint = _xifexpression_2;
                ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
                XNode _target = this.connection.getTarget();
                ArrowHead _targetArrowHead = this.connection.getTargetArrowHead();
                _xblockexpression_1 = _connectionRouter.getNearestAnchor(_target, anchorRefPoint, _targetArrowHead);
              }
              _xifexpression_1 = _xblockexpression_1;
            } else {
              double _layoutX_1 = controlPoint.getLayoutX();
              double _layoutY_1 = controlPoint.getLayoutY();
              _xifexpression_1 = new Point2D(_layoutX_1, _layoutY_1);
            }
            final Point2D segmentEnd = _xifexpression_1;
            IntegerRange _upTo = new IntegerRange(1, segmentRemoveCount);
            for (final Integer j : _upTo) {
              boolean _equals_2 = Objects.equal(this.toKind, XConnection.Kind.POLYLINE);
              if (_equals_2) {
                Point2D _linear = Point2DExtensions.linear(segmentStart, segmentEnd, 
                  (((double) (j).intValue()) / (segmentRemoveCount + 1)));
                toPoints.add(_linear);
              } else {
                XControlPoint.Type _type = controlPoint.getType();
                boolean _notEquals_2 = (!Objects.equal(_type, XControlPoint.Type.CONTROL_POINT));
                if (_notEquals_2) {
                  toPoints.add(segmentEnd);
                } else {
                  toPoints.add(segmentStart);
                }
              }
            }
            segmentRemoveCount = 0;
          }
          lastRemaining = controlPoint;
          double _layoutX_2 = controlPoint.getLayoutX();
          double _layoutY_2 = controlPoint.getLayoutY();
          Point2D _point2D = new Point2D(_layoutX_2, _layoutY_2);
          toPoints.add(_point2D);
        }
      }
    }
    return toPoints;
  }
  
  protected Point2D midPoint(final XNode node) {
    Node _node = node.getNode();
    Bounds _boundsInLocal = _node.getBoundsInLocal();
    Point2D _center = BoundsExtensions.center(_boundsInLocal);
    return CoreExtensions.localToRootDiagram(node, _center);
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    ParallelTransition _xblockexpression = null;
    {
      this.initialize();
      Map<Integer, XControlPoint> _emptyMap = CollectionLiterals.<Integer, XControlPoint>emptyMap();
      Duration _executeDuration = this.getExecuteDuration(context);
      _xblockexpression = this.createMorphTransition(this.fromPoints, this.toKind, this.toPoints, _emptyMap, this.removedPoints, _executeDuration);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    Map<Integer, XControlPoint> _emptyMap = CollectionLiterals.<Integer, XControlPoint>emptyMap();
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMorphTransition(this.toPoints, this.fromKind, this.fromPoints, 
      this.removedPoints, _emptyMap, _defaultUndoDuration);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    Map<Integer, XControlPoint> _emptyMap = CollectionLiterals.<Integer, XControlPoint>emptyMap();
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMorphTransition(this.fromPoints, this.toKind, this.toPoints, _emptyMap, this.removedPoints, _defaultUndoDuration);
  }
  
  protected ParallelTransition createMorphTransition(final List<Point2D> from, final XConnection.Kind toKind, final List<Point2D> to, final Map<Integer, XControlPoint> addBefore, final Map<Integer, XControlPoint> removeAfter, final Duration duration) {
    final ParallelTransition morph = new ParallelTransition();
    final HashMap<XConnectionLabel, Point2D> label2position = CollectionLiterals.<XConnectionLabel, Point2D>newHashMap();
    ObservableList<XConnectionLabel> _labels = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
      double _position = it.getPosition();
      Point2D _at = this.connection.at(_position);
      label2position.put(it, _at);
    };
    _labels.forEach(_function);
    ObservableList<XConnectionLabel> _labels_1 = this.connection.getLabels();
    final Consumer<XConnectionLabel> _function_1 = (XConnectionLabel label) -> {
      Point2D _get = label2position.get(label);
      final ConnectionExtensions.PointOnCurve startPointOnCurve = ConnectionExtensions.getNearestPointOnConnection(_get, from, this.fromKind);
      Point2D _get_1 = label2position.get(label);
      final ConnectionExtensions.PointOnCurve endPointOnCurve = ConnectionExtensions.getNearestPointOnConnection(_get_1, to, toKind);
      double _parameter = startPointOnCurve.getParameter();
      label.setPosition(_parameter);
      ObservableList<Animation> _children = morph.getChildren();
      Timeline _timeline = new Timeline();
      final Procedure1<Timeline> _function_2 = (Timeline it) -> {
        ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
        Duration _millis = DurationExtensions.millis(0);
        DoubleProperty _positionProperty = label.positionProperty();
        double _parameter_1 = startPointOnCurve.getParameter();
        KeyValue _keyValue = new <Number>KeyValue(_positionProperty, Double.valueOf(_parameter_1));
        KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
        _keyFrames.add(_keyFrame);
        ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
        DoubleProperty _positionProperty_1 = label.positionProperty();
        double _parameter_2 = endPointOnCurve.getParameter();
        KeyValue _keyValue_1 = new <Number>KeyValue(_positionProperty_1, Double.valueOf(_parameter_2));
        KeyFrame _keyFrame_1 = new KeyFrame(duration, _keyValue_1);
        _keyFrames_1.add(_keyFrame_1);
      };
      Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function_2);
      _children.add(_doubleArrow);
    };
    _labels_1.forEach(_function_1);
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    final ArrayList<XControlPoint> newControlPoints = new ArrayList<XControlPoint>(_controlPoints);
    Set<Map.Entry<Integer, XControlPoint>> _entrySet = addBefore.entrySet();
    final Function1<Map.Entry<Integer, XControlPoint>, Integer> _function_2 = (Map.Entry<Integer, XControlPoint> it) -> {
      return it.getKey();
    };
    List<Map.Entry<Integer, XControlPoint>> _sortBy = IterableExtensions.<Map.Entry<Integer, XControlPoint>, Integer>sortBy(_entrySet, _function_2);
    final Consumer<Map.Entry<Integer, XControlPoint>> _function_3 = (Map.Entry<Integer, XControlPoint> it) -> {
      Integer _key = it.getKey();
      XControlPoint _value = it.getValue();
      newControlPoints.add((_key).intValue(), _value);
    };
    _sortBy.forEach(_function_3);
    ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
    _controlPoints_1.setAll(newControlPoints);
    int _size = newControlPoints.size();
    int _minus = (_size - 1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Point2D fromPoint = from.get((i).intValue());
        final Point2D toPoint = to.get((i).intValue());
        final XControlPoint currentControlPoint = newControlPoints.get((i).intValue());
        double _distance = fromPoint.distance(toPoint);
        boolean _greaterThan = (_distance > NumberExpressionExtensions.EPSILON);
        if (_greaterThan) {
          ObservableList<Animation> _children = morph.getChildren();
          PathTransition _createMoveTransition = this.createMoveTransition(currentControlPoint, fromPoint, toPoint, duration);
          _children.add(_createMoveTransition);
        }
      }
    }
    final EventHandler<ActionEvent> _function_4 = (ActionEvent it) -> {
      this.connection.setKind(toKind);
      ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
      int _size_1 = to.size();
      _connectionRouter.shrinkToSize(_size_1);
      ObservableList<XControlPoint> _controlPoints_2 = this.connection.getControlPoints();
      Collection<XControlPoint> _values = removeAfter.values();
      Iterables.removeAll(_controlPoints_2, _values);
      ObservableList<XConnectionLabel> _labels_2 = this.connection.getLabels();
      final Consumer<XConnectionLabel> _function_5 = (XConnectionLabel it_1) -> {
        Point2D _get = label2position.get(it_1);
        ObservableList<XControlPoint> _controlPoints_3 = this.connection.getControlPoints();
        final Function1<XControlPoint, Point2D> _function_6 = (XControlPoint it_2) -> {
          return ConnectionExtensions.toPoint2D(it_2);
        };
        List<Point2D> _map = ListExtensions.<XControlPoint, Point2D>map(_controlPoints_3, _function_6);
        ConnectionExtensions.PointOnCurve _nearestPointOnConnection = ConnectionExtensions.getNearestPointOnConnection(_get, _map, toKind);
        double _parameter = _nearestPointOnConnection.getParameter();
        it_1.setPosition(_parameter);
      };
      _labels_2.forEach(_function_5);
      ObservableList<XControlPoint> _controlPoints_3 = this.connection.getControlPoints();
      final Consumer<XControlPoint> _function_6 = (XControlPoint it_1) -> {
        MoveBehavior _behavior = it_1.<MoveBehavior>getBehavior(MoveBehavior.class);
        if (_behavior!=null) {
          _behavior.setManuallyPlaced(false);
        }
      };
      _controlPoints_3.forEach(_function_6);
      this.connection.updateShapes();
    };
    morph.setOnFinished(_function_4);
    return morph;
  }
  
  protected PathTransition createMoveTransition(final XControlPoint shape, final Point2D from, final Point2D to, final Duration duration) {
    PathTransition _xblockexpression = null;
    {
      Group _group = new Group();
      final Procedure1<Group> _function = (Group it) -> {
        double _x = from.getX();
        it.setTranslateX(_x);
        double _y = from.getY();
        it.setTranslateY(_y);
      };
      final Group dummyNode = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      DoubleProperty _layoutXProperty = shape.layoutXProperty();
      DoubleProperty _translateXProperty = dummyNode.translateXProperty();
      _layoutXProperty.bind(_translateXProperty);
      DoubleProperty _layoutYProperty = shape.layoutYProperty();
      DoubleProperty _translateYProperty = dummyNode.translateYProperty();
      _layoutYProperty.bind(_translateYProperty);
      PathTransition _pathTransition = new PathTransition();
      final Procedure1<PathTransition> _function_1 = (PathTransition it) -> {
        it.setNode(dummyNode);
        it.setDuration(duration);
        it.setCycleCount(1);
        Path _path = new Path();
        final Procedure1<Path> _function_2 = (Path it_1) -> {
          ObservableList<PathElement> _elements = it_1.getElements();
          double _x = from.getX();
          double _y = from.getY();
          MoveTo _moveTo = new MoveTo(_x, _y);
          _elements.add(_moveTo);
          ObservableList<PathElement> _elements_1 = it_1.getElements();
          double _x_1 = to.getX();
          double _y_1 = to.getY();
          LineTo _lineTo = new LineTo(_x_1, _y_1);
          _elements_1.add(_lineTo);
        };
        Path _doubleArrow = ObjectExtensions.<Path>operator_doubleArrow(_path, _function_2);
        it.setPath(_doubleArrow);
        final EventHandler<ActionEvent> _function_3 = (ActionEvent it_1) -> {
          final Procedure1<XControlPoint> _function_4 = (XControlPoint it_2) -> {
            DoubleProperty _layoutXProperty_1 = it_2.layoutXProperty();
            _layoutXProperty_1.unbind();
            DoubleProperty _layoutYProperty_1 = it_2.layoutYProperty();
            _layoutYProperty_1.unbind();
            double _x = to.getX();
            it_2.setLayoutX(_x);
            double _y = to.getY();
            it_2.setLayoutY(_y);
          };
          ObjectExtensions.<XControlPoint>operator_doubleArrow(shape, _function_4);
        };
        it.setOnFinished(_function_3);
      };
      _xblockexpression = ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function_1);
    }
    return _xblockexpression;
  }
  
  public RemoveControlPointCommand(final XConnection connection, final List<XControlPoint> selectedPoints) {
    super();
    this.connection = connection;
    this.selectedPoints = selectedPoints;
  }
}
