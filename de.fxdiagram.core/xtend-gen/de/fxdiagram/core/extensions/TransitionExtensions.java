package de.fxdiagram.core.extensions;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ConnectionMemento;
import de.fxdiagram.core.anchors.ConnectionRouter;
import de.fxdiagram.core.anchors.ManhattanRouter;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class TransitionExtensions {
  public static Transition createMoveTransition(final XShape shape, final Point2D from, final Point2D to, final boolean toManuallyPlaced, final Duration duration) {
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
          final Procedure1<XShape> _function_4 = (XShape it_2) -> {
            DoubleProperty _layoutXProperty_1 = it_2.layoutXProperty();
            _layoutXProperty_1.unbind();
            DoubleProperty _layoutYProperty_1 = it_2.layoutYProperty();
            _layoutYProperty_1.unbind();
            double _x = to.getX();
            it_2.setLayoutX(_x);
            double _y = to.getY();
            it_2.setLayoutY(_y);
          };
          ObjectExtensions.<XShape>operator_doubleArrow(shape, _function_4);
          shape.setManuallyPlaced(toManuallyPlaced);
        };
        it.setOnFinished(_function_3);
      };
      _xblockexpression = ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function_1);
    }
    return _xblockexpression;
  }
  
  public static Transition createMorphTransition(final XConnection connection, final ConnectionMemento fromMemento, final ConnectionMemento toMemento, final Duration duration) {
    final List<Point2D> from = fromMemento.getPoints();
    final List<Point2D> to = toMemento.getPoints();
    ConnectionRouter _connectionRouter = connection.getConnectionRouter();
    ManhattanRouter _manhattanRouter = _connectionRouter.getManhattanRouter();
    _manhattanRouter.setReroutingEnabled(false);
    final ParallelTransition morph = new ParallelTransition();
    ConnectionRouter _connectionRouter_1 = connection.getConnectionRouter();
    int _size = to.size();
    _connectionRouter_1.growToSize(_size);
    final ObservableList<XControlPoint> controlPoints = connection.getControlPoints();
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
          List<XControlPoint> _controlPoints = toMemento.getControlPoints();
          int _size_4 = to.size();
          int _minus_3 = (_size_4 - 1);
          int _min_2 = Math.min(_minus_3, (i).intValue());
          XControlPoint _get = _controlPoints.get(_min_2);
          final boolean toManuallyPlaced = _get.getManuallyPlaced();
          ObservableList<Animation> _children = morph.getChildren();
          Transition _createMoveTransition = TransitionExtensions.createMoveTransition(currentControlPoint, fromPoint, toPoint, toManuallyPlaced, duration);
          _children.add(_createMoveTransition);
        }
      }
    }
    final EventHandler<ActionEvent> _function = (ActionEvent it) -> {
      ConnectionRouter _connectionRouter_2 = connection.getConnectionRouter();
      int _size_2 = to.size();
      _connectionRouter_2.shrinkToSize(_size_2);
      XConnection.Kind _kind = toMemento.getKind();
      connection.setKind(_kind);
      ObservableList<XControlPoint> _controlPoints = connection.getControlPoints();
      XControlPoint _head = IterableExtensions.<XControlPoint>head(_controlPoints);
      final Procedure1<XControlPoint> _function_1 = (XControlPoint it_1) -> {
        List<XControlPoint> _controlPoints_1 = toMemento.getControlPoints();
        final XControlPoint toCP = IterableExtensions.<XControlPoint>head(_controlPoints_1);
        double _layoutX = toCP.getLayoutX();
        it_1.setLayoutX(_layoutX);
        double _layoutY = toCP.getLayoutY();
        it_1.setLayoutY(_layoutY);
        Side _side = toCP.getSide();
        it_1.setSide(_side);
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_head, _function_1);
      ObservableList<XControlPoint> _controlPoints_1 = connection.getControlPoints();
      XControlPoint _last = IterableExtensions.<XControlPoint>last(_controlPoints_1);
      final Procedure1<XControlPoint> _function_2 = (XControlPoint it_1) -> {
        List<XControlPoint> _controlPoints_2 = toMemento.getControlPoints();
        final XControlPoint toCP = IterableExtensions.<XControlPoint>last(_controlPoints_2);
        double _layoutX = toCP.getLayoutX();
        it_1.setLayoutX(_layoutX);
        double _layoutY = toCP.getLayoutY();
        it_1.setLayoutY(_layoutY);
        Side _side = toCP.getSide();
        it_1.setSide(_side);
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_last, _function_2);
      ConnectionRouter _connectionRouter_3 = connection.getConnectionRouter();
      ManhattanRouter _manhattanRouter_1 = _connectionRouter_3.getManhattanRouter();
      _manhattanRouter_1.setReroutingEnabled(true);
    };
    morph.setOnFinished(_function);
    return morph;
  }
}
