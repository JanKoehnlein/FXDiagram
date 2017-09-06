package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XConnectionLabel;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
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
import javafx.geometry.Point2D;
import javafx.scene.Group;
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
      this.fromKind = this.connection.getKind();
      final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
        double _layoutX = it.getLayoutX();
        double _layoutY = it.getLayoutY();
        return new Point2D(_layoutX, _layoutY);
      };
      this.fromPoints = CollectionLiterals.<Point2D>newArrayList(((Point2D[])Conversions.unwrapArray(ListExtensions.<XControlPoint, Point2D>map(this.connection.getControlPoints(), _function), Point2D.class)));
      final Set<XControlPoint> removePoints = this.calculateControlPointsToRemove(this.selectedPoints);
      final ObservableList<XControlPoint> controlPoints = this.connection.getControlPoints();
      int _size = controlPoints.size();
      int _size_1 = removePoints.size();
      int _minus = (_size - _size_1);
      boolean _equals = (_minus == 2);
      if (_equals) {
        this.toKind = XConnection.Kind.POLYLINE;
      } else {
        this.toKind = this.connection.getKind();
      }
      this.toPoints = this.calculateToPoints(removePoints);
      final Function1<XControlPoint, Integer> _function_1 = (XControlPoint it) -> {
        return Integer.valueOf(controlPoints.indexOf(it));
      };
      _xblockexpression = this.removedPoints = IterableExtensions.<Integer, XControlPoint>toMap(removePoints, _function_1);
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
    removePoints.remove(IterableExtensions.<XControlPoint>head(controlPoints));
    removePoints.remove(IterableExtensions.<XControlPoint>last(controlPoints));
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
            final boolean hasRemainingControlPoints = ((!Objects.equal(lastRemaining, IterableExtensions.<XControlPoint>head(controlPoints))) || (!Objects.equal(controlPoint, IterableExtensions.<XControlPoint>last(controlPoints))));
            Point2D _xifexpression = null;
            XControlPoint _head = IterableExtensions.<XControlPoint>head(controlPoints);
            boolean _equals = Objects.equal(lastRemaining, _head);
            if (_equals) {
              Point2D _xblockexpression = null;
              {
                Point2D _xifexpression_1 = null;
                if (hasRemainingControlPoints) {
                  double _layoutX = controlPoint.getLayoutX();
                  double _layoutY = controlPoint.getLayoutY();
                  _xifexpression_1 = new Point2D(_layoutX, _layoutY);
                } else {
                  _xifexpression_1 = this.midPoint(this.connection.getTarget());
                }
                final Point2D anchorRefPoint = _xifexpression_1;
                _xblockexpression = this.connection.getConnectionRouter().getNearestAnchor(
                  this.connection.getSource(), anchorRefPoint, this.connection.getSourceArrowHead());
              }
              _xifexpression = _xblockexpression;
            } else {
              double _layoutX = lastRemaining.getLayoutX();
              double _layoutY = lastRemaining.getLayoutY();
              _xifexpression = new Point2D(_layoutX, _layoutY);
            }
            final Point2D segmentStart = _xifexpression;
            Point2D _xifexpression_1 = null;
            XControlPoint _last = IterableExtensions.<XControlPoint>last(controlPoints);
            boolean _equals_1 = Objects.equal(controlPoint, _last);
            if (_equals_1) {
              Point2D _xblockexpression_1 = null;
              {
                Point2D _xifexpression_2 = null;
                if (hasRemainingControlPoints) {
                  double _layoutX_1 = lastRemaining.getLayoutX();
                  double _layoutY_1 = lastRemaining.getLayoutY();
                  _xifexpression_2 = new Point2D(_layoutX_1, _layoutY_1);
                } else {
                  _xifexpression_2 = this.midPoint(this.connection.getSource());
                }
                final Point2D anchorRefPoint = _xifexpression_2;
                _xblockexpression_1 = this.connection.getConnectionRouter().getNearestAnchor(
                  this.connection.getTarget(), anchorRefPoint, this.connection.getTargetArrowHead());
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
                boolean _notEquals = (!Objects.equal(_type, XControlPoint.Type.CONTROL_POINT));
                if (_notEquals) {
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
    return CoreExtensions.localToRootDiagram(node, BoundsExtensions.center(node.getNode().getBoundsInLocal()));
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    ParallelTransition _xblockexpression = null;
    {
      this.initialize();
      _xblockexpression = this.createMorphTransition(this.fromPoints, this.toKind, this.toPoints, 
        CollectionLiterals.<Integer, XControlPoint>emptyMap(), this.removedPoints, this.getExecuteDuration(context));
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    return this.createMorphTransition(this.toPoints, this.fromKind, this.fromPoints, 
      this.removedPoints, CollectionLiterals.<Integer, XControlPoint>emptyMap(), context.getDefaultUndoDuration());
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    return this.createMorphTransition(this.fromPoints, this.toKind, this.toPoints, 
      CollectionLiterals.<Integer, XControlPoint>emptyMap(), this.removedPoints, context.getDefaultUndoDuration());
  }
  
  protected ParallelTransition createMorphTransition(final List<Point2D> from, final XConnection.Kind toKind, final List<Point2D> to, final Map<Integer, XControlPoint> addBefore, final Map<Integer, XControlPoint> removeAfter, final Duration duration) {
    final ParallelTransition morph = new ParallelTransition();
    final HashMap<XConnectionLabel, Point2D> label2position = CollectionLiterals.<XConnectionLabel, Point2D>newHashMap();
    final Consumer<XConnectionLabel> _function = (XConnectionLabel it) -> {
      label2position.put(it, this.connection.at(it.getPosition()));
    };
    this.connection.getLabels().forEach(_function);
    final Consumer<XConnectionLabel> _function_1 = (XConnectionLabel label) -> {
      final ConnectionExtensions.PointOnCurve startPointOnCurve = ConnectionExtensions.getNearestPointOnConnection(label2position.get(label), from, this.fromKind);
      final ConnectionExtensions.PointOnCurve endPointOnCurve = ConnectionExtensions.getNearestPointOnConnection(label2position.get(label), to, toKind);
      label.setPosition(startPointOnCurve.getParameter());
      ObservableList<Animation> _children = morph.getChildren();
      Timeline _timeline = new Timeline();
      final Procedure1<Timeline> _function_2 = (Timeline it) -> {
        ObservableList<KeyFrame> _keyFrames = it.getKeyFrames();
        Duration _millis = DurationExtensions.millis(0);
        DoubleProperty _positionProperty = label.positionProperty();
        double _parameter = startPointOnCurve.getParameter();
        KeyValue _keyValue = new <Number>KeyValue(_positionProperty, Double.valueOf(_parameter));
        KeyFrame _keyFrame = new KeyFrame(_millis, _keyValue);
        _keyFrames.add(_keyFrame);
        ObservableList<KeyFrame> _keyFrames_1 = it.getKeyFrames();
        DoubleProperty _positionProperty_1 = label.positionProperty();
        double _parameter_1 = endPointOnCurve.getParameter();
        KeyValue _keyValue_1 = new <Number>KeyValue(_positionProperty_1, Double.valueOf(_parameter_1));
        KeyFrame _keyFrame_1 = new KeyFrame(duration, _keyValue_1);
        _keyFrames_1.add(_keyFrame_1);
      };
      Timeline _doubleArrow = ObjectExtensions.<Timeline>operator_doubleArrow(_timeline, _function_2);
      _children.add(_doubleArrow);
    };
    this.connection.getLabels().forEach(_function_1);
    ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
    final ArrayList<XControlPoint> newControlPoints = new ArrayList<XControlPoint>(_controlPoints);
    final Function1<Map.Entry<Integer, XControlPoint>, Integer> _function_2 = (Map.Entry<Integer, XControlPoint> it) -> {
      return it.getKey();
    };
    final Consumer<Map.Entry<Integer, XControlPoint>> _function_3 = (Map.Entry<Integer, XControlPoint> it) -> {
      newControlPoints.add((it.getKey()).intValue(), it.getValue());
    };
    IterableExtensions.<Map.Entry<Integer, XControlPoint>, Integer>sortBy(addBefore.entrySet(), _function_2).forEach(_function_3);
    this.connection.getControlPoints().setAll(newControlPoints);
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
      this.connection.getConnectionRouter().shrinkToSize(to.size());
      ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
      Collection<XControlPoint> _values = removeAfter.values();
      Iterables.removeAll(_controlPoints_1, _values);
      final Consumer<XConnectionLabel> _function_5 = (XConnectionLabel it_1) -> {
        final Function1<XControlPoint, Point2D> _function_6 = (XControlPoint it_2) -> {
          return ConnectionExtensions.toPoint2D(it_2);
        };
        it_1.setPosition(ConnectionExtensions.getNearestPointOnConnection(label2position.get(it_1), 
          ListExtensions.<XControlPoint, Point2D>map(this.connection.getControlPoints(), _function_6), toKind).getParameter());
      };
      this.connection.getLabels().forEach(_function_5);
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
        it.setTranslateX(from.getX());
        it.setTranslateY(from.getY());
      };
      final Group dummyNode = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      shape.layoutXProperty().bind(dummyNode.translateXProperty());
      shape.layoutYProperty().bind(dummyNode.translateYProperty());
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
            it_2.layoutXProperty().unbind();
            it_2.layoutYProperty().unbind();
            it_2.setLayoutX(to.getX());
            it_2.setLayoutY(to.getY());
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
