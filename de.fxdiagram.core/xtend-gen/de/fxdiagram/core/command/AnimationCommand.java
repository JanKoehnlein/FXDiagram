package de.fxdiagram.core.command;

import de.fxdiagram.core.command.CommandContext;
import javafx.animation.Animation;

@SuppressWarnings("all")
public interface AnimationCommand {
  public abstract Animation getExecuteAnimation(final CommandContext context);
  
  public abstract Animation getUndoAnimation(final CommandContext context);
  
  public abstract Animation getRedoAnimation(final CommandContext context);
  
  public abstract boolean clearRedoStackOnExecute();
  
  public abstract void skipViewportRestore();
}
