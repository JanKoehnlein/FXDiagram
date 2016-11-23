package de.fxdiagram.core.command;

import de.fxdiagram.core.XNode;
import de.fxdiagram.core.XShape;
import de.fxdiagram.core.command.AbstractAnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import de.fxdiagram.core.extensions.TransitionExtensions;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
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
  
  @Override
  public Animation createExecuteAnimation(final CommandContext context) {
    Transition _xblockexpression = null;
    {
      if ((this.shape instanceof XNode)) {
        double _fromX = this.fromX;
        Group _placementGroup = ((XNode)this.shape).getPlacementGroup();
        double _layoutX = _placementGroup.getLayoutX();
        this.fromX = (_fromX - _layoutX);
        double _fromY = this.fromY;
        Group _placementGroup_1 = ((XNode)this.shape).getPlacementGroup();
        double _layoutY = _placementGroup_1.getLayoutY();
        this.fromY = (_fromY - _layoutY);
        Group _placementGroup_2 = ((XNode)this.shape).getPlacementGroup();
        final Procedure1<Group> _function = (Group it) -> {
          it.setLayoutX(0);
          it.setLayoutY(0);
        };
        ObjectExtensions.<Group>operator_doubleArrow(_placementGroup_2, _function);
        ((XNode)this.shape).setPlacementHint(null);
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
  
  protected Transition createMoveTransition(final double fromX, final double fromY, final double toX, final double toY, final Duration duration) {
    Transition _xblockexpression = null;
    {
      if (((this.shape.getLayoutX() == toX) && (this.shape.getLayoutY() == toY))) {
        return null;
      }
      Point2D _point2D = new Point2D(fromX, fromY);
      Point2D _point2D_1 = new Point2D(toX, toY);
      _xblockexpression = TransitionExtensions.createMoveTransition(this.shape, _point2D, _point2D_1, duration);
    }
    return _xblockexpression;
  }
}
