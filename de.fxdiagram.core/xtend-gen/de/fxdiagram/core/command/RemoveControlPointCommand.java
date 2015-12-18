package de.fxdiagram.core.command;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XNode;
import de.fxdiagram.core.anchors.ArrowHead;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.BoundsExtensions;
import de.fxdiagram.core.extensions.CoreExtensions;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import de.fxdiagram.core.extensions.Point2DExtensions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
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
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class RemoveControlPointCommand extends AbstractAnimationCommand {
  private final XConnection connection;
  
  private final XConnection.Kind fromKind;
  
  private final XConnection.Kind toKind;
  
  private final List<Point2D> fromPoints;
  
  private final List<Point2D> toPoints;
  
  private final Map<Integer, XControlPoint> removedPoints;
  
  /**
   * Removing anchor points is ignored
   */
  public RemoveControlPointCommand(final XConnection connection, final List<XControlPoint> removeControlPoints) {
    this.connection = connection;
    this.toKind = XConnection.Kind.POLYLINE;
    XConnection.Kind _kind = connection.getKind();
    this.fromKind = _kind;
    ObservableList<XControlPoint> _controlPoints = connection.getControlPoints();
    final Function1<XControlPoint, Point2D> _function = (XControlPoint it) -> {
      double _layoutX = it.getLayoutX();
      double _layoutY = it.getLayoutY();
      return new Point2D(_layoutX, _layoutY);
    };
    List<Point2D> _map = ListExtensions.<XControlPoint, Point2D>map(_controlPoints, _function);
    ArrayList<Point2D> _newArrayList = CollectionLiterals.<Point2D>newArrayList(((Point2D[])Conversions.unwrapArray(_map, Point2D.class)));
    this.fromPoints = _newArrayList;
    final ObservableList<XControlPoint> controlPoints = connection.getControlPoints();
    final Set<XControlPoint> reallyRemovedPoints = IterableExtensions.<XControlPoint>toSet(removeControlPoints);
    XControlPoint _head = IterableExtensions.<XControlPoint>head(controlPoints);
    reallyRemovedPoints.remove(_head);
    XControlPoint _last = IterableExtensions.<XControlPoint>last(controlPoints);
    reallyRemovedPoints.remove(_last);
    ArrayList<Point2D> _newArrayList_1 = CollectionLiterals.<Point2D>newArrayList();
    this.toPoints = _newArrayList_1;
    XControlPoint lastRemaining = null;
    int segmentRemoveCount = 0;
    int _size = controlPoints.size();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final XControlPoint controlPoint = controlPoints.get((i).intValue());
        boolean _contains = reallyRemovedPoints.contains(controlPoint);
        if (_contains) {
          segmentRemoveCount++;
        } else {
          if ((segmentRemoveCount > 0)) {
            boolean _or = false;
            XControlPoint _head_1 = IterableExtensions.<XControlPoint>head(controlPoints);
            boolean _notEquals = (!Objects.equal(lastRemaining, _head_1));
            if (_notEquals) {
              _or = true;
            } else {
              XControlPoint _last_1 = IterableExtensions.<XControlPoint>last(controlPoints);
              boolean _notEquals_1 = (!Objects.equal(controlPoint, _last_1));
              _or = _notEquals_1;
            }
            final boolean hasRemainingControlPoints = _or;
            Point2D _xifexpression = null;
            XControlPoint _head_2 = IterableExtensions.<XControlPoint>head(controlPoints);
            boolean _equals = Objects.equal(lastRemaining, _head_2);
            if (_equals) {
              Point2D _xblockexpression = null;
              {
                Point2D _xifexpression_1 = null;
                if (hasRemainingControlPoints) {
                  double _layoutX = controlPoint.getLayoutX();
                  double _layoutY = controlPoint.getLayoutY();
                  _xifexpression_1 = new Point2D(_layoutX, _layoutY);
                } else {
                  XNode _target = connection.getTarget();
                  _xifexpression_1 = RemoveControlPointCommand.midPoint(_target);
                }
                final Point2D anchorRefPoint = _xifexpression_1;
                ConnectionRouter _connectionRouter = connection.getConnectionRouter();
                XNode _source = connection.getSource();
                ArrowHead _sourceArrowHead = connection.getSourceArrowHead();
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
            XControlPoint _last_2 = IterableExtensions.<XControlPoint>last(controlPoints);
            boolean _equals_1 = Objects.equal(controlPoint, _last_2);
            if (_equals_1) {
              Point2D _xblockexpression_1 = null;
              {
                Point2D _xifexpression_2 = null;
                if (hasRemainingControlPoints) {
                  double _layoutX_1 = lastRemaining.getLayoutX();
                  double _layoutY_1 = lastRemaining.getLayoutY();
                  _xifexpression_2 = new Point2D(_layoutX_1, _layoutY_1);
                } else {
                  XNode _source = connection.getSource();
                  _xifexpression_2 = RemoveControlPointCommand.midPoint(_source);
                }
                final Point2D anchorRefPoint = _xifexpression_2;
                ConnectionRouter _connectionRouter = connection.getConnectionRouter();
                XNode _target = connection.getTarget();
                ArrowHead _targetArrowHead = connection.getTargetArrowHead();
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
              Point2D _linear = Point2DExtensions.linear(segmentStart, segmentEnd, 
                (((double) (j).intValue()) / (segmentRemoveCount + 1)));
              this.toPoints.add(_linear);
            }
            segmentRemoveCount = 0;
          }
          lastRemaining = controlPoint;
          double _layoutX_2 = controlPoint.getLayoutX();
          double _layoutY_2 = controlPoint.getLayoutY();
          Point2D _point2D = new Point2D(_layoutX_2, _layoutY_2);
          this.toPoints.add(_point2D);
        }
      }
    }
    final Function1<XControlPoint, Integer> _function_1 = (XControlPoint it) -> {
      return Integer.valueOf(controlPoints.indexOf(it));
    };
    Map<Integer, XControlPoint> _map_1 = IterableExtensions.<Integer, XControlPoint>toMap(reallyRemovedPoints, _function_1);
    this.removedPoints = _map_1;
  }
  
  protected static Point2D midPoint(final XNode node) {
    Node _node = node.getNode();
    Bounds _boundsInLocal = _node.getBoundsInLocal();
    Point2D _center = BoundsExtensions.center(_boundsInLocal);
    return CoreExtensions.localToRootDiagram(node, _center);
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    Map<Integer, XControlPoint> _emptyMap = CollectionLiterals.<Integer, XControlPoint>emptyMap();
    Duration _executeDuration = this.getExecuteDuration(context);
    return this.createMorphTransition(this.fromPoints, this.toKind, this.toPoints, _emptyMap, this.removedPoints, _executeDuration);
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    Map<Integer, XControlPoint> _emptyMap = CollectionLiterals.<Integer, XControlPoint>emptyMap();
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMorphTransition(this.toPoints, this.fromKind, this.fromPoints, this.removedPoints, _emptyMap, _defaultUndoDuration);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    Map<Integer, XControlPoint> _emptyMap = CollectionLiterals.<Integer, XControlPoint>emptyMap();
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMorphTransition(this.fromPoints, this.toKind, this.toPoints, _emptyMap, this.removedPoints, _defaultUndoDuration);
  }
  
  public ParallelTransition createMorphTransition(final List<Point2D> from, final XConnection.Kind toKind, final List<Point2D> to, final Map<Integer, XControlPoint> addBefore, final Map<Integer, XControlPoint> removeAfter, final Duration duration) {
    final ParallelTransition morph = new ParallelTransition();
    this.connection.setKind(toKind);
    Set<Map.Entry<Integer, XControlPoint>> _entrySet = addBefore.entrySet();
    final Function1<Map.Entry<Integer, XControlPoint>, Integer> _function = (Map.Entry<Integer, XControlPoint> it) -> {
      return it.getKey();
    };
    List<Map.Entry<Integer, XControlPoint>> _sortBy = IterableExtensions.<Map.Entry<Integer, XControlPoint>, Integer>sortBy(_entrySet, _function);
    final Consumer<Map.Entry<Integer, XControlPoint>> _function_1 = (Map.Entry<Integer, XControlPoint> it) -> {
      ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
      Integer _key = it.getKey();
      XControlPoint _value = it.getValue();
      _controlPoints.add((_key).intValue(), _value);
    };
    _sortBy.forEach(_function_1);
    final ObservableList<XControlPoint> controlPoints = this.connection.getControlPoints();
    int _size = controlPoints.size();
    int _minus = (_size - 1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        int _size_1 = from.size();
        int _minus_1 = (_size_1 - 1);
        int _min = Math.min(_minus_1, (i).intValue());
        final Point2D fromPoint = from.get(_min);
        int _size_2 = to.size();
        int _minus_2 = (_size_2 - 1);
        int _min_1 = Math.min(_minus_2, (i).intValue());
        final Point2D toPoint = to.get(_min_1);
        final XControlPoint currentControlPoint = controlPoints.get((i).intValue());
        double _distance = fromPoint.distance(toPoint);
        boolean _greaterThan = (_distance > NumberExpressionExtensions.EPSILON);
        if (_greaterThan) {
          ObservableList<Animation> _children = morph.getChildren();
          PathTransition _createMoveTransition = this.createMoveTransition(currentControlPoint, fromPoint, toPoint, duration);
          _children.add(_createMoveTransition);
        }
      }
    }
    final EventHandler<ActionEvent> _function_2 = (ActionEvent it) -> {
      ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
      int _size_1 = to.size();
      _connectionRouter.shrinkToSize(_size_1);
      ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
      Collection<XControlPoint> _values = removeAfter.values();
      Iterables.removeAll(_controlPoints, _values);
      ObservableList<XControlPoint> _controlPoints_1 = this.connection.getControlPoints();
      final Consumer<XControlPoint> _function_3 = (XControlPoint it_1) -> {
        MoveBehavior _behavior = it_1.<MoveBehavior>getBehavior(MoveBehavior.class);
        if (_behavior!=null) {
          _behavior.setIsManuallyPlaced(false);
        }
      };
      _controlPoints_1.forEach(_function_3);
      this.connection.updateShapes();
    };
    morph.setOnFinished(_function_2);
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
}
