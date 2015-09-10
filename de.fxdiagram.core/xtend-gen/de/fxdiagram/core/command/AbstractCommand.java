package de.fxdiagram.core.command;

import de.fxdiagram.core.command.AnimationCommand;
import de.fxdiagram.core.command.CommandContext;
import javafx.animation.Animation;

/**
 * A command without animations
 */
@SuppressWarnings("all")
public abstract class AbstractCommand implements AnimationCommand {
  @Override
  public Animation getExecuteAnimation(final CommandContext context) {
    this.execute(context);
    return null;
  }
  
  public abstract void execute(final CommandContext context);
  
  @Override
  public Animation getUndoAnimation(final CommandContext context) {
    Object _xblockexpression = null;
    {
      this.undo(context);
      _xblockexpression = null;
    }
    return ((Animation)_xblockexpression);
  }
  
  public abstract void undo(final CommandContext context);
  
  @Override
  public Animation getRedoAnimation(final CommandContext context) {
    Object _xblockexpression = null;
    {
      this.redo(context);
      _xblockexpression = null;
    }
    return ((Animation)_xblockexpression);
  }
  
  public abstract void redo(final CommandContext context);
  
  @Override
  public boolean clearRedoStackOnExecute() {
    return false;
  }
  
  @Override
  public void skipViewportRestore() {
  }
}
