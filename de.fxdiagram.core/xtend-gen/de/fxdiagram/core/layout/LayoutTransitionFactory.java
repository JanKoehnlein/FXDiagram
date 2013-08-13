package de.fxdiagram.core.layout;

import com.google.common.base.Objects;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.layout.LayoutTransitionStyle;
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
  public PathTransition createTransition(final XShape shape, final double endX, final double endY, final LayoutTransitionStyle style, final Duration duration) {
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
              boolean _matched = false;
              if (!_matched) {
                if (Objects.equal(style,LayoutTransitionStyle.STRAIGHT)) {
                  _matched=true;
                  ObservableList<PathElement> _elements_1 = it.getElements();
                  LineTo _lineTo = new LineTo(endX, endY);
                  _elements_1.add(_lineTo);
                }
              }
              if (!_matched) {
                if (Objects.equal(style,LayoutTransitionStyle.CURVE_XFIRST)) {
                  _matched=true;
                  ObservableList<PathElement> _elements_2 = it.getElements();
                  double _layoutY_1 = shape.getLayoutY();
                  QuadCurveTo _quadCurveTo = new QuadCurveTo(endX, _layoutY_1, endX, endY);
                  _elements_2.add(_quadCurveTo);
                }
              }
              if (!_matched) {
                if (Objects.equal(style,LayoutTransitionStyle.CURVE_YFIRST)) {
                  _matched=true;
                  ObservableList<PathElement> _elements_3 = it.getElements();
                  double _layoutX_1 = shape.getLayoutX();
                  QuadCurveTo _quadCurveTo_1 = new QuadCurveTo(_layoutX_1, endY, endX, endY);
                  _elements_3.add(_quadCurveTo_1);
                }
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
