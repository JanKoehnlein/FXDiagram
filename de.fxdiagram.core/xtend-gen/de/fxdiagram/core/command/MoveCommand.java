package de.fxdiagram.core.command;

import de.fxdiagram.core.XNode;
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
import javafx.scene.Node;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Duration;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class MoveCommand extends AbstractAnimationCommand {
  private Node node;
  
  private double fromX;
  
  private double fromY;
  
  private double toX;
  
  private double toY;
  
  public MoveCommand(final Node shape, final double toX, final double toY) {
    this.node = shape;
    double _layoutX = shape.getLayoutX();
    this.fromX = _layoutX;
    double _layoutY = shape.getLayoutY();
    this.fromY = _layoutY;
    this.toX = toX;
    this.toY = toY;
  }
  
  public MoveCommand(final XShape shape, final double fromX, final double fromY, final double toX, final double toY) {
    this.node = shape;
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;
  }
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    PathTransition _xblockexpression = null;
    {
      if ((this.node instanceof XNode)) {
        double _fromX = this.fromX;
        Group _placementGroup = ((XNode)this.node).getPlacementGroup();
        double _layoutX = _placementGroup.getLayoutX();
        this.fromX = (_fromX - _layoutX);
        double _fromY = this.fromY;
        Group _placementGroup_1 = ((XNode)this.node).getPlacementGroup();
        double _layoutY = _placementGroup_1.getLayoutY();
        this.fromY = (_fromY - _layoutY);
        Group _placementGroup_2 = ((XNode)this.node).getPlacementGroup();
        final Procedure1<Group> _function = (Group it) -> {
          it.setLayoutX(0);
          it.setLayoutY(0);
        };
        ObjectExtensions.<Group>operator_doubleArrow(_placementGroup_2, _function);
        ((XNode)this.node).setPlacementHint(null);
      }
      Duration _executeDuration = this.getExecuteDuration(context);
      _xblockexpression = this.createMoveTransition(this.fromX, this.fromY, this.toX, this.toY, _executeDuration);
    }
    return _xblockexpression;
  }
  
  @Override
  public Animation createUndoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMoveTransition(this.toX, this.toY, this.fromX, this.fromY, _defaultUndoDuration);
  }
  
  @Override
  public Animation createRedoAnimation(final CommandContext context) {
    Duration _defaultUndoDuration = context.getDefaultUndoDuration();
    return this.createMoveTransition(this.fromX, this.fromY, this.toX, this.toY, _defaultUndoDuration);
  }
  
  protected PathTransition createMoveTransition(final double fromX, final double fromY, final double toX, final double toY, final Duration duration) {
    PathTransition _xblockexpression = null;
    {
      boolean _and = false;
      double _layoutX = this.node.getLayoutX();
      boolean _equals = (_layoutX == toX);
      if (!_equals) {
        _and = false;
      } else {
        double _layoutY = this.node.getLayoutY();
        boolean _equals_1 = (_layoutY == toY);
        _and = _equals_1;
      }
      if (_and) {
        return null;
      }
      Group _group = new Group();
      final Procedure1<Group> _function = (Group it) -> {
        it.setTranslateX(fromX);
        it.setTranslateY(fromY);
      };
      final Group dummyNode = ObjectExtensions.<Group>operator_doubleArrow(_group, _function);
      DoubleProperty _layoutXProperty = this.node.layoutXProperty();
      DoubleProperty _translateXProperty = dummyNode.translateXProperty();
      _layoutXProperty.bind(_translateXProperty);
      DoubleProperty _layoutYProperty = this.node.layoutYProperty();
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
          MoveTo _moveTo = new MoveTo(fromX, fromY);
          _elements.add(_moveTo);
          ObservableList<PathElement> _elements_1 = it_1.getElements();
          LineTo _lineTo = new LineTo(toX, toY);
          _elements_1.add(_lineTo);
        };
        Path _doubleArrow = ObjectExtensions.<Path>operator_doubleArrow(_path, _function_2);
        it.setPath(_doubleArrow);
        final EventHandler<ActionEvent> _function_3 = (ActionEvent it_1) -> {
          final Procedure1<Node> _function_4 = (Node it_2) -> {
            DoubleProperty _layoutXProperty_1 = it_2.layoutXProperty();
            _layoutXProperty_1.unbind();
            DoubleProperty _layoutYProperty_1 = it_2.layoutYProperty();
            _layoutYProperty_1.unbind();
            it_2.setLayoutX(toX);
            it_2.setLayoutY(toY);
          };
          ObjectExtensions.<Node>operator_doubleArrow(
            this.node, _function_4);
        };
        it.setOnFinished(_function_3);
      };
      _xblockexpression = ObjectExtensions.<PathTransition>operator_doubleArrow(_pathTransition, _function_1);
    }
    return _xblockexpression;
  }
}
