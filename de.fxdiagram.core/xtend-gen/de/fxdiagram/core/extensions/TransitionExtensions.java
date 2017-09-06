package de.fxdiagram.core.extensions;

import de.fxdiagram.core.XConnection;
import de.fxdiagram.core.XControlPoint;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.anchors.ConnectionMemento;
import de.fxdiagram.core.anchors.ManhattanRouter;
import de.fxdiagram.core.extensions.NumberExpressionExtensions;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.Transition;
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
          final Procedure1<XShape> _function_4 = (XShape it_2) -> {
            it_2.layoutXProperty().unbind();
            it_2.layoutYProperty().unbind();
            it_2.setLayoutX(to.getX());
            it_2.setLayoutY(to.getY());
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
    ManhattanRouter _manhattanRouter = connection.getConnectionRouter().getManhattanRouter();
    _manhattanRouter.setReroutingEnabled(false);
    final ParallelTransition morph = new ParallelTransition();
    connection.getConnectionRouter().growToSize(to.size());
    final ObservableList<XControlPoint> controlPoints = connection.getControlPoints();
    int _size = controlPoints.size();
    int _minus = (_size - 1);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(1, _minus, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        int _size_1 = from.size();
        int _minus_1 = (_size_1 - 1);
        final Point2D fromPoint = from.get(Math.min(_minus_1, (i).intValue()));
        int _size_2 = to.size();
        int _minus_2 = (_size_2 - 1);
        final Point2D toPoint = to.get(Math.min(_minus_2, (i).intValue()));
        final XControlPoint currentControlPoint = controlPoints.get((i).intValue());
        double _distance = fromPoint.distance(toPoint);
        boolean _greaterThan = (_distance > NumberExpressionExtensions.EPSILON);
        if (_greaterThan) {
          int _size_3 = to.size();
          int _minus_3 = (_size_3 - 1);
          final boolean toManuallyPlaced = toMemento.getControlPoints().get(Math.min(_minus_3, (i).intValue())).getManuallyPlaced();
          ObservableList<Animation> _children = morph.getChildren();
          Transition _createMoveTransition = TransitionExtensions.createMoveTransition(currentControlPoint, fromPoint, toPoint, toManuallyPlaced, duration);
          _children.add(_createMoveTransition);
        }
      }
    }
    final EventHandler<ActionEvent> _function = (ActionEvent it) -> {
      connection.setKind(toMemento.getKind());
      connection.getConnectionRouter().shrinkToSize(to.size());
      XControlPoint _head = IterableExtensions.<XControlPoint>head(connection.getControlPoints());
      final Procedure1<XControlPoint> _function_1 = (XControlPoint it_1) -> {
        final XControlPoint toCP = IterableExtensions.<XControlPoint>head(toMemento.getControlPoints());
        it_1.setLayoutX(toCP.getLayoutX());
        it_1.setLayoutY(toCP.getLayoutY());
        it_1.setSide(toCP.getSide());
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_head, _function_1);
      XControlPoint _last = IterableExtensions.<XControlPoint>last(connection.getControlPoints());
      final Procedure1<XControlPoint> _function_2 = (XControlPoint it_1) -> {
        final XControlPoint toCP = IterableExtensions.<XControlPoint>last(toMemento.getControlPoints());
        it_1.setLayoutX(toCP.getLayoutX());
        it_1.setLayoutY(toCP.getLayoutY());
        it_1.setSide(toCP.getSide());
      };
      ObjectExtensions.<XControlPoint>operator_doubleArrow(_last, _function_2);
      ManhattanRouter _manhattanRouter_1 = connection.getConnectionRouter().getManhattanRouter();
      _manhattanRouter_1.setReroutingEnabled(true);
    };
    morph.setOnFinished(_function);
    return morph;
  }
}
