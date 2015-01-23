package de.fxdiagram.core.command;

import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import javafx.animation.Animation;
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
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MoveCommand extends AbstractAnimationCommand {
  private XShape shape;
  
  private double fromX;
  
  private double fromY;
  
  private double toX;
  
  private double toY;
  
  public MoveCommand(final XShape shape, final double toX, final double toY) {
    this.shape = shape;
    double _layoutX = shape.getLayoutX();
    this.fromX = _layoutX;
    double _layoutY = shape.getLayoutY();
    this.fromY = _layoutY;
    this.toX = toX;
    this.toY = toY;
  }
  
  public MoveCommand(final XShape shape, final double fromX, final double fromY, final double toX, final double toY) {
    this.shape = shape;
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;
  }
  
  public Animation createExecuteAnimation(final CommandContext context) {
    Duration _executeDuration = this.getExecuteDuration(context);
    return this.createMoveTransition(this.fromX, this.fromY, this.toX, this.toY, _executeDuration);
  }
  
  public Animation createUndoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMoveTransition(this.toX, this.toY, this.fromX, this.fromY, _defaultUndoDuration);
  }
  
  public Animation createRedoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMoveTransition(this.fromX, this.fromY, this.toX, this.toY, _defaultUndoDuration);
  }
  
  protected PathTransition createMoveTransition(final double fromX, final double fromY, final double toX, final double toY, final Duration duration) {
    PathTransition _xblockexpression = null;
    {
      boolean _and = false;
      double _layoutX = this.shape.getLayoutX();
      boolean _equals = (_layoutX == toX);
      if (!_equals) {
        _and = false;
      } else {
        double _layoutY = this.shape.getLayoutY();
        boolean _equals_1 = (_layoutY == toY);
        _and = _equals_1;
      }
      if (_and) {
        return null;
      }
      Group _group = new Group();
      final Procedure1<Group> _function = new Procedure1<Group>() {
        public void apply(final Group it) {
          it.setTranslateX(fromX);
          it.setTranslateY(fromY);
        }
      };
      final Group dummyNode = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      DoubleProperty _layoutXProperty = this.shape.layoutXProperty();
      DoubleProperty _translateXProperty = dummyNode.translateXProperty();
      _layoutXProperty.bind(_translateXProperty);
      DoubleProperty _layoutYProperty = this.shape.layoutYProperty();
      DoubleProperty _translateYProperty = dummyNode.translateYProperty();
      _layoutYProperty.bind(_translateYProperty);
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
              MoveTo _moveTo = new MoveTo(fromX, fromY);
              _elements.add(_moveTo);
              ObservableList<PathElement> _elements_1 = it.getElements();
              LineTo _lineTo = new LineTo(toX, toY);
              _elements_1.add(_lineTo);
            }
          };
          Path _doubleArrow = ObjectExtensions.<Path>operator_doubleArrow(_path, _function);
          it.setPath(_doubleArrow);
          final EventHandler<ActionEvent> _function_1 = new EventHandler<ActionEvent>() {
            public void handle(final ActionEvent it) {
              final Procedure1<XShape> _function = new Procedure1<XShape>() {
                public void apply(final XShape it) {
                  DoubleProperty _layoutXProperty = it.layoutXProperty();
                  _layoutXProperty.unbind();
                  DoubleProperty _layoutYProperty = it.layoutYProperty();
                  _layoutYProperty.unbind();
                  it.setLayoutX(toX);
                  it.setLayoutY(toY);
                }
              };
              ObjectExtensions.<XShape>operator_doubleArrow(
                MoveCommand.this.shape, _function);
            }
          };
          it.setOnFinished(_function_1);
        }
      };
      _xblockexpression = ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function_1);
    }
    return _xblockexpression;
  }
}
