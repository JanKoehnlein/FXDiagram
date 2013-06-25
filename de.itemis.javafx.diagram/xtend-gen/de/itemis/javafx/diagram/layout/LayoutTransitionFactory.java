package de.itemis.javafx.diagram.layout;

import de.itemis.javafx.diagram.XNode;
import javafx.animation.PathTransition;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LayoutTransitionFactory {
  public PathTransition createTransition(final XNode node, final double endX, final double endY, final Duration duration) {
    PathTransition _xblockexpression = null;
    {
      Group _group = new Group();
      final Group dummyNode = _group;
      PathTransition _pathTransition = new PathTransition();
      final Procedure1<PathTransition> _function = new Procedure1<PathTransition>() {
          public void apply(final PathTransition it) {
            it.setNode(dummyNode);
            it.setDuration(duration);
            it.setCycleCount(1);
            Path _path = new Path();
            final Procedure1<Path> _function = new Procedure1<Path>() {
                public void apply(final Path it) {
                  double controlX = 0;
                  double controlY = 0;
                  double _random = Math.random();
                  boolean _greaterThan = (_random > 0.5);
                  if (_greaterThan) {
                    double _layoutX = node.getLayoutX();
                    controlX = _layoutX;
                    controlY = endY;
                  } else {
                    controlX = endX;
                    double _layoutY = node.getLayoutY();
                    controlY = _layoutY;
                  }
                  ObservableList<PathElement> _elements = it.getElements();
                  double _layoutX_1 = node.getLayoutX();
                  double _layoutY_1 = node.getLayoutY();
                  MoveTo _moveTo = new MoveTo(_layoutX_1, _layoutY_1);
                  _elements.add(_moveTo);
                  ObservableList<PathElement> _elements_1 = it.getElements();
                  QuadCurveTo _quadCurveTo = new QuadCurveTo(controlX, controlY, endX, endY);
                  _elements_1.add(_quadCurveTo);
                }
              };
            Path _doubleArrow = ObjectExtensions.<Path>operator_doubleArrow(_path, _function);
            it.setPath(_doubleArrow);
          }
        };
      final PathTransition delegate = ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function);
      DoubleProperty _layoutXProperty = node.layoutXProperty();
      DoubleProperty _translateXProperty = dummyNode.translateXProperty();
      _layoutXProperty.bind(_translateXProperty);
      DoubleProperty _layoutYProperty = node.layoutYProperty();
      DoubleProperty _translateYProperty = dummyNode.translateYProperty();
      _layoutYProperty.bind(_translateYProperty);
      final EventHandler<ActionEvent> _function_1 = new EventHandler<ActionEvent>() {
          public void handle(final ActionEvent it) {
            DoubleProperty _layoutXProperty = node.layoutXProperty();
            _layoutXProperty.unbind();
            DoubleProperty _layoutYProperty = node.layoutYProperty();
            _layoutYProperty.unbind();
          }
        };
      delegate.setOnFinished(_function_1);
      _xblockexpression = (delegate);
    }
    return _xblockexpression;
  }
}
