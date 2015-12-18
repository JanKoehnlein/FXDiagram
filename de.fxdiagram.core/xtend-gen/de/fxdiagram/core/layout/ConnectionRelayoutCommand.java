package de.fxdiagram.core.layout;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.behavior.MoveBehavior;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
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
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ConnectionRelayoutCommand extends AbstractAnimationCommand {
  private XConnection connection;
  
  private XConnection.Kind fromKind;
  
  private XConnection.Kind toKind;
  
  private List<Point2D> fromPoints;
  
  private List<Point2D> toPoints;
  
  public ConnectionRelayoutCommand(final XConnection connection, final XConnection.Kind toKind, final List<Point2D> toPoints) {
    this.connection = connection;
    this.toKind = toKind;
    this.toPoints = toPoints;
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    ParallelTransition _xblockexpression = null;
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
      Duration _executeDuration = this.getExecuteDuration(context);
      _xblockexpression = this.createMorphTransition(this.fromPoints, this.toKind, this.toPoints, _executeDuration);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMorphTransition(this.toPoints, this.fromKind, this.fromPoints, _defaultUndoDuration);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMorphTransition(this.fromPoints, this.toKind, this.toPoints, _defaultUndoDuration);
  }
  
  public ParallelTransition createMorphTransition(final List<Point2D> from, final XConnection.Kind toKind, final List<Point2D> to, final Duration duration) {
    final ParallelTransition morph = new ParallelTransition();
    this.connection.setKind(toKind);
    ConnectionRouter _connectionRouter = this.connection.getConnectionRouter();
    int _size = to.size();
    _connectionRouter.growToSize(_size);
    final ObservableList<XControlPoint> controlPoints = this.connection.getControlPoints();
    int _size_1 = controlPoints.size();
    int _minus = (_size_1 - 1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        int _size_2 = from.size();
        int _minus_1 = (_size_2 - 1);
        int _min = Math.min(_minus_1, (i).intValue());
        final Point2D fromPoint = from.get(_min);
        int _size_3 = to.size();
        int _minus_2 = (_size_3 - 1);
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
    final EventHandler<ActionEvent> _function = (ActionEvent it) -> {
      ConnectionRouter _connectionRouter_1 = this.connection.getConnectionRouter();
      int _size_2 = to.size();
      _connectionRouter_1.shrinkToSize(_size_2);
      ObservableList<XControlPoint> _controlPoints = this.connection.getControlPoints();
      final Consumer<XControlPoint> _function_1 = (XControlPoint it_1) -> {
        MoveBehavior _behavior = it_1.<MoveBehavior>getBehavior(MoveBehavior.class);
        if (_behavior!=null) {
          _behavior.setIsManuallyPlaced(false);
        }
      };
      _controlPoints.forEach(_function_1);
      this.connection.updateShapes();
    };
    morph.setOnFinished(_function);
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
