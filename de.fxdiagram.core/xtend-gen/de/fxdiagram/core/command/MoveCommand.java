package de.fxdiagram.core.command;

import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.Command;
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
public class MoveCommand implements Command {
  private XShape shape;
  
  private double targetX;
  
  private double targetY;
  
  public MoveCommand(final XShape shape, final double fromX, final double fromY) {
    this.shape = shape;
    this.targetX = fromX;
    this.targetY = fromY;
  }
  
  public Animation execute(final Duration duration) {
    return null;
  }
  
  public boolean canUndo() {
    return true;
  }
  
  public Animation undo(final Duration duration) {
    return this.createMoveTransition(duration);
  }
  
  protected PathTransition createMoveTransition(final Duration duration) {
    PathTransition _xblockexpression = null;
    {
      final double fromX = this.shape.getLayoutX();
      final double fromY = this.shape.getLayoutY();
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
              LineTo _lineTo = new LineTo(MoveCommand.this.targetX, MoveCommand.this.targetY);
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
                  it.setLayoutX(MoveCommand.this.targetX);
                  MoveCommand.this.targetX = fromX;
                  it.setLayoutY(MoveCommand.this.targetY);
                  MoveCommand.this.targetY = fromY;
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
  
  public boolean canRedo() {
    return true;
  }
  
  public Animation redo(final Duration duration) {
    return this.createMoveTransition(duration);
  }
}
