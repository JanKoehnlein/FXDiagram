package de.fxdiagram.core.layout;

import de.fxdiagram.core.XShape;
import javafx.animation.PathTransition;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class LayoutTransitionFactory {
  public PathTransition createTransition(final XShape shape, final double endX, final double endY, final boolean curve, final Duration duration) {
    PathTransition _xblockexpression = null;
    {
      Group _group = new Group();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          double _layoutX = shape.getLayoutX();
          it.setTranslateX(_layoutX);
          double _layoutY = shape.getLayoutY();
          it.setTranslateY(_layoutY);
        }
      };
      final Group dummyNode = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      PathTransition _pathTransition = new PathTransition();
      final Procedure1<PathTransition> _function_1 = new Procedure1<PathTransition>() {
        public void apply(final PathTransition it) {
          it.setNode(dummyNode);
          it.setDuration(duration);
          it.setCycleCount(1);
          Path _path = new Path();
          final Procedure1<Path> _function = new Procedure1<Path>() {
            public void apply(final Path it) {
              ObservableList<PathElement> _elements = it.getElements();
              double _layoutX = shape.getLayoutX();
              double _layoutY = shape.getLayoutY();
              MoveTo _moveTo = new MoveTo(_layoutX, _layoutY);
              _elements.add(_moveTo);
              if (curve) {
                double controlX = 0;
                double controlY = 0;
                double _random = Math.random();
                boolean _greaterThan = (_random > 0.5);
                if (_greaterThan) {
                  double _layoutX_1 = shape.getLayoutX();
                  controlX = _layoutX_1;
                  controlY = endY;
                } else {
                  controlX = endX;
                  double _layoutY_1 = shape.getLayoutY();
                  controlY = _layoutY_1;
                }
                ObservableList<PathElement> _elements_1 = it.getElements();
                QuadCurveTo _quadCurveTo = new QuadCurveTo(controlX, controlY, endX, endY);
                _elements_1.add(_quadCurveTo);
              } else {
                ObservableList<PathElement> _elements_2 = it.getElements();
                LineTo _lineTo = new LineTo(endX, endY);
                _elements_2.add(_lineTo);
              }
            }
          };
          Path _doubleArrow = ObjectExtensions.<Path>operator_doubleArrow(_path, _function);
          it.setPath(_doubleArrow);
        }
      };
      final PathTransition delegate = ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function_1);
      DoubleProperty _layoutXProperty = shape.layoutXProperty();
      DoubleProperty _translateXProperty = dummyNode.translateXProperty();
      _layoutXProperty.bind(_translateXProperty);
      DoubleProperty _layoutYProperty = shape.layoutYProperty();
      DoubleProperty _translateYProperty = dummyNode.translateYProperty();
      _layoutYProperty.bind(_translateYProperty);
      final EventHandler<ActionEvent> _function_2 = new EventHandler<ActionEvent>() {
        public void handle(final ActionEvent it) {
          DoubleProperty _layoutXProperty = shape.layoutXProperty();
          _layoutXProperty.unbind();
          DoubleProperty _layoutYProperty = shape.layoutYProperty();
          _layoutYProperty.unbind();
        }
      };
      delegate.setOnFinished(_function_2);
      _xblockexpression = (delegate);
    }
    return _xblockexpression;
  }
}
